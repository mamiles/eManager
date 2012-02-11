/*************************************************************************
* Copyright (C) 1998, Chris Cheetham, fooware                            *
* Distributed under the GNU General Public License                       *
*   http://www.fsf.org/copyleft/gpl.html                                 *
*************************************************************************/

package com.cisco.eManager.eManager.util;
import org.apache.log4j.Logger;
import java.io.FilterOutputStream;
import java.io.OutputStream;

import java.io.IOException;

/**
* @author <A HREF="mailto:cheetham@fooware.com">Chris Cheetham</A>
* @version $Revision: 1.2 $
**/
public class FtpOutputStream extends FilterOutputStream {
	private static Logger logger = Logger.getLogger(FtpOutputStream.class);
    //
    // constructors
    //

    /**
     * Contruct an FtpOutputStream for the specified FtpClient.
     **/
    FtpOutputStream(OutputStream ostr, FtpClient client) throws IOException {
        super(ostr);
        this.client = client;
    }

    /**
     * Close the underlying Writer and signal the FtpClient that
     * Writer processing has completed.
     **/
    public void close() throws IOException {
        super.close();
        client.closeTransferSocket();
    }

    public void write( byte[] bytes, int offset, int length  ) throws IOException {
        out.write( bytes, offset, length );
    }

    //
    // member variables
    //

    private FtpClient client;

}

