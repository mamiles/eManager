#
# Copyright 1999-2000 TIBCO Software Inc.
# ALL RIGHTS RESERVED.
# TIB/Rendezvous is protected under US Patent No. 5,187,787.
# For more information, please contact:
# TIBCO Software Inc., Palo Alto, California, USA
#
# @(#)test.pl	1.6
#


# Before `make install' is performed this script should be runnable with
# `make test'. After `make install' it should work as `perl test.pl'

######################### We start with some black magic to print on failure.

# Change 1..1 below to 1..last_test_to_print .
# (It may become useful if the test is moved to ./t subdirectory.)

BEGIN { $| = 1; print "1..6\n"; }
END {print "not ok 1\n" unless $loaded;}

use Tibrv;
use Tibrvft;

$loaded = 1;
print "ok 1\n";

$test_done=0;               # flags when the test is done
$ft_mon_callbacks=0;        # how many times do we get called should be twice

######################### End of black magic.

# Insert your test code below (better if it prints "ok 13"
# (correspondingly "not ok 13") depending on the success of chunk 13
# of the test code):

sub init_transport()
{

    $service="7548";
    $network="127.0.0.1";
    $daemon ="";
    $desc="Test Transport";

    $status = Tibrv::tibrvTransport_Create($transport,$service,$network,
					   $daemon);

    if ($status == Tibrv::TIBRV_DAEMON_NOT_FOUND){print "no daemon...\n";}

    return $status;
}

#
# ft member call back, checks rest of tibrvft calls
#
sub ftmbr_callback()
{
    my($member,$groupName,$action,$closure)=@_;
    my($status,$queue,$ecount,$tport,$gname,$weight);
    
    $ecount=0;

    $ecount++ if ($closure ne "ftmember");

    $status=Tibrvft::tibrvftMember_GetQueue($member,$queue);

    $ecount++ if ($status != Tibrv::TIBRV_OK || $queue != Tibrv::TIBRV_DEFAULT_QUEUE);

    $status=Tibrvft::tibrvftMember_GetTransport($member,$tport);

    $ecount++ if ($status != Tibrv::TIBRV_OK || 
		  $queue != Tibrv::TIBRV_DEFAULT_QUEUE);

    $status=Tibrvft::tibrvftMember_GetGroupName($member,$gname);
    $ecount++ if ($status != Tibrv::TIBRV_OK || $gname != "PERL_FT_TEST");

    $status=Tibrvft::tibrvftMember_GetWeight($member,$weight);
    $ecount++ if ($status != Tibrv::TIBRV_OK ); #|| $weight != $something);

    $status=Tibrvft::tibrvftMember_SetWeight($member,0);
    $ecount++ if ($status != Tibrv::TIBRV_OK);

    if ($ecount == 0)
    {
	#
	# leave ft group
	#
        $status=Tibrvft::tibrvftMember_Destroy($member);
	if ($status == Tibrv::TIBRV_OK)
	{
	    print "ok 5\n";
	}
	else
	{
	    print "not ok 5 on Destroy failure\n";
	}
    }
    else
    {
	print "not ok 5 - $action\n";
    }
}

#
# monitor callback
#
sub ftmon_callback()
{
    my($monitor,$gname,$num_active,$closure)=@_;
    my($my_queue,$status,$my_grp,$ecount);

    $ecount = 0;

    if ($closure ne "ftmon"){$ecount++;print "closure wrong\n"};

    $status = Tibrvft::tibrvftMonitor_GetQueue($monitor, $my_queue);
    $status = Tibrvft::tibrvftMonitor_GetGroupName($monitor, $my_grp);

    if ($my_grp ne $gname){print "group name mismatch\n";$ecount++;}
    if ($my_queue != Tibrv::TIBRV_DEFAULT_QUEUE)
    {
	print "queue mismatch\n";
	$ecount++;
    }
    $ft_mon_callbacks++;

    if ( $ft_mon_callbacks == 2 )
    {
	$status=Tibrvft::tibrvftMonitor_Destroy($monitor);
	if ($status == Tibrv::TIBRV_OK && ecount == 0 )
	{
	    print "ok 6\n";
	}
	else
	{
	    print "not ok 6\n";
	}
    }
}
sub member_test()
{
    $status=Tibrvft::tibrvftMember_Create( $tmbr,
					   Tibrv::TIBRV_DEFAULT_QUEUE,
				           "ftmbr_callback",
				           $transport,
				           "PERL_FT_TEST",
				           50,
				           1,
				           2,
				           0,
				           5,
				          "ftmember");

    return $status;
}

sub monitor_test()
{
    $status=Tibrvft::tibrvftMonitor_Create( $tmon,
 				            Tibrv::TIBRV_DEFAULT_QUEUE,
					    "ftmon_callback",
				            $transport,
				            "PERL_FT_TEST",
				            10,
				            "ftmon");

    $status;
}

#
# run the tests
#

$status=Tibrv::tibrv_Open();
if ($status != Tibrv::TIBRV_OK) {print "couldn't open library\n";exit;}

$ftver=Tibrvft::tibrvft_Version();
if ($ftver =~ /6.*/){ print "ok 2\n";}else{print "not ok 2\n";}

if (Tibrv::TIBRV_OK == init_transport() )
{
    if (member_test() == Tibrv::TIBRV_OK)
    {
	print "ok 3\n";
    }
    else
    {
	print "not ok 3\n";
    }

    if (monitor_test() == Tibrv::TIBRV_OK)
    {
	print "ok 4\n";
    }
    else
    {
	print "not ok 4\n";
    }

    #
    # wait for events to happen
    #
    do
    {
	$status=Tibrv::tibrvQueue_Dispatch(Tibrv::TIBRV_DEFAULT_QUEUE)

    } while ($status == Tibrv::TIBRV_OK && $ft_mon_callbacks < 2);

    $status=Tibrv::tibrvTransport_Destroy($transport);
    Tibrv::tibrv_Close();

}
