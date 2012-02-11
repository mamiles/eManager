// NAME
//      $RCSfile: OneGetNextPdu.java,v $
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
import java.util.*;

/**
 * <p>
 * The OneGetNextPdu class will ask for one (1) object (oid), based on
 * the GetNext request.
 * </p>
 *
 * <p>
 * Unless an exception occurred the Object to the update() method of the
 * Observer will be a varbind, so any AsnObject type can be returned.
 * In the case of an exception, that exception will be passed.
 * </p>
 *
 * @see varbind
 * @see InterfaceGetNextPdu
 * @see GetNextPdu_vec
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.11 $ $Date: 2002/10/10 15:26:15 $
 */
public class OneGetNextPdu extends GetNextPdu 
{
    private static final String     version_id =
        "@(#)$Id: OneGetNextPdu.java,v 3.11 2002/10/10 15:26:15 birgit Exp $ Copyright Westhawk Ltd";

    varbind var;

    /**
     * Constructor.
     *
     * @param con The context of the request
     */
    public OneGetNextPdu(SnmpContextBasisFace con)
    {
        super(con);
    }

    /**
     * Constructor that will send the request immediately. No Observer
     * is set.
     *
     * @param con the SnmpContextBasisFace
     * @param oid the oid 
     */
    public OneGetNextPdu(SnmpContextBasisFace con, String oid) 
    throws PduException, java.io.IOException
    {
        this(con, oid, null);
    }

    /**
     * Constructor that will send the request immediately. 
     *
     * @param con the SnmpContextBasisFace
     * @param oid the oid 
     * @param o the Observer that will be notified when the answer is received
     */
    public OneGetNextPdu(SnmpContextBasisFace con, String oid, Observer o) 
    throws PduException, java.io.IOException
    {
        super(con);
        if (o != null) 
        {
            addObserver(o);
        }
        addOid(oid);
        send();
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
            var = a_var;
        }
    }

    /**
     * The methods notifies all observers. 
     * This will be called by Pdu.fillin().
     * 
     * <p>
     * Unless an exception occurred the Object to the update() method of the
     * Observer will be a varbind, so any AsnObject type can be returned.
     * In the case of an exception, that exception will be passed.
     * </p>
     */
    protected void tell_them()  
    {
        notifyObservers(var);
    }

}
