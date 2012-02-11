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
 * NOTE: DO NOT MODIFY THIS FILE 
 *       IF YOU NEED TO MODIFY THIS FILE, YOU NEED TO:
 *       1. Regenerate the SOAP generated files
 *       2. Update EventManagerSoapBindingImpl.java
 *       3. Update ../common/SoapToEmgr.java
 *       4. Update ../common/EmgrToSoap.java
 *
 *       Contact chido@cisco.com for more information
 * 
 *********************************************************************/

package com.cisco.eManager.soap.admin;

public interface AdminManagerInterface
{
   
    public final static String ADMINISTRATOR = "administrator";
    public final static String OPERATOR = "operator";
    public final static String GUEST = "guest";

    public String login(String userName, String password);
    public void updateUserPassword (String sessionID, String oldPassword, String newPassword);
    public void resetUserPassword (String sessionID, String userid, String password);
    public void updateUserTimeoutSession(String sessionID, long timeout);
    public void logout(String sessionID);
    public void createUserAccount (String sessionID, String userid, String password, String role);
    public void deleteUserAccount(String sessionID, String userid);

}
