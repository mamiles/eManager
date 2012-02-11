// NAME
//      $RCSfile: SnmpContextv3.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.20 $
// CREATED
//      $Date: 2002/10/23 11:48:01 $
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

import java.net.*;
import java.io.*;
import java.util.*;

import uk.co.westhawk.snmp.pdu.*;
import uk.co.westhawk.snmp.util.*;
import uk.co.westhawk.snmp.event.*;

/**
 * This class contains the SNMP v3 context that is needed by every Pdu to
 * send a SNMP v3 request.
 *
 * <p>
 * This class will perform the v3 discovery of the SNMP engine ID and
 * time line if necessary. This is done with the classes
 * <code>TimeWindow</code> and <code>UsmDiscoveryBean</code>.
 * </p>
 *
 * <p>
 * Now that the stack can send traps, it needs to be able to act as an
 * authoritative SNMP engine. This is done via the interface UsmAgent.
 * The DefaultUsmAgent is not guaranteed to work; agents (or rather 
 * authoritative engines) <em>should</em> provide a better implementation.
 * </p>
 *
 * <p>
 * This class will use the User Security Model (USM) as described in 
 * <a href="http://ietf.org/rfc/rfc2574.txt">RFC 2574</a>.
 * See also <a href="http://ietf.org/rfc/rfc2574.txt">RFC 2571</a>.
 * </p>
 *
 * <p>
 * It is advised to set all the properties of this class before any Pdu,
 * using this class, is sent. 
 * All properties are being used to encode the message. Some properties are 
 * being used to decode the Reponse or Report Pdu. 
 * When any of these last properties were changed in between flight there 
 * is a possibility the decoding fails, causing a
 * <code>DecodingException</code>. 
 * </p>
 * 
 * <p>
 * <code>destroy()</code> should be called when the context is no longer
 * used. This is the only way the threads will be stopped and garbage
 * collected.
 * </p>
 *
 * @see SnmpContextv3Face
 * @see SnmpContextv3Pool
 * @see TimeWindow
 * @see UsmAgent
 * @see uk.co.westhawk.snmp.beans.UsmDiscoveryBean
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.20 $ $Date: 2002/10/23 11:48:01 $
 */
public class SnmpContextv3 extends AbstractSnmpContext 
implements SnmpContextv3Face, Cloneable
{
    private static final String     version_id =
        "@(#)$Id: SnmpContextv3.java,v 3.20 2002/10/23 11:48:01 birgit Exp $ Copyright Westhawk Ltd";

    private Hashtable msgIdHash = new Hashtable(MAXPDU);
    private String userName = Default_UserName;
    private boolean useAuthentication = false;
    private String userAuthenticationPassword;
    private byte[] userAuthKeyMD5 = null;
    private byte[] userAuthKeySHA1 = null;
    private int authenticationProtocol = MD5_PROTOCOL;
    private boolean usePrivacy = false;
    private String userPrivacyPassword;
    private byte[] userPrivKeyMD5 = null;
    private byte[] userPrivKeySHA1 = null;
    private byte [] contextEngineId = new byte[0];
    private String contextName = Default_ContextName;

    private static UsmAgent usmAgent = null;
    private static int  next_id = 1;

/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @see AbstractSnmpContext#AbstractSnmpContext(String, int)
 */
public SnmpContextv3(String host, int port) throws java.io.IOException
{
    this(host, port, STANDARD_SOCKET);
}

/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @param typeSocketA The type of socket to use. 
 *
 * @see AbstractSnmpContext#AbstractSnmpContext(String, int, String)
 * @see SnmpContextBasisFace#STANDARD_SOCKET
 * @see SnmpContextBasisFace#NETSCAPE_SOCKET
 * @see SnmpContextBasisFace#KVM_SOCKET
 */
public SnmpContextv3(String host, int port, String typeSocketA) 
throws java.io.IOException
{
    super(host, port, typeSocketA);

    if (TimeWindow.getCurrent() == null)
    {
        TimeWindow timew = new TimeWindow();
    }
    if (usmAgent == null)
    {
        usmAgent = createUsmAgent();
    }
}

public int getVersion()
{
    return AsnObject.SNMP_VERSION_3;
}

/**
 * Returns the username.
 *
 * @return the username
 */
public String getUserName()
{
    return userName;
}

/**
 * Sets the username.
 * This username will be used for all PDU send with this context.
 * The username corresponds to the 'msgUserName' in RFC 2574.
 * The default value is "initial".
 *
 * @param newUserName The new username
 * @see #Default_UserName
 */
public void setUserName(String newUserName)
{
    userName = newUserName;
}

/**
 * Return if authentication is used or not.
 * By default no authentication will be used.
 *
 * @return true if authentication is used, false if not
 */
public boolean isUseAuthentication()
{
    return useAuthentication;
}

/**
 * Sets whether authentication has to used. 
 * By default no authentication will be used.
 *
 * @param newUseAuthentication The use of authentication
 */
public void setUseAuthentication(boolean newUseAuthentication)
{
    useAuthentication = newUseAuthentication;
}

/**
 * Returns the user authentication password.
 * This password will be transformed into the user authentication secret key.
 *
 * @return The user authentication password
 */
public String getUserAuthenticationPassword()
{
    return userAuthenticationPassword;
}

/**
 * Sets the user authentication password.
 * This password will be transformed into the user authentication secret
 * key. A user MUST set this password.
 *
 * @param newUserAuthPassword The user authentication password
 */
public void setUserAuthenticationPassword(String newUserAuthPassword)
{
    if (newUserAuthPassword != null
            &&
        newUserAuthPassword.equals(userAuthenticationPassword) == false)
    {
        userAuthenticationPassword = newUserAuthPassword;
        userAuthKeyMD5 = null;
        userAuthKeySHA1 = null;
    }
}

/**
 * Sets the protocol to be used for authentication.
 * This can either be MD5 or SHA-1.
 * By default MD5 will be used.
 *
 * @param protocol The authentication protocol to be used
 * @see #MD5_PROTOCOL
 * @see #SHA1_PROTOCOL
 */
public void setAuthenticationProtocol(int protocol)
throws IllegalArgumentException
{
    if (protocol == MD5_PROTOCOL || protocol == SHA1_PROTOCOL)
    {
        if (protocol != authenticationProtocol)
        {
            authenticationProtocol = protocol;
        }
    }
    else
    {
        throw new IllegalArgumentException("Authentication Protocol "
            + "should be MD5 or SHA1");
    }
}

/**
 * Returns the protocol to be used for authentication.
 * This can either be MD5 or SHA-1.
 * By default MD5 will be used.
 *
 * @return The authentication protocol to be used
 * @see #MD5_PROTOCOL
 * @see #SHA1_PROTOCOL
 */
public int getAuthenticationProtocol()
{
    return authenticationProtocol;
}

byte[] getAuthenticationPasswordKeyMD5()
{
    if (userAuthKeyMD5 == null)
    {
        userAuthKeyMD5 = SnmpUtilities.passwordToKeyMD5(userAuthenticationPassword);
    }
    return userAuthKeyMD5;
}

byte[] getAuthenticationPasswordKeySHA1()
{
    if (userAuthKeySHA1 == null)
    {
        userAuthKeySHA1 = SnmpUtilities.passwordToKeySHA1(userAuthenticationPassword);
    }
    return userAuthKeySHA1;
}


byte[] getPrivacyPasswordKeyMD5()
{
    if (userPrivKeyMD5 == null)
    {
        userPrivKeyMD5 = SnmpUtilities.passwordToKeyMD5(userPrivacyPassword);
    }
    return userPrivKeyMD5;
}

byte[] getPrivacyPasswordKeySHA1()
{
    if (userPrivKeySHA1 == null)
    {
        userPrivKeySHA1 = SnmpUtilities.passwordToKeySHA1(userPrivacyPassword);
    }
    return userPrivKeySHA1;
}


/**
 * Return if privacy is used or not.
 *
 * @return true if privacy is used, false if not
 */
public boolean isUsePrivacy()
{
    return usePrivacy;
}

/**
 * Sets whether privacy has to used. 
 * Note, privacy (encryption) without authentication is not allowed.
 *
 * @param newUsePrivacy The use of privacy
 */
public void setUsePrivacy(boolean newUsePrivacy)
{
    usePrivacy = newUsePrivacy;
}

/**
 * Returns the user privacy password.
 * This password will be transformed into the user privacy secret key.
 *
 * @return The user privacy password
 */
public String getUserPrivacyPassword()
{
    return userPrivacyPassword;
}


/**
 * Sets the user privacy password.
 * This password will be transformed into the user privacy secret
 * key. A user <em>must</em> set this password in order to use privacy.
 *
 * @param newUserPrivacyPassword The user privacy password
 */
public void setUserPrivacyPassword(String newUserPrivacyPassword)
{
    if (newUserPrivacyPassword != null
            &&
        newUserPrivacyPassword.equals(userPrivacyPassword) == false)
    {
        userPrivacyPassword = newUserPrivacyPassword;
        userPrivKeyMD5 = null;
        userPrivKeySHA1 = null;
    }
}

/**
 * Sets the contextEngineID. 
 * See RFC 2571.
 *
 * A contextEngineID uniquely
 * identifies an SNMP entity that may realize an instance of a context
 * with a particular contextName.
 *
 * @param newContextEngineId The contextEngineID
 */
public void setContextEngineId(byte [] newContextEngineId)
throws IllegalArgumentException
{
    contextEngineId = newContextEngineId;
    if (contextEngineId == null)
    {
        throw new IllegalArgumentException("contextEngineId is null");
    }
}

/**
 * Returns the contextEngineID. 
 *
 * @return The contextEngineID
 */
public byte [] getContextEngineId()
{
    return contextEngineId;
}

/**
 * Sets the contextName. 
 * See RFC 2571.
 *
 * A contextName is used to name a context. Each contextName MUST be
 * unique within an SNMP entity.
 * By default this is "". 
 *
 * @param newContextName The contextName
 * @see #Default_ContextName
 */
public void setContextName(String newContextName)
{
    contextName = newContextName;
}

/**
 * Returns the contextName. 
 *
 * @return The contextName
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
 * @see AbstractSnmpContext#addPdu(Pdu)
 * @see #addPdu(Pdu)
 */
public boolean addDiscoveryPdu(DiscoveryPdu pdu)
throws java.io.IOException, PduException
{
    // since this is a DiscoveryPdu we do not check the for discovery :-)
    return this.addPdu(pdu, false);
}

/**
 * Adds an pdu. This method adds the Pdu and block until it has all the
 * discovery parameters it needs.
 *
 * @param pdu the pdu
 * @return pdu is succesful added
 * @see AbstractSnmpContext#addPdu(Pdu)
 * @see #addDiscoveryPdu(DiscoveryPdu)
 */
public boolean addPdu(Pdu pdu)
throws java.io.IOException, PduException
{
    return this.addPdu(pdu, true);
}

/**
 * Creates the USM agent. 
 */
protected UsmAgent createUsmAgent()
{
    return new DefaultUsmAgent();
}

/**
 * Sets the UsmAgent, needed when this stack is used as authoritative
 * SNMP engine. This interface provides authentiation details, like its
 * clock and its Engine ID.
 * 
 * @param agent The USM authoritative interface
 */
public static void setUsmAgent(UsmAgent agent)
{
    usmAgent = agent;
}

/**
 * Adds an pdu. This method adds the Pdu and checks if discovery is
 * needed depending on the parameter <code>checkDiscovery</code>.
 * If discovery is needed this method will block until it have done so.
 * Discovery is only needed if the stack is non authoritative.
 *
 * @param pdu the pdu
 * @param checkDiscovery check if discovery is needed
 * @return pdu is succesful added
 * @see AbstractSnmpContext#addPdu(Pdu)
 * @see #addDiscoveryPdu(DiscoveryPdu)
 * @see #addPdu(Pdu)
 */
protected boolean addPdu(Pdu pdu, boolean checkDiscovery)
throws java.io.IOException, PduException
{
    boolean added = super.addPdu(pdu);
    if (checkDiscovery == true && isAuthoritative(pdu.getMsgType()) == false)
    {
        discoverIfNeeded();
    }
    return added;
}

/**
 * Removes a pdu. This removes the Pdu from the AbstractSnmpContext and
 * clear the link with the SNMPv3 msgId.
 *
 * @param rid the Pdu request id
 * @return whether the pdu has been successfully removed
 * @see AbstractSnmpContext#removePdu(int)
 */
public synchronized boolean removePdu(int rid)
{
    boolean removed = super.removePdu(rid);
    if (removed)
    {
        Enumeration keys = msgIdHash.keys();
        Integer msgIdI = null;
        boolean found = false;
        while (keys.hasMoreElements() && found == false)
        {
            msgIdI = (Integer) keys.nextElement();
            Integer pduIdI = (Integer) msgIdHash.get(msgIdI);
            found = (pduIdI.intValue() == rid);
        }
        if (found)
        {
            msgIdHash.remove(msgIdI);
        }
    }
    return removed;
}

/**
 * Encodes a discovery pdu packet. This methods encodes without checking
 * is the discovery paramters are all known.
 */
public byte[] encodeDiscoveryPacket(byte msg_type, int rId, int errstat, 
      int errind, Enumeration ve) 
      throws java.io.IOException, EncodingException
{
    String engineId = "";
    TimeWindow tWindow = TimeWindow.getCurrent();
    if (tWindow.isSnmpEngineIdKnown(hostAddr, hostPort) == true)
    {
        engineId = tWindow.getSnmpEngineId(hostAddr, hostPort);
    }
    TimeWindowNode node = new TimeWindowNode(engineId, 0, 0);

    return actualEncodePacket(msg_type, rId, errstat, errind, ve, node);
}

/**
 * Encodes a pdu packet. If the stack is authoritative, the timeline
 * details are retrieved from the usmAgent. If not, this methods first checks 
 * if all the discovery
 * paramters are all known, if not it will throw an EncodingException.
 * If so, it encodes and returns the bytes.
 */
public byte[] encodePacket(byte msg_type, int rId, int errstat, 
      int errind, Enumeration ve) 
      throws java.io.IOException, EncodingException
{
    TimeWindowNode node;
    if (isDestroyed == true)
    {
        throw new EncodingException("Context can no longer be used, since it is already destroyed");
    }
    else
    {
        if (isAuthoritative(msg_type) == true)
        {
            usmAgent.setSnmpContext(this);
            node = new TimeWindowNode(usmAgent.getSnmpEngineId(),
                usmAgent.getSnmpEngineBoots(), usmAgent.getSnmpEngineTime());
        }
        else
        {
            TimeWindow tWindow = TimeWindow.getCurrent();
            if (tWindow.isSnmpEngineIdKnown(hostAddr, hostPort) == false)
            {
                throw new EncodingException("Engine ID of host " + hostAddr 
                    + ", port " + hostPort + " is unknown. Perform discovery.");
            }
            String engineId = tWindow.getSnmpEngineId(hostAddr, hostPort);
            node = new TimeWindowNode(engineId, 0, 0);

            if (isUseAuthentication())
            {
                if (tWindow.isTimeLineKnown(engineId) == true)
                {
                    node = tWindow.getTimeLine(engineId);
                }
                else
                {
                    throw new EncodingException("Time Line of Engine ID of host " 
                        + hostAddr + ", port " + hostPort + " is unknown. "
                        + "Perform discovery.");
                }
            }
        }
    }
    return actualEncodePacket(msg_type, rId, errstat, errind, ve, node);
}

/**
 * Does the actual encoding. This method stores the SNMPv3 msgId and Pdu
 * request id in a Hashtable. 
 * Since the encoding only happens once and every retry sends the same 
 * encoded packet, only one msgId is used.
 *
 * @see #encodeDiscoveryPacket
 * @see #encodePacket
 */
protected byte[] actualEncodePacket(byte msg_type, int rId, int errstat, 
      int errind, Enumeration ve, TimeWindowNode node) 
      throws java.io.IOException, EncodingException
{
    AsnEncoder enc = new AsnEncoder();
    int msgId = next_id;
    next_id++;
    msgIdHash.put(new Integer(msgId), new Integer(rId));
    if (AsnObject.debug > 6)
    {
        System.out.println("SnmpContextv3.actualEncodePacket(): msgId="
            + msgId + ", Pdu reqId=" + rId);
    }
    byte[] packet = enc.EncodeSNMPv3(this, msgId, node, 
          msg_type, rId, errstat, errind, ve);

    return packet;
}

/**
 * Processes an incoming SNMP v3 message.
 */
protected void ProcessIncomingMessage(AsnDecoder rpdu, ByteArrayInputStream in)
throws DecodingException, IOException
{
    byte [] bu = null;
    // need to duplicate the message for V3 to rewrite 
    int nb = in.available();
    bu = new byte[nb];
    in.read(bu);
    in = new ByteArrayInputStream(bu);

    AsnSequence asnTopSeq = rpdu.DecodeSNMPv3(in);
    int msgId = rpdu.getMsgId(asnTopSeq);
    Integer rid = (Integer) msgIdHash.get(new Integer(msgId));
    if (rid != null)
    {
        if (AsnObject.debug > 6)
        {
            System.out.println("SnmpContextv3.ProcessIncomingMessage(): msgId="
                + msgId + ", Pdu reqId=" + rid);
        }
        Pdu pdu = getPdu(rid);
        try
        {
            AsnPduSequence pduSeq = rpdu.processSNMPv3(this, asnTopSeq,bu);
            if (pduSeq != null)
            {
                // got a message
                Integer rid2 = new Integer(pduSeq.getReqId());
                if (AsnObject.debug > 6)
                {
                    System.out.println("SnmpContextv3.ProcessIncomingMessage():"
                    + " rid2=" + rid2);
                }

                Pdu newPdu = null;
                if (rid2.intValue() != rid.intValue())
                {
                    newPdu = getPdu(rid2);
                    if (AsnObject.debug > 3)
                    {
                        System.out.println("ProcessIncomingMessage(): "
                            + "pduReqId of msgId (" + rid.intValue() 
                            + ") != pduReqId of Pdu (" + rid2.intValue()
                            + ")");
                    }
                    if (newPdu == null)
                    {
                        if (AsnObject.debug > 3)
                        {
                            System.out.println("ProcessIncomingMessage(): "
                                + "Using pduReqId of msgId (" + rid.intValue() + ")");
                        }
                    }
                }

                if (newPdu != null)
                {
                    pdu = newPdu;
                }
            }
            else
            {
                if (AsnObject.debug > 6)
                {
                    System.out.println("SnmpContextv3.ProcessIncomingMessage():"
                    + " pduSeq is null.");
                }
            }

            if (pdu != null)
            {
                pdu.fillin(pduSeq);
            }
            else
            {
                if (AsnObject.debug > 6)
                {
                    System.out.println("ProcessIncomingMessage(): No Pdu with reqid " + rid.intValue());
                }
            }
        }
        catch (DecodingException exc)
        {
            if (pdu != null)
            {
                pdu.setErrorStatus(AsnObject.SNMP_ERR_DECODING_EXC, exc);
                pdu.fillin(null);
            }
            else
            {
                throw exc;
            }
        }
    }
    else
    {
        if (AsnObject.debug > 3)
        {
            System.out.println("Pdu of msgId " + msgId 
                + " is already answered");
        }
        rid = new Integer(-1);
    }
}

/**
 * Returns if we send this pdu in authoritative role or not.
 * The engine who sends a Response, a Trapv2 or a Report is
 * authoritative.
 *
 * @return true if authoritative, false if not.
 */
// Note: for when addnig INFORM
// When sending an INFORM, the receiver is the authorative engine, so
// the INFORM does NOT have to be added to this list!
protected boolean isAuthoritative(byte msg_type)
{
    return (msg_type == AsnObject.GET_RSP_MSG
                ||
            msg_type == AsnObject.TRPV2_REQ_MSG
                ||
            msg_type == AsnObject.GET_RPRT_MSG);
}

void discoverIfNeeded()
throws java.io.IOException, PduException
{
    uk.co.westhawk.snmp.beans.UsmDiscoveryBean discBean = null;
    boolean isNeeded = false;

    TimeWindow tWindow = TimeWindow.getCurrent();
    String engineId = tWindow.getSnmpEngineId(hostAddr, hostPort);
    if (engineId == null)
    {
        isNeeded = true;
        discBean = new uk.co.westhawk.snmp.beans.UsmDiscoveryBean(hostAddr, 
              hostPort, typeSocket);
    }

    if (isUseAuthentication())
    {
        if (isNeeded)
        {
            discBean.setAuthenticationDetails(userName,
                userAuthenticationPassword, authenticationProtocol);
        }
        else if (tWindow.isTimeLineKnown(engineId) == false)
        {
            isNeeded = true;
            discBean = new uk.co.westhawk.snmp.beans.UsmDiscoveryBean(
                    hostAddr, hostPort, typeSocket);
            discBean.setAuthenticationDetails(userName,
                userAuthenticationPassword, authenticationProtocol);
        }

        if (isNeeded && isUsePrivacy())
        {
            discBean.setPrivacyDetails(userPrivacyPassword);
        }
    }

    if (isNeeded)
    {
        discBean.startDiscovery();
    }
}

public Pdu processIncomingTrap(byte [] message) 
throws DecodingException, IOException
{
    int l = message.length;
    byte [] copyOfMessage = new byte[l];
    System.arraycopy(message, 0, copyOfMessage, 0, l);

    AsnDecoder rpdu = new AsnDecoder();
    ByteArrayInputStream in = new ByteArrayInputStream(message);
    AsnSequence asnTopSeq = rpdu.DecodeSNMPv3(in);
    AsnPduSequence pduSeq = rpdu.processSNMPv3(this, asnTopSeq, copyOfMessage);

    TrapPduv2 trapPdu = new uk.co.westhawk.snmp.pdu.OneTrapPduv2(this);
    trapPdu.fillin(pduSeq);
    return trapPdu;
}

/**
 * Returns a clone of this SnmpContextv3.
 *
 * @exception CloneNotSupportedException Thrown when the constructor
 * generates an IOException
 */
public Object clone() throws CloneNotSupportedException
{
    SnmpContextv3 clContext = null;
    try
    {
        clContext = new SnmpContextv3(hostAddr, hostPort, typeSocket);
        clContext.setUserName(new String(userName));
        clContext.setUseAuthentication(useAuthentication);
        if (userAuthenticationPassword != null)
        {
            clContext.setUserAuthenticationPassword(
                new String(userAuthenticationPassword));
        }
        clContext.setAuthenticationProtocol(authenticationProtocol);
        /*
        clContext.setUsePrivacy(usePrivacy);
        if (userPrivacyPassword != null)
        {
            clContext.setUserPrivacyPassword(new String(userPrivacyPassword));
        }
        */

        clContext.setContextName(new String(contextName));

        int l = contextEngineId.length;
        byte[] newContextEngineId = new byte[l];
        System.arraycopy(contextEngineId, 0, newContextEngineId, 0, l);  
        clContext.setContextEngineId(newContextEngineId);
    }
    catch (java.io.IOException exc)
    {
        throw new CloneNotSupportedException("IOException " 
            + exc.getMessage());
    }
    return clContext;
}

/**
 * Returns a string representation of the object.
 * @return The string
 */
public String toString()
{
    StringBuffer buffer = new StringBuffer("SnmpContextv3[");
    buffer.append("host=").append(hostAddr);
    buffer.append(", port=").append(hostPort);
    buffer.append(", socketType=").append(typeSocket);
    buffer.append(", contextEngineId=").append(SnmpUtilities.toHexString(contextEngineId));
    buffer.append(", contextName=").append(contextName);
    buffer.append(", userName=").append(userName);
    buffer.append(", useAuthentication=").append(useAuthentication);
    buffer.append(", authenticationProtocol=").append(ProtocolNames[authenticationProtocol]);
    buffer.append(", userAuthenticationPassword=").append(userAuthenticationPassword);
    buffer.append(", usePrivacy=").append(usePrivacy);
    buffer.append(", userPrivacyPassword=").append(userPrivacyPassword);
    buffer.append("]");
    return buffer.toString();
}

}
