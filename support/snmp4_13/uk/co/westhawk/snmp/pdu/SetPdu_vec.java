// NAME
//      $RCSfile: SetPdu_vec.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.11 $
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
import java.lang.*;

/**
 * <p>
 * The SetPdu_vec class will set the value of a number of 
 * objects (OIDs), based on the Set request.
 * </p>
 *
 * <p>
 * Specify with <em>addOid()</em> the OIDs that should be requested with this
 * Pdu request. No more than <em>count</em> (see constructor) should be added.
 * Add an Observer to the Pdu with <em>addObserver()</em>, and send the Pdu
 * with <em>send()</em>.
 * </p>
 *
 * <p>
 * If no exception occurred whilst receiving the response, the Object to the 
 * update() method of the Observer will be an array of
 * varbinds, so they may contains any AsnObject type.
 * If an exception occurred, that exception will be passed as the Object
 * to the update() method.
 * </p>
 *
 * @see SetPdu#addOid
 * @see Pdu#send
 * @see varbind
 * @see OneSetPdu
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.11 $ $Date: 2002/10/10 15:26:15 $
 */
public class SetPdu_vec extends SetPdu 
{
    private static final String     version_id =
        "@(#)$Id: SetPdu_vec.java,v 3.11 2002/10/10 15:26:15 birgit Exp $ Copyright Westhawk Ltd";

    varbind[]  value;

    /**
     * Constructor.
     *
     * @param con The context of the request
     * @param count The number of OIDs to be get
     */
    public SetPdu_vec(SnmpContextBasisFace con, int count) 
    {
        super(con);
        value = new varbind[count];
    }

    /**
     * The value of the request is set. This will be called by
     * Pdu.fillin(). These are the values of the OIDs after the Set request
     * was done. If the SNMP server allowed the sets, these will be the
     * same values as was set in SetPdu.addOid().
     *
     * @param n the index of the value
     * @param a_var the value
     * @see Pdu#new_value 
     * @see SetPdu#addOid(String, AsnObject)
     */
    protected void new_value(int n, varbind var) 
    {
        if (n <value.length) 
        {
            value[n] = var;
        }
    }

    /**
     * The methods notifies all observers. 
     * This will be called by Pdu.fillin().
     * 
     * <p>
     * If no exception occurred whilst receiving the response, the
     * Object to the update() method of the Observer will be an array of
     * varbinds, so they may contains any AsnObject type.  If an
     * exception occurred, that exception will be passed as the Object
     * to the update() method. 
     * </p>
     */
    protected void tell_them()  
    {
        notifyObservers(value);
    }
}
