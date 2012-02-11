
import java.io.*;

import COM.TIBCO.hawk.talon.*;
/*
 * Copyright (c) 2000 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.rulebase.*;
import COM.TIBCO.hawk.config.rbengine.rulebase.policy.*;

/**
 * This example create a simple rulebase and save it to a file.
 * It then read rulebase from the file and modify it to include
 * a compound test.
*/
class RBISample2 extends RBISample1
{
	public static void main(String args[])
	{
		try
		{
			RBISample2 sample = new RBISample2();

			// Create a rulebase
			Rulebase rb = sample.createRulebase(SAMPLE_SPOT_RB_NAME);

			// Save it to a file
			sample.save(rb);

			// Read the rulebase from file
            rb = sample.load(SAMPLE_SPOT_RB_NAME+".hrb");

            // Update to a more complex one
			sample.makeRulebaseMoreComplex(rb);

			// Again save it to a file
			sample.save(rb);

		}
		catch (Exception e)
		{
			System.out.println("Error: e="+e);
		}
	}

	/**
	 * Change the test from just test for blue to test for blue or red.
	 * <p>
	 * This method assume the rulebase is the one created from RBISample1
	 * which has one rule, one test and one action.
	 */
    void makeRulebaseMoreComplex(Rulebase rulebase)
    {
		// Get the existing rules
		Rule[] rules = rulebase.getRules();
		if (rules.length != 1)
			return;

		// Get the existing tests
		Test[] tests = rules[0].getTests();
		if (tests.length != 1)
			return;

		// get the existing consequence actions
		ConsequenceAction[] actions = tests[0].getConsequenceActions();
		if (actions.length != 1)
			return;

		//
		actions[0].setPerformActionPolicy(new PerformAlways());

		// Save the current test
		Operator opOrig = tests[0].getTestExpressionOperator();

		// Create operators to test if color is blue
		Operator opGetColor =  new Operator(RULEDATA_OP, new Object[]{new String("Color")});
		Operator opEqRed =  new Operator(EQUALOBJ_OP, new Object[]{opGetColor, new String("red")});
		Operator opOr =  new Operator(OR_OP, new Object[]{opOrig, opEqRed});

		// Set consequence actions in the new test
		tests[0].setConsequenceActions(actions);

		// Set new operator in the new test
		tests[0].setTestExpressionOperator(opOr);

		// Set new tests in the rule
		rules[0].setTests(tests);

		// Set new rules in the rulebase
		rulebase.setRules(rules);
    }

	/**
	 * load a rulebase from a file.
	 */
    Rulebase load(String filename) throws IOException, RBEConfigObjectException
    {
		// Create a reader
		FileReader fw = new FileReader(filename);

		// Construct the rulebase from the reader
		return new Rulebase(fw);
    }
}
	
