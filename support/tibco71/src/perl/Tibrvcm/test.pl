#
# Copyright 1999-2000 TIBCO Software Inc.
# ALL RIGHTS RESERVED.
# TIB/Rendezvous is protected under US Patent No. 5,187,787.
# For more information, please contact:
# TIBCO Software Inc., Palo Alto, California, USA
#
# @(#)test.pl	1.7
#

# Before `make install' is performed this script should be runnable with
# `make test'. After `make install' it should work as `perl test.pl'

######################### We start with some black magic to print on failure.

# Change 1..1 below to 1..last_test_to_print .
# (It may become useful if the test is moved to ./t subdirectory.)

BEGIN { $| = 1; print "1..5\n"; }
END {print "not ok 1\n" unless $loaded;}

use Tibrv;
use Tibrvcm;

$loaded = 1;
print "ok 1\n";

$test_done=0;
$cm_pass=0;
$timer_count=0;
$sub_ok=0;

######################### End of black magic.

# Insert your test code below (better if it prints "ok 13"
# (correspondingly "not ok 13") depending on the success of chunk 13
# of the test code):


sub cm_review_cb
{
    my($tport,$subj,$msg,$closure)=@_;
    my($string);
    
    $status=Tibrv::tibrvMsg_ConvertToString($msg,$string);
    print "msg=$string\n";
    
    print "ok 4\n";
    return(1);
}
sub cm_event_cb
{
    my ($event,$msg,$closure)=@_;
    my ($queue,$subj,$tport,$status);

    print "  listener closure is wrong\n" if ($closure ne "cmlistener");

    #
    # get queue information
    #
    $status = 0;

    $status+=Tibrvcm::tibrvcmEvent_GetQueue( $event, $queue );
    print "  not from the event queue" if ($queue != Tibrv::TIBRV_DEFAULT_QUEUE);

    $status+=Tibrvcm::tibrvcmEvent_GetListenerSubject($event, $subj);
    print "  not expected subj. " if ($subj ne "PERL.CM.TEST");


    $status+=Tibrvcm::tibrvcmEvent_GetListenerTransport($event,$tport);
    print "  not expected transport " if ($tport != $cmtport_l);


    #
    # get info from message
    #
    $status+=Tibrvcm::tibrvMsg_GetCMSender($msg,$cm_sndr);
    print "not expected sender" if($cm_sndr ne "PERL_PUB");

    $status+=Tibrvcm::tibrvMsg_GetCMSequence($msg,$cm_seq);
    print "cm init sequence wrong\n" if ($cm_pass==0 && $cm_seq !=0);
    
    if ($cm_pass == 5)
    {
	$status+=Tibrvcm::tibrvcmEvent_Destroy($cmlistener,0);
    }
    $cm_pass++;

    $sub_ok++ if ($status == Tibrv::TIBRV_OK) ;

}
#
# listen event callback
#
sub cm_adv
{
    my ($event, $msg, $closure) = @_;
    my ($status,$etype);
    my ($err);

}

sub timercb
{
    my ($event, $msg, $closure) = @_;
    my ($err);

    $err = 0;


    if ($timer_count == 10)
    {
      Tibrvcm::tibrvcmTransport_ReviewLedger($cmtport,"cm_review_cb",
					     "PERL.CM.TEST","reviewledg");

	$test_done=1;
    }
    else
    {
	$status=Tibrvcm::tibrvcmTransport_Send($cmtport,$cm_msg);    
    }
    $timer_count++;

}

sub init_transport()
{

    $service="7548";
    $network="127.0.0.1";
    $daemon ="";
    $desc="Test Transport";

    $status = Tibrv::tibrvTransport_Create($transport,$service,$network,
					   $daemon);

    if ($status == Tibrv::TIBRV_DAEMON_NOT_FOUND)
    {
	print "no daemon...\n";
    }

    #
    # listen for cm advisories.
    #

    $lstatus=Tibrv::tibrvEvent_CreateListener($advs,Tibrv::TIBRV_DEFAULT_QUEUE,
				              "cm_adv",$transport,
					      "_RV.*.RVCM.>","");

    $lstatus=Tibrv::tibrvEvent_CreateListener($advs,Tibrv::TIBRV_DEFAULT_QUEUE,
				              "cm_adv",$transport,
					      "_RVCM.>","");

    #
    # do some stuff on a timer
    #
    $ltatus=Tibrv::tibrvEvent_CreateTimer( $tevent, 
					    Tibrv::TIBRV_DEFAULT_QUEUE,
					   "timercb", 
					   1.0, "timer" );

    return $status;
}

sub cm_transport()
{

    $status=Tibrvcm::tibrvcmTransport_Create($cmtport,
					     $transport,
					     "PERL_PUB",
					     Tibrv::TIBRV_TRUE,
					     "pub_ledg",
					     Tibrv::TIBRV_FALSE,
					     "");

    if( $status != Tibrv::TIBRV_OK )
    {
	print "status exit from $status Transport_Create\n";
	return  $status;
    }

    $status=Tibrvcm::tibrvcmTransport_SetDefaultCMTimeLimit($cmtport,
							    60);

    if ( Tibrv::tibrvMsg_Create($cm_msg) == Tibrv::TIBRV_OK)
    {
      Tibrv::tibrvMsg_SetSendSubject($cm_msg,"PERL.CM.TEST");
      Tibrv::tibrvMsg_AddStringEx($cm_msg,"message","cm test message",0);
    }

    $status=Tibrvcm::tibrvcmTransport_AddListener($cmtport,
						  "PERL_SUB",
						  "PERL.CM.TEST");

    #
    # if we created a listener, create one!
    #
    if ($status == Tibrv::TIBRV_OK)
    {
	$status=Tibrvcm::tibrvcmTransport_Create($cmtport_l,
						 $transport,
						 "PERL_SUB",
					         Tibrv::TIBRV_TRUE,
					         "sub_ledg",
					         Tibrv::TIBRV_FALSE,
					         "");
	if ($status==Tibrv::TIBRV_OK)
	{
	  $status=Tibrvcm::tibrvcmEvent_CreateListener($cmlistener,
					       Tibrv::TIBRV_DEFAULT_QUEUE,
					       "cm_event_cb",
					       $cmtport_l,
					       "PERL.CM.TEST",
					       "cmlistener");
	}
    }

    $status=Tibrvcm::tibrvcmTransport_Send($cmtport,$cm_msg);

    return $status;
}
#
# start more testing
#

$status=Tibrv::tibrv_Open();
if ($status != Tibrv::TIBRV_OK) {print "couldn't open library\n";exit;}

$cmver=Tibrvcm::tibrvcm_Version();
if ($cmver =~ /6.*/){ print "ok 2\n";}else{print "not ok 2\n";}

if (Tibrv::TIBRV_OK == init_transport() )
{

    if ( cm_transport() != Tibrv::TIBRV_OK)
    {
	print "not ok 3\n";
	exit;
    }
    else
    {
	print "ok 3\n";
    }
    #
    # wait for events to happen
    #
    do
    {
	$status=Tibrv::tibrvQueue_Dispatch(Tibrv::TIBRV_DEFAULT_QUEUE);

    } while ($status == Tibrv::TIBRV_OK && $test_done == 0);


    Tibrv::tibrv_Close();
    if ( $sub_ok)
    {
	print "ok 5\n";
    }
    else
    {
	print "not ok 5\n";
    }
}
