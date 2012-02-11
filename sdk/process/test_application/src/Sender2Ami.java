import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import java.util.*;

public class Sender2Ami
{
   //***************************************************************************
   //                             Constants
   //***************************************************************************

   private Sender        mSender = null;
   /** Instance of AMI API session.*/
   private AmiSession  mAMI  = null;


   //***************************************************************************
   //* Method: Sender2Ami.<init>
   //*
   /** Constructs an instance of the TIBCO Hawk Application Management Interface
    *  (AMI) for the Sender application.
    *
    *  @param inSender      Instance of Sender application being instrumented.
    *  @param rvTransport RV transport for AMI communication with the Hawk agent.
    *  @param rvQueue     RV queue for AMI communication with the Hawk agent.
    ***************************************************************************/
    Sender2Ami(
      Sender         inSender,
      String         rvService,
      String         rvNetwork,
      String         rvDaemon,
      TibrvQueue     rvQueue)
      throws AmiException
    {

	System.out.println("Sender2Ami constructor...");
      // Preserve Sender instance.
      mSender = inSender;

      // Create an AMI API session.
      mAMI = new AmiSession( rvService, rvNetwork, rvDaemon, rvQueue,
                             "COM.TIBCO.ami_api.java.Sender2",
                             "Sender2",
      "This is a sample applicatio designed to illustrate " +
      "how to instrument a Java application using the TIBCO Hawk Application " +
      "Management Interface (AMI) Java API.", null );

      // Add Sender methods to session.
      mAMI.addMethod ( new methodFoo());

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
   //* Method: Sender2Ami.terminate
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
    class methodFoo extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodStopSendingMsg.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodFoo()
       {
         super( "foo",
                "This method does nothing",
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
           	// do nothing
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

}
