#
# Copyright 1999-2000 TIBCO Software Inc.
# ALL RIGHTS RESERVED.
# TIB/Rendezvous is protected under US Patent No. 5,187,787.
# For more information, please contact:
# TIBCO Software Inc., Palo Alto, California, USA
#
# @(#)Tibrvft.pm	1.7
#


package Tibrvft;

use strict;
use Carp;
use vars qw($VERSION @ISA @EXPORT @EXPORT_OK $AUTOLOAD);

use constant TIBRVFT_ADV_SOURCE          => "RVFT";
use constant TIBRVFT_PREPARE_TO_ACTIVATE => 1;
use constant TIBRVFT_ACTIVATE            => 2;
use constant TIBRVFT_DEACTIVATE          => 3;

require Exporter;
require DynaLoader;
require AutoLoader;
require Tibrv;

@ISA = qw(Exporter DynaLoader);
# Items to export into callers namespace by default. Note: do not export
# names by default without a very good reason. Use EXPORT_OK instead.
# Do not simply export all your public functions/methods/constants.
@EXPORT = qw(
);
$VERSION = '6.4';

sub AUTOLOAD {
    # This AUTOLOAD is used to 'autoload' constants from the constant()
    # XS function.  If a constant is not found then control is passed
    # to the AUTOLOAD in AutoLoader.

    my $constname;
    ($constname = $AUTOLOAD) =~ s/.*:://;
    croak "& not defined" if $constname eq 'constant';
    my $val = constant($constname, @_ ? $_[0] : 0);
    if ($! != 0) {
	if ($! =~ /Invalid/) {
	    $AutoLoader::AUTOLOAD = $AUTOLOAD;
	    goto &AutoLoader::AUTOLOAD;
	}
	else {
		croak "Your vendor has not defined Tibrvft macro $constname";
	}
    }
    no strict 'refs';
    *$AUTOLOAD = sub () { $val };
    goto &$AUTOLOAD;
}

bootstrap Tibrvft $VERSION;

# Preloaded methods go here.

# Autoload methods go after =cut, and are processed by the autosplit program.

1;
__END__
# Below is the stub of documentation for your module. You better edit it!

=head1 NAME

Tibrvft - Perl extension for TIB/Rendezvous Version 6 Fault Tolerant API.

=head1 SYNOPSIS

  require Tibrv;
  use Tibrvft;

  Tibrvft::tibrvftMember_Create(member, queue, callback, transport, groupName, 
                       weight, activeGoal, heartbeatInterval, 
                       preparationInterval, activationInterval, closure)

  Tibrvft::tibrvftMember_Destroy(member)
  Tibrvft::tibrvftMember_DestroyEx(member, completeCallback)
  Tibrvft::tibrvftMember_GetQueue(member, queue)
  Tibrvft::tibrvftMember_GetTransport(member, transport)
  Tibrvft::tibrvftMember_GetGroupName(member, groupName)
  Tibrvft::tibrvftMember_GetWeight(member, weight)
  Tibrvft::tibrvftMember_SetWeight(member, weight)

  Tibrvft::tibrvftMonitor_Create(monitor, queue, callback, transport, groupName, 
                        lostInterval, closure)
  Tibrvft::tibrvftMonitor_Destroy(monitor)
  Tibrvft::tibrvftMonitor_DestroyEx(monitor, completeCallback)
  Tibrvft::tibrvftMonitor_GetQueue(monitor, queue)
  Tibrvft::tibrvftMonitor_GetTransport(monitor, transport)
  Tibrvft::tibrvftMonitor_GetGroupName(monitor, groupName)

=head1 DESCRIPTION

  The Tibrvft module provides the TIB/Rendezvous V6 Fault Tolerant API 
  functionality to the perl programming language.  For further details 
  please see the TIB/Rendezvous C Programmers Guide

=head1 Exported constants

  Tibrvft::TIBRVFT_ADV_SOURCE
  Tibrvft::TIBRVFT_PREPARE_TO_ACTIVATE
  Tibrvft::TIBRVFT_ACTIVATE
  Tibrvft::TIBRVFT_DEACTIVATE


=head1 AUTHOR

TIBCO Software Inc.

=head1 SEE ALSO

perl(1).

=cut
