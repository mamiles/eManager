import java.util.*;
import java.io.*;
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Receiver implements Runnable, TibrvMsgCallback
{

	static String service = "7474";
	static String network = null;
	static String daemon  = "tcp:7474";
	String subject = "cisco.mgmt.Test";

	private ReceiverAmi mReceiverAmi = null;
	private TibrvQueue mRvQueue = null;
    	protected TibrvListener listenerObj=null;
    	private static Logger logger = null;

	private Timer queueTimer = null;
	private ReminderTask reminderTask = null;

	private int pastQueueLength;
	private int percentChangeOfQueue = 0;
	private int msgCount = 0;
	private int msgRate = 0;
	public static void main( String[] args )
	{


		Receiver theReceiver = null;

		// initialize logger
		try {
			System.out.println("Initialize log4j");
			String log4jConfigFile = System.getProperty("log4j");
			PropertyConfigurator.configure(log4jConfigFile);
		} catch (Exception e) {
			System.out.println("Unexpected exceptiole configuring log4j: " + e);
		}
		logger = Logger.getLogger(Receiver.class);

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
			theReceiver = new Receiver (service, network, daemon);

			theReceiver.execute();

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
	public Receiver(String service, String network, String daemon)
		throws java.lang.Exception
	{

      		mRvQueue = new TibrvQueue();
		// Initialize Spot AMI interface class.
		System.out.println("Create ReceiverAmi...");
		mReceiverAmi = new ReceiverAmi( this, service, network, daemon, mRvQueue);

		// create Timer
		queueTimer = new Timer();
		System.out.println("Create the Timer...");
		if (reminderTask == null)
			reminderTask = new ReminderTask();
		queueTimer.schedule(reminderTask, 60000,60000);

	}

	public void execute()
	{
		new Thread(this).start();
		// Create RVD transport
		TibrvTransport transport = null;

		try {
			System.out.println("Create TibrvRvdTranport");
			transport = new TibrvRvdTransport(service,network,daemon);
		} catch (TibrvException e) {
			System.err.println("Failed to create TibrvRvdTransport\n Exception: " + e.getMessage());
			e.printStackTrace(System.out);
			System.exit(0);
		}

		// create listener using default queue
		try {
	    		System.out.println("Creat TibrvListener to listen on subject: "  + subject);
	    		 listenerObj = new TibrvListener(Tibrv.defaultQueue(),this,transport,subject,null);
	    		// listenerObj = new TibrvListener(mRvQueue,this,transport,subject,null);
		} catch (TibrvException e) {
	    		e.printStackTrace();
	    		System.err.println("Pattern to create Tibco listener\n Exception: " + e.getMessage());
	   		 System.exit(0);
		}
      		while( true )
      		{
        		try { mRvQueue.dispatch(); }
        		catch( java.lang.Throwable caughtThrowable ) { break; }
      		}
    	}


    	public void run()
    	{
		logger.debug("Receiver run....");

		while(true)
        	{
	    		try {
			 	Thread.sleep(1000);
				Tibrv.defaultQueue().dispatch();
	    		} catch(TibrvException e) {
				logger.error("TibrvException occured while dispatching default queue:" + e);
				System.out.println("TibrvException occured while dispatching default queue:" + e);
				e.printStackTrace();
	    		} catch(Exception e) {
				logger.error("Receiver::run catch exception: " + e.getMessage());
				Thread.currentThread().interrupted();
	    		}
		}
    	}

	public void onMsg(TibrvListener listener, TibrvMsg msg)
    	{
	    	logger.debug("TibcoMesssageListener receive msg: " + msg.toString());
		msgCount++;
    	}

    	public int getQueueLength()
    	{
		try {
	    		// int len =  mRvQueue.getCount();
	    		int len =  Tibrv.defaultQueue().getCount();
	    		return len;
   		} catch (Exception e) {
	    		System.err.println("Exception during getQueueLength: " + e);
	 	   	return 0;
		}
    	}

	public int getChangeofRateInQueueLength()
	{
		return percentChangeOfQueue;
	}

	public int getProcessingMsgRate()
	{
		return msgRate;
	}


	public void exitApp()
	{
		System.out.println("Receiving ExitApp msg...");
      		try {
        		mReceiverAmi.terminate(); // Shutdown AMI application.
        		mRvQueue.destroy();   // Shutdown message dispatcher.
      		} catch ( Exception caughtException )
      		{ caughtException.printStackTrace(); }
	}

	// Timer class to calculate the rate of queue
	class ReminderTask extends TimerTask
	{
		public void run()
		{
			try {
				int currentQueueLength = Tibrv.defaultQueue().getCount();
				// calculate the percentage change of queue's length in the last 1 minutes
				if (pastQueueLength == 0 && currentQueueLength > 0)
					percentChangeOfQueue = 100;
				else if (pastQueueLength == 0 && currentQueueLength == 0)
					percentChangeOfQueue = 0;
				else
					percentChangeOfQueue = (int)(((float)(currentQueueLength - pastQueueLength)/pastQueueLength)*100);
				logger.debug("Percentage Change of Queue is: " + percentChangeOfQueue +
					" - currentQueueLength = " + currentQueueLength +
					" - pasQueueLength = " + pastQueueLength);

				pastQueueLength = currentQueueLength;

				msgRate = msgCount;
				msgCount = 0; // reset msgCount
				logger.debug("Current processing msg rate is: " + msgRate + " msgs/min");

			} catch (Exception e) {
				System.out.println("Exeception occurred from ReminderTask: " + e);
				logger.error("Exeception occurred from ReminderTask: " + e);
			}
		}
	}

}
