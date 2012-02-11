// NAME
//      $RCSfile: EncodingException.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.3 $
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

/**
 * Thrown to indicate that the Pdu cannot be build or encoded.
 * 
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.3 $ $Date: 2002/10/10 15:13:57 $
 */
public class EncodingException extends PduException 
{
    private static final String     version_id =
        "@(#)$Id: EncodingException.java,v 3.3 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

/** 
 * Constructs an EncodingException with no specified detail message. 
 */
public EncodingException() 
{
    super();
}

/** 
 * Constructs an EncodingException with the specified detail
 * message. 
 *
 * @param str The detail message.
 */
public EncodingException(String str) 
{
    super(str);
}


}
