/*
 * Copyright (c) 1998-2000 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software Inc., Palo Alto, California, USA
 *
 * @(#)tibrv.h	2.9
 */

#ifndef _INCLUDED_tibrv_h
#define _INCLUDED_tibrv_h

#include <stdio.h>
#include <time.h>

#include "types.h"
#include "events.h"
#include "status.h"
#include "msg.h"
#include "queue.h"
#include "qgroup.h"
#include "tport.h"
#include "disp.h"


#if defined(__cplusplus)
extern "C" {
#endif

extern const char*
tibrv_Version(void);

/* Initialization */
extern tibrv_status
tibrv_Open(void);

extern tibrv_status
tibrv_Close(void);

/* for EBCDIC systems this call sets the iconv conversion code page
   or CCSID.  On all other systems this is a no op. */
extern tibrv_status
tibrv_SetCodePages(
    char *host_codepage,
    char *net_codepage);
    
#ifdef  __cplusplus
}
#endif

#endif /* _INCLUDED_tibrv_h */
