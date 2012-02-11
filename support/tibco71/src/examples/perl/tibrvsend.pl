#!/usr/bin/perl

#
# @(#)tibrvsend.pl	1.3
#

use Tibrv;
use Getopt::Long;



sub help_func
{
    print "usage:  tibrvsend [-servcie s] [-network n]\n";
    print "        [-daemon d] subject message\n";

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
help_func if $#ARGV < 1;


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

#
# only send one message
#
$subject = shift;
$message = shift;

$status = Tibrv::tibrvMsg_Create($msg);
die "Tibrv::tibrvMsg_Create failed." if $status != Tibrv::TIBRV_OK;

$status=Tibrv::tibrvMsg_SetSendSubject($msg,$subject);

while( $message ne "" )
{
    print "Publishing:  subject=$subject  \"$message\"\n";

    $status=Tibrv::tibrvMsg_UpdateStringEx($msg,"DATA", $message);
    die "UpdateString failed" if $status != Tibrv::TIBRV_OK;

    $status=Tibrv::tibrvTransport_Send($transport,$msg);
    die "tibrvTransport_Send failed" if $status != Tibrv::TIBRV_OK;

    $message=shift;
}

Tibrv::tibrvMsg_Destroy($msg);

Tibrv::tibrv_Close();
