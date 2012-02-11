/**
 * ProcessManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.process;

public class ProcessManagerServiceLocator extends org.apache.axis.client.Service implements com.cisco.eManager.soap.process.ProcessManagerService {

    // Use to get a proxy class for ProcessManager
    private final java.lang.String ProcessManager_address = "http://localhost:9080/emanager/services/ProcessManager";

    public java.lang.String getProcessManagerAddress() {
        return ProcessManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ProcessManagerWSDDServiceName = "ProcessManager";

    public java.lang.String getProcessManagerWSDDServiceName() {
        return ProcessManagerWSDDServiceName;
    }

    public void setProcessManagerWSDDServiceName(java.lang.String name) {
        ProcessManagerWSDDServiceName = name;
    }

    public com.cisco.eManager.soap.process.ProcessManager getProcessManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ProcessManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getProcessManager(endpoint);
    }

    public com.cisco.eManager.soap.process.ProcessManager getProcessManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.eManager.soap.process.ProcessManagerSoapBindingStub _stub = new com.cisco.eManager.soap.process.ProcessManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getProcessManagerWSDDServiceName());
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
            if (com.cisco.eManager.soap.process.ProcessManager.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.eManager.soap.process.ProcessManagerSoapBindingStub _stub = new com.cisco.eManager.soap.process.ProcessManagerSoapBindingStub(new java.net.URL(ProcessManager_address), this);
                _stub.setPortName(getProcessManagerWSDDServiceName());
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
        if ("ProcessManager".equals(inputPortName)) {
            return getProcessManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://process.soap.eManager.cisco.com", "ProcessManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("ProcessManager"));
        }
        return ports.iterator();
    }

}
