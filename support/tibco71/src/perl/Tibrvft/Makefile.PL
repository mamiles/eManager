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
    NAME        => "Tibrvft",
    'VERSION_FROM' => 'Tibrvft.pm', # finds $VERSION
    INC		=> "-I${TIBRV_DIR}/include",
    DEFINE      => "-D_STDC_PROTO_",
    LIBS	=> ["-L${TIBRV_DIR}/lib -ltibrvft -ltibrv"],
    macro       => {DLBASE => "libtibrvft"},
    dynamic_lib => {OTHERLDFLAGS => "msvcrt.lib kernel32.lib"},
    XSPROTOARG  => "-prototypes"
);
}
else  # unix
{
WriteMakefile(
    NAME        => "Tibrvft",
    'VERSION_FROM' => 'Tibrvft.pm', # finds $VERSION
    INC		=> "-I${TIBRV_DIR}/include",
    DEFINE      => $define,
    LIBS	=> ["-L${TIBRV_DIR}/lib -ltibrvft -ltibrv"],
    XSPROTOARG  => "-prototypes"
);
}
