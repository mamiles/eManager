/**
 * AuditLogManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.auditlog;

public class AuditLogManagerServiceLocator extends org.apache.axis.client.Service implements com.cisco.eManager.soap.auditlog.AuditLogManagerService {

    // Use to get a proxy class for AuditLogManager
    private final java.lang.String AuditLogManager_address = "http://localhost:9080/emanager/services/AuditLogManager";

    public java.lang.String getAuditLogManagerAddress() {
        return AuditLogManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AuditLogManagerWSDDServiceName = "AuditLogManager";

    public java.lang.String getAuditLogManagerWSDDServiceName() {
        return AuditLogManagerWSDDServiceName;
    }

    public void setAuditLogManagerWSDDServiceName(java.lang.String name) {
        AuditLogManagerWSDDServiceName = name;
    }

    public com.cisco.eManager.soap.auditlog.AuditLogManager getAuditLogManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AuditLogManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAuditLogManager(endpoint);
    }

    public com.cisco.eManager.soap.auditlog.AuditLogManager getAuditLogManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.eManager.soap.auditlog.AuditLogManagerSoapBindingStub _stub = new com.cisco.eManager.soap.auditlog.AuditLogManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getAuditLogManagerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.cisco.eManager.soap.auditlog.AuditLogManager.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.eManager.soap.auditlog.AuditLogManagerSoapBindingStub _stub = new com.cisco.eManager.soap.auditlog.AuditLogManagerSoapBindingStub(new java.net.URL(AuditLogManager_address), this);
                _stub.setPortName(getAuditLogManagerWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("AuditLogManager".equals(inputPortName)) {
            return getAuditLogManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://auditlog.soap.eManager.cisco.com", "AuditLogManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("AuditLogManager"));
        }
        return ports.iterator();
    }

}
