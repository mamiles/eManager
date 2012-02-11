#!/usr/bin/perl


#
# @(#)tibrvfttime.pl	1.2
#

use Tibrv;
use Tibrvft;
use Getopt::Long;

$is_active = 0;


sub ft_callback
{
    my($member, $grpname, $action, , $arg) = @_;

    if ($action == Tibrvft::TIBRVFT_ACTIVATE)
    {
        printf("####### ACTIVATE: %s\n", $grpname);
        $is_active = 1;
    }

    if ($action == Tibrvft::TIBRVFT_DEACTIVATE)
    {
        printf("####### DEACTIVATE: %s\n", $grpname);
        $is_active = 0;
    }

    if ($action == Tibrvft::TIBRVFT_PREPARE_TO_ACTIVATE)
    {
        printf("####### PREPARE TO ACTIVATE: %s\n", $grpname);
    }
}

sub publish
{
    my($event, $msg, $closure) = @_;

    if( $is_active == 1 )
    {
        $now = time;
        $datetime=pack "lL", time, 0;

        $status=Tibrv::tibrvMsg_Create($time_msg);

        if ($status != Tibrv::TIBRV_OK)
        {
            print "failed to create message ",
                   Tibrv::tibrvStatusGetText($status), "\n";
        }
        else
        {
            $status=Tibrv::tibrvMsg_AddDateTimeEx($time_msg, "DATA", $datetime);
            $status=Tibrv::tibrvMsg_SetSendSubject($time_msg,"TIBRVFT_TIME");

            $status=Tibrv::tibrvTransport_Send($transport, $time_msg);

          Tibrv::tibrvMsg_Destroy($time_msg);
        }
    }

}

sub help_func
{
    print "usage:  rvfttime [-servcie s] [-network n]\n";
    print "        [-daemon d] [-ft-weight weight]\n";

    exit;
}

#
# set some defaults
#

$ftweight=50;
$numactive=1;
$hbInterval=1.5;
$prepareInterval=0;
$activateInterval=4.8;
$timer=10.0;


#
# process args, if any
#
$ret=&GetOptions("service=s" ,\$service, "network=s", \$network,
                 "daemon=s", \$daemon, "ft-weight=i", \$ftweight,
                 "help", \&help_func);
help_func if $ret == 0;


#
# creates the internal TIB/Rendezvous machinery.
#

$status = Tibrv::tibrv_Open();

die "Tibrv::tibrv_Open failed." if $status != Tibrv::TIBRV_OK;

$status = Tibrv::tibrvTransport_Create($transport,
                                       $service,$network,$daemon);

die "Tibrv::tibrv_TransportCreate failed" if $status != Tibrv::TIBRV_OK;

print "Active group member will publish time every 10 seconds.\n";
print "subject is TIBRVFT_TIME\n
";
print "Inactive will not publish time\n";

$status = Tibrv::tibrvEvent_CreateTimer( $tid, Tibrv::TIBRV_DEFAULT_QUEUE,
                                         "publish", $timer, 0 );

$groupName= "RVFT_TIME_EXAMPLE";


# join the ft group

$status=Tibrvft::tibrvftMember_Create( $tmbr,
                                       Tibrv::TIBRV_DEFAULT_QUEUE,
                                       "ft_callback",
                                        $transport,
                                        "TIBRVFT_TIME_EXAMPLE",
                                         $ftweight,
                                         $numactive,
                                         $hbInterval,
                                         $prepareInterval,
                                         $activateInterval,
                                         0);


if ( $status != RVFT_OK )
{
    $sret = Tibrv::tibrvStatus_GetText($status);
    die $sret;
}

do
{
    $status=Tibrv::tibrvQueue_TimedDispatch(Tibrv::TIBRV_DEFAULT_QUEUE,
                                            Tibrv::TIBRV_WAIT_FOREVER);
}
while( $status == Tibrv::TIBRV_OK );
