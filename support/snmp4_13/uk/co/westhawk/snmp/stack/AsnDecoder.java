// NAME
//      $RCSfile: AsnDecoder.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.19 $
// CREATED
//      $Date: 2002/10/10 15:13:57 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 1995, 1996 by West Consulting BV
 *
 * Permission to use, copy, modify, and distribute this software
 * for any purpose and without fee is hereby granted, provided
 * that the above copyright notices appear in all copies and that
 * both the copyright notice and this permission notice appear in
 * supporting documentation.
 * This software is provided "as is" without express or implied
 * warranty.
 * original version by hargrave@dellgate.us.dell.com (Jordan Hargrave)
 */

/*
 * Copyright (C) 1996, 1997, 1998, 1999, 2000, 2001, 2002 by Westhawk Ltd
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

import uk.co.westhawk.snmp.util.*;
import java.io.*;
import java.util.*;

/**
 * This class decodes bytes into a Pdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.19 $ $Date: 2002/10/10 15:13:57 $
 */
class AsnDecoder extends Object implements usmStatsConstants
{
    private static final String     version_id =
        "@(#)$Id: AsnDecoder.java,v 3.19 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

/**
 * Reads the input into an asn sequence.
 */
AsnSequence getAsnSequence(InputStream in)
throws IOException
{
    AsnSequence dummy = new AsnSequence();
    AsnSequence asnTopSeq = (AsnSequence) dummy.AsnReadHeader(in);
    return asnTopSeq;
}

/**
 * Returns the SNMP version number of the asn sequence.
 */
int getSNMPVersion(AsnSequence asnTopSeq) throws DecodingException
{
    int version = -1;
    AsnObject obj = asnTopSeq.getObj(0);
    if (obj instanceof AsnInteger)
    {
        AsnInteger v = (AsnInteger) obj;
        version = v.getValue();
    }
    else
    {
        String msg = "SNMP version should be of type AsnInteger"
            + " instead of " + obj.getRespTypeString();
        throw new DecodingException(msg);
    }
    return version;
}

/**
 * Returns the SNMP v1 and v2c community of the asn sequence.
 */
String getCommunity(AsnSequence asnTopSeq) throws DecodingException
{
    String comm ="";
    AsnObject obj = asnTopSeq.getObj(1);
    if (obj instanceof AsnOctets)
    {
        AsnOctets estat = (AsnOctets) obj;
        comm = estat.getValue();
    }
    else
    {
        String msg = "community should be of type AsnOctets"
            + " instead of " + obj.getRespTypeString();
        throw new DecodingException(msg);
    }
    return comm;
}

/**
 * This method creates an AsnPduSequence out of the characters of the
 * InputStream for v1.
 *
 * @see AbstractSnmpContext#run
 * @see SnmpContext#DecodeSNMP
 */
AsnPduSequence DecodeSNMP(InputStream in, String community)
throws IOException, DecodingException
{
    AsnSequence asnTopSeq = getAsnSequence(in);
    int snmpVersion = getSNMPVersion(asnTopSeq);
    if (snmpVersion != AsnObject.SNMP_VERSION_1)
    {
        String str = SnmpUtilities.getSnmpVersionString(snmpVersion);
        String msg = "Wrong SNMP version: expected SNMPv1, received "
            + str;
        throw new DecodingException(msg);
    }
    String comm = getCommunity(asnTopSeq);
    if (comm.equals(community) == false)
    {
        String msg = "Wrong community: expected "
            + community + ", received " + comm;
        throw new DecodingException(msg);
    }
    AsnPduSequence Pdu = (AsnPduSequence) asnTopSeq.findPdu();
    return Pdu;
}

AsnTrapPduv1Sequence DecodeTrap1Pdu(InputStream in, String community)
throws IOException, DecodingException
{
    AsnSequence asnTopSeq = getAsnSequence(in);
    int snmpVersion = getSNMPVersion(asnTopSeq);
    if (snmpVersion != AsnObject.SNMP_VERSION_1)
    {
        String str = SnmpUtilities.getSnmpVersionString(snmpVersion);
        String msg = "Wrong SNMP version: expected SNMPv1, received "
            + str;
        throw new DecodingException(msg);
    }
    String comm = getCommunity(asnTopSeq);
    if (comm.equals(community) == false)
    {
        String msg = "Wrong community: expected "
            + community + ", received " + comm;
        throw new DecodingException(msg);
    }
    AsnTrapPduv1Sequence trapPdu = (AsnTrapPduv1Sequence) asnTopSeq.findTrapPduv1();
    return trapPdu;
}


/**
 * This method creates an AsnPduSequence out of the characters of the
 * InputStream for v2c.
 *
 * @see AbstractSnmpContext#run
 * @see SnmpContext#DecodeSNMP
 */
AsnPduSequence DecodeSNMPv2c(InputStream in, String community)
throws IOException, DecodingException
{
    AsnSequence asnTopSeq = getAsnSequence(in);
    int snmpVersion = getSNMPVersion(asnTopSeq);
    if (snmpVersion != AsnObject.SNMP_VERSION_2c)
    {
        String str = SnmpUtilities.getSnmpVersionString(snmpVersion);
        String msg = "Wrong SNMP version: expected SNMPv2c, received "
            + str;
        throw new DecodingException(msg);
    }
    String comm = getCommunity(asnTopSeq);
    if (comm.equals(community) == false)
    {
        String msg = "Wrong community: expected "
            + community + ", received " + comm;
        throw new DecodingException(msg);
    }
    AsnPduSequence Pdu = (AsnPduSequence) asnTopSeq.findPdu();
    return Pdu;
}

AsnSequence getAsnHeaderData(AsnSequence asnTopSeq) throws DecodingException
{
    AsnSequence asnHeaderData = null;
    AsnObject obj = asnTopSeq.getObj(1);
    if (obj instanceof AsnSequence)
    {
        asnHeaderData = (AsnSequence) obj;
    }
    else
    {
        String msg = "asnHeaderData should be of type AsnSequence"
            + " instead of " + obj.getRespTypeString();
        throw new DecodingException(msg);
    }
    return asnHeaderData;
}

/**
 * Return the msgId of the SNMPv3 asn sequence.
 */
int getMsgId(AsnSequence asnTopSeq) throws DecodingException
{
    int msgId = -1;
    AsnSequence asnHeaderData = getAsnHeaderData(asnTopSeq);
    AsnObject obj = asnHeaderData.getObj(0);
    if (obj instanceof AsnInteger)
    {
        AsnInteger value = (AsnInteger) obj;
        msgId = value.getValue();
    }
    else
    {
        String msg = "msgId should be of type AsnInteger"
            + " instead of " + obj.getRespTypeString();
        throw new DecodingException(msg);
    }
    return msgId;
}

/**
 * This method creates an AsnPduSequence out of the characters of the
 * InputStream for v3.
 *
 * @see AbstractSnmpContext#run
 * @see SnmpContextv3#DecodeSNMP
 */
AsnSequence DecodeSNMPv3(InputStream in)
throws IOException, DecodingException
{
    AsnSequence asnTopSeq = getAsnSequence(in);
    int snmpVersion = getSNMPVersion(asnTopSeq);
    if (snmpVersion != AsnObject.SNMP_VERSION_3)
    {
        String str = SnmpUtilities.getSnmpVersionString(snmpVersion);
        String msg = "Wrong SNMP version: expected SNMPv3, received "
            + str;
        throw new DecodingException(msg);
    }
    else
    {
        int securityModel = -1;
        AsnSequence asnHeaderData = getAsnHeaderData(asnTopSeq);
        AsnObject obj = asnHeaderData.getObj(3);
        if (obj instanceof AsnInteger)
        {
            AsnInteger value = (AsnInteger) obj;
            securityModel = value.getValue();
            if (securityModel != SnmpContextv3.USM_Security_Model)
            {
                String msg = "Wrong v3 Security Model: expected USM("
                    + SnmpContextv3.USM_Security_Model
                    + "), received "
                    + securityModel;
                throw new DecodingException(msg);
            }
        }
        else
        {
            String msg = "securityModel should be of type AsnInteger"
                + " instead of " + obj.getRespTypeString();
            throw new DecodingException(msg);
        }
    }
    return asnTopSeq;
}

/**
 * Processes the SNMP v3 AsnSequence.
 * See section 3.2 of <a href="http://ietf.org/rfc/rfc2574.txt">RFC 2574</a>.
 */
AsnPduSequence processSNMPv3(SnmpContextv3 context, AsnSequence asnTopSeq, byte[] message)
throws IOException, DecodingException
{
    AsnPduSequence pduSeq = null;

    // if not correct, I'll just skip a lot of tests.
    boolean isCorrect = asnTopSeq.isCorrect;

    AsnSequence asnHeaderData = getAsnHeaderData(asnTopSeq);
    //int msgId = ((AsnInteger)asnHeaderData.getObj(0)).getValue();
    //int maxSize = ((AsnInteger)asnHeaderData.getObj(1)).getValue();
    byte [] msgFlags = ((AsnOctets)asnHeaderData.getObj(2)).getBytes();
    boolean isUseAuthentication = isUseAuthentication(msgFlags[0]);
    boolean isUsePrivacy = isUsePrivacy(msgFlags[0]);

    AsnOctets asnSecurityParameters = (AsnOctets)asnTopSeq.getObj(2);
    AsnSequence usmObject = decodeUSM(asnSecurityParameters);

    byte [] engineIdBytes = ((AsnOctets)usmObject.getObj(0)).getBytes();
    String engineId = SnmpUtilities.toHexString(engineIdBytes);
    int boots = ((AsnInteger)usmObject.getObj(1)).getValue();
    int time = ((AsnInteger)usmObject.getObj(2)).getValue();
    String userName = ((AsnOctets)usmObject.getObj(3)).getValue();
    AsnOctets realFingerPrintObject = (AsnOctets)usmObject.getObj(4);
    byte [] realFingerPrint = realFingerPrintObject.getBytes();
    byte [] salt = ((AsnOctets)usmObject.getObj(5)).getBytes();

    // Section 3.2 rfc2574
    if (TimeWindow.getCurrent().isEngineIdOK(context.getHost(),
        context.getPort(), engineId) == false)
    {
        String msg = "Received engine Id (" + engineId + ") is not correct";
        throw new DecodingException(msg);
    }

    if (userName.equals(context.getUserName()) == false)
    {
        String msg = "Received userName (" + userName + ") is not correct";
        throw new DecodingException(msg);
    }

    // I'm not really supposed to encrypt before checking and doing
    // authentication, but I would like to use the pduSeq
    // So, I'll encrypt and save the possible exception.
    DecodingException encryptionDecodingException = null;
    IOException encryptionIOException = null;
    try
    {
        AsnObject asnScopedObject = asnTopSeq.getObj(3);
        AsnSequence asnPlainScopedPdu = null;
        if (isUsePrivacy == true)
        {
            // if decryption was used, the asnScopedObject would be AsnOctets
            byte[] privKey = null;
            int prot = context.getAuthenticationProtocol();
            if (prot == context.MD5_PROTOCOL)
            {
                byte[] passwKey = context.getPrivacyPasswordKeyMD5();
                privKey = SnmpUtilities.getLocalizedKeyMD5(passwKey, engineId);
            }
            else
            {
                byte[] passwKey = context.getPrivacyPasswordKeySHA1();
                privKey = SnmpUtilities.getLocalizedKeySHA1(passwKey, engineId);
            }

            AsnOctets asnEncryptedScopedPdu = (AsnOctets)asnScopedObject;
            byte[] encryptedText = asnEncryptedScopedPdu.getBytes();
            byte[] plainText = SnmpUtilities.decrypt(encryptedText, salt, privKey);
            if (AsnObject.debug > 10)
            {
                System.out.println("Encrypted PDU: ");
            }

            ByteArrayInputStream plainIn = new ByteArrayInputStream(plainText);
            asnPlainScopedPdu = getAsnSequence(plainIn);
        }
        else
        {
            asnPlainScopedPdu = (AsnSequence)asnScopedObject;
        }

        byte [] contextId = ((AsnOctets)asnPlainScopedPdu.getObj(0)).getBytes();
        String contextName = ((AsnOctets)asnPlainScopedPdu.getObj(1)).getValue();
        pduSeq = (AsnPduSequence) asnPlainScopedPdu.findPdu();
    }
    catch (DecodingException exc)
    {
        encryptionDecodingException = exc;
    }
    catch (IOException exc)
    {
        encryptionIOException = exc;
    }

    boolean userIsUsingAuthentication = context.isUseAuthentication();
    if (isCorrect == true && (isUseAuthentication != userIsUsingAuthentication))
    {
        String msg = "User " + userName + " does ";
        if (userIsUsingAuthentication == false)
        {
            msg += "not ";
        }
        msg += "support authentication, but received message ";

        if (isUseAuthentication)
        {
            msg += "with authentication.";
        }
        else
        {
            msg += "without authentication";
            msg += getUsmStats(pduSeq);
        }
        throw new DecodingException(msg);
    }

    boolean isAuthentic = false;
    if (isCorrect == true && isUseAuthentication == true)
    {
        int fpPos = realFingerPrintObject.getContentsPos();
        if (AsnObject.debug > 10)
        {
            int fpLength = realFingerPrintObject.getContentsLength();
            String str = "Pos finger print = " + fpPos
                + ", len = " + fpLength;
            SnmpUtilities.dumpBytes(str, realFingerPrint);
        }

        byte[] calcFingerPrint = null;
        // Replace the real finger print with the dummy finger print
        System.arraycopy(AsnEncoder.dummyFingerPrint, 0, 
              message, fpPos, realFingerPrint.length);
        int prot = context.getAuthenticationProtocol();
        if (prot == context.MD5_PROTOCOL)
        {
            byte[] passwKey = context.getAuthenticationPasswordKeyMD5();
            byte[] authkey = SnmpUtilities.getLocalizedKeyMD5(passwKey,
                  engineId);
            calcFingerPrint = SnmpUtilities.getFingerPrintMD5(authkey,
                  message);
        }
        else
        {
            byte[] passwKey = context.getAuthenticationPasswordKeySHA1();
            byte[] authkey = SnmpUtilities.getLocalizedKeySHA1(passwKey,
                  engineId);
            calcFingerPrint = SnmpUtilities.getFingerPrintSHA1(authkey,
                  message);
        }

        if (SnmpUtilities.areBytesEqual(realFingerPrint, calcFingerPrint) == false)
        {
            String msg = "Authentication comparison failed";
            throw new DecodingException(msg);
        }
        else
        {
            if (TimeWindow.getCurrent().isOutsideTimeWindow(engineId,
                boots, time))
            {
                String msg = "Message is outside time window";
                throw new DecodingException(msg);
            }
            isAuthentic = true;
        }
    }
    TimeWindow.getCurrent().updateTimeWindow(engineId, boots, time,
          isAuthentic);

    boolean userIsUsingPrivacy = context.isUsePrivacy();
    if (isCorrect == true && (isUsePrivacy != userIsUsingPrivacy))
    {
        String msg = "User " + userName + " does ";
        if (userIsUsingPrivacy == false)
        {
            msg += "not ";
        }
        msg += "support privacy, but received message ";
        if (isUsePrivacy)
        {
            msg += "with privacy.";
        }
        else
        {
            msg += "without privacy";
            msg += getUsmStats(pduSeq);
        }
        throw new DecodingException(msg);
    }

    if (encryptionDecodingException != null)
    {
        throw encryptionDecodingException;
    }
    if (encryptionIOException != null)
    {
        throw encryptionIOException;
    }

    if (pduSeq != null && isCorrect == false)
    {
        pduSeq.isCorrect = false;
    }
    return pduSeq;
}

private boolean isUseAuthentication(byte msgFlags)
{
    boolean isUseAuthentication = ((byte)(0x01) & msgFlags) > 0;
    return isUseAuthentication;
}

private boolean isUsePrivacy(byte msgFlags)
{
    boolean isUsePrivacy = ((byte)(0x02) & msgFlags) > 0;
    return isUsePrivacy;
}

private AsnSequence decodeUSM(AsnOctets asnSecurityParameters)
throws IOException
{
    byte [] usmBytes = asnSecurityParameters.getBytes();
    if (AsnObject.debug > 10)
    {
        SnmpUtilities.dumpBytes("Decoding USM:", usmBytes);
    }

    ByteArrayInputStream usmIn = new ByteArrayInputStream(usmBytes);
    AsnSequence usmOctets = new AsnSequence(usmIn, usmBytes.length, 
          asnSecurityParameters.getContentsPos());
    AsnSequence usmObject = (AsnSequence)usmOctets.getObj(0);
    return usmObject;
}

/**
 * Sometimes when an error occurs the usmStats is sent in the varbind
 * list.
 */
private String getUsmStats(AsnPduSequence pduSeq)
{
    String msg = "";
    AsnSequence varBind = (AsnSequence) pduSeq.getObj(3);
    int size = varBind.getObjCount();
    if (size > 0)
    {
        AsnSequence varSeq = (AsnSequence) varBind.getObj(0);
        varbind vb = new varbind(varSeq);
        AsnObjectId oid = vb.getOid();
        boolean found=false;
        int i=0;
        while (i< usmStatsOids.length && found==false)
        {
            AsnObjectId usmOid = new AsnObjectId(usmStatsOids[i]);
            found = (oid.startsWith(usmOid) == true);
            i++;
        }
        if (found == true)
        {
            i--;
            msg += ": " + usmStatsStrings[i] + " " + vb.getValue();
        }
        else
        {
            msg += ": " + vb;
        }
    }
    return msg;
}
}
