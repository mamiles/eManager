/*
 * Copyright (c) 1999-2001 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import COM.TIBCO.hawk.console.hawkeye.*;
import COM.TIBCO.hawk.talon.*;

/*
 This class is provided purely for demonstration purposes. The format of its
 output is subject to change. This class illustrates  how to perform
 a method invocation and a method subscription using TIBCO Hawk's Console API.

 This class tries to retrieve the MicroAgentID for the "Self MicroAgent" from
 ANY agent in the TIBCO Hawk network. It then performs a AgentManager.describe(),
 AgentManager.invoke() and AgentManager.subscribe() and prints all its
 results to the screen.
*/

public class ConsoleApp
{
    TIBHawkConsole _console;
    AgentManager   _agentMgr;
    Subscription   _subscription;

    ConsoleApp(String hawkDomain, String rvService,
               String rvNetwork, String rvDaemon)
    {
        // Create the TIBHawkConsole Instance
        _console = new TIBHawkConsole(hawkDomain,rvService,rvNetwork,rvDaemon);
        // retrieve and initialize AgentManager
        _agentMgr = _console.getAgentManager();
        try {
            _agentMgr.initialize();
        }
        catch( Exception e ) {
            System.out.println("ERROR while initializing AgentManager: " + e);
            System.exit(1);
        }

        // let's try and retrieve the COM.TIBCO.hawk.talon.MicroAgentID for the
        // "Self Microagent" from ANY agent in the TIBCO Hawk network

        MicroAgentID[] maids = null;
        try {
            maids = _agentMgr.getMicroAgentIDs("COM.TIBCO.hawk.microagent.Self", 1);
            if( maids.length == 0 ) {
                System.out.println("EXITING ... Unable to find any TIBCO Hawk agents\n"+
                                   "for the specified RV session and Hawk domain parameters.");
                System.exit(1);
            }
            System.out.println(maids[0].toString());
        }
        catch( MicroAgentException mae ) {
            System.out.println("ERROR while performing getMicroAgentIDs: " + mae);
            System.exit(1);
        }

        // let's do a COM.TIBCO.hawk.console.hawkeye.AgentManager.describe()
        // describe() returns a COM.TIBCO.hawk.talon.MicroAgentDescriptor
        // check out the documentation of MicroAgentDescriptor

        MicroAgentDescriptor mad = null;
        try {
            mad = _agentMgr.describe(maids[0]);
            System.out.println(mad.toFormattedString());
        }
        catch( MicroAgentException mae ) {
            System.out.println("ERROR while performing a describe: " + mae);
            System.exit(1);
        }

        // we know Self has a getUptime() method which takes no arguments
        // let's invoke Self.getUptime()
        String methName = "getUptime";
        MethodInvocation mi = new MethodInvocation(methName, null);
        try {
            MicroAgentData m = _agentMgr.invoke(maids[0], mi);
            Object maData = m.getData();
            if( maData != null ) {
                System.out.println("\nResults of Method Invocation\n");
                printData( maData );
            }
        }
        catch( MicroAgentException me ) {
            System.out.println("ERROR while performing a method invocation: " + me);
            System.exit(1);
        }

        // subscribe to Self.getMicroAgentInfo() every 15 secs
        // we know that getMicroAgentInfo() takes an argument "Name"
        // we could examine MethodDescriptor.getArgumentDescriptor() as well !!
        DataElement[] args = new DataElement[1];
        args[0] = new DataElement("Name", new String(""));
        MethodSubscription mas = new MethodSubscription("getMicroAgentInfo", args, 15*1000);
        try {
            _subscription = _agentMgr.subscribe(maids[0], mas, new Subscriber(), null);
            System.out.println("\nResults of Method Subscription\n");
        }
        catch( MicroAgentException e ) {
            System.out.println("ERROR while performing a method subscription: " + e);
            System.exit(1);
        }
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
    class Subscriber implements SubscriptionHandler
    {
        // after we get data a few times let's terminate the subscription
        int noOfTimes = 0;

        public void onData( Subscription s, MicroAgentData mad )
        {
            Object maData = mad.getData();

            if( maData != null ) {
                printData( maData );
                System.out.println("\n\n");
            }

            noOfTimes++;

            if( noOfTimes == 3 ) {
                _subscription.cancel();
                System.exit(1);
            }
        }
        public void onError( Subscription s, MicroAgentException e )
        {
            System.out.println("ERROR occured during subscription: " + e);
        }
        public void onErrorCleared( Subscription s )
        {
            System.out.println("ERROR cleared resuming subscription: " + s);
        }
        public void onTermination( Subscription s, MicroAgentException e )
        {
            System.out.println("Subscription Terminated: " + e);
            System.exit(1);
        }
    }

    // Main - Usage: java ConsoleApp <hawkDomain> <rvService> <rvNetwork> <rvDaemon>
    public static void main(String[] args)
    {
        ConsoleApp ca = null;

        if (args.length != 4 ) {
            System.err.println("Usage: java ConsoleApp <hawkDomain> <rvService> <rvNetwork> <rvDaemon>");
            System.exit(1);
        }
        System.out.println("NOTICE:\n"+
            "This utility is provided as an example use of the TIBHawkConsole API.\n"+
            "It is intended to be used purely for demonstration and educational purposes ONLY.\n"
        );

        if (args.length == 4)
            ca = new ConsoleApp(args[0],args[1],args[2],args[3]);
    }
}
