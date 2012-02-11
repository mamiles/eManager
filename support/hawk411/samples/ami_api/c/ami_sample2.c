/*******************************************************************************
** File:    ami_sample2.c
**
** Purpose: This is a TIBCO Hawk AMI C API sample program to demonstrate how to
**          instrument a C application for TIBCO Hawk monitoring and management.
**
**          This sample shows how to AMI instrument a Rendezvous application.
**          The users application creates a Rendezvous transport and queue
**          and is responsible for dispatching that queue. The users application
**          passes that transport and queue to the ami_SessionCreate function
**          allowing the AMI C API to add an additional listener to that
**          transport and queue for processing AMI protocol messages. The users
**          application then calls the ami_SessionAnnounce function to
**          establish the AMI session. It is now up to the users application to
**          dispatch the queue which now contains AMI related messages as well.
**
**          The users application must call the ami_SessionStop function to
**          terminate the AMI session. The transport and queue are unaffected
**          by this call. The AMI C API simply removes its listeners from the
**          queue and performs the AMI stop protocol. The AMI session can be
**          announced and stopped repeatedly as the users application demands.
**
**          The users application is free to be single or multiple threaded.
**          This simply depends on how many threads are assigned to dispatch
**          the queue specified in the ami_SessionCreate function. These threads
**          are the only threads that will call the AMI C API user callback
**          functions. In other words, the users application will never be
**          called by the AMI API on a thread the users application did not
**          explicitly create. Therefore, to be single threaded the users
**          application should only use a single thread to dispatch. To be
**          multi-threaded create more than one thread. The users application
**          is responsible for synchronizing access to user application data
**          in the multi-threaded case. The AMI C API is fully multi-threaded
**          and supports single and multi threaded applications.
**
**          As always, the AMI API uses multiple-threads under the covers to
**          improve performance and respond to heartbeat, discovery, and other
**          AMI protocol request in a timely manner. The users application does
**          not need to concern itself with these additional threads and will
**          never be called back on any of these threads.
**
**          Copyright 2002 by TIBCO Software Inc. All rights reserved.
*******************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <malloc.h>
#include <string.h>

/*==============================================================================
**                        Required TIBCO Product Headers
**============================================================================*/

#include <ami.h>
#include <tibrv/tibrv.h>

/*==============================================================================
**                                Constants
**============================================================================*/

/* Rendezvous transport initialization.*/
#define APP_RV_SERVICE  "7474"
#define APP_RV_NETWORK  ""
#define APP_RV_DAEMON   "tcp:7474"

/*==============================================================================
**                        Internal Function Prototypes
**============================================================================*/

int       main               ( int, char ** );
int       HaveAmiError       ( ami_Error );
ami_Error AmiErrorFromRvError( tibrv_status, char * );
void      TraceHandler       ( ami_Session, ami_TraceCode, int, const char *,
                               void * );
ami_Error CreateMethods      ( ami_Session );
ami_Error SetMethodCallback  ( ami_Session, ami_Method, ami_Subscription, void *,
                               ami_ParameterList, ami_ParameterListList * );
ami_Error GetMethodCallback  ( ami_Session, ami_Method, ami_Subscription, void *,
                               ami_ParameterList, ami_ParameterListList * );
ami_Error StopMethodCallback ( ami_Session, ami_Method, ami_Subscription, void *,
                               ami_ParameterList, ami_ParameterListList * );

/*==============================================================================
**                                 Locals
**============================================================================*/

/* Define an unsigned 64-bit integer for AMI sample. */
#if   defined(_WIN32) || defined(__vms)
#define SAMPLE_U64 unsigned __int64
#define U64_VALUE 0xFFFFFFFFFFFFFFFF
#elif defined(__alpha)
#define SAMPLE_U64 unsigned long
#define U64_VALUE 0xFFFFFFFFFFFFFFFFUL
#else
#define SAMPLE_U64 unsigned long long
#define U64_VALUE 0xFFFFFFFFFFFFFFFFULL
#endif

/* The values of these variables are assigned and retrieved by the AMI methods
   implemented in this sample application.*/
int         sInteger;
double      sDouble;
char        sString[64];
ami_Boolean sBoolean;
SAMPLE_U64  sU64;

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
  ami_Session    AmiSession;          /* AMI session handle.*/
  tibrvTransport RvTransport;         /* RV transport handle.*/
  tibrvQueue     RvQueue;             /* RV queue handle.*/

  tibrv_status   RvError  = TIBRV_OK; /* RV return code.*/
  ami_Error      AmiError = AMI_OK;   /* AMI C API return code.*/
  int            RC = 0;              /* Main return code.*/

  /* Initialize application data.*/
  sInteger = 1;
  sDouble  = 1.1;
  sBoolean = AMI_TRUE;
  sU64 = U64_VALUE;
  strncpy( sString, "string", sizeof(sString) );
  sString[sizeof(sString)-1] = '\0';

  /* Initialize AMI C API.*/
  AmiError = ami_Open();
  if ( !(RC = HaveAmiError(AmiError)) )
  {
    /* Create Rendezvous transport.*/
    RvError = tibrvTransport_Create( &RvTransport, APP_RV_SERVICE,
                                     APP_RV_NETWORK, APP_RV_DAEMON );
    AmiError = AmiErrorFromRvError( RvError, "tibrvTransport_Create" );
    if ( !(RC = HaveAmiError(AmiError)) )
    {
      /* Create Rendezvous queue. The queue passed to the ami_SessionCreate can
         be any valid RV queue with any number of user application listeners
         and dispatch threads. Keep in mind that with multiple dispatch threads
         the user application must synchronize access to application data.*/
      RvError = tibrvQueue_Create( &RvQueue );
      AmiError = AmiErrorFromRvError( RvError, "tibrvQueue_Create" );
      if ( !(RC = HaveAmiError(AmiError)) )
      {
        /* Create an AMI Session. Pass RV queue handle as user data for stop
           AMI method.*/
        AmiError = ami_SessionCreate( &AmiSession,
                                      (ami_TraceCode)(AMI_INFO  | AMI_WARNING |
                                      AMI_ERROR ),
                                      APP_RV_SERVICE, APP_RV_NETWORK, APP_RV_DAEMON,
                                      RvTransport, RvQueue,
                                      "com.tibco.hawk.ami_api.c.ami_sample2",
                                      "AmiSample2",
                                      "TIBCO Hawk AMI C API Sample Program 2",
                                      TraceHandler, (void *)&RvQueue );
        if ( !(RC = HaveAmiError(AmiError)) )
        {
          /* Create application specific AMI methods.*/
          AmiError = CreateMethods( AmiSession );
          if ( !(RC = HaveAmiError(AmiError)) )
          {
            /* Announce AMI session.*/
            AmiError = ami_SessionAnnounce( AmiSession );
            if ( !(RC = HaveAmiError(AmiError)) )
            {
              /* Dispatch Rendezvous queue.*/
              while( RvError == TIBRV_OK )
                RvError = tibrvQueue_Dispatch( RvQueue );
            }
          }

          /* Destroy AMI session (log any errors). */
          AmiError = ami_SessionDestroy( AmiSession );
          HaveAmiError(AmiError);
        }

        /* Destroy RV queue (log any errors). */
        if ( RvQueue != (tibrvQueue)0 )
        {
          RvError = tibrvQueue_Destroy( RvQueue );
          AmiError = AmiErrorFromRvError( RvError, "tibrvQueue_Destroy" );
          HaveAmiError(AmiError);
        }
      }

      /* Destroy RV transport (log any errors). */
      RvError = tibrvTransport_Destroy( RvTransport );
      AmiError = AmiErrorFromRvError( RvError, "tibrvTransport_Destroy" );
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
    AmiError = ami_MethodCreate( AmiSession, &AmiMethod, "getData", AMI_METHOD_INFO,
                                 "This method demonstrates how AMI methods can "
                                 "retrieve data from an instrumented application.",
                                 AMI_METHOD_DEFAULT_TIMEOUT, GetMethodCallback,
                                 (void *)0 );
    if ( AmiError != AMI_OK ) break;

    /* Create AMI API returns for get (INFO) method.*/
    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                      "String", AMI_STRING, "This is a string return.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                      "Integer", AMI_I32, "This is an integer return.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                      "Double", AMI_F64, "This is a double return.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                      "Boolean", AMI_BOOLEAN, "This is a boolean return.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                      "U64", AMI_U64, "This is an unsigned 64 bit integer return.");

    /* Create AMI API set (ACTION) method.*/
    AmiError = ami_MethodCreate( AmiSession, &AmiMethod, "setData", AMI_METHOD_ACTION,
                                 "This method demonstrates how AMI methods can "
                                 "modify data in an instrumented application.",
                                 AMI_METHOD_DEFAULT_TIMEOUT, SetMethodCallback,
                                 (void *)0 );
    if ( AmiError != AMI_OK ) break;

    /* Create AMI API arguments for set (ACTION) method.*/
    AmiError = ami_ParameterCreateIn( AmiSession, AmiMethod, &AmiPrameter,
                                      "String", AMI_STRING, "This is a string argument.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateIn( AmiSession, AmiMethod, &AmiPrameter,
                                      "Integer", AMI_I32, "This is an integer argument.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateIn( AmiSession, AmiMethod, &AmiPrameter,
                                      "Double", AMI_F64, "This is a double argument.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateIn( AmiSession, AmiMethod, &AmiPrameter,
                                      "Boolean", AMI_BOOLEAN, "This is a boolean argument.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateIn( AmiSession, AmiMethod, &AmiPrameter,
                                      "U64", AMI_U64, "This is an unsigned 64 bit integer argument.");

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
** Function: SetMethodCallback
**
** Purpose:  This is the OnInvoke() callback function for the sample set method.
**
** Returns:  Success: AMI_OK
**           Failure: ami_Error describing error encountered.
**============================================================================*/
ami_Error SetMethodCallback(
  ami_Session             inAmiSession,   /* Handle of AMI session.*/
  ami_Method              inAmiMethod,    /* Handle of AMI method.*/
  ami_Subscription        inAmiSubscription, /* Async. method subscription.*/
  void *                  inpUserData,    /* User data associated with method.*/
  ami_ParameterList       inArguments,    /* Handle of AMI input parameter list.*/
  ami_ParameterListList * inpReturns )    /* Target for method return values.*/
{
  int                     aInteger;       /* Temp storage for argument value.*/
  double                  aDouble;        /* Temp storage for argument value.*/
  char *                  aString;        /* Temp storage for argument value.*/
  ami_Boolean             aBoolean;       /* Temp storage for argument value.*/
  SAMPLE_U64              aU64;           /* Temp storage for argument value.*/

  ami_Error               AmiError=AMI_OK;/* ami_Error return value.*/

  /* Break out of loop on error. No values are set unless all values can be set.*/
  do
  {
    AmiError = ami_ParameterGetValue( inAmiSession, inAmiMethod, inArguments,
                                      "String",  &aString );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterGetValue( inAmiSession, inAmiMethod, inArguments,
                                      "Integer", &aInteger );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterGetValue( inAmiSession, inAmiMethod, inArguments,
                                      "Double",  &aDouble );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterGetValue( inAmiSession, inAmiMethod, inArguments,
                                      "Boolean", &aBoolean );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterGetValue( inAmiSession, inAmiMethod, inArguments,
                                      "U64", &aU64 );
    if ( AmiError != AMI_OK ) break;

    /* Success store new values.*/
    sInteger = aInteger;
    sDouble  = aDouble;
    sBoolean = aBoolean;
    sU64     = aU64;
    strncpy( sString, aString, sizeof(sString) );
    sString[sizeof(sString)-1] = '\0';

  } while( 0 );

  return( AmiError );
}

/*==============================================================================
** Function: GetMethodCallback
**
** Purpose:  This is the OnInvoke() callback function for the sample get method.
**
** Returns:  Success: AMI_OK
**           Failure: ami_Error describing error encountered.
**============================================================================*/
ami_Error GetMethodCallback(
  ami_Session             inAmiSession,   /* Handle of AMI session.*/
  ami_Method              inAmiMethod,    /* Handle of AMI method.*/
  ami_Subscription        inAmiSubscription, /* Async. method subscription.*/
  void *                  inpUserData,    /* User data associated with method.*/
  ami_ParameterList       inArguments,    /* Handle of AMI input parameter list.*/
  ami_ParameterListList * inpReturns )    /* Target for method return values.*/
{
  ami_ParameterList       Returns;        /* Handle of AMI return parameter list.*/
  ami_Error               AmiError=AMI_OK;/* AMI C API return code. */

  /* Break out of loop on error.*/
  do
  {
    /* Get AMI parmeter list for return values.*/
    AmiError = ami_ParameterListOut ( inAmiSession, inAmiMethod, inpReturns,
                                      &Returns );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "String",  sString );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "Integer", &sInteger );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "Double",  &sDouble );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "Boolean", &sBoolean );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "U64", &sU64 );
    if ( AmiError != AMI_OK ) break;

  } while( 0 );

  return( AmiError );
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
  ami_Session             inAmiSession,    /* Handle of AMI session.*/
  ami_Method              inAmiMethod,     /* Handle of AMI method.*/
  ami_Subscription        inAmiSubscription, /* Async. method subscription.*/
  void *                  inpUserData,     /* User data associated with method.*/
  ami_ParameterList       inArguments,     /* Handle of AMI input parameter list.*/
  ami_ParameterListList * inpReturns )     /* Target for method return values.*/
{
  tibrvQueue *            pRvQueue;        /* Pointer to RV queue handle.*/
  tibrvQueue              RvQueue;         /* Pointer to RV queue handle.*/
  tibrv_status            RvError=TIBRV_OK;/* RV return code.*/
  ami_Error               AmiError=AMI_OK; /* ami_Error return value.*/

  /* Get RV queue associated with AMI session.*/
  AmiError = ami_SessionGetUserData( inAmiSession, (void **)&pRvQueue );
  if ( AmiError == AMI_OK )
  {
    /* Stop the AMI session.*/
    AmiError = ami_SessionStop( inAmiSession );
    if ( AmiError == AMI_OK )
    {
      /* Destroy RV queue to terminate dispatching.*/
      RvQueue   = *pRvQueue;
      *pRvQueue = (tibrvQueue)0;
      RvError = tibrvQueue_Destroy( RvQueue );
      AmiError = AmiErrorFromRvError( RvError, "tibrvQueue_Destroy" );
    }
  }

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
             ami_ErrorGetCode  (inAmiError),ami_ErrorGetThread(inAmiError),
             ami_ErrorGetText  (inAmiError),ami_ErrorGetFile  (inAmiError),
             ami_ErrorGetLine  (inAmiError) );

    /* Extract the error code for use as the return code.*/
    RC = ami_ErrorGetCode(inAmiError);

    /* Destroy the AMI error.*/
    ami_ErrorDestroy( inAmiError );
  }

  return( RC );
}
/*==============================================================================
** Function: AmiErrorFromRvError
**
** Purpose:  This function returns an ami_Error for the specified RV status.
**
** Returns:  AMI_OK    - Rendesvous error has NOT occurred.
**           ami_Error - Rendesvous error has     occurred.
**============================================================================*/
ami_Error AmiErrorFromRvError(
  tibrv_status inRvStatus,     /* Rendezvous status.*/
  char *       inFunction )    /* Rendezvous function returning status.*/
{
  ami_Error    AmiError=AMI_OK;/* ami_Error return value.*/

  if ( inRvStatus != TIBRV_OK )
  { AmiError = ami_ErrorCreate( AMI_RV_ERROR,
                                "Rendezvous function %s returned error %d: %s",
                                inFunction, inRvStatus,
                                tibrvStatus_GetText(inRvStatus) );
  }
  return( AmiError );
}

