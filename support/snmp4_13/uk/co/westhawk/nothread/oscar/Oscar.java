// NAME
//      $RCSfile: Oscar.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.1 $
// CREATED
//      $Date: 2002/10/15 13:37:01 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//
/*
 * Copyright (C) 2000, 2001, 2002 by Westhawk Ltd (www.westhawk.co.uk)
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
package uk.co.westhawk.nothread.oscar;

import java.util.*;
import java.io.*;
import java.beans.*;

/**
 * The Oscar class is a very simple class to test if uploading java into
 * Oracle works and to test if this method can be called with a stored
 * function/procedure. 
 *
 * <p>
 * See 
 * <a href="./package-summary.html">notes</a>
 * on how to send traps in an Oracle JServer environment.
 * </p>
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.1 $ $Date: 2002/10/15 13:37:01 $
 */
public class Oscar 
{
    // return a quotation from Oscar Wilde
    public static String quote() 
    {
        return "I can resist everything except temptation.";
    }
}

