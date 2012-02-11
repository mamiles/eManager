/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2001-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Axis" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package samples.misc ;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.utils.Options;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;


/**
 *
 * @author Doug Davis (dug@us.ibm.com)
 * @author Glen Daniels (gdaniels@allaire.com)
 */
public class TestClient {
    public static String msg = "<SOAP-ENV:Envelope " +
        "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
        "xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" > " +
        "<SOAP-ENV:Body>\n" +
        "<echo:Echo xmlns:echo=\"EchoService\">\n" +
        "<symbol>IBM</symbol>\n" +
        "</echo:Echo>\n" +
        "</SOAP-ENV:Body></SOAP-ENV:Envelope>\n";

    /**
     * Send a hardcoded message to the server, and print the response.
     *
     * @param args the command line arguments (mainly for specifying URL)
     * @param op an optional service argument, which will be used for
     * specifying the transport-level service
     */
    public static String doTest (String args[], String op) throws Exception {
      Options      opts    = new Options( args );
      String       url     = opts.getURL();
      String       action  = "EchoService" ;
        
      if (op != null) action = op;

      args = opts.getRemainingArgs();
      if ( args != null ) action = args[0];

      InputStream   input   = new ByteArrayInputStream(msg.getBytes());
      Service       service = new Service();
      Call          call    = (Call) service.createCall();
      SOAPEnvelope  env     = new SOAPEnvelope(input);

      call.setTargetEndpointAddress( new URL(url) );
      if (action != null) {
          call.setUseSOAPAction( true );
          call.setSOAPActionURI( action );
      }

      System.out.println( "Request:\n" + msg );

      env = call.invoke( env );

      System.out.println( "Response:\n" + env.toString() );
      return( env.toString() );
    }
    
  public static void main(String args[]) throws Exception{
    doTest(args, null);
  }
  public static void mainWithService(String args[], String service) throws Exception{
    doTest(args, service);
  }
}
