/*
 * Parameter.java
 *
 * Created on November 5, 2003, 2:59 PM
 */

package DynamicClient;

import org.apache.axis.Constants;
import javax.xml.namespace.QName;

/**
 *
 * @author  rchuang
 */
public class Parameter {
    
    /** Creates a new instance of Parameter */
    public Parameter() {
        m_name = null;
        m_type = null;
        m_typeString = null;
    }
    
    public Parameter( String paramName, QName paramType )
    {
        m_name = paramName;
        m_type = paramType;
        m_typeString = null;
    }
    
    public Parameter( String paramName, String paramType )
    {
        QName paramXmlType = stringToXmlType(paramType);
        
        m_name = paramName;
        m_typeString = paramType;  //paramXmlType;
        m_type = paramXmlType;
        System.out.println("Parameter: m_type (QName) = " + m_type);
    }
    

    /**
     *  TODO: How to specify the types dynamically?  
     *  Need to parse through all the xmlns??
     *  xmlns="http://schemas.xmlsoap.org/wsdl/" 
     *  xmlns:apachesoap="http://xml.apache.org/xml-soap" 
     *  xmlns:impl="http://admin.soap.eManager.cisco.com" 
     *  xmlns:intf="http://admin.soap.eManager.cisco.com" 
     *  xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" 
     *  xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" 
     *  xmlns:wsdlsoap="http://schemas.xmlsoap.org/wsdl/soap/" 
     *  xmlns:xsd="http://www.w3.org/2001/XMLSchema">
     */
    public QName stringToXmlType (String paramType )
    {
        QName paramXmlType = null;
        if( paramType.equals("xsd:string")
            || paramType.equals("soapenc:string"))
            paramXmlType = org.apache.axis.Constants.XSD_STRING;
        else if ( paramType.equals("xsd:int"))
            paramXmlType = org.apache.axis.Constants.XSD_INTEGER;
        else if ( paramType.equals("xsd:long")
            || paramType.equals("soapenc:long"))
            paramXmlType = org.apache.axis.Constants.XSD_LONG;
        else if ( paramType.equals("xsd:boolean"))
            paramXmlType = org.apache.axis.Constants.XSD_BOOLEAN;
        else if (paramType.equals("xsd:dateTime"))
            paramXmlType = org.apache.axis.Constants.XSD_DATETIME;
        //else if (paramType.equals("tns2:EmanagerInventoryException"))
            //paramXmlType = tns2:EmanagerInventoryException;
        else
            paramXmlType = null; // TODO
            
        return paramXmlType;
    }
    
    public void dump()
    {
        System.out.println("Parameter: name = " + m_name);
        System.out.println("type = " + m_type);
        System.out.println("type (String) = " + m_typeString );
    }
    
    public  String      m_name;
    public  QName       m_type;
    public  String      m_typeString;       // from the WSDL directly
}
