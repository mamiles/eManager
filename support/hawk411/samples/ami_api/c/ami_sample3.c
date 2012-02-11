/*******************************************************************************
** File:    ami_sample3.c
**
** Purpose: This is a TIBCO Hawk AMI C API sample program to demonstrate how to
**          instrument a C application for TIBCO Hawk monitoring and management.
**
**          This sample is identical to ami_sample1.c except that it
**          demonstrates how to create methods that return tabular data.
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
**                            Tabular Data
**============================================================================*/
/* Define an unsigned 64-bit integer for AMI sample. */
#if   defined(_WIN32) || defined(__vms)
#define SAMPLE_U64 unsigned __int64
#elif defined(__alpha)
#define SAMPLE_U64 unsigned long
#else
#define SAMPLE_U64 unsigned long long
#endif

struct _DATA_ROW_
{
  struct _DATA_ROW_ * pNext;       /* Pointer to next row.   */
  char                aString[64]; /* Sample string  return. */
  int                 aInteger;    /* Sample integer return. */
  double              aDouble;     /* Sample double  return. */
  ami_Boolean         aBoolean;    /* Sample boolean return. */
  SAMPLE_U64          aU64;        /* Sample unsigned 64-bit integer return. */
};
typedef struct _DATA_ROW_    DATA_ROW;
typedef struct _DATA_ROW_ * PDATA_ROW;

/* Tabular data maintained by sample (single-threaded) program.*/
PDATA_ROW pTabularData = (PDATA_ROW)0;

/*==============================================================================
**                        Internal Function Prototypes
**============================================================================*/

int       main              ( int, char ** );
int       HaveAmiError      ( ami_Error );
void      TraceHandler      ( ami_Session, ami_TraceCode, int, const char *,
                              void * );
ami_Error CreateMethods     ( ami_Session );
ami_Error SetDataRow        ( int, double, ami_Boolean, char *, SAMPLE_U64 );

ami_Error SetMethodCallback ( ami_Session, ami_Method, ami_Subscription, void *,
                              ami_ParameterList, ami_ParameterListList * );
ami_Error GetMethodCallback ( ami_Session, ami_Method, ami_Subscription, void *,
                              ami_ParameterList, ami_ParameterListList * );
ami_Error StopMethodCallback( ami_Session, ami_Method, ami_Subscription, void *,
                              ami_ParameterList, ami_ParameterListList * );

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
                                  "com.tibco.hawk.ami_api.c.ami_sample3",
                                  "AmiSample3",
                                  "TIBCO Hawk AMI C API Sample Program 3",
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
      "This method demonstrates how AMI methods can retrieve tabular data from "
      "an instrumented application. The table of data returned is initially "
      "empty. Use the setData method to add to or update the table of data. "
      "The tabular data is indexed by the \"Integer\" column.",
      AMI_METHOD_DEFAULT_TIMEOUT, GetMethodCallback, (void *)0 );
    if ( AmiError != AMI_OK ) break;

    /* Tabular data will be indexed by the integer return.*/
    AmiError = ami_MethodSetIndex( AmiSession, AmiMethod, "Integer" );
    if ( AmiError != AMI_OK ) break;

    /* Create AMI API returns for tabular get (INFO) method.*/
    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                      "Integer", AMI_I32, "This is an integer return.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateOut( AmiSession, AmiMethod, &AmiPrameter,
                                      "String", AMI_STRING, "This is a string return.");
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
      "This method demonstrates how AMI methods can modify tabular data in an "
      "instrumented application. This method adds or updates a row in the "
      "table of data returned by the getData method. The tabular data is "
      "indexed by the \"Integer\" column.",
      AMI_METHOD_DEFAULT_TIMEOUT, SetMethodCallback, (void *)0 );
    if ( AmiError != AMI_OK ) break;

    /* Create AMI API arguments for set (ACTION) method.*/
    AmiError = ami_ParameterCreateIn( AmiSession, AmiMethod, &AmiPrameter,
                                      "Integer", AMI_I32, "This is an integer argument.");
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterCreateIn( AmiSession, AmiMethod, &AmiPrameter,
                                      "String", AMI_STRING, "This is a string argument.");
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
    AmiError = SetDataRow( aInteger, aDouble, aBoolean, aString, aU64 );
    if ( AmiError != AMI_OK ) break;

  } while( 0 );

  return( AmiError );
}

/*==============================================================================
** Function: SetDataRow
**
** Purpose:  This function adds/updates a row in the tabular data.
**
** Returns:  Success: AMI_OK
**           Failure: ami_Error describing error encountered.
**============================================================================*/
ami_Error SetDataRow(
  int         inInteger,       /* Integer value to set. */
  double      inDouble,        /* Double  value to set. */
  ami_Boolean inBoolean,       /* Boolean value to set. */
  char *      inString,        /* String  value to set. */
  SAMPLE_U64  inU64 )          /* Unsigned 64-bit integer value to set.*/
{
  PDATA_ROW   pLastDataRow;    /* Last    row of tabular data.*/
  PDATA_ROW   pNextDataRow;    /* Current row of tabular data.*/
  PDATA_ROW   pNewDataRow;     /* New     row of tabular data.*/

  ami_Error   AmiError=AMI_OK; /* ami_Error return value.*/

  /* Find appropriate slot to insert new row. If index value already
     exists then update row.*/
  pLastDataRow = (PDATA_ROW)0;
  pNextDataRow = pTabularData;

  while( 1 )
  {
    /* If hit end of table or hit a row with higher index then insert new row
       at this location. */
    if ( pNextDataRow == (PDATA_ROW)0 || inInteger < pNextDataRow->aInteger )
    {
      /* Allocate new row of data.*/
      pNewDataRow = (PDATA_ROW)calloc( 1, sizeof(DATA_ROW) );
      if ( pNewDataRow == (PDATA_ROW)0 )
      {
        AmiError = ami_ErrorCreate( 101, "Insufficient memory to add new row." );
        break;
      }

      /* Initialize new row of data.*/
      pNewDataRow->aInteger = inInteger;
      pNewDataRow->aDouble  = inDouble;
      pNewDataRow->aBoolean = inBoolean;
      pNewDataRow->aU64     = inU64;
      strncpy( pNewDataRow->aString, inString, sizeof(pNewDataRow->aString) );
      pNewDataRow->aString[sizeof(pNewDataRow->aString)-1] = '\0';

      /* add new row of data to table.*/
      if ( pLastDataRow == (PDATA_ROW)0 )
      {
        pNewDataRow->pNext  = pNextDataRow;
        pTabularData        = pNewDataRow;
      }
      else
      {
        pNewDataRow->pNext  = pLastDataRow->pNext;
        pLastDataRow->pNext = pNewDataRow;
      }

      break;
    }

    /* If specified index matches row index then update this row.*/
    else if ( inInteger == pNextDataRow->aInteger )
    {
      pNextDataRow->aDouble  = inDouble;
      pNextDataRow->aBoolean = inBoolean;
      pNextDataRow->aU64     = inU64;
      strncpy( pNextDataRow->aString, inString, sizeof(pNextDataRow->aString) );
      pNextDataRow->aString[sizeof(pNextDataRow->aString)-1] = '\0';

      break;
    }

    /* Go on to next row of tabular data.*/
    pLastDataRow = pNextDataRow;
    pNextDataRow = pNextDataRow->pNext;
  }

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
  PDATA_ROW               pDataRow;       /* Current row of tabular data.*/
  ami_ParameterList       Returns;        /* Handle of AMI return parameter list.*/
  ami_Error               AmiError=AMI_OK;/* AMI C API return code. */

  /* Return all rows of tabular data.*/
  pDataRow = pTabularData;
  while( pDataRow != (PDATA_ROW)0 )
  {
    /* Get AMI parmeter list for newt row of return values.*/
    AmiError = ami_ParameterListOut ( inAmiSession, inAmiMethod, inpReturns,
                                      &Returns );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "Integer", &pDataRow->aInteger );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "String",  pDataRow->aString );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "Double",  &pDataRow->aDouble );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "Boolean", &pDataRow->aBoolean );
    if ( AmiError != AMI_OK ) break;

    AmiError = ami_ParameterSetValue( inAmiSession, inAmiMethod, Returns,
                                      "U64", &pDataRow->aU64 );
    if ( AmiError != AMI_OK ) break;

    /* Go on to next row of tabular data.*/
    pDataRow = pDataRow->pNext;
  }

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
