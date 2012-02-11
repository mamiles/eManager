/**
 * ProcessManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.process;

public interface ProcessManagerService extends javax.xml.rpc.Service {
    public java.lang.String getProcessManagerAddress();

    public com.cisco.eManager.soap.process.ProcessManager getProcessManager() throws javax.xml.rpc.ServiceException;

    public com.cisco.eManager.soap.process.ProcessManager getProcessManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
