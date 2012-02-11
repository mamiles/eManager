// NAME
//      $RCSfile: ReachHostServlet.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.7 $
// CREATED
//      $Date: 2002/10/22 16:41:14 $
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
import uk.co.westhawk.snmp.beans.*;
import java.util.*;
import java.io.*;
import java.text.*;

/**
 * <p>
 * The ReachHostServlet servlet uses the IsHostReachableBean to see 
 * if a host is reachable. 
 * </p>
 *
 * <p>
 * It will hold the connection until it has received the information. 
 * Then it will print the date the host went up, following by a nice
 * HTML form which makes it easier to do a next request. 
 * </p>
 *
 * <p>
 * This servlet should be installed on a Java bases webserver. The
 * parameters should be a part of the URL. If no parameters are send,
 * the servlet will use the default values.
 * </p>
 *
 * <p>
 * <ul>
 * Parameters:
 * <li>host - the host, default is <em>your localhost</em></li>
 * <li>port - the port, default <em>161</em></li>
 * <li>comm - the community name, default <em>public</em></li>
 * </ul>
 * For example:
 * <pre>
 *     http:&lt;servlet URL&gt;?host=localhost&port=161&comm=public
 * </pre>
 * </p>
 *
 * Some general information about <a
 * href="./Interfaces.html#_general_">servlets</a>.
 *
 * @see uk.co.westhawk.snmp.beans.SNMPBean
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.7 $ $Date: 2002/10/22 16:41:14 $
 */
public class ReachHostServlet extends HttpServlet
{
    private static final String     version_id =
        "@(#)$Id: ReachHostServlet.java,v 3.7 2002/10/22 16:41:14 birgit Exp $ Copyright Westhawk Ltd";

    private IsHostReachableBean bean;
    private Date lastUpdateDate;

public void init (ServletConfig config) throws ServletException
{
    super.init(config);
    bean = new IsHostReachableBean();
    lastUpdateDate = new Date();
}

protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
{
    ServletOutputStream out;
    String host, community, port;

    if (bean == null)
    {
        bean = new IsHostReachableBean();
    }


    String localhost = req.getRemoteHost();
    if (localhost == null)
    {
        localhost = "localhost";
    }
    host = getStringParameter(req, "host", localhost);
    port = getStringParameter(req, "port", "161");
    community = getStringParameter(req, "comm", "public");

    bean.setHost(host);
    bean.setPort(port);
    bean.setCommunityName(community);

    try
    {
        bean.waitForSelfAction();
    }
    catch (PduException exc)
    {
        throw new IOException("PduException " + exc.getMessage());
    }
    lastUpdateDate = new Date();

    try
    {
        out = res.getOutputStream();

        res.setStatus(res.SC_OK);
        res.setContentType("text/html");

        out.println("<html>");
        out.println("<head><title>Output of ReachHostServlet</title></head>");
        out.println("<body>");

        out.println("<p>");
        out.println(bean.getMessage());
        out.println("</p><hr>");

        String URL = req.getRequestURL().toString();
        print_submit(out, URL);
        print_text(out, host, port, community);

        out.println("</form></body></html>");
    }
    catch (IOException e)
    {
        res.setStatus(res.SC_INTERNAL_SERVER_ERROR);
    }
}

private void print_submit(ServletOutputStream o, String URL) 
      throws IOException
{
    o.println("<form method=GET action=\"" + URL + "\">");
    o.println("<input type=submit value=\"Try host\"><br>");
}

private void print_text(ServletOutputStream o, String h, String p, String c) 
    throws IOException
{
    o.println("<input type=text name=host size=30 maxlength=100 value="
          + h + "> Host <br>");
    o.println("<input type=text name=port size=30 maxlength=100 value="
          + p + "> Port <br>");
    o.println("<input type=text name=comm size=30 maxlength=100 value="
          + c + "> Community <br>");
}

protected long getLastModified(HttpServletRequest req)
{
    return lastUpdateDate.getTime();
}

public String getServletInfo()
{
    return "Requests the uptime of a host";
}

private String getStringParameter(HttpServletRequest req, String str, 
      String def)
{
    String value = req.getParameter(str);
    if (value == null)
    {
        value = def;
    }
    return value;
}

private int getIntParameter(HttpServletRequest req, String str, int def)
{
    String val = req.getParameter(str);
    int value = def;
    try
    {
        value = Integer.valueOf(val).intValue();
    }
    catch (NumberFormatException e)
    {
        ;
    }
    return value;
}
 
}
