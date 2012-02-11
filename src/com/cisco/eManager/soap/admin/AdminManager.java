/**
 * AdminManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.admin;

public interface AdminManager extends java.rmi.Remote {
    public void createUserAccount(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException;
    public void deleteUserAccount(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String login(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public void logout(java.lang.String in0) throws java.rmi.RemoteException;
    public void resetUserPassword(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public void updateUserPassword(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public void updateUserTimeoutSession(java.lang.String in0, long in1) throws java.rmi.RemoteException;
}
