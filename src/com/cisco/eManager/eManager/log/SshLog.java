package com.cisco.eManager.eManager.log;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.*;
import org.apache.log4j.Logger;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class SshLog
    extends ProcessLog {

        private static Logger logger = Logger.getLogger(SshLog.class);
        public static final String expectSsh = "/scripts/ssh.expect";
        public static final String expectScp = "/scripts/scp.expect";
        private String expectCommand = "expect";
        private String ettxRoot = null;
        private String hostname;
        private String userid;
        private String password;
        private String prompt;

        public SshLog(String dir, String hostname, String userid, String password, String prompt) {
                ettxRoot = dir;
                setHostname(hostname);
                setUserid(userid);
                setPassword(password);
                setPrompt(prompt);
                logger.info("SshLog initialized successfully...");
        }

        public List getFileList(List pathVector) throws LogManagerException {
                Iterator it = pathVector.iterator();
                String path = null;

                Runtime r = Runtime.getRuntime();
                Process p; // Process tracks one external native process
                BufferedReader is; // reader for output of process
                BufferedReader es;
                String line;
                setFileNameList(new LinkedList());

                while (it.hasNext()) {
                        path = it.next().toString();
                        logger.debug("Processing path: " + path);
                        StringBuffer fileList = new StringBuffer();
                        try {
                                p = r.exec(expectCommand + " -f " + ettxRoot + expectSsh + " -- " + getHostname() + " " + getUserid() + " " + getPassword() + " " + getPrompt() + " /usr/bin/ls -l " + path);
                                is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                while ( (line = is.readLine()) != null) {
                                        fileList.append(line + "\n");
                                        logger.debug(line);
                                }

                                try {
                                        p.waitFor(); // wait for process to complete
                                }
                                catch (InterruptedException e) {
                                        logger.error(e); // "Can'tHappen"
                                        throw new LogManagerException(LogManagerException.SSH_ERROR);
                                }
                                if (p.exitValue() != 0) {
                                        es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                        logger.error(es.readLine());
                                        logger.error("Process done, exit status: " + p.exitValue());
                                        throw new LogManagerException(LogManagerException.SSH_ERROR);
                                }
                        }
                        catch (IOException io) {
                                logger.error(io);
                                throw new LogManagerException(LogManagerException.SSH_ERROR);
                        }
                        logger.debug("Ready to process");
                        processFileList(fileList, path);
                }

                return (super.fileNameList);
        }

        public List getFileListWithString(List pathVector, String searchValue) throws LogManagerException {
                Iterator it = pathVector.iterator();
                String path = null;
                int i = 0;
                String pathSearch = "";

                Runtime r = Runtime.getRuntime();
                Process p; // Process tracks one external native process
                BufferedReader is; // reader for output of process
                BufferedReader es;
                String line;
                setFileNameList(new LinkedList());

                while (it.hasNext()) {
                        path = it.next().toString();
                        StringBuffer fileList = new StringBuffer();

                        boolean foundAst = false;
                        for (i = 0; i < path.length(); i++) {
                                if (path.charAt(i) == '*') {
                                        foundAst = true;
                                        break;
                                }
                        }
                        if (foundAst) {
                                pathSearch = path;
                        }
                        else {
                                pathSearch = path + "/*";
                        }
                        try {
                                p = r.exec(expectCommand + " -f " + ettxRoot + expectSsh + " -- " + getHostname() + " " + getUserid() + " " + getPassword() + " " + getPrompt() + " /usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " +
                                           pathSearch + " || echo null`");
                                is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                while ( (line = is.readLine()) != null) {
                                        fileList.append(line + "\n");
                                        logger.debug(line);
                                }
                                try {
                                        p.waitFor(); // wait for process to complete
                                }
                                catch (InterruptedException e) {
                                        logger.error(e); // "Can'tHappen"
                                        throw new LogManagerException(LogManagerException.SSH_ERROR);
                                }
                                if (p.exitValue() != 0) {
                                        es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                        logger.error(es.readLine());
                                        logger.error("Process done, exit status: " + p.exitValue());
                                        throw new LogManagerException(LogManagerException.SSH_ERROR);
                                }
                        }
                        catch (IOException io) {
                                logger.error(io);
                                throw new LogManagerException(LogManagerException.SSH_ERROR);
                        }
                        processFileList(fileList, pathSearch);
                }

                return (super.fileNameList);
        }

        public void getRemoteFile(String remotePath, String localPath) throws LogManagerException {
                Runtime r = Runtime.getRuntime();
                Process p; // Process tracks one external native process
                BufferedReader is; // reader for output of process
                BufferedReader es;
                String line;
                boolean errorSW = false;
                logger.debug("SCP - Get Remote file: " + remotePath + " Local Path: " + localPath);
                try {
                        logger.debug(expectCommand + " -f " + ettxRoot + expectScp + " -- " + getPassword() + " " + getUserid() + "@" + getHostname() + ":" + remotePath + " " + localPath);
                        p = r.exec(expectCommand + " -f " + ettxRoot + expectScp + " -- " + getPassword() + " " + getUserid() + "@" + getHostname() + ":" + remotePath + " " + localPath);
                        is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        while ( (line = is.readLine()) != null) {
                                logger.debug(line);
                                if (errorSW) {
                                        logger.error(line);
                                        throw new LogManagerException(LogManagerException.SFTP_ERROR_WRITING_FILE);
                                }
                                if (line.startsWith("Fetching")) {
                                        errorSW = true;
                                }
                                System.out.println(line);
                        }
                        try {
                                p.waitFor(); // wait for process to complete
                        }
                        catch (InterruptedException e) {
                                logger.error(e); // "Can'tHappen"
                                throw new LogManagerException(LogManagerException.SFTP_ERROR_WRITING_FILE);
                        }
                        if (p.exitValue() != 0) {
                                        es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                        logger.error(es.readLine());
                                        logger.error("Process done, exit status: " + p.exitValue());
                                        throw new LogManagerException(LogManagerException.SFTP_ERROR_WRITING_FILE);
                        }

                }
                catch (IOException io) {
                        logger.error(io);
                        throw new LogManagerException(LogManagerException.SFTP_ERROR_WRITING_FILE);
                }
                return;
        }

        public void getRemoteFileAndDelete(String remotePath, String localPath) throws LogManagerException {
                try {
                        getRemoteFile(remotePath, localPath);
                }
                catch (LogManagerException ex) {
                        throw new LogManagerException(LogManagerException.SFTP_ERROR_WRITING_FILE);
                }

                Runtime r = Runtime.getRuntime();
                Process p; // Process tracks one external native process
                BufferedReader is; // reader for output of process
                BufferedReader es;
                String line;
                logger.debug("SSH Delete Remote file: " + remotePath);
                try {

                        p = r.exec(expectCommand + " -f " + ettxRoot + expectSsh + " -- " + getHostname() + " " + getUserid() + " " + getPassword() + " " + getPrompt() + " /usr/bin/rm -f " + remotePath);
                        is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        while ( (line = is.readLine()) != null) {
                                logger.debug(line);
                                System.out.println(line);
                        }
                        try {
                                p.waitFor(); // wait for process to complete
                        }
                        catch (InterruptedException e) {
                                logger.error(e); // "Can'tHappen"
                                throw new LogManagerException(LogManagerException.SFTP_ERROR_WRITING_FILE);
                        }
                        if (p.exitValue() != 0) {
                                        es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                        logger.error(es.readLine());
                                        logger.error("Process done, exit status: " + p.exitValue());
                                        throw new LogManagerException(LogManagerException.SFTP_ERROR_WRITING_FILE);
                        }
                }
                catch (IOException io) {
                        logger.error(io);
                        throw new LogManagerException(LogManagerException.SFTP_ERROR_WRITING_FILE);
                }
                return;
        }

        public long[] getCheckSumAndSize(String fileName) throws LogManagerException {
                Runtime r = Runtime.getRuntime();
                Process p; // Process tracks one external native process
                BufferedReader is; // reader for output of process
                BufferedReader es;
                String line;
                String ckSum = "";
                String size = "";
                String name = "";
                long[] longArray = {
                    0, 0};
                try {
                        p = r.exec(expectCommand + " -f " + ettxRoot + expectSsh + " -- " + getHostname() + " " + getUserid() + " " + getPassword() + " " + getPrompt() + " /usr/bin/cksum " + fileName);
                        is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        while ( (line = is.readLine()) != null) {
                                logger.debug(line);
                                //System.out.println(line);
                                StringTokenizer st = new StringTokenizer(line);
                                if (st.countTokens() == 3) {
                                        ckSum = st.nextToken();
                                        size = st.nextToken();
                                        name = st.nextToken();
                                        try {
                                                longArray[0] = Long.parseLong(ckSum);
                                                longArray[1] = Long.parseLong(size);
                                        }
                                        catch (NumberFormatException exc) {
                                                //System.out.println("Invalid line");
                                                logger.debug("Invalid line");
                                                continue;
                                        }
                                        if (fileName.compareTo(name) == 0) {
                                                return longArray;
                                        }
                                }
                        }
                        try {
                                p.waitFor(); // wait for process to complete
                        }
                        catch (InterruptedException e) {
                                logger.error(e); // "Can'tHappen"
                                throw new LogManagerException(LogManagerException.CHECKSUM_ERROR);
                        }
                        if (p.exitValue() != 0) {
                                        es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                        logger.error(es.readLine());
                                        logger.error("Process done, exit status: " + p.exitValue());
                                        throw new LogManagerException(LogManagerException.CHECKSUM_ERROR);
                        }
                }
                catch (IOException io) {
                        logger.error(io);
                        throw new LogManagerException(LogManagerException.CHECKSUM_ERROR);
                }
                return longArray;
        }

        public void setHostname(String hostname) {
                this.hostname = hostname;
        }

        public String getHostname() {
                return hostname;
        }

        public void setUserid(String userid) {
                this.userid = userid;
        }

        public String getUserid() {
                return userid;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getPassword() {
                return password;
        }

        public void setPrompt(String prompt) {
                this.prompt = prompt;
        }

        public String getPrompt() {
                return prompt;
        }

}
