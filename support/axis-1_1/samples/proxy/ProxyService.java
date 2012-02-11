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

package samples.proxy;

import org.apache.axis.AxisFault;
import org.apache.axis.Handler;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.w3c.dom.Document;
import samples.transport.tcp.TCPSender;
import samples.transport.tcp.TCPTransport;

/**
 * Proxy sample.  Relays message on to hardcoded URL.
 * Soon, URL becomes configurable (via deployment?!);
 * later, URL becomes specifiable in custom header.
 *
 * @author Rob Jellinghaus <robj@unrealities.com>
 */

public class ProxyService {
    /**
     * Process the given message, treating it as raw XML.
     */
    public Document proxyService(MessageContext msgContext)
        throws AxisFault
    {
        try {
            // Look in the message context for our service
            Handler self = msgContext.getService();
            
            // what is our target URL?
            String dest = (String)self.getOption("URL");
            
            // use the server's client engine in case anything has 
            // been deployed to it
            Service service = new Service();
            service.setEngine( msgContext.getAxisEngine().getClientEngine() );
            Call    call = (Call) service.createCall();

            SimpleTargetedChain c = new SimpleTargetedChain(new TCPSender());
            // !!! FIXME
            //service.getEngine().deployTransport("tcp", c);
    
            // add TCP for proxy testing
            call.addTransportPackage("samples.transport");
            call.setTransportForProtocol("tcp", TCPTransport.class);
            
            // NOW set the client's URL (since now the tcp handler exists)
            call.setTargetEndpointAddress(new java.net.URL(dest));
    
            call.setRequestMessage(msgContext.getRequestMessage());
            
            call.invoke();
            
            Message msg = call.getResponseMessage();

            msgContext.setResponseMessage(msg);
        
            // return null so MsgProvider will not muck with our response
            return null;
        }
        catch( Exception exp ) {
            throw AxisFault.makeFault( exp );
        }
    }
}

