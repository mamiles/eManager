/**
 * AdminManagerSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.admin;

public class AdminManagerSoapBindingSkeleton implements com.cisco.eManager.soap.admin.AdminManager, org.apache.axis.wsdl.Skeleton {
    private com.cisco.eManager.soap.admin.AdminManager impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createUserAccount", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://admin.soap.eManager.cisco.com", "createUserAccount"));
        _oper.setSoapAction("createUserAccount");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createUserAccount") == null) {
            _myOperations.put("createUserAccount", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createUserAccount")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteUserAccount", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://admin.soap.eManager.cisco.com", "deleteUserAccount"));
        _oper.setSoapAction("deleteUserAccount");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteUserAccount") == null) {
            _myOperations.put("deleteUserAccount", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteUserAccount")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("login", _params, new javax.xml.namespace.QName("", "loginReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://admin.soap.eManager.cisco.com", "login"));
        _oper.setSoapAction("login");
        _myOperationsList.add(_oper);
        if (_myOperations.get("login") == null) {
            _myOperations.put("login", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("login")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("logout", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://admin.soap.eManager.cisco.com", "logout"));
        _oper.setSoapAction("logout");
        _myOperationsList.add(_oper);
        if (_myOperations.get("logout") == null) {
            _myOperations.put("logout", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("logout")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("resetUserPassword", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://admin.soap.eManager.cisco.com", "resetUserPassword"));
        _oper.setSoapAction("resetUserPassword");
        _myOperationsList.add(_oper);
        if (_myOperations.get("resetUserPassword") == null) {
            _myOperations.put("resetUserPassword", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("resetUserPassword")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("updateUserPassword", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://admin.soap.eManager.cisco.com", "updateUserPassword"));
        _oper.setSoapAction("updateUserPassword");
        _myOperationsList.add(_oper);
        if (_myOperations.get("updateUserPassword") == null) {
            _myOperations.put("updateUserPassword", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("updateUserPassword")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("updateUserTimeoutSession", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://admin.soap.eManager.cisco.com", "updateUserTimeoutSession"));
        _oper.setSoapAction("updateUserTimeoutSession");
        _myOperationsList.add(_oper);
        if (_myOperations.get("updateUserTimeoutSession") == null) {
            _myOperations.put("updateUserTimeoutSession", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("updateUserTimeoutSession")).add(_oper);
    }

    public AdminManagerSoapBindingSkeleton() {
        this.impl = new com.cisco.eManager.soap.admin.AdminManagerSoapBindingImpl();
    }

    public AdminManagerSoapBindingSkeleton(com.cisco.eManager.soap.admin.AdminManager impl) {
        this.impl = impl;
    }
    public void createUserAccount(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        impl.createUserAccount(in0, in1, in2, in3);
    }

    public void deleteUserAccount(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        impl.deleteUserAccount(in0, in1);
    }

    public java.lang.String login(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.login(in0, in1);
        return ret;
    }

    public void logout(java.lang.String in0) throws java.rmi.RemoteException
    {
        impl.logout(in0);
    }

    public void resetUserPassword(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        impl.resetUserPassword(in0, in1, in2);
    }

    public void updateUserPassword(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        impl.updateUserPassword(in0, in1, in2);
    }

    public void updateUserTimeoutSession(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        impl.updateUserTimeoutSession(in0, in1);
    }

}
