// NAME
//      $RCSfile: UpSincePdu.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.13 $
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
 * The UpSincePdu class will send a Get request for the sysUpTime and
 * will calculate that date the system rebooted the last time.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.13 $ $Date: 2002/10/10 15:26:15 $
 */
public class UpSincePdu extends Pdu
{
    private static final String     version_id =
        "@(#)$Id: UpSincePdu.java,v 3.13 2002/10/10 15:26:15 birgit Exp $ Copyright Westhawk Ltd";

    Date since;
    /**
     * The oid of sysUpTime
     */
    public final static String SYSUPTIME="1.3.6.1.2.1.1.3.0";

    /**
     * Constructor that will send the request immediately.
     *
     * @param con The context of the request
     * @param o the Observer that will be notified when the answer is received
     */
    public UpSincePdu(SnmpContextBasisFace con, Observer o) 
    throws PduException, java.io.IOException
    {
        super(con);
        addOid(SYSUPTIME);
        if (o != null)
        {
            addObserver(o);
        }
        send();
    }


    /**
     * Returns the date when the system went up, (sysUpTime).
     * @return the date
     */
    public Date getDate()
    {
        return since;
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
        // given the uptime in centi seconds and the time now,
        // calculate the time it rebooted
    
        AsnObject val = res.getValue();
        if (val instanceof AsnUnsInteger)
        {
            AsnUnsInteger va = (AsnUnsInteger) res.getValue();
            if (n == 0) 
            {
                long value = va.getValue();
                Date now = new Date();
                long then = now.getTime();
                then -= 10 * value;
                since = new Date(then);
            }
        }
        else
        {
            since = null;
        }
    }

    /**
     * The methods notifies all observers. 
     * This will be called by Pdu.fillin().
     * 
     * <p>
     * Unless an exception occurred the Object to the update() method of the
     * Observer will be a Date.
     * In the case of an exception, that exception will be passed.
     * </p>
     */
    protected void tell_them()  
    {
        notifyObservers(since);
    }

}

