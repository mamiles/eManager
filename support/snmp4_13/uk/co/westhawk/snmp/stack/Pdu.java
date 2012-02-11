// NAME
//      $RCSfile: Pdu.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.25 $
// CREATED
//      $Date: 2002/10/23 09:48:01 $
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
import java.util.*;
import java.io.*;

import uk.co.westhawk.snmp.util.*;

/**
 * This class represents the ASN Pdu object, this is the equivalent of
 * a GetRequest Pdu.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.25 $ $Date: 2002/10/23 09:48:01 $
 */
public abstract class Pdu extends Observable 
{
    private static final String     version_id =
        "@(#)$Id: Pdu.java,v 3.25 2002/10/23 09:48:01 birgit Exp $ Copyright Westhawk Ltd";

    protected Vector    reqVarbinds = null;
    protected Vector    respVarbinds = null;

    private final static String TIMED_OUT = "Timed out";
    private final static String [] errorStrings =
    {
        "No error",
        "Value too big error",
        "No such name error",
        "Bad value error",
        "Read only error",
        "General error",
        "No access error",
        "Wrong type error",
        "Wrong length error",
        "Wrong encoding error",
        "Wrong value error",
        "No creation error",
        "Inconsistent value error",
        "Resource unavailable error",
        "Commit failed error",
        "Undo failed error",
        "Authorization error",
        "Not writable error",
        "Inconsistent name error",
    };

    private static int  next_id = 1;
    
    private int retry_intervals[] = {500,1000,2000,5000,5000};

    protected byte[]    encodedPacket = null;
    protected SnmpContextBasisFace context;
    protected boolean   added = false;
    protected byte      msg_type;

    private int         req_id;
    protected int       errstat;
    protected int       errind;

    private Transmitter trans = null;
    private int         retries;
    protected boolean   answered;
    private boolean     got = false;
    private boolean     isTimedOut;
    private PduException respException = null;

/**
 * The value of the request is set. This will be called by
 * Pdu.fillin().
 */
protected abstract void new_value(int n, varbind res);

/**
 * The methods notifies all observers.
 * This will be called by Pdu.fillin().
 *
 * <p>
 * The Object to the update() method of the Observer will be a varbind, 
 * unless an exception occurred. 
 * In the case of an exception, that exception will be passed.
 * So watch out casting!
 * </p>
 */
protected abstract void tell_them();

/** 
 * Constructor.
 *
 * @param con The context of the Pdu
 * @see SnmpContext
 * @see SnmpContextv2c
 * @see SnmpContextv3
 */
public Pdu(SnmpContextBasisFace con) 
{
    context = con;

    // TODO: would not work if we ever were to send response or report!
    // TODO: We would have to set the req_id!
    req_id  = next_id; 
    errstat = AsnObject.SNMP_ERR_NOERROR;
    errind  = 0x00;
    next_id++;
    reqVarbinds = new Vector(1,1);
    setMsgType(AsnObject.GET_REQ_MSG);
    isTimedOut = false;

    // this is a lie, but since we have not sent
    // anything yet, an answer is an error
    answered = true; 
}

/**
 * Returns the context of this pdu.
 * @return The context
 */
public SnmpContextBasisFace getContext()
{
    return context;
}

/** 
 * Sets the retry intervals of the Pdu. The length of the array
 * corresponds with the number of retries. Each entry in the array
 * is the number of milliseconds of each try.
 *
 * Please use this method before sending.
 *
 * The default is {500, 1000, 2000, 5000, 5000}.
 * It is good practice to make the interval bigger with each retry,
 * if the numbers are the same the chance of collision is bigger.
 *
 * @param rinterval The interval in msec of each retry
 */
public void setRetryIntervals(int rinterval[])
{
    retry_intervals = rinterval;
}

/** 
 * Send the Pdu.
 * Note that all properties of the context have to be set before this
 * point.
 */
public boolean send() throws java.io.IOException, PduException
{
    return send(errstat, errind);
}

/** 
 * Send the Pdu. This method accommodates the GetBulk request.
 *
 * @param error_status The value of the error_status field.
 * @param error_index The value of the error_index field.
 * @see #send()
 */
protected boolean send(int error_status, int error_index) 
throws java.io.IOException, PduException
{
    if (added == false)
    {
        // Moved this statement from the constructor because it
        // conflicts with the way the SnmpContextXPool works.
        added = context.addPdu(this);
    }
    Enumeration vbs = reqVarbinds.elements();
    encodedPacket = context.encodePacket(msg_type, req_id, error_status, 
        error_index, vbs);
    addToTrans();
    return added;  
}

/**
 * Adds the Pdu to its transmitter. The transmitter is the thread that
 * will send the Pdu and then waits for the answer.
 * @see #send
 */
protected void addToTrans()
{
    if (added && (trans != null))
    {
        trans.setPdu(this);
        trans.stand();
    }
}

/**
 * Sends the actual packet and updates the retries.
 *
 * @see AbstractSnmpContext#sendPacket(byte[] p)
 */
protected boolean sendme() 
{
    context.sendPacket(encodedPacket);
    retries ++;

    if (AsnObject.debug > 6)
    {
        System.out.println("Pdu.sendme(): Sent Pdu reqId " +req_id + ", retries "+retries);
    }
    return (retries > 3);
}


/** 
 * Send the Pdu.
 * For backwards compatibility only. Please use send() instead.
 * The community name will be passed to the SnmpContext. If using
 * SnmpContextv3, the community name will be ignored.
 *
 * @param com The community name of the Pdu in SNMPv1 and SNMPv2c.
 * @deprecated Community name has moved to SnmpContext. Use send().
 *
 * @see SnmpContext#setCommunity
 * @see #send
 */
public boolean send(String com) throws java.io.IOException, PduException
{
    if (context instanceof SnmpContext)
    {
        ((SnmpContext)context).setCommunity(com);
    }
    return send();
}

/** 
 * Add an OID (object identifier) to the Pdu. The OID indicates WHAT
 * MIB variable we request for or WHAT MIB variable should be set.
 *
 * @param oid The oid 
 * @see #addOid(varbind)
 * @see varbind
 */
public void addOid(String oid) 
{
    varbind vb = new varbind(oid);
    addOid(vb);
}

/** 
 * Add an OID (object identifier) to the Pdu. The OID indicates WHAT
 * MIB variable we request for or WHAT MIB variable should be set.
 *
 * @param oid The oid 
 * @see #addOid(varbind)
 * @see varbind
 * @since 4_12
 */
public void addOid(AsnObjectId oid) 
{
    varbind vb = new varbind(oid);
    addOid(vb);
}

/** 
 * Add an OID (object identifier) to the Pdu and the value that has
 * to be set. This method has moved from SetPdu to this class in version
 * 4_12.
 *
 * @param oid The oid 
 * @param val The value 
 * @see Pdu#addOid
 * @see varbind
 * @since 4_12
 */
public void addOid(String oid, AsnObject val) 
{
    varbind vb = new varbind(oid, val);
    addOid(vb);
}

/** 
 * Add an OID (object identifier) to the Pdu and the value that has
 * to be set. 
 *
 * <p>
 * Thanks to Eli Bishop (eli@graphesthesia.com) for the suggestion.
 * </p>
 *
 * @param oid The oid 
 * @param val The value 
 * @see Pdu#addOid
 * @see varbind
 * @since 4_12
 */
public void addOid(AsnObjectId oid, AsnObject val) 
{
    varbind vb = new varbind(oid, val);
    addOid(vb);
}

/** 
 * Add an OID (object identifier) to the Pdu. 
 *
 * @param var The varbind 
 * @see #addOid(String)
 */
public void addOid(varbind var)
{
    reqVarbinds.addElement(var);
}

/**
 * Returns a copy of the varbinds used to build the request.
 *
 * @returns the request varbinds of this pdu.
 */
public varbind[] getRequestVarbinds()
{
    int sz = reqVarbinds.size();
    varbind[] arr = new varbind[sz];
    reqVarbinds.copyInto(arr);
    return arr;
}

/**
 * Returns a copy of the varbinds received in the response.
 * If there was no response (yet), null will be returned.
 *
 * @returns the response varbinds of this pdu.
 * @exception PduException An agent or decoding exception occurred
 * whilst receiving the response.
 *
 * @see #getErrorStatus
 * @see #notifyObservers
 */
public varbind[] getResponseVarbinds() throws PduException
{
    if (respException != null)
    {
        throw respException;
    }

    varbind[] arr = null;
    if (respVarbinds != null)
    {
        int sz = respVarbinds.size();
        arr = new varbind[sz];
        respVarbinds.copyInto(arr);
    }
    return arr;
}

private void dump(Vector v, varbind[] array)
{
    int sz = v.size();
    System.out.println("Vector: ");
    for (int i=0; i<sz; i++)
    {
        System.out.println("\t" + v.elementAt(i));
    }
    System.out.println("Array: ");
    for (int i=0; i<array.length; i++)
    {
        System.out.println("\t" + array[i]);
    }
    System.out.println("--");
}

void setResponseException(PduException exc)
{
    if (AsnObject.debug > 6)
    {
        System.out.println("Pdu.setResponseException(): " + exc.getMessage());
    }
    respException = exc;
}

/** 
 * Returns the request id of the Pdu.
 *
 * @return The ID
 */
public int getReqId() 
{
    return req_id;
}

/** 
 * Returns the error index.
 *
 * @return the error index
 * @see #getErrorStatus
 */
public int getErrorIndex()
{
    return errind;
}

/** 
 * Returns the error status as indicated by the error-status field in
 * the reponse pdu. 
 * In case of a decoding exception the error status
 * will be set to one of the decoding errors:
 * <ul>
 *   <li>
 * <code>SnmpConstants.SNMP_ERR_DECODING_EXC</code>.
 *   </li>
 *   <li>
 * <code>SnmpConstants.SNMP_ERR_DECODINGASN_EXC</code>.
 *   </li>
 *   <li>
 * <code>SnmpConstants.SNMP_ERR_DECODINGPKTLNGTH_EXC</code>.
 *   </li>
 * </ul>
 *
 * <p>
 * The actual exception will be passed to your 
 * <code>update(Observable ob, Object arg)</code>
 * method via the the parameter 
 * <code>arg</code>.
 * </p>
 *
 * @return the error status
 * @see #notifyObservers
 * @see #getResponseVarbinds
 * @see SnmpConstants#SNMP_ERR_NOERROR
 * @see SnmpConstants#SNMP_ERR_DECODING_EXC
 * @see SnmpConstants#SNMP_ERR_DECODINGASN_EXC
 * @see SnmpConstants#SNMP_ERR_DECODINGPKTLNGTH_EXC
 * @see #getErrorStatusString
 */
public int getErrorStatus()
{
    return errstat;
}

/** 
 * Returns the string representation of the error status.
 *
 * @return the error string
 * @see #getErrorStatus
 */
public String getErrorStatusString()
{
    String errString = "";
    if (errstat >= 0)
    {
        if (errstat < errorStrings.length)
        {
            errString = errorStrings[errstat];

            if (errstat == AsnObject.SNMP_ERR_GENERR 
                    && 
                isTimedOut() == true)
            {
                errString = TIMED_OUT;
            }
        }
        else
        {
            // they much be one of the DECODING*_EXC
            if (respException != null)
            {
                errString = respException.getMessage();
            }
            else
            {
                errString = "Decoding Exception";
            }
        }
    }
    return errString;
}

/** 
 * Returns whether or not this Pdu is timed out, i.e. it did not get
 * a response. 
 * Its errorStatus will be set to AsnObject.SNMP_ERR_GENERR .
 *
 * Note that a SNMP agent can respond with an errorStatus of 
 * AsnObject.SNMP_ERR_GENERR.
 *
 * @return true is the Pdu was timed out 
 * @see #getErrorStatus
 * @see SnmpConstants#SNMP_ERR_GENERR
 */
public boolean isTimedOut()
{
    return isTimedOut;
}


/**
 * This method will wait until the answer is received, instead of
 * continue with other stuff.
 */
public boolean waitForSelf()
{
    // Add an extra second to the waiting. This gives the Pdu a change
    // to handle the timeout correctly before this tread wakes up.
    long del = 1000;
    for (int i=0; i< retry_intervals.length; i++)
    {
        del += retry_intervals[i];
    }
    boolean res = waitForSelf(del);

    if (AsnObject.debug > 6)
    {
        System.out.println("Pdu.waitForSelf(): " + res);
    }

    // Should I??
    if (!answered) 
    {
        handleNoAnswer();
    }

    return res;
}

/** 
 * Returns the string representation of the Pdu.
 *
 * @return The string of the Pdu
 */
public String toString()
{
    return toString(false);
}

/**
 * Returns the string representation of the Pdu with or without the
 * response varbinds.
 *
 * @param withRespVars Include the response varbinds or not
 * @return The string of the Pdu
 */
protected String toString(boolean withRespVars)
{
    StringBuffer buffer = new StringBuffer(getClass().getName());
    buffer.append("[");
    buffer.append("context=").append(context);
    buffer.append(", reqId=").append(req_id);
    buffer.append(", msgType=0x").append(SnmpUtilities.toHex(msg_type));
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

    if (withRespVars == true)
    {
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
        }
        else
        {
            buffer.append("null");
        }
    }

    buffer.append("]");
    return buffer.toString();
}

synchronized boolean waitForSelf(long delay)
{
    if (!got)
    {
        try 
        {
            wait(delay);
        } 
        catch (InterruptedException ix) 
        {
            ;
        }
    }
    return answered;
}

void transmit() 
{
    transmit(true);
}

void transmit(boolean withRetries) 
{
    if (withRetries == true)
    {
        int n=0;
        answered=false;

        while (!answered && n<retry_intervals.length)
        {
            sendme();

            try 
            {
                Thread.sleep(retry_intervals[n]);
            }
            catch (java.lang.InterruptedException e) {}
            n++;
        }        

        if (!answered) 
        {
            handleNoAnswer();
        }
    }
    else
    {
        // just send it once. this will only happen if we are in a trap
        // pdu.
        sendme();
        answered=true;
    }

    if (!context.removePdu(req_id))
    {
        if (AsnObject.debug > 6)
        {
            System.out.println("Pdu.transmit(): Failed to remove "+req_id);
        }
    }
}

void setTrans(Transmitter t)
{
    trans = t;
}


/** 
 * Returns the message type, this will indicate what kind of request we
 * are dealing with. 
 * By default it will be set to the GET_REQ_MSG
 *
 * @return The message type 
 */
public byte getMsgType()
{
    return msg_type;
}

/** 
 * Sets the message type, this will indicate what kind of request we
 * are dealing with. 
 * By default it will be set to the GET_REQ_MSG
 *
 * @param type The message type 
 */
protected void setMsgType(byte type)
{
    msg_type = type;
}

/**
 * Sets the error status, indicating what went wrong.
 *
 * @param err the error status
 * @see #getErrorIndex
 * @see #getErrorStatusString
 * @see #getErrorStatus
 */
protected void setErrorStatus(int err)
{
    errstat = err;
    if (AsnObject.debug > 6)
    {
        System.out.println("Pdu.setErrorStatus(): " + errstat);
    }
    if (errstat != AsnObject.SNMP_ERR_NOERROR)
    {
        setResponseException(new AgentException(getErrorStatusString()));
    }
}

/**
 * Sets the error status and the exception, indicating what went wrong.
 *
 * @param err the error status
 * @param exc the Pdu Exception that was thrown whilst decoding
 *
 * @see #getErrorIndex
 * @see #getErrorStatusString
 * @see #getErrorStatus
 */
protected void setErrorStatus(int err, PduException exc)
{
    errstat = err;
    setResponseException(exc);
}

/**
 * Sets the error index, this indicates which of the OIDs went wrong.
 * @param ind the error index
 * @see #setErrorStatus(int)
 * @see #getErrorIndex
 */
protected void setErrorIndex(int ind)
{
    errind = ind;
}


/**
 * This method is called when no answer is received after all
 * retries.
 * The application is notified of this.
 * See also fillin()
 */
private void handleNoAnswer()
{
    if (AsnObject.debug > 6)
    {
        System.out.println("Pdu.handleNoAnswer()");
    }

    // it's a lie, but it will prevent this method from
    // being called twice
    answered=true; 

    isTimedOut = true;
    setErrorStatus(AsnObject.SNMP_ERR_GENERR);
    setErrorIndex(0);

    setChanged();
    tell_them();
    clearChanged();


    synchronized(this)
    {
        notify();
    }
}

/**
 * Fill in the received response. 
 * @see Pdu#getResponseVarbinds()
 *
 */
void fillin(AsnPduSequence seq) 
{
    if (answered) 
    {
        if (AsnObject.debug > 6)
        {
            System.out.println("Pdu.fillin(): Got a second answer to request "
                + req_id);
        }
        return;
    }

    // fillin(null) can be called in case of a Decoding exception
    if (seq != null)
    {
        if (seq.isCorrect == true)
        {
            int n=-1;
            try
            {
                // Fill in the request id
                this.req_id = seq.getReqId();
                setErrorStatus(seq.getWhatError());
                setErrorIndex(seq.getWhereError());

                // The varbinds from the response/report are set in a
                // new Vector.
                AsnSequence varBind = seq.getVarBind();
                int size = varBind.getObjCount();
                respVarbinds = new Vector(size, 1);
                for (n=0; n<size; n++) 
                {
                    AsnSequence varSeq = (AsnSequence) varBind.getObj(n);
                    varbind vb = new varbind(varSeq);
                    respVarbinds.addElement(vb);

                    new_value(n, vb);
                }
            }
            catch (Exception e)
            {
                // it happens that an agent does not encode the varbind
                // list properly. Since we try do decode as much as
                // possible there may be wrong elements in this list.

                DecodingException exc = new DecodingException(
                    "Incorrect varbind list, element " + n);
                setErrorStatus(AsnObject.SNMP_ERR_DECODINGASN_EXC, exc);
            }
        }
        else
        {
            // we couldn't read the whole message
            // see AsnObject.AsnReadHeader, isCorrect

            DecodingException exc = new DecodingException(
                "Incorrect packet. No of bytes received less than packet length.");
            setErrorStatus(AsnObject.SNMP_ERR_DECODINGPKTLNGTH_EXC, exc);
        }

    }

    // always do 'setChanged', even if there are no varbinds.
    setChanged();
    tell_them();
    clearChanged();

    synchronized(this)
    {
        got = true;
        answered = true;
        notify();             // see also handleNoAnswer()
        if (trans != null)
        {
            // free up the transmitter, since 
            // we are happy with the answer.
            // trans may be null if we are receiving a trap.
            trans.interruptMe();  
        }
    }
}

/**
 * Notify all observers. If a decoding exception had occurred, the argument
 * will be replaced with the exception. 
 *
 * <p>
 * In the case of an exception, the error status
 * will be set to one of the decoding errors (see
 * <code>getErrorStatus</code>)
 * and passed as the parameter 
 * <code>arg</code> in the
 * <code>update(Observable obs, Object arg)</code>
 * method.
 * </p>
 *
 * @param arg The argument passed to update, can be a PduException.
 * @see SnmpConstants#SNMP_ERR_DECODING_EXC
 * @see #getErrorStatus
 * @see #getResponseVarbinds
 * @since 4.5
 */
public void notifyObservers(Object arg)
{
    if (respException != null)
    {
        super.notifyObservers(respException);
    }
    else
    {
        super.notifyObservers(arg);
    }
}

}
