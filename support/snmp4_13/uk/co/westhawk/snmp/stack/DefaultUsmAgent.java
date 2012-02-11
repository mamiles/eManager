// NAME
//      $RCSfile: DefaultUsmAgent.java,v $
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

package uk.co.westhawk.snmp.stack;

import uk.co.westhawk.snmp.beans.UsmDiscoveryBean;

/**
 * This implementation of UsmAgent tries to discover the parameters by
 * doing the default USM discovery process on localhost. 
 *
 * <p>
 * Note that it is not guaranteed that the agent will allow discovery 
 * by itself. Also, if the SNMP agent reboots while the stack is
 * running, it will not pick up the new boots and time.
 * </p>
 *
 * <p>
 * Users are advised and encouraged to provide a better, more accurate
 * implementation of UsmAgent.
 * </p>
 *
 * @see SnmpContextv3
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.3 $ $Date: 2002/10/10 15:13:57 $
 */
public class DefaultUsmAgent implements UsmAgent
{
    static final String version_id =
        "@(#)$Id: DefaultUsmAgent.java,v 3.3 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

      /**
       * The default name of the local host, <em>localhost</em>.
       */
      public final static String LOCAL_HOST = "localhost";

      /**
       * The default port number of the local host, <em>161</em>.
       */
      public final static int LOCAL_PORT = 161;

      private SnmpContextv3 context;
      private String hostname;
      private int port;

public DefaultUsmAgent()
{
    hostname = LOCAL_HOST;
    port = LOCAL_PORT;
}

/**
 * Returns the authoritative SNMP Engine ID. If the discovery failed,
 * <em>null</em> will be returned.
 *
 * @return The Engine ID
 */
public String getSnmpEngineId()
{
    TimeWindow tWindow = TimeWindow.getCurrent();
    String engineId = tWindow.getSnmpEngineId(hostname, port);
    return engineId;
}

/**
 * Returns the authoritative Engine Boots. If the discovery failed,
 * <em>1</em> will be returned.
 *
 * @return The Engine Boots
 */
public int getSnmpEngineBoots()
{
    int boots = 1;
    TimeWindowNode node = null;
    TimeWindow tWindow = TimeWindow.getCurrent();
    String engineId = tWindow.getSnmpEngineId(hostname, port);
    if (engineId != null)
    {
        node = tWindow.getTimeLine(engineId);
    }
    if (node != null)
    {
        boots = node.getSnmpEngineBoots();
    }
    return boots;
}

/**
 * Returns the authoritative Engine Time. If the discovery failed,
 * <em>1</em> will be returned.
 *
 * @return The Engine Time
 */
public int getSnmpEngineTime()
{
    int time = 1;
    TimeWindowNode node = null;
    TimeWindow tWindow = TimeWindow.getCurrent();
    String engineId = tWindow.getSnmpEngineId(hostname, port);
    if (engineId != null)
    {
        node = tWindow.getTimeLine(engineId);
    }
    if (node != null)
    {
        time = node.getSnmpEngineTime();
    }
    return time;
}

/**
 * Sets the SNMP context. It will do a discovery if needed.
 */
public void setSnmpContext(SnmpContextv3 c) 
{
    context = c;
    try
    {
        discoverIfNeeded();
    }
    catch (PduException exc)
    {
        if (AsnObject.debug > 4)
        {
            System.out.println("DefaultUsmAgent.setSnmpContext():"
                + exc.getMessage()); 
        }
    }
    catch (java.io.IOException exc)
    {
        if (AsnObject.debug > 4)
        {
            System.out.println("DefaultUsmAgent.setSnmpContext():"
                + exc.getMessage()); 
        }
    }
}

/**
 * Sets the my own hostname, ie the name of the agent or authoritative
 * engine. By default <em>localhost</em> is used.
 *
 * @see #LOCAL_HOST
 */
public void setAgentName(String host)
{
    hostname = host;
}

/**
 * Sets the my own port number, ie the port number of the agent 
 * or authoritative engine. By default <em>161</em> is used.
 *
 * @see #LOCAL_PORT
 */
public void setAgentPort(int p)
{
    port = p;
}

void discoverIfNeeded()
throws java.io.IOException, PduException
{
    uk.co.westhawk.snmp.beans.UsmDiscoveryBean discBean = null;
    boolean isNeeded = false;

    TimeWindow tWindow = TimeWindow.getCurrent();
    String engineId = tWindow.getSnmpEngineId(hostname, port);
    if (engineId == null)
    {
        isNeeded = true;
        discBean = new
        uk.co.westhawk.snmp.beans.UsmDiscoveryBean(hostname, 
              port, context.getTypeSocket());
    }

    if (context.isUseAuthentication())
    {
        if (isNeeded)
        {
            discBean.setAuthenticationDetails(context.getUserName(),
                context.getUserAuthenticationPassword(),
                context.getAuthenticationProtocol());
        }
        else if (tWindow.isTimeLineKnown(engineId) == false)
        {
            isNeeded = true;
            discBean = new uk.co.westhawk.snmp.beans.UsmDiscoveryBean(
                    hostname, port, context.getTypeSocket());
            discBean.setAuthenticationDetails(context.getUserName(),
                context.getUserAuthenticationPassword(),
                context.getAuthenticationProtocol());
        }

        if (isNeeded && context.isUsePrivacy())
        {
            discBean.setPrivacyDetails(context.getUserPrivacyPassword());
        }
    }

    if (isNeeded)
    {
        discBean.startDiscovery();
    }
}

}
