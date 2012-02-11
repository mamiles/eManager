package com.cisco.eManager.eManager.processSequencer.common;

import com.cisco.eManager.eManager.processSequencer.common.logging.*;
import com.cisco.eManager.common.register.solutionRegistration.*;
import org.exolab.castor.xml.*;
import java.io.*;

public class UnmarshalSolution
    extends Thread
{
    protected CiscoLogger mLogger;
    String xmlString = null;
    SolutionRegistration reg = null;

    public UnmarshalSolution(String xml)
    {
        xmlString = xml;
    }

    public SolutionRegistration getSolutionRegistration()
    {
        return reg;
    }

    public void run()
    {
        mLogger = CiscoLogger.getCiscoLogger("processSequencer");

        mLogger.info("entered");
        try
        {
            System.out.println("preparing to unmarshalSolutionRegistration");

            reg = (SolutionRegistration)
                  SolutionRegistration.unmarshalSolutionRegistration(new StringReader(xmlString));
            System.out.println("returned from unmarshalSolutionRegistration");

        }
        catch (ValidationException ex)
        {
            mLogger.severe("XML Validation Exception: " + ex.getMessage());
            throw new PSRuntimeException("XML Validation Exception: " + ex.getMessage());
        }
        catch (MarshalException ex)
        {
            mLogger.severe("XML Marshal Exception: " + ex.getMessage());
            throw new PSRuntimeException("XML Marshal Exception: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            mLogger.severe("Exception: " + ex);
        }
        mLogger.info("completed");
    }

    public static void main(String[] args) {
        StringBuffer xmlMsg = new StringBuffer();
        xmlMsg.append("<SolutionRegistration>");
        xmlMsg.append("   <SolutionName>SA</SolutionName>");
        xmlMsg.append("   <Components>");
        xmlMsg.append("     <Component>");
        xmlMsg.append("       <AppType>TestApp</AppType>");
        xmlMsg.append("       <AppInstance>sunfire-v880</AppInstance>");
        xmlMsg.append("       <AppVersion>1.0.0</AppVersion>");
        xmlMsg.append("      </Component>");
        xmlMsg.append("    </Components>");
        xmlMsg.append("</SolutionRegistration>");

        UnmarshalSolution us = new UnmarshalSolution(xmlMsg.toString());

        us.start();
        try {
            us.join(60000);
        }
        catch (InterruptedException ex1) {
            System.out.println("SolutionXml interrupted while unmarshaling xmlString");
        }

        System.out.println("Ready to get Reg Object");
        SolutionRegistration reg = us.getSolutionRegistration();
        System.out.println("Solution Name: " + reg.getSolutionName());

    }

}
