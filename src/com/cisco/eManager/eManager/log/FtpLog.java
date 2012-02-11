package com.cisco.eManager.eManager.log;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.*;
import com.cisco.eManager.eManager.util.FtpClient;
import com.cisco.eManager.eManager.util.FtpReader;
import com.cisco.eManager.eManager.util.FtpInputStream;
import org.apache.log4j.Logger;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

/**
 * This class will ftp a remote log file or provide a listing of log files.
 * @author Marvin Miles
 * @version 1
 **/
public class FtpLog
    extends ProcessLog {

        private static Logger logger = Logger.getLogger(FtpLog.class);
        private String hostname;
        private String userid;
        private String password;
        List fileNameList = new LinkedList();
        public static final char IMAGE_TYPE = 'I';
        public static final char ASCII_TYPE = 'A';

        public FtpLog(String hostname, String userid, String password) {
                setHostname(hostname);
                setUserid(userid);
                setPassword(password);
                logger.info("FTP to host: " + hostname + " using userid: " + userid + " initialized successfully");
        }

        public List getFileList(String path) throws LogManagerException {

                setFileNameList(new LinkedList());

                logger.info("FTP get file list for: " + path);
                try {
                        FtpClient ftp = new FtpClient();
                        ftp.setDebugger(new OutputStreamWriter(System.out));
                        ftp.connect(getHostname());
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Failed to connect to " + getHostname() + ".");
                                throw new LogManagerException(LogManagerException.FAILED_TO_CONNECT);
                        }
                        ftp.userName(getUserid());
                        if (!ftp.getResponse().isPositiveIntermediary()) {
                                logger.error("Userid " + getUserid() + " not accepted.");
                                throw new LogManagerException(LogManagerException.USERID_ERROR);
                        }
                        ftp.password(getPassword());
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Password not accepted.");
                                throw new LogManagerException(LogManagerException.PASSWORD_ERROR);
                        }
                        ftp.representationType(ASCII_TYPE);
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("TYPE IMAGE failed.");
                                throw new LogManagerException(LogManagerException.TYPE_IMAGE_FAILED);
                        }
                        ftp.dataPort();
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("PORT failed.");
                                throw new LogManagerException(LogManagerException.PORT_FAILED);
                        }

                        FtpReader ftpin = ftp.list(path);
                        if (!ftp.getResponse().isPositivePreliminary()) {
                                logger.error("LIST failed.");
                                throw new LogManagerException(LogManagerException.LIST_FAILED);
                        }

                        processFileList(ftpin, path);

                        ftpin.close();

                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("List processing failed.");
                                throw new LogManagerException(LogManagerException.LIST_PROCESSING_FAILED);
                        }
                        ftp.logout();
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Logout failed.");
                                throw new LogManagerException(LogManagerException.LOGOUT_FAILED);
                        }
                        ftp.disconnect();
                }
                catch (Exception exc) {
                        logger.error("FTP failed");
                        throw new LogManagerException(exc.getMessage());
                }

                return (this.fileNameList);
        }

        public void getRemoteFile(String remotePath, String localPath) throws LogManagerException {
                String listline = null;
                StringBuffer sb = new StringBuffer();
                int b;
                String subPath = null;
                setFileNameList(new LinkedList());
                logger.info("FTP remote file: " + remotePath + " to " + localPath);
                try {
                        FtpClient ftp = new FtpClient();
                        ftp.setDebugger(new OutputStreamWriter(System.out));
                        ftp.connect(getHostname());
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Failed to connect to " + getHostname() + ".");
                                throw new LogManagerException(LogManagerException.FAILED_TO_CONNECT);
                        }
                        ftp.userName(getUserid());
                        if (!ftp.getResponse().isPositiveIntermediary()) {
                                logger.error("Userid " + getUserid() + " not accepted.");
                                throw new LogManagerException(LogManagerException.USERID_ERROR);
                        }
                        ftp.password(getPassword());
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Password not accepted.");
                                throw new LogManagerException(LogManagerException.PASSWORD_ERROR);
                        }
                        ftp.representationType(IMAGE_TYPE);
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("TYPE IMAGE failed.");
                                throw new LogManagerException(LogManagerException.TYPE_IMAGE_FAILED);
                        }
                        ftp.dataPort();
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("PORT failed.");
                                throw new LogManagerException(LogManagerException.PORT_FAILED);
                        }

                        FtpInputStream ftpstr = ftp.retrieveStream(remotePath);
                        if (!ftp.getResponse().isPositivePreliminary()) {
                                logger.error("RETR retrieveStream failed.");
                                throw new LogManagerException(LogManagerException.RETR_FAILED);
                        }
                        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localPath));
                        while (true) {
                                int ch = ftpstr.read();
                                if (ch == -1) {
                                        ftpstr.close();
                                        os.close();
                                        break;
                                }
                                os.write(ch);
                                // System.out.print((char)ch);
                        }
                        // System.logger.debug();

                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("RETR retrieveStream processing failed.");
                                throw new LogManagerException(LogManagerException.RETR_PROCESSING_FAILED);
                        }
                        ftp.logout();
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Logout failed.");
                                throw new LogManagerException(LogManagerException.LOGOUT_FAILED);
                        }
                        ftp.disconnect();
                }
                catch (Exception exc) {
                        logger.error("FTP failed");
                        throw new LogManagerException(exc.getMessage());
                }

                return;
        }

        public void getRemoteFileAndDelete(String remotePath, String localPath) throws LogManagerException {
                String listline = null;
                StringBuffer sb = new StringBuffer();
                int b;
                String subPath = null;
                setFileNameList(new LinkedList());
                logger.info("FTP and delete remote file: " + remotePath + " to " + localPath);
                try {
                        FtpClient ftp = new FtpClient();
                        ftp.setDebugger(new OutputStreamWriter(System.out));
                        ftp.connect(getHostname());
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Failed to connect to " + getHostname() + ".");
                                throw new LogManagerException(LogManagerException.FAILED_TO_CONNECT);
                        }
                        ftp.userName(getUserid());
                        if (!ftp.getResponse().isPositiveIntermediary()) {
                                logger.error("Userid " + getUserid() + " not accepted.");
                                throw new LogManagerException(LogManagerException.USERID_ERROR);
                        }
                        ftp.password(getPassword());
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Password not accepted.");
                                throw new LogManagerException(LogManagerException.PASSWORD_ERROR);
                        }
                        ftp.representationType(IMAGE_TYPE);
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("TYPE IMAGE failed.");
                                throw new LogManagerException(LogManagerException.TYPE_IMAGE_FAILED);
                        }
                        ftp.dataPort();
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("PORT failed.");
                                throw new LogManagerException(LogManagerException.PORT_FAILED);
                        }

                        FtpInputStream ftpstr = ftp.retrieveStream(remotePath);
                        if (!ftp.getResponse().isPositivePreliminary()) {
                                logger.error("RETR retrieveStream failed.");
                                throw new LogManagerException(LogManagerException.RETR_FAILED);
                        }
                        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localPath));
                        while (true) {
                                int ch = ftpstr.read();
                                if (ch == -1) {
                                        ftpstr.close();
                                        os.close();
                                        break;
                                }
                                os.write(ch);
                        }
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("RETR retrieveStream processing failed.");
                                throw new LogManagerException(LogManagerException.RETR_PROCESSING_FAILED);
                        }
                        ftp.delete(remotePath);
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("DELE failed.");
                                throw new LogManagerException(LogManagerException.DELETE_FAILED);
                        }

                        ftp.logout();
                        if (!ftp.getResponse().isPositiveCompletion()) {
                                logger.error("Logout failed.");
                                throw new LogManagerException(LogManagerException.LOGOUT_FAILED);
                        }
                        ftp.disconnect();
                }
                catch (Exception exc) {
                        logger.error("FTP failed");
                        throw new LogManagerException(exc.getMessage());
                }

                return;
        }

        private void processFileList(FtpReader ftpin, String path) throws LogManagerException {
                String listline = null;
                StringBuffer sb = new StringBuffer();
                int b;
                int i;
                String subPath = null;
                // Determine if there is a * in the path name

                boolean foundAst = false;
                for (i = 0; i < path.length(); i++) {
                        if (path.charAt(i) == '*') {
                                foundAst = true;
                                break;
                        }
                }
                if (foundAst) {
                        subPath = null;
                }
                else {
                        subPath = path;
                }

                try {
                        while (ftpin.ready()) {
                                b = ftpin.read();
                                if (b != '\n') {
                                        sb.append( (char) b);
                                }
                                else {
                                        listline = sb.toString();
                                        if (listline.charAt(0) == '-') {
                                                StringTokenizer st = new StringTokenizer(listline);
                                                FileNameAttr fileName = new FileNameAttr();
                                                fileName.setAttributes(st.nextToken());
                                                fileName.setNumInodes(st.nextToken());
                                                fileName.setOwnerName(st.nextToken());
                                                fileName.setGroupName(st.nextToken());
                                                fileName.setSize(st.nextToken());
                                                fileName.setChangeDate(st.nextToken() + " " + st.nextToken() + " " + st.nextToken());
                                                if (subPath == null) {
                                                        fileName.setFileName(st.nextToken());
                                                }
                                                else {
                                                        fileName.setFileName(subPath + "/" + st.nextToken());
                                                }
                                                while (st.hasMoreTokens()) {
                                                        fileName.setFileName(fileName.getFileName() + " " + st.nextToken());
                                                }
                                                if (fileName.getFileName().charAt(0) != '.') {
                                                        this.fileNameList.add(fileName);
                                                        logger.debug(fileName.toString());
                                                }
                                        }
                                        else {
                                                if (listline.charAt(0) == '/') {
                                                        StringTokenizer sPath = new StringTokenizer(listline, ":");
                                                        subPath = sPath.nextToken();
                                                        // System.out.println(listline);

                                                }
                                        }
                                        sb = new StringBuffer();
                                }
                        }
                }
                catch (Exception exc) {
                        logger.error("Error Processsing File List in FTP");
                        throw new LogManagerException(exc.getMessage());
                }

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

        public void setFileNameList(List fileNameList) {
                this.fileNameList = fileNameList;
        }

        public List getFileNameList() {
                return fileNameList;
        }

}
