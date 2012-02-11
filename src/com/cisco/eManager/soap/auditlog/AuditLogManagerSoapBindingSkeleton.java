/**
 * AuditLogManagerSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.auditlog;

public class AuditLogManagerSoapBindingSkeleton implements com.cisco.eManager.soap.auditlog.AuditLogManager, org.apache.axis.wsdl.Skeleton {
    private com.cisco.eManager.soap.auditlog.AuditLogManager impl;
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
        };
        _oper = new org.apache.axis.description.OperationDesc("xmlMessage", _params, new javax.xml.namespace.QName("", "xmlMessageReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://auditlog.soap.eManager.cisco.com", "xmlMessage"));
        _oper.setSoapAction("xmlMessage");
        _myOperationsList.add(_oper);
        if (_myOperations.get("xmlMessage") == null) {
            _myOperations.put("xmlMessage", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("xmlMessage")).add(_oper);
    }

    public AuditLogManagerSoapBindingSkeleton() {
        this.impl = new com.cisco.eManager.soap.auditlog.AuditLogManagerSoapBindingImpl();
    }

    public AuditLogManagerSoapBindingSkeleton(com.cisco.eManager.soap.auditlog.AuditLogManager impl) {
        this.impl = impl;
    }
    public java.lang.String xmlMessage(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.xmlMessage(in0, in1);
        return ret;
    }

}
