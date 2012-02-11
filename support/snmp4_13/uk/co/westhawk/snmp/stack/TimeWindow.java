// NAME
//      $RCSfile: TimeWindow.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 3.9 $
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
 */
package uk.co.westhawk.snmp.stack;
import java.util.*;

/**
 * TimeWindow keeps the lookup tables for the engine Id information.
 * TimeWindow should be created only once. Only use the 
 * <code>getCurrent()</code> 
 * method to access any other method, i.e. 
 * <p><pre>
 * if (TimeWindow.getCurrent() == null)
 * {
 *     TimeWindow timew = new TimeWindow();
 * }
 * boolean known = TimeWindow.getCurrent().isSnmpEngineIdKnown(host, port);
 * </pre></p>
 *
 * <p>
 * This class contains 2 lookup tables. One that matches the
 * hostname+port onto the SNMP engine ID. Another that matches the SNMP
 * engine ID with the timeline details about this engine.
 * </p>
 *
 * <p>
 * This class will also update the timeline details every second.
 * </p>
 *
 * @see #getCurrent()
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.9 $ $Date: 2002/10/10 15:13:57 $
 */
public class TimeWindow 
{
    private static final String     version_id =
        "@(#)$Id: TimeWindow.java,v 3.9 2002/10/10 15:13:57 birgit Exp $ Copyright Westhawk Ltd";

    /**
     * The maximum number of seconds the engine time in the Pdu is allowed 
     * to differ from my estimated engine time.
     */
    public final static int MaxTimeDifference = 150;

    private static TimeWindow current = null;

    // lookup table host+port -> engine id
    private Hashtable hostLookup; 

    // lookup table engine id -> TimeWindowNode
    private Hashtable engineLookup; 

    private long startTime;

/**
 * Constructor.
 */
public TimeWindow()
{
    if (current == null)
    {
        current = this;

        hostLookup = new Hashtable(5);
        engineLookup = new Hashtable(5);
    }
}

/**
 * Returns the current reference to this class. 
 * TimeWindow should be created only once. Only use the 
 * <code>getCurrent()</code> 
 * method to access any other method, i.e. 
 * <p><pre>
 * if (TimeWindow.getCurrent() == null)
 * {
 *     TimeWindow timew = new TimeWindow();
 * }
 * boolean known = TimeWindow.getCurrent().isSnmpEngineIdKnown(host, port);
 * </pre></p>
 * 
 * @return the current timewindow
 */
public static TimeWindow getCurrent()
{
    return current;
}

/**
 * Returns the snmp engine ID. This method will lookup the engine ID
 * based on the host and port. If it the engine ID is not known, null
 * will be returned. 
 *
 * @param host The hostname of the engine ID
 * @param port The port number of the engine ID
 * @return the snmp engine ID
 * @see #isSnmpEngineIdKnown(String, int)
 */
public String getSnmpEngineId(String host, int port)
{
    String key = getKey(host, port);
    String snmpEngineId = (String) hostLookup.get(key);
    return snmpEngineId;
}

/**
 * Returns if the snmp engine ID is known. This method will lookup if the 
 * engine ID belonging to this host and port is known.
 * <p>
 * When the SNMP engine ID is known, this doesn't necessarily mean that
 * the timeline details of this engine ID are known, since it takes a
 * second discovery step to find out.
 * </p>
 *
 * @param host The hostname of the engine ID
 * @param port The port number of the engine ID
 * @return whether the snmp engine ID is known
 */
public boolean isSnmpEngineIdKnown(String host, int port)
{
    String key = getKey(host, port);
    return hostLookup.containsKey(key);
}

/**
 * Sets the SNMP engine ID that belongs to the specified host and port.
 * The old SNMP engine ID (if any) will be overwritten.
 *
 * @param host The hostname of the engine ID
 * @param port The port number of the engine ID
 * @param snmpEngineId The engine ID
 */
public void setSnmpEngineId(String host, int port, String snmpEngineId)
{
    String key = getKey(host, port);
    hostLookup.put(key, snmpEngineId);
    if (AsnObject.debug > 4)
    {
        System.out.println();
        System.out.println("TimeWindow.setSnmpEngineId(): host "
            + host + ", port " + port
            + ", snmpEngineId " + snmpEngineId);
    }
}

/**
 * Checks if the engine ID is OK. If there is no engine ID known for
 * this host and port, the specified engine ID is added to the table. 
 * <p>
 * If there is
 * already an engine ID for this host and port, the method returns true
 * is they are the same and false if they differ. In the latter case the
 * engine ID in the table is not updated.
 * </p>
 *
 * @param host The hostname of the engine ID
 * @param port The port number of the engine ID
 * @param snmpEngineId The engine ID
 * @return whether the engine ID matches the stored engine ID 
 *
 * @see #setSnmpEngineId(String, int, String)
 */
public boolean isEngineIdOK(String host, int port, String snmpEngineId)
{
    boolean ok = true;
    String key = getKey(host, port);
    if (hostLookup.containsKey(key) == false)
    {
        setSnmpEngineId(host, port, snmpEngineId);
    }
    else
    {
        String myEngineId = getSnmpEngineId(host, port);
        if (myEngineId.equals(snmpEngineId) == false)
        {
            ok = false;
        }
    }
    return ok;
}

/**
 * Returns if the timeline details of this snmp engine ID are known. 
 *
 * @param snmpEngineId The engine ID
 * @return whether the timeline details are known
 */
public boolean isTimeLineKnown(String snmpEngineId)
{
    return engineLookup.containsKey(snmpEngineId);
}

/**
 * Returns if the time details are outside the time window.
 * When a response or report is received, first check the time window
 * before updating it. Always do an update, even if outside the time
 * window!
 *
 * @param snmpEngineId  The SNMP engine ID
 * @param bootsA The SNMP engine boots
 * @param timeA The SNMP engine time
 * @return true if outside or when no details can be found. false if
 * inside time window
 * @see #updateTimeWindow(String, int, int, boolean)
 */
public boolean isOutsideTimeWindow(String snmpEngineId, int bootsA, 
    int timeA)
{
    boolean isOut = false;

    TimeWindowNode node = getTimeLine(snmpEngineId);
    if (node != null)
    {
        int bootsL = node.getSnmpEngineBoots();
        int timeL = node.getSnmpEngineTime();
        if (bootsA == TimeWindowNode.maxTime
                ||
            bootsA < bootsL
                ||
           (bootsA == bootsL && timeA < (timeL-MaxTimeDifference)))
        {
            isOut = true;
        }
    }
    else
    {
        // We don't have any info, so by definition it is not out.
        isOut = false;
    }
    return isOut;
}

/**
 * Tries to update the time window and returns if succeeded. 
 * When a response or report is received, first check the time window
 * before updating it.
 *
 * <p>
 * An update will only occur if the message was authentic 
 * and the bootsA and timeA meet the requirements.
 * New data will be inserted if the (bootsA>0), irrespectively whether the
 * message was authentic or not.
 * </p>
 *
 * @param snmpEngineId  The SNMP engine ID
 * @param bootsA The SNMP engine boots
 * @param timeA The SNMP engine time
 * @return true if update succeeded, or false when not succeeded or when
 * no details could be found. 
 * @see #isOutsideTimeWindow(String, int, int)
 */
public boolean updateTimeWindow(String snmpEngineId, int bootsA, int
timeA, boolean isAuthentic)
{
    boolean updated = false;

    TimeWindowNode node = getTimeLine(snmpEngineId);
    if (node != null)
    {
        if (isAuthentic)
        {
            int bootsL = node.getSnmpEngineBoots();
            int latestL = node.getLatestReceivedEngineTime();

            if (bootsA > bootsL
                    ||
               (bootsA == bootsL && timeA > latestL))
            {
                synchronized (this)
                {
                    node.setSnmpEngineBoots(bootsA);
                    node.setSnmpEngineTime(timeA);
                    updated = true;
                }
            }
        }
    }
    else if (bootsA > 0)
    {
        node = new TimeWindowNode(snmpEngineId, bootsA, timeA);
        setTimeLine(snmpEngineId, node);
    }
    return updated;
}

/**
 * Updates the estimated engine time of all gathered time details. 
 * It calculats the seconds that
 * have been passed since the last call and updates all time window
 * nodes accordingly.
 *
 * @see #setTimeLine(String, TimeWindowNode)
 * @see #getTimeLine(String)
 */
protected void updateTimeWindows()
{
    if (engineLookup.size() > 0)
    {
        long now = System.currentTimeMillis();
        long milli = now - startTime;
        int sec = (int)(milli / 1000L);

        long lostMillis = milli - (sec * 1000L);
        if (lostMillis < 0L)
        {
            lostMillis = 0L;
        }
        startTime = now - lostMillis;

        Enumeration nodes = engineLookup.elements();
        while (nodes.hasMoreElements())
        {
            TimeWindowNode node = (TimeWindowNode) nodes.nextElement();
            node.incrementSnmpEngineTime(sec);
        }
    }
    else
    {
        startTime = System.currentTimeMillis();
    }
}


/**
 * Returns the key based on the specified hostname and port to the
 * engine ID lookup table.
 *
 * @param host The hostname
 * @param port The port
 * @return the key
 */
protected String getKey(String host, int port)
{
    return host + ":" + port;
}

/**
 * Returns the timeline details of the snmp engine ID. 
 * If there are no matching timeline details for this engine ID, null
 * will be returned. 
 * The timeline details will be updated before the node is retrieved
 * from the table.
 *
 * @param snmpEngineId The engine ID
 * @return The timeline details
 * @see #updateTimeWindows()
 */
protected TimeWindowNode getTimeLine(String snmpEngineId)
{
    updateTimeWindows();
    TimeWindowNode node = (TimeWindowNode) engineLookup.get(snmpEngineId);
    return node;
}

/**
 * Sets the timeline details of the snmp engine ID. 
 * The timeline details will be updated before the node is put
 * in the table.
 *
 * @param snmpEngineId The engine ID
 * @param newNode The added time window node node
 * @return The timeline details
 * @see #updateTimeWindows()
 */
protected TimeWindowNode setTimeLine(String snmpEngineId, 
    TimeWindowNode newNode)
{
    updateTimeWindows();
    engineLookup.put(snmpEngineId, newNode);
    if (AsnObject.debug > 4)
    {
        System.out.println();
        System.out.println("TimeWindow.setTimeLine(): snmpEngineId " 
            + snmpEngineId 
            + ", node " + newNode);
    }
    return newNode;
}

}

