// NAME
//      $RCSfile: AbstractSnmpContext.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.22 $
// CREATED
//      $Date: 2002/11/18 14:08:20 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 2000, 2001, 2002 by Westhawk Ltd
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

import java.io.*;
import java.util.*;
import uk.co.westhawk.snmp.net.*;
import uk.co.westhawk.snmp.event.*;
import uk.co.westhawk.snmp.util.*;

/**
 * This class contains the abstract SNMP context that is needed by every
 * Pdu to send a SNMP v1, v2c or v3 request.
 * The context also provides functionality to receive traps.
 *
 * <p>
 * <code>destroy()</code> should be called when the context is no longer
 * used. This is the only way the threads will be stopped and garbage
 * collected.
 * </p>
 *
 * @see SnmpContext
 * @see SnmpContextv2c
 * @see SnmpContextv3
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.22 $ $Date: 2002/11/18 14:08:20 $
 */
public abstract class AbstractSnmpContext extends Object
    implements SnmpContextBasisFace, Runnable, TrapListener
{
    private static final String     version_id =
        "@(#)$Id: AbstractSnmpContext.java,v 3.22 2002/11/18 14:08:20 birgit Exp $ Copyright Westhawk Ltd";

    private ContextSocketFace  soc;
    private Transmitter []  transmitters;
    private Pdu []          pdus;
    private Thread          me;
    private String          basename;

    protected String        typeSocket;
    protected String        hostAddr;
    protected int           hostPort;
    protected int           maxRecvSize;
    protected boolean       isDestroyed;
    private TrapReceivedSupport trapSupport;


/**
 * Processes an incoming response. Has to be overload by each context.
 *
 * @see #run
 */
protected abstract void ProcessIncomingMessage(AsnDecoder rpdu,
      ByteArrayInputStream in) throws DecodingException, IOException;
/**
 * Encodes a packet. Has to be overload by each context.
 */
public abstract byte[] encodePacket(byte msg_type, int rId, int errstat,
      int errind, Enumeration ve)
      throws java.io.IOException, EncodingException;
/**
 * Processes an incoming trap. Has to be overload by each context.
 * @see #trapReceived
 */
public abstract Pdu processIncomingTrap(byte [] message) throws DecodingException, IOException;
/**
 * Return the SNMP version of this context. Has to be overload by each
 * context.
 */
public abstract int getVersion();

/**
 * Constructor.
 * The Standard socket type will be used.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @see SnmpContextBasisFace#STANDARD_SOCKET
 */
protected AbstractSnmpContext(String host, int port) throws java.io.IOException
{
    this(host, port, STANDARD_SOCKET);
}

/**
 * Constructor.
 *
 * The typeSocket will indicate which type of socket to use. This way
 * different handlers can be provided for Netscape or Standard JVM.
 * The Netscape implementation will make the necessary security calls
 * to access hosts that are not the applet's webserver. The KVM
 * version will be for small device support (e.g. Palm Pilot).
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @param typeSocketA The type of socket to use.
 *
 * @see SnmpContextBasisFace#STANDARD_SOCKET
 * @see SnmpContextBasisFace#NETSCAPE_SOCKET
 * @see SnmpContextBasisFace#KVM_SOCKET
 */
protected AbstractSnmpContext(String host, int port, String typeSocketA)
throws java.io.IOException
{
    pdus = new Pdu[MAXPDU];
    hostAddr = host;
    hostPort = port;
    typeSocket = typeSocketA;
    transmitters = new Transmitter[MAXPDU];
    basename = host+"_"+ port;
    trapSupport = new TrapReceivedSupport(this);

    isDestroyed = false;
    maxRecvSize = MSS;

    soc = getSocket(typeSocketA);
    soc.create(host, port);

    activate();
}


/**
 * Creates and starts the Receive thread that allows this context to
 * receive packets.
 * Subclasses may override this to adjust the threading behaviour.
 *
 * @see PassiveSnmpContext#activate()
 * @see PassiveSnmpContextv2c#activate()
 *
 */
// Note: would it not be better to call activate() in sendPacket?
// This way the run will not start until a packet has been sent.
// It would even be better if activate() was only called if a packet had
// been sent that needs/expects a reply. I don't know how to distinguish
// between them in sendPacket
protected void activate()
{
    if (me == null)
    {
        me = new Thread(this, basename+"_Receive");
        me.setPriority(me.MAX_PRIORITY);
        me.start();
    }
}

static ContextSocketFace getSocket(String type) throws IOException
{
    ContextSocketFace sf = null;
    String className = "uk.co.westhawk.snmp.net.StandardSocket";
    if (type.equals(NETSCAPE_SOCKET))
    {
        className = "uk.co.westhawk.snmp.net.NetscapeSocket";
    }
    else if (type.equals(KVM_SOCKET))
    {
        className = "uk.co.westhawk.snmp.net.KvmSocket";
    }

    try
    {
        Class cl = Class.forName(className);
        Object obj = cl.newInstance();
        sf = (ContextSocketFace) obj;
    }
    catch (ClassNotFoundException exc)
    {
        String str = "ClassNotFound problem " + exc.getMessage();
        throw (new IOException(str));
    }
    catch (InstantiationException exc)
    {
        String str = "Instantiation problem " + exc.getMessage();
        throw (new IOException(str));
    }
    catch (IllegalAccessException exc)
    {
        String str = "IllegalAccess problem " + exc.getMessage();
        throw (new IOException(str));
    }
    catch (ClassCastException exc)
    {
        String str = "ClassCast problem " + exc.getMessage();
        throw (new IOException(str));
    }
    if (sf == null)
    {
        String str = "Cannot create socket ";
        throw (new IOException(str));
    }
    else
    {
        if (AsnObject.debug > 12)
        {
            System.out.println("AbstractSnmpContext.getSocket(): New socket " + sf.getClass().getName());
        }
    }
    return sf;
}

/**
 * Returns the host
 *
 * @return The host
 */
public String getHost()
{
    return hostAddr;
}

/**
 * Returns the IP address string "%d.%d.%d.%d" of the host.
 *
 * @return The IP address of the host
 * @see java.net.InetAddress#getHostAddress()
 */
public String getHostAddress()
{
    return soc.getHostAddress();
}

/**
 * Returns the port number
 *
 * @return The port no
 */
public int getPort()
{
    return hostPort;
}

/**
 * Returns the type socket
 *
 * @return The type socket
 */
public String getTypeSocket()
{
    return typeSocket;
}

/**
 * Returns the maximum number of bytes this context will read from the
 * socket. By default this will be set to <code>MSS</code> (i.e. 1300).
 *
 * @since 4_12
 * @see SnmpContextBasisFace#MSS
 * @see #setMaxRecvSize(int)
 * @return The number
 */
public int getMaxRecvSize()
{
    return maxRecvSize;
}

/**
 * Sets the maximum number of bytes this context will read from the
 * socket. By default this will be set to <code>MSS</code> (i.e. 1300).
 * The default size seems a reasonable size. The problem usually occurs
 * when sending Bulk requests.
 *
 * <p>
 * If a packet arrives that is bigger than the maximum size of received
 * bytes, the stack will try to decode it nevertheless. The usual
 * error that will occur is:
 * <pre>
 * Error message: "Incorrect packet. No of bytes received less than packet length."
 * </pre>
 * </p>
 *
 * <p>
 * Although UDP datagrams can be fragmented (fragmentation is part of
 * the network layer (IP), not the transport layer (UDP/TCP)), some
 * firewalls reject incoming fragments. Therefor it is best not to set
 * maxRecvSize higher than the largest packet size you can get through
 * your network topology.
 * </p>
 *
 * <p>
 * Thanks to Pete Kazmier (pete@kazmier.com) for the suggestion.
 * </p>
 *
 * <p><em>
 * <font color="red">
 * Note, this property is NOT supported in any of the SNMPContextXXPool
 * classes.
 * </font>
 * </em></p>
 *
 * @since 4_12
 * @see SnmpContextBasisFace#MSS
 * @see #getMaxRecvSize()
 * @param no The new number
 */
public void setMaxRecvSize(int no)
{
    maxRecvSize = no;
}

/**
 * Returns the thread usage of the AbstractSnmpContext.
 * It returns a String in the form of <code>=PO=QR--------------0</code>.
 *
 * <p>
 * The String represents the array of transmitters.
 * Each character represents a transmitter slot.
 * The transmitters form a thread pool of a maximum size, MAXPDU.
 * Each transmitter is used to wait for one PDU response at a given
 * moment in time.
 * When the response is received the transmitter will stop running, but
 * is not destroyed. It will be reused.
 * </p>
 *
 * <p>
 * Meaning of each character:
 * <ul>
 * <li><code>-</code> a transmitter slot has not yet been allocated a thread</li>
 * <li><code>=</code> there is a thread but it is idle</li>
 * <li><code>A->Z</code> the thread is transmitting a Pdu</li>
 * <li>
 * The last character represents the context's recv thread:
 *   <ul>
 *   <li><code>0</code> there isn't one</li>
 *   <li><code>1</code> it exists but isn't running </li>
 *   <li><code>2</code> it exists and is alive.</li>
 *   </ul>
 * </li>
 * </ul>
 * </p>
 *
 * @since 4_12
 * @return The thread usage of the AbstractSnmpContext
 */
public String getDebugString()
{
    char [] cret = new char[MAXPDU+1];
    for (int i=0;i<MAXPDU; i++)
    {
        if (transmitters[i] != null)
        {
            if (pdus[i] != null)
            {
                cret[i] = (char ) ('A' + (pdus[i].getReqId() % 26));
            }
            else
            {
                cret[i] = '=';
            }
        }
        else
        {
            cret[i] = '-';
        }
    }

    char res='0';
    if (me != null)
    {
        res++;
        if (me.isAlive())
        {
            res++;
        }
    }
    cret[MAXPDU] = res;

    return new String(cret);
}

/**
 * This method will stop the thread.
 * All transmitters, pdus in flight and traplisteners will be removed
 * when run() finishes.
 *
 * <p>
 * It closes the socket.
 * The tread will actually stop/finish when the run() finishes. Since
 * the socket is closed, the run() will fall through almost instantly.
 * </p>
 *
 * <p>
 * Although the traplisteners are removed, this method does not stop the
 * DefaultTrapContext listening for trap.
 * </p>
 *
 * <p>
 * Note: The thread(s) will not die immediately; this will take about
 * half a minute.
 * </p>
 *
 * @see DefaultTrapContext#destroy()
 */
public synchronized void destroy()
{
    if (me != null)
    {
        me = null;
    }

    if (AsnObject.debug > 0)
    {
        System.out.println("AbstractSnmpContext.destroy(): Closing socket ");
    }
    soc.close();

    isDestroyed = true;
}

/**
 * This method will stop the thread.
 * All transmitters, pdus in flight and traplisteners will be removed
 * when run() finishes.
 * <p>
 * It does NOT close the socket.
 * The tread will actually stop/finish when the run() finishes. That is when
 * a packet arrives on the socket or when the socket times out.
 * </p>
 *
 * <p>
 * We have deprecated this method since there is no point in stopping
 * the context, but not destroying it. The context cannot start again anyway.
 * The difference between destroy() and stop() was not very clear.
 * </p>
 *
 * @deprecated As of version 4_12, should use destroy()
 * @see #destroy()
 */
public synchronized void stop()
{
    if (me != null)
    {
        me = null;
    }
}

/**
 * We wait for any incoming packets. After receiving one, decode
 * the packet into an Pdu. The Pdu will notify the observers waiting
 * for an response.
 */
public void run()
{
    // while It is visible
    while (me != null)
    {
        AsnDecoder rpdu = new AsnDecoder();
        // block for incoming packets
        me.yield();
        try
        {
            ByteArrayInputStream in;

            if (me == null)
            {
                break;
            }
            in = soc.receive(maxRecvSize);

            if (AsnObject.debug > 10)
            {
                int nb = in.available();
                byte [] bu = new byte[nb];
                in.read(bu);
                in.reset();

                SnmpUtilities.dumpBytes("Received from "
                    + soc.getHostAddress() + ": ", bu);
            }
            ProcessIncomingMessage(rpdu, in);
        }
        catch (java.io.IOException exc)
        {
            if (exc instanceof InterruptedIOException)
            {
                if (AsnObject.debug > 15)
                {
                    System.out.println("AbstractSnmpContext.run(): Idle recv " + exc.getMessage());
                }
            }
            else if (exc instanceof java.net.SocketException)
            {
                if (AsnObject.debug > 15)
                {
                    System.out.println("AbstractSnmpContext.run(): SocketException " + exc.getMessage());
                }
            }
            else
            {
                if (AsnObject.debug > 0)
                {
                    System.out.println("AbstractSnmpContext.run(): "
                        + exc.getClass().getName() + " " + exc.getMessage());
                    exc.printStackTrace();
                }
            }
        }
        catch (DecodingException exc)
        {
            if (AsnObject.debug > 1)
            {
                System.out.println("AbstractSnmpContext.run(): DecodingException: " + exc.getMessage());
            }
        }
        catch (Exception exc)
        {
            if (AsnObject.debug > 1)
            {
                System.out.println("AbstractSnmpContext.run(): Exception: " + exc.getMessage());
                exc.printStackTrace();
            }
        }
    }

    for (int i=0;i<MAXPDU; i++)
    {
        if (transmitters[i] != null)
        {
            transmitters[i].destroy();
            transmitters[i] = null;
        }
        if (pdus[i] != null)
        {
            pdus[i] = null;
        }
    }

    trapSupport.empty();
    try
    {
        DefaultTrapContext defaultTrapContext = DefaultTrapContext.getInstance(DefaultTrapContext.DEFAULT_TRAP_PORT, typeSocket);

        defaultTrapContext.removeTrapListener(this);
    }
    catch (java.io.IOException exc) { }
}



/**
 * Pass a correctly encoded SNMP packet, and we'l send it...
 */
public synchronized void sendPacket(byte[] p)
{
    try
    {
        if (AsnObject.debug > 10)
        {
            SnmpUtilities.dumpBytes("Sending to "
                + soc.getHostAddress() + ": ", p);
        }
        soc.send(p);
    }
    catch (IOException exc)
    {
        if (AsnObject.debug > 0)
        {
            System.out.println("AbstractSnmpContext.sendPacket(): IOException "+ exc.getMessage());
        }
    }
}

Pdu getPdu(Integer ReqId)
{
    return getPdu(ReqId.intValue());
}

Pdu getPdu(int rid)
{
    Pdu ret = null;
    for (int i=0; i< MAXPDU; i++)
    {
        if ((pdus[i] != null) && (pdus[i].getReqId() == rid))
        {
            ret = pdus[i];
            break;
        }
    }
    return ret;
}

/**
 * Removes a pdu.
 *
 * @param rid the Pdu request id
 * @return whether the pdu has been successfully removed
 */
public synchronized boolean removePdu(int rid)
{
    boolean ret = false;
    for (int i=0; i< MAXPDU; i++)
    {
        if ((pdus[i] != null) && (pdus[i].getReqId() == rid))
        {
            pdus[i] = null;
            ret = true;
            break;
        }
    }
    return ret;
}

/**
 * Adds a pdu. A transmitter is allocated to this pdu as well.
 *
 * @param pdu the Pdu
 * @return whether the pdu has been successfully added
 */
public synchronized boolean addPdu(Pdu p)
throws java.io.IOException, PduException
{
    boolean done = false;
    if (isDestroyed == true)
    {
        throw new EncodingException("Context can no longer be used, since it is already destroyed");
    }
    else
    {
        for (int i=0; i<MAXPDU; i++)
        {
            if (pdus[i] == null)
            {
                pdus[i] = p;
                pdus[i].setTrans(getTrans(i));
                done = true;
                break;
            }
        }
    }
    return done;
}

/**
 * Adds the specified trap listener to receive traps from the host that
 * matches this context.
 *
 * <p>
 * The DefaultTrapContext class will do the actual listening for traps.
 * This context will add itself to the DefaultTrapContext object and
 * will only pass the event to its listeners if the trap matches this
 * context. Note that the portnumber of this class is ignored. Unless
 * there already exists a DefaultTrapContext object, the
 * DEFAULT_TRAP_PORT is used.
 * </p>
 *
 * @see DefaultTrapContext#getInstance(int, String)
 * @see DefaultTrapContext#addTrapListener(TrapListener)
 * @see DefaultTrapContext#DEFAULT_TRAP_PORT
 */
public void addTrapListener(TrapListener l) throws java.io.IOException
{
    DefaultTrapContext defaultTrapContext = DefaultTrapContext.getInstance(DefaultTrapContext.DEFAULT_TRAP_PORT, typeSocket);

    trapSupport.addTrapListener(l);
    defaultTrapContext.addTrapListener(this);
}

/**
 * Removes the specified trap listener.
 */
public void removeTrapListener(TrapListener l) throws java.io.IOException
{
    DefaultTrapContext defaultTrapContext = DefaultTrapContext.getInstance(DefaultTrapContext.DEFAULT_TRAP_PORT, typeSocket);

    trapSupport.removeTrapListener(l);
    if (trapSupport.getListenerCount() == 0)
    {
        defaultTrapContext.removeTrapListener(this);
    }
}

/**
 * Invoked when an undecoded trap is received.
 * First the version and the hostaddress are checked, if correct
 * an attempt is made to decode the trap.
 * When successful the original event is consumed and a decoded trap event
 * is passed on the listeners.
 *
 * @see TrapReceivedSupport#fireTrapReceived(Pdu)
 */
public void trapReceived(TrapEvent evt)
{
    String hostAddress = evt.getHostAddress();
    int version = evt.getVersion();
    if (version == this.getVersion())
    {
        if (hostAddress != null && hostAddress.equals(this.getHostAddress()) == true)
        {
            byte [] message = evt.getMessage();
            Pdu pdu = null;
            try
            {
                pdu = processIncomingTrap(message);
            }
            catch(DecodingException exc)
            {
                if (AsnObject.debug > 2)
                {
                    System.out.println("AbstractSnmpContext.trapReceived(): DecodingException: " + exc.getMessage());
                }
            }
            catch(IOException exc)
            {
                if (AsnObject.debug > 0)
                {
                    System.out.println("AbstractSnmpContext.trapReceived(): IOException "+ exc.getMessage());
                }
            }

            if (pdu != null)
            {
                evt.consume();
                trapSupport.fireTrapReceived(pdu);
            }
        }
        else
        {
            if (AsnObject.debug > 5)
            {
                System.out.println("AbstractSnmpContext.trapReceived(): "
                    + "Trap host (" + hostAddress
                    + "), does not correspond with context host ("
                    + this.getHostAddress() + ")");
            }
        }
    }
    else
    {
        if (AsnObject.debug > 5)
        {
            String theirs = SnmpUtilities.getSnmpVersionString(version);
            String ours = SnmpUtilities.getSnmpVersionString(this.getVersion());
            System.out.println("AbstractSnmpContext.trapReceived(): "
                + "Trap version " + theirs
                + ", does not correspond with context version "
                + ours);
        }
    }
}

Transmitter getTrans(int i)
{
    if (transmitters[i] == null)
    {
        transmitters[i] = new Transmitter(basename+"_Trans"+i);
    }
    return transmitters[i];
}

}
