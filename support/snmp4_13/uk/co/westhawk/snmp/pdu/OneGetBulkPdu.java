// NAME
//      $RCSfile: OneGetBulkPdu.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.6 $
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
 
package uk.co.westhawk.snmp.pdu;
import uk.co.westhawk.snmp.stack.*;
import java.util.*;

/**
 * <p>
 * The OneGetBulkPdu class performs a getBulkRequest and collects 
 * the response varbinds into a Vector.
 * </p>
 *
 * <p>
 * If no exception occurred whilst receiving the response, the Object to the 
 * update() method of the Observer will be an Vector of
 * varbinds, so they may contains any AsnObject type.
 * If an exception occurred, that exception will be passed as the Object
 * to the update() method.
 * </p>
 *
 * @see varbind
 * @see java.util.Vector
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.6 $ $Date: 2002/10/10 15:13:57 $
 */
public class OneGetBulkPdu extends GetBulkPdu 
{
    private static final String     version_id =
        "@(#)$Id: OneGetBulkPdu.java,v 3.6 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * The GetBulk request is the only request that will return more
     * variables than you've sent.
     */
    java.util.Vector vars;

/**
 * Constructor.
 *
 * @param con The context of the request
 */
public OneGetBulkPdu(SnmpContextBasisFace con)
{
    super(con);
    vars = new java.util.Vector();
}


/**
 * Returns a vector with the response varbinds.
 */
public java.util.Vector getVarbinds()
{
    return vars;
}


/**
 * The value of the request is set. This will be called by
 * Pdu.fillin().
 *
 * @param n the index of the value
 * @param a_var the value
 * @see Pdu#new_value 
 */
protected void new_value(int n, varbind a_var) 
{
    if (n == 0) 
    {
        vars = new java.util.Vector();
    }
    vars.addElement(a_var);
}

/**
 * The methods notifies all observers. 
 * This will be called by Pdu.fillin().
 * 
 * <p>
 * If no exception occurred whilst receiving the response, the Object to the 
 * update() method of the Observer will be an Vector of
 * varbinds, so they may contains any AsnObject type.
 * If an exception occurred, that exception will be passed as the Object
 * to the update() method.
 * </p>
 *
 * @see java.util.Vector
 */
protected void tell_them()  
{
    notifyObservers(vars);
}

}
