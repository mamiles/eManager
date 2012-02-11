/*
 * Copyright (c) 2002 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import java.util.*;
import java.io.*;
import java.text.*;

import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.schedule.*;

/**
 * This example extends ScheduleUsingExclusion to illustrate 
 * the use of PeriodGroup. 
 *
 * The exclusion period in the schedule created in ScheduleUsingExclusion
 * is replaced by a period group that contain a similar period.
 */
class ScheduleWithPeriodGroup extends ScheduleUsingExclusion
{
	final static String SummerLunchPeriodGroup = "SummerLunchPeriodGroup";

	public static void main(String args[])
	{
		try
		{
			ScheduleWithPeriodGroup sample = new ScheduleWithPeriodGroup();
			sample.doIt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			println("Error: e="+e);
		}
	}

	ScheduleWithPeriodGroup()
	{
		super();
	}

	/**
	* Overide to add a PeriodGroup.
	*/
     void addBusinessHourInSummer(Schedules schedules) throws RBEConfigObjectException
	  { 
		  addSummerLunchPeriodGroup(schedules);
        super.addBusinessHourInSummer(schedules);
	  }

	/**
	 * Adds a PeriodGroup named "SummerLunchPeriodGroup" that contain a period 
	 * for 12:00pm to 2:00pm to the Schedules object.
	 */
	 void addSummerLunchPeriodGroup(Schedules schedules)
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
	   	println("Added exclusion period: " + xpWD);

			Period[] periods = new Period[1];
			periods[0] = xpWD;

			try
			{
				PeriodGroup pg = new PeriodGroup(SummerLunchPeriodGroup, periods);
				schedules.addPeriodGroup(pg);
	       	println("Added exclusion period: " + pg);
			}
			catch (ScheduleException se)
			{
				// should not get the exception.
			}
	 }

	/**
	 * Use the PeriodGroup "SummerLunchPeriodGroup" to exclude 12:00pm to 2:00pm.
	 */
	 void exclude12to2(Schedule schedule)
	 {
		schedule.addExclusionPeriod(new PeriodGroupReference(SummerLunchPeriodGroup));
	 }
}

