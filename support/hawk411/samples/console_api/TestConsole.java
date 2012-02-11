/*
 * Copyright (c) 1999-2001 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import COM.TIBCO.hawk.console.hawkeye.*;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * This class implements a console test application that simply listens for all
 * console events and prints their contents.
 *<p>
 * This class is provided purely for demonstration purposes. The format of its
 * output is subject to change.
 *<p>
 * Usage: <blockquote><code>java TestConsole hawkDomain rvService rvNetwork rvDaemon</code></blockquote>
 */

import COM.TIBCO.hawk.talon.*;

public class TestConsole {
    TIBHawkConsole t;
    AgentMonitor c;

    DateFormat df = DateFormat.getInstance();

    TestConsole(String hawkDomain,
				String rvService,
				String rvNetwork,
				String rvDaemon,
				String licenseFile) {


	// Create the TIBHawkConsole Instance
	t = new TIBHawkConsole(hawkDomain,rvService,rvNetwork,rvDaemon);
	c = t.getAgentMonitor();



	// Add listeners for all possible events
	try {
	    c.addAgentMonitorListener(new AgentMonitorListener() {
		public void onAgentAlive(AgentMonitorEvent e) {
		    System.out.print("*** onAgentAlive ***");
		    printAgentMonitorEvent(e, System.out, 1);
		    System.out.println();
		}
		public void onAgentExpired(AgentMonitorEvent e) {
		    System.out.print("*** onAgentExpired ***");
		    printAgentMonitorEvent(e, System.out, 1);
		    System.out.println();
		}
	    });
	    c.addMicroAgentListMonitorListener(new MicroAgentListMonitorListener() {
		public void onMicroAgentAdded(MicroAgentListMonitorEvent e) {
		    System.out.print("*** onMicroAgentAdded ***");
		    printMicroAgentListMonitorEvent(e, System.out, 1);
		    System.out.println();
		}
		public void onMicroAgentRemoved(MicroAgentListMonitorEvent e) {
		    System.out.print("*** onMicroAgentRemoved ***");
		    printMicroAgentListMonitorEvent(e, System.out, 1);
		    System.out.println();
		}
	    });
	    c.addRuleBaseListMonitorListener(new RuleBaseListMonitorListener() {
		public void onRuleBaseAdded(RuleBaseListMonitorEvent e) {
		    System.out.print("*** onRuleBaseAdded ***");
		    printRuleBaseListMonitorEvent(e, System.out, 1);
		    System.out.println();
		}
		public void onRuleBaseRemoved(RuleBaseListMonitorEvent e) {
		    System.out.print("*** onRuleBaseRemoved ****");
		    printRuleBaseListMonitorEvent(e, System.out, 1);
		    System.out.println();
		}
	    });
	    c.addAlertMonitorListener(new AlertMonitorListener() {
		public void onRetransmittedAlert(AlertMonitorEvent e) {
		    System.out.print("*** Retransmitted Alert ***");
		    printPostAlertEvent((PostAlertEvent)e, System.out, 0);
		    System.out.println();
		}
		public void onAlertMonitorEvent(AlertMonitorEvent e) {
		    if (e instanceof PostAlertEvent) {
			System.out.print("*** PostAlertEvent ***");
			printPostAlertEvent((PostAlertEvent)e, System.out, 0);
			System.out.println();
		    } else if (e instanceof ClearAlertEvent) {
			System.out.print("*** ClearAlertEvent ***");
			printClearAlertEvent((ClearAlertEvent)e, System.out, 0);
			System.out.println();
		    }
		}
	    });
	    /*
	     * When an ErrorExceptionEvent is received, the application should exit.
	     */
	    c.addErrorExceptionListener(new ErrorExceptionListener() {
		public void onErrorExceptionEvent(ErrorExceptionEvent e) {
		    System.out.println("*** onErrorExceptionEvent ***");
		    printErrorExceptionEvent(e, System.out, 1);
		    System.out.println("Exiting");
		    System.exit(1);
		}
	    });
	    /*
	     * When a WarningExceptionEvent is received, the user should be warned.
	     */
	    c.addWarningExceptionListener(new WarningExceptionListener() {
		public void onWarningExceptionEvent(WarningExceptionEvent e) {
		    System.out.println("*** onWarningExceptionEvent ***");
		    printWarningExceptionEvent(e, System.out, 1);
		}
	    });
	} catch (java.util.TooManyListenersException e) {
	    e.printStackTrace();
	    System.exit(1);
	}

	try {
	    c.initialize();
	} catch (ConsoleInitializationException  e) {
	    e.printStackTrace();
	    System.exit(1);
	}

    }


    /***************************************************************************
     * The following printXXX() methods serve as examples of how to extract    *
     * data from all of the classes in the TIBCO Hawk Console API.             *
     * They also illustrate the event class hierarchy.                         *
     ***************************************************************************/
	
    private String indent(int i) {
	StringBuffer sb = new StringBuffer("\n");
	for (int j=0; j<i; j++) sb.append("  ");
	return sb.toString();
    }

    /*
     * MonitorEvent extends java.util.EventObject
     */
    private void printMonitorEvent(MonitorEvent e, PrintStream p, int i) {
 	p.print(indent(i++)+"MonitorEvent{");
	i++;
	printAgentInstance(e.getAgentInstance(), p, i);
	--i;
	p.print(indent(--i)+"}");
    }

    /*
     * AgentMonitorEvent extends MonitorEvent
     */
    private void printAgentMonitorEvent(AgentMonitorEvent e, PrintStream p, int i) {
	p.print(indent(i)+"AgentMonitorEvent{}");
	printMonitorEvent(e,p,i);
    }

    /*
     * MicroAgentListMonitorEvent extends MonitorEvent
     */
    private void printMicroAgentListMonitorEvent(MicroAgentListMonitorEvent e, PrintStream p, int i) {
	p.print(indent(i++)+"MicroAgentListMonitorEvent{");
	p.print(indent(i)+"microagent=");
	i++;
	printMicroAgentID(e.getMicroAgentID(),p,i);
	--i;
	p.print(indent(--i)+"}");
	printMonitorEvent(e,p,i);
    }

    /*
     * RuleBaseMonitorEvent extends MonitorEvent
     */
    private void printRuleBaseMonitorEvent(RuleBaseMonitorEvent e, PrintStream p, int i) {

	p.print(indent(i++)+"RuleBaseMonitorEvent{");
	p.print(indent(i)+"rulebase=");
        printRuleBaseStatus(e.getRuleBaseStatus(),p,i);
	p.print(indent(--i)+"}");
	printMonitorEvent(e,p,i);
    }

    /*
     * RuleBaseListMonitorEvent extends RuleBaseMonitorEvent
     */
    private void printRuleBaseListMonitorEvent(RuleBaseListMonitorEvent e, PrintStream p, int i) {
	p.print(indent(i++)+"RuleBaseListMonitorEvent{}");
	// RuleBaseListMonitorEvent adds no additional fields to RuleBaseMonitorEvent
	printRuleBaseMonitorEvent(e,p,i);
    }

    /*
     * AlertMonitorEvent extends RuleBaseMonitorEvent
     */
    private void printAlertMonitorEvent(AlertMonitorEvent e, PrintStream p, int i) {
	p.print(indent(i++)+"AlertMonitorEvent{");
	p.print(indent(i)+"engineState="+stateToString(e.getRuleBaseEngineState()));
	p.print(", stateChanged="+e.ruleBaseEngineStateChanged());
	p.print(", rbStateChanged="+e.ruleBaseStateChanged());
	p.print(", alertID="+e.getAlertID());
	p.print(", time="+df.format(new Date(e.getTimeGenerated())));
	p.print(indent(--i)+"}");
	printRuleBaseMonitorEvent(e,p,i);
    }
    /*
     * PostAlertEvent extends AlertMonitorEvent
     */
    private void printPostAlertEvent(PostAlertEvent e, PrintStream p, int i) {
	p.print(indent(i++)+"PostAlertEvent{");
	p.print(indent(i)+"state="+stateToString(e.getAlertState()));
	p.print(indent(i)+"text="+e.getAlertText());
	p.print(indent(i)+"isRetransmitted="+e.isRetransmittedAlert());
	p.print(indent(i)+"properties=[");
	String key;
	for (java.util.Enumeration en = e.getProperties().propertyNames();en.hasMoreElements();) {
	    key = (String)en.nextElement();
	    p.print(key+"="+e.getProperties().getProperty(key)+", ");
	}
	p.print("]");
	p.print(indent(--i)+"}");
	printAlertMonitorEvent(e,p,i);
    }

    /*
     * ClearAlertEvent extends AlertMonitorEvent
     */
    private void printClearAlertEvent(ClearAlertEvent e, PrintStream p, int i) {
	p.print(indent(i++)+"ClearAlertEvent{");
	p.print(indent(i)+"clearReason=\" "+e.getReasonClearedText()+"\" ");
	p.print(indent(--i)+"}");
	printAlertMonitorEvent(e,p,i);
    }

    /*
     * ErrorExceptionEvent
     */
    private void printErrorExceptionEvent(ErrorExceptionEvent e, PrintStream p, int i) {
	p.println("ErrorExceptionEvent: "+e.getConsoleError().toString());
    }

    /*
     * WarningExceptionEvent
     */
    private void printWarningExceptionEvent(WarningExceptionEvent e, PrintStream p, int i) {
	p.println("WarningExceptionEvent: "+e.getConsoleWarning().toString());
    }

    /*
     * AgentID
     */
    private void printAgentID(AgentID aid, PrintStream p, int i) {
	p.print(indent(i)+"AgentID{");
	p.print("name="+aid.getName()+", ");
	p.print("dns="+aid.getDns()+", ");
	p.print("hawkDomain="+aid.getHawkDomain());
	p.print("}");
    }

    /*
     * AgentInstance
     */
    private void printAgentInstance(AgentInstance ai, PrintStream p, int i) {
	p.print(indent(i++)+"AgentInstance{");
	printAgentID(ai.getAgentID(),p,i);
	p.print(", started="+df.format(new Date(ai.getStartTime())));
	p.print(indent(i)+"engineState="+stateToString(ai.getRuleBaseEngineState()));
	p.print(", isValid="+ai.isValid());
	p.print(", userData=["+ai.getUserData()+"]");
	p.print(indent(i)+"rulebases=[");
	// array of RuleBaseStatus
	for (int j=0; j<ai.getStatusRuleBases().length; j++)
	    printRuleBaseStatus(ai.getStatusRuleBases()[j],p,i);
	p.print("]");
	p.print(indent(i)+"microagents=[");
	// array of COM.TIBCO.hawk.talon.MicroAgentID
	for (int j=0; j<ai.getStatusMicroAgents().length; j++)
	    printMicroAgentID(ai.getStatusMicroAgents()[j],p,i);
	p.print("]");
	p.print(indent(i)+"cluster="+ai.getCluster());
	p.print(", ip="+ai.getIPAddress());
	p.print(", agentVersion="+
		  ai.getAgentVersion().getMajorVersion()+"."+
		  ai.getAgentVersion().getMinorVersion()+"."+
		  ai.getAgentVersion().getUpdateVersion()+", ");
	p.print("platform=("+
		  ai.getAgentPlatform().getOsName()+", "+
		  ai.getAgentPlatform().getOsVersion()+", "+
		  ai.getAgentPlatform().getOsArch()+")");
	p.print(indent(--i)+"}");
    }


    /*
     * COM.TIBCO.hawk.talon.MicroAgentID
     */
    private void printMicroAgentID(COM.TIBCO.hawk.talon.MicroAgentID m, PrintStream p, int i) {
	p.print(m.toString());
	/*
	p.print("{");
	p.print("name="+m.getName()+", ");
	p.print("displayName="+m.getDisplayName()+", ");
	p.print("instance="+m.getInstance());
	p.print("checksum="+m.getChecksum()+", ");
	p.print("isService="+m.isService());
	p.print("}");
	*/
    }

    /*
     * RuleBaseStatus
     */
    private void printRuleBaseStatus(RuleBaseStatus r, PrintStream p, int i) {
	p.print("{");
	p.print(r.getName()+", ");
	p.print(stateToString(r.getState())+", ");
	p.print("checksum="+r.getChecksum()+", ");
	p.print("alertIDs=[");
	for (int j=0; j<r.getAlertIDs().length; j++)
	    p.print(r.getAlertIDs()[j] +",");
	p.print("]");
	p.print("}");
    }

    private String stateToString(int state) {
	switch(state) {
	case AlertState.NO_ALERT:
	    return "NO_ALERT";
	case AlertState.ALERT_LOW:
	    return "ALERT_LOW";
	case AlertState.ALERT_MEDIUM:
	    return "ALERT_MEDIUM";
	case AlertState.ALERT_HIGH:
	    return "ALERT_HIGH";
	default:
	    return "UnknownState:"+state;
	}
    }

    /**
     * main
     *
     * Usage: java SampleConsole <hawkDomain> <rvService> <rvNetwork> <rvDaemon>
     */
    public static void main(String[] args) {
	TestConsole sc;

	if (args.length != 4 ) {
	    System.err.println("Usage: java TestConsole <hawkDomain> <rvService> <rvNetwork> <rvDaemon>");
	    System.exit(1);
	}
	System.out.println("NOTICE:\n"+
			   "This utility is provided as an example use of the TIBHawkConsole API.\n"+
			   "It is intended to be used purely for demonstration and educational purposes ONLY.\n"
			   );

	if (args.length == 4)
		sc = new TestConsole(args[0],args[1],args[2],args[3],null);
	}
}

