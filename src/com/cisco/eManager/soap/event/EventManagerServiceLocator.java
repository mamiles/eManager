/**
 * EventManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.event;

public class EventManagerServiceLocator extends org.apache.axis.client.Service implements com.cisco.eManager.soap.event.EventManagerService {

    // Use to get a proxy class for EventManager
    private final java.lang.String EventManager_address = "http://localhost:9080/emanager/services/EventManager";

    public java.lang.String getEventManagerAddress() {
        return EventManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EventManagerWSDDServiceName = "EventManager";

    public java.lang.String getEventManagerWSDDServiceName() {
        return EventManagerWSDDServiceName;
    }

    public void setEventManagerWSDDServiceName(java.lang.String name) {
        EventManagerWSDDServiceName = name;
    }

    public com.cisco.eManager.soap.event.EventManager getEventManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EventManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEventManager(endpoint);
    }

    public com.cisco.eManager.soap.event.EventManager getEventManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.eManager.soap.event.EventManagerSoapBindingStub _stub = new com.cisco.eManager.soap.event.EventManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getEventManagerWSDDServiceName());
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
            if (com.cisco.eManager.soap.event.EventManager.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.eManager.soap.event.EventManagerSoapBindingStub _stub = new com.cisco.eManager.soap.event.EventManagerSoapBindingStub(new java.net.URL(EventManager_address), this);
                _stub.setPortName(getEventManagerWSDDServiceName());
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
        if ("EventManager".equals(inputPortName)) {
            return getEventManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://event.soap.eManager.cisco.com", "EventManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("EventManager"));
        }
        return ports.iterator();
    }

}
