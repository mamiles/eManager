#!/usr/bin/perl

#
# @(#)tibrvlisten.pl	1.4
#

use Tibrv;
use Getopt::Long;


sub my_callback
{
    my ($event, $msg, $closure) = @_;
    my ($str,$send_subj,$reply_subj);

    Tibrv::tibrvMsg_GetSendSubject($msg,$send_subj);
    Tibrv::tibrvMsg_GetReplySubject($msg,$reply_subj);

    Tibrv::tibrvMsg_ConvertToString($msg, $str);

    if ($reply_subj ne "" )
    {
        print "subject=$send_subj, reply=$reply_subj, message=$str\n";
    }
    else
    {
        print "subject=$send_subj,  message=$str\n";
    }
}



sub help_func
{
    print "ARGV = $#ARGV\n";
    print "ret = $ret\n";
    print "usage:  tibrvlisten [-servcie s] [-network n]\n";
    print "        [-daemon d] subject\n";

    exit;
}

#
# initialize some defaults
#
$service="";
$network="";
$daemon="";
$subject="";


$ret=&GetOptions("service:s" ,\$service, "network:s", \$network,
                        "daemon:s", \$daemon, "help",\&help_func);

help_func if $ret == 0;

#
# creates the internal TIB/Rendezvous machinery.
#

$status = Tibrv::tibrv_Open();

die "Tibrv::tibrv_Open failed." if $status != Tibrv::TIBRV_OK;

$status = Tibrv::tibrvTransport_Create($transport,
                                       $service,$network,$daemon);

die "Tibrv::tibrv_TransportCreate failed" if $status != Tibrv::TIBRV_OK;

#
# when you check the connected clients via rvd this is how you know what
# is connected
#
Tibrv::tibrvTransport_SetDescription($transport,$0);

$subject = shift;

while ($subject ne "" )
{
  $status = Tibrv::tibrvEvent_CreateListener($lid,Tibrv::TIBRV_DEFAULT_QUEUE,
                                   "my_callback",$transport, $subject, 0);

  die "Tibrv::tibrvEvent_CreateListener failed." if $status != Tibrv::TIBRV_OK;

  $subject = shift;
}

do
{
    $status=Tibrv::tibrvQueue_TimedDispatch(Tibrv::TIBRV_DEFAULT_QUEUE,
                                            Tibrv::TIBRV_WAIT_FOREVER);
}
while( $status == Tibrv::TIBRV_OK );


#
# if for some reason we exit out of tibrvQueue_TimedDispatch, cleanup
#

tibrv_Close();
