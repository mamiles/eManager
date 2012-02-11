
/*
 * Copyright (c) 1995-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software, Inc., Palo Alto, California, USA
 *
 * @(#)tibrv.cpp	2.6
 *
 */

#include "tibrvcpp.h"

//=====================================================================
// Tibrv
//=====================================================================

TibrvQueue Tibrv::_defaultQueue(TIBRV_DEFAULT_QUEUE);
TibrvProcessTransport Tibrv::_processTransport(TIBRV_PROCESS_TRANSPORT);

TibrvStatus
Tibrv::open()
{
    return tibrv_Open();
}

TibrvStatus
Tibrv::close()
{
    return tibrv_Close();
}

const char*
Tibrv::version()
{
    return tibrv_Version();
}
