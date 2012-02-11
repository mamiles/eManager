/*
 * Copyright (c) 2000 TIBCO Software Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */

/**
 * RBISampleConstant provide some constants use through out the rulebase API sample.
 */
interface RBISampleConstant
{
	final String SPOT_MICROAGENT_NAME = "COM.TIBCO.ami_api.java.Spot";
	final String RULEBASEENGINE_MICROAGENT_NAME = "COM.TIBCO.hawk.microagent.RuleBaseEngine";
	final String SAMPLE_SPOT_RB_NAME = "RBISampleRulebase";

	final String RULEDATA_OP = "COM.TIBCO.hawk.config.rbengine.rulebase.operators.RuleData";
	final String EQUALOBJ_OP = "COM.TIBCO.hawk.config.rbengine.rulebase.operators.EqualsObject";
	final String OR_OP = "COM.TIBCO.hawk.config.rbengine.rulebase.operators.Or";

	final int subscriptionInterval = 15 * 1000;
}
	
