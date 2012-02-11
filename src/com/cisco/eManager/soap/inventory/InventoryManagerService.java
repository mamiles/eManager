/**
 * InventoryManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.eManager.soap.inventory;

public interface InventoryManagerService extends javax.xml.rpc.Service {
    public java.lang.String getInventoryManagerAddress();

    public com.cisco.eManager.soap.inventory.InventoryManager getInventoryManager() throws javax.xml.rpc.ServiceException;

    public com.cisco.eManager.soap.inventory.InventoryManager getInventoryManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
