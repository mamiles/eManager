//******************************************************************************
//*
//* Module:  SpotAmi.java
//*
//* Purpose: Implements the TIBCO Hawk Application Management Interface (AMI)
//*          for the Spot application using the TIBCO Hawk AMI Java API.
//*
//*           Copyright 2001 TIBCO Software Inc. All rights reserved.
//******************************************************************************
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;
import java.util.*;

//******************************************************************************
//* Class: SpotAmi
//*
/** Implements the TIBCO Hawk Application Management Interface (AMI) for the
 *  Spot application.
 ******************************************************************************/
 public class SpotAmi
 {
   //***************************************************************************
   //                             Constants
   //***************************************************************************

   /** Instance of Spot application being exposed via AMI.*/
   private Spot        mSpot = null;
   /** Instance of AMI API session.*/
   private AmiSession  mAMI  = null;
   /** Instance of asynchronous AMI method onAngleChange().*/
   methodOnAngleChange mMethodOnAngleChange = null;
   /** Instance of asynchronous AMI method onColorChange().*/
   methodOnColorChange mMethodOnColorChange = null;

   //***************************************************************************
   //* Method: SpotAmi.<init>
   //*
   /** Constructs an instance of the TIBCO Hawk Application Management Interface
    *  (AMI) for the Spot application.
    *
    *  @param inSpot      Instance of Spot application being instrumented.
    *  @param rvTransport RV transport for AMI communication with the Hawk agent.
    *  @param rvQueue     RV queue for AMI communication with the Hawk agent.
    ***************************************************************************/
    SpotAmi(
      Spot           inSpot,
      String         rvService,
      String         rvNetwork,
      String         rvDaemon,
      TibrvQueue     rvQueue)
      throws AmiException
    {
      // Preserve Spot instance.
      mSpot = inSpot;

      // Create an AMI API session.
      mAMI = new AmiSession( rvService, rvNetwork, rvDaemon, rvQueue,
                             "COM.TIBCO.ami_api.java.Spot",
                             "Spot",
      "This is a sample application designed to illustrate " +
      "how to instrument a Java application using the TIBCO Hawk Application " +
      "Management Interface (AMI) Java API. The source code for this " +
      "application can be found in your TIBCO Hawk installation samples " +
      "directory.", null );

      // Add Spot methods to session.
      mMethodOnAngleChange = new methodOnAngleChange();
      mMethodOnColorChange = new methodOnColorChange();

      mAMI.addMethod ( new methodSetAngle()      );
      mAMI.addMethod ( new methodGetAngle()      );
      mAMI.addMethod ( mMethodOnAngleChange      );
      mAMI.addMethod ( new methodSetColor()      );
      mAMI.addMethod ( new methodGetColor()      );
      mAMI.addMethod ( mMethodOnColorChange      );
      mAMI.addMethod ( new methodSuspend()       );
      mAMI.addMethod ( new methodResume()        );
      mAMI.addMethod ( new methodAccelerate()    );
      mAMI.addMethod ( new methodDecelerate()    );
      mAMI.addMethod ( new methodGetColorCount() );
      mAMI.addMethod ( new methodTimeOutTest()   );
      mAMI.addMethod ( new methodExceptionTest() );
      mAMI.addMethod ( new methodStop()          );

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
      mAMI.createCommonMethods("Spot (Sample AMI Java API Application)",
                               "4.1.1_FINAL",
                               "Wed 04/30/2003",
                               4,
                               1,
                               1);

      // Annouce our existence.
      mAMI.announce();
    }

   //***************************************************************************
   //* Method: SpotAmi.terminate
   //*
   /** This method is used to shutdown the AMI session.
    ***************************************************************************/
   public void terminate()
     throws AmiException
   {
     mAMI.stop();
   }

   //***************************************************************************
   //* Method: SpotAmi.sendUnsolicitedMsg
   //*
   /** This method is used send unsolicited messages to the Hawk agent.
    ***************************************************************************/
   public void sendUnsolicitedMsg(
     AmiAlertType inType,
     String       inText,
     Integer      inID )
     throws AmiException
   {
     mAMI.sendUnsolicitedMsg( inType, inText, inID );
   }
   //***************************************************************************
   //* Method: SpotAmi.notifyAngleChange
   //*
   /** This method is used to notify the AMI interface of an angle change.
    ***************************************************************************/
   public void notifyAngleChange()
     throws AmiException
   {
     if ( mMethodOnAngleChange != null )
       mMethodOnAngleChange.onData();
   }

   //***************************************************************************
   //* Method: SpotAmi.notifyColorChange
   //*
   /** This method is used to notify the AMI interface of a color change.
    ***************************************************************************/
   public void notifyColorChange()
     throws AmiException
   {
     if ( mMethodOnColorChange != null )
       mMethodOnColorChange.onData();
   }

   //***************************************************************************
   //* Inner Class: methodStop
   //*
   /** This class implements the SpotAmi stop AMI method.
    ***************************************************************************/
    class methodStop extends AmiMethod
    {
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodStop.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodStop().
       ************************************************************************/
       public methodStop()
       { super( "stop",
                "This method terminates the Spot application.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodStop.onInvoke
      //*
      /** This method processes invocations of the suspend AMI method.
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
           mSpot.selectedExit();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

   //***************************************************************************
   //* Inner Class: methodSuspend
   //*
   /** This class implements the SpotAmi suspend AMI method.
    ***************************************************************************/
    class methodSuspend extends AmiMethod
    {
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodSuspend.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodSuspend().
       ************************************************************************/
       public methodSuspend()
       { super( "suspend",
                "This method suspends the movement of the ball.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodSuspend.onInvoke
      //*
      /** This method processes invocations of the suspend AMI method.
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
           mSpot.suspendAnimation();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

   //***************************************************************************
   //* Inner Class: methodResume
   //*
   /** This class implements the SpotAmi resume AMI method.
    ***************************************************************************/
    class methodResume extends AmiMethod
    {
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodResume.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodResume().
       ************************************************************************/
       public methodResume()
       { super( "resume",
                "This method resumes the movement of the ball.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodResume.onInvoke
      //*
      /** This method processes invocations of the resume AMI method.
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
           mSpot.resumeAnimation();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

   //***************************************************************************
   //* Inner Class: methodAccelerate
   //*
   /** This class implements the SpotAmi accelerate AMI method.
    ***************************************************************************/
    class methodAccelerate extends AmiMethod
    {
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodAccelerate.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodAccelerate().
       ************************************************************************/
       public methodAccelerate()
       { super( "accelerate",
                "This method accelerates the movement of the ball.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodAccelerate.onInvoke
      //*
      /** This method processes invocations of the accelerate AMI method.
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
           mSpot.accelerate();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

   //***************************************************************************
   //* Inner Class: methodDecelerate
   //*
   /** This class implements the SpotAmi decelerate AMI method.
    ***************************************************************************/
    class methodDecelerate extends AmiMethod
    {
      public AmiParameterList getArguments(){ return( null ); }
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodDecelerate.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodDecelerate().
       ************************************************************************/
       public methodDecelerate()
       { super( "decelerate",
                "This method decelerates the movement of the ball.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodDecelerate.onInvoke
      //*
      /** This method processes invocations of the decelerate AMI method.
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
           mSpot.decelerate();
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }

   //***************************************************************************
   //* Inner Class: methodSetAngle
   //*
   /** This class implements the SpotAmi setAngle AMI method.
    ***************************************************************************/
    class methodSetAngle extends AmiMethod
    {
      private final Integer[] sLegalChoices =
        { new Integer( 0), new Integer(30), new Integer(45), new Integer(60),
          new Integer(90), new Integer(91), new Integer(-5) };

      /** This AMI method returns no values.*/
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodSetAngle.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodSetAngle().
       ************************************************************************/
       public methodSetAngle()
       { super( "setAngle",
                "This method sets the angle of trajectory of the ball. " +
                "Attempting to set an angle other than zero to 360 degrees " +
                "causes this application to generate an AMI unsolicited " +
                "notification of type ERROR.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodSetAngle.getArguments
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

         theArg = new AmiParameter( "Angle",
                     "The angle to be set in the range 0 to 360 degrees.", 0 );
         theArg.setLegalChoices( sLegalChoices );
         theArgs.addElement( theArg );

         return( theArgs );
       }
      //************************************************************************
      //* Method: methodSetAngle.onInvoke
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
           int theAngle =
              ((Integer)((AmiParameter)inParms.elementAt(0)).getValue()).intValue();

           // If specified angle is invalid send unsolicited message.
           if ( theAngle > 360 || theAngle < 0 )
           {
             sendUnsolicitedMsg( AmiConstants.ALERT_TYPE_ERROR,
                                 "Angle specified, " + theAngle +
                                 ", is invalid.", new Integer(1) );
           }
           // If specified angle is valid then set it.
           else
           {
             mSpot.setAngle( theAngle );
           }
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }
   //***************************************************************************
   //* Inner Class: methodGetAngle
   //*
   /** This class implements the SpotAmi getAngle AMI method.
    ***************************************************************************/
    class methodGetAngle extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodGetAngle.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetAngle().
       ************************************************************************/
       public methodGetAngle()
       {
         super( "getAngle",
                "This method returns the current angle of trajectory of the ball.",
                AmiConstants.METHOD_TYPE_INFO );
       }
      //************************************************************************
      //* Method: methodGetAngle.getReturns
      //*
      /** This method returns an AMIParameterList describing the values returned
       *  by the getAngle AMI method.
       *
       *  @returns AmiParameterList describing return values for this method.
       ************************************************************************/
       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "Angle",
                     "The current angle in the range 0 to 360 degrees.", 0 ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodGetAngle.onInvoke
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
             new AmiParameter( "Angle", new Integer(mSpot.getAngle()) ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );
       }
    }
   //***************************************************************************
   //* Inner Class: methodSetColor
   //*
   /** This class implements the SpotAmi setColor AMI method.
    ***************************************************************************/
    class methodSetColor extends AmiMethod
    {
      private final String[] sLegalChoices
        = { "black", "blue", "cyan", "darkGray", "gray", "green",
            "lightGray", "magenta", "orange", "pink", "red", "yellow" };

      /** This AMI method returns no values.*/
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodSetColor.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodSetColor().
       ************************************************************************/
       public methodSetColor()
       { super( "setColor",
                "This method sets the color for the ball.",
                AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodSetColor.getArguments
      //*
      /** This method returns an AMIParameterList describing the parameters
       *  expected by the setColor AMI method.
       *
       *  @returns AmiParameterList describing the parameters expected by this
       *           method.
       ************************************************************************/
       public AmiParameterList getArguments()
       {
         AmiParameterList theArgs = new AmiParameterList();
         AmiParameter     theArg  = null;

         theArg = new AmiParameter( "Color",
                                    "The name of the color to be set.", "" );
         theArg.setLegalChoices( sLegalChoices );
         theArgs.addElement( theArg );

         return( theArgs );
       }
      //************************************************************************
      //* Method: methodSetColor.onInvoke
      //*
      /** This method processes invocations of the setColor AMI method.
       *
       *  @param inParms Parameters for method invocation.
       *  @returns AmiParameterList of return values for this method.
       ************************************************************************/
       public AmiParameterList onInvoke(
         AmiParameterList inParms )
         throws AmiException
       {
         try
         { String theColor = (String)((AmiParameter)inParms.elementAt(0)).getValue();
           mSpot.setColorByName( theColor );
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( null );
       }
    }
   //***************************************************************************
   //* Inner Class: methodGetColor
   //*
   /** This class implements the SpotAmi getColor AMI method.
    ***************************************************************************/
    class methodGetColor extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodGetColor.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetColor().
       ************************************************************************/
       public methodGetColor()
       {
         super( "getColor",
                "This method returns the current color of the ball.",
                AmiConstants.METHOD_TYPE_INFO );
       }
      //************************************************************************
      //* Method: methodGetColor.getReturns
      //*
      /** This method returns an AMIParameterList describing the values returned
       *  by the getColor AMI method.
       *
       *  @returns AmiParameterList describing return values for this method.
       ************************************************************************/
       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "Color",
                                "The current color of the ball.", "" ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodGetColor.onInvoke
      //*
      /** This method processes invocations of the getColor AMI method.
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
             new AmiParameter( "Color", mSpot.getColorByName() ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );
       }
    }
   //***************************************************************************
   //* Inner Class: methodGetColorCount
   //*
   /** This class implements the SpotAmi getColorAssignmentCount AMI method.
    ***************************************************************************/
    class methodGetColorCount extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodGetColorCount.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodGetColorCount().
       ************************************************************************/
       public methodGetColorCount()
       { super( "getColorAssignmentCount",
                "This method returns the number of times each color has been " +
                "assigned.",
                AmiConstants.METHOD_TYPE_INFO, "Color" );
       }
      //************************************************************************
      //* Method: methodGetColorCount.getReturns
      //*
      /** This method returns an AMIParameterList describing the values returned
       *  by the getColorAssignmentCount AMI method.
       *
       *  @returns AmiParameterList describing return values for this method.
       ************************************************************************/
       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();

         theReturns.addElement( new AmiParameter( "Color",
                                "One of the permitted colors", "" ) );
         theReturns.addElement( new AmiParameter( "Count",
                                "Number of times this color has been assigned",
                                0 ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodGetColorCount.onInvoke
      //*
      /** This method processes invocations of the getColorAssignmentCount AMI
       *  method.
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
         {
           Hashtable   theCounts = mSpot.getAssignmentCount();
           Enumeration theKeys   = theCounts.keys();

           theValues = new AmiParameterList();

           while( theKeys.hasMoreElements() )
           {
             String  theKey   = (String)theKeys.nextElement();
             Integer theCount = (Integer)theCounts.get(theKey);

             theValues.addElement(
               new AmiParameter( "Color", theKey   ));
             theValues.addElement(
               new AmiParameter( "Count", theCount ));
           }
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );
       }
    }
   //***************************************************************************
   //* Inner Class: methodOnAngleChange
   //*
   /** This class implements the SpotAmi onAngleChange AMI method.
    ***************************************************************************/
    class methodOnAngleChange extends AmiAsyncMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodOnAngleChange.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodOnAngleChange().
       ************************************************************************/
       public methodOnAngleChange()
       {
         super( "onAngleChange",
                "This is an asynchronous method which returns the new angle " +
                "of trajectory of the ball whenever it changes.",
                AmiConstants.METHOD_TYPE_INFO );
       }
      //************************************************************************
      //* Method: methodOnAngleChange.getReturns
      //*
      /** This method returns an AMIParameterList describing the values returned
       *  by the getAngle AMI method.
       *
       *  @returns AmiParameterList describing return values for this method.
       ************************************************************************/
       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "Angle",
                   "The angle to be set in the range 0 to 360 degrees.", 0 ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodOnAngleChange.onInvoke
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
             new AmiParameter( "Angle", new Integer(mSpot.getAngle()) ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );
       }
    }
   //***************************************************************************
   //* Inner Class: methodOnColorChange
   //*
   /** This class implements the SpotAmi getColor AMI method.
    ***************************************************************************/
    class methodOnColorChange extends AmiAsyncMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }

      //************************************************************************
      //* Method: methodOnColorChange.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodOnColorChange().
       ************************************************************************/
       public methodOnColorChange()
       {
         super( "onColorChange",
                "This is an asynchronous method which returns the new color " +
                "of the ball whenever it changes.",
                AmiConstants.METHOD_TYPE_INFO );
       }
      //************************************************************************
      //* Method: methodOnColorChange.getReturns
      //*
      /** This method returns an AMIParameterList describing the values returned
       *  by the getColor AMI method.
       *
       *  @returns AmiParameterList describing return values for this method.
       ************************************************************************/
       public AmiParameterList getReturns()
       {
         AmiParameterList theReturns = new AmiParameterList();
         theReturns.addElement( new AmiParameter( "Color",
                                "The current color of the ball.", "" ) );
         return( theReturns );
       }
      //************************************************************************
      //* Method: methodOnColorChange.onInvoke
      //*
      /** This method processes invocations of the getColor AMI method.
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
             new AmiParameter( "Color", mSpot.getColorByName() ));
         }
         catch ( Exception caughtException )
         { throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                    caughtException.getMessage() ));
         }
         return( theValues );
       }
    }
   //***************************************************************************
   //* Inner Class: methodTimeOutTest
   //*
   /** This class implements the SpotAmi timeOutTest AMI method.
    ***************************************************************************/
    class methodTimeOutTest extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }
      /** This AMI method returns no values.*/
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodTimeOutTest.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodTimeOutTest().
       ************************************************************************/
       public methodTimeOutTest()
       { super( "timeOutTest",
                "This method intentionally takes too long to return and should " +
                "result in a time out.", AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodTimeOutTest.onInvoke
      //*
      /** This method processes invocations of the timeOutTest AMI method.
       *
       *  @param inParms Parameters for method invocation.
       *  @returns AmiParameterList of return values for this method.
       ************************************************************************/
       public AmiParameterList onInvoke(
         AmiParameterList inParms )
         throws AmiException
       {
         try
         { Thread.sleep( 60000 );
         }
         catch( InterruptedException caughtException ) {}

         return( null );
       }
    }
   //***************************************************************************
   //* Inner Class: methodExceptionTest
   //*
   /** This class implements the SpotAmi exceptionTest AMI method.
    ***************************************************************************/
    class methodExceptionTest extends AmiMethod
    {
      /** This AMI method takes no arguments.*/
      public AmiParameterList getArguments(){ return( null ); }
      /** This AMI method returns no values.*/
      public AmiParameterList getReturns()  { return( null ); }

      //************************************************************************
      //* Method: methodExceptionTest.<init>
      //*
      /** Constructs an instance of the SpotAmi AMI method methodExceptionTest().
       ************************************************************************/
       public methodExceptionTest()
       {
         super( "exceptionTest",
                "This method throws an exception to test exception handling " +
                "in AMI. One should receive an AMI popup message window " +
                "describing the exception.", AmiConstants.METHOD_TYPE_ACTION );
       }
      //************************************************************************
      //* Method: methodExceptionTest.onInvoke
      //*
      /** This method processes invocations of the exceptionTest AMI method.
       *
       *  @param inParms Parameters for method invocation.
       *  @returns AmiParameterList of return values for this method.
       ************************************************************************/
       public AmiParameterList onInvoke(
         AmiParameterList inParms )
         throws AmiException
       {
         throw( new AmiException( AmiErrors.AMI_REPLY_ERR,
                                  "This is a test exception" ) );
       }
    }
 }
