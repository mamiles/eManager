
/*
 * Copyright (c) 1998-2003 Cisco Systems, Inc.
 * All rights reserved.
 */


import java.util.*;
import com.tibco.tibrv.*;

public class eMgrClientReceiver implements TibrvMsgCallback
{

    String service = "7500";
    String network = null;
    String daemon  = "tcp:7500";
    String subject = "cisco.mgmt.emanager.*.*";

    String FIELD_NAME = "DATA";

    TibrvTransport transport = null;
    TibrvListener listener = null;
    TibrvQueue queue = null;

    public eMgrClientReceiver(String args[])
    {
        // parse arguments for possible optional
        // parameters. These must precede the number of requests
        int i = get_InitParams(args);

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
        try
        {
            transport = new TibrvRvdTransport(service,network,daemon);
            transport.setDescription("eMgrClientReceiver");
        }
        catch (TibrvException e)
        {
            System.err.println("Failed to create TibrvRvdTransport:");
            e.printStackTrace();
            System.err.println(" ");
            System.exit(0);
        }

	processMessage();
    }

    private void processMessage() 
    {
	try {
		System.out.println("Create TibrvrvQueue...");
        	queue = new TibrvQueue();
		System.out.println("Create TibrvListener to listen on subject: " + subject);
		listener = new TibrvListener(queue, this, transport, subject, null);
	} catch (TibrvException e) {
		e.printStackTrace();
		System.err.println("Pattern to create Tibco listener\n Exception: " + e);
		System.exit(0);
	}
	
	System.out.println("Ready to dispatch msg...");

	while (true) {
		try {
			queue.dispatch();
		} catch (TibrvException e) {
			System.err.println("TibrvException occured while dispatching queue:" + e);
			e.printStackTrace();
	    	} catch(Exception e) {
			System.err.println("Exception occured while dispatching queue:" + e);
			e.printStackTrace();
	    	}
	}	
		
    }

    // Listener callback counts responses, reports after all replies received.
    public void onMsg(TibrvListener listener, TibrvMsg msg)
    {
	    System.out.println("Receiving msg subject: " + msg.getSendSubject());
	    System.out.println("Message: " + msg.toString());
    }

    // Print usage information and quit
    void usage()
    {
        System.err.println("Usage: java eMgrClientReceiver [-service service] [-network network] [-daemon daemon]");
        System.exit(-1);
    }

    // Parse command line parameters.
    int get_InitParams(String[] args)
    {
        int i=0;
        if (args.length > 0)
        {
            if (args[i].equals("-?") ||
                args[i].equals("-h") ||
                args[i].equals("-help"))
            {
                usage();
            }
        }
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
        new eMgrClientReceiver(args);
    }

}
