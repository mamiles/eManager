#
# Copyright 1999-2000 TIBCO Software Inc.
# ALL RIGHTS RESERVED.
# TIB/Rendezvous is protected under US Patent No. 5,187,787.
# For more information, please contact:
# TIBCO Software Inc., Palo Alto, California, USA
#
# @(#)Makefile.PL	1.4
#


use ExtUtils::MakeMaker;

$TIBRV_DIR = $ENV{'TIBRV_DIR'};

if ("$TIBRV_DIR" eq "" )
{
    die "TIBRV_DIR must be set to the directory you instaled TIB/Rendezvous V6 into.\n";
}

if ($^O eq 'MSWin32')
{
WriteMakefile(
    NAME        => "Tibrvcm",
    'VERSION_FROM' => 'Tibrvcm.pm', # finds $VERSION
    INC		=> "-I${TIBRV_DIR}/include",
    DEFINE      => "-D_STDC_PROTO_",
    LIBS	=> ["-L${TIBRV_DIR}/lib -ltibrvcmq -ltibrvcm -ltibrvft -ltibrv"],
    macro       => {DLBASE => "libtibrvcm"},
    dynamic_lib => {OTHERLDFLAGS => "msvcrt.lib kernel32.lib"},
    XSPROTOARG  => "-prototypes"
);
}
else  # unix
{
WriteMakefile(
    NAME        => "Tibrvcm",
    'VERSION_FROM' => 'Tibrvcm.pm', # finds $VERSION
    INC		=> "-I${TIBRV_DIR}/include",
    DEFINE      => $define,
    LIBS	=> ["-L${TIBRV_DIR}/lib -ltibrvcmq -ltibrvcm -ltibrvft -ltibrv"],
    XSPROTOARG  => "-prototypes"
);
}


# See lib/ExtUtils/MakeMaker.pm for details of how to influence
# the contents of the Makefile that is written.
#WriteMakefile(
#    'NAME'	=> 'Tibrvcm',
#    'VERSION_FROM' => 'Tibrvcm.pm', # finds $VERSION
#    'LIBS'	=> ['-ltibrvcmq -ltibrvcm -ltibrvft'],   # e.g., '-lm' 
#    'DEFINE'	=> '',     # e.g., '-DHAVE_SOMETHING' 
#    'INC'	=> '',     # e.g., '-I/usr/include/other' 
#);
