import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import java.util.*;

public class SenderAmi
{
   //***************************************************************************
   //                             Constants
   //***************************************************************************

   private Sender        mSender = null;
   /** Instance of AMI API session.*/
   private AmiSession  mAMI  = null;


   //***************************************************************************
   //* Method: SenderAmi.<init>
   //*
   /** Constructs an instance of the TIBCO Hawk Application Management Interface
    *  (AMI) for the Sender application.
    *
    *  @param inSender      Instance of Sender application being instrumented.
    *  @param rvTransport RV transport for AMI communication with the Hawk agent.
    *  @param rvQueue     RV queue for AMI communication with the Hawk agent.
    ***************************************************************************/
    SenderAmi(
      Sender         inSender,
      String         rvService,
      String         rvNetwork,
      String         rvDaemon,
      TibrvQueue     rvQueue)
      throws AmiException
    {

	System.out.println("SenderAmi constructor...");
      // Preserve Sender instance.
      mSender = inSender;

      // Create an AMI API session.
      mAMI = new AmiSession( rvService, rvNetwork, rvDaemon, rvQueue,
                             "COM.TIBCO.ami_api.java.Sender",
                             "Sender",
      "This is a sample applicatio designed to illustrate " +
      "how to instrument a Java application using the TIBCO Hawk Application " +
      "Management Interface (AMI) Java API.", null );

      // Add Sender methods to session.
      mAMI.addMethod ( new methodStopSendingMsg());
      mAMI.addMethod ( new methodResumeSendingMsg());
      mAMI.addMethod ( new methodExitApp());
      mAMI.addMethod ( new methodGetSendMsgRate());
      mAMI.addMethod ( new methodChangeSendMsgRate());

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
      mAMI.createCommonMethods("Sender (Sample AMI Sender Java API Application)",
                               "1.0.0L",
                               "Wed 02/12/2003",
                               1,
                               0,
                               0);

	System.out.println("METHOD CLASS is: " + AmiConstants.METHOD_TYPE_INFO.getClass().getName());
	System.out.println("METHOD_TYPE_INFO: " + AmiConstants.METHOD_TYPE_INFO );
	System.out.println("METHOD_TYPE_ACTION: " + AmiConstants.METHOD_TYPE_ACTION);
	System.out.println("METHOD_TYPE_ACTION_INFO: " + AmiConstants.METHOD_TYPE_ACTION_INFO );
      // Annouce our existence.
      mAMI.announce();
	System.out.println("Constructor completed...");
    }

   //***************************************************************************
   //* Method: SenderAmi.terminate
   //*
   /** This method is used to shutdown the AMI session.
    ***************************************************************************/
   public void terminate()
     throws AmiException
   {
     mAMI.stop();
   }

   /***************************************************************************
    Inner Class: methodStopSendingMsg
    ***************************************************************************/
    class methodStopSendingMsg extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodStopSendingMsg.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodStopSendingMsg()
       {
         super( "stopSendingMsg",
                "This method tells the Sender to stop sending Tibco msg.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodStopSendingMsg.onInvoke
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
           	mSender.stopSendingMsg();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

   /***************************************************************************
    Inner Class: methodResumeSendingMsg
    ***************************************************************************/
    class methodResumeSendingMsg extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodResumeSendingMsg.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodResumeSendingMsg()
       {
         super( "resumeSendingMsg",
                "This method tells the Sender to resume sending Tibco msg.",
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
           	mSender.resumeSendingMsg();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
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
                "This method terminates the Sender App.",
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
           	mSender.exitApp();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

    class methodGetSendMsgRate extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

       public methodGetSendMsgRate()
       {
         super( "getSendMsgRate",
                "This method returns the rate the receiver send msg in number of msg/minute",
                AmiConstants.METHOD_TYPE_INFO );
       }

       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "SendMsgRate",
                     "Get the current Sender's sending msg rate", 0 ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodGetMsgRate.onInvoke
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
             new AmiParameter( "SendMsgRate", new Integer(mSender.getSendMsgRate()) ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );

       }
    }

    class methodChangeSendMsgRate extends AmiMethod
    {
      private final Integer[] sLegalChoices =
        { new Integer( 0), new Integer(30), new Integer(45), new Integer(60),
          new Integer(90), new Integer(91), new Integer(-5) };

      /** This AMI method returns no values.*/
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodChangeSendMsgRate.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodChangeSendMsgRate().
       ************************************************************************/
       public methodChangeSendMsgRate()
       { super( "changeSendMsgRate",
                "This method change the rate of senfing msg from the Sender in percentage. " ,
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodChangeSendMsgRate.getArguments
      //*
      /** This method returns an AMIParameterList describing the parameters
       *  expected by the setAngle AMI method.
       *
       *  @returns AmiParameterList describing the parameters expected by this
       *           method.
       ************************************************************************/
       public AmiParameterList getArguments()
       {
         AmiParameterList theArgs = new AmiParameterList();
         AmiParameter     theArg  = null;

         theArg = new AmiParameter( "percentageChange",
                     "Change the sending msg rate to plus or minus the percentage change.", 0 );
         // theArg.setLegalChoices( sLegalChoices );
         theArgs.addElement( theArg );

         return( theArgs );
       }
      //************************************************************************
      //* Method: methodChangeSendMsgRate.onInvoke
      //*
      /** This method processes invocations of the setAngle AMI method.
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
           // Get method parameters.
           int percentChange =
              ((Integer)((AmiParameter)inParms.elementAt(0)).getValue()).intValue();

             mSender.changeSendMsgRate( percentChange );
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

}
