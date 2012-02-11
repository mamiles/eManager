/*
 * Copyright (c) 2001 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;

import java.util.*;

/*
 This class is provided purely for demonstration purposes. The format of its
 output is subject to change. This class illustrates  how to perform
 a method invocation and a method subscription using TIBCO Hawk's Console API.

 This class tries to retrieve the MicroAgentID for the "RuleBaseEngine MicroAgent" from
 ANY 5 agents in the TIBCO Hawk network. It then performs a AgentManager.groupOp()
 and prints all its results to the screen.
*/

public class GroupOpSample
{
    TIBHawkConsole _console;
    AgentManager   _agentMgr;

    GroupOpSample(String hawkDomain, String rvService,
               String rvNetwork, String rvDaemon)
    {
        // Create the TIBHawkConsole Instance
        _console = new TIBHawkConsole(hawkDomain,rvService,rvNetwork,rvDaemon);
        // retrieve and initialize AgentManager
        _agentMgr = _console.getAgentManager();
        try {
            AgentMonitor amon = _console.getAgentMonitor();
            amon.initialize();
            _agentMgr.initialize();
        }
        catch( Exception e ) {
            System.out.println("ERROR while initializing AgentManager: " + e);
            System.exit(1);
        }

        // let's try and retrieve the COM.TIBCO.hawk.talon.MicroAgentID for the
        // "RuleBaseEngine Microagent" from ANY FIVE agents in the TIBCO Hawk network

        MicroAgentID[] maids = null;
        try {
            maids = _agentMgr.getMicroAgentIDs("COM.TIBCO.hawk.microagent.RuleBaseEngine", 5);
            if( maids.length == 0 ) {
                System.out.println("EXITING ... Unable to find any TIBCO Hawk agents\n"+
                                   "for the specified RV session and Hawk domain parameters.");
                System.exit(1);
            }
            System.out.println("\n----- Results of AgentManager.getMicroAgentIDs() -----\n");
            for( int i=0; i<maids.length; i++) {
                System.out.println(maids[i].toString());
            }
        }
        catch( MicroAgentException mae ) {
            System.out.println("ERROR while performing getMicroAgentIDs: " + mae);
            System.exit(1);
        }

        // temporary Hashtable of discovered MicroAgentIDs
        Hashtable maidTable = new Hashtable(maids.length);
        for( int kk = 0; kk < maids.length; kk++ ) {
             maidTable.put(maids[kk], maids[kk]);
        }

        // we know RuleBaseEngine has a getRuleBaseNames() method which takes no arguments
        // let's invoke RuleBaseEngine.getRuleBaseNames()
        String methName = "getRuleBaseNames";
        MethodInvocation mi = new MethodInvocation(methName, null);
        try {
            MicroAgentData reply[] = _agentMgr.groupOp(maids, mi);
            System.out.println("\n----- Results of Group Operation -----\n");
            for( int ii=0; ii<reply.length; ii++ ) {
                MicroAgentData m = reply[ii];
                MicroAgentID mid = m.getSource();

                // remove this id from the target microagentID hashtable
                maidTable.remove(mid);

                Object maData = m.getData();
                System.out.println("\n ----- Group Op results for agent " +
                                     maids[ii].getAgent().toString() + " ------\n");
                printData(maData);
            }
        }
        catch( MicroAgentException me ) {
            System.out.println("ERROR while performing Group Operation: " + me);
            System.exit(1);
        }

        // compute the agents that timed out
        // if the reply size is less than sizeof maids then certain
        // agents have failed to reply, walk thru' the individual
        // arrays looking for the agent(s) that timed out

        if( maidTable.size() > 0 ) {
        	System.out.println("\n ------  The following agents timed out --------- \n");
        }
        Enumeration e = maidTable.keys();
        while( e.hasMoreElements() ) {
            MicroAgentID m = (MicroAgentID)e.nextElement();
            System.out.println(m.getAgent().toString());
        }
        System.exit(1);
    }

    public void printData( Object madata )
    {
        // it could be CompositeData
        if( madata instanceof CompositeData ) {
            CompositeData compData = (CompositeData)madata;
            DataElement[] data = compData.getDataElements();

            StringBuffer sb = new StringBuffer("composite{");

            for (int i=0; i<data.length; i++)
                sb.append(data[i] + ((i==(data.length-1))?"}":", "));

            System.out.println(sb.toString());
        }
        // it could be TabularData
        else if( madata instanceof TabularData ) {
            TabularData tabData = (TabularData)madata;

            String[] columnNames = tabData.getColumnNames();
            String[] indexNames = tabData.getIndexNames();
            // alternatively you can use getAllDataElements() as well
            Object[][] table = tabData.getAllData();

            StringBuffer sb = new StringBuffer();
            sb.append("table{");
            sb.append("columns={");

            for (int i=0; i<columnNames.length; i++)
                sb.append(columnNames[i]+ ((i==(columnNames.length-1))?"} ":", "));

            sb.append("indexColumns={");

            for (int i=0; i<indexNames.length; i++)
                sb.append(indexNames[i]+ ((i==(indexNames.length-1))?"} ":", "));

            sb.append("values={");
            if (table==null)
                sb.append("null");
            else {
                for (int i=0; i<table.length; i++) {
                    sb.append("row"+i+"={");
                    for (int j=0; j<table[i].length; j++)
                        sb.append(table[i][j] + ((j==(table[i].length-1))?"} ":", "));
                }
            }
            sb.append("}");
            sb.append("}");

            System.out.println(sb.toString());
        }
        // it could be MicroAgentException .. security violations etc.
        else if( madata instanceof MicroAgentException ) {
            MicroAgentException exc = (MicroAgentException)madata;
            System.out.println("EXCEPTION: " + exc);
        }
        // it could be null - some IMPACT and IMPACT_INFO methods could return null
        else if( madata == null ) {
            System.out.println("Method Invocation returned NO data ");
        }
        // could be none of the above, possibly NOT a openMethod ??
        else {
            System.out.println("Method Invocation returned data of UNKNOWN TYPE");
        }
    }

    // Main - Usage: java GroupOpSample <hawkDomain> <rvService> <rvNetwork> <rvDaemon>
    public static void main(String[] args)
    {
        GroupOpSample app = null;

        if (args.length != 4 ) {
            System.err.println("Usage: java GroupOpSample <hawkDomain> <rvService> <rvNetwork> <rvDaemon>");
            System.exit(1);
        }
        System.out.println("NOTICE:\n"+
            "This utility is provided as an example use of the TIBHawkConsole API.\n"+
            "It is intended to be used purely for demonstration and educational purposes ONLY.\n"
        );

        if (args.length == 4)
            app = new GroupOpSample(args[0],args[1],args[2],args[3]);
    }
}
