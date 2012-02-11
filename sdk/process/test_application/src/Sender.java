
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import java.util.*;
import java.io.*;

import org.apache.log4j.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
public class Sender implements Runnable
{

	private TibrvQueue mRvQueue = null;
	private SenderAmi mSenderAmi = null;
	private Sender2Ami mSender2Ami = null;

	static String service = "7474";
	static String network = null;
	static String daemon  = "tcp:7474";
	String subject = "cisco.mgmt.Test";
	private boolean sendMsg = true;
	private boolean needToExit = false;

	String FIELD_NAME = "APP_MSG";
 	private static int msgRate = 120; // initially set msg rate at 1 msg/sec
    	private static Logger logger = null;


	public static void main( String[] args )
	{

		Sender theSender = null;
        	int i=0;
        	while(i < args.length-1 && args[i].startsWith("-"))
        	{
            		if (args[i].equals("-rateSend"))
            		{
                		msgRate = Integer.parseInt(args[i+1]);
                		i += 2;
			}
            	}

		// initialize logger
		try {
			System.out.println("Initialize log4j");
			String log4jConfigFile = System.getProperty("log4j");
			PropertyConfigurator.configure(log4jConfigFile);
		} catch (Exception e) {
			System.out.println("Unexpected exceptiole configuring log4j: " + e);
		}
		logger = Logger.getLogger(Sender.class);

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

		try {
			theSender = new Sender (service, network, daemon);

			theSender.execute();

			System.out.println("Close Tibrv before exiting...");
			Tibrv.close();
        	} catch (TibrvException e) {
			System.err.println("TibrvException: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception: " + e);
			e.printStackTrace();
		}


		System.exit(0);

	}


	public Sender(String service, String network, String daemon)
		throws java.lang.Exception
	{

      		mRvQueue = new TibrvQueue();
		// Initialize Spot AMI interface class.
		System.out.println("Create SenderAmi...");
		mSenderAmi = new SenderAmi( this, service, network, daemon, mRvQueue);
		mSender2Ami = new Sender2Ami( this, service, network, daemon, mRvQueue);

	}

	public void execute()
	{
		new Thread(this).start();
      		// Dispatch Tibrv events. Destroy RV queue to terminate this loop.
      		while( true )
      		{
        		try { mRvQueue.dispatch(); }
        		catch( java.lang.Throwable caughtThrowable ) { break; }
      		}
    	}


	public void run()
	{
		int msgCnt = 0;

		System.out.println("Running....");

        	// Create RVD transport
        	TibrvTransport transport = null;
        	try
        	{
            		transport = new TibrvRvdTransport(service,network,daemon);
        	}
        	catch (TibrvException e)
        	{
            		System.err.println("Sender failed to create TibrvRvdTransport:");
            		e.printStackTrace();
            		System.exit(0);
        	}

        	// Create the message
        	TibrvMsg msg = new TibrvMsg();

        	// Set send subject into the message
        	try
        	{
            		msg.setSendSubject(subject);
        	}
        	catch (TibrvException e)
		{
            		System.err.println("Failed to set send subject:");
            		e.printStackTrace();
            		System.exit(0);
        	}

		while (true) {
		    try {
			if (sendMsg == true) {
				if ( msgCnt >  10 && msgCnt < 30 && logger.getLevel() != Level.INFO) {
					logger.debug("Set debug level to INFO");
					logger.setLevel((Level)Level.INFO);
					logger.debug("Should not display debug msg");
					logger.info("Should display INFO msg");
				} else if (msgCnt >  30 && logger.getLevel() != Level.DEBUG) {
					logger.debug("Set debug level back to DEBUG");
					logger.setLevel((Level)Level.DEBUG);
					logger.debug("Should display debug msg");
					logger.info("Should display INFO msg");
				}
				String msgText = "Sending Tibco msg to Receiver - Message count = " + msgCnt++;
				msg.update(FIELD_NAME, msgText);
				logger.debug("Sending msg: " + msg.toString());
				transport.send(msg);
			}
			else
				logger.debug("Not Sending msg");
			// initialy send msg with rate of 1 ms/sec
			Thread.sleep(60000/msgRate);
		    } catch (Exception e) {
			System.err.println("Exception while sending msg: " + e);
		    }
		}

	}

	public void exitApp()
	{
		System.out.println("Receiving ExitApp msg...");
      		try {
        		mSenderAmi.terminate(); // Shutdown AMI application.
        		mRvQueue.destroy();   // Shutdown message dispatcher.
      		} catch ( Exception caughtException )
      		{ caughtException.printStackTrace(); }
	}



	public void resumeSendingMsg()
	{
		System.out.println("Receiving resumestartSendMsg from AMI");
		sendMsg = true;

	}

	public void stopSendingMsg()
	{
		System.out.println("Receiving stopSendingMsg from AMI");
		sendMsg = false;

	}

	public void changeSendMsgRate(int changeRate)
	{
		msgRate = msgRate + (msgRate*changeRate)/100;

		logger.info("Adjust sending msg approximately by " + changeRate + " percent - Sending msg at the rate of " + msgRate + " msgs/min");

	}

	public int getSendMsgRate()
	{
		return msgRate;
	}



}

