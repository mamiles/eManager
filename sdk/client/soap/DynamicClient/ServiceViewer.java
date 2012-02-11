/*
 * ServiceViewer.java
 *
 * Created on November 11, 2003, 10:24 AM
 * TODO: 
 * - create service from WSDL URL
 * - move output text area to a separate tabbed pane
 * - add "Input" heading
 * - add a column for PARAMETER_MODE
 * - add "Output" heading
 *  *  * - display SOAP fault in JTextArea
 * Done
 *- parse <wsdl:fault name = "...">
 *- add return value: name & type
 * - display type as raw string, rather than converting to XSD_*
 *
 * Usage:
 *  java DynamicClient/ServiceViewer
 * 
 * Prerequisites:
 * CLASSPATH must include
 * axis.jar
 * axis-ant.jar
 * commons-logging.jar
 * commons-discovery.jar
 * jaxrpc-api.jar
 * jax-qname.jar
 * log4j-1.2.8.jar
 * saaj.jar
 * wsdl4j.jar
 */

package DynamicClient;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.lang.String;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;

/**
 *
 * @author  rchuang
 */
public class ServiceViewer extends javax.swing.JFrame {
    
    final static int HEIGHT = 700;
    final static int WIDTH  = 700;
    final static int MAX_OPERATIONS = 50;
    final static int MAX_PARAMETERS = 50;
    final static int DEFAULT_COLUMNS = 20;  // size of m_wsdlUrl, m_wsdlFile
    /** Creates new form ServiceViewer */
    public ServiceViewer() 
    {
        initComponents();
        m_aboutDialog = null;
        setTitle("NMTG SOAP Client");
        setSize( WIDTH, HEIGHT);
        createMenus();
        
        Container container = getContentPane();
        JPanel masterPanel = createMainPanel();        
        container.add(masterPanel, BorderLayout.CENTER);
    }
    /*
    public ServiceViewer() 
    {
        initComponents();
        setTitle("eManager AXIS client");
        setSize( WIDTH, HEIGHT);
        createMenus();
        
        File wsdlFile = new File("D:\\work\\dns.wsdl");
        ConstructedService constructedService = new ConstructedService(wsdlFile);
        m_service = constructedService;
        constructedService.dump();
        Container container = getContentPane();
        JPanel masterPanel = createMainPanel(constructedService);        
        container.add(masterPanel, BorderLayout.CENTER);
    }
     */
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        pack();
    }//GEN-END:initComponents
    
    private void createMenus()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar( menuBar );
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new WSDLFileBrowseAction());
        fileMenu.add(openMenuItem);
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        // exitMenuItem.addActionListener();
        fileMenu.add(exitMenuItem);
        
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        
        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);
        aboutMenuItem.addActionListener(new
            ActionListener()
            {
                public void actionPerformed(ActionEvent ae)
                {
                    if (m_aboutDialog == null)
                    {
                        m_aboutDialog = new AboutDialog(ServiceViewer.this);
                    }
                    m_aboutDialog.show();
                }
            }
        );
        
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ServiceViewer().show();
    }

    /**
     *  mainPanel (2, 1)
     *      row 0: m_wsdlPanel
     *      row 1: m_servicePanel
     *
     */    
    JPanel createMainPanel()
    {
        m_mainPanel = new JPanel();
        m_mainPanel.setLayout( new BorderLayout() );
        m_wsdlPanel = createWsdlPanel();
        
        m_mainPanel.add(m_wsdlPanel, BorderLayout.NORTH);
        
        m_servicePanel = new JPanel();
        m_servicePanel.setLayout( new BorderLayout() );
        m_mainPanel.add(m_servicePanel, BorderLayout.CENTER);
        return m_mainPanel;        
    }


    
    /**
     *  createWsdlPanel()
        // wsdlPanel(2,1)
        // row 0:   wsdlInputPanel
        // row 1:   wsdlOutputPanel
        
        
        //      wsdlInputPanel( 5, 1)
        //      row 0: WSDL label
        //      row 1: WSDL URL input
        //      row 2: WSDL file input
        //      row 3: web service host, port
      
        //      row 4 : controls: clear, add   
                  
        
        //      wsdlOutputPanel( 5, 2)
        //      row 0:  WSDL
        //      row 1:  Service
        //      row 2:  Port
        //      row 3:  Address
        //      row 4:  Operations:
    
     *  Output:
     *      m_portTextField
     *      m_hostTextField
     */
    
    JPanel createWsdlPanel()
    {
        JPanel wsdlPanel = new JPanel();
        wsdlPanel.setLayout(new GridLayout(2, 1));

        
        JPanel wsdlInputPanel = new JPanel();
        wsdlInputPanel.setLayout( new GridLayout(5, 1));
        
        // row 0
        JPanel  wsdlInputHeaderPanel = new JPanel();
        wsdlInputHeaderPanel.setLayout( new BorderLayout());
        JLabel wsdlInputHeaderLabel = new JLabel("WSDL: ");
        wsdlInputHeaderPanel.add( wsdlInputHeaderLabel, BorderLayout.WEST);
        wsdlInputPanel.add(wsdlInputHeaderPanel);
        
        // row 1
        JPanel  wsdlInputUrlPanel = new JPanel();
        //wsdlInputUrlPanel.setLayout ( new BorderLayout ());
        
        JLabel  urlLabel = new JLabel ("URL: ");
        wsdlInputUrlPanel.add( urlLabel); //, BorderLayout.WEST);
        m_wsdlUrl = new JTextField("http://rchuang-w2k02:8080/axis/DNSLookup.jws?wsdl", DEFAULT_COLUMNS);
        wsdlInputUrlPanel.add( m_wsdlUrl); //, BorderLayout.CENTER);
        JButton addUrlButton = new JButton("Create service (URL)");
        wsdlInputUrlPanel.add( addUrlButton);
        wsdlInputPanel.add( wsdlInputUrlPanel );
        
        // row 2
        JPanel wsdlInputFilePanel = new JPanel();
        //wsdlInputFilePanel.setLayout( new BorderLayout());
        JLabel inputFileLabel = new JLabel("File: ");
        wsdlInputFilePanel.add( inputFileLabel); //, BorderLayout.WEST);
        String[] fileList = { File.separator 
                            + "auto" + File.separator 
                            + "austin-cdm" + File.separator 
                            + "emanager" + File.separator 
                            + "wsdl",
                                " "};
        m_wsdlFileModel = new DefaultComboBoxModel(fileList);
        m_wsdlFile = new JComboBox(m_wsdlFileModel); 
        m_wsdlFile.setEditable(true);
        wsdlInputFilePanel.add( m_wsdlFile); //, BorderLayout.CENTER);
        JButton browse = new JButton("Browse");
        browse.addActionListener( new WSDLFileBrowseAction( (String) m_wsdlFile.getSelectedItem() ));
        wsdlInputFilePanel.add( browse); //, BorderLayout.EAST);        
        JButton addFileButton = new JButton("Create service (file)");
        addFileButton.addActionListener( new ParseServiceAction());
        wsdlInputFilePanel.add( addFileButton);
        wsdlInputPanel.add( wsdlInputFilePanel );

        // row 3: web service host, port
        JPanel  webServiceHostPanel = new JPanel();
        JLabel  hostLabel = new JLabel("Override host IP address: ");
        webServiceHostPanel.add(hostLabel);
        m_hostTextField = new JTextField(4);
        webServiceHostPanel.add(m_hostTextField);
        JLabel portNumberLabel = new JLabel("Override TCP port number: ");
        webServiceHostPanel.add(portNumberLabel);
        m_portTextField = new JTextField(4);
        webServiceHostPanel.add ( m_portTextField );
        wsdlInputPanel.add(webServiceHostPanel);
               
        // row 4: controls
        JPanel wsdlControlPanel = new JPanel(); 
        wsdlControlPanel.setLayout( new BorderLayout());
        JButton   clearButton = new JButton("Clear");
        clearButton.addActionListener( new ResetStateAction());
        wsdlControlPanel.add(clearButton, BorderLayout.WEST);
        JButton addUrl = new JButton("Add service");
        //addUrl.addActionListener( new ParseServiceAction());
        //wsdlControlPanel.add(addUrl, BorderLayout.EAST);
        wsdlInputPanel.add( wsdlControlPanel);
        
        wsdlPanel.add( wsdlInputPanel);
         
        JPanel wsdlOutputPanel = new JPanel();
        wsdlOutputPanel.setLayout( new GridLayout(5, 2 ));
        
        JLabel  wsdlLabel = new JLabel("WSDL: ");
        wsdlOutputPanel.add(wsdlLabel);
        m_wsdlValue = new JLabel();
        wsdlOutputPanel.add(m_wsdlValue);
        JLabel endpointLabel = new JLabel("Service: ");
        wsdlOutputPanel.add( endpointLabel);
        m_serviceValue = new JLabel();//("DNSLookupService");
        wsdlOutputPanel.add( m_serviceValue );
        
        JLabel portLabel = new JLabel("Port:");
        wsdlOutputPanel.add(portLabel);
        m_portValue = new JLabel();     //("DNSLookup");
        wsdlOutputPanel.add(m_portValue);
        
        JLabel addressLabel = new JLabel("Address:");
        wsdlOutputPanel.add(addressLabel);
        m_addressValue = new JLabel();  //("http://rchuang-w2k02:8080/axis/DNSLookup.jws");
        wsdlOutputPanel.add(m_addressValue);
        
        JLabel  operationLabel = new JLabel("Operations: ");
        wsdlOutputPanel.add(operationLabel);
        m_operationsCountValue = new JLabel();
        wsdlOutputPanel.add(m_operationsCountValue);
        
        wsdlPanel.add(wsdlOutputPanel);
        
        return wsdlPanel;
        
    }

    JPanel createOperationPanel(int index, ConstructedOperation operation)
    {
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout( new BorderLayout());
        
        JPanel inPanel = new JPanel();
        inPanel.setLayout( new BorderLayout());
        
        JPanel urlPanel = new JPanel();
        urlPanel.setLayout( new GridLayout(2, 1));
        
        JPanel urlInputPanel = new JPanel();        
        urlInputPanel.setLayout(new BorderLayout());
        //JLabel urlLabel = new JLabel("Service URL");
        //m_urlText = new JTextField("http://rchuang-w2k02:8080/axis/DNSLookup.jws");
        //urlInputPanel.add(urlLabel, BorderLayout.WEST);
        //urlInputPanel.add(m_urlText, BorderLayout.CENTER);
        urlPanel.add( urlInputPanel );
        
        JPanel urlControlPanel = new JPanel();
        urlControlPanel.setLayout( new BorderLayout());
        //JButton addUrl = new JButton("Add service");
        //urlControlPanel.add(addUrl, BorderLayout.EAST);
        urlPanel.add( urlControlPanel);
        

        
        inPanel.add(urlPanel, BorderLayout.NORTH);
        
        JPanel parameterPanel = new JPanel();
        parameterPanel.setLayout( new BorderLayout());
        // parameterPanel
        //      inputParameterPanel
        //      outputValuePanel
        //      faultPanel
       
        
        JPanel inputParameterPanel = new JPanel();
        m_paramCount[index] = operation.m_parameters.size();
        System.out.println( "numParameters = " + m_paramCount[index]);
        inputParameterPanel.setLayout( new GridLayout( m_paramCount[index]  + 1, 3)); // "+1" is for the title heading "Input parameters"
        
        JLabel  inputLabel = new JLabel("Input parameters: ");
        inputParameterPanel.add(inputLabel);
        JLabel  inBlank01 = new JLabel();
        inputParameterPanel.add(inBlank01);
        JLabel  inBlank02 = new JLabel();
        inputParameterPanel.add(inBlank02);
        
        for (int i = 0; i < m_paramCount[index]; i++)
        {
            Parameter parameter = (Parameter) operation.m_parameters.get(i);
            m_paramLabel[index][i] = new JLabel(parameter.m_name);
            inputParameterPanel.add(m_paramLabel[index][i] );
            
            m_paramType[index][i] = parameter.m_type;
            
            m_paramTypeString[index][i] = new JLabel("[" + parameter.m_typeString + "]", SwingConstants.LEFT);
            m_paramTypeString[index][i].setToolTipText(m_paramType[index][i].toString());
            inputParameterPanel.add(m_paramTypeString[index][i]);
            
            m_paramText[index][i] = new JTextField();
            inputParameterPanel.add(m_paramText[index][i]);     
            
            
        }
        parameterPanel.add(inputParameterPanel, BorderLayout.NORTH);
        
        // Return value panel
        JPanel outputValuePanel = new JPanel();
        outputValuePanel.setLayout( new GridLayout(2, 3));
        JLabel outputTitle = new JLabel("Return value:");
        outputValuePanel.add( outputTitle);
        JLabel blank01 = new JLabel();
        outputValuePanel.add( blank01 );
        JLabel blank02 = new JLabel();
        outputValuePanel.add( blank02);
        Parameter returnParameter = operation.m_returnParameter;        
        if (returnParameter.m_name != null )
        {
            m_returnValueName[index] = new JLabel(returnParameter.m_name);
            outputValuePanel.add(m_returnValueName[index]);
            //m_returnValueType[index] = new JLabel(returnParameter.m_type.toString());
            m_returnValueTypeString[index] = new JLabel( "[" + returnParameter.m_typeString + "]");
            m_returnValueType[index] = returnParameter.m_type;
            if (m_returnValueType[index] != null)
            {
                m_returnValueTypeString[index].setToolTipText(m_returnValueType[index].toString());
            }
            outputValuePanel.add(m_returnValueTypeString[index]);
            m_returnValue[index] = new JTextField();
            outputValuePanel.add(m_returnValue[index]);                       
        }
        else
        {
            JLabel blank10 = new JLabel();
            outputValuePanel.add(blank10);
            JLabel blank11 = new JLabel();
            outputValuePanel.add(blank11);
            JLabel blank12 = new JLabel();
            outputValuePanel.add(blank12);
        }
        parameterPanel.add(outputValuePanel, BorderLayout.CENTER);
        
        // Fault panel
        JPanel faultPanel = new JPanel();
        faultPanel.setLayout( new GridLayout( 3, 3));    // assume only 1 fault per operation
        
        // row 0
        JLabel  faultTitle = new JLabel("Fault: ");
        faultPanel.add(faultTitle);
        JLabel blankf01 = new JLabel();
        faultPanel.add( blankf01);
        JLabel blankf02 = new JLabel();
        faultPanel.add( blankf02);
        
        // row 1
        JLabel faultMessageHeading = new JLabel("<message name>");
        faultPanel.add(faultMessageHeading);
        JLabel faultPartNameHeading = new JLabel("<part name>");
        faultPanel.add( faultPartNameHeading);
        JLabel faultPartTypeHeading = new JLabel("<part type>");
        faultPanel.add( faultPartTypeHeading);        
        
        // row 2
        String faultName = operation.m_faultName;
        if (faultName != null)
        {
            m_faultMessageName[index] = new JLabel(faultName);
            faultPanel.add( m_faultMessageName[index] );
            //String faultPartName = operation.m_
            m_faultPartName[index] = new JLabel(operation.m_faultParameter.m_name);
            faultPanel.add( m_faultPartName[index]);
            m_faultPartType[index] = new JLabel("[" + operation.m_faultParameter.m_typeString + "]");
            faultPanel.add( m_faultPartType[index]);
        }
        else
        {
            JLabel blankf10 = new JLabel();
            faultPanel.add(blankf10);
            JLabel blankf11 = new JLabel();
            faultPanel.add(blankf11);
            JLabel blankf12 = new JLabel();
            faultPanel.add(blankf12);
        }        
        parameterPanel.add(faultPanel, BorderLayout.SOUTH);
        
        inPanel.add( parameterPanel, BorderLayout.CENTER );
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout( new BorderLayout());
        JButton invokeButton = new JButton( "Invoke");
        invokeButton.addActionListener( new InvokeSoapOperation());
        controlPanel.add( invokeButton, BorderLayout.EAST);
        JButton clearButton = new JButton("Clear");
        controlPanel.add( clearButton, BorderLayout.WEST);
        
        inPanel.add( controlPanel, BorderLayout.SOUTH);
        
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout( new BorderLayout());
        m_outputTextArea[index] = new JTextArea();
        m_outputScrollArea[index] = new JScrollPane( m_outputTextArea[index]);
        outputPanel.add( m_outputScrollArea[index], BorderLayout.CENTER);
        
        operationPanel.add(inPanel, BorderLayout.NORTH);
        operationPanel.add(outputPanel, BorderLayout.CENTER);

        return operationPanel;
    }
    
    public void emit_debug(String info)
    {
        System.out.println(info);
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    JPanel      m_mainPanel;
    JPanel      m_wsdlPanel;
    JPanel      m_servicePanel;
    
    DefaultComboBoxModel m_wsdlFileModel;
    JComboBox   m_wsdlFile;
    JTextField  m_wsdlUrl;
    JTextField  m_hostTextField;
    JTextField  m_portTextField;    // TCP port, not WSDL port
    
    
    ConstructedService m_service;
    JLabel      m_wsdlValue;
    JLabel      m_serviceValue;
    JLabel      m_portValue;
    JLabel      m_addressValue;
    JLabel      m_operationsCountValue;
    
    JTabbedPane m_operations;
    JLabel      m_paramLabel[][]            = new JLabel[MAX_OPERATIONS][MAX_PARAMETERS];
    JLabel      m_paramTypeString[][]       = new JLabel[MAX_OPERATIONS][MAX_PARAMETERS];
    QName       m_paramType[][]             = new QName[MAX_OPERATIONS][MAX_PARAMETERS];
    JTextField  m_paramText[][]             = new JTextField[MAX_OPERATIONS][MAX_PARAMETERS];
    JTextArea   m_outputTextArea[]          = new JTextArea[MAX_OPERATIONS];
    JScrollPane m_outputScrollArea[]        = new JScrollPane[MAX_OPERATIONS];
    int         m_paramCount[]              = new int[MAX_PARAMETERS];
    JLabel      m_returnValueName[]         = new JLabel[MAX_OPERATIONS];
    QName       m_returnValueType[]         = new QName[MAX_OPERATIONS];
    JLabel      m_returnValueTypeString[]   = new JLabel[MAX_OPERATIONS];    
    JTextField  m_returnValue[]             = new JTextField[MAX_OPERATIONS];  
    JLabel      m_faultMessageName[]        = new JLabel[MAX_OPERATIONS];
    JLabel      m_faultPartName[]           = new JLabel[MAX_OPERATIONS];
    JLabel      m_faultPartType[]           = new JLabel[MAX_OPERATIONS];
    
    JDialog     m_aboutDialog;
    
    private class ResetStateAction  implements ActionListener
    {
        
        public void actionPerformed(ActionEvent e) 
        {
            
            m_servicePanel.removeAll();
            m_wsdlValue.setText("");
            m_serviceValue.setText("");
            m_portValue.setText("");
            m_addressValue.setText("");
            m_operationsCountValue.setText("");
        }
        
    }
    private class WSDLFileBrowseAction implements ActionListener
    {
        public WSDLFileBrowseAction()
        {
            _currentDirectoryName = null;
        }
        
        public WSDLFileBrowseAction( String inputDir )
        {
            _currentDirectoryName = inputDir;
        }
        
        public void actionPerformed( ActionEvent ae)
        {
            JFileChooser wsdlChooser = new JFileChooser(_currentDirectoryName);
            WSDLFileFilter wsdlFileFilter = new WSDLFileFilter();
            //wsdlFileFilter.addExtension("wsdl");
            //wsdlFileFilter.setDescription("WSDL");
            wsdlChooser.setFileFilter(wsdlFileFilter);
            int returnVal = wsdlChooser.showOpenDialog(null);
            String selectedWsdl = "";
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    selectedWsdl = wsdlChooser.getSelectedFile().getCanonicalPath();
                    m_wsdlFileModel.insertElementAt( selectedWsdl, 0);
                    
                    m_wsdlFile.setSelectedIndex(0);
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
                
                
            }
        }
        
        String _currentDirectoryName;
    }
    
    public class WSDLFileFilter extends FileFilter
    {
        
        public boolean accept(File f) {
            return true;
        }
        
        public String getDescription() {
            return ("WSDL files");
        }
        
    }
    // Associated with the "Add " button
    //  Input:
    //      m_wsdlFile
    //      m_servicePanel
    //  Parse the WSDL: file or URL
    //  Create the opeartion panel
    //  Add to main panel
    //  set m_serviceValue
    //  set m_portValue
    //  set m_addressValue
    private class   ParseServiceAction implements ActionListener
    {
        public void actionPerformed( ActionEvent ae)
        {

            File wsdlFile = new File( (String) m_wsdlFile.getSelectedItem());
            ConstructedService constructedService = new ConstructedService( wsdlFile, m_hostTextField.getText().trim(), m_portTextField.getText().trim() );
            //try
            //{
            //URI wsdlURI = new URI( m_wsdlUrl.getText());
            //ConstructedService constructedService = new ConstructedService(  wsdlURI);
            m_service = constructedService;
            //m_service.dump();
            
            m_operations = new JTabbedPane();
            m_servicePanel.add( m_operations, BorderLayout.CENTER);
            for (int i = 0; i < m_service.m_operations.size(); i++)
            {
                ConstructedOperation operation = (ConstructedOperation) m_service.m_operations.get(i);
                JPanel operationPanel = createOperationPanel( i, operation );
                m_operations.addTab( operation.m_name, operationPanel);
            }
                        
            m_wsdlValue.setText((String) m_wsdlFile.getSelectedItem());
            m_serviceValue.setText(m_service.m_serviceName);
            m_portValue.setText(m_service.m_portName);
            m_addressValue.setText(m_service.m_serviceLocation);          
            Integer opCount = new Integer(m_service.m_operations.size());
            m_operationsCountValue.setText(opCount.toString());
                
            m_wsdlUrl.setText("");
            m_wsdlFile.setSelectedIndex(0);            
            //}
            //catch (URISyntaxException use)
            //{
            //    use.printStackTrace();
            //}
        }
    }
    
    /**
     * InvokeAxisOperation
     */
    private class InvokeSoapOperation implements ActionListener
    {
        public void actionPerformed (ActionEvent ae)
        
        {
            int currentTabIndex = -1;
            try
            {
              
                String  endpoint = m_addressValue.getText();
                System.out.println("ISO: endpoint = " + endpoint);
                currentTabIndex = m_operations.getSelectedIndex();
                System.out.println("ISO: currentTabIndex = " + currentTabIndex);
                String  method   = m_operations.getTitleAt(currentTabIndex);

                Service service = new Service();
                Call    call    = (Call) service.createCall();

                call.setTargetEndpointAddress( new java.net.URL( endpoint ));
                call.setOperationName( method );                                
                
                // Set input parameter name, type, mode
                for (int i = 0; i < m_paramCount[currentTabIndex]; i++)
                {
                    System.out.println(" [" + i + "]=" + m_paramTypeString[currentTabIndex][i].getText());
                    String parameterName = "op" + i;
                    //QName xmlType = new QName( m_paramTypeString[currentTabIndex][i].getText());
                    QName xmlType = m_paramType[currentTabIndex][i];
                    System.out.println("xmlType = " + xmlType.toString());
                    // TODO: how to get ParameterMode from WSDL?
                    call.addParameter( parameterName, xmlType, ParameterMode.IN);   // TODO Handle ParameterMode.INOUT
                   
                }

                // Set parameter values as Object[]
                Object[] inObjects = new Object[m_paramCount[currentTabIndex]]; 
                for (int j = 0; j < m_paramCount[currentTabIndex]; j++)
                {
                    String currentField = m_paramText[currentTabIndex][j].getText();
                    if (m_paramType[currentTabIndex][j] == XMLType.XSD_STRING)
                    {
                        inObjects[j] = currentField;
                    }
                    else if (m_paramType[currentTabIndex][j] == XMLType.XSD_INT)
                    {
                        inObjects[j]= new Integer(currentField);
                    }
                    else if (m_paramType[currentTabIndex][j] == XMLType.XSD_LONG)
                    {
                        inObjects[j] = new Long( currentField);
                    }
                    else
                    {
                        System.err.println("ISO: Error!  Unsupported parameter type");
                    }
                    
                    System.out.println("m_paramText[" + j + "]=" + m_paramText[currentTabIndex][j].getText());
                   
                }                
                
                // Set return type
                QName   returnType = null;
                returnType = m_returnValueType[currentTabIndex];                
                System.out.println("ISO: returnType (QName) = " + returnType);
                
                if (returnType != null)
                {
                    
                    call.setReturnType( returnType );
                    if (m_returnValueType[currentTabIndex] != null)
                    {
                        String returnT = m_returnValueType[currentTabIndex].toString();
                        System.out.println("return = " + returnT);
                    }
                
                    String returnValue = "";
                    if (returnType == XMLType.XSD_STRING)
                    {
                        returnValue = (String) call.invoke(inObjects);   // how to dynamically determine type of return value?
                                                                            // TODO: handle other return types
                    }
                    else if (returnType == XMLType.XSD_INT)
                    {
                        Integer  returnInt    = (Integer)  call.invoke(inObjects);
                        returnValue =  returnInt.toString();
                    }
                    else if (returnType == XMLType.XSD_INTEGER)
                    {
                        BigInteger returnBigInt = (BigInteger) call.invoke(inObjects);
                        returnValue = returnBigInt.toString();
                    }
                    else if (returnType == XMLType.XSD_LONG)
                    {
                        Long returnLong = (Long) call.invoke(inObjects);
                        returnValue = returnLong.toString();
                    }
                    /*
                    else if (returnType == XMLType.XSD_BOOLEAN)
                    {
                        boolean returnBool = (boolean) call.invoke(inObjects);
                        returnValue = returnBool;
                    }
                     */
                    else
                    {
                        System.err.println("ISO: Error!  Unsupported return type!");
                        // TODO: throw Exception
                    }
                    m_outputTextArea[currentTabIndex].append(returnValue + "\n");
                    m_returnValue[currentTabIndex].setText(returnValue);
                    System.out.println("returnValue = " + returnValue );
                }
                else    // returnType == null
                {
                    call.setReturnType( XMLType.XSD_ANY ); // Must set or get 
                    call.invoke(inObjects);
                }
                

                System.out.println("ISO: Operation complete");
                 
            }
            catch (ServiceException se)
            {
                m_outputTextArea[currentTabIndex].append(se.toString() + "\n");
                se.printStackTrace();
            }
            catch (MalformedURLException mue)
            {
                mue.printStackTrace();
            }
            catch (RemoteException re)
            {
                re.printStackTrace();
            }
           
        }
    }
    
    class AboutDialog extends JDialog
    {
        public AboutDialog( JFrame owner)
        {
            super( owner, "About NMTG SOAP / AXIS client", true);
            Container contentPane = getContentPane();
            
            contentPane.add( new JLabel(
                "<HTML>NMTG client for dynamically invoking SOAP operations<HR>"
                + "Version 0.6<BR>"
                + "1/20/2004<BR>"
                + "Roger Huang (rchuang)<BR>"
                + "Implemented using Apache AXIS 1.1</HTML>"),
                BorderLayout.CENTER);
            
            JButton okButton = new JButton("OK");
            okButton.addActionListener( new
                ActionListener()
            {
                public void actionPerformed(ActionEvent ae)
                {
                    setVisible(false);
                }
            });
            
            JPanel panel = new JPanel();
            panel.add(okButton);
            contentPane.add(panel, BorderLayout.SOUTH);
            setSize( 250, 250);
        }
    }
};