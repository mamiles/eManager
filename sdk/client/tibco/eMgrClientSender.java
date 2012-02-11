
/*
 * Copyright (c) 1998-2003 Cisco Systems, Inc.
 * All rights reserved.
 */

import com.tibco.tibrv.*;
import java.util.*;
import java.io.*;


public class eMgrClientSender
{

    String service = "7500";
    String network = null;
    String daemon  = "tcp:7500";

    String FIELD_NAME = "DATA";


    public eMgrClientSender(String args[])
    {
        // parse arguments for possible optional
        // parameters. These must precede the subject
        // and message strings
        int i = get_InitParams(args);

        // we must have at least one subject and one message
        if (i > args.length-2)
            usage();

        // open Tibrv in native implementation
        try
        {
            Tibrv.open(Tibrv.IMPL_NATIVE);
        }
        catch (TibrvException e)
        {
            System.err.println("Failed to open Tibrv in native implementation:");
            e.printStackTrace();
            System.exit(0);
        }

        // Create RVD transport
        TibrvTransport transport = null;
        try
        {
            transport = new TibrvRvdTransport(service,network,daemon);
        }
        catch (TibrvException e)
        {
            System.err.println("Failed to create TibrvRvdTransport:");
            e.printStackTrace();
            System.exit(0);
        }

	String subject = args[i++];
	String msg_file = args[i];

	String [] msgs = prepareMessages(msg_file);
	StringBuffer xmlMsg;

        // Create the message
        TibrvMsg msg = new TibrvMsg();
	xmlMsg = new StringBuffer();

        // Set send subject into the message
        try
        {
            msg.setSendSubject(subject);
        }
        catch (TibrvException e) {
            System.err.println("Failed to set send subject:");
            e.printStackTrace();
            System.exit(0);
        }

        try
        {
	    System.out.println("Publishing: subject="+msg.getSendSubject());

	    // we assume that all of the lines comprise a single xml message
	    
	    for (int count=0; count<msgs.length; count++) {
		xmlMsg.append (msgs[count]);
		xmlMsg.append ("\n");
	    }

	    msg.update(FIELD_NAME, xmlMsg.toString());
	    System.out.println("Sending msg: "+msg.toString());
	    transport.send(msg);
        }
        catch (TibrvException e)
        {
            System.err.println("Error sending a message:");
            e.printStackTrace();
            System.exit(0);
        }

	closeTibrv();
    }

    public void closeTibrv()
    {


        // Close Tibrv, it will cleanup all underlying memory, destroy
        // transport and guarantee delivery.
        try
        {
            Tibrv.close();
        }
        catch(TibrvException e)
        {
            System.err.println("Exception dispatching default queue:");
            e.printStackTrace();
            System.exit(0);
        }

    }

    public String [] prepareMessages(String filename)
    {
	    List msgs = new ArrayList();
	    try {
		   java.io.File msgFile = new java.io.File(filename);
		   if (msgFile.exists()) {
			   BufferedReader br = new BufferedReader(new FileReader(msgFile));
			   String line = null;
			   while ( (line = br.readLine()) != null) {
				   msgs.add(line);
			   }
		   }
	    } catch (Exception e) {
            	    System.err.println("Exception while reading msg file: " + filename);
            	    e.printStackTrace();
	    }

	    if (msgs.size() == 0) {
		    System.out.println(" Message file: " + filename + "has no msg to send");
		    closeTibrv();
	    } 

	    return (String[]) msgs.toArray(new String[0]);
     }

    // print usage information and quit
    void usage()
    {
        System.err.println("Usage: java eMgrClientSender [-service service] [-network network]");
        System.err.println("            [-daemon daemon] <subject> <msg_file>");
        System.exit(-1);
    }

    int get_InitParams(String[] args)
    {
        int i=0;
        while(i < args.length-1 && args[i].startsWith("-"))
        {
            if (args[i].equals("-service"))
            {
                service = args[i+1];
                i += 2;
            }
            else
            if (args[i].equals("-network"))
            {
                network = args[i+1];
                i += 2;
            }
            else
            if (args[i].equals("-daemon"))
            {
                daemon = args[i+1];
                i += 2;
            }
            else
                usage();
        }
        return i;
    }


    public static void main(String args[])
    {
        new eMgrClientSender(args);
    }

}




