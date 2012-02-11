/**
 * LogManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.log;

public interface LogManagerService extends javax.xml.rpc.Service {
    public java.lang.String getLogManagerAddress();

    public com.cisco.eManager.soap.log.LogManager getLogManager() throws javax.xml.rpc.ServiceException;

    public com.cisco.eManager.soap.log.LogManager getLogManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
