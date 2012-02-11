/**
 * AdminManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.admin;

public interface AdminManagerService extends javax.xml.rpc.Service {
    public java.lang.String getAdminManagerAddress();

    public com.cisco.eManager.soap.admin.AdminManager getAdminManager() throws javax.xml.rpc.ServiceException;

    public com.cisco.eManager.soap.admin.AdminManager getAdminManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
