// NAME
//      $RCSfile: Transmitter.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.12 $
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

package uk.co.westhawk.snmp.stack;

import java.util.*;

/**
 * Transmitter is a thread that sends pdus, when done
 * it waits.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.12 $ $Date: 2002/10/10 15:26:15 $
*/
class Transmitter extends Object implements Runnable
{
    private static final String     version_id =
        "@(#)$Id: Transmitter.java,v 3.12 2002/10/10 15:26:15 birgit Exp $ Copyright Westhawk Ltd";

    Pdu pdu = null;
    Thread me;
    String myName;

    Transmitter(String name)
    {
        me = new Thread(this,name);
        me.setPriority(me.MIN_PRIORITY);
        if (AsnObject.debug > 12)
        {
            System.out.println("Transmitter(): Made thread " + name);
        }
        myName =name;
        me.start();
    }

    /**
     * The method for the Runnable interface.
     * 
     * It will do a sit() untill stand() wakes it up. The Pdu will then
     * be transmitted
     *
     * @see #sit()
     * @see #stand()
     * @see Pdu#transmit()
     */
    public void run()
    {
        while (me != null)
        {
            sit();
            synchronized (this) 
            {
                if (pdu != null)
                {
                    pdu.transmit();
                    // I will say this only once....
                    pdu = null; 
                }
            }
        }
    }

    /** 
     * Returns the string representation of the Transmitter.
     *
     * @return The string of the Transmitter
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer(getClass().getName());
        buffer.append("[");
        buffer.append("name=").append(myName);
        buffer.append("]");
        return buffer.toString();
    }

    synchronized void setPdu(Pdu p)
    {
        pdu = p;
    }

    /**
     * This method is the counterpart of stand().
     *
     * It does not more than waiting to be notified.
     *
     * @see #stand()
     * @see #run()
     */
    synchronized void sit()
    {
        while ((me != null) && (pdu == null))
        {
            try
            {
                wait();
            }
            catch (InterruptedException iw)
            {
                ;
            }

        }
    }

    /**
     * This method is the counterpart of sit().
     *
     * The Pdu will call this method when it is sent.
     * This method will notify itself and in the end it is transmitted
     * in run.
     *
     * @see #sit()
     * @see #run()
     * @see Pdu#send
     */
    synchronized void stand()
    {
        notifyAll();
    }

    /**
     * It may be sleeping (as opposed to wait()ing
     * so send it a kick.
     */
    void interruptMe()
    {
        // unsafe ?
        me.interrupt();
    }

    void destroy()
    {
        me = null;
        pdu=null;
        stand();
    }
}

