// NAME
//      $RCSfile: SnmpContextv3Face.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.12 $
// CREATED
//      $Date: 2002/10/10 15:13:57 $
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
import uk.co.westhawk.snmp.pdu.*;

/**
 * This interface contains the SNMP context interface that is needed by every 
 * Pdu to send a SNMP v3 request.
 * 
 * See <a href="http://ietf.org/rfc/rfc2574.txt">RFC 2574</a>,
 * <a href="http://ietf.org/rfc/rfc2571.txt">RFC 2571</a>.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.12 $ $Date: 2002/10/10 15:13:57 $
 */
public interface SnmpContextv3Face extends SnmpContextBasisFace
{
    static final String     version_id =
        "@(#)$Id: SnmpContextv3Face.java,v 3.12 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * The SNMPv1 security model. This has value 1.
     * The stack does not implement this security model.
     */
    public final static int SNMPv1_Security_Model  = (byte)(0x1);

    /**
     * The SNMPv2c security model. This has value 2.
     * The stack does not implement this security model.
     */
    public final static int SNMPv2c_Security_Model = (byte)(0x2);

    /**
     * The USM security model. This has value 3.
     * This stack only implements this security model!
     */
    public final static int USM_Security_Model     = (byte)(0x3);

    /**
     * The MD5 protocol type.
     */
    public final static int MD5_PROTOCOL=0;

    /**
     * The SHA-1 protocol type.
     */
    public final static int SHA1_PROTOCOL=1;

    /**
     * The default value for the (security) user name. This is
     * "initial".
     */
    public final static String Default_UserName = "initial";

    /**
     * The default Context Name. This is the zero length string, i.e. "".
     */
    public final static String Default_ContextName = "";

    /**
     * The array with the String represensations of the protocols.
     */
    public final static String ProtocolNames [] =
    {
        "MD5",
        "SHA1"
    };

/**
 * Returns the username.
 *
 * @return the username
 */
public String getUserName();

/**
 * Sets the username.
 * This username will be used for all PDU send with this context.
 * The username corresponds to the 'msgUserName' in RFC 2574.
 * The default value is "initial".
 *
 * @param newUserName The new username
 */
public void setUserName(String newUserName);

/**
 * Return if authentication is used or not.
 * By default no authentication will be used.
 *
 * @return true if authentication is used, false if not
 */
public boolean isUseAuthentication();

/**
 * Sets whether authentication has to used. 
 * By default no authentication will be used.
 *
 * @param newUseAuthentication The use of authentication
 */
public void setUseAuthentication(boolean newUseAuthentication);

/**
 * Returns the user authentication password.
 * This password will be transformed into the user authentication secret key.
 *
 * @return The user authentication password
 */
public String getUserAuthenticationPassword();

/**
 * Sets the user authentication password.
 * This password will be transformed into the user authentication secret
 * key. A user MUST set this password.
 *
 * @param newUserAuthPassword The user authentication password
 */
public void setUserAuthenticationPassword(String newUserAuthPassword);

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
throws IllegalArgumentException;

/**
 * Returns the protocol to be used for authentication.
 * This can either be MD5 or SHA-1.
 * By default MD5 will be used.
 *
 * @return The authentication protocol to be used
 * @see #MD5_PROTOCOL
 * @see #SHA1_PROTOCOL
 */
public int getAuthenticationProtocol();


/**
 * Return if privacy is used or not.
 * By default no privacy will be used.
 *
 * @return true if privacy is used, false if not
 */
public boolean isUsePrivacy();

/**
 * Sets whether privacy has to used. 
 * By default no privacy will be used.
 *
 * @param newUsePrivacy The use of privacy
 */
public void setUsePrivacy(boolean newUsePrivacy);

/**
 * Returns the user privacy password.
 * This password will be transformed into the user privacy secret key.
 *
 * @return The user privacy password
 */
public String getUserPrivacyPassword();

/**
 * Sets the user privacy password.
 * This password will be transformed into the user privacy secret
 * key. A user MUST set this password.
 *
 * @param newUserAuthPassword The user privacy password
 */
public void setUserPrivacyPassword(String newUserAuthPassword);


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
throws IllegalArgumentException;

/**
 * Returns the contextEngineID. 
 *
 * @return The contextEngineID
 */
public byte [] getContextEngineId();

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
public void setContextName(String newContextName);

/**
 * Returns the contextName. 
 *
 * @return The contextName
 */
public String getContextName();

/**
 * Adds an discovery pdu. 
 * This is for internal use only and should
 * NOT be called by the developper. 
 *
 * This is called by the the Pdu itself and is added to the interface to
 * cover the different kind of Contexts.
 * @param pdu the discovery pdu
 * @return pdu is succesful added
 */
public boolean addDiscoveryPdu(DiscoveryPdu pdu)
throws java.io.IOException, PduException;


/**
 * Encodes a discovery pdu. 
 * This is for internal use only and should
 * NOT be called by the developper. 
 *
 * This is called by the the Pdu itself and is added to the interface to
 * cover the different kind of Contexts.
 * @return The encoded packet
 */
public byte[] encodeDiscoveryPacket(byte msg_type, int rId, int errstat, 
    int errind, java.util.Enumeration ve) 
    throws java.io.IOException, EncodingException;

}
