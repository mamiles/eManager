#
# Copyright 1999-2000 TIBCO Software Inc.
# ALL RIGHTS RESERVED.
# TIB/Rendezvous is protected under US Patent No. 5,187,787.
# For more information, please contact:
# TIBCO Software Inc., Palo Alto, California, USA
#
# @(#)test.pl	1.14
#

# Before `make install' is performed this script should be runnable with
# `make test'. After `make install' it should work as `perl test.pl'

######################### We start with some black magic to print on failure.

# Change 1..1 below to 1..last_test_to_print .
# (It may become useful if the test is moved to ./t subdirectory.)

BEGIN { $| = 1; print "1..9\n"; }
END {print "not ok 1\n" unless $loaded;}
use Tibrv;
$loaded = 1;
print "ok 1\n";

$timer_count=0;

######################### End of black magic.

# Insert your test code below (better if it prints "ok 13"
# (correspondingly "not ok 13") depending on the success of chunk 13
# of the test code):


#
# this is the callback function for the timer event
#
sub timercb
{
    my ($event, $msg, $closure) = @_;
    my ($status,$queue,$interval,$etype);
    my ($err);

    $err = 0;
    $timer_count++;

    print "  timer closure is wrong\n" if ($closure ne "timer");

    $status=Tibrv::tibrvEvent_GetType( $event, $etype );
    print "  expected a timer event\n" if ($etype != Tibrv::TIBRV_TIMER_EVENT) ;

    #
    # get queue information
    #
    $status=Tibrv::tibrvEvent_GetQueue( $event, $queue );
    print "  not from the timer queue\n" if ($queue != $timer_queue);
    

    #
    # dink with the timer stuff
    #
    $status=Tibrv::tibrvEvent_GetTimerInterval( $event, $interval );

    if ($interval == 1 && $timer_count==1)
    {
	$status=Tibrv::tibrvEvent_ResetTimerInterval($event, 5);
	if ($status != Tibrv::TIBRV_OK)
        {
	    print "Tibrv::tibrvEvent_ResetTimerInterval failed $status\n";
	}
    }
    #
    # send a test message using the message we created earlier.
    #

    $status=Tibrv::tibrvMsg_SetSendSubject($nmsg,"PERL.TIBRV.TEST");
    $status+=Tibrv::tibrvMsg_SetReplySubject($nmsg,$my_inbox);
    if ($status == Tibrv::TIBRV_OK)
    {
	#
	# all things are ok to send.
	#
	$status=Tibrv::tibrvTransport_Send($transport,$nmsg);
	print ".";
	if ($status != Tibrv::TIBRV_OK)
	{
	    print "  Tibrv::tibrvTransport_Send failed\n";
        }
    }

    if ($timer_count == 5 )
    {
        $status=Tibrv::tibrvEvent_Destroy( $event );
    }
}

#
# listen event callback
#
sub listencb
{
    my ($event, $msg, $closure) = @_;
    my ($status,$etype);
    my ($err);

    $err = 0;

    print "  listener closure is wrong" if ($closure ne "listener");

    $status=Tibrv::tibrvEvent_GetType( $event, $etype );
    print "  expected a listen event ($etype)\n" if ($etype != Tibrv::TIBRV_LISTEN_EVENT) ;

    #
    # get queue information
    #
    $status=Tibrv::tibrvEvent_GetQueue( $event, $queue );
    print "  not from the event queue" if ($queue != $event_queue);
    
    $status=Tibrv::tibrvEvent_GetListenerSubject($event, $subj);
    print "  not expected subj. " if ($subj ne "PERL.TIBRV.TEST");

    $status=Tibrv::tibrvEvent_GetListenerTransport($event,$tport);
    print "  not expected transport " if ($tport != $transport);

    
    if ($timer_count == 5 )
    {
         $status=Tibrv::tibrvQueueGroup_Destroy($qgrp);
	 print "\n";
    }
}

#
# basic message functionality
#
sub message_test()
{
    $status=0;
    
    my %field;

    $status=Tibrv::tibrvMsg_Create($tmsg);
    #
    # bail immediately if we cant create a message
    #
    return 1 if ($status != Tibrv::TIBRV_OK);
    
    #
    # populate message
    # unlike C the messages are all handled via the Ex functions
    #

    $status+=Tibrv::tibrvMsg_GetCurrentTime($dt);
    $my_time=time;


    $status+=Tibrv::tibrvMsg_AddBoolEx($tmsg, "bool", 1,0);
    $status+=Tibrv::tibrvMsg_AddI8Ex($tmsg,"i8",-2,0);     
    $status+=Tibrv::tibrvMsg_AddI16Ex($tmsg,"i16",-3,0);  
    $status+=Tibrv::tibrvMsg_AddI32Ex($tmsg,"i32",-4,0);  
    $status+=Tibrv::tibrvMsg_AddDateTimeEx($tmsg,"dt",$dt,0);

    #
    # note, this may not give a true 64 bit int if perl isn't compiled
    # to deal with 64 bit integers
    #
    $status+=Tibrv::tibrvMsg_AddI64Ex($tmsg,"i64",-5,0);

    $status+=Tibrv::tibrvMsg_AddF32Ex($tmsg,"f32",-6.0,0);
    $status+=Tibrv::tibrvMsg_AddF64Ex($tmsg,"f64",-7.0,0);

    return 1 if ($status != Tibrv::TIBRV_OK);

    $my_string="This is a string";
    $tlen=length($my_string);

    $status+=Tibrv::tibrvMsg_AddStringEx($tmsg,"string",$my_string,0);
    $status+=Tibrv::tibrvMsg_AddOpaqueEx($tmsg,"opaque",$my_string,$tlen,0);

    return 1 if ($status != Tibrv::TIBRV_OK);

    $status+=Tibrv::tibrvMsg_AddU8Ex($tmsg,"u8",10,0);
    $status+=Tibrv::tibrvMsg_AddU16Ex($tmsg,"u16",11,0);
    $status+=Tibrv::tibrvMsg_AddU32Ex($tmsg,"u32",12,0);

    #
    # note, this may not give a true 64 bit int if perl isn't compiled
    # to deal with 64 bit integers
    #
    $status+=Tibrv::tibrvMsg_AddU64Ex($tmsg,"u64",13,0);

    return 1 if ($status != 0);

    $status+=Tibrv::tibrvMsg_GetNumFields($tmsg,$tfields);
    
    return 1 if ($tfields != 14);

    #
    # see if we got what we put in.
    #
    $status+=Tibrv::tibrvMsg_GetBoolEx($tmsg, "bool", $bl,0);
    $status++ if (!$bl);
    $status+=Tibrv::tibrvMsg_GetI8Ex($tmsg,"i8",$i8,0);     
    $status++ if ($i8 != -2 );
    $status+=Tibrv::tibrvMsg_GetI16Ex($tmsg,"i16",$i16,0);  
    $status++ if ($i16 != -3 );
    $status+=Tibrv::tibrvMsg_GetI32Ex($tmsg,"i32",$i32,0);  
    $status++ if ($i32 != -4 );
    return 1 if ($status != Tibrv::TIBRV_OK);
    
    $status+=Tibrv::tibrvMsg_GetDateTimeEx($tmsg,"dt",$idt,0);
    ($nsec,$msec)=unpack "lL", $idt;
    $status++ if ($nsec != $my_time);
    #
    # note, this may not give a true 64 bit int if perl isn't compiled
    # to deal with 64 bit integers
    #
    $status+=Tibrv::tibrvMsg_GetI64Ex($tmsg,"i64",$i64,0);
    $status++ if ($i64 != -5 );
    return 1 if ($status != Tibrv::TIBRV_OK);

    $status+=Tibrv::tibrvMsg_GetF32Ex($tmsg,"f32",$f32,0);
    $status++ if ($f32 != -6.0 );
    $status+=Tibrv::tibrvMsg_GetF64Ex($tmsg,"f64",$f64,0);
    $status++ if ($f64 != -7.0 );

    return 1 if ($status != Tibrv::TIBRV_OK);

    $status+=Tibrv::tibrvMsg_GetStringEx($tmsg,"string",$string,0);
    return 1 if ($status != Tibrv::TIBRV_OK || $string !~ $my_string);
    $status+=Tibrv::tibrvMsg_GetOpaqueEx($tmsg,"opaque",$opaque,$tlen,0);
    return 1 if ($status != Tibrv::TIBRV_OK || $opaque !~ $my_string);

    $status+=Tibrv::tibrvMsg_GetU8Ex($tmsg,"u8",$u8,0);
    $status++ if ($u8 != 10);
    $status+=Tibrv::tibrvMsg_GetU16Ex($tmsg,"u16",$u16,0);
    $status++ if ($u16 != 11);
    $status+=Tibrv::tibrvMsg_GetU32Ex($tmsg,"u32",$u32,0);
    $status++ if ($u32 != 12);
    $status+=Tibrv::tibrvMsg_GetU64Ex($tmsg,"u64",$u64,0);
    $status++ if ($u64 != 13);

    $status++ if ($string !~ $my_string);

    return 1 if ($status != Tibrv::TIBRV_OK);

    $status+=Tibrv::tibrvMsg_CreateCopy($tmsg,$nmsg);
    return 1 if ($status != Tibrv::TIBRV_OK);


    $status+=Tibrv::tibrvMsg_GetNumFields($nmsg,$nfields);

    return 1 if ($status != Tibrv::TIBRV_OK || $nfields != $tfields);

    $status+=Tibrv::tibrvMsg_GetByteSize($tmsg,$tmsg_size);
    $status+=Tibrv::tibrvMsg_GetByteSize($nmsg,$nmsg_size);

    $status++ if ($nmsg_size != $tmsg_size);

    $status+=Tibrv::tibrvMsg_ConvertToString($tmsg,$tstr);
    $status+=Tibrv::tibrvMsg_ConvertToString($nmsg,$nstr);

    $status++ if ($status != Tibrv::TIBRV_OK || $tstr ne $nstr);
    
    # pause this moment

    $status+=Tibrv::tibrvMsg_GetAsBytes( $tmsg, $mptr );
    $status+=Tibrv::tibrvMsg_CreateFromBytes( $bmsg, $mptr );
    $status+=Tibrv::tibrvMsg_GetNumFields($bmsg, $bfields );
    $status++ if( $bfields != $tfields );


    $status+=Tibrv::tibrvMsg_RemoveFieldEx($tmsg, "bool", 0);

    $dstatus=Tibrv::tibrvMsg_GetBoolEx($tmsg,"bool",$bval,0);
    $status++ if ($dstatus == Tibrv::TIBRV_OK);  # we expect this to fail...

    $status+=Tibrv::tibrvMsg_UpdateI8Ex($tmsg,"i8",100,0);
    $status+=Tibrv::tibrvMsg_GetI8Ex($tmsg,"i8",$i8,0);

    return 1 if ($status != Tibrv::TIBRV_OK || $i8 != 100 );


    $status+=Tibrv::tibrvMsg_Reset($tmsg);
    $status+=Tibrv::tibrvMsg_GetNumFields($tmsg,$tfields);
    $status++ if ($tfields != 0);

    $status+=Tibrv::tibrvMsg_Destroy($tmsg);

    $str=Tibrv::tibrvStatus_GetText($status);

    return 1 if ($str ne "Success");

    $status = Tibrv::tibrvMsg_GetFieldEx( $nmsg, "i8", \%field );
    if( $status == Tibrv::TIBRV_OK )
    {
	if ($field{type} == Tibrv::TIBRVMSG_I8)
	{
	    return 1 if( $field{data} != -2 );
	}
	else
	{
	    return 1;
	}

    }
    else
    {
	return 1;
    }


    $status = Tibrv::tibrvMsg_GetFieldByIndex( $nmsg, \%field, 1 );
    if( $status == Tibrv::TIBRV_OK )
    {
	if ($field{type} == Tibrv::TIBRVMSG_I8)
	{
	    return 1 if( $field{data} != -2 );
	}
	else
	{
	    return 1;
	}

    }
    else
    {
	return 1;
    }

    return 0;

}

sub transport_test()
{
    $service="7548";
    $network="127.0.0.1";
    $daemon ="";
    $desc="Test Transport";

    $status = Tibrv::tibrvTransport_Create($transport,$service,$network,
					   $daemon);

    if ($status == Tibrv::TIBRV_DAEMON_NOT_FOUND){print "no daemon...\n";}

    return 1 if ($status != Tibrv::TIBRV_OK);

    $status+=Tibrv::tibrvTransport_CreateInbox($transport, $my_inbox, 
					       Tibrv::TIBRV_SUBJECT_MAX);

    return 1 if ($status != Tibrv::TIBRV_OK);

    #
    # just a few more tests
    #

    $status+=Tibrv::tibrvTransport_SetDescription($transport,$desc);

    $status+=Tibrv::tibrvTransport_GetService($transport, $tsvc);
    $status+=Tibrv::tibrvTransport_GetNetwork($transport, $tnet);
    $status+=Tibrv::tibrvTransport_GetDaemon($transport, $tdaemon);
    $status+=Tibrv::tibrvTransport_GetDescription($transport, $tdesc);

    return 1 if ($status != Tibrv::TIBRV_OK);

    $status++ if ($tsvc ne $service);
    $status++ if ($tnet ne $network);
    $status++ if ($tdaemon ne "tcp:7500");
    $status++ if ($tdesc ne $desc);
    return $status;
}
#
# check basic queue functionality
#
sub queue_test()
{
    $status=0;

    $status+=Tibrv::tibrvQueue_Create( $timer_queue );
    $status+=Tibrv::tibrvQueue_SetName( $timer_queue, "Timer");
    $status+=Tibrv::tibrvQueue_SetPriority( $timer_queue, 2 );

    return 1 if ($status != Tibrv::TIBRV_OK) ;

    $status+=Tibrv::tibrvQueue_Create( $event_queue );
    $status+=Tibrv::tibrvQueue_SetName( $event_queue, "Event");

    return 1 if ($status != Tibrv::TIBRV_OK);
    
    $status+=Tibrv::tibrvQueue_GetName($timer_queue,$tqname);
    $status++ if ($tqname ne "Timer");
    $status+=Tibrv::tibrvQueue_GetPriority( $timer_queue, $tpri );
    $status++ if ($tpri != 2);
    $status;
}


sub queue_grp_test()
{
    $status=0;

    $status = Tibrv::tibrvQueueGroup_Create($qgrp);

    return $status if ($status != Tibrv::TIBRV_OK);

    $status+=Tibrv::tibrvQueueGroup_Add($qgrp,$timer_queue);
    $status+=Tibrv::tibrvQueueGroup_Add($qgrp,$event_queue);

    return $status if ($status != Tibrv::TIBRV_OK);

    $status+=Tibrv::tibrvQueueGroup_Remove($qgrp,$event_queue);
    $status+=Tibrv::tibrvQueueGroup_Add($qgrp,$event_queue);

    $status;
}

sub event_setup_test()
{
    #
    # setup a timer
    #
    $status=Tibrv::tibrvEvent_CreateTimer( $tevent, $timer_queue,"timercb", 
					   1.0, "timer" );

    $status+=Tibrv::tibrvEvent_CreateListener($levent,$event_queue,"listencb",
					      $transport,
					      "PERL.TIBRV.TEST","listener");

    $status;
}
#########################################################################
#     start of test
#########################################################################
$status=Tibrv::tibrv_Open();
if ($status != Tibrv::TIBRV_OK) {print "not ok 2";exit;}

print "ok 2\n";
$ver=Tibrv::tibrv_Version();
if ($ver =~ /7.*/){ print "ok 3\n";}else{print "not ok 3\n";}

#
# basic message
#
if ( message_test() == 0 ) {print "ok 4\n";}else{print "not ok 4\n"};


if (transport_test() == 0 ) {print "ok 5\n"}else{print "not ok 5\n"};

if (queue_test() == 0 ){print "ok 6\n"}else{print "not ok 6\n"};

if (queue_grp_test() == 0 ){print "ok 7\n"}else{print "not ok 7\n"};

if (event_setup_test() == 0){print "ok 8\n"}else{print "not ok 8\n"};

#
# process events
#
do
{
    $status=Tibrv::tibrvQueueGroup_Dispatch($qgrp);
}
while($status == Tibrv::TIBRV_OK);

if ($status == Tibrv::TIBRV_INVALID_QUEUE_GROUP) 
{
    print "ok 9\n";
}
else
{
    print "not ok 9\n";
}

