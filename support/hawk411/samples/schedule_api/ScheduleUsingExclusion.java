/*
 * Copyright (c) 2002 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import java.util.Vector;
import java.io.*;

import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.schedule.*;

/**
 * This example create a schedule and save it to a file.
 *
 * The schedule created is named BusinessHourInSummer and will be in-schedule 
 * from Monday to Friday 8:00am to 5:59pm except 12:00pm to 2:00pm in the months
 * June, July and August.  This schedule definition uses both inclusion and 
 * exclusion period. The same schedule can be created without using the exclusion 
 * period by including more specific inclusion period.
*/
class ScheduleUsingExclusion extends ScheduleCreateAndSave
{
	public static void main(String args[])
	{
		try
		{
			ScheduleUsingExclusion sample = new ScheduleUsingExclusion();
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
	ScheduleUsingExclusion()
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

		// Delete the schedule MondayOnly 
		deleteMondayOnly(schedules);

		// Add a schedule BusinessHourInSummer 
		// (Mon-Fri 8:00am to 6:pm except 12:00pm to 2:00pm, from June to August)
		addBusinessHourInSummer(schedules);

		// Save it to a file
		save(schedules, SAMPLE_SCHEDULES_NAME+schedules.getFileExtension());
		Schedules schedulesLoaded = load(SAMPLE_SCHEDULES_NAME+schedules.getFileExtension());
		if (!schedules.equals(schedulesLoaded))
			println("Error saving schedules to file!");
	}

	/**
	 * Delete the schedule MondayOnly from the list of schedule.
	 */
    void deleteMondayOnly(Schedules schedules) throws RBEConfigObjectException
	 {
		 schedules.removeSchedule("MondayOnly");
	 }

	/**
	* Add a schedule BusinessHourInSummer 
	* - Mon-Fri 8:00am to  6:00pm except 12:00pm to 2:00pm, from June to August
	* - Sat     8:00am to 12:00pm from June to August
	*/
     void addBusinessHourInSummer(Schedules schedules) throws RBEConfigObjectException
    {
		// create a new schedule and add the inclusion period to it.
		Schedule schedule = new Schedule("BusinessHourInSummer");

		includeWeekDays8to6(schedule);
		exclude12to2(schedule);
		includeSaturdayMorning(schedule);

		// add the schedule to the list of schedule
		schedules.addSchedule(schedule);
		println("Schedule added: ("+schedule+")");
    }

	/**
	 * Adds an inclusion period for week days from 8 to 6 to the schedule.
	 */
	 void includeWeekDays8to6(Schedule schedule)
	 {
		// create a new inclusion period for week day 8 to 6
		Period ipWD826 = new Period();

		// 8:00am is time interval 480 (8 * 60).
		// 6:00pm is time interval 1080 (18 * 60)
		for (int i = 480; i < 1080; i++)
	   	ipWD826.include(Period.MINUTES_IN_DAY, i);

		// include weekdays in days in week
	   ipWD826.excludeAll(Period.WEEKDAYS_IN_MONTH);
		for (int i = Period.MONDAY; i <= Period.FRIDAY; i++)
	   	ipWD826.include(Period.WEEKDAYS_IN_MONTH, i);

		// include Jun to Aug in months in year
		// exclude every month in the year first as
		// the default includes every month in the year
	   ipWD826.excludeAll(Period.MONTHS_IN_YEAR);
		for (int i = Period.JUNE; i <= Period.AUGUST; i++)
	   	ipWD826.include(Period.MONTHS_IN_YEAR, i);

		schedule.addInclusionPeriod(ipWD826);
		println("Added inclusion period: " + ipWD826);
	 }

	/**
	 * Adds an inclusion period for weekend the schedule.
	 */
	 void includeSaturdayMorning(Schedule schedule)
	 {
		// create a new inclusion period for weekend
		Period ipWE = new Period();

		// 8:00am is time interval 480 (8 * 60).
		// 12:00pm is time interval 720 (12 * 60)
		// since the default includes 9am to 5pm,
		//  first exclude all then include only what we need.
	   ipWE.excludeAll(Period.MINUTES_IN_DAY);
		for (int i = 480; i < 720; i++)
	   	ipWE.include(Period.MINUTES_IN_DAY, i);

		// get days in week and change it to only monday
		// note: sunday = 0, saturday = 6.
	   ipWE.excludeAll(Period.WEEKDAYS_IN_MONTH);
	   ipWE.include(Period.WEEKDAYS_IN_MONTH, Period.SATURDAY);

		// include Jun to Aug in months in year
		// since the default includes every month in the year
		// just exclude Jan to May and Sep to Dec.
		for (int i = 0; i < 5; i++)
	   	ipWE.exclude(Period.MONTHS_IN_YEAR, i);
		for (int i = 8; i < 12; i++)
	   	ipWE.exclude(Period.MONTHS_IN_YEAR, i);

		schedule.addInclusionPeriod(ipWE);
		println("Added inclusion period: " + ipWE);
	 }

	/**
	 * Adds an exclusion period for 12:00pm to 2:00pm.
	 */
	 void exclude12to2(Schedule schedule)
	 {
		// create a new exclusion period for weekday 12-2
		Period xpWD = new Period();

		// 12:00pm is time interval 720 (12 * 60).
		// 2:00pm is time interval  840 (14 * 60).
	   xpWD.excludeAll(Period.MINUTES_IN_DAY);
		for (int i = 720; i < 840; i++)
	  	    xpWD.include(Period.MINUTES_IN_DAY, i);

		// need to include days in week because
		// by default they are excluded.
	   xpWD.includeAll(Period.WEEKDAYS_IN_MONTH);

		schedule.addExclusionPeriod(xpWD);
		println("Added exclusion period: " + xpWD);
	 }
}
	
