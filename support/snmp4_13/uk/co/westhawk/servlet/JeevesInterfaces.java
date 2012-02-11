// NAME
//      $RCSfile: JeevesInterfaces.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.7 $
// CREATED
//      $Date: 2002/10/22 16:34:23 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

/*
 * Copyright (C) 1996, 1997, 1998, 1999, 2000, 2001, 2002 by Westhawk Ltd (www.westhawk.co.uk)
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


package uk.co.westhawk.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import uk.co.westhawk.snmp.stack.*;
import uk.co.westhawk.snmp.pdu.*;
import java.util.*;
import java.io.*;


/**
 * <p>
 * When accessed, the JeevesInterfaces servlet will collect once the 
 * information about all the interfaces on a SNMP server. It will hold the
 * connection until it has received the information. Then it will print
 * the information to your webpage.
 * </p>
 *
 * <p>
 * It supports the GET action. The following parameter should be sends
 * in the URL: 
 * <ul>
 * <li>host - the hostname or IP address of the SNMP server, default is <em>your localhost</em></li>
 * </ul>
 * For example:
 * <pre>
 *     http:&lt;servlet URL&gt;?host=localhost
 * </pre>
 * </p>
 *
 * <p>
 * It will use port <em>161</em> and community name <em>public</em>.
 * </p>
 *
 * Some general information about <a
 * href="./Interfaces.html#_general_">servlets</a>.
 *
 * @see InterfacePdu
 * @author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 3.7 $ $Date: 2002/10/22 16:34:23 $
 */
public class JeevesInterfaces extends HttpServlet
{
    private static final String     version_id =
        "@(#)$Id: JeevesInterfaces.java,v 3.7 2002/10/22 16:34:23 birgit Exp $ Copyright Westhawk Ltd";

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        final int port = 161;
        final String comm = "public";

        SnmpContext context;
        String [] answers = null;
        String error = null;

        String host = req.getParameter("host");
        if (host == null)
        {
            host = req.getRemoteHost();
            if (host == null)
            {
                host = "localhost";
            }
        }

        try
        {
            context = new SnmpContext(host, port);
            context.setCommunity(comm);
            int numIfs = InterfacePdu.getNumIfs(context);
            answers = new String[numIfs+1];
            for (int i=1;i <= numIfs; i++)    
            {
                InterfacePdu intef = new InterfacePdu(context, null, i);
                if (intef.waitForSelf() )
                {
                    answers[i] = intef.getOperStatusString();
                } 
                else
                {
                    answers[i] = "waited in vain";
                }
            }
        }
        catch (java.io.IOException exc)
        {
            error = "IOException " + exc.getMessage();
        }
        catch (PduException exc)
        {
            error = "PduException " + exc.getMessage();
        }
        try
        {
            res.setStatus(res.SC_OK);
            res.setContentType("text/html");

            //res.writeHeaders();
            PrintWriter out = new PrintWriter(res.getOutputStream());
            out.println("<html>");
            out.println("<head><title>"+getServletInfo()+"</title>");
            out.println("</head><body>");
            out.println("<h2>"+getServletInfo()+"</h2>");
            if (error != null)
            {
                out.println("<p> Host "+host+" interfaces "+"<ol>");
                if (answers != null)
                {
                    for (int i=1; i< answers.length; i++)
                    {
                        out.println("<li>"+answers[i]+"</li>");
                    }
                }
                out.println("</ol></p>");
            }
            else
            {
                out.println("<p> Error " + error + "</p>");
            }
            out.println("</body></html>");
        }
        catch (java.io.IOException exc)
        {
            // should log this
            ;
        }
    }

    public String getServletInfo()
    {
        return "Snmp Interface status checker";
    }

}

