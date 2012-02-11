// NAME
//      $RCSfile: AsnPduSequence.java,v $
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
 * original version by hargrave@dellgate.us.dell.com (Jordan Hargrave)
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

import java.io.*;
import java.util.*;

/**
 * The AsnPduSequence class know how a Pdu v1 and v2c is build, it knows its
 * sequence.
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.8 $ $Date: 2002/10/10 15:13:57 $
 */
class AsnPduSequence extends AsnSequence 
{
    private static final String     version_id =
        "@(#)$Id: AsnPduSequence.java,v 3.8 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    AsnPduSequence(InputStream in, int len, int pos) throws IOException 
    {
        super(in,len,pos);
    }

    int getReqId() 
    {
        AsnInteger rid = (AsnInteger) getObj(0);
        return(rid.getValue());
    }

    int getWhatError() 
    {
        AsnInteger estat = (AsnInteger) getObj(1);
        return (estat.getValue());
    }

    int getWhereError() 
    {
        AsnInteger estat = (AsnInteger) getObj(2);
        return (estat.getValue());
    }

    AsnSequence getVarBind()
    {
        return (AsnSequence) getObj(3);
    }

    /** 
     * recursively look for a pduSequence object
     * - got one :-)
     */
    AsnObject findPdu() 
    {
        return this;  
    }

    int getValue() 
    {
        AsnSequence varBind = (AsnSequence) getObj(3);
        AsnSequence varPair = (AsnSequence) varBind.getObj(0);
        AsnInteger val = (AsnInteger) varPair.getObj(1);
        int value =  val.getValue();

        return value;
    }

    boolean hadError() 
    {
        return (SNMP_ERR_NOERROR != getWhatError());
    }

}
