// NAME
//      $RCSfile: StandardSocket.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.8 $
// CREATED
//      $Date: 2002/10/10 15:13:57 $
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

package uk.co.westhawk.snmp.net;

import java.io.*;
import java.net.*;
import uk.co.westhawk.snmp.stack.*;

/**
 * This is a wrapper class around the standard DatagramSocket.
 *
 * @see DatagramSocket
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.8 $ $Date: 2002/10/10 15:13:57 $
 */
public class StandardSocket implements ContextSocketFace 
{
    static final String     version_id =
        "@(#)$Id: StandardSocket.java,v 1.8 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    private DatagramSocket  soc=null;
    private InetAddress     hostAddr;
    private int             hostPort;

public StandardSocket()
{
}

public void create(int port) throws IOException
{
    hostPort = port;
    try
    {
        soc = new DatagramSocket(hostPort);
    }
    catch (SocketException exc)
    {
        String str = "Socket problem " + exc.getMessage();
        throw (new IOException(str));
    }
}


public void create(String host, int port) throws IOException
{
    hostPort = port;
    try
    {
        hostAddr = InetAddress.getByName(host);
        soc = new DatagramSocket();
    }
    catch (SocketException exc)
    {
        String str = "Socket problem " + exc.getMessage();
        throw (new IOException(str));
    }
    catch (UnknownHostException exc)
    {
        String str = "Cannot find host " + exc.getMessage();
        throw (new IOException(str));
    }
}

public String getHostAddress()
{
    String res = null;
    if (hostAddr != null)
    {
        res = hostAddr.getHostAddress();
    }
    return res;
}

public ByteArrayInputStream receive(int maxRecvSize) throws IOException
{
    ByteArrayInputStream in = null;
    if (soc != null)
    {
        byte [] data = new byte[maxRecvSize];
        DatagramPacket p = new DatagramPacket(data,maxRecvSize);

        // timeout will throw an exception
        // every 1000 secs whilst idle
        // it is caught and ignored
        // but as a side effect it loops,
        // checking 'me'
        soc.setSoTimeout(1000);

        soc.receive(p);
        hostAddr = p.getAddress();
        in = new ByteArrayInputStream(p.getData(), 0, p.getLength());
    }
    return in;
}

public void send(byte[] packet) throws IOException
{
    if (soc != null)
    {
        DatagramPacket pack = new DatagramPacket(packet, packet.length, 
              hostAddr, hostPort);
        soc.send(pack);
    }
}

public void close()
{
    if (soc != null)
    {
        soc.close();
    }
}

}
