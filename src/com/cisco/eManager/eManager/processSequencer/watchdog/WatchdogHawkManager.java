package com.cisco.eManager.eManager.processSequencer.watchdog;

import java.util.Properties;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import com.cisco.eManager.eManager.util.GlobalProperties;
import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;
import com.cisco.eManager.eManager.util.WatchdogRulebaseNameHelper;

import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import COM.TIBCO.hawk.config.rbengine.*;
import COM.TIBCO.hawk.config.rbengine.rulebase.*;
import COM.TIBCO.hawk.config.rbengine.rulebase.policy.*;
import COM.TIBCO.hawk.talon.*;

public class WatchdogHawkManager
    extends Thread
{
    protected CiscoLogger mLogger;
    protected String mDomain = null;
    protected String mService = null;
    protected String mNetwork = null;
    protected String mDaemon = null;
    protected String emHome = null;
    private WatchdogHawkConsole mConsole = null;
    private TibrvQueue mRvQueue = null;
    private final String WDMicroAgentName = "com.cisco.eManager.eManager.processSequencer.watchdog.WD-";
    private static WatchdogHawkManager instance;

    private WatchdogHawkManager()
    {
        mLogger = CiscoLogger.getCiscoLogger("watchdog");
        setupConfig();
    }

    public static WatchdogHawkManager instance()
    {
        if (instance == null)
        {
            instance = new WatchdogHawkManager();
        }
        return instance;
    }

    private void setupConfig()
    {
        mService = DCPLib.getSystemProperty("tibhawk.service", "7474");
        mLogger.finest("TibHawk UDP Service: " + mService);
        mNetwork = DCPLib.getSystemProperty("tibhawk.network", null);
        mLogger.finest(
        "TibHawk network to use for outbound session communications: " +
        mNetwork);
        mDaemon = DCPLib.getSystemProperty("tibhawk.daemon", "tcp:7474");
        mLogger.finest(
        "TIBCO Rendezvous daemon to handle communication for the session: " +
        mDaemon);
        mDomain = DCPLib.getSystemProperty("tibhawk.domain", "default");
        mLogger.finest("TibHawk Domain on which the console is to communicate: " + mDomain);
        emHome = System.getProperty("em.home");
        mLogger.finest("eManager Home directory (EM_HOME):" + emHome);
    }

    public void run()
    {
        WatchdogHawkConsole mConsole = WatchdogHawkConsole.instance();
        File rulebaseFile = null;
        try
        {
            rulebaseFile = createRulebase();
        }
        catch (RBEConfigObjectException ex1)
        {
            mLogger.severe("RBEConfigObjectException while creating Rulebase file:" + ex1);
        }
        catch (IOException e)
        {
            mLogger.severe("IOException creating Rulebase file: " + e);
        }
        int tryCount = 0;
        MicroAgentID maidRBE = null;
        while (true)
        {
            maidRBE = mConsole.getRulebaseEngineMicroagentID();
            if (maidRBE == null)
            {
                mLogger.fine("Waiting to get RuleBaseEngine Micro Agent...");
                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException ex2)
                {
                    mLogger.severe("InterruptedException while sleeping 3000ms: " + ex2);
                }
                tryCount++;
            }
            else
            {
                break;
            }
            if (tryCount >= 20)
            {
                mLogger.severe("RuleBaseEngine MicroAgent not found");
                break;
            }
        }
        if (maidRBE != null)
        {
            String wdRulebaseName = WatchdogRulebaseNameHelper.instance().
                                    generateWatchdogRulebaseName(LogUtil.getAppType(), LogUtil.getAppInst());

            mLogger.info("Adding Rulebase: " + wdRulebaseName + " to TibHawk Agent");
            addRulebase(maidRBE, rulebaseFile);
        }

    }

    private File createRulebase()
        throws IOException, RBEConfigObjectException
    {
        String line;
        String wdRulebaseName = WatchdogRulebaseNameHelper.instance().
                                generateWatchdogRulebaseName(LogUtil.getAppType(), LogUtil.getAppInst());
        File wdRB = new File(emHome + "/config/WD.hrb");
        BufferedReader rb = new BufferedReader(new FileReader(wdRB));
        File outputFile = new File(emHome + "/config/processRulebase/" + wdRulebaseName + ".hrb");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

        while ((line = rb.readLine()) != null)
        {
            String line1 = line.replaceFirst("_WD_", wdRulebaseName);
            String line2 = line1.replaceFirst("cisco.mgmt.emanager.ps.watchdog",
                                              WDMicroAgentName + LogUtil.getAppType() + "-" +
                                              LogUtil.getAppInst());
            out.println(line2);
        }
        out.close();
        return outputFile;
    }

    public void addRulebase(MicroAgentID maidRBE, File rbFile)
    {
        try
        {
            mLogger.fine("addRulebase: " + rbFile.getCanonicalPath() + " using MicroAgent: " +
                         maidRBE.toString());
            DataElement[] dataElements = new DataElement[1];
            dataElements[0] = new DataElement("File", rbFile.getCanonicalPath());
            mLogger.fine("Created DataElement: " + dataElements[0].toString());
            mLogger.fine("Agent Name: " + WatchdogHawkConsole.instance().getAgentName());
            MethodInvocation mi = new MethodInvocation("loadRuleBaseFromFile", dataElements);
            mLogger.fine("Ready to invoke method on console");
            WatchdogHawkConsole.instance().invoke(maidRBE, mi);
        }
        catch (MicroAgentException ex3)
        {
            mLogger.severe("MicroAgentException when invoking method: " + ex3);
        }
        catch (IOException ex)
        {
            mLogger.severe("IOException when getting RuleBase File Name " + ex);
        }
    }

    /**
     * Delete a rulebase on the agent.
     */
    public void deleteRulebase(String rb)
    {
        try
        {
            mLogger.fine("deleteRulebase: " + rb + " using MicroAgent: " +
                         WatchdogHawkConsole.instance().getRulebaseEngineMicroagentID().toString());
            DataElement[] args = new DataElement[1];
            args[0] = new DataElement("RuleBaseName", rb);
            MethodInvocation mi = new MethodInvocation("deleteRuleBase", args);
            WatchdogHawkConsole.instance().invoke(WatchdogHawkConsole.instance().getRulebaseEngineMicroagentID(), mi);
        }
        catch (MicroAgentException ex3)
        {
            mLogger.severe("MicroAgentException when invoking method: " + ex3);
        }

    }

}
