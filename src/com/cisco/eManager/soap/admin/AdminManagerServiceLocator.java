/**
 * AdminManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.admin;

public class AdminManagerServiceLocator extends org.apache.axis.client.Service implements com.cisco.eManager.soap.admin.AdminManagerService {

    // Use to get a proxy class for AdminManager
    private final java.lang.String AdminManager_address = "http://localhost:9080/emanager/services/AdminManager";

    public java.lang.String getAdminManagerAddress() {
        return AdminManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AdminManagerWSDDServiceName = "AdminManager";

    public java.lang.String getAdminManagerWSDDServiceName() {
        return AdminManagerWSDDServiceName;
    }

    public void setAdminManagerWSDDServiceName(java.lang.String name) {
        AdminManagerWSDDServiceName = name;
    }

    public com.cisco.eManager.soap.admin.AdminManager getAdminManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AdminManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAdminManager(endpoint);
    }

    public com.cisco.eManager.soap.admin.AdminManager getAdminManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.eManager.soap.admin.AdminManagerSoapBindingStub _stub = new com.cisco.eManager.soap.admin.AdminManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getAdminManagerWSDDServiceName());
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
            if (com.cisco.eManager.soap.admin.AdminManager.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.eManager.soap.admin.AdminManagerSoapBindingStub _stub = new com.cisco.eManager.soap.admin.AdminManagerSoapBindingStub(new java.net.URL(AdminManager_address), this);
                _stub.setPortName(getAdminManagerWSDDServiceName());
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
        if ("AdminManager".equals(inputPortName)) {
            return getAdminManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://admin.soap.eManager.cisco.com", "AdminManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("AdminManager"));
        }
        return ports.iterator();
    }

}
