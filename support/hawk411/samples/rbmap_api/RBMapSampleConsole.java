/*
 * Copyright (c) 2002 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */


import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.console.*;
import COM.TIBCO.hawk.console.hawkeye.*;

/**
 * RBISampleConsole is a simple console that keep track of rulebase engine
 * and repository microagent on a given agent.  It is used by the rulebase map API
 * samples to demonstrate how a console application interact with an agent
 * or a repository.
 */
class RBMapSampleConsole implements RBMapSampleConstant,
	AgentMonitorListener, MicroAgentListMonitorListener, ErrorExceptionListener
{
	private TIBHawkConsole _console;
	private AgentMonitor _agentMonitor;
	private AgentManager _agentManager;

   private AgentInstance _agntInst;
	private String _agentName;
	private MicroAgentID _maidRBE;
	private MicroAgentID _maidRepo;

	private static final boolean DEBUG = false;

	/**
	 *  Construct RBISampleConsole using values from an argument list.
	 *  <p>
	 *  Argument 1: The agent to interact with.
	 *			    Default is the local host.
	 *  <p>
	 *  Argument 2: The hawk domain on which to communicate.
	 *			    Default to "" if not specified.
	 *  <p>
	 *  Argument 3: The TIBCO Rendezvous service parameter to use for console communication.
	 *			    Default to "7475" if not specified.
	 *  <p>
	 *  Argument 4: The TIBCO Rendezvous network parameter to use for console communication.
	 *			    Default to ";" if not specified.
	 *  <p>
	 *  Argument 5: The TIBCO Rendezvous daemon parameter to use for console communication.
	 *			    Default to "tcp:7475" if not specified.
	 */
	public RBMapSampleConsole(String args[])
	{
		String agentName = null;
		String hawkDomain = HAWK_DOMAIN;
		String rvService = RV_SERVICE;
		String rvNetwork = RV_NETWORK;
		String rvDaemon = RV_DAEMON;

		// Set agent name
		if (args.length > 0)
		   agentName = args[0];

		// Set hawk domain
		if (args.length > 1)
		   hawkDomain = args[1];

		// Set rv service
		if (args.length > 2)
		   rvService = args[2];

		// Set rv network
		if (args.length > 3)
		   rvNetwork = args[3];

		// Set rv daemon
		if (args.length > 4)
		   rvDaemon = args[4];

		// if agent name is not specified use the local host name
		if (agentName == null)
		{
			try
			{
				java.net.InetAddress hostInetInfo = java.net.InetAddress.getLocalHost();
				agentName = hostInetInfo.getHostName();
			}
			catch (java.net.UnknownHostException uhe)
			{
				println("Unable to retrieve host name.\n"+uhe.toString());
				System.exit(1);
			}
		}

		create(agentName, hawkDomain, rvService, rvNetwork, rvDaemon);
	}

    /**
	 * Constructs a RBISampleConsole using specified values.
     * <p>
     * The parameters are used to set up the console's communications channels.
     * The <code>hawkDomain</code> and <code>rvService</code>
     * parameters must be the same as the <b>hawk domain</b> and
     * TIBCO Rendezvous <b>service</b> configuration parameters used by
     * the agents in order to communicate with them.
     *
     * @param hawkDomain The hawk domain on which to communicate
     * @param rvService The TIBCO Rendezvous service parameter to use for console communication.
     *                  See the TIBCO Rendezvous documentation for more information.
     * @param rvNetwork The TIBCO Rendezvous network parameter to use for console communication.
     *                  See the TIBCO Rendezvous documentation for more information.
     * @param rvDaemon  The TIBCO Rendezvous daemon parameter to use for console communication.
     *                  See the TIBCO Rendezvous documentation for more information.
     */
	public RBMapSampleConsole(String agentName, String hawkDomain, String rvService, String rvNetwork, String rvDaemon)
	{
		create(agentName, hawkDomain, rvService, rvNetwork, rvDaemon);
	}


    /**
	 * Constructs a TIBHawkConsole using specified values.
     */
	 void create(String agentName, String hawkDomain, String rvService, String rvNetwork, String rvDaemon)
	{
		log("Agent Name: "+agentName);
		log("Hawk Domain: "+hawkDomain);
		log("RV Service: "+rvService);
		log("RV Network: "+rvNetwork);
		log("RV Daemon: "+rvDaemon);
		log(" ");

		try {
			_agentName = agentName;

			// setup console
			_console = new TIBHawkConsole(hawkDomain,rvService,rvNetwork,rvDaemon);

			_agentMonitor = _console.getAgentMonitor();
			_agentManager = _console.getAgentManager();

			// create and add listener for console errors
			_agentMonitor.addErrorExceptionListener(this);
		
			// create and add listener for agents
			_agentMonitor.addAgentMonitorListener(this);
		
			// create and add listener for microagents
			_agentMonitor.addMicroAgentListMonitorListener(this);
		
		}
		catch (java.util.TooManyListenersException e)
		{
			println("Too many Listeners in Hawk console:"+e.toString());
			System.exit(1);
		}

		try
		{
			_agentMonitor.initialize();
			_agentManager.initialize();
		}
		catch (ConsoleInitializationException e)
		{
			println("Unable to initilize Hawk console: "+e.toString());
			System.exit(1);
		}
	}

	/**
	 *  Clean up the colsole.
	 */
	final void cleanUp()
	{
		_agentMonitor.removeErrorExceptionListener(this);
		_agentMonitor.removeAgentMonitorListener(this);
		_agentMonitor.removeMicroAgentListMonitorListener(this);

		_agentMonitor.shutdown();
		_agentManager.shutdown();

		System.exit(0);
	}

	/**
	 *  Retrieve the agent name.
	 */
	String getAgentName() { return _agentName; }

	/**
	 *  Retrieve the rulebase microagent ID.
	 */
	MicroAgentID getRulebaseEngineMicroagentID() { return _maidRBE; }

	/**
	 *  Retrieve the spot microagent ID.
	 */
	MicroAgentID getRepositoryMicroagentID() { return _maidRepo; }

    /**
	 * Invoke a MethodInvocation an a given microagent ID.
	 */
    MicroAgentData invoke(MicroAgentID maid, MethodInvocation mi)
       throws MicroAgentException
    {
        AgentManager am = _console.getAgentManager();
        return( am.invoke(maid, mi) );
    }


	/**
	 * Save rulebase engine or spot microagent IDs when they are deteceted.
	 */
	void addMicroagent(MicroAgentID mID)
	{
		log("addMicroagent:  " + mID.getAgent().getName() + ":" + mID.getName());

		if (mID.getName().equals(REPOSITORY_MICROAGENT_NAME))
			_maidRepo = mID;
		else if (mID.getName().equals(RULEBASEENGINE_MICROAGENT_NAME))
			_maidRBE = mID;
	}

	/**
	 * Remove rulebase engine or spot microagent IDs when they are expired.
	 */
	void removeMicroagent(MicroAgentID mID)
	{
		log("removeMicroagent:  " + mID.getAgent().getName() + ":" + mID.getName());

		if (mID.getName().equals(REPOSITORY_MICROAGENT_NAME))
			_maidRepo = null;
		else if (mID.getName().equals(RULEBASEENGINE_MICROAGENT_NAME))
			_maidRBE = null;
	}


    /**
     * Process event when an agent is discovered.
     */
	public synchronized void onAgentAlive(AgentMonitorEvent event)
	{
		AgentInstance _agntInst = event.getAgentInstance();
		//log("onAgentAlive:  " + agntInst.getAgentID().getName()+":"+ agntInst.getAgentID().getHawkDomain());

		if ( !_agntInst.getAgentID().getName().equals(_agentName) )
			return;
		log("Found agent: "+_agentName);

		MicroAgentID[] mAgentIDs = _agntInst.getStatusMicroAgents();
		for (int i=0; i< mAgentIDs.length; i++)
		 	addMicroagent(mAgentIDs[i]);
	}

    /**
     * Process event when an agent have expired.
     */
	public synchronized void onAgentExpired(AgentMonitorEvent event)
	{
		AgentInstance agntInst = event.getAgentInstance();
		//log("onAgentExpired:  " + agntInst.getAgentID().getName()+":"+ agntInst.getAgentID().getHawkDomain());

		if ( !agntInst.getAgentID().getName().equals(_agentName) )
			return;
		println("Loosing agent: "+_agentName);

		MicroAgentID[] mIDs = agntInst.getStatusMicroAgents();
		for (int i=0; i< mIDs.length; i++)
		{
		 	removeMicroagent(mIDs[i]);
		}
	}

    /**
     * Process event when a microAgent is added.
     */
	public synchronized void onMicroAgentAdded(MicroAgentListMonitorEvent event)
	{
		MicroAgentID mID = event.getMicroAgentID();
		addMicroagent(mID);
	}

    /**
     * Process event when a microAgent is removed.
     */
	public synchronized void onMicroAgentRemoved(MicroAgentListMonitorEvent event)
	{
		MicroAgentID mID = event.getMicroAgentID();
		removeMicroagent(mID);
	}

    /**
     * Process event when error is signaled.
     */
	public synchronized void onErrorExceptionEvent(ErrorExceptionEvent event)
	{
		println("onErrorExceptionEvent: event=" + event);
	}

	/**
	 *  Print a line of text to console.
	 */
	void println(String s)
	{
		System.out.println(s);
	}

	/**
	 *  Print a line of text to console if debug is on.
	 */
	void log(String s)
	{
		if (!DEBUG)
			return;
		System.out.println(s);
	}
}
	
