#
# Copyright 1999-2000 TIBCO Software Inc.
# ALL RIGHTS RESERVED.
# TIB/Rendezvous is protected under US Patent No. 5,187,787.
# For more information, please contact:
# TIBCO Software Inc., Palo Alto, California, USA
#
# @(#)Tibrvcm.pm	1.5
#


package Tibrvcm;

use strict;
use Carp;
use vars qw($VERSION @ISA @EXPORT @EXPORT_OK $AUTOLOAD);


#

require Exporter;
require DynaLoader;
require AutoLoader;
require Tibrv;

@ISA = qw(Exporter DynaLoader);
# Items to export into callers namespace by default. Note: do not export
# names by default without a very good reason. Use EXPORT_OK instead.
# Do not simply export all your public functions/methods/constants.
@EXPORT = qw(
	TIBRVCM_CANCEL
	TIBRVCM_DEFAULT_ACCEPT_TIME
	TIBRVCM_DEFAULT_COMPLETE_TIME
	TIBRVCM_DEFAULT_SCHEDULER_ACTIVE
	TIBRVCM_DEFAULT_SCHEDULER_HB
	TIBRVCM_DEFAULT_SCHEDULER_WEIGHT
	TIBRVCM_DEFAULT_TRANSPORT_TIMELIMIT
	TIBRVCM_DEFAULT_WORKER_TASKS
	TIBRVCM_DEFAULT_WORKER_WEIGHT
	TIBRVCM_PERSIST
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
		croak "Your vendor has not defined Tibrvcm macro $constname";
	}
    }
    no strict 'refs';
    *$AUTOLOAD = sub () { $val };
    goto &$AUTOLOAD;
}

bootstrap Tibrvcm $VERSION;

# Preloaded methods go here.

# Autoload methods go after =cut, and are processed by the autosplit program.

1;
__END__
# Below is the stub of documentation for your module. You better edit it!

=head1 NAME

Tibrvcm - Perl extension for TIB/Rendezvous V6 Certified Messaging API.

=head1 SYNOPSIS

  require Tibrv;
  use Tibrvcm;

  $string=Tibrvcm::tibrvcm_Version()
  $status=Tibrvcm::tibrvcmTransport_Create(cmTransport, transport, cmName, 
                                         requestOld, ledgerName,
					 syncLedger,   relayAgent)
  $status=Tibrvcm::tibrvcmTransport_Send(cmTransport, message)
  $status=Tibrvcm::tibrvcmTransport_SendRequest(cmTransport, message, reply, 
                                              timeout)
  $status=Tibrvcm::tibrvcmTransport_SendReply(cmTransport, message, 
                                            requestMessage)
  $status=Tibrvcm::tibrvcmTransport_GetTransport(cmTransport, transport)
  $status=Tibrvcm::tibrvcmTransport_GetName(cmTransport, cmName)
  $status=Tibrvcm::tibrvcmTransport_GetRelayAgent(cmTransport, relayAgent)
  $status=Tibrvcm::tibrvcmTransport_GetLedgerName(cmTransport, ledgerName)
  $status=Tibrvcm::tibrvcmTransport_GetSyncLedger(cmTransport, syncLedger)
  $status=Tibrvcm::tibrvcmTransport_GetRequestOld(cmTransport, requestOld)
  $status=Tibrvcm::tibrvcmTransport_AllowListener(cmTransport, cmName)
  $status=Tibrvcm::tibrvcmTransport_DisallowListener(cmTransport, cmName)
  $status=Tibrvcm::tibrvcmTransport_AddListener(cmTransport, cmName, subject)
  $status=Tibrvcm::tibrvcmTransport_RemoveListener(cmTransport, cmName, subject)
  $status=Tibrvcm::tibrvcmTransport_RemoveSendState(cmTransport, subject)
  $status=Tibrvcm::tibrvcmTransport_SyncLedger(cmTransport)
  $status=Tibrvcm::tibrvcmTransport_ConnectToRelayAgent(cmTransport)
  $status=Tibrvcm::tibrvcmTransport_DisconnectFromRelayAgent(cmTransport)
  $status=Tibrvcm::tibrvcmTransport_Destroy(cmTransport)
  $status=Tibrvcm::tibrvcmEvent_CreateListener(event, queue, callback, 
                                             transport, subject, closure)
  $status=Tibrvcm::tibrvcmEvent_GetQueue(event, queue)
  $status=Tibrvcm::tibrvcmEvent_GetListenerSubject(event, subject)
  $status=Tibrvcm::tibrvcmEvent_GetListenerTransport(event, transport)
  $status=Tibrvcm::tibrvcmEvent_SetExplicitConfirm(cmListener)
  $status=Tibrvcm::tibrvcmEvent_ConfirmMsg(cmListener, message)
  $status=Tibrvcm::tibrvcmEvent_Destroy(cmListener, cancelAgreements)
  $status=Tibrvcm::tibrvcmEvent_DestroyEx(cmListener, cancelAgreements, 
                                        completeCallback)
  $status=Tibrvcm::tibrvcmTransport_GetDefaultCMTimeLimit(cmTransport, timeLimit)
  $status=Tibrvcm::tibrvcmTransport_SetDefaultCMTimeLimit(cmTransport, timeLimit)
  $status=Tibrvcm::tibrvcmTransport_ReviewLedger(cmTransport, callback, 
                                               subject, closure)
  $status=Tibrvcm::tibrvcmTransport_CreateDistributedQueue(cmTransport, 
                                                         transport, cmName)
  $status=Tibrvcm::tibrvcmTransport_CreateDistributedQueueEx(cmTransport, 
                                                           transport, 
                                                           cmName, 
                                                           workerWeight, 
                                                           workerTasks, 
                                                           schedulerWeight, 
                                                           schedulerHeartbeat,
                                                           schedulerActivation)
  $status=Tibrvcm::tibrvcmTransport_GetCompleteTime(cmTransport, completeTime)
  $status=Tibrvcm::tibrvcmTransport_SetWorkerWeight(cmTransport, listenerWeight)
  $status=Tibrvcm::tibrvcmTransport_GetWorkerWeight(cmTransport, listenerWeight)
  $status=Tibrvcm::tibrvcmTransport_SetWorkerTasks(cmTransport, listenerTasks)
  $status=Tibrvcm::tibrvcmTransport_GetWorkerTasks(cmTransport, listenerTasks)


=head1 DESCRIPTION

Provides the Certified Messaging components of TIB/Rendezvous V6.

See the C programmers guide for more information.


=head1 AUTHOR

TIBCO Software Inc.

=head1 SEE ALSO

perl(1).

=cut
