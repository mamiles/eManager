// NAME
//      $RCSfile: SnmpContextv2cPool.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.8 $
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

package uk.co.westhawk.snmp.stack;

import java.util.*;

/**
 * This class contains the pool of SNMP v2c contexts.
 * It extends the SnmpContextPool and is similar in every way, except it
 * uses a pool of SnmpContextv2c.
 *
 * <p>
 * Thanks to Seon Lee (slee@virtc.com) for reporting thread safety
 * problems.
 * </p>
 *
 * @see SnmpContextv2c
 * @see SnmpContextPool
 * @see SnmpContextv3Pool
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.8 $ $Date: 2002/10/10 15:13:57 $
 */
public class SnmpContextv2cPool extends SnmpContextPool 
    implements SnmpContextv2cFace
{
    private static final String     version_id =
        "@(#)$Id: SnmpContextv2cPool.java,v 3.8 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";


/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @see SnmpContextv2c#SnmpContextv2c(String, int)
 */
public SnmpContextv2cPool(String host, int port) throws java.io.IOException
{
    super(host, port, STANDARD_SOCKET);
}

/**
 * Constructor.
 *
 * @param host The host to which the Pdu will send
 * @param port The port where the SNMP server will be
 * @param typeSocket The type of socket to use. 
 * @see SnmpContextv2c#SnmpContextv2c(String, int, String)
 * @see SnmpContextBasisFace#STANDARD_SOCKET
 * @see SnmpContextBasisFace#NETSCAPE_SOCKET
 * @see SnmpContextBasisFace#KVM_SOCKET
 */
public SnmpContextv2cPool(String host, int port, String typeSocket) 
throws java.io.IOException
{
    super(host, port, typeSocket);
}

/**
 * Return the SNMP version of the context.
 *
 * @return The version
 */
public int getVersion()
{
    return AsnObject.SNMP_VERSION_2c;
}

/**
 * Returns a v2c context from the pool. 
 * This methods checks for an existing context that matches all our
 * properties. If such a context does not exist a new one is created and
 * added to the pool. 
 *
 * This method actually returns a SnmpContextv2c, although it doesn't
 * look like it.
 *
 * @return A context (v2c) from the pool 
 * @see #getHashKey
 * @see SnmpContext
 * @see SnmpContextv2c
 */
protected SnmpContext getMatchingContext() throws java.io.IOException
{
    SnmpContextPoolItem item = null;
    SnmpContextv2c newContext = null;
    String hashKey = getHashKey();

    synchronized(contextPool)
    {
        int count=0;
        if (contextPool.containsKey(hashKey))
        {
            item = (SnmpContextPoolItem) contextPool.get(hashKey);
            newContext = (SnmpContextv2c) item.getContext();
            count = item.getCounter();
        }
        else
        {
            newContext = new SnmpContextv2c(hostAddr, hostPort, socketType);
            newContext.setCommunity(community);
            item = new SnmpContextPoolItem(newContext);
            contextPool.put(hashKey, item);
        }
        count++;
        item.setCounter(count);
    }
    return newContext;
}

}
