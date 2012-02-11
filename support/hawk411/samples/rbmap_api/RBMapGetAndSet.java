/*
 * Copyright (c) 2002 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import java.io.*;

import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.console.hawkeye.*;

import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.rbmap.*;

/**
 * This example extends RBMapUseCommand to illustrate how an application can update rulebase map
 * from a Hawk Repository.
*/
class RBMapGetAndSet implements RBMapSampleConstant ,Runnable
{
	RBMapSampleConsole _console;

	/**
	 *  Construct RBMapGetAndSet using values from an argument list.
	 *  <p>
	 *  Argument 1: The agent to intercate with.
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
	public static void main(String args[])
	{
		Thread rbmThread =  new Thread(new RBMapGetAndSet(new RBMapSampleConsole(args)));
		rbmThread.start();
	}
	
	/**
	 * Create a RBMapGetAndSet.
	 */
   RBMapGetAndSet(RBMapSampleConsole console)
	{
		_console = console;
	}

	/**
	* Update a rulebase map with the following mapping:
	* - agent1A, agent1B, and agent1C are in +group1
	* - agent1A", agent1B", and agent1C are using rulebase1A 
	* - +group1 and +group2 are using rulebase12A and rulebase12B
	* - +group1 and +group2 are using rulebase12A and rulebase12B
	*/
	void updateRulebaseMap(RBMap rbMap) throws RBEConfigObjectException
	{
		String[] agentsArray = {"agent1A", "agent1B", "agent1C"};
		String[] groupsArray = {"group1", "group2"};
		String[] ossArray = {"++Windows"};

		rbMap.setMembersInGroup("+group1", agentsArray);

		rbMap.setMembersForRulebase("rulebase1A", agentsArray);
		rbMap.setMembersForRulebase("rulebase12A", groupsArray);
		rbMap.setMembersForRulebase("rulebase12B", groupsArray);
		rbMap.setMembersForRulebase("rulebaseNT", ossArray);
    }


	/**
	 * Retrieve a rulebase map from the specified microagent.
     */
	RBMap getRulebaseMap(MicroAgentID maidRepo) throws MicroAgentException, RBMapException
	{
		// construct the method invocation object
		MethodInvocation mi = new MethodInvocation("getRBMap", null);
		MicroAgentData mad =  _console.invoke( maidRepo, mi );

		Object o = mad.getData();

		// get the RBMap from the RBMapXML 
		if (o instanceof RBMapXML)
		{
			RBMapXML rbXMLs = (RBMapXML)mad.getData();

		   // return the retrieved rulebase
		   return new RBMap(rbXMLs.getXMLReader());
		}
		else if (o instanceof MicroAgentException)
			throw (MicroAgentException)o;
		else
			throw new RBMapException("Unable to retrieve rulebase map. Error: "+ o);
	}

	/**
	 * Replace a rulebase map in the specified microagent.
     */
	void setRulebaseMap(MicroAgentID maidRepo, RBMap rbMap) throws MicroAgentException, RBMapException
	{
		// construct the method invocation object
		DataElement[] dataElements = new DataElement[2];
		dataElements[0] = new DataElement("RBMapXML", new RBMapXML(rbMap));
		dataElements[1] = new DataElement("Notify" , new Boolean(false));
		MethodInvocation mi = new MethodInvocation("setRBMap", dataElements);

		// invoke the method
		MicroAgentData mad =  _console.invoke( maidRepo, mi );
	}

	/**
 	 * Perform the following steps:
	 *
	 * 1. Get the rulebase from the repository.
	 * 2. Update the rulebase map.
	 * 3. Replace the rulebase map in the repository.
	 */
	public void run()
	{
		try
		{
			MicroAgentID maidRepo = null;
			int loop = 0;
			int waitTime = subscriptionInterval + 5000;

			while (maidRepo == null)
			{
				maidRepo = _console.getRepositoryMicroagentID();

				if ((maidRepo == null) && loop > 15)
				{
				   println("Can't find a repository microagent on " + _console.getAgentName() + "...");
				   println("Please an agent with a repository...");
				   break;
				}

				if (maidRepo != null)
					{
					RBMap rbmCheck = null;

					try
					{
						rbmCheck = getRulebaseMap(maidRepo);
						if (rbmCheck != null)
							println("RBMap on agent '" + _console.getAgentName() + "' will be updated...");

						RBMapCreateAndSave.save(rbmCheck, SAMPLE_RB_MAP_NAME+_console.getAgentName()+rbmCheck.getFileExtension());

					} catch (Exception e)
					{
						e.printStackTrace();
						println("Error getting rulebase map from agent\n Exception: " + e);
						System.exit(2);
					}

					try
					{
						updateRulebaseMap(rbmCheck);
						setRulebaseMap(maidRepo, rbmCheck);
						if (rbmCheck != null)
							println("RBMap on agent '" + _console.getAgentName() + "' has been replaced...");
					} catch (Exception e)
					{
						e.printStackTrace();
						println("Error replacing rulebase map on agent\n Exception: " + e);
						System.exit(3);
					}

					try
					{
						RBMap rbmReloaded = getRulebaseMap(maidRepo);
						if (rbmReloaded != null)
						   RBMapCreateAndSave.save(rbmReloaded, SAMPLE_RB_MAP_NAME+_console.getAgentName()+"Reloaded"+rbmReloaded.getFileExtension());

					} catch (Exception e)
					{
						e.printStackTrace();
						println("Error getting rulebase map from agent\n Exception: " + e);
						System.exit(4);
					}
					System.exit(0);
				}
            else
				{
				   loop++;
				   Thread.sleep(waitTime);
				}
		   }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		   println("Error running RBMapGetAndSet...\n Exception: " + e);
		   System.exit(5);
		}
	}


	/**
	 *  Print a line of text to console.
	 */
	static void println(String s)
	{
		System.out.println(s);
	}
}

