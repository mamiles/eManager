// NAME
//      $RCSfile: InterfaceGetNextPdu.java,v $
// DESCRIPTION
//      [given below inOctets javadoc format]
// DELTA
//      $Revision: 3.14 $
// CREATED
//      $Date: 2002/10/29 14:44:40 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 1996, 1997 by Westhawk Ltd (www.westhawk.co.uk)
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
 
package uk.co.westhawk.snmp.pdu;
import uk.co.westhawk.snmp.stack.*;
import java.util.*;


/**
 * <p>
 * The InterfaceGetNextPdu class can be used collect information off all 
 * current interfaces.
 * </p>
 *
 * <p>
 * To get the successor (GetNext) of a request, the application must 
 * build the new request with the previous one. This is done by calling 
 * addOids(InterfaceGetNextPdu) before sending it. 
 * </p>
 *
 * <p>
 * This class can best be used inOctets a loop. To start the loop, build the
 * Pdu with addOids(), when the answer is received, request the next
 * interface by building the Pdu with addOids(InterfaceGetNextPdu).
 * The loops ends when the errorStatus equals
 * AsnObject.SNMP_ERR_NOSUCHNAME
 * </p>
 *
 * <p>
 * The requested OID are from the 
 * <a href="http://ietf.org/rfc/rfc2863.txt">RFC 2863</a>.
 * </p>
 *
 * @see #addOids()
 * @see #addOids(InterfaceGetNextPdu)
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.14 $ $Date: 2002/10/29 14:44:40 $
 */
public class InterfaceGetNextPdu extends GetNextPdu_vec
{
    private static final String     version_id =
        "@(#)$Id: InterfaceGetNextPdu.java,v 3.14 2002/10/29 14:44:40 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * ifNumber
     * The number of network interfaces (regardless of their current state) 
     * present on this system.
     */
    public final static String IFNUMBER      ="1.3.6.1.2.1.2.1.0";

    /**
     * sysUpTime
     * The time (inOctets hundredths of a second) since the network management 
     * portion of the system was last re-initialized.
     */
    public final static String SYS_UPTIME    ="1.3.6.1.2.1.1.3";

    /**
     * ifIndex
     * A unique value for each interface.  Its value
     * ranges between 1 and the value of ifNumber.  The
     * value for each interface must remain constant at
     * least from one re-initialization of the entity's
     * network management system to the next re-
     * initialization.
     */
    public final static String INDEX         ="1.3.6.1.2.1.2.2.1.1";

    /**
     * ifDescr
     * A textual string containing information about the
     * interface.  This string should include the name of
     * the manufacturer, the product name and the version
     * of the hardware interface.
     */
    public final static String DESCR         ="1.3.6.1.2.1.2.2.1.2";

    /**
     * ifOperStatus
     * The current operational state of the interface.
     * The testing(3) state indicates that no operational
     * packets can be passed.
     */
    public final static String OPR_STATUS    ="1.3.6.1.2.1.2.2.1.8";

    /**
     * ifInOctets
     * The total number of octets received on the
     * interface, including framing characters.
     */
    public final static String IN_OCTETS     ="1.3.6.1.2.1.2.2.1.10";

    /**
     * ifOutOctets
     * The total number of octets transmitted outOctets of the
     * interface, including framing characters.
     */
    public final static String OUT_OCTETS    ="1.3.6.1.2.1.2.2.1.16";

    /**
     * The current operational state is up
     */
    final public static String UP       = "up";
    /**
     * The current operational state is down
     */
    final public static String DOWN     = "down";
    /**
     * The current operational state is testing
     */
    final public static String TESTING  = "testing";
    /**
     * The current operational state is unknown
     */
    final public static String UNKNOWN  = "unknown";

    final static int    NR_OID     = 6;
    final static String inf_oids[] =
    {
        SYS_UPTIME,
        INDEX,
        DESCR,
        OPR_STATUS,
        IN_OCTETS,
        OUT_OCTETS
    };

    private varbind[] inf_var;

    int index;
    long sysUpTime;
    int operStatus;
    long inOctets;
    long outOctets;
    long speed;
    String descr;
    boolean valid = false;


/**
 * Constructor.
 *
 * @param con The context of the request
 */
public InterfaceGetNextPdu(SnmpContextBasisFace con)
{
    super(con, NR_OID);
    inf_var = new varbind[NR_OID];
    for (int i=0; i<NR_OID; i++)
    {
        inf_var[i]=null;
    }
    speed = 0;
    valid = false;
}

/**
 * Returns the index of the interface.
 * @return the index
 * @see #INDEX
 */
public int getIndex()
{
    return index;
}

/**
 * Returns the description of the interface.
 * @return the description
 * @see #DESCR
 */
public String getDescription()
{
    return descr;
}

/**
 * Returns the operational state of the interface.
 * @return the operational state
 * @see #OPR_STATUS 
 */
public int getOperStatus()
{
    return operStatus;
}

/**
 * Returns the string representation of the operational state of the
 * interface.
 * @return the operational state as string
 * @see #getOperStatus() 
 * @see #getOperStatusString(int) 
 */
public String getOperStatusString()
{
    return getOperStatusString(operStatus);
}

/**
 * Returns the string representation of a operational state.
 * @see #getOperStatusString() 
 */
public String getOperStatusString(int status)
{
    String str = null;
    switch (status)
    {
        case 1:
            str = UP;
            break;
        case 2:
            str = DOWN;
            break;
        case 3:
            str = TESTING;
            break;
        default:
            str = UNKNOWN;
            break;
    }
    return str;
}


/**
 * Returns the speed of the interface.
 * @return the speed
 * @see #getSpeed(InterfaceGetNextPdu)
 */
public long getSpeed()
{
    return speed;
}

/**
 * Calculates the speed of the interface. This is done by providing the
 * method with <i>the previous value of this interface</i>. An interface 
 * is marked by its index. Do <i>not</i> confuse it 
 * with <i>the previous interface inOctets the MIB</i>.
 *
 * @param old The previous value of this interface 
 */
public long getSpeed(InterfaceGetNextPdu old)
{
    speed = -1;
    if ((this.operStatus <1) 
            || 
        (old.operStatus <1) 
            || 
        !this.valid 
            || 
        !old.valid)
    {
        return -1;
    }
    long tdif = (this.sysUpTime - old.sysUpTime);
    if (tdif != 0)
    {
        long inO = this.inOctets - old.inOctets;
        long outO = this.outOctets - old.outOctets;

        speed = 100 * (inO + outO) / tdif;
    }
    return speed;
}


/**
 * This method should be used when the variables are asked for the first
 * time, the start of the loop.
 *
 * @see #addOids(InterfaceGetNextPdu)
 */
public boolean addOids()
{
    for (int i=0; i<NR_OID; i++)
    {
        inf_var[i] = new varbind(inf_oids[i]);
        addOid(inf_var[i]);
    }
    return true;
}

/**
 *  The method addOids is the basic for the GetNext functionality.
 *  It copies the old reqVarbinds, except for SYS_UPTIME, so the request
 *  continues where the previous one left.
 *
 * @see #addOids()
 */
public void addOids(InterfaceGetNextPdu old)
{
    inf_var[0] = new varbind(SYS_UPTIME);

    for (int i=1; i<NR_OID; i++)
    {
        inf_var[i] = old.inf_var[i];
    }

    for (int i=0; i<NR_OID; i++)
    {
        addOid(inf_var[i]);
    }
}

/**
 * The value of the request is set. This will be called by
 * Pdu.fillin().
 *
 * I check if the variables are still in range.
 * I do this because I'm only interessed in a part of the MIB. If I
 * would not do this check, I'll get the whole MIB from the starting
 * point, instead of the variables in the ifTable.
 *
 * @param n the index of the value
 * @param a_var the value
 * @see Pdu#new_value 
 */
protected void new_value(int n, varbind res)
{
    inf_var[n] = res;

    if (getErrorStatus() == AsnObject.SNMP_ERR_NOERROR)
    {
        if (inf_var[n].getOid().toString().startsWith(inf_oids[n]))
        {
            try
            {
                switch (n)
                {
                    case 0:
                        sysUpTime = ((AsnUnsInteger) 
                                      inf_var[n].getValue()).getValue();
                        break;
                    case 1:
                        index = ((AsnInteger) inf_var[n].getValue()).getValue();
                        break;
                    case 2:
                        descr = ((AsnOctets) inf_var[n].getValue()).getValue();
                        break;
                    case 3:
                        operStatus = ((AsnInteger)
                                      inf_var[n].getValue()).getValue();
                        break;
                    case 4:
                        inOctets  = ((AsnUnsInteger)
                                    inf_var[n].getValue()).getValue();
                        break;
                    case 5:
                        outOctets = ((AsnUnsInteger)
                                    inf_var[n].getValue()).getValue();
                        valid = true;
                        break;
                    default:
                        valid = false;
                }
            }
            catch(ClassCastException exc)
            {
                descr = null;
                index = 0;
                sysUpTime = 0;
                operStatus = 0;
                inOctets = 0;
                outOctets = 0;
                valid = false;
            }
        }
        else
        {
            setErrorStatus(AsnObject.SNMP_ERR_NOSUCHNAME);
            setErrorIndex(n);
        }
    }
}

/**
 * Returns the string represenation of this request.
 *
 * @return the string
 */
public String toString()
{
    String str = new String();

    for (int i=0; i<NR_OID; i++)
    {
        str = str + inf_var[i] + "\n";
    }
    return str;
}


/**
 * Method to discover how many interfaces are present.
 * @return the number of interfaces
 */
public static int getNumIfs(SnmpContextBasisFace con)
throws PduException, java.io.IOException
{
    int ifNumber=0;

    if (con != null)
    {
        OneIntPdu numIfs = new OneIntPdu(con, IFNUMBER);
        if (numIfs.waitForSelf())
        {
            ifNumber = numIfs.getValue().intValue();
        }
    }
    return ifNumber;
}

}

