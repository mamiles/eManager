// NAME
//      $RCSfile: InterfacesPdu.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.9 $
// CREATED
//      $Date: 2002/10/10 15:26:15 $
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
 * The InterfacesPdu class will ask for the number of current interfaces.
 * For each interface it will send an InterfacePdu to get the
 * information of the specific interface.
 *
 * @see InterfacePdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a
 * @version $Revision: 3.9 $ $Date: 2002/10/10 15:26:15 $
 *
 */
public class InterfacesPdu extends InterfacePdu
{
    private static final String     version_id =
        "@(#)$Id: InterfacesPdu.java,v 3.9 2002/10/10 15:26:15 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * ifNumber
     * The number of network interfaces (regardless of their current state) 
     * present on this system.
     */
    final static String IFNUMBER      ="1.3.6.1.2.1.2.1.0";

    InterfacePdu [] ifs;


/**
 * Constructor that will send the request immediately.
 *
 * @param con the SnmpContextBasisFace
 * @param o the Observer that will be notified when the answer is received
 * @param interf the index of the requested interface
 */
public InterfacesPdu(SnmpContextBasisFace con, Observer o, int interfs) 
throws PduException, java.io.IOException
{
    super(con);
    ifs = new InterfacePdu [interfs];
    for (int interf=0; interf < interfs; interf++)
    {
        addOids(interf);
        ifs[interf] = new InterfacePdu(con);
    }
    if (o!=null) 
    {
        addObserver(o);
    }
    send();
}

/**
 * Returns how many interfaces are present.
 *
 * @return the number of interfaces
 */
public static int getNumIfs(SnmpContextBasisFace con)
throws PduException, java.io.IOException
{
    int ifCount =0;

    if (con != null)
    {
        OneIntPdu numIfs = new OneIntPdu(con, IFNUMBER);
        if (numIfs.waitForSelf())
        {
            ifCount = numIfs.getValue().intValue();
        }
    }
    return ifCount; 
} 

/**
 * Returns the interfaces.
 *
 * @return the interfaces as an array of InterfacePdu
 */
public InterfacePdu [] getInterfacePdus()
{
    return ifs;
}

/**
 * The value of the request is set. This will be called by
 * Pdu.fillin().
 *
 * @param n the index of the value
 * @param a_var the value
 * @see Pdu#new_value 
 */
protected void new_value(int n, varbind res)  
{
    int thif = n / 4;
    if (thif < ifs.length)
    {
        ifs[thif].new_value(n%4,res);
    }
}


}

