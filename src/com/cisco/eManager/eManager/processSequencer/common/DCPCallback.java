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
 * Interface to be implemented by the callback objects for DCPLibrary.
 * When components register with DCPLibrary, they have to provide a
 * callback object that implements this interface.
 * @see DCPLib#registerComponent(String, DCPCallback)
 */
public interface DCPCallback
{
	/**
	 * This method is called by DCPLibrary code when a change
	 * is made to the property represented by propertyPath.
	 * @param propertyPath the fully specified property name
	 * @param newValue the changed value for this property
	 * @return true if the change is accepted by the callback object; false otherwise
	 */
	public boolean handleChange(String propertyPath, String newValue);
}
