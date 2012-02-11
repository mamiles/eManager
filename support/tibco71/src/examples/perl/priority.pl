#!/usr/bin/perl

#
# @(#)priority.pl	1.2
#

use Tibrv;
use Getopt::Long;

$subject1="1";
$subject2="2";

sub msgCallback
{
    my ($event, $msg, $closure) = @_;
    my ($str,$send_subj,$reply_subj);

    Tibrv::tibrvMsg_GetSendSubject($msg,$send_subj);

    Tibrv::tibrvMsg_ConvertToString($msg, $str);


    printf "Received message on subject %s: %s\n", $send_subj, $str;
}





#
# creates the internal TIB/Rendezvous machinery.
#

$status = Tibrv::tibrv_Open();
die "Tibrv::tibrv_Open failed." if $status != Tibrv::TIBRV_OK;

#
# create two queues
#
Tibrv::tibrvQueue_Create($queue1);
Tibrv::tibrvQueue_Create($queue2);

#
# set the priority
#
Tibrv::tibrvQueue_SetPriority($queue1,1);
Tibrv::tibrvQueue_SetPriority($queue2,2);

#
# Create queue group and add queues
#
Tibrv::tibrvQueueGroup_Create($group);
Tibrv::tibrvQueueGroup_Add($group,$queue1);
Tibrv::tibrvQueueGroup_Add($group,$queue2);

#
# now create listeners
#

Tibrv::tibrvEvent_CreateListener($listener1,
                                 $queue1,
                                 "msgCallback",
                                 Tibrv::TIBRV_PROCESS_TRANSPORT, 
                                 $subject1, 0);

Tibrv::tibrvEvent_CreateListener($listener1,
                                 $queue2,
                                 "msgCallback",
                                 Tibrv::TIBRV_PROCESS_TRANSPORT, 
                                 $subject2, 0);



$status = Tibrv::tibrvMsg_Create($msg);

die "Tibrv::tibrvMsg_Create failed." if $status != Tibrv::TIBRV_OK;

#
# send 10 messages on subject 1
#
$status=Tibrv::tibrvMsg_SetSendSubject($msg,$subject1);
for( $i=0 ; $i < 10; $i++ )
{
    $index=$i+1;
    $valstr="value-1-" . $index;
    Tibrv::tibrvMsg_UpdateStringEx($msg,"field",$valstr);
    Tibrv::tibrvTransport_Send(Tibrv::TIBRV_PROCESS_TRANSPORT,$msg);
}

#
# send 10 messages on subject 2
#
$status=Tibrv::tibrvMsg_SetSendSubject($msg,$subject2);
for( $i=0 ; $i < 10; $i++ )
{
    $index=$i+1;
    $valstr="value-2-" . $index;
    Tibrv::tibrvMsg_UpdateStringEx($msg,"field",$valstr);
    Tibrv::tibrvTransport_Send(Tibrv::TIBRV_PROCESS_TRANSPORT,$msg);
}

#
# Dispatch the group.  When all events are dispatched, 
# timedDispatch will time out and fall out of the loop.
#

do
{
    $status=Tibrv::tibrvQueueGroup_TimedDispatch($group,1);
}while( $status == Tibrv::TIBRV_OK );

#
# cleanup and leave.
#
Tibrv::tibrvMsg_Destroy($msg);

Tibrv::tibrv_Close();
