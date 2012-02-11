/*
 * Copyright (c) 2002 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import java.util.Vector;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import COM.TIBCO.hawk.config.rbengine.RBEConfigObjectException;
import COM.TIBCO.hawk.config.rbengine.schedule.Schedules;
import COM.TIBCO.hawk.config.rbengine.schedule.Period;
import COM.TIBCO.hawk.config.rbengine.schedule.Schedule;

/**
 * This example create a schedule and save it to a file.
 *
 * The schedule created is a schedule that contain a period which
 * will be in-schedule between 8:00am to 5:59pm every Monday.
*/
class ScheduleCreateAndSave implements ScheduleSampleConstant
{
	public static void main(String args[])
	{
		try
		{
			ScheduleCreateAndSave sample = new ScheduleCreateAndSave();
			sample.doIt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			println("Error: e="+e);
		}
	}

	/**
	 * Constructor for ScheduleUsingExclusion
	 */
	ScheduleCreateAndSave()
	{
	}

	/**
	 * Main method of the sample.
	 */
	void doIt() throws RBEConfigObjectException, IOException
	{
		// Create a schedules
		Schedules schedules = new Schedules();

		// add a schedule that include a period for 8:00am to 6:00pm every Monday
		addMondayOnly(schedules);

		// Save it to a file
		save(schedules, SAMPLE_SCHEDULES_NAME+schedules.getFileExtension());

		// load the schedules from and check if it is equals to the original.
		Schedules schedulesLoaded = load(SAMPLE_SCHEDULES_NAME+schedules.getFileExtension());
		if (!schedules.equals(schedulesLoaded))
			println("Error saving schedules to file!");
	}

	/**
	* Add a schedule MondayOnly - All day for every Monday.
	*/
    static void addMondayOnly(Schedules schedules) throws RBEConfigObjectException
    {
		// Create a new inclusion period
		Period ip = new Period();

		// Include minutes between 8:00am to 5:59pm.
		// Note: 8:00am = 480 and 6:00pm = 1080
		//       By default a new period includes minutes from
		//       9:00am to 5:00pm. However, all minutes are
		//       between 8:00am to 5:59pm explicitely included here.
		for (int i = 480; i < 1080; i++)
			ip.include(Period.MINUTES_IN_DAY, i);

		// Change days in week to include only monday
		// Note: sunday = 0, saturday = 6.
		ip.excludeAll(Period.WEEKDAYS_IN_MONTH);
		ip.include(Period.WEEKDAYS_IN_MONTH, Period.MONDAY);

		// Note: By default a new period includes all Period.DAYS_IN_MONTH.
		//       So there no need to explicitely include them here.

		// create a new schedule and add the inclusion period to it.
		Schedule schedule = new Schedule("MondayOnly");
		schedule.addInclusionPeriod(ip);

		// add the schedule to the list of schedule
		schedules.addSchedule(schedule);
		println("Schedule added: ("+schedule+")");
    }


	/**
	* Save a schedules to a file.
	*/
    static void save(Schedules schedules, String filename) throws IOException, RBEConfigObjectException
    {
		FileWriter fw = new FileWriter(filename);
		schedules.toXML(fw);
		fw.close();
    }

	/**
	* Load a schedules from a file.
	*/
   static Schedules load(String fileName) throws RBEConfigObjectException
   {

      try
      {
         FileReader fis = new FileReader(fileName);
		   return new Schedules(fis);
      } catch (Exception ioe) {
         throw new RBEConfigObjectException(ioe.toString());
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
	
