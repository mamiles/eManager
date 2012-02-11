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

package samples.bidbuy ;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.utils.Options;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.net.URL;
import java.util.Calendar;


/**
 * Test Client for the echo interop service.  See the main entrypoint
 * for more details on usage.
 *
 * @author Sam Ruby <rubys@us.ibm.com>
 */
public class TestClient {

    private static Service service         = null ;
    private static Call    call            = null;

    /**
     * Test an echo method.  Declares success if the response returns
     * true with an Object.equal comparison with the object to be sent.
     * @param method name of the method to invoke
     * @param toSend object of the correct type to be sent
     */
    private static void test(String method, Object toSend) {

    }

    /**
     * Main entry point.  Tests a variety of echo methods and reports
     * on their results.
     *
     * Arguments are of the form:
     *   -h localhost -p 8080 -s /soap/servlet/rpcrouter
     */
    public static void main(String args[]) throws Exception {
        // set up the call object
        Options opts = new Options(args);
        service = new Service();
        call = (Call) service.createCall();
        call.setTargetEndpointAddress( new URL(opts.getURL()) );
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://www.soapinterop.org/Bid");

        // register the PurchaseOrder class
        QName poqn = new QName("http://www.soapinterop.org/Bid",
                               "PurchaseOrder");
        Class cls = PurchaseOrder.class;
        call.registerTypeMapping(cls, poqn, BeanSerializerFactory.class, BeanDeserializerFactory.class);

        // register the Address class
        QName aqn = new QName("http://www.soapinterop.org/Bid", "Address");
        cls = Address.class;
        call.registerTypeMapping(cls, aqn, BeanSerializerFactory.class, BeanDeserializerFactory.class);

        // register the LineItem class
        QName liqn = new QName("http://www.soapinterop.org/Bid", "LineItem");
        cls = LineItem.class;
        call.registerTypeMapping(cls, liqn, BeanSerializerFactory.class, BeanDeserializerFactory.class);

        try {
            // Default return type based on what we expect
            call.setOperationName( new QName("http://www.soapinterop.org/Bid", "Buy" ));
            call.addParameter( "PO", poqn, ParameterMode.IN );
            call.setReturnType( XMLType.XSD_STRING );

            LineItem[] li = new LineItem[2];
            li[0] = new LineItem("Tricorder", 1, "2500.95");
            li[1] = new LineItem("Phasor", 3, "7250.95");

            PurchaseOrder po = new PurchaseOrder(
              "NCC-1701",
              Calendar.getInstance(),
              new Address("Sam Ruby", "Home", "Raleigh", "NC", "27676"),
              new Address("Lou Gerstner", "Work", "Armonk", "NY", "15222"),
              li
            );

            // issue the request
            String receipt = (String) call.invoke( new Object[] {po} );

            System.out.println(receipt);
        } catch (Exception e) {
           System.out.println("Buy failed: " + e);
            throw e;
        }
    }

}
