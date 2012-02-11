
/*
 * Copyright (c) 1998-2003 Cisco Systems, Inc.
 * All rights reserved.
 */

import com.tibco.tibrv.*;
import java.util.*;
import java.io.*;


public class tibrvclient implements TibrvMsgCallback
{

    String service = "7500";
    String network = null;
    String daemon  = "tcp:7500";

    String FIELD_NAME = "DATA";
    String response_subject;
    double time_out = 60.0;


    public tibrvclient(String args[])
    {
        // parse arguments for possible optional
        // parameters. These must precede the subject
        // and message strings
        int i = get_InitParams(args);

        // we must have at least one subject and one message
        if (i > args.length-1)
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
	    System.out.println("creating Tibrv transport object...");
            transport = new TibrvRvdTransport(service,network,daemon);

        }
        catch (TibrvException e)
        {
            System.err.println("Failed to create TibrvRvdTransport:");
            e.printStackTrace();
            System.exit(0);
        }

	// create response queue
	TibrvQueue response_queue = null;
	try {
	    System.out.println("create response queue...");
	    response_queue = new TibrvQueue();
        }
        catch (TibrvException e)
        {
            System.err.println("Failed to create TibrvRvQueue:");
            e.printStackTrace();
            System.exit(0);
        }

	// create an inbox subject for communication with the server
	try {
	    System.out.println("createInbox....");
	    response_subject = transport.createInbox();
	    System.out.println("create listener to listen to the response_subject: " + response_subject);
	    new TibrvListener(response_queue, this, transport, response_subject, null);
        }
        catch (TibrvException e)
        {
            System.err.println("Failed to create listener:");
            e.printStackTrace();
            System.exit(0);
        }


        // Create the message
	String subject = args[i++];
	System.out.println("Attemting to send synchronous msg using subject: " + subject + " ...");
        TibrvMsg msg = new TibrvMsg();

        // Set send subject into the message
        try
        {
            msg.setSendSubject(subject);

        }
        catch (TibrvException e) {
            System.err.println("Failed to setSendSubject");
            e.printStackTrace();
            System.exit(0);
        }

	TibrvMsg reply_msg = null;
        try
        {
	    System.out.println("senRequest synchronous msg");
	    reply_msg = transport.sendRequest(msg, time_out);
	    System.out.println("sendRequest return");

        }
        catch (TibrvException e) {
            System.err.println("Failed to sendRequest msg");
            e.printStackTrace();
            System.exit(0);
        }

	if (reply_msg == null) {
		System.err.println("timeout occured - failed to contact server");
		System.exit(0);
	} else {
		System.out.println("server_response_subject:" + reply_msg.getReplySubject());
	}

	System.out.println("Close tibrv and quit ....");

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

    // print usage information and quit
    void usage()
    {
        System.err.println("Usage: java tibrvclient [-service service] [-network network]");
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

    public void onMsg(TibrvListener listener, TibrvMsg msg)
    {
	    System.out.println("onMsg: " + msg);
    }



    public static void main(String args[])
    {
        new tibrvclient(args);
    }

}




