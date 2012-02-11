// NAME
//      $RCSfile: SnmpContextv3Pool.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.16 $
// CREATED
//      $Date: 2002/10/23 09:56:03 $
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
 */

package uk.co.westhawk.snmp.stack;

import uk.co.westhawk.snmp.pdu.*;
import uk.co.westhawk.snmp.util.*;
import uk.co.westhawk.snmp.event.*;
import java.util.*;

/**
 * This class contains the pool of SNMP v3 contexts.
 * This class reuses the existings contexts instead of creating a new
 * one everytime.
 * <p>
 * Everytime a property changes the pool is checked for a SnmpContextv3
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
 * @see SnmpContextv3
 * @see SnmpContextPool
 * @see SnmpContextv2cPool
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.16 $ $Date: 2002/10/23 09:56:03 $
 */
public class SnmpContextv3Pool implements SnmpContextv3Face
{
    private static final String     version_id =
        "@(#)$Id: SnmpContextv3Pool.java,v 3.16 2002/10/23 09:56:03 birgit Exp $ Copyright Westhawk Ltd";

    protected static Hashtable contextPool;

    protected String hostAddr, socketType;
    protected int hostPort;

    protected SnmpContextv3 context = null;
    protected String userName = "";
    protected boolean useAuthentication = false;
    protected String userAuthenticationPassword = "";
    protected boolean usePrivacy = false;
    protected String userPrivacyPassword = "";
    protected int authenticationProtocol = MD5_PROTOCOL;
    protected byte [] contextEngineId = new byte[0];
    protected String contextName = Default_ContextName;

    protected boolean hasChanged = false;

/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @see SnmpContextv3#SnmpContextv3(String, int)
 */
public SnmpContextv3Pool(String host, int port) throws java.io.IOException
{
    this(host, port, STANDARD_SOCKET);
}

/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @param typeSocket The type of socket to use.
 * @see SnmpContextv3#SnmpContextv3(String, int, String)
 * @see SnmpContextBasisFace#STANDARD_SOCKET
 * @see SnmpContextBasisFace#NETSCAPE_SOCKET
 * @see SnmpContextBasisFace#KVM_SOCKET
 */
public SnmpContextv3Pool(String host, int port, String typeSocket)
throws java.io.IOException
{
    initPools();
    hostAddr = host;
    hostPort = port;
    socketType = typeSocket;

    // No point in creating a context, a lot of the parameters 
    // are probably going to be set.
    //context = getMatchingContext();
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
    return AsnObject.SNMP_VERSION_3;
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
 * Returns the username.
 *
 * @return the username
 * @see SnmpContextv3#getUserName()
 */
public String getUserName()
{
    return userName;
}

/**
 * Sets the username.
 *
 * @param newUserName The new username
 * @see SnmpContextv3#setUserName(String)
 */
public void setUserName(String newUserName)
{
    if (newUserName != null && newUserName.equals(userName) == false)
    {
        userName = newUserName;
        hasChanged = true;
    }
}

/**
 * Return if authentication is used or not.
 *
 * @return true if authentication is used, false if not
 * @see SnmpContextv3#isUseAuthentication()
 */
public boolean isUseAuthentication()
{
    return useAuthentication;
}

/**
 * Sets whether authentication has to used.
 *
 * @param newUseAuthentication The use of authentication
 * @see SnmpContextv3#setUseAuthentication(boolean)
 */
public void setUseAuthentication(boolean newUseAuthentication)
{
    if (newUseAuthentication != useAuthentication)
    {
        useAuthentication = newUseAuthentication;
        hasChanged = true;
    }
}

/**
 * Returns the user authentication password.
 *
 * @return The user authentication password
 * @see SnmpContextv3#getUserAuthenticationPassword()
 */
public String getUserAuthenticationPassword()
{
    return userAuthenticationPassword;
}

/**
 * Sets the user authentication password.
 *
 * @param newUserAuthenticationPassword The user authentication password
 * @see SnmpContextv3#setUserAuthenticationPassword(String)
 */
public void setUserAuthenticationPassword(String newUserAuthenticationPd)
{
    if (newUserAuthenticationPd != null
            &&
        newUserAuthenticationPd.equals(userAuthenticationPassword) == false)
    {
        userAuthenticationPassword = newUserAuthenticationPd;
        hasChanged = true;
    }
}

/**
 * Sets the protocol to be used for authentication.
 *
 * @param protocol The authentication protocol to be used
 * @see SnmpContextv3#setAuthenticationProtocol(int)
 */
public void setAuthenticationProtocol(int protocol)
{
    if (protocol != authenticationProtocol)
    {
        authenticationProtocol = protocol;
        hasChanged = true;
    }
}

/**
 * Returns the protocol to be used for authentication.
 *
 * @return The authentication protocol to be used
 * @see SnmpContextv3#getAuthenticationProtocol()
 */
public int getAuthenticationProtocol()
{
    return authenticationProtocol;
}

/**
 * Return if privacy is used or not.
 *
 * @return true if privacy is used, false if not
 * @see SnmpContextv3#isUsePrivacy()
 */
public boolean isUsePrivacy()
{
    return usePrivacy;
}

/**
 * Sets whether privacy has to used.
 *
 * @param newUsePrivacy The use of privacy
 * @see SnmpContextv3#setUsePrivacy(boolean)
 */
public void setUsePrivacy(boolean newUsePrivacy)
{
    if (newUsePrivacy != usePrivacy)
    {
        usePrivacy = newUsePrivacy;
        hasChanged = true;
    }
}

/**
 * Returns the user privacy password.
 *
 * @return The user privacy password
 * @see SnmpContextv3#getUserPrivacyPassword()
 */
public String getUserPrivacyPassword()
{
    return userPrivacyPassword;
}

/**
 * Sets the user privacy password.
 *
 * @param newUserPrivacyPassword The user privacy password
 * @see SnmpContextv3#setUserPrivacyPassword(String)
 */
public void setUserPrivacyPassword(String newUserPrivacyPd)
{
    if (newUserPrivacyPd != null
            &&
        newUserPrivacyPd.equals(userPrivacyPassword) == false)
    {
        userPrivacyPassword = newUserPrivacyPd;
        hasChanged = true;
    }
}


/**
 * Sets the contextEngineID.
 *
 * @param newContextEngineId The contextEngineID
 * @see SnmpContextv3#setContextEngineId(byte [])
 */
public void setContextEngineId(byte [] newContextEngineId)
{
    if (newContextEngineId != null
              &&
        newContextEngineId.equals(contextEngineId) == false)
    {
        contextEngineId = newContextEngineId;
        hasChanged = true;
    }
}

/**
 * Returns the contextEngineID.
 *
 * @return The contextEngineID
 * @see SnmpContextv3#getContextEngineId()
 */
public byte [] getContextEngineId()
{
    return contextEngineId;
}

/**
 * Sets the contextName.
 *
 * @param newContextName The contextName
 * @see SnmpContextv3#setContextName(String)
 */
public void setContextName(String newContextName)
{
    if (newContextName != null
            &&
        newContextName.equals(contextName) == false)
    {
        contextName = newContextName;
        hasChanged = true;
    }
}

/**
 * Returns the contextName.
 *
 * @return The contextName
 * @see SnmpContextv3#getContextName()
 */
public String getContextName()
{
    return contextName;
}

/**
 * Adds an discovery pdu. This method adds the Pdu without checking if
 * discovery is needed.
 *
 * @param pdu the discovery pdu
 * @return pdu is succesful added
 * @see SnmpContextv3#addPdu(Pdu)
 * @see #addPdu(Pdu)
 */
public boolean addDiscoveryPdu(DiscoveryPdu pdu)
throws java.io.IOException, PduException, IllegalArgumentException
{
    try
    {
        if (hasChanged == true || context == null)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) 
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("SnmpContextv3Pool.addDiscoveryPdu(): " + exc.getMessage());
        }
    }
    return context.addDiscoveryPdu(pdu);
}

/**
 * Adds an pdu. This method adds the Pdu and block until it has all the
 * discovery parameters it needs.
 *
 * @param pdu the pdu
 * @return pdu is succesful added
 * @see SnmpContextv3#addPdu(Pdu)
 * @see #addDiscoveryPdu(DiscoveryPdu)
 */
public boolean addPdu(Pdu pdu)
throws java.io.IOException, PduException, IllegalArgumentException
{
    try
    {
        if (hasChanged == true || context == null)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) 
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("SnmpContextv3Pool.addPdu(): " + exc.getMessage());
        }
    }
    return context.addPdu(pdu);
}

/**
 * Removes a pdu. This removes the Pdu from the AbstractSnmpContext and
 * clear the link with the SNMPv3 msgId.
 *
 * @param rid the Pdu request id
 * @return whether the pdu has been successfully removed
 * @see SnmpContextv3#removePdu(int)
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
 * Encodes a discovery pdu packet. This methods encodes without checking
 * is the discovery paramters are all known.
 */
public byte [] encodeDiscoveryPacket(byte msg_type, int rId, int errstat,
      int errind, Enumeration ve)
      throws java.io.IOException, EncodingException
{
    byte [] res = null;
    if (context != null)
    {
        res = context.encodeDiscoveryPacket(msg_type, rId, errstat, errind, ve);
    }
    return res;
}

/**
 * Encodes a pdu packet. This methods first checks if all the discovery
 * paramters are all known, if not it will throw an EncodingException.
 * If so, it encodes and returns the bytes.
 */
public byte [] encodePacket(byte msg_type, int rId, int errstat,
      int errind, Enumeration ve)
      throws java.io.IOException, EncodingException
{
    byte [] res = null;
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
    try
    {
        if (hasChanged == true)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) 
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("SnmpContextv3Pool.destroy(): " + exc.getMessage());
        }
    }

    if (context != null)
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
protected SnmpContextv3 getMatchingContext()
throws java.io.IOException, IllegalArgumentException
{
    SnmpContextPoolItem item = null;
    SnmpContextv3 newContext = null;
    String hashKey = getHashKey();

    synchronized(contextPool)
    {
        int count=0;
        if (contextPool.containsKey(hashKey))
        {
            item = (SnmpContextPoolItem) contextPool.get(hashKey);
            newContext = (SnmpContextv3) item.getContext();
            count = item.getCounter();
        }
        else
        {
            newContext = new SnmpContextv3(hostAddr, hostPort, socketType);
            newContext.setContextEngineId(contextEngineId);
            newContext.setContextName(contextName);
            newContext.setUserName(userName);
            newContext.setUseAuthentication(useAuthentication);
            newContext.setUserAuthenticationPassword(userAuthenticationPassword);
            newContext.setAuthenticationProtocol(authenticationProtocol);
            newContext.setUsePrivacy(usePrivacy);
            newContext.setUserPrivacyPassword(userPrivacyPassword);

            item = new SnmpContextPoolItem(newContext);
            contextPool.put(hashKey, item);
        }
        hasChanged = false;
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
    try
    {
        if (hasChanged == true)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) 
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("SnmpContextv3Pool.dumpContexts(): " + exc.getMessage());
        }
    }

    System.out.println(title + " " + contextPool.size());
    Enumeration keys = contextPool.keys();
    int i=0;
    while (keys.hasMoreElements())
    {
        String key = (String) keys.nextElement();
        SnmpContextPoolItem item = (SnmpContextPoolItem) contextPool.get(key);
        if (item != null)
        {
            int count = item.getCounter();
            SnmpContextv3 cntxt = (SnmpContextv3) item.getContext();

            System.out.println("\tcontext: " + key + ", count: " + count
                + ", index: " + i);
            if (cntxt == context)
            {
                System.out.println("\t\tcurrent context");
            }
            i++;
        }
    }
    System.out.println("\thasChanged: " + hasChanged);
}


/**
 * Returns the hash key. This key is built out of all properties. It
 * serves as key for the hashtable of (v3) contexts.
 *
 * @return The hash key
 */
public String getHashKey()
{
    String str = hostAddr
          + "_" + hostPort
          + "_" + socketType
          + "_" + useAuthentication
          + "_" + authenticationProtocol
          + "_" + userAuthenticationPassword
          + "_" + userName
          + "_" + usePrivacy
          + "_" + userPrivacyPassword
          + "_" + SnmpUtilities.toHexString(contextEngineId)
          + "_" + contextName
          + "_v" + getVersion();
    return str;
}

/**
 * Adds the specified trap listener. The listener will be added to the
 * current context, <em>not</em> to all the contexts in the hashtable.
 *
 * @see SnmpContextv3#addTrapListener
 */
public void addTrapListener(TrapListener l) throws java.io.IOException
{
    try
    {
        if (hasChanged == true || context == null)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) 
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("SnmpContextv3Pool.addTrapListener(): " + exc.getMessage());
        }
    }
    context.addTrapListener(l);
}

/**
 * Removes the specified trap listener. The listener will be removed
 * from the current context, <em>not</em> from all the contexts in the
 * hashtable.
 *
 * @see SnmpContextv3#removeTrapListener
 */
public void removeTrapListener(TrapListener l) throws java.io.IOException
{
    try
    {
        if (hasChanged == true || context == null)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) 
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("SnmpContextv3Pool.removeTrapListener(): " + exc.getMessage());
        }
    }
    context.removeTrapListener(l);
}

/**
 * Processes the incoming trap with the current context.
 *
 * @see SnmpContextv3#processIncomingTrap
 */
public Pdu processIncomingTrap(byte [] message)
throws DecodingException, java.io.IOException
{
    try
    {
        if (hasChanged == true || context == null)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) 
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("SnmpContextv3Pool.processIncomingTrap(): " + exc.getMessage());
        }
    }

    Pdu pdu = null;
    pdu = context.processIncomingTrap(message);
    return pdu;
}

/**
 * Returns a string representation of the object.
 * @return The string
 */
public String toString()
{
    try
    {
        if (hasChanged == true || context == null)
        {
            context = getMatchingContext();
        }
    }
    catch (java.io.IOException exc) 
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("SnmpContextv3Pool.toString(): " + exc.getMessage());
        }
    }

    String res = "";
    res = context.toString();
    return res;
}

}
