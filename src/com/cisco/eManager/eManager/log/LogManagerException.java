package com.cisco.eManager.eManager.log;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class LogManagerException extends Exception {
        public static final String SINGLETON_VIOLATION = "Singleton_Violation";
        public static final String NO_VALUE_FOR_NODE = "Expected value for node";
        public static final String SFTP_ERROR_WRITING_FILE = "Error writing file using SFTP";
        public static final String FAILED_TO_CONNECT = "Failed to connect to host";
        public static final String USERID_ERROR = "Userid not accepted";
        public static final String PASSWORD_ERROR = "Password not accepted";
        public static final String TYPE_IMAGE_FAILED = "TYPE IMAGE failed";
        public static final String PORT_FAILED = "PORT failed";
        public static final String LIST_FAILED = "LIST failed.";
        public static final String LIST_PROCESSING_FAILED = "List processing failed";
        public static final String LOGOUT_FAILED = "Logout failed";
        public static final String FTP_FAILED = "FTP Failed";
        public static final String RETR_FAILED = "RETR retrieveStream failed";
        public static final String RETR_PROCESSING_FAILED = "RETR retrieveStream processing failed";
        public static final String DELETE_FAILED = "DELE failed";
        public static final String CHECKSUM_ERROR = "Error executing SSH to get Checksum";
        public static final String SSH_ERROR = "SSH Error getting file list";
        public static final String TELNET_ERROR = "Telnet Error - Invalid prompt or password";
        public static final String TELNET_IOERROR = "Telnet I/O Error";
        public static final String CKSUM_ERROR = "Telnet Error - Can't get remote Checksum for file";
        public LogManagerException(String msg) {
                super(msg);
        }
}
