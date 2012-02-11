import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import java.util.*;

public class ReceiverAmi
{
   //***************************************************************************
   //                             Constants
   //***************************************************************************

   private Receiver        mReceiver = null;
   /** Instance of AMI API session.*/
   private AmiSession  mAMI  = null;


   //***************************************************************************
   //* Method: ReceiverAmi.<init>
   //*
   /** Constructs an instance of the TIBCO Hawk Application Management Interface
    *  (AMI) for the Receiver application.
    *
    *  @param inReceiver      Instance of Receiver application being instrumented.
    *  @param rvTransport RV transport for AMI communication with the Hawk agent.
    *  @param rvQueue     RV queue for AMI communication with the Hawk agent.
    ***************************************************************************/
    ReceiverAmi(
      Receiver       inReceiver,
      String         rvService,
      String         rvNetwork,
      String         rvDaemon,
      TibrvQueue     rvQueue)
      throws AmiException
    {

	System.out.println("ReceiverAmi constructor...");
      // Preserve Receiver instance.
      mReceiver = inReceiver;

      // Create an AMI API session.
      mAMI = new AmiSession( rvService, rvNetwork, rvDaemon, rvQueue,
                             "COM.TIBCO.ami_api.java.Receiver",
                             "Receiver",
      "This is a sample applicatio designed to illustrate " +
      "how to instrument a Java application using the TIBCO Hawk Application " +
      "Management Interface (AMI) Java API.", null );

      // Add Receiver methods to session.
      mAMI.addMethod ( new methodGetQueueLength());
      mAMI.addMethod ( new methodExitApp());
      mAMI.addMethod ( new methodGetChangeOfRateFromQueueLength());
      mAMI.addMethod ( new methodGetProcessingMsgRate());
      mAMI.addMethod ( new methodGetReceiverStats());
      // AMI session is itself AMI enabled. Add its AMI methods as well.
      //   getMaxThreads
      //   setMaxThreads
      mAMI.addMethods( mAMI );

      // Creates the following common methods for this Session:
      //   getReleaseVersion
      //   getTraceLevel
      //   setTraceLevel
      //   getTraceParameters
      //   setTraceParameters
      mAMI.createCommonMethods("Receiver (Sample AMI Receiver Java API Application)",
                               "1.0.0L",
                               "Wed 02/12/2003",
                               1,
                               0,
                               0);

      // Annouce our existence.
      mAMI.announce();
	System.out.println("Constructor completed...");
    }

   //***************************************************************************
   //* Method: ReceiverAmi.terminate
   //*
   /** This method is used to shutdown the AMI session.
    ***************************************************************************/
   public void terminate()
     throws AmiException
   {
     mAMI.stop();
   }

   /***************************************************************************
    Inner Class: methodGetQueueLength
    ***************************************************************************/
    class methodGetQueueLength extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodGetQueueLength.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodGetQueueLength()
       {
         super( "GetQueueLength",
                "This method returns the number of Tibco msg from  the Receiver's queue.",
                AmiConstants.METHOD_TYPE_INFO );
       }

       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "Queue's Length",
                     "The current outstanding msg in the Receiver's queue ", 0 ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodGetQueueLength.onInvoke
      //*
      /** This method processes invocations of the getAngle AMI method.
       *
       *  @param inParms Parameters for method invocation.
       *  @returns AmiParameterList of return values for this method.
       ************************************************************************/
       public AmiParameterList onInvoke(
         AmiParameterList inParms )
         throws AmiException
       {
         AmiParameterList theValues = null;

         try
         { theValues = new AmiParameterList();
           theValues.addElement(
             new AmiParameter( "Queue's Length", new Integer(mReceiver.getQueueLength()) ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );

       }
    }

    /***************************************************************************
    Inner Class: methodGetChangeOfRateFromQueueLength
    ***************************************************************************/
    class methodGetChangeOfRateFromQueueLength extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodGetChangeOfRateFromQueueLength.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodGetChangeOfRateFromQueueLength()
       {
         super( "getChangeOfRateFromQueueLength",
                "This method returns the percentage change of length of queue length for the last minute",
                AmiConstants.METHOD_TYPE_INFO );
       }

       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "PercentageChanged",
                     "The percentage change in Receiver's queue ", (float)0.00 ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodGetChangeOfRateFromQueueLength.onInvoke
      //*
      /** This method processes invocations of the getAngle AMI method.
       *
       *  @param inParms Parameters for method invocation.
       *  @returns AmiParameterList of return values for this method.
       ************************************************************************/
       public AmiParameterList onInvoke(
         AmiParameterList inParms )
         throws AmiException
       {
         AmiParameterList theValues = null;

         try
         { theValues = new AmiParameterList();
           theValues.addElement(
             new AmiParameter( "PercentageChanged", new Float(mReceiver.getChangeofRateInQueueLength()) ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );

       }
    }

    /***************************************************************************
    Inner Class: methodGetProcessingMsgRate
    ***************************************************************************/
    class methodGetProcessingMsgRate extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodGetProcessingMsgRate.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodGetProcessingMsgRate()
       {
         super( "getProcessingMsgRate",
                "This method returns the rate that the Receiver process the msg - in msgs/min",
                AmiConstants.METHOD_TYPE_INFO );
       }

       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "ProcessingRate",
                     "The rate Receiver processes msgs", 0 ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodGetProcessingMsgRate.onInvoke
      //*
      /** This method processes invocations of the getAngle AMI method.
       *
       *  @param inParms Parameters for method invocation.
       *  @returns AmiParameterList of return values for this method.
       ************************************************************************/
       public AmiParameterList onInvoke(
         AmiParameterList inParms )
         throws AmiException
       {
         AmiParameterList theValues = null;

         try
         { theValues = new AmiParameterList();
           theValues.addElement(
             new AmiParameter( "ProcessingRate", new Integer(mReceiver.getProcessingMsgRate()) ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );

       }
    }


    class methodExitApp extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodResumeSendingMsg.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodExitApp()
       {
         super( "exitApp",
                "This method terminates the Receiver App.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodResumeSendingMsg.onInvoke
      //*
      /** This method processes invocations of the getAngle AMI method.
       *
       *  @param inParms Parameters for method invocation.
       *  @returns AmiParameterList of return values for this method.
       ************************************************************************/
       public AmiParameterList onInvoke(
         AmiParameterList inParms )
         throws AmiException
       {

         try
         { 
           	mReceiver.exitApp();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

    class methodGetReceiverStats extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodGetReceiverStats.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodGetReceiverStats()
       {
         super( "getReceiverStats",
                "This method returns the percentage change of length of queue length for the last minute",
                AmiConstants.METHOD_TYPE_INFO);
                // AmiConstants.METHOD_TYPE_INFO,"PercentageChanged");
       }

       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "PercentageChanged",
                     "The percentage change in Receiver's queue ", 0 ) );
	 theReturns.addElement(new AmiParameter( "QueueLength",
                     "The current outstanding msg in the Receiver's queue ", 0 ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodGetReceiverStats.onInvoke
      //*
      /** This method processes invocations of the getAngle AMI method.
       *
       *  @param inParms Parameters for method invocation.
       *  @returns AmiParameterList of return values for this method.
       ************************************************************************/
       public AmiParameterList onInvoke(
         AmiParameterList inParms )
         throws AmiException
       {
         AmiParameterList theValues = null;

         try
         { theValues = new AmiParameterList();
           theValues.addElement(
             new AmiParameter( "PercentageChanged", new Integer(mReceiver.getChangeofRateInQueueLength()) ));
           theValues.addElement(
             new AmiParameter( "QueueLength", new Integer(mReceiver.getQueueLength()) ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );

       }
    }

}
