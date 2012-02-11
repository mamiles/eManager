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
import COM.TIBCO.hawk.config.rbengine.schedule.*;

/**
 * This example extends ScheduleUsingExclusion to illustrate how an application updates schedules
 * from a Hawk Repository.
*/
class ScheduleGetAndSet extends ScheduleUsingExclusion implements ScheduleSampleConstant, Runnable
{
	ScheduleSampleConsole _console;

	/**
	 *  Construct ScheduleGetAndSet using values from an argument list.
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
		try
		{
			new ScheduleGetAndSet(args);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			println("Error: e="+e);
		}
	}
	
	/**
	 * Create a ScheduleGetAndSet.
	 */
   ScheduleGetAndSet(String args[]) throws RBEConfigObjectException, IOException
	{
		_console = new ScheduleSampleConsole(args);

		Thread sample3 = new Thread(this);
		sample3.start();
	}

	/**
	 * Retrieve a schedules from the repository.
     */
	Schedules getSchedules(MicroAgentID maidRepo) throws MicroAgentException, RBEConfigObjectException, ScheduleException
	{
		// construct the method invocation object
		MethodInvocation mi = new MethodInvocation("getSchedules", null);
		MicroAgentData mad =  _console.invoke( maidRepo, mi );

		Object o = mad.getData();
		if (o instanceof SchedulesXML)
		{
			SchedulesXML rbXMLs = (SchedulesXML)mad.getData();

		   // return the retrieved Schedules
		   return new Schedules(rbXMLs.getXMLReader());
		}
		// if error occurred on the agent, the returned object will be a MicroAgentException
		else if (o instanceof MicroAgentException)
			throw (MicroAgentException)o;
		else
			throw new ScheduleException("Unable to retrieve schedules. Error: "+ o);
	}

	/**
	 * Replace a schedules in the repository.
     */
	void setSchedules(MicroAgentID maidRepo, Schedules schedules) throws MicroAgentException, ScheduleException
	{
		// construct the method invocation object
		DataElement[] dataElements = new DataElement[2];
		dataElements[0] = new DataElement("SchedulesXML", new SchedulesXML(schedules));
		dataElements[1] = new DataElement("Notify" , new Boolean(false));
		MethodInvocation mi = new MethodInvocation("setSchedules", dataElements);
		MicroAgentData mad =  _console.invoke( maidRepo, mi );
	}

	/**
 	 * Perform the following steps:
	 *
	 * 1. Get the rulebase from the repository.
	 * 2. Update the schedules.
	 * 3. Replace the schedules in the repository.
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

				// try for 15 time only.
				if ((maidRepo == null) && loop > 15)
				{
				   println("Can't find a repository microagent on " + _console.getAgentName() + "...");
				   println("Please an agent with a repository...");
				   break;
				}

				if (maidRepo != null)
					{
					Schedules schedulesTmp = null;

					try
					{
						// Retrieve the schedules from the repository
						schedulesTmp = getSchedules(maidRepo);
						if (schedulesTmp != null)
							println("Schedules on agent '" + _console.getAgentName() + "' will be updated...");

						ScheduleCreateAndSave.save(schedulesTmp, SAMPLE_SCHEDULES_NAME+_console.getAgentName()+schedulesTmp.getFileExtension());

					} catch (Exception e)
					{
						e.printStackTrace();
						println("Error getting schedules from agent\n Exception: " + e);
						System.exit(2);
					}

					try
					{
						// delete MondayOnly from the schedules.
						deleteMondayOnly(schedulesTmp);

						// add OffBusinessHourInSummer to the schedules.
						addBusinessHourInSummer(schedulesTmp); 

						// replace the schedules on the agent with the updated schedules.
						setSchedules(maidRepo, schedulesTmp);
						if (schedulesTmp != null)
							println("Schedules on agent '" + _console.getAgentName() + "' has been replaced...");
					} catch (Exception e)
					{
						e.printStackTrace();
						println("Error replacing schedules on agent\n Exception: " + e);
						System.exit(3);
					}

					try
					{
						// re-load the schedule from the agent to save it on the load disk.
						Schedules rbmReloaded = getSchedules(maidRepo);
						if (rbmReloaded != null)
						   ScheduleCreateAndSave.save(rbmReloaded, SAMPLE_SCHEDULES_NAME+_console.getAgentName()+"Reloaded"+rbmReloaded.getFileExtension());

					} catch (Exception e)
					{
						e.printStackTrace();
						println("Error getting schedules from agent\n Exception: " + e);
						System.exit(4);
					}
					System.exit(0);
				}
            else
				{
					// continue looping until the repository agent is found
				   loop++;
				   Thread.sleep(waitTime);
				}
		   }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		   println("Error running ScheduleGetAndSet...\n Exception: " + e);
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

