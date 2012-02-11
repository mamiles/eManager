/*
 * Copyright (c) 2002 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */
import java.util.Vector;
import java.io.FileWriter;
import java.io.IOException;

import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.rbmap.*;

/**
 * This example extends RBMapCreateAndSave to include using an
 * external command to generate a list of rulebases for an agent.
 *
 * This example depends on RBMapUseCommand.exe and will run on Windows
 * NT or 2000 only. RBMapUseCommand.exe is a simple program that reads
 * up to 512 bytes from RBMapUseCommand.txt and output the contents to 
 * the standard output.
*/
class RBMapUseCommand extends RBMapCreateAndSave
{
	public static void main(String args[])
	{
		try
		{
			RBMapUseCommand sample = new RBMapUseCommand();

			// Create a rulebase map
			RBMap rbMap = sample.createRulebaseMap();

			// Use RBMapUseCommand to generate rulebases for agent1A
         rbMap.setMemberForCommand("RBMapUseCommand", "agent1A");

         // Get all the rulebases for agent1A 
			// The list should include rulebase generated from RBMapUseCommand
			// because agent1A is in +group1 (see createRulebaseMap() in RBMapCreateAndSave)
			String[]  v = rbMap.getAgentRulebases("agent1A", "++Windows");
		   println("Rulebases for agent1A: " + toString(v));
			sample.save(rbMap, SAMPLE_RB_MAP_NAME+"2A"+rbMap.getFileExtension());

			// Remove "RBMapUseCommand" from the command mapping
		   rbMap.removeCommand("RBMapUseCommand");

         // Get all the rulebases for agent1A 
			// The list should not include rulebase generated from RBMapUseCommand
		   v = rbMap.getAgentRulebases("agent1A", "++Windows");
		   println("Rulebases for agent1A: " + toString(v));

			// Use RBMapUseCommand to generate rulebases for +group1
			// Since agent1A is in +group1, RBMapUseCommand will also be use to generate 
			// rulebases for agent1A.
         rbMap.setMemberForCommand("RBMapUseCommand", "+group1");

         // Get all the rulebases for agent1A
			// The list should include rulebase generated from RBMapUseCommand
		   v = rbMap.getAgentRulebases("agent1A", "++Windows");
		   println("Rulebases for agent1A: " + toString(v));

			sample.save(rbMap, SAMPLE_RB_MAP_NAME+"2B"+rbMap.getFileExtension());
		}
		catch (Exception e)
		{
			println("Error: e="+e);
		}
	}
}
	
