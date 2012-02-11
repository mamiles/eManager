/**
 * LogManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.log;

public class LogManagerServiceLocator extends org.apache.axis.client.Service implements com.cisco.eManager.soap.log.LogManagerService {

    // Use to get a proxy class for LogManager
    private final java.lang.String LogManager_address = "http://localhost:9080/emanager/services/LogManager";

    public java.lang.String getLogManagerAddress() {
        return LogManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LogManagerWSDDServiceName = "LogManager";

    public java.lang.String getLogManagerWSDDServiceName() {
        return LogManagerWSDDServiceName;
    }

    public void setLogManagerWSDDServiceName(java.lang.String name) {
        LogManagerWSDDServiceName = name;
    }

    public com.cisco.eManager.soap.log.LogManager getLogManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LogManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLogManager(endpoint);
    }

    public com.cisco.eManager.soap.log.LogManager getLogManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.eManager.soap.log.LogManagerSoapBindingStub _stub = new com.cisco.eManager.soap.log.LogManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getLogManagerWSDDServiceName());
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
            if (com.cisco.eManager.soap.log.LogManager.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.eManager.soap.log.LogManagerSoapBindingStub _stub = new com.cisco.eManager.soap.log.LogManagerSoapBindingStub(new java.net.URL(LogManager_address), this);
                _stub.setPortName(getLogManagerWSDDServiceName());
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
        if ("LogManager".equals(inputPortName)) {
            return getLogManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://log.soap.eManager.cisco.com", "LogManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("LogManager"));
        }
        return ports.iterator();
    }

}
