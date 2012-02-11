/*
 * MessageSender.java
 *
 * Created on December 16, 2003, 9:34 AM
 *
 *  TODO:
 *  Populate TIBCO subjects from Properties file.  
 *  Done:
 *      Save the modified XML document
 *      Allow specifying the directory of input XML files
 *      Allow specifying the directory of output modified XML files
 *      
 *      TIBCO subject GUI widget is ComboBox that remembers entered values.
 */

package TIBCOClient;
import java.lang.Short;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.StringWriter;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvXml;
import com.tibco.tibrv.TibrvRvdTransport;

/**
 *
 * @author  rchuang
 */
public class MessageSender extends javax.swing.JFrame
{
    final static int HEIGHT = 700;
    final static int WIDTH  = 700;
    final static int    MAX_ELEMENTS = 50;  // # of XML Elements in the eManager XML document
    final static String XML_MESSAGE_DIRECTORY = new String ("D:" + File.separator + "work");
       
                                    
    final static String[] SEND_SUBJECT_NAMES = { "cisco.mgmt.cs.command",
                                                 "cisco.mgmt.cns.discovery.command",
                                                 "cisco.mgmt.emanager",
                                                 "cisco.mgmt.emanager.event.request",
                                                 "cisco.mgmt.emanager.inventory.request",
                                                 "cisco.mgmt.emanager.process.request",
                                                 "cisco.mgmt.emanager.log.request",
                                                 "cisco.mgmt.emanager.audit.request"};
                                                 
                                                 
   final static String[] RECEIVE_SUBJECT_NAMES = { "cisco.mgmt.cs.response",
                                                    "cisco.mgmt.cns.discovery.response",
                                                    "cisco.mgmt.cs.notification.success",
                                                    "cisco.mgmt.cs.notification.failure",
                                                    "cisco.mgmt.cs.notification.info",
                                                    "cisco.mgmt.emanager.inventory.response",
                                                    "cisco.mgmt.emanager.event.response",
                                                    "cisco.mgmt.emanager.process.response",
                                                    "cisco.mgmt.emanager.log.response",
                                                    "cisco.mgmt.emanager.audit.response"                                                    
                                                    };
                                                    
    final static      String[] FIELD_NAMES = { "DATA",      
                                "CS_DATA",
                                "AD_DATA" };         
                                
    final static String[] SERVICES = { "7500" };
    final static String[] NETWORKS = {};
    final static String[] DAEMONS = { "tcp:7500" };
    
    /** Creates a new instance of MessageSender */
    public MessageSender() 
    {
        initComponents();
        setTitle("NMTG TIBCO RV client");
        setSize(WIDTH, HEIGHT);
        createMenus();
        
        Container container = getContentPane();
        JPanel masterPanel = createMainPanel();
        container.add(masterPanel, BorderLayout.CENTER);
        
        _wireFormatDatatype = TibrvMsg.DEFAULT;
        m_messageSourceDirectory = "";
        m_messageSaveDirectory = "";
        m_outputDirectory = "";
    }
    
    private void initComponents()
    {
        m_aboutDialog = null;
        addWindowListener( new java.awt.event.WindowAdapter()
        {
            public void windowClosing( java.awt.event.WindowEvent evt)
            {
                exitForm(evt);
            }
        }
        );
    }
    
    private void createMenus()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar( menuBar );
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener( new BrowseXMLMessageAction());
        fileMenu.add(openMenuItem);
        
        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        
        JMenu   toolMenu = new JMenu("Tools");
        menuBar.add(toolMenu);
        
        JMenuItem sourceDirMenuItem = new JMenuItem("Set Message Source Directory");
        sourceDirMenuItem.addActionListener( new SetMessageSourceAction());
        toolMenu.add(sourceDirMenuItem);
        
        JMenuItem saveDirMenuItem = new JMenuItem("Set Saved Messages Directory");
        saveDirMenuItem.addActionListener( new SetSavedMessageDirectoryAction());
        toolMenu.add(saveDirMenuItem);
        
        JMenuItem outputDirMenuItem = new JMenuItem("Set Output Directory");
        toolMenu.add(outputDirMenuItem);
        
        //JMenuItem traversePreorderMenuItem = new JMenuItem("Traverse document - preorder");
        //traversePreorderMenuItem.addActionListener( new PreorderTraverseAction(m_xmlStringWriter.getBuffer().toString()));
        //toolMenu.add(traversePreorderMenuItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener( new
            ActionListener()
            {
                public void actionPerformed(ActionEvent ae)
                {
                    if (m_aboutDialog == null)
                    {
                        m_aboutDialog = new AboutDialog(MessageSender.this);
                    }
                    m_aboutDialog.show();
                }
            });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
    }
    
    private void exitForm( WindowEvent evt)
    {
        System.exit(0);
    }
    /**
     * @param param the command line arguments
     */
    public static void main(String[] param) 
    {
        MessageSender messageSender = new MessageSender();
        messageSender.show();
    }
    
    private JPanel createMainPanel()
    {
        m_mainPanel = new JPanel();
        JTabbedPane mainTabbedPane = new JTabbedPane();
        m_mainPanel.add(mainTabbedPane);
        
        // tab 0
        m_senderPanel = new JPanel();
        m_senderPanel.setLayout( new BorderLayout());
        m_xmlPanel = createXmlPanel();        
        m_senderPanel.add( m_xmlPanel, BorderLayout.NORTH);
        
        m_outputPanel = new JPanel();
        m_senderPanel.add(m_outputPanel, BorderLayout.CENTER);    
        mainTabbedPane.addTab( "Sender", m_senderPanel );
        
        // tab 1
        JPanel receiverPanel = new JPanel();
        receiverPanel.setLayout( new BorderLayout());
        JPanel receiverInputParameterPanel = createReceiverInputParameterPanel();
        receiverPanel.add(receiverInputParameterPanel, BorderLayout.NORTH);
        JPanel receiverOutputPanel = createReceiverOutputParameterPanel();
        receiverPanel.add(receiverOutputPanel, BorderLayout.CENTER);
        
        mainTabbedPane.addTab("Receiver", receiverPanel);
        
        return m_mainPanel;
    }
    /**
     *  xmlPanel( 2, 1)
     *      row 0: xmlInputPanel
     *          xmlInputPanel(4, 1)
     *            
     *              row 0: XML label, XML file input
     *              row 1: TIBCO parameters: Send subject, Reply subject, field name
     *              row 2: TIBCO parameters: Service, Network, Daemon
     *              row 3: controls: "Clear" button
     *      row 1: xmlOutputPanel
     *              
     */
    private JPanel createXmlPanel()
    {  
        m_stringifiedXml = new String();
        JPanel xmlPanel = new JPanel();
        //xmlPanel.setLayout( new GridLayout(2, 1));
        
        JPanel xmlInputPanel = new JPanel();
        xmlInputPanel.setLayout( new GridLayout( 5, 1));
        // row 0,0
        //JPanel xmlInputHeaderPanel = new JPanel();
        //xmlInputHeaderPanel.setLayout( new BorderLayout());
        //JLabel xmlInputHeaderLabel = new JLabel("TIBCO RV message input:");
        //xmlInputHeaderPanel.add( xmlInputHeaderLabel, BorderLayout.WEST);
        //xmlInputPanel.add(xmlInputHeaderPanel);
        
        // row 0, 1
        JPanel xmlInputFilePanel = new JPanel();
        JLabel inputFileLabel = new JLabel("XML Message: ");
        xmlInputFilePanel.add(inputFileLabel);
        
       
        String[] fileList = { "/tmp",
                            m_messageSourceDirectory };
        m_xmlFileModel = new DefaultComboBoxModel(fileList);
        m_xmlFileText = new JComboBox(m_xmlFileModel);
        m_xmlFileText.setEditable(true);
        xmlInputFilePanel.add( m_xmlFileText  );
        //xmlInputFilePanel.add(m_xmlFile);
        JButton browse = new JButton("Browse");
        browse.addActionListener( new BrowseXMLMessageAction());
        xmlInputFilePanel.add(browse);
        JButton addFileButton = new JButton("Parse message");
        addFileButton.addActionListener( new ParseMessageAction());
        xmlInputFilePanel.add(addFileButton);
        
        //JButton preorder = new JButton("Pre-order traverse");
        //preorder.addActionListener(new PreorderTraverseAction(m_xmlStringWriter.getBuffer().toString()));
        //xmlInputFilePanel.add(preorder);
        
        
        xmlInputPanel.add(xmlInputFilePanel);
        
        // row 0, 2: TIBCO parameters 1
        JPanel tibcoParameterPanel = new JPanel();
        //tibcoParameterPanel.setLayout( new BorderLayout());
        JLabel tibcoParameterLabel = new JLabel("TIBCO: ");
        tibcoParameterPanel.add(tibcoParameterLabel);   //, BorderLayout.WEST);
        JLabel tibcoSendSubjectLabel = new JLabel("Send Subject:");
        tibcoParameterPanel.add( tibcoSendSubjectLabel);

        m_sendSubjectModel = new DefaultComboBoxModel(SEND_SUBJECT_NAMES);
        m_tibcoSendSubjectText = new JComboBox(m_sendSubjectModel);
        m_tibcoSendSubjectText.setEditable(true);
        //m_tibcoSendSubjectText = new JTextField(10);
        tibcoParameterPanel.add(m_tibcoSendSubjectText);
        
        JLabel tibcoReplySubjectLabel = new JLabel("Reply Subject: ");
        tibcoParameterPanel.add( tibcoReplySubjectLabel);
        m_tibcoReplySubjectText = new JTextField("cisco.mgmt");
        tibcoParameterPanel.add( m_tibcoReplySubjectText);
        xmlInputPanel.add(tibcoParameterPanel);
        
        // row 0, 3
        JPanel tibcoFieldPanel = new JPanel();
        
        JLabel fieldTypeLabel = new JLabel("Wire format datatype: ");
        tibcoFieldPanel.add(fieldTypeLabel);
        
        JRadioButton clearTextButton = new JRadioButton("Default");
        clearTextButton.addActionListener(
            new ActionListener()
            {
                public void actionPerformed (ActionEvent ae)
                {
                    _wireFormatDatatype = TibrvMsg.DEFAULT;
                }
            }
        );
        clearTextButton.setToolTipText("eManager, Cornerstone default");
        tibcoFieldPanel.add(clearTextButton);
        clearTextButton.setSelected(true);
        
        JRadioButton opaqueTextButton = new JRadioButton("Opaque");
        opaqueTextButton.addActionListener(
            new ActionListener()
            {
                public void actionPerformed( ActionEvent ae)
                {
                    _wireFormatDatatype = TibrvMsg.OPAQUE;
                    m_tibcoFieldText.setSelectedItem("AD_DATA");
                }
            }
        );
        opaqueTextButton.setToolTipText("Cornerstone C-NDR");
        tibcoFieldPanel.add(opaqueTextButton);
        ButtonGroup group = new ButtonGroup();
        group.add(clearTextButton);
        group.add(opaqueTextButton);
        
        JLabel tibcoFieldLabel = new JLabel("Field name: ");
        tibcoFieldLabel.setToolTipText("eManager: DATA, Cornerstone C-NDR: AD_DATA, Cornerstone other: CS_DATA");        
        tibcoFieldPanel.add(tibcoFieldLabel);
        
        m_fieldModel = new DefaultComboBoxModel(FIELD_NAMES);
        m_tibcoFieldText = new JComboBox(m_fieldModel);
        m_tibcoFieldText.setEditable(true);
        tibcoFieldPanel.add(m_tibcoFieldText);
        
        xmlInputPanel.add(tibcoFieldPanel);
        
        
        // row 0, 4: TIBCO parameters 2
        JPanel tibcoParameterPanel2 = new JPanel();
        JLabel  serviceLabel = new JLabel("Service:");
        tibcoParameterPanel2.add( serviceLabel );
        _sendServiceModel = new DefaultComboBoxModel( SERVICES );
        m_serviceText = new JComboBox(_sendServiceModel);
        m_serviceText.setEditable(true);
        tibcoParameterPanel2.add( m_serviceText );
        
        JLabel  networkLabel = new JLabel( "Network:");
        tibcoParameterPanel2.add( networkLabel);
        _sendNetworkModel = new DefaultComboBoxModel( NETWORKS );
        m_networkText = new JComboBox(_sendNetworkModel);
        m_networkText.setEditable(true);
        tibcoParameterPanel2.add(m_networkText);
        JLabel daemonLabel = new JLabel("Daemon:");
        tibcoParameterPanel2.add(daemonLabel);
        _sendDaemonModel = new DefaultComboBoxModel( DAEMONS );
        m_daemonText = new JComboBox(_sendDaemonModel);
        m_daemonText.setEditable(true);
        tibcoParameterPanel2.add(m_daemonText);
        xmlInputPanel.add(tibcoParameterPanel2);
        
        // row 0, 5: controls
        JPanel xmlControlPanel = new JPanel();
        //xmlControlPanel.setLayout( new BorderLayout());
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ResetStateAction());
        xmlControlPanel.add(clearButton);
        JButton saveButton = new JButton("Save Message");
        saveButton.addActionListener( new SaveSendMessageAction());
        xmlControlPanel.add(saveButton);        
        JButton sendButton = new JButton("Send");
        sendButton.setToolTipText("Send message [TibrvTransport.send()]");
        sendButton.addActionListener( new SendMessageAction());
        xmlControlPanel.add(sendButton);
        JButton sendRequestButton = new JButton("Send Request");
        sendRequestButton.setToolTipText("Send message and wait for reply.  [TibrvTransport.sendRequest()]");
        sendRequestButton.addActionListener( new SendRequestAction());
        xmlControlPanel.add(sendRequestButton);

        xmlInputPanel.add(xmlControlPanel);
        
        xmlPanel.add(xmlInputPanel);
        
        return xmlPanel;
    }
    
    private void createOutputPanel(ArrayList values)
    {
        
        JTabbedPane outputPane = new JTabbedPane();
        m_outputPanel.add(outputPane);
        
        JPanel xmlOutputPanel = createXmlOutputPanel(values);
        outputPane.addTab("XML Message Input", xmlOutputPanel);
        
        JPanel messagePanel = createMessagePanel();
        outputPane.addTab("Message Viewer", messagePanel);
        
        JPanel stringOutputPanel = createMiscellaneousOutputPanel();
        outputPane.addTab("Additional Output", stringOutputPanel);

    }
    
    /**
     *  Input:
     *      LinkedList of Value2: NV pair of (Element tag, Text child) from the XML message document
     *      
     *  Output:
     *      xmlOutputPanel
     */
    private JPanel createXmlOutputPanel(ArrayList values)
    {
        int numElements = values.size();             
        
        JPanel xmlOutputPanel = new JPanel();
        xmlOutputPanel.setLayout( new GridLayout( numElements, 2));
        
        for (int i = 0; i < numElements; i++)
        {
            Value2 value = (Value2) values.get(i);
            debug("cXOP: value = " + value.toString());
            m_elementTag[i] = new JLabel(value.m_name);
            debug( m_elementTag[i].getText());            
            xmlOutputPanel.add( m_elementTag[i]);
            
            m_elementText[i] = new JTextField(value.m_content);
            xmlOutputPanel.add( m_elementText[i]);
        }
        
        return xmlOutputPanel;
    }
    /**
     * 
     *
     *      row 0: 
     *      row 1: output text area
     */
    private JPanel createMessagePanel()
    {
        m_messagePanel = new JPanel();
        
        m_outputTextArea = new JTextArea(20, 60); //WIDTH);
        m_outputScrollPane = new JScrollPane(m_outputTextArea);
        m_messagePanel.add(m_outputScrollPane);

        return m_messagePanel;
    }
    
    private JPanel createMiscellaneousOutputPanel()
    {
        m_stringOutputPanel = new JPanel();
        
        JLabel stringifiedXMLLabel = new JLabel("Stringified XML: ");
        m_stringOutputPanel.add( stringifiedXMLLabel);
        m_stringifiedXmlText = new JTextField(64);
        m_stringOutputPanel.add(m_stringifiedXmlText);
        return m_stringOutputPanel;
    }
    
    /**
     *  receiverInputPanel( 2, 1)
     *      row 0: xmlInputPanel
     *          xmlInputPanel(3, 1)
     *            
     *              row 0: XML label, XML file input
     *              row 1: TIBCO parameters: Send subject, Reply subject, field name
     *              row 2: TIBCO parameters: Service, Network, Daemon
     *              row 3: controls: "Clear" button
     *      row 1: xmlOutputPanel
     *              
     */
    private JPanel createReceiverInputParameterPanel()
    {  
        JPanel receiverInputPanel = new JPanel();
        
        JPanel xmlInputPanel = new JPanel();
        xmlInputPanel.setLayout( new GridLayout( 3, 1));


        
        // row 0: TIBCO parameters 1
        JPanel tibcoParameterPanel = new JPanel();
        //tibcoParameterPanel.setLayout( new BorderLayout());
        JLabel tibcoParameterLabel = new JLabel("TIBCO: ");
        tibcoParameterPanel.add(tibcoParameterLabel);   //, BorderLayout.WEST);
        JLabel tibcoSendSubjectLabel = new JLabel("Receive Subject:");
        tibcoParameterPanel.add( tibcoSendSubjectLabel);

        m_receiveSubjectModel = new DefaultComboBoxModel(RECEIVE_SUBJECT_NAMES);
        m_tibcoReceiverSubjectText = new JComboBox(m_receiveSubjectModel); //JTextField(10);
        m_tibcoReceiverSubjectText.setEditable(true);
        tibcoParameterPanel.add(m_tibcoReceiverSubjectText);


        
        xmlInputPanel.add(tibcoParameterPanel);
        
        // row 1: TIBCO parameters 2
        JPanel tibcoParameterPanel2 = new JPanel();
        JLabel  serviceLabel = new JLabel("Service:");
        tibcoParameterPanel2.add( serviceLabel );
        m_receiverServiceText = new JComboBox(_sendServiceModel);
        m_receiverServiceText.setEditable(true);
        tibcoParameterPanel2.add( m_receiverServiceText );
        
        JLabel  networkLabel = new JLabel( "Network:");
        tibcoParameterPanel2.add( networkLabel);
        m_receiverNetworkText = new JComboBox(_sendNetworkModel);
        m_receiverNetworkText.setEditable(true);
        tibcoParameterPanel2.add(m_receiverNetworkText);
        JLabel daemonLabel = new JLabel("Daemon:");
        tibcoParameterPanel2.add(daemonLabel);
        m_receiverDaemonText = new JComboBox(_sendDaemonModel);
        m_receiverDaemonText.setEditable(true);
        tibcoParameterPanel2.add(m_receiverDaemonText);
        xmlInputPanel.add(tibcoParameterPanel2);
        
        // row 2: controls
        JPanel xmlControlPanel = new JPanel();
        //xmlControlPanel.setLayout( new BorderLayout());
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ResetStateAction());
        xmlControlPanel.add(clearButton);
        JButton sendButton = new JButton("Receive messages");
        sendButton.addActionListener( new ReceiveMessageAction());
        xmlControlPanel.add(sendButton);
        xmlInputPanel.add(xmlControlPanel);
        
        receiverInputPanel.add(xmlInputPanel);
        
        return receiverInputPanel;
    }
    
    private JPanel createReceiverOutputParameterPanel()
    {
        JPanel outputPanel = new JPanel();
        m_receiverOutputTextArea = new JTextArea(30, 60);
        JScrollPane receiverOutputScrollPane = new JScrollPane(m_receiverOutputTextArea);
        outputPanel.add(receiverOutputScrollPane);
        return outputPanel;
    }
    /**
     *  INput:
     *      
     *      m_values
     *      m_elementTag[]
     *      m_elementText[]
     *  Output:
     *      ArrayList of Value2, from the GUI of the XML document
     */    
    private ArrayList getInputValues()
    {
        ArrayList valueList = new ArrayList();
        for (int i = 0; i < m_values.size(); i++)
        {
            String name = m_elementTag[i].getText();
            String value = m_elementText[i].getText();
            Value2 currentValue = new Value2(name, value);
            valueList.add(currentValue);
        }
        return valueList;
    }
    
    private Document updateDocument( Document inputDoc, ArrayList valueList)
    {
        Document outputDoc = inputDoc;
        XMLUtilities xmlUtils = new XMLUtilities();
        
        for (int i = 0; i < valueList.size(); i++)
        {
            Value2 currentValue = (Value2) valueList.get(i);
            xmlUtils.setElementText( outputDoc, currentValue);
        }
        return outputDoc;
    }
    
    /**
     *  INput:
     *      @param input XML document
     *      @param m_values
     *      @param m_elementTag[]
     *      @param m_elementText[]
     *
     *  Output:
     *      outputDoc - contains new Text child nodes whose values are specified in m_elementText[]
     */
    private Document updateDocument( Document inputDoc)
    {
        Document outputDoc = inputDoc;
        XMLUtilities xmlUtilities = new XMLUtilities();
                
        for (int i = 0; i < m_values.size(); i++)   // m_elementTag[] is derived from and same size as m_values[]
        {
            String name = m_elementTag[i].getText();
            String value = m_elementText[i].getText();
            debug("SMA: name = " + name  + ", value = " + value);
            Value2 currentValue = new Value2(name, value);
            xmlUtilities.setElementText( outputDoc, currentValue );
        }
                
        return outputDoc;
    }
    
    /**
     *
     * @param m_xmlStringWriter
     * Output:
     *      m_stringifiedXml
     *
     */
    /*
    private String saveNewMessageValues()
    {
        // Read the XML message from m_xmlFileText
        String stringifiedXml = new String( m_xmlStringWriter.getBuffer().toString());  
                
        XMLUtilities xmlUtilities = new XMLUtilities();
        Document inputDoc = xmlUtilities.stringToDocument( stringifiedXml, false);
                
        // Update the XML document with the new values entered in from the GUI
        ArrayList newValues = getInputValues();
        Document outputDoc = updateDocument( inputDoc, newValues);
                
        String updatedStringifiedXml = xmlUtilities.documentToString( outputDoc );
        m_stringifiedXml = updatedStringifiedXml;        
        return updatedStringifiedXml;
    }
     */
    /**
     *  Handles Documents with multiple Elements that have the same tag
     *  1/14/2004
     */
    private String saveNewMessageValues()
    {
        // Read the XML message from m_xmlFileText
        String stringifiedXml = new String( m_xmlStringWriter.getBuffer().toString());  
                
        XMLUtilities xmlUtilities = new XMLUtilities();
        Document inputDoc = xmlUtilities.stringToDocument( stringifiedXml, false);
        Document outputDoc = inputDoc;
                
        // Update the XML document with the new values entered in from the GUI
        ArrayList newValues = getInputValues();
        
        xmlUtilities._globalCount = -1;
        Element rootElement = inputDoc.getDocumentElement();
        xmlUtilities.setAllElementText( outputDoc, rootElement, newValues);
                
        String updatedStringifiedXml = xmlUtilities.documentToString( outputDoc );
        m_stringifiedXml = updatedStringifiedXml;        
        return updatedStringifiedXml;        
    }

    private void  saveNewTibcoParameterValues()
    {
        String newSubject = (String) m_tibcoSendSubjectText.getSelectedItem();
                m_sendSubjectModel.insertElementAt(newSubject, 0);
                String newService = (String) m_serviceText.getSelectedItem();
                if (newService != null)
                    _sendServiceModel.insertElementAt(newService, 0);
                String newNetwork = (String) m_networkText.getSelectedItem();
                if (newNetwork != null)
                    _sendNetworkModel.insertElementAt(newNetwork, 0);
                String newDaemon = (String) m_daemonText.getSelectedItem();
                if (newDaemon != null)
                    _sendDaemonModel.insertElementAt(newDaemon, 0);    
    }
    
    
    public void debug (String output)
    {
        //if (m_debug.equals("TRUE"))            
            System.out.println(output);
    }    
    
    JPanel                  m_mainPanel;
    JPanel                  m_senderPanel;      // tab 0
    JPanel                  m_receiverPanel;    // tab 1
    
    // m_senderPanel
    JPanel                  m_xmlPanel;
    JPanel                  m_outputPanel;
    JTabbedPane             m_outputPane;

    JLabel                  m_elementTag[]  = new JLabel[MAX_ELEMENTS];
    JTextField              m_elementText[] = new JTextField[MAX_ELEMENTS];
    
    ArrayList               m_values;

    String                  m_messageSourceDirectory;
    String                  m_messageSaveDirectory;
    String                  m_outputDirectory;
    
    // panel 0
    DefaultComboBoxModel    m_xmlFileModel;
    JComboBox               m_xmlFileText;
    
    DefaultComboBoxModel    m_sendSubjectModel;
    JComboBox               m_tibcoSendSubjectText;
    JTextField              m_tibcoReplySubjectText;
    short                   _wireFormatDatatype;
    DefaultComboBoxModel    m_fieldModel;
    JComboBox               m_tibcoFieldText;
    DefaultComboBoxModel    _sendServiceModel;
    JComboBox               m_serviceText;
    DefaultComboBoxModel    _sendNetworkModel;
    JComboBox               m_networkText;
    DefaultComboBoxModel    _sendDaemonModel;
    JComboBox               m_daemonText;
    
    StringWriter            m_xmlStringWriter;
    String                  m_stringifiedXml;
    
    JPanel                  m_messagePanel;
    JTextArea               m_outputTextArea;
    JScrollPane             m_outputScrollPane;
    
    // m_receiverPanel
    // panel 1
    DefaultComboBoxModel    m_receiveSubjectModel;
    JComboBox               m_tibcoReceiverSubjectText;
    DefaultComboBoxModel    _receiverServiceModel;
    JComboBox               m_receiverServiceText;
    DefaultComboBoxModel    _receiverNetworkModel;    
    JComboBox               m_receiverNetworkText;
    DefaultComboBoxModel    _receiverDaemonModel;    
    JComboBox               m_receiverDaemonText;
    JTextArea               m_receiverOutputTextArea;
    
    // Panel 2
    JPanel                  m_stringOutputPanel;
    JTextField              m_stringifiedXmlText;
    
    JDialog                 m_aboutDialog;
    
    
    /**
     *  Sets m_xmlFileText
     *      m_messageSourceDirectory
     */
    private class SetMessageSourceAction implements ActionListener
    {
        public void actionPerformed (ActionEvent ae)
        {
            JFileChooser sourceDirChooser = new JFileChooser(m_messageSourceDirectory);
            sourceDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retValue = sourceDirChooser.showOpenDialog(null);
           
            if (retValue == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    m_messageSourceDirectory = sourceDirChooser.getSelectedFile().getCanonicalPath();
                    
                    //m_xmlFileText.setText(m_messageSourceDirectory);
                    m_xmlFileModel.insertElementAt( m_messageSourceDirectory, 0);
                    m_xmlFileText.setSelectedIndex(0);
                    
                }
                catch( IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
              
        }
    }
    
    
    /**
     *  Sets 
     *      m_messageSaveDirectory
     */
    private class SetSavedMessageDirectoryAction implements ActionListener
    {
        public void actionPerformed (ActionEvent ae)
        {
            JFileChooser sourceDirChooser = new JFileChooser(m_messageSaveDirectory);
            sourceDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retValue = sourceDirChooser.showOpenDialog(null);
           
            if (retValue == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    m_messageSaveDirectory = sourceDirChooser.getSelectedFile().getCanonicalPath();
                                       
                }
                catch( IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
              
        }
    }
        
    /**
     * @arg: m_messageSourceDirectory
     *
     */
    private class BrowseXMLMessageAction implements ActionListener
    {
        public void actionPerformed( ActionEvent ae)
        {
            JFileChooser xmlChooser = new JFileChooser(m_messageSourceDirectory);
            int retValue = xmlChooser.showOpenDialog(null);
            String selectedXml = "";
            if (retValue == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    selectedXml = xmlChooser.getSelectedFile().getCanonicalPath();
                    
                    //m_xmlFileText.setText(selectedXml);
                    m_xmlFileModel.insertElementAt( selectedXml, 0);
                    m_xmlFileText.setSelectedIndex(0);
                    
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
    }
    
    /**
     *  Input:
     *      m_xmlFileText
     *
     *
     *  Output:
     *      m_xmlStringWriter
     *      m_outputTextArea
     *      m_values
     *
     *  Parse m_xmlFileText to get list of Element tags.
     *  Create a GUI with corresponding JTextFields for each Element
     *  Create button.  ActionHandler will construct a LinkedList of parameter tuples
     *
     */
    private class ParseMessageAction implements ActionListener
    {
        public void actionPerformed( ActionEvent ae )
        {
            try
            {
                String xmlFileName = (String) m_xmlFileText.getSelectedItem(); //getText().trim();
                File xmlFile = new File( xmlFileName );
                if (xmlFile.isFile())
                {
                    FileReader inputFileReader = new FileReader(xmlFile);
                    m_xmlStringWriter = new StringWriter();
                    int c = -1;
                    while ( (c = inputFileReader.read()) != -1)
                    {
                        m_xmlStringWriter.write(c);
                    }
                }
                else
                    System.err.println( xmlFileName + " is not a file");
                
                debug(m_xmlStringWriter.getBuffer().toString());
             
                LinkedList elements = new LinkedList();
                m_values = getElements( xmlFile );
                
                createOutputPanel(m_values);
                String stringifiedXml = m_xmlStringWriter.getBuffer().toString();
                m_outputTextArea.append(stringifiedXml);
                m_stringifiedXmlText.setText(stringifiedXml);
                
                // pre order traverse
                
         
                XMLUtilities xmlUtils = new XMLUtilities();
                Document _document = xmlUtils.stringToDocument(m_xmlStringWriter.getBuffer().toString(), false  );

                if (_document != null)
                {
                    Element rootElement = _document.getDocumentElement();
                
                    int depth = 0;
                    int count = 0;
                    xmlUtils.preorderTraverseDocument( rootElement, depth, count );
                }
                else
                    System.err.println("PMA: could not get document element");

            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
             
        }
        
        /**
         *  Input:
         *      xmlFile.  
         *  Output:
         *      values - ArrayList of Value2.  NV pair of (Element tag, Text child)
         */
        public ArrayList getElements( File xmlFile)
        {
            LinkedList elements = new LinkedList();
            ArrayList  values = new ArrayList();
            if (xmlFile != null)
            {
                try
                {
                    ElementHandler elementHandler = new ElementHandler();
                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    saxParserFactory.setNamespaceAware(true);
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    saxParser.parse( xmlFile, elementHandler);
                    elements = elementHandler.m_elements;
                    values = elementHandler.m_values;
                    
                    for (int i = 0; i < values.size(); i++)
                    {
                        Value2 value = (Value2) values.get(i);
                        debug("values[" + i + "]= " + value );
                    }
                }
                catch (ParserConfigurationException pce)
                {
                    pce.printStackTrace();
                }
                catch ( IOException ioe)
                {
                    ioe.printStackTrace();
                }
                catch ( SAXException se)
                {
                    se.printStackTrace();
                }
            }
            else
                System.err.println("gE: input file is null!");
            
            return values;
        }
    }
    
    private class ResetStateAction implements ActionListener
    {
        public void actionPerformed (ActionEvent ae)
        {
            m_outputPanel.removeAll();
            //m_tibcoSendSubjectText.setText("");
            m_tibcoSendSubjectText.setSelectedIndex(0);
            //m_tibcoReplySubjectText.setText("");
            m_outputTextArea.setText("");
            
            
        }
    }
    
    /**
     * Input:
     *      m_tibcoSubjectText - message subject
     *      m_serviceText
     *      m_networkText
     *      m_daemonText
     *      m_tibrvRvdTransport
     *      m_xmlStringWriter - message contents
     *      m_tibcoFieldText
     *
     *      m_elementText[]
     *      m_elementTag[]
     *      m_values
     *      
     *      TODO: LinkedList of tuples: < parameter name, parameter type, parameter value >
     *      TODO: create DOM of the input XML file and add the list of parameter values as child Text Elements of corresponding Element
     *
     *  Output:
     *      m_outputTextArea
     *      m_stringifiedXml
     *
     *  Restriction: Only handles single occurence of Element tags
     */
    private class SendMessageAction implements ActionListener
    {        
        
        public void actionPerformed( ActionEvent ae )
        {
            // Open TIBCO RV transport
            try
            {
                
                
                String service = (String) m_serviceText.getSelectedItem();                
                String network = (String) m_networkText.getSelectedItem();
                String daemon = (String) m_daemonText.getSelectedItem();
                
                TIBCOTransport tibcoTransport = new TIBCOTransport(service, network, daemon);
                TibrvRvdTransport transport = tibcoTransport.m_tibrvRvdTransport;

                // Create outgoing TIBCO RV message
                TibrvMsg    message = new TibrvMsg();

                // subject
                String subject = (String) m_tibcoSendSubjectText.getSelectedItem(); //getText().trim();
                message.setSendSubject(subject);
                debug("subject = " + subject );
                
                // body
                // Read the XML message from m_xmlFileText
                String updatedStringifiedXml = saveNewMessageValues();
                
                m_outputTextArea.append(updatedStringifiedXml);
                
                debug(updatedStringifiedXml);
                String fieldId = (String) m_tibcoFieldText.getSelectedItem();
                debug(fieldId);
                if (_wireFormatDatatype == TibrvMsg.OPAQUE)
                {
                    message.update(fieldId, updatedStringifiedXml.getBytes(), TibrvMsg.OPAQUE);
                }
                else if (_wireFormatDatatype == TibrvMsg.DEFAULT)
                {
                    message.update(fieldId, updatedStringifiedXml);
                }
                else
                    System.err.println("Unsupported wire format datatype");
                
                // send
                if (transport != null)
                    transport.send(message); 
                else
                    System.err.println("SMA: transport == null");
                               
                // Update JComboBox with user entered info
                saveNewTibcoParameterValues();

            }
            catch (TibrvException te)
            {
                te.printStackTrace();
            }            
        }
    }

    private class SendRequestAction implements ActionListener
    {        
        
        public void actionPerformed( ActionEvent ae )
        {
            // Open TIBCO RV transport
            try
            {
                String service = (String) m_serviceText.getSelectedItem();
                String network = (String) m_networkText.getSelectedItem();
                String daemon = (String) m_daemonText.getSelectedItem();
                
                TIBCOTransport tibcoTransport = new TIBCOTransport(service, network, daemon);
                TibrvRvdTransport transport = tibcoTransport.m_tibrvRvdTransport;

                // Create outgoing TIBCO RV message
                TibrvMsg    message = new TibrvMsg();

                // subject
                String subject = (String) m_tibcoSendSubjectText.getSelectedItem(); //getText().trim();
                message.setSendSubject(subject);
                debug("subject = " + subject );
                
                String reply = m_tibcoReplySubjectText.getText().trim();
                debug("reply = " + reply);
                message.setReplySubject(reply);

                // body
                // Read the XML message from m_xmlFileText
                String updatedStringifiedXml = saveNewMessageValues();
                
                m_outputTextArea.append(updatedStringifiedXml);
                
                debug(updatedStringifiedXml);

                if (_wireFormatDatatype == TibrvMsg.OPAQUE)
                {
                    message.update((String) m_tibcoFieldText.getSelectedItem(), updatedStringifiedXml.getBytes(), TibrvMsg.OPAQUE);
                }
                else if (_wireFormatDatatype == TibrvMsg.DEFAULT)
                {
                    message.update((String) m_tibcoFieldText.getSelectedItem(), updatedStringifiedXml);
                }
                else
                    System.err.println("Unsupported wire format datatype");                

                
                // sendRequest 
                
                TibrvMsg replyMessage = null;
                replyMessage =  transport.sendRequest(  message, 5.0);
                if (replyMessage == null)
                {
                    m_outputTextArea.append("replyMessage == null");
                    System.err.println("SMA: replyMessage == null");
                }
                else
                {
                    String replySubject = replyMessage.getReplySubject();
                    debug("SMA: replySubject = " + replySubject);
                    m_outputTextArea.append("replySubject = " + replySubject);
                    // get data contents of reply
                }
                                
                // Update JComboBox with user entered subject
                saveNewTibcoParameterValues();
         
            }
            catch (TibrvException te)
            {
                te.printStackTrace();
            }            
        }
    }
    
    
    public class TIBCOTransport
    {
        public TIBCOTransport(String service,
                                String network,
                                String daemon)
            throws TibrvException
        {
            m_service = null;
            m_network = null;
            m_daemon = null;
            
            Tibrv.open(Tibrv.IMPL_NATIVE);
            try
            {
                m_service = service; //m_serviceText.getText().trim();
                m_network = network; // m_networkText.getText().trim();
                m_daemon = daemon; //m_daemonText.getText().trim();
                m_tibrvRvdTransport = new TibrvRvdTransport( m_service, m_network, m_daemon);
            }
            catch (TibrvException te)
            {
                te.printStackTrace();
            }
        }
        
        public TibrvRvdTransport getTransport()
        {
            return m_tibrvRvdTransport;
            
            
        }
        
        String              m_service;
        String              m_network;
        String              m_daemon;
        TibrvRvdTransport   m_tibrvRvdTransport;        
    }
    
    /**
     * Input
     *      @param m_stringifiedXml
     *
     *  saveFileName should not have ".xml" extension
     */
    private class SaveSendMessageAction implements ActionListener
    {
        public void actionPerformed( ActionEvent ae)
        {
            JFileChooser saveChooser = new JFileChooser(m_messageSaveDirectory);
            int retValue = saveChooser.showSaveDialog(null);
            String saveFileName = "";
            if (retValue == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    saveFileName = saveChooser.getSelectedFile().getCanonicalPath();
                    
                    debug("Save " + saveFileName);
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
                
                String updatedStringifiedXml = saveNewMessageValues();
                m_stringifiedXmlText.setText(updatedStringifiedXml);
                m_outputTextArea.append(updatedStringifiedXml);
                if (m_stringifiedXml != null)
                {
                    XMLUtilities xmlUtilities = new XMLUtilities();
                    Document doc = xmlUtilities.stringToDocument(updatedStringifiedXml, false);
                    xmlUtilities.documentToFile(doc, saveFileName);
                }
            }
        }
    }
    
    
    /**
     * Display messages in m_receiverOutputTextArea
     *  Updates m_receiveSubjectModel with the newly entered subject
     */
    
    public class ReceiveMessageAction implements ActionListener
    {
        public void actionPerformed( ActionEvent ae)
        {
            String service = (String) m_receiverServiceText.getSelectedItem();
            String network = (String) m_receiverNetworkText.getSelectedItem();
            String daemon = (String) m_receiverDaemonText.getSelectedItem();
            String subject = (String) m_tibcoReceiverSubjectText.getSelectedItem(); //getText().trim();
 
            
            TIBCOReceiver tibcoReceiver = new TIBCOReceiver( service, network, daemon, subject, m_receiverOutputTextArea);
            tibcoReceiver.start();
            
            String newSubject = (String) m_tibcoReceiverSubjectText.getSelectedItem();
            m_receiveSubjectModel.insertElementAt(newSubject, 0);
        }
    }
    
    public class PreorderTraverseAction implements ActionListener
    {
        public PreorderTraverseAction (Document doc)
        {
            _document = doc;
        }
        
        public PreorderTraverseAction( String stringifiedXmlDoc )
        {
            XMLUtilities xmlUtils = new XMLUtilities();
            _document = xmlUtils.stringToDocument( stringifiedXmlDoc );
        }
        
        public void actionPerformed( ActionEvent ae)
        {
            Element rootElement = _document.getDocumentElement();
            XMLUtilities xmlUtils = new XMLUtilities();
            xmlUtils.preorderTraverseDocument( rootElement, 0, 0 );
        }
        
        Document _document;
    }
    
    class AboutDialog extends JDialog
    {
        public AboutDialog( JFrame owner)
        {
            super( owner, "About NMTG TIBCO RV client", true);
            Container contentPane = getContentPane();
            
            contentPane.add( new JLabel(
                "<HTML>Application for sending and receiving messages via TIBCO Rendezvous 7.1<HR>"
                + "Version 0.9<BR>"
                + "1/15/2004<BR>"
                + "Roger Huang (rchuang)</HTML>"),
                BorderLayout.CENTER);
            
            JButton okButton = new JButton("Ok");
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
            setSize(250, 150);
        }
    }
}
