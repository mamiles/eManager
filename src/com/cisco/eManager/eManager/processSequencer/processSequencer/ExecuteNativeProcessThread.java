package com.cisco.eManager.eManager.processSequencer.processSequencer;

import java.io.*;
import com.cisco.eManager.eManager.processSequencer.common.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ExecuteNativeProcessThread
    extends Thread {

    int exitValue;
    String command;
    Thread t;
    protected CiscoLogger mLogger;

    public ExecuteNativeProcessThread(String cmd) {
        command = cmd;
    }

    public int getExitValue() {
        return exitValue;
    }

    public void run() {
        try {
            mLogger = CiscoLogger.getCiscoLogger("processSequencer");
            Runtime r = Runtime.getRuntime();
            Process p; // Process tracks one external native process
            BufferedReader is; // reader for output of process
            BufferedReader es;
            String line;

            mLogger.finest("Exceuting command: " + command);
            p = r.exec(command);
            is = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ( (line = is.readLine()) != null) {
                mLogger.finest(line);
            }
            try {
                p.waitFor(); // wait for process to complete
            }
            catch (InterruptedException e) {
                mLogger.severe(e.toString()); // "Can'tHappen"
            }
            if (p.exitValue() != 0) {
                es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                mLogger.severe(es.readLine());
                mLogger.severe("Process done, exit status: " + p.exitValue());
            }
            exitValue = p.exitValue();
        }
        catch (IOException io) {
            io.printStackTrace();
        }

    }

}