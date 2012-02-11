/**
 * InventoryManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.inventory;

public class InventoryManagerServiceLocator extends org.apache.axis.client.Service implements com.cisco.eManager.soap.inventory.InventoryManagerService {

    // Use to get a proxy class for InventoryManager
    private final java.lang.String InventoryManager_address = "http://localhost:9080/emanager/services/InventoryManager";

    public java.lang.String getInventoryManagerAddress() {
        return InventoryManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String InventoryManagerWSDDServiceName = "InventoryManager";

    public java.lang.String getInventoryManagerWSDDServiceName() {
        return InventoryManagerWSDDServiceName;
    }

    public void setInventoryManagerWSDDServiceName(java.lang.String name) {
        InventoryManagerWSDDServiceName = name;
    }

    public com.cisco.eManager.soap.inventory.InventoryManager getInventoryManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(InventoryManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getInventoryManager(endpoint);
    }

    public com.cisco.eManager.soap.inventory.InventoryManager getInventoryManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.eManager.soap.inventory.InventoryManagerSoapBindingStub _stub = new com.cisco.eManager.soap.inventory.InventoryManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getInventoryManagerWSDDServiceName());
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
            if (com.cisco.eManager.soap.inventory.InventoryManager.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.eManager.soap.inventory.InventoryManagerSoapBindingStub _stub = new com.cisco.eManager.soap.inventory.InventoryManagerSoapBindingStub(new java.net.URL(InventoryManager_address), this);
                _stub.setPortName(getInventoryManagerWSDDServiceName());
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
        if ("InventoryManager".equals(inputPortName)) {
            return getInventoryManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://inventory.soap.eManager.cisco.com", "InventoryManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("InventoryManager"));
        }
        return ports.iterator();
    }

}
