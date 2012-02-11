/*
 * ConstructedService.java
 *
 * Created on November 5, 2003, 2:23 PM
 */

package DynamicClient;
import java.util.LinkedList;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.net.MalformedURLException;
import java.util.StringTokenizer;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 *
 * @author  rchuang
 */
public class ConstructedService {
    
    /** Creates a new instance of Service */
    
    /*
    public ConstructedService() 
    {
        m_serviceName = null;
        m_operations = null;
       
    }
     */
    
    public ConstructedService(String name, LinkedList operations)
    {
        m_serviceName = name;
        m_operations = operations;
    }
    /*
    public ConstructedService( )
    {
        // Parse the WSDL XML file
        m_serviceName = "DNSLookup";
        m_operations = new LinkedList();
        
        // ConstructedOperation 0
        String opName0 = "ip2dns";        
        
        LinkedList parameterOrder0 = new LinkedList();
        String po = new String("IPAddress");
        parameterOrder0.add(po); //new String("IPAddress"));
        
        LinkedList parameters0 = new LinkedList();
        Parameter parameter00 = new Parameter("IPAddress", "xsd:string");
        parameters0.add(parameter00);
        //Parameter parameter01
        
        Parameter returnParameter = new Parameter("ip2dnsReturn", "xsd:string");
        
        ConstructedOperation op0 = new ConstructedOperation( opName0, parameters0, parameterOrder0, returnParameter);
        
        m_operations.add(op0);
        
        //  ConstructedOperation 1
        String opName1 = "dns2ip";
        
        LinkedList parameterOrder1 = new LinkedList();
        String po1 = new String("DNSName");
        parameterOrder1.add(po1);
        
        LinkedList parameters1 = new LinkedList();
        Parameter parameter10 = new Parameter("DNSName", "xsd:string");
        parameters1.add(parameter10);
        // Parameter parameter 11
        Parameter returnParameter1 = new Parameter("dns2ipReturn", "xsd:string");
        
        ConstructedOperation op1 = new ConstructedOperation( opName1, parameters1, parameterOrder1,  returnParameter1);
        
        m_operations.add(op1);
        
        //  ConstructedOperation 2
        String opName2 = "route";
        
        LinkedList parameterOrder2 = new LinkedList();
        String po20 = new String("sourceIPAddress");
        parameterOrder2.add(po20);
        String po21 = new String("destinationIPAddress");
        parameterOrder2.add(po21);
        String po22 = new String("count");
        parameterOrder2.add(po22);
        
        LinkedList parameters2 = new LinkedList();
        Parameter parameter20 = new Parameter("sourceIPAddress", "xsd:string");
        parameters2.add(parameter20);
        Parameter parameter21 = new Parameter("destinationIPAddress", "xsd:string");
        parameters2.add(parameter21);        
        Parameter parameter22 = new Parameter("count", "xsd:int");
        parameters2.add(parameter22);   
        Parameter parameter23 = new Parameter("isAdjacent", "xsd:boolean");
        parameters2.add(parameter23);
        Parameter parameter24 = new Parameter("startTime", "xsd:dateTime");
        parameters2.add(parameter24);
        Parameter returnParameter2 = new Parameter("routeReturn", "xsd:string");
        
        ConstructedOperation op2 = new ConstructedOperation( opName2, parameters2, parameterOrder2,  returnParameter2);
        
        m_operations.add(op2);        
    }
     */
    /*
    public ConstructedService (URL  wsdlUrl)
    {
        try
        {
        m_serviceName = "";
        m_wsdlUrl = new URL ("http://rchuang-w2k02:8080/axis/DNSLookup.jws?wsdl");
        m_operations = new LinkedList();
        
        String service = getServiceName( wsdl);
        m_serviceName = service;
        System.out.println("m_serviceName = " + m_serviceName);

        m_portName = getPort(m_serviceName);
        System.out.println("m_portName = " + m_portName);
        m_serviceLocation = getServiceLocation(m_serviceName, m_portName);
        System.out.println("m_serviceLocation = " + m_serviceLocation);
        
        LinkedList operations = new LinkedList();
        operations = getOperations(wsdl, m_portName);
        for (int i = 0; i < operations.size(); i++)
        {
            String operationName = (String) operations.get(i);
            String requestName = getOperationRequest( operationName );
            String responseName = getOperationResponse( operationName );
            LinkedList parameters = getRequestParameters( requestName );
            LinkedList parameterOrder = getRequestParameterOrder (requestName );
            Parameter returnValue = getReturnParameter( responseName );
            
            ConstructedOperation operation = new ConstructedOperation( operationName, parameters, parameterOrder, returnValue );
            m_operations.add(operation);
            
        }
        }
        catch (MalformedURLException mue)
        {
            mue.printStackTrace();
        }
    }    
     */
    
    public ConstructedService (URI wsdlUri)
    {
        try
        {
            m_wsdlFile = new File( wsdlUri );
            System.out.println("wsdlUri = " +  wsdlUri);
            m_wsdlUrl = wsdlUri.toURL();
            System.out.println("m_wsdlUrl = " + m_wsdlUrl);
        }
        catch (NullPointerException npe)
        {
            npe.printStackTrace();
        }
        catch (MalformedURLException mue)
        {
            mue.printStackTrace();
        }
    }
    
    public ConstructedService (File wsdlFile,
                                String hostName,
                                String newTcpPortNumber)
    {
        //try
        //{
        m_serviceName = "";
        //m_wsdlUrl = new URL ("http://rchuang-w2k02:8080/axis/DNSLookup.jws?wsdl");
        m_wsdlFile = wsdlFile;
        m_operations = new LinkedList();
        
        String service = getServiceName( wsdlFile);
        m_serviceName = service;
        System.out.println("m_serviceName = " + m_serviceName);

        m_portName = getPort(m_serviceName);
        System.out.println("m_portName = " + m_portName);
        m_serviceLocation = getServiceLocation(wsdlFile, m_serviceName, m_portName);
        System.out.println("m_serviceLocation = " + m_serviceLocation);
        String  wsdlHostName = "";
        String  wsdlTcpPortNumber = "";
        
        wsdlHostName = getInputHost( m_serviceLocation );
        
        wsdlTcpPortNumber = getInputPort( m_serviceLocation );
        
        //System.out.println("wsdlHostName = " + wsdlHostName + ", wsdlTcpPortNumber = " + wsdlTcpPortNumber );
        m_serviceLocation = setServiceLocation( m_serviceLocation, wsdlHostName, wsdlTcpPortNumber, hostName, newTcpPortNumber );
        
        LinkedList operations = new LinkedList();
        operations = getOperations(wsdlFile, m_portName);
        for (int i = 0; i < operations.size(); i++)
        {
            String operationName = (String) operations.get(i);
            String requestName = getOperationRequest( wsdlFile, operationName );
            String responseName = getOperationResponse( wsdlFile, operationName );
            String faultName = getOperationFault( wsdlFile, operationName);
            LinkedList parameters = getRequestParameters( wsdlFile, requestName );
            LinkedList parameterOrder = getRequestParameterOrder (wsdlFile, requestName );
            Parameter returnValue = getReturnParameter( wsdlFile, responseName );
            Parameter faultParameter = getFaultParameter( wsdlFile, faultName );
            ConstructedOperation operation = new ConstructedOperation( operationName, parameters, parameterOrder, returnValue, faultName, faultParameter );
            m_operations.add(operation);
            
        }
        //}
        //catch (MalformedURLException mue)
        //{
        //    mue.printStackTrace();
        //}
    }
    
    private String getServiceName( File wsdl )
    {
        String serviceName = "";
        try
        {
            ServiceParser serviceParser = new ServiceParser();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            FileInputStream fileInputStream = new FileInputStream( wsdl);
            //InputStream inputStream = m_wsdlUrl.openStream();
            saxParser.parse(fileInputStream, serviceParser);
            serviceName = serviceParser.m_service;
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch ( SAXException se)
        {
            se.printStackTrace();
        }
        return serviceName;
    }
    
    private URL getServiceLocation( File wsdl )
    {
        URL serviceLocation = null;
        try
        {
            serviceLocation = new URL("http://localhost:8080");
        
        }
        catch ( MalformedURLException mue)
        {
            mue.printStackTrace();
        }
        return serviceLocation;
    }
    
    private String getServiceLocation(File wsdlFile,
                                        String service,
                                        String port)
    {
        String serviceLocation = "";
        try
        {
            ServiceParser serviceParser = new ServiceParser( service, port );
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //InputStream inputStream = m_wsdlUrl.openStream();
            FileInputStream fileInputStream = new FileInputStream( wsdlFile);
            saxParser.parse( fileInputStream, serviceParser);
            serviceLocation = serviceParser.m_serviceLocation;
                
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }
        
        return serviceLocation;
    }
    
    private String setServiceLocation( String originalServiceLocation,
                                        String wsdlHostName,
                                        String wsdlTcpPortNumber,
                                        String newHostName,
                                        String newTcpPortNumber )
    {
        String newServiceLocation = "";
        if ( !newHostName.equals(""))
        {
            newServiceLocation = originalServiceLocation.replaceFirst( wsdlHostName, newHostName );
        }
        if ( !newTcpPortNumber.equals(""))
        {
            if (newServiceLocation.equals(""))
            {
                newServiceLocation = originalServiceLocation.replaceFirst( wsdlTcpPortNumber, newTcpPortNumber );
                
            }
            else
            {
                newServiceLocation = newServiceLocation.replaceFirst( wsdlTcpPortNumber, newTcpPortNumber );
            }
        }
        if (newServiceLocation.equals(""))
            return originalServiceLocation;
        else
            return newServiceLocation;
    }
    /**
     *  Input:
     *      serviceLocation - example http://rchuang-w2k02:8080/axis/DNSLookup.jws
     *
     */
    private String getInputPort( String serviceLocation )
    {
        String inputPort = "";
        String delim = ":";
        StringTokenizer stringTokenizer = new StringTokenizer( serviceLocation, delim );
        String protocol = stringTokenizer.nextToken();
        String host = stringTokenizer.nextToken();
        String portString = stringTokenizer.nextToken();
        
        String portDelim = "/";
        StringTokenizer stPort = new StringTokenizer( portString, portDelim );
        inputPort = stPort.nextToken();

        String serviceString = stPort.nextToken();
        //System.out.println("protocol = " + protocol + ", host = " + host + ", portString = " + portString 
        //            + ", inputPort = " + inputPort 
        //            + ", serviceString = " + serviceString );        
        return inputPort;
    }
    
    private String getInputHost( String serviceLocation )
    {
        String host = "";
        String delim = "/";
        StringTokenizer stringTokenizer = new StringTokenizer( serviceLocation, delim );
        
        String token = "";
        String protocol = stringTokenizer.nextToken();
        String hostPort = stringTokenizer.nextToken();
        
        StringTokenizer sT = new StringTokenizer( hostPort, ":");
        host = sT.nextToken();
        /*
        while ( stringTokenizer.hasMoreTokens() )
        {
            token = stringTokenizer.nextToken();
            System.out.println("token = " + token);
        }
         */
        
        return host;
    }
    
    private String getPort(String serviceName)
    {
        String portName = "";
        LinkedList ports = new LinkedList();
        try
        {
            ServiceParser serviceParser = new ServiceParser( serviceName );
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //InputStream inputStream = m_wsdlUrl.openStream();
            FileInputStream fileInputStream = new FileInputStream( m_wsdlFile);
            saxParser.parse(fileInputStream, serviceParser);
            ports = serviceParser.m_ports;
            portName = (String) ports.get(0);
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch ( SAXException se)
        {
            se.printStackTrace();
        }
        return portName;
    }
    
    // LinkedList of String
    private LinkedList getOperations( File wsdl, 
                                        String port)
    {
        LinkedList operations = new LinkedList();
        try
        {
            OperationParser operationParser = new OperationParser(port);
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //InputStream inputStream = m_wsdlUrl.openStream();
            FileInputStream fileInputStream = new FileInputStream( wsdl);
            saxParser.parse(fileInputStream, operationParser);
            operations = operationParser.m_operations;
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }        
         
        return operations;
    }
    
    private String getOperationRequest( File wsdlFile,
                                        String operationName )
    {
        String requestMessageName = "";                         
        try
        {
        System.out.println("operationName = " + operationName);
        MessageParser messageParser = new MessageParser( m_portName, operationName);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        //InputStream inputStream = m_wsdlUrl.openStream();
        FileInputStream fileInputStream = new FileInputStream( wsdlFile );
        saxParser.parse(fileInputStream, messageParser);
        requestMessageName = messageParser.m_request;
        System.out.println("requestMessageName = " + requestMessageName);

        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }        
        return requestMessageName;
    }
    
    /**
     *  Input: m_wsdlUrl
     *
     */
    private String getOperationResponse( File wsdlFile,
                                        String operationName )
    {
        String responseMessageName = "";
        try
        {        
            MessageParser messageParser = new MessageParser( m_portName, operationName);
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //InputStream inputStream = m_wsdlUrl.openStream();
            FileInputStream fileInputStream = new FileInputStream( wsdlFile);
            saxParser.parse(fileInputStream, messageParser);
            responseMessageName = messageParser.m_response;
            System.out.println("responseMessageName = " +  responseMessageName);
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }        
        return responseMessageName;
    }
    
    private String getOperationFault( File wsdlFile,
                                        String operationName )
    {
        String faultMessageName = "";
        try
        {
            MessageParser messageParser = new MessageParser( m_portName, operationName );
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            FileInputStream fileInputStream = new FileInputStream( wsdlFile);
            saxParser.parse( fileInputStream, messageParser );
            faultMessageName = messageParser.m_fault;
            
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }
        return faultMessageName;
    }
    // LinkedList of Parameter
    private LinkedList getRequestParameters( File wsdlFile,
                                            String requestMessageName )
    {
        LinkedList requests = new LinkedList();
        try
        {
            ParameterParser parameterParser = new ParameterParser( requestMessageName );
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //InputStream inputStream = m_wsdlUrl.openStream();
            FileInputStream fileInputStream = new FileInputStream( wsdlFile);
            saxParser.parse(fileInputStream, parameterParser);
            requests = parameterParser.m_parameters;
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }
        return requests;
    }
    
    // LinkedList of String
    private LinkedList getRequestParameterOrder( File wsdlFile,
                                                String requestMessageName )
    {
        LinkedList parameterOrder = new LinkedList();
        return parameterOrder;
    }
    
    private Parameter getReturnParameter( File wsdlFile,  
                                        String responseMessageName )
    {
        Parameter returnParameter = new Parameter();
        try
        {
            ParameterParser parameterParser = new ParameterParser( responseMessageName );
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser saxParser = spf.newSAXParser();
            FileInputStream fileInputStream = new FileInputStream( wsdlFile);
            saxParser.parse(fileInputStream, parameterParser );
            if (parameterParser.m_parameters.size() > 0)
            {
                returnParameter = (Parameter) parameterParser.m_parameters.get(0);
            }
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }

        return returnParameter;
    }

    private Parameter getFaultParameter( File wsdlFile,  
                                        String faultMessageName )
    {
        Parameter faultParameter = new Parameter();
        try
        {
            ParameterParser parameterParser = new ParameterParser( faultMessageName );
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser saxParser = spf.newSAXParser();
            //InputStream inputStream = m_wsdlUrl.openStream();
            FileInputStream fileInputStream = new FileInputStream( wsdlFile);
            saxParser.parse(fileInputStream, parameterParser );
            if (parameterParser.m_parameters.size() > 0)
            {
                faultParameter = (Parameter) parameterParser.m_parameters.get(0);
            }
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }

        return faultParameter;
    }
        
    public void dump()
    {
        System.out.println("ConstructedService: ");
        System.out.println("name = " + m_serviceName);
        for (int i = 0; i < m_operations.size(); i++)
        {
            ConstructedOperation operation = (ConstructedOperation) m_operations.get(i);
            operation.dump();
        }
    }
    public URI          m_wsdlUri = null;
    public URL          m_wsdlUrl = null;
    public File         m_wsdlFile = null;
    public String       m_serviceName = null;
    public String       m_portName = null;
    public String       m_serviceLocation = null;
    public LinkedList   m_operations = null;
}
