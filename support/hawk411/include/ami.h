/*******************************************************************************
 * File:    ami.h
 *
 * Purpose: Contains the external definition of the TIB/Hawk AMI C API. This is
 *          the only include file required by AMI C API applications.
 *
 *        Copyright 2001 by TIBCO Software Inc. All rights reserved.
 ******************************************************************************/
#ifndef _INCLUDED_AMI_H
#define _INCLUDED_AMI_H

#include <string.h>
#include <stdlib.h>
#include <stdarg.h>

#if defined(__cplusplus)
extern "C" {
#endif

/*==============================================================================
 *                           AMI API Constants
 *============================================================================*/

/* The version of the AMI specification implemented by this version of the
   AMI C API.*/
#define AMI_VERSION  "1.3"

/* AMI method default timeout period specified in milliseconds. If the method
   takes longer than this time period the TIB/Hawk Agent will reflect a timeout
   error. For methods that require a longer timeout period this can be
   overridden by passing the timeout parameter through the ami_MethodCreate
   function. */
#define AMI_METHOD_DEFAULT_TIMEOUT  10000

/*==============================================================================
 *                           AMI API Error Codes
 *============================================================================*/

#define AMI_OK ((ami_Error)0)

typedef enum
{
  AMI_AMIERROR_FAILURE       =  1,
  AMI_INSUFFICIENT_MEMORY    =  2,
  AMI_INVALID_ERROR          =  3,
  AMI_CORRUPT_ERROR          =  4,
  AMI_MISSING_ARGUMENT       =  5,
  AMI_INVALID_ARGUMENT       =  6,
  AMI_INVALID_SESSION        =  7,
  AMI_CORRUPT_SESSION        =  8,
  AMI_INVALID_METHOD         =  9,
  AMI_CORRUPT_METHOD         = 10,
  AMI_INVALID_SUBSCRIPTION   = 11,
  AMI_CORRUPT_SUBSCRIPTION   = 12,
  AMI_INVALID_PARM_TABLE     = 13,
  AMI_CORRUPT_PARM_TABLE     = 14,
  AMI_INVALID_PARM_LIST      = 15,
  AMI_CORRUPT_PARM_LIST      = 16,
  AMI_INVALID_PARAMETER      = 17,
  AMI_CORRUPT_PARAMETER      = 18,
  AMI_RV_ERROR               = 19,
  AMI_UNKNOWN_INVOCATION     = 20,
  AMI_UNKNOWN_PARAMETER      = 21,
  AMI_LIST_ADD_FAILED        = 22,
  AMI_ARGUMENT_GET_FAILED    = 23,
  AMI_UNKNOWN_SUBSCRIPTION   = 24,
  AMI_SESSION_ANNOUNCED      = 25,
  AMI_SESSION_STOPPED        = 26
} ami_ErrorCode;

/*==============================================================================
 *                           AMI C API Data Types
 *============================================================================*/

typedef enum
{
  AMI_I32     =  3, /* 32-bit   signed integer.*/
  AMI_I64     =  4, /* 64-bit   signed integer.*/
  AMI_U64     =  8, /* 64-bit unsigned integer.*/
  AMI_F64     = 10, /* 64-bit floating-point number.*/
  AMI_STRING  = 11, /* Null-terminated character string (UTF8 encoding).*/
  AMI_BOOLEAN = 12  /* Boolean.*/
} ami_DataType;

/* For backward compatibility with previous AMI C API versions.*/
#define AMI_INTEGER AMI_I32
#define AMI_LONG    AMI_I64
#define AMI_REAL    AMI_F64

typedef enum
{
  AMI_FALSE = 0,
  AMI_TRUE  = 1
} ami_Boolean;

/*==============================================================================
 *                          Hawk AMI API Constants
 *============================================================================*/

/* Alert classifications for unsolicited messages. These directly relate to the
   TIB/Hawk alert classifications. */
typedef enum
{
  AMI_ALERT_INFO,
  AMI_ALERT_WARNING,
  AMI_ALERT_ERROR
} ami_AlertType;

/* AMI method types. INFO methods return data. ACTION methods perform an action.
   ACTION_INFO methods perform an action and also return data. */
typedef enum
{
  AMI_METHOD_INFO,
  AMI_METHOD_ACTION,
  AMI_METHOD_ACTION_INFO
} ami_MethodType;

/* AMI C API object handles.*/
typedef void * ami_Error;             /* AMI error handle.                 */
typedef void * ami_Session;           /* AMI session handle.               */
typedef void * ami_Method;            /* AMI method handle.                */
typedef void * ami_Parameter;         /* AMI parameter handle.             */
typedef void * ami_Subscription;      /* AMI subscription handle.          */
typedef void * ami_ParameterList;     /* AMI parameter list handle.        */
typedef void * ami_ParameterListList; /* AMI list of parameter list handle */

/*==============================================================================
 *                          AMI Error API Functions
 *============================================================================*/

/*------------------------------------------------------------------------------
 * Creates a new AMI error for the specified error code and descriptive text.
 * Descriptive text is specified as a template (printf format) and substitution
 * arguments. If error creation fails then an error describing this failure is
 * returned in place of the specified error.
 *
 * ami_Error's created by the caller and passed to the AMI C API, for example in
 * the ami_OnInvokeCallback function (see below), will be destroyed by the AMI
 * C API. ami_Error's created by the AMI C API and returned to the user must
 * be destroyed by the user with the ami_ErrorDestroy() function or memory will
 * be leaked.
 *----------------------------------------------------------------------------*/
 ami_Error ami_ErrorCreate(
   int           inErrorCode,    /* Error code.*/
   const char *  inpTemplate,    /* Error description template.*/
   ... );                        /* Error template substitution arguments.*/

 ami_Error ami_ErrorCreateV(
   int           inErrorCode,    /* Error code.*/
   const char *  inpTemplate,    /* Error description template.*/
   va_list       inArguments );  /* Error template substitution arguments.*/

/*------------------------------------------------------------------------------
 * Destroys the specified AMI error. Can be called with NULL error handles
 * which are ignored.
 *----------------------------------------------------------------------------*/
 void ami_ErrorDestroy(
   ami_Error     inAmiError );   /* AMI error handle.*/

/*------------------------------------------------------------------------------
 * Stamps the specified AMI error with the specified file name and line number
 * to identify the location in the code where this error was generated.
 *----------------------------------------------------------------------------*/
 void ami_ErrorStamp(
   ami_Error     inAmiError,     /* AMI error object to stamp. */
   const char *  inpFilename,    /* Source file name in which error occurred.*/
   int           inLineNumber ); /* Source file line number where error occurred.*/
/*------------------------------------------------------------------------------
 * Returns the AMI C API error code of the specified AMI error.
 *----------------------------------------------------------------------------*/
 int ami_ErrorGetCode(
   ami_Error     inAmiError );   /* AMI error handle.*/

/*------------------------------------------------------------------------------
 * Returns the textual description of the specified AMI error. This function
 * always returns a description (never NULL). If no description was specified
 * in the create call then a default message is used which states that no
 * description is available.
 *----------------------------------------------------------------------------*/
 const char * ami_ErrorGetText(
   ami_Error     inAmiError );   /* AMI error handle.*/

/*------------------------------------------------------------------------------
 * Returns the thread ID of the thread which created the specified AMI error.
 *----------------------------------------------------------------------------*/
 int ami_ErrorGetThread(
   ami_Error     inAmiError );   /* AMI error handle.*/

/*------------------------------------------------------------------------------
 * Returns the name of the source file which generated the specified AMI error.
 * This function can return a NULL pointer if the specified error was not file
 * stamped.
 *----------------------------------------------------------------------------*/
 const char * ami_ErrorGetFile(
   ami_Error     inAmiError );   /* AMI error handle.*/

/*------------------------------------------------------------------------------
 * Returns the line number of the source file which generated the specified AMI
 * error. This function could return zero if the specified error was not file
 * stamped.
 *----------------------------------------------------------------------------*/
 int ami_ErrorGetLine(
   ami_Error     inAmiError );   /* AMI error handle.*/

/*==============================================================================
 *                       AMI Trace Control Functions
 *============================================================================*/

/*------------------------------------------------------------------------------
 * AMI C API trace levels. All trace messages output by the AMI C API are
 * classified under one of the following trace levels. When a trace message is
 * generated it is passed to the ami_TraceHandler of the associated AMI session
 * only if the corresponding trace level is enabled. This allows for
 * programmatic control of the level of tracing performed.
 *
 * These values may be OR'ed together when used as arguments in functions that
 * take an ami_TraceCode.
 *----------------------------------------------------------------------------*/
 typedef enum
 { AMI_INFO    =  1, /* Indicates information level trace message. */
   AMI_WARNING =  2, /* Indicates warning     level trace message. */
   AMI_ERROR   =  4, /* Indicates error       level trace message. */
   AMI_DEBUG   =  8, /* Indicates debug       level trace message. */
   AMI_AMI     = 16, /* Indicates AMI         level trace message. */
   AMI_STAMP   = 32, /* Adds source file name and line number to all messages.*/

   AMI_ALL     = 0x7FFFFFFF  /* For enabling/disabling all levels. */
 } ami_TraceCode;

/*------------------------------------------------------------------------------
 * Returns the current AMI session trace level settings.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionGetTraceLevels(
   ami_Session     inAmiSession,    /* Handle of AMI session */
   ami_TraceCode * inpTraceLevel ); /* Target for returned trace levels.*/
/*------------------------------------------------------------------------------
 * Resets all AMI session trace level settings to the specified settings.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionSetTraceLevels(
   ami_Session    inAmiSession,    /* Handle of AMI session */
   ami_TraceCode  inTraceLevel );  /* Trace levels to set.*/
/*------------------------------------------------------------------------------
 * Enables the specified level(s) of trace output. All other trace level
 * settings are uneffected.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionEnableTraceLevels(
   ami_Session    inAmiSession,    /* Handle of AMI session */
   ami_TraceCode  inTraceLevel );  /* Trace level(s) to enable.*/
/*------------------------------------------------------------------------------
 * Disables the specified level(s) of trace output. All other trace level
 * settings are uneffected.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionDisableTraceLevels(
   ami_Session    inAmiSession,    /* Handle of AMI session */
   ami_TraceCode  inTraceLevel );  /* Trace level(s) to disable.*/
/*==============================================================================
 *                      AMI API Callback Function Types
 *============================================================================*/

/*------------------------------------------------------------------------------
 * This is the prototype for an AMI method callback function. These functions
 * are invoked whenever the associated method is executed by TIB/Hawk. The
 * callback should return method return values or an AmiError if the function
 * call fails. When this callback is executed for an asynchronous method the
 * inAmiSubscription argument is provided. For synchronous methods this
 * argument is NULL.
 *----------------------------------------------------------------------------*/
 typedef ami_Error (*ami_OnInvokeCallback)(
   ami_Session             inAmiSession,  /* Handle of AMI session.*/
   ami_Method              inAmiMethod,   /* Handle of AMI method.*/
   ami_Subscription        inAmiSubscription, /* Async. method subscription.*/
   void *                  inpUserData,   /* User data associated with method.*/
   ami_ParameterList       inArguments,   /* Handle of AMI input parameter list.*/
   ami_ParameterListList * inpReturns );  /* Target for method return values.*/

/*------------------------------------------------------------------------------
 * This is the prototype for the optional AMI asyncronous method on start
 * callback function. These functions are called whenever a new subscription is
 * started. The application should perform any necessary initialization required
 * to process this new subscription. This callback should return AMI_OK if no
 * error, otherwise an AmiError describing the failure.
 *----------------------------------------------------------------------------*/
 typedef ami_Error (*ami_OnStartCallback)(
   ami_Session       inAmiSession,      /* Handle of AMI session.*/
   ami_Method        inAmiMethod,       /* Handle of AMI method.*/
   void *            inpUserData,       /* User data associated with method.*/
   ami_Subscription  inAmiSubscription, /* Async. method subscription.*/
   ami_ParameterList inArguments );     /* Handle of AMI input parameter list.*/

/*------------------------------------------------------------------------------
 * This is the prototype for the optional AMI asyncronous method on stop
 * callback function. These functions are called whenever a subscription is
 * stopped. The application should perform any necessary clean-up required
 * when terminating a subscription. This callback should return AMI_OK if no
 * error, otherwise an AmiError describing the failure.
 *----------------------------------------------------------------------------*/
 typedef void (*ami_OnStopCallback)(
   ami_Session       inAmiSession,        /* Handle of AMI session.*/
   ami_Method        inAmiMethod,         /* Handle of AMI method.*/
   void *            inpUserData,         /* User data associated with method.*/
   ami_Subscription  inAmiSubscription ); /* Async. method subscription.*/

/*------------------------------------------------------------------------------
 * This is the prototype for the optional AMI trace handler callback. This
 * callback is used by AMI API to report events to the application. These events
 * are classified by ami_TraceCode (see above). If no trace handler is provided
 * then tracing is disabled. Tracing can be controlled (including turned off)
 * using the trace control functions (see above).
 *----------------------------------------------------------------------------*/
 typedef void (*ami_TraceHandler)(
   ami_Session    inAmiSession, /* AMI session reporting trace event.*/
   ami_TraceCode  inTraceCode,  /* Category of trace event.*/
   int            inTraceID,    /* Unique ID of trace event.*/
   const char *   inpText,      /* Textual description of trace event.*/
   void *         inpUserData );/* User data associated with this AMI session */

/*==============================================================================
 *                           AMI C API Functions
 *============================================================================*/
/*------------------------------------------------------------------------------
 * These functions return the AMI C API version information. The version
 * information consists of a major, minor, and patch number formatted left to
 * right, respectively like this 3.1.1
 *----------------------------------------------------------------------------*/
 const char * ami_VersionName  (); /* Returns product name. */
 const char * ami_Version      (); /* Returns version string (e.g. 3.1.1).*/
 const char * ami_VersionDate  (); /* Returns build date. */
 int          ami_VersionMajor (); /* Returns major version number.*/
 int          ami_VersionMinor (); /* Returns minor version number.*/
 int          ami_VersionUpdate(); /* Returns patch level   number.*/

/*------------------------------------------------------------------------------
 * Initializes the AMI C API. Must be called prior to calling any other AMI C
 * API functions.
 *----------------------------------------------------------------------------*/
 ami_Error ami_Open();

/*------------------------------------------------------------------------------
 * Terminates the AMI C API and releases associated resources.
 *----------------------------------------------------------------------------*/
 ami_Error ami_Close();

/*==============================================================================
 *                        AMI Session API Functions
 *============================================================================*/

/*------------------------------------------------------------------------------
 * Creates a new AMI session. Each session represents a single TIB/Hawk
 * microagent.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionCreate(
   ami_Session *    inpAmiSession,  /* Target for returned session handle */
   ami_TraceCode    inTraceLevel,   /* AMI trace levels for this AMI session.*/
   const char *     inpRvService,   /* The RV service parameter.*/
   const char *     inpRvNetwork,   /* The RV network parameter.*/
   const char *     inpRvDaemon,    /* The RV daemon  parameter.*/
   unsigned int     inRvTransport,  /* Rendezvous transport for AMI session */
   unsigned int     inRvQueue,      /* Rendezvous queue     for AMI session */
   const char *     inpName,        /* Unique name string for microagent */
   const char *     inpDisplayName, /* User-friendly name string for microagent */
   const char *     inpHelp,        /* User-friendly microagent description */
   ami_TraceHandler inTraceHandler, /* AMI session error callback function */
   const void *     inpUserData );  /* AMI session user data */

/*------------------------------------------------------------------------------
 * Destroys the AMI session. The AMI session (and associated handle) is no
 * longer valid. If the AMI session is active it will be stopped prior to being
 * destroyed.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionDestroy(
   ami_Session   inAmiSession ); /* Handle of AMI session */

/*------------------------------------------------------------------------------
 * Activate the AMI session. All interested Hawk agents are notified that this
 * AMI session is running and available. These agents will add the associated
 * microagent to their microagent lists. This AMI session will be active until
 * ami_SessionStop or ami_SessionDestroy is called.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionAnnounce(
   ami_Session   inAmiSession ); /* Handle of AMI session */

/*------------------------------------------------------------------------------
 * Stop the AMI session. All associated Hawk agents are notified that this AMI
 * session is no longer running or supported. These agents will remove the
 * associated microagent from their microagent lists. This AMI session will be
 * inactive until ami_SessionAnnounce is called to re-activate this sesson.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionStop(
   ami_Session   inAmiSession ); /* Handle of AMI session */

/*------------------------------------------------------------------------------
 * Get the name string of AMI session (microagent). This string should not be
 * modified.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionGetName(
   ami_Session   inAmiSession,  /* Handle of AMI session */
   const char ** inpName );     /* Target for returned AMI session name */

/*------------------------------------------------------------------------------
 * Get the user-friendly name string of AMI session (microagent). This string
 * should not be modified.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionGetDisplayName(
   ami_Session   inAmiSession,  /* Handle of AMI session */
   const char ** inpName);      /* Target for returned AMI session display name */

/*------------------------------------------------------------------------------
 * Get the descriptive text string of AMI session (microagent). This string
 * should not be modified.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionGetHelp(
   ami_Session   inAmiSession,  /* Handle of AMI session */
   const char ** inpHelp );     /* Target for returned AMI session description */

/*------------------------------------------------------------------------------
 * Returns the user data of the specified AMI session.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionGetUserData(
   ami_Session   inAmiSession,  /* Handle of AMI session */
   void **       inpUserData ); /* Target for returned user data */

/*------------------------------------------------------------------------------
 * Returns data for the specified async. method subscription.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionSendData(
   ami_Session           inAmiSession,      /* Handle of AMI session */
   ami_Subscription      inAmiSubscription, /* Async. method subscription.*/
   ami_ParameterListList inReturns );       /* Data to be returned. */

/*------------------------------------------------------------------------------
 * Reports an error condition for the specified async. method subscription.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionSendError(
   ami_Session           inAmiSession,      /* Handle of AMI session */
   ami_Subscription      inAmiSubscription, /* Async. method subscription.*/
   ami_Error             inAmiError );      /* Error to be reported. */

/*------------------------------------------------------------------------------
 * Calls the ami_OnInvokeCallback function of the specified AMI async. method
 * once for each currently active subscription. This function is typically
 * invoked when new data becomes available for an asynchronous method. The
 * ami_OnInvokeCallback is called with the subscriptions argument values
 * allowing the application to properly send the new data to each subscription.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionOnData(
   ami_Session  inAmiSession, /* Handle of AMI session.*/
   ami_Method   inAmiMethod); /* Handle of the AMI asychronous method.*/

/*------------------------------------------------------------------------------
 * Send an unsolicited message to any interested subscribers.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SessionSendUnsolicitedMsg(
   ami_Session    inAmiSession,  /* Handle of AMI session. */
   ami_AlertType  inType,        /* Alert type of message. */
   const char *   inpText,       /* Textual description of the message. */
   int            inId );        /* User defined ID of message. */

/*==============================================================================
 *                         AMI Method API Functions
 *============================================================================*/

/*---------------------------------------------------------------------------------
 * Allocates and initializes an ami_Method object and returns the
 * handle to the object. The AMI session that created the object owns the object.
 * The specified callback must return data synchronously.
 *-------------------------------------------------------------------------------*/
 ami_Error ami_MethodCreate(
   ami_Session          inAmiSession, /* Handle of AMI session */
   ami_Method *         inpAmiMethod, /* Location to store new method handle */
   const char *         inpName,      /* Name of the method for AMI purpose */
   ami_MethodType       inType,       /* Type of method */
   const char *         inpHelp,      /* Textual description of method */
   int                  inTimeout,    /* Timeout period in milliseconds */
   ami_OnInvokeCallback inOnInvoke,   /* Method invocation callback */
   const void *         inpUserData );/* (Optional) AMI method user data */

/*---------------------------------------------------------------------------
 * Allocates and initializes an ami_Method object and returns
 * the handle to the object. The AMI session that created the object owns
 * the object. The callback returns data asynchronously. However the method
 * can also return data synchronously.
 *-------------------------------------------------------------------------*/
 ami_Error ami_AsyncMethodCreate(
   ami_Session          inAmiSession, /* Handle of AMI session */
   ami_Method *         inpAmiMethod, /* Location to store new method handle */
   const char *         inpName,      /* Name of the method for AMI purpose */
   ami_MethodType       inType,       /* Type of method */
   const char *         inpHelp,      /* Optional help text */
   int                  inTimeout,    /* Timeout period in milliseconds */
   ami_OnInvokeCallback inOnInvoke,   /* Method invocation callback */
   ami_OnStartCallback  inOnStart,    /* (Optional) Start subscription callback */
   ami_OnStopCallback   inOnStop,     /* (Optional) Stop  subscription callback */
   const void *         inpUserData );/* (Optional) AMI method user data */

/*------------------------------------------------------------------------------
 * Checks the specified AMI session interface for the ami_Method object
 * corresponding to the name specified. If the specified method name is not
 * found in the AMI session object, this method returns AMI_OK with the
 * ami_Method set to NULL.
 *----------------------------------------------------------------------------*/
 ami_Error ami_MethodFind(
   ami_Session      inAmiSession,   /* Handle of AMI session */
   const char *     inpName,        /* The name of the method */
   ami_Method *     inpAmiMethod ); /* Location to store new method handle */

/*------------------------------------------------------------------------------
 * Returns the name of the specified method in the specified AMI session. This
 * string should not be modified.
 *----------------------------------------------------------------------------*/
 ami_Error ami_MethodGetName(
   ami_Session      inAmiSession,   /* Handle of AMI session */
   ami_Method       inAmiMethod,    /* Handle of AMI method */
   const char **    inpMethodName );/* Target for returned method name.*/

/*------------------------------------------------------------------------------
 * Returns the textual description of the specified method in the specified AMI
 * session. This string should not be modified.
 *----------------------------------------------------------------------------*/
ami_Error ami_MethodGetHelp(
  ami_Session      inAmiSession,   /* Handle of AMI session */
  ami_Method       inAmiMethod,    /* Handle of AMI method */
  const char **    inpMethodHelp );/* Target for returned method help.*/

/*------------------------------------------------------------------------------
 * Returns the user data of the specified method.
 *----------------------------------------------------------------------------*/
 ami_Error ami_MethodGetUserData(
   ami_Session     inAmiSession,   /* Handle of AMI session */
   ami_Method      inAmiMethod,    /* Handle of AMI method */
   void **         inpUserData );  /* Target for returned user data */

/*------------------------------------------------------------------------------
 * Specifies which return parameter to use as the primary key for methods that
 * return tabular data. If you need to establish a composite index consisting
 * of multiple parameters, this method can be called repeatedly, once for each
 * index return parameter, in order of precedence with primary key first.
 *----------------------------------------------------------------------------*/
 ami_Error ami_MethodSetIndex(
   ami_Session      inAmiSession,  /* Handle of AMI session */
   ami_Method       inAmiMethod,   /* Handle of AMI method */
   const char *     inpIndexName );/* Name of return parameter.*/

/*==============================================================================
 *                       AMI Subscription API Functions
 *============================================================================*/

/*------------------------------------------------------------------------------
 * Allows user to retieve the associated AMI method object for a particular
 * aync. method subscription.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SubscriptionGetMethod(
   ami_Session         inAmiSession,      /* Handle of AMI session */
   ami_Subscription    inAmiSubscription, /* Async. method subscription.*/
   ami_Method *        inpAmiMethod );    /* Target for returned method object.*/

/*------------------------------------------------------------------------------
 * Allows user to retieve the method argument values for a a particular aync.
 * method subscription. This ami_ParameterList is only valid while the
 * associated subscription is valid.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SubscriptionGetArguments(
   ami_Session         inAmiSession,      /* Handle of AMI session */
   ami_Subscription    inAmiSubscription, /* Async. method subscription.*/
   ami_ParameterList * inpArguments );    /* Target for returned argument list.*/

/*------------------------------------------------------------------------------
 * Allows user to attach application specific data to a particular aync. method
 * subscription. This function is usually used in the onStart() callback.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SubscriptionSetUserData(
   ami_Session       inAmiSession,      /* Handle of AMI session */
   ami_Subscription  inAmiSubscription, /* Async. method subscription.*/
   void *            inpUserData );     /* User data.*/

/*------------------------------------------------------------------------------
 * Allows user to retieve the application specific data attached to a particular
 * aync. method subscription. This function is usually used in the onInvoke()
 * callback when processing asynchronous method invocations to obtain access to
 * the application specific data associated with that invocation.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SubscriptionGetUserData(
   ami_Session       inAmiSession,      /* Handle of AMI session */
   ami_Subscription  inAmiSubscription, /* Async. method subscription.*/
   void **           inpUserData );     /* Target for returned user data.*/

/*------------------------------------------------------------------------------
 * Indicates that for this subscription the associated onInvoke() callback
 * should be auto-invoked at the specified interval. This provides a psuedo-
 * aynchronous event to trigger (what would normally be) synchronous methods so
 * that they can behave as asyncronous methods. A typical scenario is a method
 * which must calculate (polled) data over a precise time interval and return
 * the calculated result based on that interval. In this case the method returns
 * data not based on a synchronous call but on a specified time interval.
 *----------------------------------------------------------------------------*/
 ami_Error ami_SubscriptionSetCallbackInterval(
   ami_Session       inAmiSession,      /* Handle of AMI session */
   ami_Subscription  inAmiSubscription, /* Async. method subscription.*/
   int               inInterval );      /* Interval in seconds. Zero turns off.*/

/*==============================================================================
 *                       AMI Parameter API Functions
 *============================================================================*/

/*------------------------------------------------------------------------------
 * Adds the specified parameter to the list of input parameters for the method.
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterCreateIn(
   ami_Session      inAmiSession, /* Handle of AMI session */
   ami_Method       inAmiMethod,  /* Handle of AMI method */
   ami_Parameter *  inpAmiParm,   /* Target for returned parameter handle. */
   const char *     inpName,      /* Name of the parameter for AMI purposes */
   ami_DataType     inType,       /* Type of the parameter */
   const char *     inpHelp );    /* Optional description for the parameter */

/*------------------------------------------------------------------------------
 * Adds the specified parameter to the description of return parameters for
 * that method.
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterCreateOut(
   ami_Session      inAmiSession, /* Handle of AMI session */
   ami_Method       inAmiMethod,  /* Handle of AMI method */
   ami_Parameter *  inpAmiParm,   /* Target for returned parameter handle. */
   const char *     inpName,      /* Name of the parameter for AMI purposes */
   ami_DataType     inType,       /* Type of the parameter */
   const char *     inpHelp );    /* Optional description for the parameter */

/*------------------------------------------------------------------------------
 * Returns a handle to a list of AMI parameter lists to be used to specify
 * return values for a method invocation. This list is empty indicating no
 * data. This handle can be returned by an invoked method to indicate that that
 * invocation has returned no data. To add return values to this empty list use
 * the ami_ParameterListOut function.
 *
 * If the created ami_ParameterListList is returned to the AMI API (e.g.
 * ami_OnInvokeCallback) then the API is responsible for destroying it. If it
 * is not returned to the API then the caller must destroy it using
 * ami_ParameterListListDestroy.
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterListListCreate(
   ami_Session             inAmiSession,      /* Handle of AMI session */
   ami_ParameterListList * inAmiParmListList);/* List of AMI parameters list */

/*------------------------------------------------------------------------------
 * Returns a handle to a list of AMI parameter lists to be used to specify
 * return values for a method invocation. The first call to this method
 * allocates and returns the list of AMI parameter lists (ami_ParameterListList)
 * and one parameter list (ami_ParameterList) member. The application should
 * then use the ami_ParameterSetValue to set the return values into this
 * parameter list (ami_ParameterList). To return more than one row of data
 * (i.e. tabular data) this function can be called repeatedly using the same
 * ami_ParameterListList handle. Each call will add an additional return row
 * and return a parameter list (ami_ParameterList) to set the return values for
 * that row. If the created ami_ParameterListList is returned to the AMI API
 * (e.g. ami_OnInvokeCallback) then the API is responsible for destroying it.
 * If it is not returned to the API then the caller must destroy it using
 * ami_ParameterListListDestroy.
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterListOut(
   ami_Session             inAmiSession,       /* Handle of AMI session */
   ami_Method              inAmiMethod,        /* Handle of AMI method */
   ami_ParameterListList * inpAmiParmListList, /* List of AMI parameters list */
   ami_ParameterList *     inpAmiParmList );   /* Handle of AMI parameter list */

/*------------------------------------------------------------------------------
 * Sets the value of an AMI parameter in the specified AMI parameter list.
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterSetValue(
   ami_Session       inAmiSession,  /* Handle of AMI session */
   ami_Method        inAmiMethod,   /* Handle of AMI method */
   ami_ParameterList inAmiParmList, /* Set parameter in this parameter list */
   const char *      inpName,       /* Name of parameter being set */
   const void *      inpValue );    /* Value being set */

/*------------------------------------------------------------------------------
 * Retrieves the value of an AMI parameter from the specified AMI parameter list.
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterGetValue(
   ami_Session       inAmiSession,  /* Handle of AMI session */
   ami_Method        inAmiMethod,   /* Handle of AMI method */
   ami_ParameterList inAmiParmList, /* Get parameter from this parameter list */
   const char *      inpName,       /* Name of parameter being retreived */
   void *            inpValue );    /* Target for retrieved value */

/*------------------------------------------------------------------------------
 * Adds a value choice for the specified parameter. Value choices can be
 * displayed by the managing application. Value choices are optional values for
 * the specified parameter. The user is allowed to choose one of these values
 * or enter a different value (See ami_ParameterAddLegal below).
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterAddChoice(
   ami_Session      inAmiSession, /* Handle of AMI session */
   ami_Parameter    inAmiParm,    /* Handle of AMI parameter */
   const void *     inpData );    /* Choice value. */

/*------------------------------------------------------------------------------
 * Adds a legal choice for the specified parameter. Legal choices can be
 * displayed by the managing application. Legal choices specify the only valid
 * values for the specified parameter. The user can only choose one of these
 * values (See ami_ParameterAddChoice above).
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterAddLegal(
   ami_Session      inAmiSession, /* Handle of AMI session */
   ami_Parameter    inAmiParm,    /* Handle of AMI parameter */
   const void *     inpData );    /* Legal choice value.*/

/*------------------------------------------------------------------------------
 * Destroys the specified list of parameter lists.
 *----------------------------------------------------------------------------*/
 ami_Error ami_ParameterListListDestroy(
   ami_Session           inAmiSession,    /* Handle of AMI session */
   ami_ParameterListList inAmiParmList ); /* Handle of list of parameter lists.*/

#if defined(__cplusplus)
}
#endif

#endif /* _INCLUDED_AMI_H */
