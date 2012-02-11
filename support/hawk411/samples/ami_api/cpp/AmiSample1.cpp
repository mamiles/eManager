/*******************************************************************************
** File:    AmiSample1.cpp
**
** Purpose: This is a TIBCO Hawk AMI C API sample program to demonstrate how to
**          instrument a C++ application for TIBCO Hawk monitoring and management.
**
**          The program creates an AMI session to support 4 different methods:
**          1) getSampleData: Demonstrates getting data from an instrumented
**                            application.
**          2) setSampleData: Demonstrates passing data to an instrumented
**                            application.
**          3) getSampleDataTable: Demonstrates how to return tabular data.
**          4) onSampleData: Demonstrates how to return data asynchronously. The
**                           asynchronous source of data being returned is
**                           simulated using a timer.
**          5) exitSampleData: Demonstrates how to create a method to shutdown
**                             an application.
**
**          Copyright 2002 by TIBCO Software Inc. All rights reserved.
*******************************************************************************/

#include <iostream.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

#include <tibrv/tibrvcpp.h>
#include <amicpp.h>

/* Constants for Rendezvous transport initialization. */
#define APP_RV_SERVICE            "7474"
#define APP_RV_NETWORK            ""
#define APP_RV_DAEMON             "tcp:7474"

#define INTEGER_PARAMETER_NAME "TheIntegerParameter"
#define INTEGER_PARAMETER_HELP "An example of an integer parameter"
#define STRING_PARAMETER_NAME  "TheStringParameter"
#define STRING_PARAMETER_HELP  "An example of a string parameter"
#define BOOLEAN_PARAMETER_NAME "TheBooleanParameter"
#define BOOLEAN_PARAMETER_HELP "An example of a boolean parameter"
#define DOUBLE_PARAMETER_NAME  "TheDoubleParameter"
#define DOUBLE_PARAMETER_HELP  "An example of a double parameter"
#define U64_PARAMETER_NAME     "TheU64Parameter"
#define U64_PARAMETER_HELP     "An example of an unsigned 64-bit integer parameter"

static int id = 1;

// Check if an object is created properly. obj can be session, method,
// or parameter.
#define AMI_CHECK_OBJ(obj, fname, objName) \
{ \
  if (obj == NULL) \
  { \
     fprintf(stderr, \
       "Function %s() reports an system error. Failed to allocate space for %s object\n", fname, objName); \
     break; \
  } \
  else \
  { \
    amiRC = obj->getStatus(); \
    if (!amiRC.ok()) \
    { \
      ami_Error err = amiRC.getAmiError(); \
      fprintf( stderr, "ERROR   (%04d): [%08x] %s FILE:[%s] LINE[%d]\n", \
  ami_ErrorGetCode(err),ami_ErrorGetThread(err), \
  ami_ErrorGetText(err),ami_ErrorGetFile(err), \
  ami_ErrorGetLine(err) ); \
      break; \
    } \
  } \
}

// Given an object, obtains its status and check if it's ok
#define AMI_CHECK_STATUS(amiRC) \
{ \
  if (!amiRC.ok()) \
  { \
      ami_Error err = amiRC.getAmiError(); \
      fprintf( stderr, "ERROR   (%04d): [%08x] %s FILE:[%s] LINE[%d]\n", \
  ami_ErrorGetCode(err),ami_ErrorGetThread(err), \
  ami_ErrorGetText(err),ami_ErrorGetFile(err), \
  ami_ErrorGetLine(err) ); \
      break; \
  } \
}

#define AMIH_GETSTR(str)     (str ? str : "null")

static TibrvQueue * rvQueue; // RV queue object.

/* Define an unsigned 64-bit integer for AMI sample. */
#if   defined(_WIN32) || defined(__vms)
#define SAMPLE_U64 unsigned __int64
#define U64_VALUE(n) (n)
#elif defined(__alpha)
#define SAMPLE_U64 unsigned long
#define U64_VALUE(n) (n##UL)
#else
#define SAMPLE_U64 unsigned long long
#define U64_VALUE(n) (n##ULL)
#endif

// Sample data object used in the application.
class SampleData
{
public:
  SampleData()
  {
    m_int     = 0;
    m_string  = NULL;
    m_boolean = AMI_FALSE;
    m_double  = 0.0;
    m_U64     = U64_VALUE(0);
  }

  virtual ~SampleData() {delete[] m_string;}

  int getInteger(void) {return m_int;}

  char * getString(void) {return m_string;}

  ami_Boolean getBoolean(void) {return m_boolean;}

  double getDouble(void) {return m_double;}

  SAMPLE_U64 getU64(void) { return m_U64;}

  void setInteger(int integer) {m_int = integer;}

  void setString (const char * stringVal)
  {
    if (stringVal)
    {
      delete[] m_string;
      m_string = NULL;
      int len = strlen(stringVal)+1;
      m_string = new char[len];
      strcpy(m_string, stringVal);
    }
  }

  void setBoolean(ami_Boolean boolVal) {m_boolean = boolVal;}

  virtual void setDouble(double d) {m_double = d;}

  void setU64(SAMPLE_U64 inU64) { m_U64 = inU64;}

private:
  int         m_int;
  char *      m_string;
  ami_Boolean m_boolean;
  double      m_double;
  SAMPLE_U64  m_U64;
};

SampleData * sampleData = NULL;

// Class implementing the "getSampleData" synchronous AMI method
class GetSampleData : public AmiSyncMethod
{
public:
  GetSampleData(AmiSession * session) :
    AmiSyncMethod(session,
                  "getSampleData",           // method name
            "Gets sample data method", // method help
            AMI_METHOD_INFO,           // method type
                  AMI_METHOD_DEFAULT_TIMEOUT)
  {
    if (m_status == AMI_OK)
    {
      m_status = createReturnArguments();
      if (m_status == AMI_OK)
      {
        m_status = setIndexName(INTEGER_PARAMETER_NAME);
      }
    }
  }

  virtual ~GetSampleData() {}

protected:
  AmiStatus createReturnArguments(void)
  {
    AmiStatus amiStatus  = AMI_OK;
    AmiParameterOut *param = NULL;

    do
    {
      param = new AmiParameterOut( this,    INTEGER_PARAMETER_NAME,
                                   AMI_I32, INTEGER_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,       STRING_PARAMETER_NAME,
                                   AMI_STRING, STRING_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,       BOOLEAN_PARAMETER_NAME,
                                   AMI_BOOLEAN,BOOLEAN_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,    DOUBLE_PARAMETER_NAME,
                                   AMI_F64, DOUBLE_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,    U64_PARAMETER_NAME,
                                   AMI_U64, U64_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

    } while(0);


    if (param == NULL)
         amiStatus.setStatus(1, "Instantiating AmiParameterOut failed");
    else amiStatus = param->getStatus();

    return amiStatus;
  }

  AmiStatus onInvoke(AmiSubscription * context,
                     AmiParameterListIn * argsIn,
                     AmiParameterListOut * argsOut)
  {
    AmiStatus   amiStatus  = AMI_OK;
    int         intVal     = sampleData->getInteger();
    char *      strVal     = sampleData->getString();
    ami_Boolean boolVal    = sampleData->getBoolean();
    double      doubleVal  = sampleData->getDouble();
    SAMPLE_U64  U64Val     = sampleData->getU64();

    do
    {
      amiStatus = argsOut->newRow();
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(INTEGER_PARAMETER_NAME, &intVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(STRING_PARAMETER_NAME, strVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(BOOLEAN_PARAMETER_NAME, &boolVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(DOUBLE_PARAMETER_NAME, &doubleVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(U64_PARAMETER_NAME, &U64Val);
      if (amiStatus != AMI_OK) break;

    } while(0);

    return amiStatus;
  }
};

// Class implementing the "setSampleData" synchronous AMI method
class SetSampleData : public AmiSyncMethod
{
public:
  SetSampleData(AmiSession * session) :
    AmiSyncMethod(session,
              "setSampleData",           // method name
        "Sets sample data method", // method help
        AMI_METHOD_ACTION_INFO,    // method type
              AMI_METHOD_DEFAULT_TIMEOUT)
  {
    if (m_status == AMI_OK)
    {
      m_status = createInputArguments();
      if (m_status == AMI_OK)
      {
        m_status = createReturnArguments();
        if (m_status == AMI_OK)
        {
          m_status = setIndexName(INTEGER_PARAMETER_NAME);
        }
      }
    }
  }

  virtual ~SetSampleData() {}

protected:
  AmiStatus createInputArguments(void)
  {
    AmiStatus amiStatus = AMI_OK;
    int        intChoices[] = { 1, 2, 3, 4 };
    char *     strChoices[] = { (char*)"Sample", (char*)"Hello", (char*)"World" };
    double     dblChoices[] = { 1.1, 2.2, 3.3 };
    SAMPLE_U64 u64Choices[] = { U64_VALUE(1), U64_VALUE(2), U64_VALUE(3) };
    AmiParameterIn * param = NULL;

    do
    {
      param = new AmiParameterIn( this,    INTEGER_PARAMETER_NAME,
                                  AMI_I32, INTEGER_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      int i;
      for ( i=0 ; i<sizeof(intChoices)/sizeof(int) ; i++ )
      { amiStatus = param->addChoice(&intChoices[i]);
        if (amiStatus != AMI_OK) break;
      }
      if (amiStatus != AMI_OK) break;

      param = new AmiParameterIn( this,       STRING_PARAMETER_NAME,
                                  AMI_STRING, STRING_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      for ( i=0 ; i<sizeof(strChoices)/sizeof(char *) ; i++ )
      { amiStatus = param->addLegal(strChoices[i]);
        if (amiStatus != AMI_OK) break;
      }
      if (amiStatus != AMI_OK) break;

      param = new AmiParameterIn( this,        BOOLEAN_PARAMETER_NAME,
                                  AMI_BOOLEAN, BOOLEAN_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterIn( this,    DOUBLE_PARAMETER_NAME,
                                  AMI_F64, DOUBLE_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      for ( i=0 ; i<sizeof(dblChoices)/sizeof(double) ; i++ )
      { amiStatus = param->addLegal(&dblChoices[i]);
        if (amiStatus != AMI_OK) break;
      }
      if (amiStatus != AMI_OK) break;

      param = new AmiParameterIn( this,    U64_PARAMETER_NAME,
                                  AMI_U64, U64_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      for ( i=0 ; i<sizeof(u64Choices)/sizeof(SAMPLE_U64) ; i++ )
      { amiStatus = param->addChoice(&u64Choices[i]);
        if (amiStatus != AMI_OK) break;
      }
      if (amiStatus != AMI_OK) break;

    } while(0);


    if (param == NULL)
    {
      amiStatus.setStatus(2, "Iinstantiating AmiParameterIn failed");
    }

    return amiStatus;
  }

  AmiStatus createReturnArguments(void)
  {
    AmiStatus amiStatus  = AMI_OK;
    AmiParameterOut *param = NULL;

    do
    {
      param = new AmiParameterOut( this,    INTEGER_PARAMETER_NAME,
                                   AMI_I32, INTEGER_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,       STRING_PARAMETER_NAME,
                                   AMI_STRING, STRING_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,       BOOLEAN_PARAMETER_NAME,
                                   AMI_BOOLEAN,BOOLEAN_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,    DOUBLE_PARAMETER_NAME,
                                   AMI_F64, DOUBLE_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,    U64_PARAMETER_NAME,
                                   AMI_U64, U64_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

    } while(0);


    if (param == NULL)
         amiStatus.setStatus(1, "Instantiating AmiParameterOut failed");
    else amiStatus = param->getStatus();

    return amiStatus;
  }

  AmiStatus onInvoke(AmiSubscription * context,
                     AmiParameterListIn * argsIn,
                     AmiParameterListOut * argsOut)
  {
    AmiStatus      amiStatus  = AMI_OK;
    int            intVal     = 0;
    char *         strVal     = NULL;
    ami_Boolean    boolVal    = AMI_FALSE;
    double         doubleVal  = 0.0;
    SAMPLE_U64     U64Val     = U64_VALUE(0);

    do
    {
      amiStatus = argsIn->getValue(INTEGER_PARAMETER_NAME, &intVal);
      if ( amiStatus != AMI_OK ) break;

      amiStatus = argsIn->getValue(STRING_PARAMETER_NAME, &strVal);
      if ( amiStatus != AMI_OK ) break;

      amiStatus = argsIn->getValue(BOOLEAN_PARAMETER_NAME, &boolVal);
      if ( amiStatus != AMI_OK ) break;

      amiStatus = argsIn->getValue(DOUBLE_PARAMETER_NAME, &doubleVal);
      if ( amiStatus != AMI_OK ) break;

      amiStatus = argsIn->getValue(U64_PARAMETER_NAME, &U64Val);
      if ( amiStatus != AMI_OK ) break;

      sampleData->setInteger(intVal);
      sampleData->setString(strVal);
      sampleData->setBoolean(boolVal);
      sampleData->setDouble(doubleVal);
      sampleData->setU64(U64Val);

      amiStatus = argsOut->newRow();
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(INTEGER_PARAMETER_NAME, &intVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(STRING_PARAMETER_NAME, strVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(BOOLEAN_PARAMETER_NAME, &boolVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(DOUBLE_PARAMETER_NAME, &doubleVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(U64_PARAMETER_NAME, &U64Val);
      if (amiStatus != AMI_OK) break;

    } while(0);

    return amiStatus;
  }
};

// Class implementing the "getSampleDataTable" synchronous AMI method
class GetSampleDataTable : public AmiSyncMethod
{
public:
  GetSampleDataTable(AmiSession * session) :
    AmiSyncMethod(session,
                  "getSampleDataTable",
            "Gets sample data in tabular form method",
            AMI_METHOD_INFO,
                  AMI_METHOD_DEFAULT_TIMEOUT)
  {
    if (m_status == AMI_OK)
    {
      m_status = createReturnArguments();
      if (m_status == AMI_OK)
      {
        m_status = setIndexName(INTEGER_PARAMETER_NAME);
        if (m_status == AMI_OK)
        {
          m_status = setIndexName(STRING_PARAMETER_NAME);
        }
      }
    }
  }

  virtual ~GetSampleDataTable() {}

protected:
  AmiStatus createReturnArguments(void)
  {
    AmiStatus amiStatus  = AMI_OK;
    AmiParameterOut *param = NULL;

    do
    {
      param = new AmiParameterOut( this,    INTEGER_PARAMETER_NAME,
                                   AMI_I32, INTEGER_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,       STRING_PARAMETER_NAME,
                                   AMI_STRING, STRING_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,       BOOLEAN_PARAMETER_NAME,
                                   AMI_BOOLEAN,BOOLEAN_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,    DOUBLE_PARAMETER_NAME,
                                   AMI_F64, DOUBLE_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,    U64_PARAMETER_NAME,
                                   AMI_U64, U64_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

    } while(0);


    if (param == NULL)
         amiStatus.setStatus(1, "Instantiating AmiParameterOut failed");
    else amiStatus = param->getStatus();

    return amiStatus;
  }

  AmiStatus onInvoke(AmiSubscription * context,
                     AmiParameterListIn * argsIn,
                     AmiParameterListOut * argsOut)
  {
    AmiStatus    amiStatus  = AMI_OK;
    int          intVal     = sampleData->getInteger();
    double       doubleVal  = sampleData->getDouble();
    char *       strVal     = sampleData->getString();
    ami_Boolean  boolVal    = sampleData->getBoolean();
    SAMPLE_U64   U64Val     = sampleData->getU64();
    char *       newStr     = new char[strlen(AMIH_GETSTR(strVal))+10];

    for (int i=0; i<5; i++ )
    {
      sprintf(newStr, "%s_%d", strVal, i);
      intVal++;
      doubleVal += 1.1;
      U64Val++;

      do
      {
        amiStatus = argsOut->newRow();
        if (amiStatus != AMI_OK) break;

        amiStatus = argsOut->setValue(INTEGER_PARAMETER_NAME, &intVal);
        if (amiStatus != AMI_OK) break;

        amiStatus = argsOut->setValue(STRING_PARAMETER_NAME, newStr );
        if (amiStatus != AMI_OK) break;

        amiStatus = argsOut->setValue(BOOLEAN_PARAMETER_NAME, &boolVal);
        if (amiStatus != AMI_OK) break;

        amiStatus = argsOut->setValue(DOUBLE_PARAMETER_NAME, &doubleVal);
        if (amiStatus != AMI_OK) break;

        amiStatus = argsOut->setValue(U64_PARAMETER_NAME, &U64Val);
        if (amiStatus != AMI_OK) break;

      } while(0);

      if (amiStatus != AMI_OK) break;
    }

    delete[] newStr;
    return amiStatus;
  }
};

// Class implementing the "onSampleData" asynchronous AMI method
class OnSampleData : public AmiAsyncMethod
{
public:
  OnSampleData(AmiSession * session) :
    AmiAsyncMethod(session,
                   "onSampleData",                               // method name
       "Asynchronous method that returns SampleData",// method help
       AMI_METHOD_INFO,                              // method type
                   AMI_METHOD_DEFAULT_TIMEOUT)
  {
    if (m_status == AMI_OK)
      m_status = createReturnArguments();
  }

  virtual ~OnSampleData() {}

protected:
  AmiStatus createReturnArguments(void)
  {
    AmiStatus amiStatus  = AMI_OK;
    AmiParameterOut *param = NULL;

    do
    {
      param = new AmiParameterOut( this,    INTEGER_PARAMETER_NAME,
                                   AMI_I32, INTEGER_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,       STRING_PARAMETER_NAME,
                                   AMI_STRING, STRING_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,       BOOLEAN_PARAMETER_NAME,
                                   AMI_BOOLEAN,BOOLEAN_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,    DOUBLE_PARAMETER_NAME,
                                   AMI_F64, DOUBLE_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

      param = new AmiParameterOut( this,    U64_PARAMETER_NAME,
                                   AMI_U64, U64_PARAMETER_HELP );
      if (param == NULL || param->getStatus() != AMI_OK) break;

    } while(0);


    if (param == NULL)
         amiStatus.setStatus(1, "Instantiating AmiParameterOut failed");
    else amiStatus = param->getStatus();

    return amiStatus;
  }

  AmiStatus onInvoke(AmiSubscription * context,
                     AmiParameterListIn * argsIn,
                     AmiParameterListOut * argsOut)
  {
    AmiStatus   amiStatus  = AMI_OK;
    int         intVal     = sampleData->getInteger();
    char *      strVal     = sampleData->getString();
    ami_Boolean boolVal    = sampleData->getBoolean();
    double      doubleVal  = sampleData->getDouble();
    SAMPLE_U64  U64Val     = sampleData->getU64();

    do
    {
      amiStatus = argsOut->newRow();
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(INTEGER_PARAMETER_NAME, &intVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(STRING_PARAMETER_NAME, strVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(BOOLEAN_PARAMETER_NAME, &boolVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(DOUBLE_PARAMETER_NAME, &doubleVal);
      if (amiStatus != AMI_OK) break;

      amiStatus = argsOut->setValue(U64_PARAMETER_NAME, &U64Val);
      if (amiStatus != AMI_OK) break;

    } while(0);

    return amiStatus;
  }

  AmiStatus onStart(AmiSubscription * context, AmiParameterListIn * args)
  {
    return AMI_OK;
  }

  virtual AmiStatus onStop(AmiSubscription * context) {return AMI_OK;}
};

// Class implementing the "exit" synchronous AMI method
class ExitSampleData : public AmiSyncMethod
{
public:
  ExitSampleData(AmiSession * session) :
    AmiSyncMethod(session,
                  "exit",              // method name
            "exit this program", // method help
            AMI_METHOD_ACTION,   // method type. expects no return back
                  AMI_METHOD_DEFAULT_TIMEOUT) {}

  virtual ~ExitSampleData() {}

protected:
  AmiStatus onInvoke(AmiSubscription * context,
                     AmiParameterListIn * argsIn,
                     AmiParameterListOut * argsOut)
  {
    rvQueue->destroy();
    return AMI_OK;
  }
};

class SessionTimer: public TibrvTimerCallback
{
public:
  SessionTimer(AmiAsyncMethod * amiMethod) :
    TibrvTimerCallback()
  {
    m_amiMethod = amiMethod;
    m_rvTimer    = NULL;
  }

  virtual ~SessionTimer() {delete m_rvTimer;}

  void startTimer()
  {
    if (m_rvTimer == NULL)
    {
      m_rvTimer = new TibrvTimer();
      m_rvTimer->create(rvQueue, this, 10, NULL);
    }
  }

  void stopTimer()
  {
    delete m_rvTimer;
    m_rvTimer = NULL;
  }

  virtual void onTimer(TibrvTimer* invoker)
  {
    AmiStatus      amiStatus  = AMI_OK;

    sampleData->setInteger(sampleData->getInteger()+1);
    sampleData->setDouble (sampleData->getDouble()+1.1);
    sampleData->setU64    (sampleData->getU64()+U64_VALUE(1) );
    sampleData->setBoolean(((sampleData->getInteger()%2 == 1)
                              ? AMI_TRUE : AMI_FALSE));
    m_amiMethod->onData();
  }

private:
  AmiAsyncMethod *       m_amiMethod;
  TibrvTimer *           m_rvTimer;
};

#if defined(__cplusplus)
extern "C" {
#endif
// This function serves as the AMI C++ API trace handler callback.
void TraceHandler(
  ami_Session   inAmiSession, /* AMI session reporting error */
  ami_TraceCode inTraceLevel, /* Trace level.*/
  int           inTraceID,    /* Uniques ID of trace event.*/
  const char *  inText,       /* Textual description of error */
  void *        inUserData )   /* User data associated with this AMI session */
{
  const char *  pTraceLevel;
  ami_AlertType AlertType;

  switch( inTraceLevel )
  { case AMI_INFO   : pTraceLevel = "INFO   "; AlertType = AMI_ALERT_INFO;
  break;
    case AMI_WARNING: pTraceLevel = "WARNING"; AlertType = AMI_ALERT_WARNING;
  break;
    case AMI_ERROR  : pTraceLevel = "ERROR  "; AlertType = AMI_ALERT_ERROR;
  break;
    case AMI_DEBUG  : pTraceLevel = "DEBUG  "; AlertType = AMI_ALERT_INFO;
  break;
    case AMI_AMI    : pTraceLevel = "AMI    "; AlertType = AMI_ALERT_INFO;
  break;
    default:          pTraceLevel = "UNKNOWN"; AlertType = AMI_ALERT_INFO;
  break;
  }

  fprintf( stderr, "%s (%04d): %s\n", pTraceLevel, inTraceID, inText );
}

#if defined(__cplusplus)
}
#endif

int
main(int argc, char ** argv)
{
  static char          fname[]            = "main";
  TibrvStatus          rvRC               = TIBRV_OK;
  AmiStatus            amiRC              = AMI_OK;
  AmiSession *         amiSession         = NULL;
  GetSampleData *      getSampleData      = NULL;
  SetSampleData *      setSampleData      = NULL;
  GetSampleDataTable * getSampleDataTable = NULL;
  OnSampleData *       onSampleData       = NULL;
  ExitSampleData *     exitSampleData     = NULL;
  int                  RC                 = 1; // Application return code.

  while (RC)
  {
    // Create internal TIBCO Rendezvous machinery
    amiRC = AmiSession::open();
    AMI_CHECK_STATUS(amiRC);

    rvQueue = new TibrvQueue();
    rvRC = rvQueue->create();
    if (rvRC != TIBRV_OK)
    {
      fprintf(stdout,"Failed to create TIBCO Rendezvous queue. Error %d: "\
                     "%s.\n", (int)rvRC, rvRC.getText());
      break;
    }

    // Create an AMI Session with customized trace object.
    amiSession = new AmiSession(AMI_ALL, //AMI_ERROR,
                                APP_RV_SERVICE, APP_RV_NETWORK, APP_RV_DAEMON,
                                0,
                                rvQueue->getHandle(),
                                "COM.TIBCO.hawk.ami_api.cpp.AmiSample1",
                                "AmiSample1",
                                "TIBCO Hawk AMI C++ API Sample Program 1",
                                TraceHandler, (const void*)0);

    AMI_CHECK_OBJ(amiSession, fname, "AmiSession");

    // Create sample data object used in every methods of the session.
    sampleData = new SampleData();

    sampleData->setInteger(1);
    sampleData->setBoolean(AMI_TRUE);
    sampleData->setString("Sample");
    sampleData->setDouble(1.1);
    sampleData->setU64(U64_VALUE(1));

    getSampleData = new GetSampleData(amiSession);
    AMI_CHECK_OBJ(getSampleData, fname, "GetSampleData");

    setSampleData = new SetSampleData(amiSession);
    AMI_CHECK_OBJ(setSampleData, fname, "SetSampleData");

    getSampleDataTable = new GetSampleDataTable(amiSession);
    AMI_CHECK_OBJ(getSampleDataTable, fname, "GetSampleDataTable");

    onSampleData = new OnSampleData(amiSession);
    AMI_CHECK_OBJ(onSampleData, fname, "OnSampleData");

    exitSampleData = new ExitSampleData(amiSession);
    AMI_CHECK_OBJ(exitSampleData, fname, "ExitSampleData");

    amiRC = amiSession->announce();
    AMI_CHECK_STATUS(amiRC);

    //Start a timer for this session to simulate an async event.
    SessionTimer sTimer(onSampleData);
    sTimer.startTimer();

    fprintf(stdout, "Entering main loop. Dispatching Tibrv events.\n");

    // Dispatch events from AMI Rendezvous queue.
    while (rvQueue->dispatch() == TIBRV_OK) {}

    sTimer.stopTimer();
    RC = 0;
  }

  delete amiSession;
  delete sampleData;
  delete rvQueue;

  AmiSession::close();

  fprintf(stdout, "Program exiting\n");

  return(RC);
}
