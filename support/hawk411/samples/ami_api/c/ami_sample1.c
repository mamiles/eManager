/*******************************************************************************
** File:    ami_sample1.c
**
** Purpose: This is a TIBCO Hawk AMI C API sample program to demonstrate how to
**          instrument a C application for TIBCO Hawk monitoring and management.
**
**          This sample shows how to AMI instrument a non-Rendezvous application.
**          This method can be used with only a Hawk license. The AMI API does
**          all the Rendezvous work under the covers. The event dispatching
**          takes place in the ami_SessionAnnounce() function which, in this
**          scenario, is a blocking call. ami_SessionAnnounce() announces the
**          AMI session and dispatches all related Hawk Rendezvous messages.
**          This continues until ami_SessionStop() is called by the application
**          at which time the ami_SessionAnnounce() function will return.
**
**          This method limits the number of dispatching threads to the one
**          thread which calls ami_SessionAnnounce(). This is the only thread
**          which will call the user applications AMI API callback functions.
**          As a result, the users application can be single threaded.
**
**          As always, the AMI API uses multiple-threads under the covers to
**          improve performance and respond to heartbeat, discovery, and other
**          AMI protocol request in a timely manner. The users applcation does
**          not need to concern itself with these additional threads.
**
**          If the users application is to be multi-threaded then the code in
**          this sample would run on a dedicated thread. The users application
**          would be responsible for thread safety regarding any of its own
**          data structures. The AMI C API itself is multi-thread safe. The
**          ami_SessionStop() function could be called by any thread to end
**          the AMI session.
**
**          Copyright 2002 by TIBCO Software Inc. All rights reserved.
*******************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <malloc.h>
#include <string.h>

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
ami_Error SetMethodCallback ( ami_Session, ami_Method, ami_Subscription, void *,
                              ami_ParameterList, ami_ParameterListList * );
ami_Error GetMethodCallback ( ami_Session, ami_Method, ami_Subscription, void *,
                              ami_ParameterList, ami_ParameterListList * );
ami_Error StopMethodCallback( ami_Session, ami_Method, ami_Subscription, void *,
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
int              sInteger;
double           sDouble;
char             sString[64];
ami_Boolean      sBoolean;
SAMPLE_U64       sU64;
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
    /* Create an AMI Session. Note RV transport and queue unspecified. */
    AmiError = ami_SessionCreate( &AmiSession,
                                  (ami_TraceCode)(AMI_INFO  | AMI_WARNING |
                                  AMI_ERROR ),
                                  APP_RV_SERVICE, APP_RV_NETWORK, APP_RV_DAEMON,
                                  0, 0,
                                  "com.tibco.hawk.ami_api.c.ami_sample1",
                                  "AmiSample1",
                                  "TIBCO Hawk AMI C API Sample Program 1",
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
    if ( AmiError != AMI_OK ) break;

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
