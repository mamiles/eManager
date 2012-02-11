// NAME
//      $RCSfile: SnmpContextPool.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.14 $
// CREATED
//      $Date: 2002/11/06 11:27:59 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 2000, 2001, 2002 by Westhawk Ltd
 * <a href="www.westhawk.co.uk">www.westhawk.co.uk</a>
 *
 * Permission to use, copy, modify, and distribute this software
 * for any purpose and without fee is hereby granted, provided
 * that the above copyright notices appear in all copies and that
 * both the copyright notice and this permission notice appear in
 * supporting documentation.
 * This software is provided "as is" without express or implied
 * warranty.
 * author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 */

package uk.co.westhawk.snmp.stack;

import java.util.*;
import uk.co.westhawk.snmp.event.*;

/**
 * This class contains the pool of SNMP v1 contexts.
 * This class reuses the existings contexts instead of creating a new
 * one everytime.
 * <p>
 * Everytime a property changes the pool is checked for a SnmpContext
 * context that matches all the new properties of this class. If no such
 * context exists, a new one is made.
 * The Pdus associated with the old context remain associated with the
 * old context. 
 * </p>
 *
 * <p>
 * A counter indicates the number of times the context is referenced.
 * The counter is decreased when <code>destroy</code> is called. 
 * When the counter 
 * reaches zero, the context is released.
 * </p>
 *
 * <p>
 * Note that because the underlying context can change when a property
 * is changed and the Pdus remain associated with the old context, all 
 * properties have to be set BEFORE a Pdu is send.
 * </p>
 *
 * <p>
 * Thanks to Seon Lee (slee@virtc.com) for reporting thread safety
 * problems.
 * </p>
 *
 * @see SnmpContext
 * @see SnmpContextv2cPool
 * @see SnmpContextv3Pool
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.14 $ $Date: 2002/11/06 11:27:59 $
 */
public class SnmpContextPool implements SnmpContextFace
{
    private static final String     version_id =
        "@(#)$Id: SnmpContextPool.java,v 3.14 2002/11/06 11:27:59 birgit Exp $ Copyright Westhawk Ltd";

    protected static Hashtable contextPool;

    protected SnmpContext context = null;
    protected String hostAddr, socketType;
    protected int hostPort;
    protected String community = SnmpContext.Default_Community;

/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @see SnmpContext#SnmpContext(String, int)
 */
public SnmpContextPool(String host, int port) throws java.io.IOException
{
    this(host, port, STANDARD_SOCKET);
}

/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @param typeSocket The type of socket to use. 
 * @see SnmpContext#SnmpContext(String, int, String)
 * @see SnmpContextBasisFace#STANDARD_SOCKET
 * @see SnmpContextBasisFace#NETSCAPE_SOCKET
 * @see SnmpContextBasisFace#KVM_SOCKET
 */
public SnmpContextPool(String host, int port, String typeSocket) 
throws java.io.IOException
{
    this(host, port, SnmpContext.Default_Community, typeSocket);
}

/**
 * Constructor.
 *
 * <p>
 * Thanks to Ernest Jones (EJones@netopia.com) for suggesting this
 * constructor.
 * </p>
 *
 * @since 4_12
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @param comm The community name. 
 * @param typeSocket The type of socket to use. 
 * @see SnmpContextBasisFace#STANDARD_SOCKET
 * @see SnmpContextBasisFace#NETSCAPE_SOCKET
 * @see SnmpContextBasisFace#KVM_SOCKET
 */
public SnmpContextPool(String host, int port, String comm, String typeSocket) 
throws java.io.IOException
{
    initPools();
    hostAddr = host;
    hostPort = port;
    community = comm;
    socketType = typeSocket;

    context = getMatchingContext();
}

private static synchronized void initPools()
{
    if (contextPool == null)
    {
        contextPool = new Hashtable(5);
    }
}

/**
 * Return the SNMP version of the context.
 *
 * @return The version
 */
public int getVersion()
{
    return AsnObject.SNMP_VERSION_1;
}

/**
 * Returns the host
 *
 * @return The host
 */
public String getHost()
{
    return hostAddr;
}

/**
 * Returns the port number
 *
 * @return The port no
 */
public int getPort()
{
    return hostPort;
}

/**
 * Returns the type socket 
 *
 * @return The type socket 
 */
public String getTypeSocket()
{
    return socketType;
}

/**
 * Returns the community name.
 * @see SnmpContext#getCommunity
 */
public String getCommunity()
{
    return community;
}

/**
 * Sets the community name.
 *
 * @see SnmpContext#setCommunity(String)
 */
public void setCommunity(String newCommunity)
{
    if (newCommunity != null
            && 
        newCommunity.equals(community) == false)

    {
        community = newCommunity;
        try
        {
            context = getMatchingContext();
        }
        catch (java.io.IOException exc) { }
    }
}

/**
 * Adds a pdu. 
 *
 * @param pdu the Pdu 
 * @return whether the pdu has been successfully added
 */
public boolean addPdu(Pdu pdu)
throws java.io.IOException, PduException
{
    try
    {
        if (context == null)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) {}
    return context.addPdu(pdu);
}

/**
 * Removes a pdu. 
 *
 * @param rid the Pdu request id
 * @return whether the pdu has been successfully removed
 */
public boolean removePdu(int requestId)
{
    boolean res = false;
    if (context != null)
    {
        res = context.removePdu(requestId);
    }
    return res;
}

/**
 * Encodes a pdu packet. 
 */
public byte[] encodePacket(byte msg_type, int rId, int errstat, 
      int errind, Enumeration ve) 
      throws java.io.IOException, EncodingException
{
    byte[] res = null;
    if (context != null)
    {
        res = context.encodePacket(msg_type, rId, errstat, errind, ve);
    }
    return res;
}

/**
 * Pass a correctly encoded SNMP packet, and we'l send it...
 */
public void sendPacket(byte[] packet)
{
    if (context != null)
    {
        context.sendPacket(packet);
    }
}

/**
 * Releases the resources held by this context. This method will
 * decrement the reference counter. When the reference counter reaches
 * zero the actual context is removed from the pool and destroyed.
 */
public void destroy()
{
    String hashKey = getHashKey();
    synchronized(contextPool)
    {
        int count = 0;
        SnmpContextPoolItem item = (SnmpContextPoolItem) contextPool.get(hashKey);
        if (item != null)
        {
            count = item.getCounter();
            count--;
            item.setCounter(count);
        }

        if (count <= 0)
        {
            contextPool.remove(hashKey);
            if (context != null)
            {
                context.destroy();
                context = null;
            }
        }
    }
}

/**
 * Returns a context from the pool. 
 * This methods checks for an existing context that matches all our
 * properties. If such a context does not exist a new one is created and
 * added to the pool. 
 *
 * @return A context from the pool 
 * @see #getHashKey
 */
protected SnmpContext getMatchingContext() throws java.io.IOException
{
    SnmpContextPoolItem item = null;
    SnmpContext newContext = null;
    String hashKey = getHashKey();

    synchronized(contextPool)
    {
        int count=0;
        if (contextPool.containsKey(hashKey))
        {
            item = (SnmpContextPoolItem) contextPool.get(hashKey);
            newContext = (SnmpContext) item.getContext();
            count = item.getCounter();
        }
        else
        {
            newContext = new SnmpContext(hostAddr, hostPort, socketType);
            newContext.setCommunity(community);
            item = new SnmpContextPoolItem(newContext);
            contextPool.put(hashKey, item);
        }
        count++;
        item.setCounter(count);
    }
    return newContext;
}

/**
 * Dumps the pool of contexts. This is for debug purposes.
 * @param title The title of the dump
 */
public void dumpContexts(String title)
{
    System.out.println(title + " " + contextPool.size());
    Enumeration keys = contextPool.keys();
    int i=0;
    Object c = null;
    while (keys.hasMoreElements())
    {
        String key = (String) keys.nextElement();
        SnmpContextPoolItem item = (SnmpContextPoolItem) contextPool.get(key);
        if (item != null)
        {
            int count = item.getCounter();
            SnmpContext cntxt = (SnmpContext) item.getContext();

            System.out.println("\tcontext: " + key + ", count: " + count
                + ", index: " + i);
            if (cntxt == context)
            {
                System.out.println("\t\tcurrent context");
            }
            i++;
        }
    }
}

/**
 * Returns the hash key. This key is built out of all properties. It
 * serves as key for the hashtable of (v1) contexts.
 *
 * @return The hash key
 */
public String getHashKey()
{
    String str = hostAddr
          + "_" + hostPort
          + "_" + socketType
          + "_" + community
          + "_v" + getVersion();
    return str;
}

/**
 * Adds the specified trap listener. The listener will be added to the
 * current context, <em>not</em> to all the contexts in the hashtable.
 *
 * @see SnmpContext#addTrapListener
 */
public void addTrapListener(TrapListener l) throws java.io.IOException
{
    if (context != null)
    {
        context.addTrapListener(l);
    }
}

/**
 * Removes the specified trap listener. The listener will be removed
 * from the current context, <em>not</em> from all the contexts in the
 * hashtable.
 *
 * @see SnmpContext#removeTrapListener
 */
public void removeTrapListener(TrapListener l) throws java.io.IOException
{
    if (context != null)
    {
        context.removeTrapListener(l);
    }
}

/**
 * Processes the incoming trap with the current context.
 *
 * @see SnmpContext#processIncomingTrap
 */
public Pdu processIncomingTrap(byte [] message)
throws DecodingException, java.io.IOException
{
    Pdu pdu = null;
    if (context != null)
    {
        pdu = context.processIncomingTrap(message);
    }
    return pdu;
}

/**
 * Returns a string representation of the object.
 * @return The string
 */
public String toString()
{
    String res = "";
    if (context != null)
    {
        res = context.toString();
    }
    return res;
}

}
