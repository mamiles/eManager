// NAME
//      $RCSfile: AsnEncoder.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.22 $
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
 * author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
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
 * This class encodes a Pdu into bytes.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.22 $ $Date: 2002/10/10 15:13:57 $
 */
class AsnEncoder extends Object
{
    private static final String     version_id =
        "@(#)$Id: AsnEncoder.java,v 3.22 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    // 12 zero octets
    static byte dummyFingerPrint[] =
    {
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
        (byte)(0x0),
    };

/**
 * Encode SNMPv3 packet into bytes.
 */
byte[] EncodeSNMPv3(SnmpContextv3 context,
    int contextMsgId, TimeWindowNode node,
    byte msg_type, int pduId, int errstat, int errind, Enumeration ve)
    throws IOException, EncodingException
{
    ByteArrayOutputStream bout;
    // Create authentication
    AsnSequence asnTopSeq = new AsnSequence();

    // msgGlobalData = HeaderData
    AsnSequence asnHeaderData = new AsnSequence();
    asnHeaderData.add(new AsnInteger(contextMsgId));
    asnHeaderData.add(new AsnInteger(context.getMaxRecvSize()));
    asnHeaderData.add(new AsnOctets(getMsgFlags(context)));
    asnHeaderData.add(new AsnInteger(context.USM_Security_Model));

    // msgData = ScopedPdu (plaintext or encrypted)
    AsnSequence asnPlainScopedPdu = new AsnSequence();
    asnPlainScopedPdu.add(new AsnOctets(context.getContextEngineId()));
    asnPlainScopedPdu.add(new AsnOctets(context.getContextName()));
    // PDU sequence.
    AsnObject asnPduObject = EncodePdu(msg_type, pduId, errstat, errind, ve);
    asnPlainScopedPdu.add(asnPduObject);

    // asnSecurityParameters
    if (AsnObject.debug > 10)
    {
        System.out.println("\nEncode USM: node " + node.toString());
    }
    AsnSequence asnSecurityObject = new AsnSequence();
    byte [] engineIdBytes = SnmpUtilities.toBytes(node.getSnmpEngineId());
    asnSecurityObject.add(new AsnOctets(engineIdBytes));
    asnSecurityObject.add(new AsnInteger(node.getSnmpEngineBoots()));
    asnSecurityObject.add(new AsnInteger(node.getSnmpEngineTime()));
    asnSecurityObject.add(new AsnOctets(context.getUserName()));

    AsnOctets fingerPrintOct;
    if (context.isUseAuthentication())
    {
        fingerPrintOct = new AsnOctets(dummyFingerPrint);
    }
    else
    {
        fingerPrintOct = new AsnOctets("");
    }
    asnSecurityObject.add(fingerPrintOct);

    AsnOctets privOct;
    AsnOctets asnEncryptedScopedPdu = null;
    if (context.isUsePrivacy())
    {
        byte[] privKey = null;
        int prot = context.getAuthenticationProtocol();
        if (prot == context.MD5_PROTOCOL)
        {
            byte[] passwKey = context.getPrivacyPasswordKeyMD5();
            privKey = SnmpUtilities.getLocalizedKeyMD5(passwKey, 
                  node.getSnmpEngineId());
        }
        else
        {
            byte[] passwKey = context.getPrivacyPasswordKeySHA1();
            privKey = SnmpUtilities.getLocalizedKeySHA1(passwKey, 
                  node.getSnmpEngineId());
        }

        byte [] salt = SnmpUtilities.getSaltDES(node.getSnmpEngineBoots());
        privOct = new AsnOctets(salt);

        bout = new ByteArrayOutputStream();
        asnPlainScopedPdu.write(bout);
        byte[] plaintext = bout.toByteArray();

        byte[] encryptedText = SnmpUtilities.encrypt(plaintext, privKey, salt);
        asnEncryptedScopedPdu = new AsnOctets(encryptedText);
    }
    else
    {
        privOct = new AsnOctets("");
    }
    asnSecurityObject.add(privOct);

    ByteArrayOutputStream secOut = new ByteArrayOutputStream();
    asnSecurityObject.write(secOut);
    byte [] bytes = secOut.toByteArray();
    AsnOctets asnSecurityParameters = new AsnOctets(bytes);


    asnTopSeq.add(new AsnInteger(AsnObject.SNMP_VERSION_3));
    asnTopSeq.add(asnHeaderData);
    asnTopSeq.add(asnSecurityParameters);
    if (context.isUsePrivacy())
    {
        asnTopSeq.add(asnEncryptedScopedPdu);
    }
    else
    {
        asnTopSeq.add(asnPlainScopedPdu);
    }


    if (AsnObject.debug > 10)
    {
        System.out.println("\nAsnEncoder.EncodeSNMPv3(): ");
    }
    // Write SNMP object
    bout = new ByteArrayOutputStream();
    asnTopSeq.write(bout);

    int sz = bout.size();
    if (sz > context.getMaxRecvSize())
    {
        throw new EncodingException("Packet size ("+ sz 
            + ") is > maximum size (" + context.getMaxRecvSize() +")");
    }
    byte[] message = bout.toByteArray();

    // can only do this at after building the whole message
    if (context.isUseAuthentication())
    {
        byte[] calcFingerPrint = null;

        int prot = context.getAuthenticationProtocol();
        if (prot == context.MD5_PROTOCOL)
        {
            byte[] passwKey = context.getAuthenticationPasswordKeyMD5();
            byte[] authkey = SnmpUtilities.getLocalizedKeyMD5(passwKey, 
                  node.getSnmpEngineId());
            calcFingerPrint = SnmpUtilities.getFingerPrintMD5(authkey, 
                  message);
        }
        else
        {
            byte[] passwKey = context.getAuthenticationPasswordKeySHA1();
            byte[] authkey = SnmpUtilities.getLocalizedKeySHA1(passwKey, 
                  node.getSnmpEngineId());
            calcFingerPrint = SnmpUtilities.getFingerPrintSHA1(authkey, 
                  message);
        }

        int usmPos = asnSecurityParameters.getContentsPos();
        int fpPos = fingerPrintOct.getContentsPos();
        fpPos += usmPos;
        if (AsnObject.debug > 10)
        {
            int fpLength = fingerPrintOct.getContentsLength();
            String str = "Pos finger print = " + fpPos
                + ", len = " + fpLength;
            SnmpUtilities.dumpBytes(str, calcFingerPrint);
        }

        // Replace the dummy finger print with the real finger print
        System.arraycopy(calcFingerPrint, 0, 
              message, fpPos, dummyFingerPrint.length);
    }
    return message;
}

/**
 * Encode SNMPv2c packet into bytes.
 */
ByteArrayOutputStream EncodeSNMPv2c(SnmpContextv2c context, byte msg_type,
    int pduId, int errstat, int errind, Enumeration ve)
    throws IOException
{
    ByteArrayOutputStream bout;
    AsnSequence asnTopSeq;

    // Create authentication
    asnTopSeq = new AsnSequence();
    asnTopSeq.add(new AsnInteger(AsnObject.SNMP_VERSION_2c));
    asnTopSeq.add(new AsnOctets(context.getCommunity()));  // community

    // Create PDU sequence.
    AsnObject asnPduObject = EncodePdu(msg_type, pduId, errstat, errind, ve);
    asnTopSeq.add(asnPduObject);

    if (AsnObject.debug > 10)
    {
        System.out.println("\nAsnEncoder.EncodeSNMPv2c(): ");
    }
    // Write SNMP object
    bout = new ByteArrayOutputStream();
    asnTopSeq.write(bout);
    return bout;
}

/**
 * Encode SNMPv1 Trap packet into bytes.
 */
ByteArrayOutputStream EncodeSNMP(SnmpContext context, byte msg_type,
    String enterprise, byte[] IpAddress, int generic_trap, int
    specific_trap, long timeTicks, Enumeration ve)
    throws IOException
{
    ByteArrayOutputStream bout;
    AsnSequence asnTopSeq;

    // Create authentication
    asnTopSeq = new AsnSequence();
    asnTopSeq.add(new AsnInteger(AsnObject.SNMP_VERSION_1));
    asnTopSeq.add(new AsnOctets(context.getCommunity()));  // community

    // Create PDU sequence.
    AsnObject asnPduObject = EncodeTrap1Pdu(msg_type, enterprise, 
          IpAddress, generic_trap, specific_trap, timeTicks, ve);

    asnTopSeq.add(asnPduObject);

    if (AsnObject.debug > 10)
    {
        System.out.println("\nAsnEncoder.EncodeSNMP(): ");
    }
    // Write SNMP object
    bout = new ByteArrayOutputStream();
    asnTopSeq.write(bout);
    return bout;
}

/**
 * Encode Trapv1 PDU itself packet into bytes.
 */
private AsnObject EncodeTrap1Pdu(byte msg_type,
    String enterprise, byte[] IpAddress, int generic_trap, int
    specific_trap, long timeTicks, Enumeration ve)
    throws IOException
{
    AsnObject asnPduObject, asnVBObject;

    // kind of request
    asnPduObject = new AsnSequence(msg_type);
    asnPduObject.add(new AsnObjectId(enterprise));    // enterprise

    // agent-addr (thanks Donnie Love (dlove@idsonline.com) for 
    // pointing out that we should have used IPADDRESS type)
    asnPduObject.add(new AsnOctets(IpAddress, AsnObject.IPADDRESS)); 

    asnPduObject.add(new AsnInteger(generic_trap));   // generic-trap
    asnPduObject.add(new AsnInteger(specific_trap));  // specific-trap
    asnPduObject.add(new AsnUnsInteger(timeTicks));   // time-stap

    // Create VarbindList sequence
    AsnObject asnVBLObject = asnPduObject.add(new AsnSequence());

    // Add variable bindings
    while (ve.hasMoreElements())
    {
        asnVBObject = asnVBLObject.add(new AsnSequence());
        varbind vb = (varbind) ve.nextElement();
        asnVBObject.add(vb.getOid());
        asnVBObject.add(vb.getValue());
    }

    return asnPduObject;
}


/**
 * Encode SNMPv1 packet into bytes.
 */
ByteArrayOutputStream EncodeSNMP(SnmpContext context, byte msg_type,
    int pduId, int errstat, int errind, Enumeration ve)
    throws IOException
{
    ByteArrayOutputStream bout;
    AsnSequence asnTopSeq;

    // Create authentication
    asnTopSeq = new AsnSequence();
    asnTopSeq.add(new AsnInteger(AsnObject.SNMP_VERSION_1));
    asnTopSeq.add(new AsnOctets(context.getCommunity()));  // community

    // Create PDU sequence.
    AsnObject asnPduObject = EncodePdu(msg_type, pduId, errstat, errind, ve);
    asnTopSeq.add(asnPduObject);

    if (AsnObject.debug > 10)
    {
        System.out.println("\nAsnEncoder.EncodeSNMP(): ");
    }
    // Write SNMP object
    bout = new ByteArrayOutputStream();
    asnTopSeq.write(bout);
    return bout;
}

/**
 * Encode PDU itself packet into bytes.
 * The actual PDU encoding is the same for v1 to v3.
 * Except for Trapv1, but we are not implementing that.
 */
private AsnObject EncodePdu(byte msg_type,
    int pduId, int errstat, int errind, Enumeration ve)
    throws IOException
{
    AsnObject asnPduObject, asnVBObject;

    // kind of request
    asnPduObject = new AsnSequence(msg_type);
    asnPduObject.add(new AsnInteger(pduId));        // reqid
    asnPduObject.add(new AsnInteger(errstat));      // errstat
    asnPduObject.add(new AsnInteger(errind));       // errindex

    // Create VarbindList sequence
    AsnObject asnVBLObject = asnPduObject.add(new AsnSequence());

    // Add variable bindings
    while (ve.hasMoreElements())
    {
        asnVBObject = asnVBLObject.add(new AsnSequence());
        varbind vb = (varbind) ve.nextElement();
        asnVBObject.add(vb.getOid());
        asnVBObject.add(vb.getValue());
    }

    return asnPduObject;
}

private byte[] getMsgFlags(SnmpContextv3 context) throws EncodingException
{
    byte authMask = (byte)(0x0);
    if (context.isUseAuthentication())
    {
        authMask = (byte)(0x1);
    }
    byte privMask = (byte)(0x0);
    if (context.isUsePrivacy())
    {
        if (context.isUseAuthentication())
        {
            privMask = (byte)(0x2);
        }
        else
        {
            throw new EncodingException("Encryption without authentication is not allowed");
        }
    }
    byte reportMask = (byte)(0x4);
    byte [] msgFlags = new byte[1];
    msgFlags[0] = (byte) (authMask | privMask | reportMask);
    return msgFlags;
}

}
