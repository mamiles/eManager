// NAME
//      $RCSfile: ContextSocketFace.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.7 $
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

/**
 * The interface for the different type of sockets.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.7 $ $Date: 2002/10/10 15:13:57 $
 */
public interface ContextSocketFace 
{
    static final String     version_id =
        "@(#)$Id: ContextSocketFace.java,v 1.7 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

/**
 * Creates the socket. This socket is used when listening for traps. Use
 * only one (1) of the two create methods.
 *
 * @param port The local port number were we receive (listen for)
 * packets.
 * @see #create(String, int)
 */
public void create(int port) throws IOException;

/**
 * Creates the socket. This socket is used to send out our requests. Use
 * only one (1) of the two create methods.
 *
 * @param host The name of the host that is to receive our packets
 * @param port The port number of the host
 * @see #create(int)
 */
public void create(String host, int port) throws IOException;

/**
 * Returns the IP address of the host of the packet we received. 
 * This will be used when we are listening for traps. 
 *
 * @return The IP address, or null when the hostname cannot be determined
 */
public String getHostAddress();

/** 
 * Receives a packet from this socket.
 * @param maxRecvSize the maximum number of bytes to receive
 * @return the packet as ByteArrayInputStream
 */
public ByteArrayInputStream receive(int maxRecvSize) throws IOException;

/** 
 * Sends a packet from this socket.
 * @param packet the packet
 */
public void send(byte[] packet) throws IOException;

/** 
 * Closes this socket.
 */
public void close();

}
