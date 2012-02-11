/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2001 by Cisco Systems, Inc.
 * All rights reserved.
 *
 *********************************************************************/
package com.cisco.eManager.eManager.processSequencer.common;

/**
 * Implementations of this interface can be registered with the ThreadPool instance and
 * the worker thread invokes this implementation class to process a request, instead of
 * run()ing it itself.
 */

public interface TPRequestProcessor {
  /**
   * Processes the request added to the ThreadPool.
   *
   * @param request the request object added to the ThreadPool
   *
   * @throws Exception if an exception during the processing the request
   */
  void process(Object request) throws Exception;
}
