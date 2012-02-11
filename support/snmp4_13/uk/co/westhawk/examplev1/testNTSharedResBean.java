// NAME
//      $RCSfile: testNTSharedResBean.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.2 $
// CREATED
//      $Date: 2002/10/22 16:38:07 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//


/*
 * Copyright (C) 1998, 1999, 2000, 2001, 2002 by Westhawk Ltd (www.westhawk.co.uk)
 *
 * Permission to use, copy, modify, and distribute this software
 * for any purpose and without fee is hereby granted, provided
 * that the above copyright notices appear in all copies and that
 * both the copyright notice and this permission notice appear in
 * supporting documentation.
 * This software is provided "as is" without express or implied
 * warranty.
 * author <a href="mailto:snmp@westhawk.co.uk">Tim Panton</a>
 */
 
package uk.co.westhawk.examplev1;

import java.awt.*; 
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

import uk.co.westhawk.snmp.beans.*;
import uk.co.westhawk.snmp.stack.*;
import java.beans.*;

/**
 * <p>
 * The class testNTSharedResBean demonstrates the use of some of the beans
 * in the uk.co.westhawk.snmp.beans packages. It shows a UI in which the
 * user can configure the bean properties.
 * </p>
 *
 * <p>
 * The user can configure the host name, the port number, the community
 * name and the update interval. When the action button is pressed
 * NTSharedResBean is activated. Via the PropertyChangeEvent it
 * will provide the UI with the list of current resources.
 * </p>
 *
 * <p>
 * The "Try it" button activates the IsHostReachableBean, who will probe
 * the configured host and signals the UI since when the host was up.
 * </p>
 *
 * <p>
 * The user can select one resource. The class OneNTSharedRes will
 * provide the UI with a dialog with more information about the
 * resource.
 * </p>
 *
 * @see OneNTSharedRes
 * @see uk.co.westhawk.snmp.beans.NTSharedResBean
 * @see uk.co.westhawk.snmp.beans.IsHostReachableBean
 *
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 1.2 $ $Date: 2002/10/22 16:38:07 $
 */
public class testNTSharedResBean extends JComponent 
        implements ActionListener, PropertyChangeListener,
        WindowListener
{
    private static final String     version_id =
        "@(#)$Id: testNTSharedResBean.java,v 1.2 2002/10/22 16:38:07 birgit Exp $ Copyright Westhawk Ltd";

    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel label1 = new JLabel(" ");
    JLabel label2 = new JLabel(" ");
    JLabel label3 = new JLabel(" ");
    JTextField hostText = new JTextField();
    JTextField portText = new JTextField();
    JLabel resourceLabel = new JLabel(" ");
    java.awt.List resourceList = new java.awt.List();
    uk.co.westhawk.snmp.beans.NTSharedResBean resourceBean = new uk.co.westhawk.snmp.beans.NTSharedResBean();
    JLabel label5 = new JLabel(" ");
    JTextField communityText = new JTextField();
    JButton tryButton = new JButton();
    JLabel messageLabel = new JLabel(" ");
    JButton showButton = new JButton();
    JLabel label7 = new JLabel(" ");
    JTextField intervalText = new JTextField();
    uk.co.westhawk.snmp.beans.IsHostReachableBean reachableBean = new uk.co.westhawk.snmp.beans.IsHostReachableBean();
    JButton actionButton = new JButton();

//Get a parameter value
    
    //Construct the application
    
    public testNTSharedResBean()
    {
    }
//Initialize the application
    
    public void init()
    {
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//Component initialization
    
    private void jbInit() throws Exception
    {
        label1.setText(" NT Server ");
        label2.setText(" Port ");
        label3.setText(" # Resources ");
        label5.setText(" Community ");
        label7.setText(" Interval ");
        hostText.setBackground(Color.white);
        hostText.setText("iffish");
        hostText.addActionListener(this);
        portText.setBackground(Color.white);
        portText.setText("" + SnmpContextBasisFace.DEFAULT_PORT);
        portText.addActionListener(this);
        resourceList.setBackground(Color.yellow);
        resourceBean.setPort(SnmpContextBasisFace.DEFAULT_PORT);
        resourceBean.setHost("iffish.westhawk.co.uk");
        resourceList.addActionListener(this);
        communityText.setBackground(Color.white);
        communityText.setText("public");
        communityText.addActionListener(this);
        tryButton.setText("Try connection");
        tryButton.addActionListener(this);
        resourceLabel.setBackground(Color.white);
        messageLabel.setBackground(Color.white);
        resourceLabel.setOpaque(true);
        messageLabel.setOpaque(true);
        showButton.setText("Show Resource");
        showButton.addActionListener(this);
        intervalText.setBackground(Color.white);
        intervalText.setText("30000");
        actionButton.setText("Action");
        actionButton.addActionListener(this);
        intervalText.addActionListener(this);
        reachableBean.addPropertyChangeListener(this);
        resourceBean.addPropertyChangeListener(this);
        this.setLayout(gridBagLayout1);
        this.setSize(400,300);
        
        this.add(label1, 
            getGridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(label2, 
            getGridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(label5, 
            getGridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(label7, 
            getGridBagConstraints2(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(tryButton, 
            getGridBagConstraints2(0, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
            new Insets(10, 10, 10, 10), 0, 0));
        this.add(label3, 
            getGridBagConstraints2(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(resourceList, 
            getGridBagConstraints2(0, 6, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(showButton, 
            getGridBagConstraints2(0, 7, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, 
            new Insets(10, 10, 10, 10), 0, 0));
        this.add(messageLabel, 
            getGridBagConstraints2(0, 8, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, 10, 0), 0, 0));

        this.add(hostText, 
            getGridBagConstraints2(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(portText, 
            getGridBagConstraints2(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(resourceLabel, 
            getGridBagConstraints2(1, 5, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(communityText, 
            getGridBagConstraints2(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(intervalText, 
            getGridBagConstraints2(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
            new Insets(0, 0, 0, 0), 0, 0));
        this.add(actionButton, 
            getGridBagConstraints2(1, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
            new Insets(10, 10, 10, 10), 0, 0));
    }

//Main method

    public static void main(String[] args)
    {
        testNTSharedResBean application = new testNTSharedResBean();
        application.init();

        JFrame frame = new JFrame();
        frame.setTitle(application.getClass().getName());
        frame.getContentPane().add(application, BorderLayout.CENTER);
        frame.setSize(400,320);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
        frame.setVisible(true);
        frame.addWindowListener(application);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if (src == hostText)
        {
            hostText_actionPerformed(e);
        }
        else if (src == portText)
        {
            portText_actionPerformed(e);
        }
        else if (src == communityText)
        {
            communityText_actionPerformed(e);
        }
        else if (src == resourceList)
        {
            resourceList_actionPerformed(e);
        }
        else if (src == tryButton)
        {
            tryButton_actionPerformed(e);
        }
        else if (src == showButton)
        {
            showButton_actionPerformed(e);
        }
        else if (src == actionButton)
        {
            actionButton_actionPerformed(e);
        }
        else if (src == intervalText)
        {
            intervalText_actionPerformed(e);
        }
    }

    public void propertyChange(PropertyChangeEvent e)
    {
        Object src = e.getSource();
        if (src == reachableBean)
        {
            reachableBean_propertyChange(e);
        }
        else if (src == resourceBean)
        {
            resourceBean_propertyChange(e);
        }
    }

    void hostText_actionPerformed(ActionEvent e)
    {
        resourceBean.setHost(hostText.getText());
    }

    void portText_actionPerformed(ActionEvent e)
    {
        resourceBean.setPort(portText.getText());
    }

    void communityText_actionPerformed(ActionEvent e)
    {
        resourceBean.setCommunityName(communityText.getText());
    }

    void intervalText_actionPerformed(ActionEvent e)
    {
        resourceBean.setUpdateInterval(intervalText.getText());
    }

    void tryButton_actionPerformed(ActionEvent e)
    {
        messageLabel.setText(" ");
        reachableBean.setHost(hostText.getText());
        reachableBean.setPort(portText.getText());
        reachableBean.setCommunityName(communityText.getText());

        try
        {
            reachableBean.action();
        }
        catch (java.io.IOException exc)
        {
            messageLabel.setText("IOException " + exc.getMessage());
        }
        catch (uk.co.westhawk.snmp.stack.PduException exc)
        {
            messageLabel.setText("PduException " + exc.getMessage());
        }
    }

    void resourceBean_propertyChange(PropertyChangeEvent e)
    {
        Enumeration names = resourceBean.getNames();
        int nr = resourceBean.getCount();

        resourceLabel.setText(String.valueOf(nr));
        if (resourceList.getItemCount() > 0)
        {
            resourceList.removeAll();
        }

        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            resourceList.add(name);
        }
    }

    void reachableBean_propertyChange(PropertyChangeEvent e)
    {
        messageLabel.setText(reachableBean.getMessage());
    }

    private void showOneNTSharedRes()
    {
        String itemStr = resourceList.getSelectedItem();
        if (itemStr != null && itemStr.length() > 0)
        {
            String index = resourceBean.getIndex(itemStr);

            OneNTSharedRes oneNTResource = new
            OneNTSharedRes(testNcdPerfDataBean.getFrame(this), 
                hostText.getText(), portText.getText(), 
                communityText.getText(), index, intervalText.getText());
        }
    }

    void resourceList_actionPerformed(ActionEvent e)
    {
        showOneNTSharedRes();
    }

    void showButton_actionPerformed(ActionEvent e)
    {
        showOneNTSharedRes();
    }

    void actionButton_actionPerformed(ActionEvent e)
    {
        messageLabel.setText(" ");
        resourceLabel.setText(" ");
        if (resourceList.getItemCount() > 0)
        {
            resourceList.removeAll();
        }

        resourceBean.setHost(hostText.getText());
        resourceBean.setPort(portText.getText());
        resourceBean.setCommunityName(communityText.getText());
        resourceBean.setUpdateInterval(intervalText.getText());

        resourceBean.action();
    }

    GridBagConstraints getGridBagConstraints2(
            int x, int y, int w, int h, double wx, double wy,
            int anchor, int fill,
            Insets ins, int ix, int iy)
    {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = w;
        gc.gridheight = h;
        gc.weightx = wx;
        gc.weighty = wy;
        gc.anchor = anchor;
        gc.fill = fill;
        gc.insets = ins;
        gc.ipadx = ix;
        gc.ipady = iy;

        return gc;
    }

public void windowActivated(WindowEvent evt)
{
}
public void windowDeactivated(WindowEvent evt)
{
}
public void windowClosing(WindowEvent evt)
{
    System.exit(0);
}
public void windowClosed(WindowEvent evt)
{
}
public void windowIconified(WindowEvent evt)
{
}
public void windowDeiconified(WindowEvent evt)
{
}
public void windowOpened(WindowEvent evt)
{
}
 
}