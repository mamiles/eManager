
/*
 * Copyright (c) 1995-2002 TIBCO Software Inc.
 * All rights reserved.
 * TIB/Rendezvous is protected under US Patent No. 5,187,787.
 * For more information, please contact:
 * TIBCO Software, Inc., Palo Alto, California, USA
 *
 * @(#)sdcpp.h	1.2
 *
 */

#ifndef _INCLUDED_tibrvsdcpp_h
#define _INCLUDED_tibrvsdcpp_h

#include "tibrvcpp.h"
#include "sd.h"

//=====================================================================
// Forward declarations
//=====================================================================

class TibrvSdContext;

//=====================================================================
// TibrvSdContext
//=====================================================================

class TibrvSdContext
{

public:
    static TibrvStatus setDaemonCert(const char* daemonName, 
                                     const char* daemonCert);

    static TibrvStatus setUserCertWithKey(const char* userCertWithKey, 
                                          const char* password);

    static TibrvStatus setUserCertWithKeyBin(const void* userCertWithKey,
                                             tibrv_u32	 userCertWithKey_size,
                                             const char* password);

    static TibrvStatus setUserNameWithPassword(const char* userName, 
                                               const char* password);
};

//=====================================================================

#endif  /* _INCLUDED_tibrvsdcpp_h */
