/*************************************************************************
* Copyright (C) 1998, Chris Cheetham, fooware                            *
* Distributed under the GNU General Public License                       *
*   http://www.fsf.org/copyleft/gpl.html                                 *
*************************************************************************/

package com.cisco.eManager.eManager.util;
import org.apache.log4j.Logger;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import java.io.IOException;


/**
* This class represents an FTP reply as outlined in section 4.2 of RFC959,
* authors J. Postel and J. Reynolds, 1985.  The RFC can be viewed in its
* entirety at <A HREF="http://sunsite.auc.dk/RFC/rfc/rfc959.html">http://sunsite.auc.dk/RFC/rfc/rfc959.html</A>.
* <P>
* @author <A HREF="mailto:cheetham@fooware.com">Chris Cheetham</A>
* @version $Revision: 1.6 $
**/
public class FtpResponse {
    private static Logger logger = Logger.getLogger(FtpResponse.class);
    //
    // constrcutors
    //

    /**
    * Construct a new FtpResponse, whose contents will be
    * derived from the BufferedReader.
    **/
    FtpResponse(BufferedReader in) throws IOException {
        setMessage(in);
    }

    FtpResponse(InputStream istr) throws IOException {
        setMessage(istr);
    }

    //
    // interface
    //

    /**
    * Return the reply from the FTP server in its entirety.
    **/
    public String getMessage() {
        return message;
    }

    /**
    * Return the 3-digit return code that is also the first 3 characters
    * of the FTP reply.
    **/
    public String getReturnCode() {
        return returnCode;
    }

    /**
    * Return a String representation of this object.  Return the same
    * as getMessage().
    **/
    public String toString() {
        return message;
    }

    /**
    * Returns <CODE>true</CODE> if the first character of the return
    * code indicates a positive prelimary reply, as outlined in RFC959.
    **/
    public boolean isPositivePreliminary() {
        return returnCode.charAt(0) == REPLY_POSITIVE_PRELIMINARY;
    }

    /**
    * Returns <CODE>true</CODE> if the first character of the return
    * code indicates a positive completion reply, as outlined in RFC959.
    **/
    public boolean isPositiveCompletion() {
        return returnCode.charAt(0) == REPLY_POSITIVE_COMPLETION;
    }

    /**
    * Returns <CODE>true</CODE> if the first character of the return
    * code indicates a positive intermediary reply, as outlined in RFC959.
    **/
    public boolean isPositiveIntermediary() {
        return returnCode.charAt(0) == REPLY_POSITIVE_INTERMEDIARY;
    }

    /**
    * Returns <CODE>true</CODE> if the first character of the return
    * code indicates a transient negative reply, as outlined in RFC959.
    **/
    public boolean isTransientNegativeCompletion() {
        return returnCode.charAt(0) == REPLY_TRANSIENT_NEGATIVE_COMPLETION;
    }

    /**
    * Returns <CODE>true</CODE> if the first character of the return
    * code indicates a permanent negative reply, as outlined in RFC959.
    **/
    public boolean isPermanentNegativeCompletion() {
        return returnCode.charAt(0) == REPLY_PERMANENT_NEGATIVE_COMPLETION;
    }

    /**
    * Returns <CODE>true</CODE> if the second character of the return
    * code indicates a reply pertaining to syntax (or maybe just superfluous),
    * as outlined in RFC959.
    **/
    public boolean isRegardingSyntax() {
        return returnCode.charAt(1) == REGARDING_SYNTAX;
    }

    /**
    * Returns <CODE>true</CODE> if the second character of the return
    * code indicates a reply pertaining to information, as outlined in RFC959.
    **/
    public boolean isRegardingInformation() {
        return returnCode.charAt(1) == REGARDING_INFORMATION;
    }

    /**
    * Returns <CODE>true</CODE> if the second character of the return
    * code indicates a reply pertaining to connection, as outlined in RFC959.
    **/
    public boolean isRegardingConnection() {
        return returnCode.charAt(1) == REGARDING_CONNECTION;
    }

    /**
    * Returns <CODE>true</CODE> if the second character of the return
    * code indicates a reply pertaining to authentication, as outlined in
    * RFC959.
    **/
    public boolean isRegardingAuthentication() {
        return returnCode.charAt(1) == REGARDING_AUTHENTICATION;
    }

    /**
    * Returns <CODE>true</CODE> if the second character of the return
    * code indicates a reply pertaining to file system, as outlined in
    * RFC959.
    **/
    public boolean isRegardingFileSystem() {
        return returnCode.charAt(1) == REGARDING_FILE_SYSTEM;
    }

    public static final char REPLY_POSITIVE_PRELIMINARY = '1';
    public static final char REPLY_POSITIVE_COMPLETION = '2';
    public static final char REPLY_POSITIVE_INTERMEDIARY = '3';
    public static final char REPLY_TRANSIENT_NEGATIVE_COMPLETION = '4';
    public static final char REPLY_PERMANENT_NEGATIVE_COMPLETION = '5';

    public static final char REGARDING_SYNTAX = '0';
    public static final char REGARDING_INFORMATION = '1';
    public static final char REGARDING_CONNECTION = '2';
    public static final char REGARDING_AUTHENTICATION = '3';
    public static final char REGARDING_UNSPECIFIED = '4';
    public static final char REGARDING_FILE_SYSTEM = '5';

    //
    // implementation
    //

    private void setMessage(BufferedReader in) throws IOException {
        StringBuffer buffer = new StringBuffer();
        while (true) {
            while (!in.ready()) {
                try {
                    Thread.sleep(10L);
                }
                catch (InterruptedException exc) {
                }
            }
            String line = in.readLine();
            if (returnCode == null)
                returnCode = line.substring(0, 3);
            buffer.append(line);
            buffer.append('\n');
            if (line.charAt(3) == ' ')
                if (returnCode.equals( line.substring(0, 3) ))
                    break;
        }
        message = buffer.toString();
    }

    private void setMessage(InputStream istr) throws IOException {
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int read = istr.read(buffer);
            if (read == -1) {
                break;
            }
            ostr.write(buffer, 0, read);
        }
        ostr.close();
        message = ostr.toString();
    }

    private String message;
    private String returnCode;

}

