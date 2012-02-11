/*
 * Copyright (c) 2000 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import java.io.*;

import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.console.hawkeye.*;

import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.rulebase.*;

/**
 * The example illustrate how an application can create, add, update, and
 * delete rulebase dynamically on an agent using rulebase and console API.
 *
 * usage:
 *
 *    java RBISample3 agentName licenseFile hawkDomain rvService rvNetwork rvDaemon
*/
class RBISample3 extends RBISample2 implements Runnable
{
	RBISampleConsole _console;

	/**
	 *  Construct RBISample3 using values from an argument list.
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
		Thread spotRBThread =  new Thread(new RBISample3(new RBISampleConsole(args)));
		spotRBThread.start();
	}
	
    RBISample3(RBISampleConsole console)
	{
		_console = console;
	}

	/**
	 * Set spot to the specified color.
     */
	void setColor(MicroAgentID maidSpot, String color) throws MicroAgentException
	{
		DataElement[] args = new DataElement[1];
		args[0] = new DataElement("Color", color);
		_console.invoke(maidSpot ,new MethodInvocation( "setColor", args) );
	}


	/**
	 * Add a rulebase to the agent.
	 * <p>
	 * It also use to update the rulebase on the agent.
     */
	void addRulebase(MicroAgentID maidRBE, Rulebase rb) throws MicroAgentException
	{
		RulebaseXML rbXML = new RulebaseXML(rb);
		DataElement[] dataElements = new DataElement[1];
		dataElements[0] = new DataElement("RulebaseXML", rbXML);
		MethodInvocation mi = new MethodInvocation( "addRuleBase", dataElements);
		_console.invoke(maidRBE ,mi );
	}

	/**
	 * Delete a rulebase on the agent.
     */
	void deleteRulebase(MicroAgentID maidRBE, String rb) throws MicroAgentException
	{
		DataElement[] args = new DataElement[1];
		args[0] = new DataElement("RuleBaseName", rb);
		MethodInvocation mi = new MethodInvocation( "deleteRuleBase", args);
		_console.invoke(maidRBE ,mi );
	}

	/**
	 * retrieve a rulebase from the agent.
     */
	Rulebase getRulebase(MicroAgentID maidRBE, String rbName) throws MicroAgentException, RulebaseException
	{
		// construct the method invocation object
		DataElement[] args = new DataElement[1];
		args[0] = new DataElement("RuleBase", rbName);
		MethodInvocation mi = new MethodInvocation("getRuleBases", args);
		MicroAgentData mad =  _console.invoke( maidRBE, mi );

		RulebaseXML[] rbXMLs = (RulebaseXML[])mad.getData();

		// return the retrieved rulebase
		if (rbXMLs.length == 1)
			return new Rulebase(rbXMLs[0].getXMLReader());
		return null;
	}

	/**
 	 * Perform the following steps:
	 *
	 * 1. Create a rulebase that will change spot color from blue to
	 *    green and add it to the agent.
	 * 2. Set spot on the agent to blue.
	 *
	 * 3. Retrieve the rulebase, modify it to change spot color from
	 *    blue/red to green, and update it on the agent.
	 *
	 * 4. Set spot on the agent to red.
	 *
	 * 5. Set spot on the agent to blue.
	 *
	 * 6. Delete the rulebase from the agent.
	 */
	public void run()
	{
		try
		{
			int step = 0;
			int loop = 0;
			int waitTime = subscriptionInterval + 5000;
			Rulebase rb = createRulebase(SAMPLE_SPOT_RB_NAME);

			while (true)
			{
				MicroAgentID maidRBE = _console.getRulebaseEngineMicroagentID();
				MicroAgentID maidSpot = _console.getSpotMicroagentID();

				if ((maidSpot == null) && loop > 7)
				{
				   println("Spot does not seem to be running on " + _console.getAgentName() + "...");
				   println("Please start spot on agent '" + _console.getAgentName() + "' to continue...");
				   loop = 0;
				}

			    switch (step)
				{
				case 0:
				   if ((maidSpot != null) && (maidRBE != null))
				   {
					   try
					   {
						   // Make sure rulebase to add is not on the agent.
						   // We do not want to override it.
						   Rulebase rbCheck = getRulebase(maidRBE, SAMPLE_SPOT_RB_NAME);
						   if (rbCheck != null)
						   {
							   println("Rulebase " + SAMPLE_SPOT_RB_NAME + " exist on agent...");
							   println("Please delete " + SAMPLE_SPOT_RB_NAME + " on agent '" + _console.getAgentName() + "' to continue...");
							   break;
						   }
					   } catch (Exception e)
					   {
						   println("Error getting rulebase " + e + " from agent\n Exception: " + e);
						   System.exit(0);
					   }

					   // start spot in red
					   println("Changing spot's color to red");
				       setColor( maidSpot, "red");

					   addRulebase(maidRBE, rb);

					   println("Added rulebase to turn spot's color from blue to green.");
					   step++;
				   }
				   break;
				case 1:
				   if ((maidSpot != null))
				   {
					   // set spot to blue for the test in the rulebase
					   println("Changing spot's color to blue");
					   setColor( maidSpot, "blue");
					   println("Spot's color should turn to green in a moment.");
					   step++;
				   }
				   break;
				case 2:
				   if ((maidSpot != null) && (maidRBE != null))
				   {
					   try
					   {
						   // Get rulebase added in step 1.
						   Rulebase rbCheck = getRulebase(maidRBE, SAMPLE_SPOT_RB_NAME);
						   if (rbCheck == null)
						   {
							   println("Rulebase " + SAMPLE_SPOT_RB_NAME + " do not exist on agent...");

							   // create it again?
							   step = 0;
							   break;
						   }

						   rb = rbCheck;
					   }
					   catch (Exception e)
					   {
						   println("Error getting rulebase " + e + " from agent\n Exception: " + e);
						   step = 0;
						   break;
					   }

					   // Update to a more complex one
					   makeRulebaseMoreComplex(rb);

					   addRulebase(maidRBE, rb);
					   println("Change rulebase to turn spot's color from blue/red to green.");

					   step++;
				   }
				   break;
				case 3:
				   if ((maidSpot != null))
				   {
					   // set spot to blue for the test in the rulebase
					   println("Changing spot's color to red");
					   setColor( maidSpot, "red");
					   println("Spot's color should turn to green in a moment.");
					   step++;
				   }
				   break;
				case 4:
				   if ((maidSpot != null))
				   {
					   // set spot to blue for the test in the rulebase
					   println("Changing spot's color to blue");
					   setColor( maidSpot, "blue");
					   println("Spot's color should turn to green in a moment.");
					   step++;
				   }
				   break;
				case 5:
				   if ((maidSpot != null) && (maidRBE != null))
				   {
					   // delete rulebase.
					   deleteRulebase(maidRBE ,SAMPLE_SPOT_RB_NAME);
					   println("Deleted rulebase to turn spot's color from blue to green.");

					   println("Changing spot's color to blue");
					   setColor( maidSpot, "blue");
					   println("Spot's color sould stay in blue");

					   step++;
				   }
				   break;
				default:
				   System.exit(0);
				}

				loop++;
				Thread.sleep(waitTime);
		   }
		}
		catch (Exception e)
		{
		   println("Error running sample... " + e + "\n Exception: " + e);
		   System.exit(3);
		}
	}
}
	
