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

package samples.userguide.example5;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.Options;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
                                           
public class Client
{
    public static void main(String [] args) throws Exception
    {
        Options options = new Options(args);
        
        Order order = new Order();
        order.setCustomerName("Glen Daniels");
        order.setShippingAddress("275 Grove Street, Newton, MA");
        
        String [] items = new String[] { "mp3jukebox", "1600mahBattery" };
        int [] quantities = new int [] { 1, 4 };
        
        order.setItemCodes(items);
        order.setQuantities(quantities);
        
        Service  service = new Service();
        Call     call    = (Call) service.createCall();
        QName    qn      = new QName( "urn:BeanService", "Order" );

        call.registerTypeMapping(Order.class, qn,
                      new org.apache.axis.encoding.ser.BeanSerializerFactory(Order.class, qn),        
                      new org.apache.axis.encoding.ser.BeanDeserializerFactory(Order.class, qn));        
        String result;
        try {
            call.setTargetEndpointAddress( new java.net.URL(options.getURL()) );
            call.setOperationName( new QName("OrderProcessor", "processOrder") );
            call.addParameter( "arg1", qn, ParameterMode.IN );
            call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );

            result = (String) call.invoke( new Object[] { order } );
        } catch (AxisFault fault) {
            result = "Error : " + fault.toString();
        }
        
        System.out.println(result);
    }
}
