/*******************************************************************************
** File:    ami_sample4.c
**
** Purpose: This is a TIBCO Hawk AMI C API sample program to demonstrate how to
**          instrument a C application for TIBCO Hawk monitoring and management.
**
**          This sample shows how to create an asynchronous AMI method for a
**          synchronous data source. This technique is used when synchronous
**          data needs to be polled at a certain rate, possibly calculations
**          performed on the data across samples, and the results returned at
**          that rate or another rate.
**
**          This technique makes use of the AMI C API auto-invoke feature. When
**          an ami_OnStartCallback is invoked to start a subscription to an
**          asynchronous method the application can specify auto-invoke for
**          that subscription specifying an interval in seconds. The AMI C API
**          will automatically call the ami_OnInvokeCallback for that
**          subscription at the specified interval.
**
**          This allows the ami_OnInvokeCallback to poll the data, peform any
**          calculations, and return the results. Because the
**          ami_OnInvokeCallback is free to return data or not, the results can
**          be returned at a sample rate (multiple of) other than the specified
**          auto-callback rate.
**
**          This technique can also be used for asynchronous data sources where
**          the data is to be sampled over an interval other than the rate of
**          the asynchronous data source.
**
**          Copyright 2002 by TIBCO Software Inc. All rights reserved.
*******************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <malloc.h>
#include <string.h>
#include <time.h>

/*==============================================================================
**                      Required TIBCO Product Headers
**============================================================================*/

#include <ami.h>

/*==============================================================================
**                                Constants
**============================================================================*/

#define APP_RV_SERVICE  "7474"
#define APP_RV_NETWORK  ""
#define APP_RV_DAEMON   "tcp:7474"

/*==============================================================================
**                        Internal Function Prototypes
**============================================================================*/

int       main              ( int, char ** );
int       HaveAmiError      ( ami_Error );
void      TraceHandler      ( ami_Session, ami_TraceCode, int, const char *,
                              void * );
ami_Error CreateMethods     ( ami_Session );
ami_Error OnInvokeCallback  ( ami_Session, ami_Method, ami_Subscription, void *,
                              ami_ParameterList, ami_ParameterListList * );
ami_Error OnStartCallback   ( ami_Session, ami_Method, void *, ami_Subscription,
                              ami_ParameterList );
void      OnStopCallback    ( ami_Session, ami_Method, void *, ami_Subscription );
ami_Error StopMethodCallback( ami_Session, ami_Method, ami_Subscription, void *,
                              ami_ParameterList, ami_ParameterListList * );

/*==============================================================================
**                                 Locals
**============================================================================*/

/* We will simulate a synchronous data source by polling the time and
   calculating average seconds per interval. In real life this might be some
   application performance statistics or cpu usage.*/

struct _APPDATA_ {      /* Structure for subscription specifiec data.*/
  int     Interval;     /* - Subscription interval. */
  time_t  Time;         /* - Time of last sample.   */
  long    ElapsedTime;  /* - Total elapsed time.    */
  long    Samples;      /* - Number of samples.     */
};
typedef struct _APPDATA_    APPDATA;
typedef struct _APPDATA_ * PAPPDATA;

/*==============================================================================
** Function: main
**
** Purpose:  This is the main entry point of the TIBCO Hawk AMI C API sample
**           program.
**
** Returns:  Success - zero.
**           Failure - nonzero.
**============================================================================*/
int main( int argc, char **argv )
{
  ami_Session AmiSession;         /* AMI session handle.*/

  ami_Error   AmiError = AMI_OK;  /* AMI C API return code. */
  int         RC = 0;             /* Main return code. */

  /* Initialize AMI C API.*/
  AmiError = ami_Open();
  if ( !(RC = HaveAmiError(AmiError)) )
  {
    /* Create an AMI Session. Note RV transport and queue unspecified. */
    AmiError = ami_SessionCreate( &AmiSession,
                                  (ami_TraceCode)(AMI_INFO  | AMI_WARNING |
                                  AMI_ERROR ),
                                  APP_RV_SERVICE, APP_RV_NETWORK, APP_RV_DAEMON,
                                  0, 0,
                                  "com.tibco.hawk.ami_api.c.ami_sample4",
                                  "AmiSample4",
                                  "TIBCO Hawk AMI C API Sample Program 4",
                                  TraceHandler, (void *)0 );
    if ( !(RC = HaveAmiError(AmiError)) )
    {
      /* Create application specific AMI methods.*/
      AmiError = CreateMethods( AmiSession );
      if ( !(RC = HaveAmiError(AmiError)) )
      {
        /* Announce AMI session. Because no Rendezvous queue was specified on
           the ami_SessionCreate the AMI C API will create one automatically
           and the ami_SessionAnnounce will perform the dispatching of this
           queue. In this scenario ami_SessionAnnounce will block until
           ami_SessionStop is called. */
           AmiError = ami_SessionAnnounce( AmiSession );
           HaveAmiError(AmiError);
      }

      /* Destroy AMI session (log any errors). */
      AmiError = ami_SessionDestroy( AmiSession );
      HaveAmiError(AmiError);
    }

    /* Close AMI C API (log any errors). */
    AmiError = ami_Close();
    HaveAmiError(AmiError);
  }

  return( RC );
}

/*==============================================================================
** Function: CreateMethods
**
** Purpose:  Adds sample AMI methods to the specified AMI session.
**
** Returns:  Success: AMI_OK
**           Failure: ami_Error describing error encountered.
**============================================================================*/
ami_Error CreateMethods(
  ami_Session   AmiSession )      /* AMI session handle.*/
{
  ami_Method    AmiMethod;        /* AMI method handle.*/
  ami_Parameter AmiPrameter;      /* AMI parameter handle.*/

  ami_Error     AmiError=AMI_OK;  /* ami_Error return value.*/

  /* Break out of loop on error. ami_SessionDestroy will destroy anything that
     was successfully created here (i.e. ami_SessionDestroy can cleanup an
     ami_Session at any point of initialization or lack there off).*/
  do
  {
    /* Create AMI API get (INFO) method.*/
    AmiError = ami_AsyncMethodCreate( AmiSession, &AmiMethod, "onData", AMI_METHOD_INFO,
                                      "This method demonstrates how AMI methods can "
                                      "be asynchronous.",
                                      AMI_METHOD_DEFAULT_TIMEOUT,
                                      OnInvokeCallback,
                                      OnStartCallback,
                                      OnStopCallback,
                                      (void *)0 );
    if ( AmiError != AMI_OK ) break;

    /* Create AMI API arguments for asynchronous (INFO) method.*/
    AmiError = ami_ParameterCreateIn ( AmiSession, AmiMethod, &AmiPrameter,
                                       "Interval", AMI_I32, "This is the data sampling rate.");
    if ( AmiError != AMI_OK ) break;

    /* Create AMI API returns for asynchronous (INFO) method.*/
    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                       "Sample Time", AMI_I32, "Time of sample.");
    if ( AmiError != AMI_OK ) break;
    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                       "Last Sample Time", AMI_I32, "Time of last sample.");
    if ( AmiError != AMI_OK ) break;
    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                       "Average Time", AMI_I32, "Average seconds per interval.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                       "Elapsed Time", AMI_I32, "Total elapsed seconds.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                       "Total Samples", AMI_I32, "Total samples.");
    if ( AmiError != AMI_OK ) break;

    /* Create AMI API stop (ACTION) method.*/
    AmiError = ami_MethodCreate( AmiSession, &AmiMethod, "stop", AMI_METHOD_ACTION,
                                 "This method demonstrates how an AMI method can "
                                 "shutdown the AMI application.",
                                 AMI_METHOD_DEFAULT_TIMEOUT, StopMethodCallback,
                                 (void *)0 );
    if ( AmiError != AMI_OK ) break;

  } while( 0 );

  return(AmiError);
}

/*==============================================================================
** Function: OnInvokeCallback
**
** Purpose:  This is the OnInvoke() callback function for the sample
**           asynchronous method.
**
** Returns:  Success: AMI_OK
**           Failure: ami_Error describing error encountered.
**============================================================================*/
ami_Error OnInvokeCallback(
  ami_Session             inAmiSession,   /* Handle of AMI session.*/
  ami_Method              inAmiMethod,    /* Handle of AMI method.*/
  ami_Subscription        inAmiSubscription, /* Async. method subscription.*/
  void *                  inpUserData,    /* User data associated with method.*/
  ami_ParameterList       inArguments,    /* Handle of AMI input parameter list.*/
  ami_ParameterListList * inpReturns )    /* Target for method return values.*/
{
  PAPPDATA                pAPPDATA;       /* Subscription specific app. data.*/
  long                    AverageTime;    /* Average elapsed time per interval.*/
  time_t                  CurrentTime;    /* Time at this invocation.*/
  time_t                  LastTime;       /* Time at last invocation.*/
  ami_ParameterList       Returns;        /* Handle of AMI return parameter list.*/
  ami_Error               AmiError=AMI_OK;/* AMI C API return code. */

  /* Break out of loop on error.*/
  do
  {
    /* If the subscription handle is NULL then the method was synchronously
       invoked. You can return data (or not) in this case if it makes sense
       for your application (e.g. return the last computed values). For this
       sample we are not going to allow synchronous invocation. */
    if ( inAmiSubscription == (ami_Subscription)0 )
    {
      AmiError = ami_ErrorCreate( 102,
            "This asynchronous method does not allow synchronous invocation." );
      break;
    }

    /* Get current time.*/
    CurrentTime = time(0);

    /* Get subscription specific application data.*/
    AmiError = ami_SubscriptionGetUserData( inAmiSession, inAmiSubscription,
                                            (void **)&pAPPDATA );
    if ( AmiError != AMI_OK ) break;

    /* Use first invocation to synchronize with the AMI C API timer which is
       on a 5 second interval (i.e. First invocation could be plus or minus 5
       seconds). By synchronizing with the AMI C API timer and using auto-
       callback intervals which are multiples of the AMI C API timer interval
       (5) we are assuring all samples use an accurate interval. On first
       invocation we do not return data (because it would be based on an
       inaccurate interval).

       NOTE: This mechanism is only necessary if your data calculations require
             all intervals to be accurate including the first.
    */
    if ( pAPPDATA->Time == (time_t)0 )
    {
      /* Reset the auto-callback interval to the requested value.*/
      AmiError = ami_SubscriptionSetCallbackInterval( inAmiSession,
                                                      inAmiSubscription,
                                                      pAPPDATA->Interval );
      if ( AmiError == AMI_OK )
      {
        /* Prime the application data. */
        pAPPDATA->Time = CurrentTime;
      }
    }

    /* Second and subsequent invocations are syncronized with AMI C API timer
       so interval is accurate. We will compute and return data.*/
    else
    {
      /* Update application data and compute average. */
      LastTime               = pAPPDATA->Time;
      pAPPDATA->ElapsedTime += CurrentTime - LastTime;
      pAPPDATA->Time         = CurrentTime;
      pAPPDATA->Samples     += 1;
      AverageTime            = pAPPDATA->ElapsedTime / pAPPDATA->Samples;

      /* Get AMI parmeter list for return values.*/
      AmiError = ami_ParameterListOut ( inAmiSession, inAmiMethod, inpReturns,
                                        &Returns );
      if ( AmiError != AMI_OK ) break;

      /* Set current values for return values.*/
      AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                        "Sample Time", &pAPPDATA->Time );
      if ( AmiError != AMI_OK ) break;

      AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                        "Last Sample Time", &LastTime );
      if ( AmiError != AMI_OK ) break;

      AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                        "Average Time", &AverageTime );
      if ( AmiError != AMI_OK ) break;

      AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                        "Elapsed Time", &pAPPDATA->ElapsedTime );
      if ( AmiError != AMI_OK ) break;

      AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                        "Total Samples", &pAPPDATA->Samples );
      if ( AmiError != AMI_OK ) break;
    }

  } while( 0 );

  return( AmiError );
}
/*==============================================================================
** Function: OnStartCallback
**
** Purpose:  This is the ami_OnStartCallback() callback function for the sample
**           asynchronous method.
**
** Returns:  Success: AMI_OK
**           Failure: ami_Error describing error encountered.
**============================================================================*/
ami_Error OnStartCallback(
  ami_Session       inAmiSession,      /* Handle of AMI session.*/
  ami_Method        inAmiMethod,       /* Handle of AMI method.*/
  void *            inpUserData,       /* User data associated with method.*/
  ami_Subscription  inAmiSubscription, /* Async. method subscription.*/
  ami_ParameterList inArguments )      /* Handle of AMI input parameter list.*/
{
  int               Interval;          /* Interval for auto-invoke.*/
  PAPPDATA          pAPPDATA;          /* Subscription specific app. data.*/
  ami_Error         AmiError=AMI_OK;   /* AMI C API return code.*/

  /* Allocate subscription specific app. data.*/
  pAPPDATA = (PAPPDATA)calloc( 1, sizeof(APPDATA) );
  if ( pAPPDATA == (PAPPDATA)0 )
  {
    AmiError = ami_ErrorCreate( 101, "Insufficient memory to start subscription." );
  }
  else
  {
    /* Get specified interval.*/
    AmiError = ami_ParameterGetValue( inAmiSession, inAmiMethod, inArguments,
                                      "Interval",  &Interval );
    if ( AmiError == AMI_OK )
    {
      /* Set auto-callback interval. We set an interval of one second to
         force an immediate auto-invocation. This allows us to synchronize
         with the AMI C API timer which has a 5 second interval.

         NOTE: See further comments in OnInvokeCallback.
      */
      AmiError = ami_SubscriptionSetCallbackInterval( inAmiSession,
                                                      inAmiSubscription, 1 );
      if ( AmiError == AMI_OK )
      {
        /* Remember the true interval so it can be reset later.*/
        pAPPDATA->Interval = Interval;

        /* Attach subscription specific app. data to subscription.*/
        AmiError = ami_SubscriptionSetUserData( inAmiSession, inAmiSubscription,
                                                (void *)pAPPDATA );
        if ( AmiError == AMI_OK )
          return( AmiError );
      }
    }

    free( pAPPDATA );
  }

  return( AmiError );
}
/*==============================================================================
** Function: OnStopCallback
**
** Purpose:  This is the ami_OnStopCallback() callback function for the sample
**           asynchronous method.
**
** Returns:  Success: AMI_OK
**           Failure: ami_Error describing error encountered.
**============================================================================*/
void OnStopCallback(
  ami_Session       inAmiSession,       /* Handle of AMI session.*/
  ami_Method        inAmiMethod,        /* Handle of AMI method.*/
  void *            inpUserData,        /* User data associated with method.*/
  ami_Subscription  inAmiSubscription ) /* Async. method subscription.*/
{
  PAPPDATA          pAPPDATA;           /* Subscription specific app. data.*/
  ami_Error         AmiError=AMI_OK;    /* AMI C API return code.*/

  /* Free subscription specific application data.*/
  AmiError = ami_SubscriptionGetUserData( inAmiSession, inAmiSubscription,
                                          (void **)&pAPPDATA );
  if ( AmiError == AMI_OK )
    free( pAPPDATA );
}

/*==============================================================================
** Function: StopMethodCallback
**
** Purpose:  This is the OnInvoke() callback function for the sample stop method.
**
** Returns:  Success: AMI_OK
**           Failure: ami_Error describing error encountered.
**============================================================================*/
ami_Error StopMethodCallback(
  ami_Session             inAmiSession,   /* Handle of AMI session.*/
  ami_Method              inAmiMethod,    /* Handle of AMI method.*/
  ami_Subscription        inAmiSubscription, /* Async. method subscription.*/
  void *                  inpUserData,    /* User data associated with method.*/
  ami_ParameterList       inArguments,    /* Handle of AMI input parameter list.*/
  ami_ParameterListList * inpReturns )    /* Target for method return values.*/
{
  ami_Error               AmiError=AMI_OK;/* ami_Error return value.*/

  AmiError = ami_SessionStop( inAmiSession );

  return( AmiError );
}

/*==============================================================================
** Function: TraceHandler
**
** Purpose:  This function serves as the AMI C API trace handler callback. This
**           function is called by the AMI C API with trace information.
**
** Returns:  void
**============================================================================*/
 void TraceHandler(
   ami_Session   inAmiSession, /* AMI session reporting trace information.*/
   ami_TraceCode inTraceLevel, /* Trace level.*/
   int           inTraceID,    /* Unique ID of trace event.*/
   const char *  inText,       /* Textual description of trace event. */
   void *        inUserData )  /* User data associated with this AMI session */
 {
   const char *  pTraceLevel;  /* Trace level description string.*/

   switch( inTraceLevel )
   { case AMI_INFO   : pTraceLevel = "INFO   "; break;
     case AMI_WARNING: pTraceLevel = "WARNING"; break;
     case AMI_ERROR  : pTraceLevel = "ERROR  "; break;
     case AMI_DEBUG  : pTraceLevel = "DEBUG  "; break;
     case AMI_AMI    : pTraceLevel = "AMI    "; break;
     default:          pTraceLevel = "UNKNOWN"; break;
   }

   fprintf( stderr, "%s (%04d): %s\n", pTraceLevel, inTraceID, inText );
 }

/*==============================================================================
** Function: HaveAmiError
**
** Purpose:  This function determines if an AMI error has occurred. If an AMI
**           error has occurred this function logs the error to standard error,
**           destroys the AMI error, and returns the AMI error code.
**
** Returns:  TRUE  - AMI error has     occurred. Error code returned.
**           FALSE - AMI error has NOT occurred. No action taken.
**============================================================================*/
int HaveAmiError(
  ami_Error  inAmiError )  /* AMI error handle. */
{
  int        RC = 0;

  /* If we have an AMI error.*/
  if ( inAmiError != AMI_OK )
  {
    /* Log the error.*/
    fprintf( stderr, "ERROR   (%04d): [%08x] %s FILE:[%s] LINE[%d]\n",
             ami_ErrorGetCode(inAmiError), ami_ErrorGetThread(inAmiError),
             ami_ErrorGetText(inAmiError), ami_ErrorGetFile  (inAmiError),
             ami_ErrorGetLine(inAmiError) );

    /* Extract the error code for use as the return code.*/
    RC = ami_ErrorGetCode(inAmiError);

    /* Destroy the AMI error.*/
    ami_ErrorDestroy( inAmiError );
  }

  return( RC );
}
