#!/usr/bin/perl

#
# @(#)tibrvftmon.pl	1.2
#

use Tibrv;
use Tibrvft;

$lost_interval=4.8;         # tibrvfttime...

sub mon_callback
{
    my($monitor,$gname,$num_active,$closure)=@_;

    print "Group [$gname]: has $num_active members\n";
}

#
# creates the internal TIB/Rendezvous machinery.
#

$status = Tibrv::tibrv_Open();

die "Tibrv::tibrv_Open failed." if $status != Tibrv::TIBRV_OK;

#
# use default parameters
#
$status = Tibrv::tibrvTransport_Create($transport);
die "Tibrv::tibrv_TransportCreate failed" if $status != Tibrv::TIBRV_OK;

#
# when you check the connected clients via rvd this is how you know what
# is connected
#
Tibrv::tibrvTransport_SetDescription($transport,$0);

$status=Tibrvft::tibrvftMonitor_Create( $monitor,
                                        Tibrv::TIBRV_DEFAULT_QUEUE,
                                        "ftmon_callback",
                                        $transport,
                                        "PERL_FT_TEST",
                                        10,
                                        0);

print "Waiting for group information...\n";

do
{
    $status=Tibrv::tibrvQueue_TimedDispatch(Tibrv::TIBRV_DEFAULT_QUEUE,
                                            Tibrv::TIBRV_WAIT_FOREVER);
}
while( $status == Tibrv::TIBRV_OK );


#
# if for some reason we exit out of tibrvQueue_Dispatch, cleanup
#

tibrv_Close();
