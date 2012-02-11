// NAME
//      $RCSfile: TrapPduv1.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.8 $
// CREATED
//      $Date: 2002/10/10 15:13:57 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 2001, 2002 by Westhawk Ltd
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
import java.io.*;
import java.net.*;

import uk.co.westhawk.snmp.util.*;

/**
 * This class represents the ASN SNMPv1 Trap Pdu object. 
 * See <a href="http://ietf.org/rfc/rfc1157.txt">RFC 1157</a>.
 * 
 * <p>
 * Note that the SNMPv1 Trap Pdu is the only Pdu with a different Pdu
 * format. It has additional fields like <code>enterprise</code>,
 * <code>ipAddress</code>, <code>genericTrap</code>,
 * <code>specificTrap</code> and <code>timeTicks</code>.
 * </p>
 *
 * @see TrapPduv2
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.8 $ $Date: 2002/10/10 15:13:57 $
 */
public abstract class TrapPduv1 extends Pdu 
{
    private static final String     version_id =
        "@(#)$Id: TrapPduv1.java,v 3.8 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    private String enterprise;
    private byte[] IpAddress;
    private int generic_trap;
    private int specific_trap;
    private long timeTicks;

    private final static String [] genericTrapStrings =
    {
        "Cold Start",
        "Warm Start",
        "Link Down",
        "Link Up",
        "Authentication Failure",
        "EGP Neighbor Loss",
        "Enterprise Specific",
    };

/** 
 * Constructor.
 *
 * @param con The context v1 of the TrapPduv1
 * @see SnmpContext
 */
public TrapPduv1(SnmpContext con) 
{
    super(con);
    setMsgType(AsnObject.TRP_REQ_MSG);

    generic_trap = AsnObject.SNMP_TRAP_WARMSTART;
}

/**
 * Sets the type of object generating the trap. This parameter is based on
 * the sysObjectId.
 */
public void setEnterprise(String var)
{
    enterprise = var;
}
/**
 * Returns the type of object generating the trap. 
 */
public String getEnterprise()
{
    return enterprise;
}

/**
 * Sets the IP Address of the object generating the trap.
 */
public void setIpAddress(byte[] var)
{
    IpAddress = var;
}
/**
 * Returns the IP Address of the object generating the trap.
 */
public byte[] getIpAddress()
{
    return IpAddress;
}

/**
 * Sets the generic trap type. By default the warmStart is sent.
 *
 * @see SnmpConstants#SNMP_TRAP_COLDSTART
 * @see SnmpConstants#SNMP_TRAP_WARMSTART
 * @see SnmpConstants#SNMP_TRAP_LINKDOWN
 * @see SnmpConstants#SNMP_TRAP_LINKUP
 * @see SnmpConstants#SNMP_TRAP_AUTHFAIL
 * @see SnmpConstants#SNMP_TRAP_EGPNEIGHBORLOSS
 * @see SnmpConstants#SNMP_TRAP_ENTERPRISESPECIFIC
 */
public void setGenericTrap(int var)
{
    generic_trap = var;
}
/**
 * Returns the generic trap type. 
 */
public int getGenericTrap()
{
    return generic_trap;
}
/**
 * Returns the string representation of the generic trap.
 *
 * @return the generic trap string
 * @see #getGenericTrap
 */
public String getGenericTrapString()
{
    String trapStr = "";
    if (generic_trap > -1 && generic_trap < genericTrapStrings.length)
    {
        trapStr = genericTrapStrings[generic_trap];
    }
    return trapStr;
}

/**
 * Sets the specific trap code.
 */
public void setSpecificTrap(int var)
{
    specific_trap = var;
}
/**
 * Returns the specific trap code.
 */
public int getSpecificTrap()
{
    return specific_trap;
}

/**
 * Sets the sysUpTime of the agent.
 */
public void setTimeTicks(long var)
{
    timeTicks = var;
}
/**
 * Returns the sysUpTime of the agent.
 */
public long getTimeTicks()
{
    return timeTicks;
}


/** 
 * Send the TrapPduv1. Since the Trap v1 Pdu has a different format
 * (sigh), a different encoding message has to be called.
 *
 * Note that all properties of the context have to be set before this
 * point.
 */
public boolean send() throws java.io.IOException, PduException
{
    if (added == false)
    {
        // Moved this statement from the constructor because it
        // conflicts with the way the SnmpContextXPool works.
        added = context.addPdu(this);
    }
    Enumeration vbs = reqVarbinds.elements();
    encodedPacket = ((SnmpContext)context).encodePacket(msg_type, enterprise,
        IpAddress, generic_trap, specific_trap, timeTicks, vbs);
    addToTrans();
    return added;  
}

/**
 * The trap Pdu does not get a response back. So it should be sent once.
 */
void transmit() 
{
    transmit(false);
}

/**
 * Returns the string representation of this object.
 *
 * @return The string of the Pdu
 */
public String toString()
{
    String iPAddressStr = "null";
    if (IpAddress != null)
    {
        iPAddressStr = (new AsnOctets(IpAddress)).toIpAddress();
    }
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("[");
    buffer.append("context=").append(context);
    buffer.append(", reqId=").append(getReqId() );
    buffer.append(", msgType=0x").append(SnmpUtilities.toHex(msg_type));
    buffer.append(", enterprise=").append(enterprise);
    buffer.append(", IpAddress=").append(iPAddressStr);
    buffer.append(", generic_trap=").append(getGenericTrapString());
    buffer.append(", specific_trap=").append(specific_trap);
    buffer.append(", timeTicks=").append(timeTicks);

    buffer.append(", reqVarbinds=");
    if (reqVarbinds != null)
    {
        int sz = reqVarbinds.size();
        buffer.append("[");
        for (int i=0; i<sz; i++)
        {
            varbind var = (varbind) reqVarbinds.elementAt(i);
            buffer.append(var.toString()).append(", ");
        }
        buffer.append("]");
    }
    else
    {
        buffer.append("null");
    }

    buffer.append(", respVarbinds=");
    if (respVarbinds != null)
    {
        int sz = respVarbinds.size();
        buffer.append("[");
        for (int i=0; i<sz; i++)
        {
            varbind var = (varbind) respVarbinds.elementAt(i);
            buffer.append(var.toString()).append(", ");
        }
        buffer.append("]");
    }
    else
    {
        buffer.append("null");
    }

    buffer.append("]");
    return buffer.toString();
}

/**
 * Fill in the received trap. 
 * @see Pdu#getResponseVarbinds()
 */
void fillin(AsnTrapPduv1Sequence seq) 
{
    if (seq != null)
    {
        try
        {
            AsnSequence varBind = seq.getVarBind();
            int size = varBind.getObjCount();
            respVarbinds = new Vector(size, 1);
            for (int n=0; n<size; n++) 
            {
                AsnSequence varSeq = (AsnSequence) varBind.getObj(n);
                varbind vb = new varbind(varSeq);
                respVarbinds.addElement(vb);
            }

            setEnterprise(seq.getEnterprise());
            setIpAddress(seq.getIPAddress());
            setGenericTrap(seq.getGenericTrap());
            setSpecificTrap(seq.getSpecificTrap());
            setTimeTicks(seq.getTimeTicks());
        }
        catch (DecodingException exc)
        {
            setErrorStatus(AsnObject.SNMP_ERR_DECODINGASN_EXC, exc);
        }
    }
}

/**
 * Has no meaning, since there is not response.
 */
protected void new_value(int n, varbind res){}

/**
 * Has no meaning, since there is not response.
 */
protected void tell_them(){}

}
