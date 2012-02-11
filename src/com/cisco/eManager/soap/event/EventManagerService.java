/**
 * EventManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.event;

public interface EventManagerService extends javax.xml.rpc.Service {
    public java.lang.String getEventManagerAddress();

    public com.cisco.eManager.soap.event.EventManager getEventManager() throws javax.xml.rpc.ServiceException;

    public com.cisco.eManager.soap.event.EventManager getEventManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
