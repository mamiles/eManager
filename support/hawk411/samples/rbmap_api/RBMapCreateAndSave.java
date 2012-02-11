/*
 * Copyright (c) 2002 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

import java.util.Vector;
import java.io.*;

import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.rbmap.*;

/**
 * This example create a rulebase map and save it to a file.
 *
 * The mapping of the rulebase map is setup in the method 
 * createRulebaseMap() as follow:
 *  
 * - agent1A, agent1B, and agent1C are in +group1
 * - agent1A", agent1B", and agent1C are using rulebase1A 
 * - +group1 is using rulebase1G
 * - +group2 is using rulebase2G
 * - +group1 and +group2 are using rulebase12A and rulebase12B
 * - ++Windows is using rulebaseNT
 *   
 * It then retrieved rulebases assigned to the agent agent1A which
 * is in autogroup "++Windows". The rulebases map to agent1A should be:
 *    rulebase1A, rulebase1G, rulebase12A. rulebase12B, and rulebaseNT
 *
 * Note that even though the rulebase map can be saved with any
 * file name, the agent always use rbmap.hrm for the rulebase map.
*/
class RBMapCreateAndSave implements RBMapSampleConstant
{
	public static void main(String args[])
	{
		try
		{
			RBMapCreateAndSave sample = new RBMapCreateAndSave();

			// Create a rulebase map
			RBMap rbMap = sample.createRulebaseMap();

			// Save it to a file
			sample.save(rbMap, SAMPLE_RB_MAP_NAME+rbMap.getFileExtension());

         // Get all the rulebases for agent1A
			// Rulebases for agent1A with autogroup ++Windows should be
			// rulebase1A, rulebase1G, rulebase12A. rulebase12B, and rulebaseNT
			String[]  v = rbMap.getAgentRulebases("agent1A", "++Windows");
		   println("Rulebases for agent1A: " + toString(v));

			RBMap rbMapLoaded = sample.load(SAMPLE_RB_MAP_NAME+rbMap.getFileExtension());
			if (!rbMap.equals(rbMapLoaded))
		      println("Error saving rulebase map to file!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			println("Error: e="+e);
		}
	}

	/**
	* Create a rulebase map with the following mapping:
	*  
	* - agent1A, agent1B, and agent1C are in +group1
	* - agent1A", agent1B", and agent1C are using rulebase1A 
	* - +group1 is using rulebase1G
	* - +group2 is using rulebase2G
	* - +group1 and +group2 are using rulebase12A and rulebase12B
	* - ++Windows is using rulebaseNT
	*  
	*/
    RBMap createRulebaseMap() throws RBEConfigObjectException
    {
		 String[] agentsArray = {"agent1A", "agent1B", "agent1C"};
		 String[] group1Array = {"+group1"};
		 String[] group2Array = {"+group2"};
		 String[] groupsArray = {"+group1", "+group2"};
		 String[] ossArray = {"++Windows"};

		// Create RBMap
		RBMap rbMap = new RBMap();

		rbMap.setMembersInGroup("+group1", agentsArray);

		rbMap.setMembersForRulebase("rulebase1A", agentsArray);
		rbMap.setMembersForRulebase("rulebase1G", group1Array);
		rbMap.setMembersForRulebase("rulebase2G", group2Array);
		rbMap.setMembersForRulebase("rulebase12A", groupsArray);
		rbMap.setMembersForRulebase("rulebase12B", groupsArray);
		rbMap.setMembersForRulebase("rulebaseNT", ossArray);

		return rbMap;
    }


	/**
	* Save a rulebase map to a file.
	*/
    static void save(RBMap rbMap, String filename) throws IOException, RBEConfigObjectException
    {
		FileWriter fw = new FileWriter(filename);
		rbMap.toXML(fw);
		fw.close();
    }

	/**
	* Load a rulebase map from a file.
	*/
   static RBMap load(String fileName) throws RBEConfigObjectException
   {

      try
      {
         FileReader fis = new FileReader(fileName);
		   return new RBMap(fis);
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


	/**
	 * Format a string array into [string1, string2, ...]
	 */
	static String toString(String[] ss)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		for (int i = 0; i < ss.length; i++)
		{
			if (i != 0)
				sb.append(", ");
			sb.append(ss[i]);
		}
		sb.append(" ] ");
		return sb.toString();
	}
}
	
