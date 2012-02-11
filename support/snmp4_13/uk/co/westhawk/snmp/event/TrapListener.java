// NAME
//      $RCSfile: TrapListener.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.3 $
// CREATED
//      $Date: 2002/10/10 15:13:56 $
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
package uk.co.westhawk.snmp.event;


/**
 * The listener interface for receiving trap events.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.3 $ $Date: 2002/10/10 15:13:56 $
 */
public interface TrapListener extends java.util.EventListener
{
    public static final String     version_id =
        "@(#)$Id: TrapListener.java,v 1.3 2002/10/10 15:13:56 birgit Exp $ Copyright Westhawk Ltd";


/**
 * Invoked when a trap is received.
 */
public void trapReceived(TrapEvent evt);

}
