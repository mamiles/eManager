/************************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 *      Copyright 2002 Cisco Systems, Inc.
 *      All rights reserved.
 *
 ************************************************************************/

/* Package */
package com.cisco.eManager.eManager.processSequencer.watchdog.servers;

import com.tibco.tibrv.*;

import com.cisco.eManager.eManager.processSequencer.common.EventUtils;
import com.cisco.eManager.eManager.processSequencer.watchdog.ProcessExecutor;


public class WDCnsServer extends ProcessExecutor
{
  /*********************************************
   *  Constructor for the WDCnsServer object
   *
   * @param  name  Name of wrapped server
   ********************************************/
  public WDCnsServer (String name)
  {
     super(name);
  }

  /*********************************************
   *  Makes sure we have a valid Tibco Connection
   *
   * @return Boolean.TRUE or Boolean.FALSE based on the
   *      state of the current Tibco Transport.
   * @throws  Exception  if the Transport is null.
   *********************************************/

  public Object heartbeat()
    throws Exception
  {
    Boolean rc = EventUtils.getDefaultTransport().isValid() ?
      Boolean.TRUE : Boolean.FALSE;
    return rc;
  }
}
