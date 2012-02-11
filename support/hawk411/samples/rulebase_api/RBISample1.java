/*
 * Copyright (c) 2000 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import java.io.*;

import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.rulebase.*;

/**
 *   This example create a simple rulebase and save it to a file.
*/
class RBISample1 implements RBISampleConstant
{
	public static void main(String args[])
	{
		try
		{
			RBISample1 sample = new RBISample1();

			// Create a rulebase
			Rulebase rb = sample.createRulebase(SAMPLE_SPOT_RB_NAME);

			// Save it to a file
			sample.save(rb);

		}
		catch (Exception e)
		{
			System.out.println("Error: e="+e);
		}
	}

	/**
	* Create a rulebase that set spot's color to green if its
	* color is blue.
	*/
    Rulebase createRulebase(String rbName) throws RBEConfigObjectException
    {
		// Create action for setting color to green
		DataElement[] args = new DataElement[1];
		args[0] = new DataElement("Color", "green");
		MethodInvocation mi = new MethodInvocation( "setColor", args);
		ConsequenceAction[] actions = new ConsequenceAction[1];
		actions[0] =  new ConsequenceAction(SPOT_MICROAGENT_NAME, mi);

		// Create operators to test if color is blue
		Operator opGetColor =  new Operator(RULEDATA_OP, new Object[]{new String("Color")});
		Operator opEqBlue =  new Operator(EQUALOBJ_OP, new Object[]{opGetColor, new String("blue")});

		Test[] tests = new Test[1];
		tests[0] = new Test(actions, opEqBlue);

		// Create a rule that subscribe to Spot's getColor
		args = new DataElement[0];
		MethodSubscription mas = new MethodSubscription( "getColor", args, subscriptionInterval);
		DataSource ds = new DataSource(SPOT_MICROAGENT_NAME, mas);

		Rule[] rules = new Rule[1];
		rules[0] = new Rule(ds, tests);

		// Create Rulebase
		return new Rulebase(rbName, rules);
    }


	/**
	* Save a rule base to a file.
	*/
    void save(Rulebase rbiRulebase) throws IOException, RBEConfigObjectException
    {
		FileWriter fw = new FileWriter(rbiRulebase.getName() + rbiRulebase.getFileExtension());
		rbiRulebase.toXML(fw);
		fw.close();
    }

	/**
	 *  Print a line of text to console.
	 */
	void println(String s)
	{
		System.out.println(s);
	}

}
	
