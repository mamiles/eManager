#
# Copyright 1999-2000 TIBCO Software Inc. 
# ALL RIGHTS RESERVED.
# TIB/Rendezvous is protected under US Patent No. 5,187,787.
# For more information, please contact:
# TIBCO Software Inc., Palo Alto, California, USA
#
# @(#)Makefile.PL	1.6
#

require 5.005;

use ExtUtils::MakeMaker;
use Config;

$TIBRV_DIR = $ENV{'TIBRV_DIR'};

if ("$TIBRV_DIR" eq "" )
{
    die "TIBRV_DIR must be set to the directory you instaled TIB/Rendezvous V6 into.\n";
}


#
# it's important especially on UNIX platforms that perl have threading
# support enabled.
#
if ( $^O ne 'MSWin32')
{
    if ( ! $Config{'usethreads'} )
    {
	if ($^O eq 'hpux' )
	{
	    print "\nIn order for the HP-UX extension to be used you must\n";
	    print "either statically link the extension into perl or \n";
	    print "rebuild perl with threading support enabled.\n";
	}
	else
	{
	    print "\nDue to the multi-threaded nature of the TIB/Rendezvous\n";
	    print "API and the single threaded nature of perl you may\n";
	    print "experience problems with perl in high message rate\n";
	    print "envronments.\n\n";
	    print "For best results perl should be rebuilt with threading\n";
	    print "enabled.\n\n"

	}
    }


    WriteMakefile(
		  NAME        => "Tibrv",
		  'VERSION_FROM' => 'Tibrv.pm', # finds $VERSION
		  INC		=> "-I${TIBRV_DIR}/include",
		  DEFINE      => $define,
		  LIBS	=> ["-L${TIBRV_DIR}/lib -ltibrv"],
		  XSPROTOARG  => "-prototypes"
		  );
}
else
{
    WriteMakefile(
		  NAME        => "Tibrv",
		  'VERSION_FROM' => 'Tibrv.pm', # finds $VERSION
		  INC		=> "-I${TIBRV_DIR}/include",
		  DEFINE      => "-D_STDC_PROTO_",
		  LIBS	=> ["-L${TIBRV_DIR}/lib -ltibrv"],
		  macro       => {DLBASE => "libtibrv"},
		  dynamic_lib => {OTHERLDFLAGS => "msvcrt.lib kernel32.lib"},
		  XSPROTOARG  => "-prototypes"
		  );
}

