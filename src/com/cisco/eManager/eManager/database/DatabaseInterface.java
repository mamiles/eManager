package com.cisco.eManager.eManager.database;

import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.StringTokenizer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Timestamp;

import org.apache.log4j.*;

import COM.TIBCO.hawk.console.hawkeye.AgentInstance;

import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.common.database.EmanagerDatabaseStatusCode;

import com.cisco.eManager.common.admin.UserAccount;
import com.cisco.eManager.common.admin.SecurityRole;

import com.cisco.eManager.common.event.EventSeverity;
import com.cisco.eManager.common.event.EventAcknowledgement;
import com.cisco.eManager.common.event.AbstractEventMessage;
import com.cisco.eManager.common.event.EmanagerEventMessage;
import com.cisco.eManager.common.event.EventDeletionCriteria;
import com.cisco.eManager.common.event.EventSearchCriteria;
import com.cisco.eManager.common.event.AbstractTibcoEventSearchCriteria;
import com.cisco.eManager.common.event.AbstractProcessSequencerEventSearchCriteria;
import com.cisco.eManager.common.event.DateSearchCriteria;
import com.cisco.eManager.common.event.NumericSearchCriteria;
import com.cisco.eManager.common.event.StringSearchCriteria;
import com.cisco.eManager.common.event.AcknowledgementSearchCriteria;
import com.cisco.eManager.common.event.EmanagerEventDetails;
import com.cisco.eManager.common.event.TibcoEventDetails;
import com.cisco.eManager.common.event.ProcessSequencerEventDetails;

import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.common.inventory.Transport;
import com.cisco.eManager.common.inventory.AppInstanceMgmtMode;

import com.cisco.eManager.eManager.event.TibcoEventMessage;
import com.cisco.eManager.common.event.EmanagerEventDetails;

import com.cisco.eManager.eManager.inventory.host.Host;
import com.cisco.eManager.eManager.inventory.host.HostData;
import com.cisco.eManager.eManager.inventory.appType.AppType;
import com.cisco.eManager.eManager.inventory.appType.AppTypeData;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy;
import com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicyData;
import com.cisco.eManager.eManager.inventory.procSeq.AppsGroup;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstance;
import com.cisco.eManager.eManager.inventory.appInstance.AppInstanceData;
import com.cisco.eManager.eManager.inventory.solution.Solution;
import com.cisco.eManager.eManager.inventory.solution.SolutionData;
import com.cisco.eManager.eManager.inventory.view.ContainerNodeData;
import com.cisco.eManager.eManager.inventory.view.ApplicationHierarchyRelationshipData;
import com.cisco.eManager.eManager.inventory.view.PhysicalHierarchyRelationshipData;
import com.cisco.eManager.eManager.inventory.view.SolutionHierarchyRelationshipData;

import com.cisco.eManager.common.audit.AuditAction;
import com.cisco.eManager.common.audit.AuditDomain;
import com.cisco.eManager.common.audit.AuditLogEntry;
import com.cisco.eManager.common.audit.AuditLogSearchCriteria;
import com.cisco.eManager.common.audit.AuditLogDeletionCriteria;

import com.cisco.eManager.eManager.network.AgentId;

import com.cisco.eManager.eManager.database.DatabaseConnectionManager;
import com.cisco.eManager.eManager.database.DatabaseGlobals;

public class DatabaseInterface
{
    private static Logger      logger = Logger.getLogger(DatabaseInterface.class);

    private static DatabaseInterface databaseInterface = null;
    private static Boolean hostSequenceLock = new Boolean (true);
    private static Boolean appTypeSequenceLock = new Boolean (true);
    private static Boolean appInstanceSequenceLock = new Boolean (true);
    private static Boolean eventSequenceLock = new Boolean (true);
    private static Boolean mgmtPolicySequenceLock = new Boolean (true);
    private static Boolean userAccountSequenceLock = new Boolean (true);
    private static Boolean securityRoleSequenceLock = new Boolean (true);
    private static Boolean physicalHierarchySequenceLock = new Boolean (true);
    private static Boolean applicationHierarchySequenceLock = new Boolean (true);
    private static Boolean solutionHierarchySequenceLock = new Boolean (true);
    private static Boolean auditLogSequenceLock = new Boolean (true);
    private static Boolean solutionSequenceLock = new Boolean (true);

    private Connection heartbeatConnection =  null;
    private PreparedStatement heartbeatPreparedStatement = null;

    public long NULL_FOREIGN_KEY = 0;

    /**
     * @roseuid 3F5767C20086
     */
    private DatabaseInterface() throws EmanagerDatabaseException
    {
        DatabaseConnectionManager.instance();
    }

    /**
     * @return com.cisco.eManager.eManager.database.DatabaseInterface
     * @roseuid 3F41963B00F4
     */
    public static synchronized DatabaseInterface instance() throws EmanagerDatabaseException
    {
        if (databaseInterface == null) {
            databaseInterface = new DatabaseInterface();
        }

        return databaseInterface;
    }

    /**
     * @return int
     * @roseuid 3F4196A30210
     */
    private long getNextEventKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (eventSequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_event_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);

                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextHostKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (hostSequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_host_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextAppTypeKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (appTypeSequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_applicationtype_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextAppInstanceKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (appInstanceSequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_application_instance_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextMgmtPolicyKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (mgmtPolicySequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_managementpolicy_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextSecurityRoleKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (securityRoleSequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_security_role_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextUserAccountKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (userAccountSequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_user_account_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextPhysicalHierarchyKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (physicalHierarchySequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_physicalhierarchy_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                logger.debug("executing stored procedure \"" + cs.toString() +
                             "\"");
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextApplicationHierarchyKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (applicationHierarchySequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_applicationhierarchy_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextSolutionHierarchyKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (solutionHierarchySequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_solution_hierarchy_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextAuditLogKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (auditLogSequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_auditlog_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
            }
        }

        return key;
    }

    private long getNextSolutionKey() throws EmanagerDatabaseException
    {
        long key;
        ResultSetMetaData rsmd;
        Connection connection;
        CallableStatement cs;

        key = 0;
        connection = null;
        synchronized (solutionSequenceLock) {
            try {
                connection = DatabaseConnectionManager.instance().getConnection();
                cs = connection.prepareCall("{? = call get_solution_id()}");
                cs.registerOutParameter(1, Types.BIGINT);
                logger.debug("executing stored procedure \"" + cs.toString() +
                             "\"");
                cs.execute();
                key = cs.getLong(1);
                connection.close();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);
                try {
                    if (connection != null) {
                        connection.close();
                    }
                }
                catch (SQLException ee) {
                }

                throw ede;
            }
        }

        return key;
    }


        public void heartbeat() throws EmanagerDatabaseException
        {
            try {
                if (heartbeatConnection == null) {
                    heartbeatConnection = DatabaseConnectionManager.instance().getConnection();
                    heartbeatPreparedStatement =
                        heartbeatConnection.prepareStatement("select value from sequence where id = 'user';");
                }

                heartbeatPreparedStatement.executeQuery();
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);
                throw ede;
            }
        }

    /**
     * @param event
     * @return com.cisco.eManager.common.event.AbstractEventMessage
     * @roseuid 3F4197970289
     */
    public ManagedObjectId createEvent(AbstractEventMessage event) throws EmanagerDatabaseException
    {
        if (event != null) {
            if (event instanceof EmanagerEventMessage) {
                return createNewEmanagerEvent ((EmanagerEventMessage) event);
            } else if (event instanceof TibcoEventMessage) {
                return createNewTibcoEvent((TibcoEventMessage) event);
            }
            /*
                      // fix
                     else if (event instanceof ProcessSequencerEventMessage) {
                  return createNewProcessSequencerEvent((ProcessSequencerEventMessage) event);
              }
             */
        }

        return null;
    }

    private String convertDateToSQLDate (Date date)
    {
	StringBuffer buffer;
	GregorianCalendar calendar;

	buffer = new StringBuffer();
	calendar = new GregorianCalendar();
	calendar.setTime (date);

	buffer.append (Integer.toString (calendar.get (Calendar.YEAR)));
	buffer.append ("-");
	buffer.append (Integer.toString (calendar.get (Calendar.MONTH) + 1));
	buffer.append ("-");
	buffer.append (Integer.toString (calendar.get (Calendar.DAY_OF_MONTH)));
	buffer.append (" ");
	buffer.append (Integer.toString (calendar.get (Calendar.HOUR_OF_DAY)));
	buffer.append (":");
	buffer.append (Integer.toString (calendar.get (Calendar.MINUTE)));
	buffer.append (":");
	buffer.append (Integer.toString (calendar.get (Calendar.SECOND)));
	buffer.append (".");
	buffer.append (Integer.toString (calendar.get (Calendar.MILLISECOND)));

        return buffer.toString();
    }

    /**
     * @param event
     * @return com.cisco.eManager.eManager.event.TibcoEventMessage
     * @roseuid 3F4197F1000B
     */
    private ManagedObjectId createNewTibcoEvent(TibcoEventMessage event) throws EmanagerDatabaseException
    {
	long newEventId;
	ManagedObjectId eventObjectId;
	StringBuffer tibcoEventData;
	StringBuffer sqlStatement;
	Connection connection;
	Statement  statement;
	Date eventTime;

	sqlStatement = new StringBuffer();
	tibcoEventData = new StringBuffer();

	tibcoEventData.append (DatabaseGlobals.TibcoEventIdKey +
			       DatabaseGlobals.DatabaseEventKeyValueSeparator +
			       Long.toString (event.getTibcoEventId()));
	tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
			       DatabaseGlobals.TibcoRuleTextKey +
			       DatabaseGlobals.DatabaseEventKeyValueSeparator +
			       event.getRuleText());
	tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
			       DatabaseGlobals.TibcoRuleTestKey +
			       DatabaseGlobals.DatabaseEventKeyValueSeparator +
			       event.getRuleTestText());
        tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
			       DatabaseGlobals.TibcoInstrumentationNameKey +
			       DatabaseGlobals.DatabaseEventKeyValueSeparator +
			       event.getMicroagentId().name());
	// The mgmt policy is not set for the watch dog rulebase generated events.
	if (event.getManagementPolicyManagedObjectId() == null) {
	    tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
				   DatabaseGlobals.TibcoEventMgmtPolicyId +
				   DatabaseGlobals.DatabaseEventKeyValueSeparator +
				   Long.toString (0));
	} else {
	    tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
				   DatabaseGlobals.TibcoEventMgmtPolicyId +
				   DatabaseGlobals.DatabaseEventKeyValueSeparator +
				   Long.toString (event.getManagementPolicyManagedObjectId().getManagedObjectKey()));
	}

	eventTime = event.getEventTime();

	newEventId = getNextEventKey();
        eventObjectId = new ManagedObjectId (ManagedObjectIdType.Event, newEventId);

	sqlStatement.append ("INSERT INTO event " +
			     "(id, event_type, post_time, severity, display_text," +
			     " managed_object_type, managed_object_key, event_type_values)" +
			     " VALUES (" +
			     newEventId + ", " +
			     DatabaseGlobals.DatabaseTibcoEventType + ", " +
			     "'" + convertDateToSQLDate (eventTime) + "', " +
			     event.getSeverity().intValue() + ", " +
			     "'" + event.getDisplayText() + "', " +
			     event.getManagedObjectId().getManagedObjectType().intValue() + ", " +
			     event.getManagedObjectId().getManagedObjectKey() + ", " +
			     "'" + tibcoEventData.toString() + "');");

	connection = DatabaseConnectionManager.instance().getConnection();

        logger.debug("Database SQL statement issued: " + sqlStatement.toString());

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
	    statement = connection.createStatement();
	    statement.executeUpdate (sqlStatementString);
	    connection.commit();
            connection.close();
	}
	catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
	}

        return eventObjectId;
    }

    /**
     * @param event
     * @return com.cisco.eManager.common.event.EmanagerEventMessage
     * @roseuid 3F419838011E
     */
    private ManagedObjectId createNewEmanagerEvent(EmanagerEventMessage event) throws EmanagerDatabaseException
    {
        long newEventId;
        ManagedObjectId eventObjectId;
        StringBuffer tibcoEventData;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;
        Date eventTime;

        sqlStatement = new StringBuffer();

        eventTime = event.getEventTime();

        newEventId = getNextEventKey();

        eventObjectId = new ManagedObjectId (ManagedObjectIdType.Event, newEventId);

        sqlStatement.append ("INSERT INTO event " +
                             "(id, event_type, post_time, severity, display_text," +
                             " managed_object_type, managed_object_key)" +
                             " VALUES (" +
                             Long.toString (newEventId) + ", " +
                             Integer.toString (DatabaseGlobals.DatabaseTibcoEventType) + ", " +
                             "'" + convertDateToSQLDate (eventTime) + "', " +
                             Integer.toString (event.getSeverity().intValue()) + ", " +
                             "'" + event.getDisplayText() + "', " +
                             event.getManagedObjectId().getManagedObjectType().intValue() + ", " +
                             Long.toString (event.getManagedObjectId().getManagedObjectKey()) + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        logger.debug("Database SQL statement issued: " + sqlStatement.toString());

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();
            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return eventObjectId;
    }

    private ManagedObjectId createNewProcessSequencerEvent(EmanagerEventMessage event) throws EmanagerDatabaseException
    {
        long newEventId;
        ManagedObjectId eventObjectId;
        StringBuffer psEventData;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;
        Date eventTime;

        sqlStatement = new StringBuffer();
        psEventData = new StringBuffer();

        eventTime = event.getEventTime();

        newEventId = getNextEventKey();
        eventObjectId = new ManagedObjectId (ManagedObjectIdType.Event, newEventId);

        sqlStatement.append ("INSERT INTO event " +
                             "(id, event_type, post_time, severity, display_text," +
                             " managed_object_type, managed_object_key, event_type_values)" +
                             " VALUES (");

        connection = DatabaseConnectionManager.instance().getConnection();

        logger.debug("Database SQL statement issued: " + sqlStatement.toString());

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();
            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return eventObjectId;
    }

    /**
     * @param event
     * @roseuid 3F4198600358
     */
    public void updateEvent(EmanagerEventDetails event) throws EmanagerDatabaseException
    {
	Collection events;

        if (event == null) {
            return;
        }

	events = new LinkedList();
	events.add (event);
	updateEvents (events);
	return;
    }

    public void updateEvents (Collection events) throws EmanagerDatabaseException
    {
        Iterator iter;
        Connection connection;
        EmanagerEventDetails event;

        if (events == null ||
            events.isEmpty()) {
            return;
        }

        connection = DatabaseConnectionManager.instance().getConnection();

        iter = events.iterator();
        while (iter.hasNext()) {
            event = (EmanagerEventDetails) iter.next();
            try {
                if (event instanceof EmanagerEventDetails)
                {
                    updateEvent((EmanagerEventDetails)event,
                                connection);
                }
                else if (event instanceof TibcoEventDetails)
                {
                    updateEvent((TibcoEventDetails)event,
                                connection);
                }
                else if (event instanceof ProcessSequencerEventDetails)
                {
                    updateEvent((ProcessSequencerEventDetails)event,
                                connection);
                }
            }
            catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                    logString);

                try {
                    connection.rollback();
                    connection.close();
                }
                catch (SQLException ee) {

                }

                throw ede;
            }
        }

        try {
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }
    }

    // fix
    // figure out how to store nulls in the event specific fields.
    private void updateEvent(TibcoEventDetails event,
                             Connection connection) throws SQLException
    {
	long newEventId;
        String postTimeString;
        String clearTimeString;
        Statement  statement;
	ManagedObjectId eventObjectId;
	StringBuffer tibcoEventData;
	StringBuffer sqlStatement;

	sqlStatement = new StringBuffer();
	tibcoEventData = new StringBuffer();

	tibcoEventData.append (DatabaseGlobals.TibcoEventIdKey +
			       DatabaseGlobals.DatabaseEventKeyValueSeparator +
			       Long.toString (event.getTibcoEventId()));
	tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
			       DatabaseGlobals.TibcoRuleTextKey +
			       DatabaseGlobals.DatabaseEventKeyValueSeparator +
			       event.getRuleText());
	tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
			       DatabaseGlobals.TibcoRuleTestKey +
			       DatabaseGlobals.DatabaseEventKeyValueSeparator +
			       event.getRuleTestText());
        tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
                               DatabaseGlobals.TibcoInstrumentationNameKey +
                               DatabaseGlobals.DatabaseEventKeyValueSeparator +
                               event.getInstrumentationName());
	// The mgmt policy is not set for the watch dog rulebase generated events.
	if (event.getManagementPolicyId() == null) {
	    tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
				   DatabaseGlobals.TibcoEventMgmtPolicyId +
				   DatabaseGlobals.DatabaseEventKeyValueSeparator +
				   Long.toString (0));
	} else {
	    tibcoEventData.append (DatabaseGlobals.DatabaseEventParmDelimeter +
				   DatabaseGlobals.TibcoEventMgmtPolicyId +
				   DatabaseGlobals.DatabaseEventKeyValueSeparator +
				   Long.toString (event.getManagementPolicyId().getManagedObjectKey()));
	}

	postTimeString = new String ("'" + convertDateToSQLDate (event.getPostTime()) + "'");
        if (event.getClearTime() != null) {
            clearTimeString = new String ("'" + convertDateToSQLDate (event.getClearTime()) + "'");
        } else {
            clearTimeString = new String ("NULL");
        }

	sqlStatement.append ("UPDATE event SET ");

        sqlStatement.append("post_time=" + postTimeString + ", ");
        sqlStatement.append("clear_time=" + clearTimeString + ", ");

        sqlStatement.append("acknowledgement_user_id=");
        if (event.getAcknowledgement() != null &&
            event.getAcknowledgement().getUserId() != null) {
            sqlStatement.append("'" + event.getAcknowledgement().getUserId() + "', ");
        } else {
            sqlStatement.append("NULL, ");
        }

        sqlStatement.append("acknowledgement_time=");
        if (event.getAcknowledgement() != null &&
            event.getAcknowledgement().getDate() != null) {
            sqlStatement.append("'" + convertDateToSQLDate (event.getAcknowledgement().getDate()) + "', ");
        } else {
            sqlStatement.append ("NULL, ");
        }

        sqlStatement.append("acknowledgement_comment=");
        if (event.getAcknowledgement() != null &&
            event.getAcknowledgement().getComment() != null) {
            sqlStatement.append("'" + event.getAcknowledgement().getComment() + "', ");
        } else {
            sqlStatement.append ("NULL, ");
        }

        sqlStatement.append("severity=" + event.getSeverity().intValue() + ", ");
        sqlStatement.append("display_text='" + event.getDisplayText() + "', ");
        sqlStatement.append("managed_object_type=" +
                            event.getManagedObjectId().getManagedObjectType().intValue() +
                            ", ");
        sqlStatement.append("managed_object_key=" +
                            event.getManagedObjectId().getManagedObjectKey() +
                            ", ");
        sqlStatement.append("event_type_values='" + tibcoEventData.toString() + "' ");

        sqlStatement.append("WHERE id=" + event.getObjectId().getManagedObjectKey() + ";");

        logger.debug("Database SQL statement issued: " + sqlStatement.toString());

        statement = connection.createStatement();
        statement.executeUpdate (sqlStatement.toString());
    }

    private void updateEvent (EmanagerEventDetails event,
                              Connection connection) throws SQLException
    {
        long newEventId;
        String postTimeString;
        String clearTimeString;
        Statement  statement;
        ManagedObjectId eventObjectId;
        StringBuffer sqlStatement;

        sqlStatement = new StringBuffer();

        postTimeString = new String ("'" + convertDateToSQLDate (event.getPostTime()) + "'");
        if (event.getClearTime() != null) {
            clearTimeString = new String ("'" + convertDateToSQLDate (event.getClearTime()) + "'");
        } else {
            clearTimeString = new String ("NULL");
        }

        sqlStatement.append ("UPDATE event SET ");

        sqlStatement.append("post_time=" + postTimeString + ", ");
        sqlStatement.append("clear_time=" + clearTimeString + ", ");

        sqlStatement.append("acknowledgement_user_id=");
        if (event.getAcknowledgement() != null &&
            event.getAcknowledgement().getUserId() != null) {
            sqlStatement.append("'" + event.getAcknowledgement().getUserId() + "', ");
        } else {
            sqlStatement.append("NULL, ");
        }

        sqlStatement.append("acknowledgement_time=");
        if (event.getAcknowledgement() != null &&
            event.getAcknowledgement().getDate() != null) {
            sqlStatement.append("'" + convertDateToSQLDate (event.getAcknowledgement().getDate()) + "', ");
        } else {
            sqlStatement.append ("NULL, ");
        }

        sqlStatement.append("acknowledgement_comment=");
        if (event.getAcknowledgement() != null &&
            event.getAcknowledgement().getComment() != null) {
            sqlStatement.append("'" + event.getAcknowledgement().getComment() + "', ");
        } else {
            sqlStatement.append ("NULL, ");
        }

        sqlStatement.append("severity=" + event.getSeverity().intValue() + ", ");
        sqlStatement.append("display_text='" + event.getDisplayText() + "', ");
        sqlStatement.append("managed_object_type=" +
                            event.getManagedObjectId().getManagedObjectType().intValue() +
                            ", ");
        sqlStatement.append("managed_object_key=" +
                            event.getManagedObjectId().getManagedObjectKey() + " ");

        sqlStatement.append("WHERE id=" + event.getObjectId().getManagedObjectKey() + ";");

        logger.debug("Database SQL statement issued: " + sqlStatement.toString());

        statement = connection.createStatement();
        statement.executeUpdate (sqlStatement.toString());
    }

    private void updateEvent (ProcessSequencerEventDetails event,
                              Connection connection) throws SQLException
    {
        long newEventId;
        String postTimeString;
        String clearTimeString;
        String sqlStatementString;
        Statement  statement;
        ManagedObjectId eventObjectId;
        StringBuffer sqlStatement;

    }

    /**
     * @param criteria
     * @roseuid 3F4198BD0048
     */
    public void removeEvents(EventDeletionCriteria criteria) throws EmanagerDatabaseException
    {
	int  totalEventIdsDeleted;
	List totalEventIdSubset;
	List searchIdSet;
	String	postDateSearchStatement;
	String	clearDateSearchStatement;
	String	acknowledgementSearchStatement;
	String	eventIdSearchStatement;
	String	severitySearchStatement;
	String	managedObjectKeysStatment;
	boolean criteriaFound;
	StringBuffer sqlStatement;
	Connection connection;
	Statement  statement;

        totalEventIdSubset = new LinkedList();

        if (criteria.getClearDate() != null &&
            criteria.getClearDate().getStartDate() == null &&
            criteria.getClearDate().getEndDate() == null) {
            String logString;
            EmanagerDatabaseException ede;

            logString = EmanagerDatabaseStatusCode.IllegalRemoveEventQualifier.getStatusCodeDescription();

            logger.log(Priority.ERROR, logString);
            ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                 logString);
            throw ede;
        }

	if (criteria.getTibcoDeletionCriteria() != null) {
	    totalEventIdSubset = retrieveTibcoEventIds(true, criteria.getTibcoDeletionCriteria());
	}

	if (criteria.getProcessSequencerDeletionCriteria() != null) {
	    totalEventIdSubset.addAll (retrieveProcessSequencerEventIds (true, criteria.getProcessSequencerDeletionCriteria()));
	}

	postDateSearchStatement = getPostDateSearchStatement (criteria.getPostDate());
	clearDateSearchStatement = getClearDateSearchStatement (criteria.getClearDate());
	acknowledgementSearchStatement = getAcknowledgementSearchStatement (criteria.getAcknowledgement());
	eventIdSearchStatement = getEventIdSearchStatement (criteria.getEmanagerEventId());
	severitySearchStatement = getSeveritySearchStatement (criteria.getSeverity());
	managedObjectKeysStatment = getManagedObjectIdSearchStatement (criteria.getManagedObjectIds());

        if (criteria.getTibcoDeletionCriteria() != null ||
            criteria.getProcessSequencerDeletionCriteria() != null) {
            if (totalEventIdSubset.size() == 0) {
                return;
            }
        }

        if (criteria.getTibcoDeletionCriteria() == null &&
            criteria.getProcessSequencerDeletionCriteria() == null) {
            if (postDateSearchStatement == null        &&
                clearDateSearchStatement == null       &&
                acknowledgementSearchStatement == null &&
                eventIdSearchStatement == null         &&
                severitySearchStatement == null        &&
                managedObjectKeysStatment == null) {
                // no criteria
                return;
            }
	}

	connection = DatabaseConnectionManager.instance().getConnection();

	try {
	    statement = connection.createStatement();
	}
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

	try {

	    totalEventIdsDeleted = 0;
	    do {
		sqlStatement = new StringBuffer();
		sqlStatement.append ("DELETE FROM event WHERE clear_time<>null AND ");

		criteriaFound = false;
		if (totalEventIdsDeleted < totalEventIdSubset.size()) {
		    criteriaFound = true;
		    sqlStatement.append ("id IN (");
		    for (int x = 0; x < DatabaseGlobals.DatabaseMaximumQuerySubsetSize &&
                                 totalEventIdsDeleted < totalEventIdSubset.size(); x++) {
			sqlStatement.append ( ((Long)totalEventIdSubset.get(totalEventIdsDeleted)).toString());

			if (totalEventIdsDeleted < (totalEventIdSubset.size() - 1) &&
			    x < (DatabaseGlobals.DatabaseMaximumQuerySubsetSize - 1)) {
			    sqlStatement.append (",");
			}

                        totalEventIdsDeleted++;
		    }

		    sqlStatement.append (") ");
		}

		if (postDateSearchStatement != null &&
                    postDateSearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (postDateSearchStatement);
		}

		if (clearDateSearchStatement != null &&
                    clearDateSearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (clearDateSearchStatement);
		}

		if (acknowledgementSearchStatement != null &&
                    acknowledgementSearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (acknowledgementSearchStatement);
		}

		if (eventIdSearchStatement != null &&
                    eventIdSearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (eventIdSearchStatement);
		}

		if (severitySearchStatement != null &&
                    severitySearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (severitySearchStatement);
		}

		if (managedObjectKeysStatment != null &&
                    managedObjectKeysStatment.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (managedObjectKeysStatment);
		}

                sqlStatement.append(";");

                logger.debug("Database SQL statement issued: " + sqlStatement.toString());

		statement.executeUpdate (sqlStatement.toString());

	    } while (totalEventIdsDeleted < totalEventIdSubset.size());

	    connection.commit();
	    connection.close();
	}
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }
    }

    private String getPostDateSearchStatement (DateSearchCriteria dateSearchCriteria)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (dateSearchCriteria != null) {
            if (dateSearchCriteria.getStartDate() != null &&
                dateSearchCriteria.getEndDate() != null){
                sqlClause.append(" post_time BETWEEN '");
                sqlClause.append(convertDateToSQLDate(dateSearchCriteria.getStartDate()));
                sqlClause.append("' AND '");
                sqlClause.append(convertDateToSQLDate(dateSearchCriteria.getEndDate()));
                sqlClause.append("' ");
            }
            else{
                sqlClause.append("post_time=null ");
            }
        }

	return sqlClause.toString();
    }

    private String getClearDateSearchStatement (DateSearchCriteria dateSearchCriteria)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (dateSearchCriteria != null) {
            if (dateSearchCriteria.getStartDate() != null &&
                dateSearchCriteria.getEndDate() != null) {
                sqlClause.append(" clear_time BETWEEN '");
                sqlClause.append(convertDateToSQLDate(dateSearchCriteria.getStartDate()));
                sqlClause.append("' AND '");
                sqlClause.append(convertDateToSQLDate(dateSearchCriteria.getEndDate()));
                sqlClause.append("' ");
            } else {
                sqlClause.append ("clear_time=null");
            }
	}

	return sqlClause.toString();
    }

    private String getAcknowledgementSearchStatement (AcknowledgementSearchCriteria acknowledgementSearchCriteria)
    {
	StringBuffer sqlClause;
	StringSearchCriteria userId;

	sqlClause = new StringBuffer();

	if (acknowledgementSearchCriteria != null) {
	    if (acknowledgementSearchCriteria.getUserId() != null) {
                userId = acknowledgementSearchCriteria.getUserId();

                if (userId.getString() == null) {
                    sqlClause.append (" acknowledgement_user_id=null ");
                } else {
                    sqlClause.append(" acknowledgement_user_id like '%" +
				     userId.getString() +
				     "%' ");
		}
	    }

	    if (acknowledgementSearchCriteria.getAcknowledgementTime() != null) {
		DateSearchCriteria dateSearchCriteria;

                dateSearchCriteria = acknowledgementSearchCriteria.getAcknowledgementTime();

                if (acknowledgementSearchCriteria.getUserId() != null) {
                    sqlClause.append ("AND");
                }

                if (dateSearchCriteria.getStartDate() != null &&
                    dateSearchCriteria.getEndDate() != null) {
                    sqlClause.append(" acknowledgement_time BETWEEN '");
                    sqlClause.append(convertDateToSQLDate(dateSearchCriteria.getStartDate()));
                    sqlClause.append("' AND '");
                    sqlClause.append(convertDateToSQLDate(dateSearchCriteria.getEndDate()));
                    sqlClause.append("' ");
                } else {
                    sqlClause.append ("acknowledgement_time=null ");
                }
	    }
	}

	return sqlClause.toString();
    }

    private String getEventIdSearchStatement (NumericSearchCriteria numericSearchCriteria)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (numericSearchCriteria != null) {
	    if (numericSearchCriteria.getStart().longValue() ==
		numericSearchCriteria.getEnd().longValue()) {
		sqlClause.append (" id=");
		sqlClause.append (numericSearchCriteria.getStart().longValue());
		sqlClause.append (" ");
	    } else {
		sqlClause.append (" id>=");
		sqlClause.append (numericSearchCriteria.getStart().longValue());
		sqlClause.append (" AND id<=");
		sqlClause.append (numericSearchCriteria.getEnd().longValue());
		sqlClause.append (" ");
	    }
	}

	return sqlClause.toString();
    }

    private String getSeveritySearchStatement (Collection severities)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (severities != null &&
            severities.size() != 0) {
	    boolean first;
	    Iterator iter;
	    EventSeverity severity;

	    sqlClause.append ("(");
	    iter = severities.iterator();
	    first = true;
	    while (iter.hasNext()) {
		if (first == false) {
		    sqlClause.append (" OR ");
		} else {
		    first = false;
		}

		severity = (EventSeverity) iter.next();

		sqlClause.append (" severity=");
		sqlClause.append (severity.intValue());
	    }

	    sqlClause.append (")");
	}

	return sqlClause.toString();
    }

    private String getManagedObjectIdSearchStatement (Collection managedObjectIds)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (managedObjectIds != null &&
            managedObjectIds.size() != 0) {
	    boolean first;
	    Iterator iter;
	    ManagedObjectId id;

	    sqlClause.append ("(");
	    iter = managedObjectIds.iterator();
	    first = true;
	    while (iter.hasNext()) {
		if (first == false) {
		    sqlClause.append (" OR ");
		} else {
		    first = false;
		}

		id = (ManagedObjectId) iter.next();

		sqlClause.append ("(managed_object_type=");
		sqlClause.append (id.getManagedObjectType().intValue());
                sqlClause.append (")");
		sqlClause.append (" AND ");
		sqlClause.append ("(managed_object_key=");
		sqlClause.append (id.getManagedObjectKey());
                sqlClause.append (")");
	    }

	    sqlClause.append (")");
	}

	return sqlClause.toString();
    }


    private List retrieveTibcoEventIds (boolean deletionCriteria,
                                        AbstractTibcoEventSearchCriteria tibcoSearchCriteria) throws EmanagerDatabaseException
    {
	long   eventId;
	String eventValues;
	boolean criteriaFound;
	StringBuffer sqlStatement;
	List eventIds;
	ResultSet  rs;
	Connection connection;
	Statement statement;

	eventIds = new LinkedList();
        sqlStatement = new StringBuffer();

	if (tibcoSearchCriteria == null                          &&
	    tibcoSearchCriteria.getTibcoEventId() == null        &&
	    tibcoSearchCriteria.getManagementPolicyIds() == null &&
	    tibcoSearchCriteria.getRule() == null                &&
	    tibcoSearchCriteria.getTest() == null                &&
	    tibcoSearchCriteria.getInstrumentation() == null) {
	    return eventIds;
	}

	sqlStatement.append ("SELECT id, event_type_values FROM event WHERE event_type = " +
			     Integer.toString (DatabaseGlobals.DatabaseTibcoEventType));
        if (deletionCriteria == true) {
            sqlStatement.append(" AND clear_time <> null");
        }
        sqlStatement.append(";");

        rs = null;
        connection = null;
        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sqlStatement.toString());

            while (rs.next()){
                eventId = rs.getLong(1);
                eventValues = rs.getString(2);

                if (tibcoSearchCriteria.meetsCriteria(eventValues) == true){
                    eventIds.add(new Long(eventId));
                }
            }

            rs.close();
            connection.close();
        }
        catch (SQLException e) {
            String logString;
            EmanagerDatabaseException ede;

            logString =
                EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                e.getMessage();

            logger.log(Priority.ERROR, logString);
            ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                 logString);

            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException ee) {
                }
            }

            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                }
                catch (SQLException ee) {
                }
            }
        }

	return eventIds;
    }


    private List retrieveProcessSequencerEventIds (boolean deletionCriteria,
                                                   AbstractProcessSequencerEventSearchCriteria psSearchCriteria)
    {
	List eventIds;

	eventIds = new LinkedList();

	return eventIds;
    }

    /**
     * @param criteria
     * @return Collection
     * @roseuid 3F41991D019F
     */
    public Collection retrieveEvents(EventSearchCriteria criteria) throws EmanagerDatabaseException
    {
	int  totalEventIdsDeleted;
	List totalEventIdSubset;
	List searchIdSet;
	String	postDateSearchStatement;
	String	clearDateSearchStatement;
	String	acknowledgementSearchStatement;
	String	eventIdSearchStatement;
	String	severitySearchStatement;
	String	managedObjectKeysStatment;
        boolean criteriaFound;
        boolean subsetCriteria;
	StringBuffer sqlStatement;
	Connection connection;
	Statement  statement;

	ResultSet rs;
	Collection events;
        long eventKey;
	int eventType;
	Timestamp postTime;
	Timestamp clearTime;
        String ackUserId;
        Timestamp ackTime;
        String ackComment;
        int severityValue;
	EventSeverity severity;
	String displayText;
        int  objectType;
	long objectKey;
        ManagedObjectId managedObjectId;
	String eventTypeValues;
        EmanagerEventDetails event;
        EventAcknowledgement eventAcknowledgement;


	rs = null;
	events = new LinkedList();
        totalEventIdSubset = new LinkedList();

        if (criteria == null) {
            return events;
        }

	if (criteria.getTibcoSearchCriteria() != null) {
	    totalEventIdSubset = retrieveTibcoEventIds(false, criteria.getTibcoSearchCriteria());
	}

	if (criteria.getProcessSequencerSearchCriteria() != null) {
	    totalEventIdSubset.addAll (retrieveProcessSequencerEventIds (false, criteria.getProcessSequencerSearchCriteria()));
	}

	postDateSearchStatement = getPostDateSearchStatement (criteria.getPostDate());
	clearDateSearchStatement = getClearDateSearchStatement (criteria.getClearDate());
	acknowledgementSearchStatement = getAcknowledgementSearchStatement (criteria.getAcknowledgement());
	eventIdSearchStatement = getEventIdSearchStatement (criteria.getEmanagerEventId());
	severitySearchStatement = getSeveritySearchStatement (criteria.getSeverity());
	managedObjectKeysStatment = getManagedObjectIdSearchStatement (criteria.getManagedObjectIds());

        if (criteria.getTibcoSearchCriteria() != null ||
            criteria.getProcessSequencerSearchCriteria() != null) {
            if (totalEventIdSubset.size() == 0) {
                return events;
            }
        }

        if (criteria.getTibcoSearchCriteria() == null &&
            criteria.getProcessSequencerSearchCriteria() == null) {
            if (postDateSearchStatement == null        &&
                clearDateSearchStatement == null       &&
                acknowledgementSearchStatement == null &&
                eventIdSearchStatement == null         &&
                severitySearchStatement == null        &&
                managedObjectKeysStatment == null) {
                // no criteria
                return events;
            }
	}

	connection = DatabaseConnectionManager.instance().getConnection();

	try {
	    statement = connection.createStatement();
	}
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

	try {

	    totalEventIdsDeleted = 0;
	    do {
		sqlStatement = new StringBuffer();
		sqlStatement.append ("SELECT id, event_type, post_time, clear_time, " +
				     "acknowledgement_user_id, acknowledgement_time, acknowledgement_comment, " +
				     "severity, display_text, managed_object_type, managed_object_key, " +
				     "event_type_values " +
				     "FROM event WHERE ");

		criteriaFound = false;
		if (totalEventIdsDeleted < totalEventIdSubset.size()) {
		    criteriaFound = true;
		    sqlStatement.append ("id IN (");
		    for (int x = 0; x < DatabaseGlobals.DatabaseMaximumQuerySubsetSize &&
                                 totalEventIdsDeleted < totalEventIdSubset.size(); x++) {
			sqlStatement.append ( ((Long)totalEventIdSubset.get(totalEventIdsDeleted)).toString());

			if (totalEventIdsDeleted < (totalEventIdSubset.size() - 1) &&
			    x < (DatabaseGlobals.DatabaseMaximumQuerySubsetSize - 1)) {
			    sqlStatement.append (",");
			}

                        totalEventIdsDeleted++;
		    }

		    sqlStatement.append (") ");
		}

		if (postDateSearchStatement != null &&
                    postDateSearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (postDateSearchStatement);
		}

		if (clearDateSearchStatement != null &&
                    clearDateSearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (clearDateSearchStatement);
		}

		if (acknowledgementSearchStatement != null &&
                    acknowledgementSearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (acknowledgementSearchStatement);
		}

		if (eventIdSearchStatement != null &&
                    eventIdSearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (eventIdSearchStatement);
		}

		if (severitySearchStatement != null &&
                    severitySearchStatement.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (severitySearchStatement);
		}

		if (managedObjectKeysStatment != null &&
                    managedObjectKeysStatment.trim().length() != 0) {
                    if (criteriaFound == true) {
                        sqlStatement.append(" AND ");
                    } else {
                        criteriaFound = true;
                    }
		    sqlStatement.append (managedObjectKeysStatment);
		}

                sqlStatement.append(";");

                logger.debug("Database SQL statement issued: " + sqlStatement.toString());

		rs = statement.executeQuery (sqlStatement.toString());
		while (rs.next()) {
                    eventKey = rs.getLong("id");
                    eventType = rs.getInt("event_type");
                    postTime = rs.getTimestamp("post_time");
                    clearTime = rs.getTimestamp("clear_time");
                    ackUserId = rs.getString("acknowledgement_user_id");
                    ackTime = rs.getTimestamp("acknowledgement_time");
                    ackComment = rs.getString("acknowledgement_comment");

                    severityValue = rs.getInt("severity");
                    if (severityValue == EventSeverity.Informational.intValue()){
                        severity = EventSeverity.Informational;
                    }
                    else if (severityValue == EventSeverity.Low.intValue()){
                        severity = EventSeverity.Low;
                    }
                    else if (severityValue == EventSeverity.Medium.intValue()){
                        severity = EventSeverity.Medium;
                    }
                    else if (severityValue == EventSeverity.High.intValue()){
                        severity = EventSeverity.High;
                    }
                    else{
                        severity = EventSeverity.Informational;
                    }

                    displayText = rs.getString("display_text");
                    objectType = Integer.parseInt(rs.getString("managed_object_type"));
                    objectKey = Long.parseLong(rs.getString("managed_object_key"));
                    if (objectType == ManagedObjectIdType.ApplicationHierarchyContainer.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.ApplicationHierarchyContainer,
                                                objectKey);
                    } else if (objectType == ManagedObjectIdType.ApplicationInstance.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.ApplicationInstance,
                                                objectKey);
                    } else if (objectType == ManagedObjectIdType.ApplicationType.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.ApplicationType,
                                                objectKey);
                    } else if (objectType == ManagedObjectIdType.Event.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.Event,
                                                objectKey);
                    } else if (objectType == ManagedObjectIdType.HostAgent.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.HostAgent,
                                                objectKey);
                    } else if (objectType == ManagedObjectIdType.Instrumentation.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.Instrumentation,
                                                objectKey);
                    } else if (objectType == ManagedObjectIdType.MgmtPolicy.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.MgmtPolicy,
                                                objectKey);
                    } else if (objectType == ManagedObjectIdType.PhysicalHierarchyContainer.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.PhysicalHierarchyContainer,
                                                objectKey);
                    } else if (objectType == ManagedObjectIdType.UserAccount.intValue()){
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.UserAccount,
                                                objectKey);
                    } else {
                        managedObjectId =
                            new ManagedObjectId(ManagedObjectIdType.ApplicationInstance,
                                                objectKey);
                    }

                    eventTypeValues = rs.getString("event_type_values");

                    if (ackTime != null) {
                        eventAcknowledgement = new EventAcknowledgement (ackUserId,
									 new Date (ackTime.getTime()),
									 ackComment);
                    } else {
                        eventAcknowledgement = null;
                    }

                    if (eventType == DatabaseGlobals.DatabaseEmanagerEventType) {
                        event =
                            new EmanagerEventDetails (eventKey,
						      postTime,
						      clearTime,
						      severity,
						      managedObjectId,
						      eventAcknowledgement,
						      displayText);
                    } else if (eventType == DatabaseGlobals.DatabaseTibcoEventType) {
                        event =
                            new TibcoEventDetails (eventKey,
						   postTime,
						   clearTime,
						   severity,
						   managedObjectId,
						   eventAcknowledgement,
						   displayText,
						   eventTypeValues);
                    } else if (eventType == DatabaseGlobals.DatabaseProcessSequencerEventType) {
                        event =
                            new ProcessSequencerEventDetails (eventKey,
							      postTime,
							      clearTime,
							      severity,
							      managedObjectId,
							      eventAcknowledgement,
							      displayText,
							      eventTypeValues);
                    } else {
                        // fix
                        // write some error here
                        event =
                            new EmanagerEventDetails (eventKey,
						      postTime,
						      clearTime,
						      severity,
						      managedObjectId,
						      eventAcknowledgement,
						      displayText);
                    }

                    events.add(event);
                }
	    } while (totalEventIdsDeleted < totalEventIdSubset.size());

	    connection.close();
	}
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return events;
    }

    private void removeObjects (Collection ids) throws EmanagerDatabaseException
    {
        Iterator iter;
        ManagedObjectId id;
        ManagedObjectId iterId;
        StringBuffer sqlStatement;
	StringBuffer sqlStatement2;
	StringBuffer sqlStatement3;
        Connection connection;
        Statement  statement;

        sqlStatement = new StringBuffer();
	sqlStatement2 = null;
	sqlStatement3 = null;

        if (ids == null ||
            ids.isEmpty()) {
            return;
        }

        sqlStatement.append ("DELETE FROM ");

        iter = ids.iterator();
        id = (ManagedObjectId) iter.next();

        if (id.getManagedObjectType().equals(ManagedObjectIdType.ApplicationHierarchyContainer)) {
                sqlStatement.append ("application_hierarchy WHERE id IN (");
		sqlStatement2 = new StringBuffer ("DELETE FROM application_relationship WHERE application_hierarchy_id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.ApplicationInstance)) {
            sqlStatement.append ("application_instance WHERE id IN (");
	    sqlStatement2 = new StringBuffer ("DELETE FROM solution_instance_relationship WHERE application_instance_id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.ApplicationType)) {
            sqlStatement.append ("application_type WHERE id IN (");
	    sqlStatement2 = new StringBuffer ("DELETE FROM application_relationship WHERE application_type_id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.HostAgent)) {
            sqlStatement.append ("host WHERE id IN (");
	    sqlStatement2 = new StringBuffer ("DELETE FROM physical_relationship WHERE host_id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.MgmtPolicy)) {
            sqlStatement.append ("management_policy WHERE id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.PhysicalHierarchyContainer)) {
            sqlStatement.append ("physical_hierarchy WHERE id IN (");
	    sqlStatement2 = new StringBuffer ("DELETE FROM physical_relationship WHERE physical_hierarchy_id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.UserAccount)) {
            sqlStatement.append ("user_account WHERE id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.SecurityRole)) {
            sqlStatement.append ("security_role WHERE id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.AuditLog)) {
            sqlStatement.append ("audit_log WHERE id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.Solution)) {
            sqlStatement.append ("solution WHERE id IN (");
	    sqlStatement2 = new StringBuffer ("DELETE FROM solution_instance_relationship WHERE solution_id IN (");
	    sqlStatement3 = new StringBuffer ("DELETE FROM solution_hierarchy_relationship WHERE solution_id IN (");
        } else if (id.getManagedObjectType().equals(ManagedObjectIdType.SolutionHierarchyContainer)) {
            sqlStatement.append ("solution_hierarchy WHERE id IN (");
            sqlStatement2 = new StringBuffer ("DELETE FROM solution_hierarchy_relationship WHERE solution_hierarchy_id IN (");
        } else {
            // fix
            // throw exception here
        }

        iter = ids.iterator();
        while (iter.hasNext()) {
            iterId = (ManagedObjectId) iter.next();
            if (iterId.getManagedObjectType() != id.getManagedObjectType()) {
                logger.log(Priority.ERROR,
			   EmanagerDatabaseStatusCode.FoundMixedObjectTypes.getStatusCodeDescription());
                throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.FoundMixedObjectTypes,
                                                     EmanagerDatabaseStatusCode.FoundMixedObjectTypes.getStatusCodeDescription());
            }

            sqlStatement.append(iterId.getManagedObjectKey() + ", ");
	    if (sqlStatement2 != null) {
		sqlStatement2.append (iterId.getManagedObjectKey() + ", ");
	    }
	    if (sqlStatement3 != null) {
		sqlStatement3.append (iterId.getManagedObjectKey() + ", ");
	    }
        }

        sqlStatement.setCharAt(sqlStatement.length() - 2, ')');
        sqlStatement.setCharAt(sqlStatement.length() - 1, ';');

	if (sqlStatement2 != null) {
	    sqlStatement2.setCharAt(sqlStatement2.length() - 2, ')');
	    sqlStatement2.setCharAt(sqlStatement2.length() - 1, ';');
	}

	if (sqlStatement3 != null) {
	    sqlStatement3.setCharAt(sqlStatement3.length() - 2, ')');
	    sqlStatement3.setCharAt(sqlStatement3.length() - 1, ';');
	}

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            if (sqlStatement3 != null) {
                logger.debug("Database SQL statement issued: " + sqlStatement3.toString());
                statement = connection.createStatement();
                statement.executeUpdate (sqlStatement3.toString());
            }

	    if (sqlStatement2 != null) {
                logger.debug("Database SQL statement issued: " + sqlStatement2.toString());
		statement = connection.createStatement();
		statement.executeUpdate (sqlStatement2.toString());
	    }

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());
            statement = connection.createStatement();
            statement.executeUpdate (sqlStatement.toString());

            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
            String logString;
            EmanagerDatabaseException ede;

            logString =
                EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                e.getMessage();

            logger.log(Priority.ERROR, logString);
            ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                 logString);

            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                }
                catch (SQLException closeE) {
                }
            }

            throw ede;
        }

        return;
    }

    /**
     * @param agentHost The agent host object to store in the database.
     * @return The agent host object that includes the database key.
     * @roseuid 3F424DDB0045
     */
    public HostData createAgent(String ipAddress,
                                String name,
                                String domainName)
        throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        sqlStatement = new StringBuffer();
        newId = getNextHostKey();
        id = new ManagedObjectId (ManagedObjectIdType.HostAgent, newId);

        HostData hostData = new HostData(id,
                                         ipAddress,
                                         domainName,
                                         name);

        sqlStatement.append("INSERT INTO host " +
			    "(id, ip_address_string, tibhawk_domain, dns_hostname)" +
			    " VALUES (" +
			    hostData.id().getManagedObjectKey() + ", " +
			    "'" + hostData.ipAddress() + "', " +
			    "'" + hostData.domain() + "', " +
			    "'" + hostData.hostname() + "');");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return hostData;
    }

    /**
     * @param agentHost The agent host information to update in the database.
     * @roseuid 3F424DED027C
     */
    public void updateAgent(Host host) throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;
        AgentId agentId;
        String dnsName;
        String tibhawkDomainName;

        if (host == null) {
            return;
        }

        sqlStatement = new StringBuffer();

        agentId = host.networkId();

        dnsName = agentId.dns();
        tibhawkDomainName = agentId.tibhawkDomain();

        sqlStatement.append ("UPDATE host SET ");

        sqlStatement.append ("tibhawk_domain='" + tibhawkDomainName + "', ");
        sqlStatement.append ("dns_hostname='" + dnsName + "'");
        sqlStatement.append (" WHERE id=" + host.id().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement = connection.createStatement();
            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public Collection retrieveAgents() throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection objects;
        long key;
        ManagedObjectId id;
        String ipString;
        String tibhawkDomain;
        String dnsName;
        HostData data;

        objects = new LinkedList();
        connection = null;

        sqlStatement =
	    "SELECT id, ip_address_string, tibhawk_domain, dns_hostname " +
	    "FROM host;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong("id");
                id = new ManagedObjectId (ManagedObjectIdType.HostAgent, key);
                ipString = rs.getString("ip_address_string");
                tibhawkDomain = rs.getString("tibhawk_domain");
                dnsName = rs.getString("dns_hostname");

                data = new HostData (id, ipString, tibhawkDomain, dnsName);
                objects.add (data);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return objects;
    }

    public HostData retrieveAgent(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        StringBuffer sqlStatement;

        String ipString;
        String tibhawkDomain;
        String dnsName;
        HostData data;

        data = null;
        connection = null;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.HostAgent) {
            return null;
        }

        sqlStatement = new StringBuffer ("SELECT ip_address_string, tibhawk_domain, dns_hostname " +
                                         "FROM host WHERE id=");
        sqlStatement.append (id.getManagedObjectKey() + ";");

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement.toString());
            while (rs.next()) {
                ipString = rs.getString("ip_address_string");
                tibhawkDomain = rs.getString("tibhawk_domain");
                dnsName = rs.getString("dns_hostname");

                data = new HostData (id, ipString, tibhawkDomain, dnsName);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return data;
    }


    /**
     * @param agentHostKey The key of the agent host to remove from the database.
     * @roseuid 3F424DF603B5
     */
    public void removeAgent(Host host) throws EmanagerDatabaseException
    {
        Collection ids;

        if (host == null) {
            return;
        }

        ids = new LinkedList();
        ids.add(host.id());
        removeObjects (ids);
    }

    /**
     * @param agentHostKeys The collection of agent host keys to remove from the
     * database.
     * @roseuid 3F424DFD02C5
     */
    public void removeAgents(Collection hosts) throws EmanagerDatabaseException
    {
        Iterator iter;
        Collection ids;

        if (hosts == null ||
            hosts.isEmpty()) {
            return;
        }

        ids = new LinkedList();
        iter = hosts.iterator();
        while (iter.hasNext()) {
            ids.add ( ((Host)iter.next()).id());
        }

        removeObjects (ids);
    }

    /**
     * @param applicationType The application type to store in the database.
     * @return The applicationType object is returned with the database key.
     * @roseuid 3F424F8103B3
     */
    public ManagedObjectId createApplicationType(AppType applicationType) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        String version;

        id = null;
        sqlStatement = new StringBuffer();

        if (applicationType == null) {
            return id;
        }

        name = applicationType.name();
        version = applicationType.version();

        newId = this.getNextAppTypeKey();
        id = new ManagedObjectId (ManagedObjectIdType.ApplicationType, newId);


        sqlStatement.append ("INSERT INTO application_type " +
                             "(id, name, version)" +
                             " VALUES (" +
                             newId + ", " +
                             "'" + name + "', " +
                             "'" + version + "');");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return id;
    }

    /**
     * @param applicationType The application object to update in the database.
     * @roseuid 3F424FE303C5
     */
    public void updateApplicationType(AppType applicationType) throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        String version;

        if (applicationType == null) {
            return;
        }

        name = applicationType.name();
        version = applicationType.version();

        sqlStatement = new StringBuffer();

        sqlStatement.append ("UPDATE application_type SET " +
                             "name='" + name + "', " +
                             "version='" + version + "' " +
                             "WHERE " +
                             "id=" + applicationType.id().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public Collection retrieveApplicationTypes() throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection objects;
        long key;
        ManagedObjectId id;
        String name;
        String version;
        AppTypeData data;

        objects = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name, version FROM application_type;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong("id");
                id = new ManagedObjectId (ManagedObjectIdType.ApplicationType, key);
                name = rs.getString("name");
                version = rs.getString("version");

                try {
                    data = new AppTypeData(id, name, version);
                    objects.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return objects;
    }

    public AppTypeData retrieveApplicationType(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        StringBuffer sqlStatement;

        long key;
        String name;
        String version;
        AppTypeData data;

        data = null;
        connection = null;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.ApplicationType) {
            return null;
        }

        sqlStatement = new StringBuffer ("SELECT id, name, version FROM application_type WHERE id=");
        sqlStatement.append (id.getManagedObjectKey() + ";");

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement.toString());
            while (rs.next()) {
                name = rs.getString("name");
                version = rs.getString("version");

                try {
                    data = new AppTypeData(id, name, version);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return data;
    }

    /**
     * @param applicationTypeKey The key to the application type object to remove from
     * the database.
     * @roseuid 3F424FF601EC
     */
    public void removeApplicationType(AppType appType) throws EmanagerDatabaseException
    {
        Collection ids;

        if (appType == null)
            return;

        ids = new LinkedList();
        ids.add (appType.id());
        removeObjects (ids);
    }

    /**
     * @param applicationTypeKeys The collection of applicationType object keys to
     * remove from the database.
     * @roseuid 3F425002001D
     */
    public void removeApplicationTypes(Collection appTypes) throws EmanagerDatabaseException
    {
        Iterator iter;
        Collection ids;

        if (appTypes == null ||
            appTypes.isEmpty()) {
            return;
        }

        ids = new LinkedList();
        iter = appTypes.iterator();
        while (iter.hasNext()) {
            ids.add ( ((AppType)iter.next()).id());
        }

        removeObjects (ids);
    }

    /**
     * @param managementPolicy
     * @return com.cisco.eManager.eManager.inventory.mgmtPolicy.MgmtPolicy
     * @roseuid 3F42511A0202
     */
    public ManagedObjectId createManagementPolicy(String name,
                                                  String path,
                                                  ManagedObjectId appTypeId,
                                                  ManagedObjectId hostId)
        throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        id = null;
        sqlStatement = new StringBuffer();

        newId = this.getNextMgmtPolicyKey();
        id = new ManagedObjectId(ManagedObjectIdType.MgmtPolicy, newId);

        sqlStatement.append ("INSERT INTO management_policy " +
                             "(id, name, path, application_type_id, host_id)" +
                             " VALUES (" +
                             newId + ", " +
                             "'" + name + "', " +
                             "'" + path + "', " +
                             appTypeId.getManagedObjectKey() + ", " +
                             hostId.getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return id;
    }

    /**
     * @param managementPolicy
     * @roseuid 3F42512802A3
     */
    public void updateManagementPolicy(MgmtPolicy mgmtPolicy) throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        String path;
        ManagedObjectId appTypeId;
        ManagedObjectId hostId;

        sqlStatement = new StringBuffer();

        if (mgmtPolicy == null) {
            return;
        }

        name = mgmtPolicy.name();
        path = mgmtPolicy.pathname();
        appTypeId = mgmtPolicy.appTypeId();
        hostId = mgmtPolicy.hostId();


        sqlStatement.append ("UPDATE management_policy SET " +
                             "name='" + name + "', " +
                             "path='" + path + "', " +
                             "application_type_id=" + appTypeId.getManagedObjectKey() + ", " +
                             "host_id=" + hostId.getManagedObjectKey() +
                             " WHERE " +
                             "id=" + mgmtPolicy.id().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public Collection retrieveManagementPolicies() throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection objects;
        long key;
        ManagedObjectId id;
        String name;
        String path;
        long appTypeKey;
        ManagedObjectId appTypeId;
        long hostKey;
        ManagedObjectId hostId;
        MgmtPolicyData data;

        objects = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name, path, application_type_id, host_id FROM management_policy;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong("id");
                id = new ManagedObjectId (ManagedObjectIdType.MgmtPolicy, key);
                name = rs.getString("name");
                path = rs.getString("path");
                appTypeKey = rs.getLong("application_type_id");
                appTypeId = new ManagedObjectId (ManagedObjectIdType.ApplicationType, appTypeKey);
                hostKey = rs.getLong("host_id");
                hostId = new ManagedObjectId (ManagedObjectIdType.HostAgent, hostKey);

                try {
                    data = new MgmtPolicyData(id, name, path, appTypeId, hostId);
                    objects.add(data);
                }
                catch (Exception e) {
                }

            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return objects;
    }

    public MgmtPolicyData retrieveManagementPolicy(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        StringBuffer sqlStatement;

        long key;
        String name;
        String path;
        long appTypeKey;
        ManagedObjectId appTypeId;
        long hostKey;
        ManagedObjectId hostId;
        MgmtPolicyData data;

        data = null;
        connection = null;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.MgmtPolicy) {
            return null;
        }

        sqlStatement = new StringBuffer ("SELECT name, path, application_type_id, host_id FROM management_policy WHERE id=");
        sqlStatement.append (id.getManagedObjectKey() + ";");

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement.toString());
            while (rs.next()) {
                name = rs.getString("name");
                path = rs.getString("path");
                appTypeKey = rs.getLong("application_type_id");
                appTypeId = new ManagedObjectId (ManagedObjectIdType.ApplicationType, appTypeKey);
                hostKey = rs.getLong("host_id");
                hostId = new ManagedObjectId (ManagedObjectIdType.HostAgent, hostKey);

                try {
                    data = new MgmtPolicyData(id, name, path, appTypeId, hostId);
                }
                catch (Exception e) {
                    data = null;
                }

            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return data;
    }

    /**
     * @param managementPolicyKey
     * @roseuid 3F425131006B
     */
    public void removeManagementPolicy(MgmtPolicy mgmtPolicy) throws EmanagerDatabaseException
    {
        Collection ids;

        if (mgmtPolicy == null) {
            return;
        }

        ids = new LinkedList();
        ids.add(mgmtPolicy.id());
        removeObjects (ids);
    }

    /**
     * @param managementPolicyKeys
     * @roseuid 3F42513A0277
     */
    public void removeManagementPolicies(Collection mgmtPolicies) throws EmanagerDatabaseException
    {
        Iterator iter;
        Collection ids;

        if (mgmtPolicies == null ||
            mgmtPolicies.isEmpty()) {
            return;
        }

        ids = new LinkedList();
        iter = mgmtPolicies.iterator();
        while (iter.hasNext()) {
            ids.add ( ((Host)iter.next()).id());
        }

        removeObjects (ids);
    }


    /**
     * @param appInstanceData The container object which holds the appInstance
     * attributes to be persisted (note that the id, assigned by this method, is
     * just a place-holder in this copy).
     * @return The container object which holds the appInstance attributes which
     * have been persisted - this differs from the appInstanceData argument in
     * that it has a valid id member.
     * @roseuid 3F4254660227
     */
    public AppInstanceData createApplicationInstance(AppInstanceData appInstanceData)
        throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        int managed;
        String logfileDirs;
        Transport logfileTransport;
        String logfileUserId;
        String logfilePassword;
        String unixPrompt;
	String propertyFile;
        ManagedObjectId appTypeId;
        ManagedObjectId hostId;

        id = null;
        sqlStatement = new StringBuffer();

        if (appInstanceData == null) {
            return null;
        }

        name = appInstanceData.name();
        if (appInstanceData.mgmtMode().isManaged()) {
            managed = 1;
        } else {
            managed = 0;
        }

        logfileDirs = appInstanceData.logfileDirectories();
        logfileTransport = appInstanceData.logfileTransport();
        logfileUserId = appInstanceData.transportUsername();
        logfilePassword = appInstanceData.transportPassword();
        unixPrompt = appInstanceData.transportUnixPrompt();
	propertyFile = appInstanceData.propertyFile();
        appTypeId = appInstanceData.appTypeId();
        hostId = appInstanceData.hostId();

        newId = this.getNextAppInstanceKey();
        id = new ManagedObjectId (ManagedObjectIdType.ApplicationInstance, newId);

        sqlStatement.append ("INSERT INTO application_instance " +
                             "(id, name, managed, logfile_directories, " +
                             "logfile_transport_method, logfile_transport_username, " +
                             "logfile_transport_password, " +
                             "unix_prompt, property_file, application_type_id, host_id)" +
                             " VALUES (" +
                             newId + ", " +
                             "'" + name + "', " +
                             managed + ", " +
                             "'" + logfileDirs + "', " +
                             logfileTransport.intValue() + ", " +
                             "'" + logfileUserId + "', " +
                             "'" + logfilePassword + "', " +
                             "'" + unixPrompt + "', " +
			     "'" + propertyFile + "', " +
                             appTypeId.getManagedObjectKey() + "," +
                             hostId.getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        AppInstanceData newAppInstanceData = null;
        try
        {
            newAppInstanceData =
                new AppInstanceData(id,
                                    appInstanceData.hostId(),
                                    appInstanceData.appTypeId(),
                                    appInstanceData.name(),
                                    appInstanceData.mgmtMode(),
                                    appInstanceData.logfileDirectories(),
                                    appInstanceData.logfileTransport(),
                                    appInstanceData.transportUsername(),
                                    appInstanceData.transportPassword(),
                                    appInstanceData.transportUnixPrompt(),
                                    appInstanceData.propertyFile());
        }
        catch (Exception ex)
        {
            logger.error("exception caught while creating an updated " +
                         "appInstanceData - post creation in db: " + ex);
            return null;
        }
        return newAppInstanceData;
    }

    /**
     * @param applicationInstance The ApplicationInstance object to update in the
     * database.
     * @roseuid 3F4254720239
     */
    public void updateApplicationInstance(AppInstance appInstance) throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        int managed;
        String logfileDirs;
        Transport logfileTransport;
        String logfileUserId;
        String logfilePassword;
        String unixPrompt;
	String propertyFile;
        ManagedObjectId appTypeId;
        ManagedObjectId hostId;

        sqlStatement = new StringBuffer();

        if (appInstance == null) {
            return;
        }

        name = appInstance.name();
        if (appInstance.managed() == true) {
            managed = 1;
        } else {
            managed = 0;
        }
        logfileDirs = appInstance.logfileDirectories();
        logfileTransport = appInstance.logfileTransport();
        logfileUserId = appInstance.logfileUsername();
        logfilePassword = appInstance.logfilePassword();
        unixPrompt = appInstance.unixPrompt();
        propertyFile = appInstance.propertyFile();
        appTypeId = appInstance.appTypeId();
        hostId = appInstance.hostId();

        sqlStatement.append ("UPDATE application_instance SET " +
                             "name='" + name + "', " +
                             "managed=" + managed + ", " +
                             "logfile_directories='" + logfileDirs + "', " +
                             "logfile_transport_method=" + logfileTransport.intValue() + ", " +
                             "logfile_transport_username='" + logfileUserId + "', " +
                             "logfile_transport_password='" + logfilePassword + "', " +
                             "unix_prompt='" + unixPrompt + "', " +
			     "property_file='" + propertyFile + "', " +
                             "application_type_id=" + appTypeId.getManagedObjectKey() + ", " +
                             "host_id=" + hostId.getManagedObjectKey() +
                             " WHERE " +
                             "id=" + appInstance.id().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public Collection retrieveApplicationInstances() throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection objects;
        long key;
        ManagedObjectId id;
        String name;
        int managedInt;
        AppInstanceMgmtMode managed;
        String logfileDirs;
        int logfileTransportInt;
        Transport logfileTransport;
        String logfileUserId;
        String logfilePassword;
        String unixPrompt;
	String propertyFile;
        long appTypeKey;
        long hostKey;
        AppInstanceData data;

        objects = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name, managed, logfile_directories, " +
                       "logfile_transport_method, logfile_transport_username, " +
                       "logfile_transport_password, unix_prompt, " +
                       "property_file, application_type_id, host_id " +
                       "FROM application_instance;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement);

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong("id");
                id = new ManagedObjectId (ManagedObjectIdType.ApplicationInstance, key);
                name = rs.getString("name");
                managedInt = rs.getInt("managed");
                if (managedInt == 1 &&
                    AppInstanceMgmtMode.MANAGED.isManaged() == true) {
                    managed = AppInstanceMgmtMode.MANAGED;
                }else {
                    managed = AppInstanceMgmtMode.UNMANAGED;
                }

                logfileDirs = rs.getString("logfile_directories");
                logfileTransportInt = rs.getInt("logfile_transport_method");
                if (logfileTransportInt == Transport.SSH.intValue()) {
                    logfileTransport = Transport.SSH;
                } else if (logfileTransportInt == Transport.TELNET.intValue()) {
                    logfileTransport = Transport.TELNET;
                } else {
                    logfileTransport = Transport.TELNET;
                    logger.error("Invalid transport value read from database for App Instance Object " +
                                 id + ".  Using default Telnet for transport.");
                }

                logfileUserId = rs.getString ("logfile_transport_username");
                logfilePassword = rs.getString("logfile_transport_password");
                unixPrompt = rs.getString ("unix_prompt");
                propertyFile = rs.getString ("property_file");
                appTypeKey = rs.getLong ("application_type_id");
                hostKey = rs.getLong("host_id");

                try {
                    data =
                        new AppInstanceData(id,
                                            new ManagedObjectId (ManagedObjectIdType.HostAgent, hostKey),
                                            new ManagedObjectId (ManagedObjectIdType.ApplicationType, appTypeKey),
                                            name,
                                            managed,
                                            logfileDirs,
                                            logfileTransport,
                                            logfileUserId,
                                            logfilePassword,
                                            unixPrompt,
                                            propertyFile);
                    objects.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return objects;
    }

    public AppInstanceData retrieveApplicationInstance(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        StringBuffer sqlStatement;

        String name;
        int managedInt;
        AppInstanceMgmtMode managed;
        String logfileDirs;
        int logfileTransportInt;
        Transport logfileTransport;
        String logfileUserId;
        String logfilePassword;
        String unixPrompt;
        String propertyFile;
        long appTypeKey;
        long hostKey;
        AppInstanceData data;

        data = null;
        connection = null;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.ApplicationInstance) {
            return null;
        }

        sqlStatement = new StringBuffer ("SELECT id, name, managed, logfile_directories, " +
                                         "logfile_transport_method, logfile_transport_username, " +
                                         "logfile_transport_password, unix_prompt, " +
                                         "property_file, application_type_id, host_id " +
                                         "FROM application_instance WHERE id=");
        sqlStatement.append (id.getManagedObjectKey() + ";");

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement.toString());
            while (rs.next()) {
                name = rs.getString("name");
                managedInt = rs.getInt("managed");
                if (managedInt == 1 &&
                    AppInstanceMgmtMode.MANAGED.isManaged() == true) {
                    managed = AppInstanceMgmtMode.MANAGED;
                }else {
                    managed = AppInstanceMgmtMode.UNMANAGED;
                }

                logfileDirs = rs.getString("logfile_directories");
                logfileTransportInt = rs.getInt("logfile_transport_method");
                if (logfileTransportInt == Transport.SSH.intValue()) {
                    logfileTransport = Transport.SSH;
                } else if (logfileTransportInt == Transport.TELNET.intValue()) {
                    logfileTransport = Transport.TELNET;
                } else {
                    logfileTransport = Transport.TELNET;
                    logger.error("Invalid transport value read from database for App Instance Object " +
                                 id + ".  Using default Telnet for transport.");
                }

                logfileUserId = rs.getString ("logfile_transport_username");
                logfilePassword = rs.getString("logfile_transport_password");
                unixPrompt = rs.getString ("unix_prompt");
                propertyFile = rs.getString ("property_file");
                appTypeKey = rs.getLong ("application_type_id");
                hostKey = rs.getLong("host_id");

                try {
                    data =
                        new AppInstanceData(id,
                                            new ManagedObjectId (ManagedObjectIdType.HostAgent, hostKey),
                                            new ManagedObjectId (ManagedObjectIdType.ApplicationType, appTypeKey),
                                            name,
                                            managed,
                                            logfileDirs,
                                            logfileTransport,
                                            logfileUserId,
                                            logfilePassword,
                                            unixPrompt,
                                            propertyFile);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return data;
    }

    /**
     * @param applicationInstanceKey The key of the ApplicationInstance object to
     * delete from the database.
     * @roseuid 3F42547B0138
     */
    public void removeApplicationInstance(AppInstance appInstance) throws EmanagerDatabaseException
    {
        Collection ids;

        if (appInstance == null) {
            return;
        }

        ids = new LinkedList();
        ids.add(appInstance.id());
        removeObjects (ids);
    }

    /**
     * @param applicationInstanceKeys The key of the ApplicationInstance objects to
     * remove from the database.
     * @roseuid 3F425486017A
     */
    public void removeApplicationInstances(Collection appInstances) throws EmanagerDatabaseException
    {
        Iterator iter;
        Collection ids;

        if (appInstances == null ||
            appInstances.isEmpty()) {
            return;
        }

        ids = new LinkedList();
        iter = appInstances.iterator();
        while (iter.hasNext()) {
            ids.add ( ((Host)iter.next()).id());
        }

        removeObjects (ids);
    }

    /**
     * @param containerNode The ContainerNode object to create in the database.
     * @return The ContainerNode object that contains the database key.
     * @roseuid 3F4255F400BD
     */
    public ManagedObjectId createPhysicalHierarchyNode(ContainerNodeData nodeData) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        ManagedObjectId parentId;

        id = null;
        sqlStatement = new StringBuffer();

        if (nodeData == null) {
            return id;
        }

        name = nodeData.name();
        parentId = nodeData.parentId();

        logger.debug("retrieving ID value from db");
        newId = this.getNextPhysicalHierarchyKey();
        logger.debug("ID after retrieving value from db: " + newId);
        id = new ManagedObjectId (ManagedObjectIdType.PhysicalHierarchyContainer, newId);

        sqlStatement.append ("INSERT INTO physical_hierarchy " +
                             "(id, name, physical_hierarchy_id)" +
                             " VALUES (" +
                             newId + ", " +
                             "'" + name + "', " +
                             parentId.getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return id;
    }

    /**
     * @param containerNode The ContainerNode object to update in the database.
     * @roseuid 3F4256F103CE
     */
    public void updatePhysicalHierarchyNode(ContainerNodeData nodeData) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        ManagedObjectId parentId;

        if (nodeData == null) {
            return;
        }

        id = null;
        sqlStatement = new StringBuffer();

        name = nodeData.name();
        parentId = nodeData.parentId();

        sqlStatement.append ("UPDATE physical_hierarchy SET " +
                             "name='" + name + "', " +
                             "physical_hierarchy_id=" + parentId.getManagedObjectKey() +
                             " WHERE " +
                             "id=" + nodeData.id().getManagedObjectKey() + ";" );

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public Collection retrievePhysicalHierarchyNodes() throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long nodeKey;
        ManagedObjectId id;
        String name;
        long parentKey;
        ManagedObjectId parentId;
        ContainerNodeData nodeData;

        nodes = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name, physical_hierarchy_id FROM physical_hierarchy;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                nodeKey = rs.getLong("id");
                id = new ManagedObjectId (ManagedObjectIdType.PhysicalHierarchyContainer, nodeKey);
                name = rs.getString("name");
                parentKey = rs.getLong("physical_hierarchy_id");
                parentId = new ManagedObjectId (ManagedObjectIdType.PhysicalHierarchyContainer, parentKey);

                nodeData = new ContainerNodeData (id, name, parentId);
                nodes.add (nodeData);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public ContainerNodeData retrievePhysicalHierarchyNode(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        StringBuffer sqlStatement;

        String name;
        long parentKey;
        ManagedObjectId parentId;
        ContainerNodeData nodeData;

        nodeData = null;
        connection = null;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.PhysicalHierarchyContainer) {
            return null;
        }

        sqlStatement = new StringBuffer ("SELECT name, physical_hierarchy_id FROM physical_hierarchy WHERE id=");
        sqlStatement.append (id.getManagedObjectKey() + ";");

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement.toString());
            while (rs.next()) {
                name = rs.getString("name");
                parentKey = rs.getLong("physical_hierarchy_id");
                parentId = new ManagedObjectId (ManagedObjectIdType.PhysicalHierarchyContainer, parentKey);
                nodeData = new ContainerNodeData (id, name, parentId);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodeData;
    }

    /**
     * @param containerNodeKey The ContainerNode key to remove from the database.
     * @roseuid 3F42570A00F0
     */
    public void removePhysicalHierarchyNode(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Collection ids;

        if (id == null  ||
	    id.getManagedObjectType() != ManagedObjectIdType.PhysicalHierarchyContainer) {
            return;
        }

        ids = new LinkedList();
        ids.add(id);
        removeObjects (ids);
    }

    public void removePhysicalHierarchyNodes (Collection ids) throws EmanagerDatabaseException
    {
	Iterator iter;
	ManagedObjectId id;

        if (ids == null ||
            ids.isEmpty()) {
            return;
        }

	iter = ids.iterator();
	id = (ManagedObjectId) iter.next();
	if (id.getManagedObjectType() != ManagedObjectIdType.PhysicalHierarchyContainer) {
	    return;
	}

        removeObjects (ids);
    }

    public void createPhysicalHierarchyRelationshipNode(PhysicalHierarchyRelationshipData data)
	throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        sqlStatement = new StringBuffer();

        if (data == null) {
            return;
        }

        sqlStatement.append ("INSERT INTO physical_relationship " +
                             "(host_id, physical_hierarchy_id)" +
                             " VALUES (" +
                             data.hostId().getManagedObjectKey() + ", " +
                             data.physicalHierarchyId().getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

	return;
    }

    public void removePhysicalHierarchyRelationshipNode(PhysicalHierarchyRelationshipData data)
	throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        sqlStatement = new StringBuffer();

        if (data == null) {
            return;
        }

        sqlStatement.append ("DELETE FROM physical_relationship " +
                             "WHERE " +
			     "host_id=" + data.hostId().getManagedObjectKey() +
			     " AND " +
			     "physical_hierarchy_id=" + data.physicalHierarchyId().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

	return;
    }

    public Collection retrievePhysicalHierarchyRelationshipNodes()
	throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long hostKey;
        ManagedObjectId hostId;
        long containerKey;
        ManagedObjectId containerId;
	PhysicalHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

        sqlStatement = "SELECT host_id, physical_hierarchy_id FROM physical_relationship;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                hostKey = rs.getLong("host_id");
                hostId = new ManagedObjectId (ManagedObjectIdType.HostAgent, hostKey);
                containerKey = rs.getLong("physical_hierarchy_id");
                containerId = new ManagedObjectId (ManagedObjectIdType.PhysicalHierarchyContainer, containerKey);

                try {
                    data = new PhysicalHierarchyRelationshipData(hostId, containerId);
                    nodes.add (data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public Collection retrievePhysicalHierarchyRelationshipPhysContainerNodes(ManagedObjectId hostId)
        throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long containerKey;
        ManagedObjectId containerId;
        PhysicalHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

	if (hostId == null ||
	    hostId.getManagedObjectType() != ManagedObjectIdType.HostAgent) {
	    return nodes;
	}

        sqlStatement =
            "SELECT physical_hierarchy_id FROM physical_relationship WHERE host_id=" +
            hostId.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                containerKey = rs.getLong("physical_hierarchy_id");
                containerId = new ManagedObjectId (ManagedObjectIdType.PhysicalHierarchyContainer, containerKey);

                try {
                    data = new PhysicalHierarchyRelationshipData(hostId, containerId);
                    nodes.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public Collection retrievePhysicalHierarchyRelationshipHostNodes(ManagedObjectId containerId)
	throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long hostKey;
        ManagedObjectId hostId;
	PhysicalHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

	if (containerId == null ||
	    containerId.getManagedObjectType() != ManagedObjectIdType.PhysicalHierarchyContainer) {
	    return nodes;
	}

        sqlStatement =
	    "SELECT host_id FROM physical_relationship WHERE physical_hierarchy_id=" +
	    containerId.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                hostKey = rs.getLong("host_id");
                hostId = new ManagedObjectId (ManagedObjectIdType.HostAgent, hostKey);

                try {
                    data = new PhysicalHierarchyRelationshipData(hostId, containerId);
                    nodes.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    /**
     * @param containerNode The ContainerNode object to store in the database.
     * @return The updated ContainerNode object that contains the database key.
     * @roseuid 3F4257D1023E
     */
    public ManagedObjectId createApplicationHierarchyNode(ContainerNodeData nodeData) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        ManagedObjectId parentId;

        id = null;
        sqlStatement = new StringBuffer();

        if (nodeData == null) {
            return id;
        }

        name = nodeData.name();
        parentId = nodeData.parentId();

        newId = this.getNextApplicationHierarchyKey();
        id = new ManagedObjectId (ManagedObjectIdType.ApplicationHierarchyContainer, newId);

        sqlStatement.append ("INSERT INTO application_hierarchy " +
                             "(id, name, application_hierarchy_id)" +
                             " VALUES (" +
                             newId + ", " +
                             "'" + name + "', " +
                             parentId.getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return id;
    }

    /**
     * @param containerNode The ContainerNode object to update in the database.
     * @roseuid 3F4257DD0354
     */
    public void updateApplicationHierarchyNode(ContainerNodeData nodeData) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        ManagedObjectId parentId;

        if (nodeData == null) {
            return;
        }

        id = null;
        sqlStatement = new StringBuffer();

        name = nodeData.name();
        parentId = nodeData.parentId();

        sqlStatement.append ("UPDATE application_hierarchy SET " +
                             "name='" + name + "', " +
                             "application_hierarchy_id=" + parentId.getManagedObjectKey() +
                             " WHERE " +
                             "id=" + nodeData.id().getManagedObjectKey() + ";" );

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public Collection retrieveApplicationHierarchyNodes() throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long nodeKey;
        ManagedObjectId id;
        String name;
        long parentKey;
        ManagedObjectId parentId;
        ContainerNodeData nodeData;

        nodes = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name, application_hierarchy_id FROM application_hierarchy;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                nodeKey = rs.getLong("id");
                id = new ManagedObjectId (ManagedObjectIdType.ApplicationHierarchyContainer, nodeKey);
                name = rs.getString("name");
                parentKey = rs.getLong("application_hierarchy_id");
                parentId = new ManagedObjectId (ManagedObjectIdType.ApplicationHierarchyContainer, parentKey);

                nodeData = new ContainerNodeData (id, name, parentId);
                nodes.add (nodeData);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public ContainerNodeData retrieveApplicationHierarchyNode(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        StringBuffer sqlStatement;

        String name;
        long parentKey;
        ManagedObjectId parentId;
        ContainerNodeData nodeData;

        nodeData = null;
        connection = null;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.ApplicationHierarchyContainer) {
            return null;
        }

        sqlStatement = new StringBuffer ("SELECT name, application_hierarchy_id FROM application_hierarchy WHERE id=");
        sqlStatement.append (id.getManagedObjectKey() + ";");

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement.toString());
            while (rs.next()) {
                name = rs.getString("name");
                parentKey = rs.getLong("application_hierarchy_id");
                parentId = new ManagedObjectId (ManagedObjectIdType.ApplicationHierarchyContainer, parentKey);
                nodeData = new ContainerNodeData (id, name, parentId);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodeData;
    }

    /**
     * @param containerNodeKey The ContainerNode object key to delete from the
     * database.
     * @roseuid 3F4257E90316
     */
    public void removeApplicationHierarchyNode(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Collection ids;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.ApplicationHierarchyContainer) {
            return;
        }

        ids = new LinkedList();
        ids.add(id);
        removeObjects (ids);
    }

    public void removeApplicationHierarchyNodes (Collection ids) throws EmanagerDatabaseException
    {
	Iterator iter;
	ManagedObjectId id;

        if (ids == null ||
            ids.isEmpty()) {
            return;
        }

	iter = ids.iterator();
	id = (ManagedObjectId) iter.next();
	if (id.getManagedObjectType() != ManagedObjectIdType.ApplicationHierarchyContainer) {
	    return;
	}

        removeObjects (ids);
    }

    public void createApplicationHierarchyRelationshipNode(ApplicationHierarchyRelationshipData data)
	throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        sqlStatement = new StringBuffer();

        if (data == null) {
            return;
        }

        sqlStatement.append ("INSERT INTO application_relationship " +
                             "(application_type_id, application_hierarchy_id)" +
                             " VALUES (" +
                             data.appTypeId().getManagedObjectKey() + ", " +
                             data.appHierarchyId().getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

	return;
    }

    public void removeApplicationHierarchyRelationshipNode(ApplicationHierarchyRelationshipData data)
	throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        sqlStatement = new StringBuffer();

        if (data == null) {
            return;
        }

        sqlStatement.append ("DELETE FROM application_relationship " +
                             "WHERE " +
			     "application_type_id=" + data.appTypeId().getManagedObjectKey() +
			     " AND " +
			     "application_hierarchy_id=" + data.appHierarchyId().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

	return;
    }

    public Collection retrieveApplicationHierarchyRelationshipNodes()
	throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long key;
        ManagedObjectId id;
        long containerKey;
        ManagedObjectId containerId;
	ApplicationHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

        sqlStatement = "SELECT application_type_id, application_hierarchy_id FROM application_relationship;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong("application_type_id");
                id = new ManagedObjectId (ManagedObjectIdType.ApplicationType, key);
                containerKey = rs.getLong("application_hierarchy_id");
                containerId = new ManagedObjectId (ManagedObjectIdType.ApplicationHierarchyContainer, containerKey);

                try {
                    data = new ApplicationHierarchyRelationshipData(id, containerId);
                    nodes.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public Collection retrieveApplicationHierarchyRelationshipAppContainerNodes(ManagedObjectId appTypeId)
	throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long containerKey;
        ManagedObjectId containerId;
	ApplicationHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

	if (appTypeId == null ||
	    appTypeId.getManagedObjectType() != ManagedObjectIdType.ApplicationType) {
	    return nodes;
	}

        sqlStatement =
	    "SELECT application_hierarchy_id FROM application_relationship WHERE application_type_id=" +
	    appTypeId.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                containerKey = rs.getLong("application_hierarchy_id");
                containerId = new ManagedObjectId (ManagedObjectIdType.ApplicationHierarchyContainer, containerKey);

                try {
                    data = new ApplicationHierarchyRelationshipData(appTypeId, containerId);
                    nodes.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public Collection retrieveApplicationHierarchyRelationshipAppTypeNodes(ManagedObjectId containerId)
	throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long key;
        ManagedObjectId id;
	ApplicationHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

	if (containerId == null ||
	    containerId.getManagedObjectType() != ManagedObjectIdType.ApplicationHierarchyContainer) {
	    return nodes;
	}

        sqlStatement =
	    "SELECT application_type_id FROM application_relationship WHERE application_hierarchy_id=" +
            containerId.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong("application_type_id");
                id = new ManagedObjectId (ManagedObjectIdType.ApplicationType, key);

                try {
                    data = new ApplicationHierarchyRelationshipData(id, containerId);
                    nodes.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

   public AuditLogEntry createAuditLogEntry (AuditDomain domain,
					     AuditAction action,
					     String subject,
					     Date time,
					     String userId,
					     String description) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;
        AuditLogEntry auditLogEntry;

        sqlStatement = new StringBuffer();
        newId = getNextAuditLogKey();

	auditLogEntry = new AuditLogEntry (newId,
					   domain,
					   action,
					   subject,
					   time,
					   userId,
					   description);

        id = auditLogEntry.getId();

	sqlStatement.append ("INSERT INTO audit_log " +
			     "(id, domain, action, subject, log_time, user_id, description)" +
			     " VALUES (" +
			     Long.toString (newId) + ", " +
			     Integer.toString (domain.intValue()) + ", " +
			     Integer.toString (action.intValue()) + ", " +
			     "'" + subject + "', " +
			     "'" + convertDateToSQLDate (time) + "', " +
			     "'" + userId + "', " +
			     "'" + description + "');" );

        connection = DatabaseConnectionManager.instance().getConnection();

        logger.debug("Database SQL statement issued: " + sqlStatement.toString());

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();
            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return auditLogEntry;
    }

    public void updateAuditLogEntry (AuditLogEntry entry) throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        if (entry == null) {
            return;
        }

        sqlStatement = new StringBuffer();

        sqlStatement.append ("UPDATE audit_log SET ");

        sqlStatement.append ("domain=" + entry.getDomain().intValue() + ", ");
        sqlStatement.append ("action=" + entry.getAction().intValue() + ", ");
	sqlStatement.append ("subject='" + entry.getSubject() + "', ");
        sqlStatement.append ("log_time='" + convertDateToSQLDate(entry.getTime()) + "', ");
	sqlStatement.append ("user_id='" + entry.getUserId() + "', ");
	sqlStatement.append ("description='" + entry.getDescription() + "' ");
        sqlStatement.append (" WHERE id=" + entry.getId().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement = connection.createStatement();
            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    private String getAuditLogIdSearchStatement (com.cisco.eManager.common.audit.NumericSearchCriteria numericSearchCriteria)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (numericSearchCriteria != null) {
	    if (numericSearchCriteria.getStart().longValue() ==
		numericSearchCriteria.getEnd().longValue()) {
		sqlClause.append (" id=");
		sqlClause.append (numericSearchCriteria.getStart().longValue());
		sqlClause.append (" ");
	    } else {
		sqlClause.append (" id>=");
		sqlClause.append (numericSearchCriteria.getStart().longValue());
		sqlClause.append (" AND id<=");
		sqlClause.append (numericSearchCriteria.getEnd().longValue());
		sqlClause.append (" ");
	    }
	}

	return sqlClause.toString();
    }

    private String getAuditDomainSearchStatement (Collection domains)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (domains != null &&
            domains.size() != 0) {
	    boolean first;
	    Iterator iter;
	    AuditDomain domain;

	    sqlClause.append ("(");
	    iter = domains.iterator();
	    first = true;
	    while (iter.hasNext()) {
		if (first == false) {
		    sqlClause.append (" OR ");
		} else {
		    first = false;
		}

		domain = (AuditDomain) iter.next();

		sqlClause.append (" domain=");
		sqlClause.append (domain.intValue());
	    }

	    sqlClause.append (")");
	}

	return sqlClause.toString();
    }

    private String getAuditActionSearchStatement (Collection actions)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (actions != null &&
            actions.size() != 0) {
	    boolean first;
	    Iterator iter;
	    AuditAction action;

	    sqlClause.append ("(");
	    iter = actions.iterator();
	    first = true;
	    while (iter.hasNext()) {
		if (first == false) {
		    sqlClause.append (" OR ");
		} else {
		    first = false;
		}

		action = (AuditAction) iter.next();

		sqlClause.append (" action=");
		sqlClause.append (action.intValue());
	    }

	    sqlClause.append (")");
	}

	return sqlClause.toString();
    }

    private String getAuditSubjectSearchStatement (com.cisco.eManager.common.audit.StringSearchCriteria criteria)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (criteria != null) {
	    if (criteria.getString() == null) {
		sqlClause.append (" subject=null ");
	    } else {
		sqlClause.append (" subject like '%" +
				  criteria.getString() +
				  "%' ");
	    }
	}

	return sqlClause.toString();
    }

    public String getAuditTimeSearchStatement (com.cisco.eManager.common.audit.DateSearchCriteria criteria)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (criteria != null) {
            if (criteria.getStartDate() != null &&
                criteria.getEndDate() != null){
                sqlClause.append(" log_time BETWEEN '");
                sqlClause.append(convertDateToSQLDate(criteria.getStartDate()));
                sqlClause.append("' AND '");
                sqlClause.append(convertDateToSQLDate(criteria.getEndDate()));
                sqlClause.append("' ");
            }
            else{
                sqlClause.append("log_time=null ");
            }
        }

	return sqlClause.toString();
    }

    private String getAuditUserIdSearchStatement (com.cisco.eManager.common.audit.StringSearchCriteria criteria)
    {
	StringBuffer sqlClause;

	sqlClause = new StringBuffer();

	if (criteria != null) {
	    if (criteria.getString() == null) {
		sqlClause.append (" user_id=null ");
	    } else {
		sqlClause.append (" user_id like '%" +
				  criteria.getString() +
				  "%' ");
	    }
	}

	return sqlClause.toString();
    }

    public Collection retrieveAuditLogEntries (AuditLogSearchCriteria criteria) throws EmanagerDatabaseException
    {
	String	idSearchStatement;
	String	domainSearchStatement;
	String	actionSearchStatement;
	String	subjectSearchStatement;
	String	timeSearchStatement;
	String	userIdSearchStatement;
	String	SearchStatement;
	boolean criteriaFound;
	StringBuffer sqlStatement;
	Connection connection;
	Statement  statement;

	ResultSet rs;
	Collection entries;
        long key;
	int domainValue;
	AuditDomain domain;
	int actionValue;
	AuditAction action;
	String subject;
	Timestamp time;
	String userId;
	String description;
	AuditLogEntry auditLogEntry;

	rs = null;
	entries = new LinkedList();

	if (criteria == null) {
	    return entries;
	}

	idSearchStatement = getAuditLogIdSearchStatement (criteria.getAuditLogId());
	domainSearchStatement = getAuditDomainSearchStatement (criteria.getDomain());
	actionSearchStatement = getAuditActionSearchStatement (criteria.getAction());
	subjectSearchStatement = getAuditSubjectSearchStatement (criteria.getSubject());
	timeSearchStatement = getAuditTimeSearchStatement (criteria.getTime());
	userIdSearchStatement = getAuditUserIdSearchStatement (criteria.getUserId());

	if (idSearchStatement      == null &&
	    domainSearchStatement  == null &&
	    actionSearchStatement  == null &&
	    subjectSearchStatement == null &&
	    timeSearchStatement    == null &&
	    userIdSearchStatement   == null) {
	    // no criteria
	    return entries;
	}

	connection = DatabaseConnectionManager.instance().getConnection();

	try {
	    statement = connection.createStatement();
	}
	catch (SQLException e) {
	    String logString;
	    EmanagerDatabaseException ede;

	    logString =
		EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
		e.getMessage();

	    logger.log(Priority.ERROR, logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
						 logString);

	    if (connection != null) {
		try {
		    connection.close();
		}
		catch (SQLException closeE) {
		}
	    }

	    throw ede;
	}

	try {
	    criteriaFound = false;
	    sqlStatement = new StringBuffer();
	    sqlStatement.append ("SELECT id, domain, action, subject, " +
				 "log_time, user_id, description " +
				 "FROM audit_log WHERE ");

	    if (idSearchStatement != null &&
		idSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (idSearchStatement);
	    }

	    if (domainSearchStatement != null &&
		domainSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (domainSearchStatement);
	    }

	    if (actionSearchStatement != null &&
		actionSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (actionSearchStatement);
	    }

	    if (subjectSearchStatement != null &&
		subjectSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (subjectSearchStatement);
	    }

	    if (timeSearchStatement != null &&
		timeSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (timeSearchStatement);
	    }

	    if (userIdSearchStatement != null &&
		userIdSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (userIdSearchStatement);
	    }

	    sqlStatement.append(";");

	    logger.debug("Database SQL statement issued: " + sqlStatement.toString());

	    rs = statement.executeQuery (sqlStatement.toString());
	    while (rs.next()) {
		key = rs.getLong("id");
		domainValue = rs.getInt("domain");
		actionValue = rs.getInt("action");
		subject = rs.getString("subject");
		time = rs.getTimestamp("log_time");
		userId = rs.getString("user_id");
		description = rs.getString("description");

		if (domainValue == AuditDomain.Inventory.intValue()) {
		    domain = AuditDomain.Inventory;
		} else if (domainValue == AuditDomain.Event.intValue()) {
		    domain = AuditDomain.Event;
		} else if (domainValue == AuditDomain.Process.intValue()) {
		    domain = AuditDomain.Process;
		} else {
		    domain = AuditDomain.Emanager;
		}

		if (actionValue == AuditAction.Create.intValue()) {
		    action = AuditAction.Create;
		} else if (actionValue == AuditAction.Read.intValue()) {
		    action = AuditAction.Read;
		} else if (actionValue == AuditAction.Update.intValue()) {
		    action = AuditAction.Update;
		} else {
		    action = AuditAction.Delete;
		}

		auditLogEntry = new AuditLogEntry (key,
						   domain,
						   action,
						   subject,
						   time,
						   userId,
						   description);
		entries.add(auditLogEntry);
	    }

	    connection.close();
	}
	catch (SQLException e) {
	    String logString;
	    EmanagerDatabaseException ede;

	    logString =
		EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
		e.getMessage();

	    logger.log(Priority.ERROR, logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
						 logString);

	    if (connection != null) {
		try {
		    connection.rollback();
		    connection.close();
		}
		catch (SQLException ee) {
		}
	    }

	    throw ede;
	}

	return entries;
    }

    public void removeAuditLogEntries (AuditLogDeletionCriteria criteria) throws EmanagerDatabaseException
    {
	String	idSearchStatement;
	String	domainSearchStatement;
	String	actionSearchStatement;
	String	subjectSearchStatement;
	String	timeSearchStatement;
	String	userIdSearchStatement;
	String	SearchStatement;
	boolean criteriaFound;
	StringBuffer sqlStatement;
	Connection connection;
	Statement  statement;

        long key;
	int domainValue;
	AuditDomain domain;
	int actionValue;
	AuditAction action;
	String subject;
	Timestamp time;
	String userId;
	String description;
	AuditLogEntry auditLogEntry;

	if (criteria == null) {
	    return;
	}

	idSearchStatement = getAuditLogIdSearchStatement (criteria.getAuditLogId());
	domainSearchStatement = getAuditDomainSearchStatement (criteria.getDomain());
	actionSearchStatement = getAuditActionSearchStatement (criteria.getAction());
	subjectSearchStatement = getAuditSubjectSearchStatement (criteria.getSubject());
	timeSearchStatement = getAuditTimeSearchStatement (criteria.getTime());
	userIdSearchStatement = getAuditUserIdSearchStatement (criteria.getUserId());

	if (idSearchStatement      == null &&
	    domainSearchStatement  == null &&
	    actionSearchStatement  == null &&
	    subjectSearchStatement == null &&
	    timeSearchStatement    == null &&
	    userIdSearchStatement   == null) {
	    // no criteria
	    return;
	}

	connection = DatabaseConnectionManager.instance().getConnection();

	try {
	    statement = connection.createStatement();
	}
	catch (SQLException e) {
	    String logString;
	    EmanagerDatabaseException ede;

	    logString =
		EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
		e.getMessage();

	    logger.log(Priority.ERROR, logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
						 logString);

	    if (connection != null) {
		try {
		    connection.close();
		}
		catch (SQLException closeE) {
		}
	    }

	    throw ede;
	}

	try {
	    criteriaFound = false;
	    sqlStatement = new StringBuffer();
	    sqlStatement.append ("DELETE FROM audit_log WHERE ");

	    if (idSearchStatement != null &&
		idSearchStatement.trim().length() != 0) {
		criteriaFound = true;
		sqlStatement.append (idSearchStatement);
	    }

	    if (domainSearchStatement != null &&
		domainSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (domainSearchStatement);
	    }

	    if (actionSearchStatement != null &&
		actionSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (actionSearchStatement);
	    }

	    if (subjectSearchStatement != null &&
		subjectSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (subjectSearchStatement);
	    }

	    if (timeSearchStatement != null &&
		timeSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (timeSearchStatement);
	    }

	    if (userIdSearchStatement != null &&
		userIdSearchStatement.trim().length() != 0) {
		if (criteriaFound == true) {
		    sqlStatement.append(" AND ");
		} else {
		    criteriaFound = true;
		}
		sqlStatement.append (userIdSearchStatement);
	    }

	    sqlStatement.append(";");

	    logger.debug("Database SQL statement issued: " + sqlStatement.toString());

	    statement.executeUpdate (sqlStatement.toString());

	    connection.commit();
	    connection.close();
	}
	catch (SQLException e) {
	    String logString;
	    EmanagerDatabaseException ede;

	    logString =
		EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
		e.getMessage();

	    logger.log(Priority.ERROR, logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
						 logString);

	    if (connection != null) {
		try {
		    connection.rollback();
		    connection.close();
		}
		catch (SQLException ee) {
		}
	    }

	    throw ede;
	}

	return;
    }

    public UserAccount retrieveUserAccount (ManagedObjectId userAccountId) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

	UserAccount userAccount;
	String name;
	String password;
	String passwordKey;
	long securityRoleKey;

        connection = null;
	userAccount = null;

	if (userAccountId == null ||
	    userAccountId.getManagedObjectType() != ManagedObjectIdType.UserAccount) {
	    return userAccount;
	}

        sqlStatement =
	    "SELECT name, password, password_key, security_role_id FROM user_account WHERE id=" +
	    userAccountId.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
		name = rs.getString ("name");
		password = rs.getString ("password");
		passwordKey = rs.getString ("password_key");
		securityRoleKey = rs.getLong ("security_role_id");

		userAccount = new UserAccount (userAccountId.getManagedObjectKey(),
					       name,
					       password,
					       passwordKey,
					       new ManagedObjectId (ManagedObjectIdType.SecurityRole, securityRoleKey));
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return userAccount;
    }

    public Collection retrieveUserAccounts () throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

	Collection objects;
	UserAccount userAccount;
        long key;
	String name;
	String password;
	String passwordKey;
	long securityRoleKey;

        objects = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name, password, password_key, security_role_id FROM user_account;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong ("id");
                name = rs.getString ("name");
		password = rs.getString ("password");
		passwordKey = rs.getString ("password_key");
		securityRoleKey = rs.getLong ("security_role_id");

		userAccount = new UserAccount (key,
					       name,
					       password,
					       passwordKey,
					       new ManagedObjectId (ManagedObjectIdType.SecurityRole, securityRoleKey));
		objects.add (userAccount);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return objects;
    }

    public UserAccount createUserAccount (String name,
					  String password,
					  String passwordKey,
					  ManagedObjectId securityRoleId) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;
	UserAccount userAccount;

        sqlStatement = new StringBuffer();
        newId = getNextUserAccountKey();

        userAccount = new UserAccount (newId,
				       name,
				       password,
				       passwordKey,
				       securityRoleId);

        sqlStatement.append("INSERT INTO user_account " +
			    "(id, name, password, password_key, security_role_id)" +
			    " VALUES (" +
			    newId + ", " +
			    "'" + name + "', " +
			    "'" + password + "', " +
			    "'" + passwordKey + "', " +
			    securityRoleId.getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return userAccount;
    }

    public void updateUserAccount (UserAccount userAccount) throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        if (userAccount == null) {
            return;
        }

        sqlStatement = new StringBuffer();

        sqlStatement.append ("UPDATE user_account SET ");

        sqlStatement.append ("name='" + userAccount.getName() + "', ");
        sqlStatement.append ("password='" + userAccount.getPassword() + "', ");
        sqlStatement.append ("password_key='" + userAccount.getPasswordKey() + "', ");
        sqlStatement.append ("security_role_id=" + userAccount.getSecurityRoleId().getManagedObjectKey() );
        sqlStatement.append (" WHERE id=" + userAccount.getId().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement = connection.createStatement();
            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public void removeUserAccount (ManagedObjectId id) throws EmanagerDatabaseException
    {
        Collection ids;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.UserAccount) {
            return;
        }

        ids = new LinkedList();
        ids.add(id);
        removeObjects (ids);
    }

    public SecurityRole retrieveSecurityRole (ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

	SecurityRole securityRole;
	String name;

        connection = null;
	securityRole = null;

	if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.SecurityRole) {
	    return null;
	}

        sqlStatement =
	    "SELECT name FROM security_role WHERE id=" +
	    id.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
		name = rs.getString ("name");

		securityRole = new SecurityRole (id.getManagedObjectKey(),
						 name);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return securityRole;
    }

    public Collection retrieveSecurityRoles () throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

	Collection objects;
	SecurityRole securityRole;
        long key;
	String name;

        objects = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name FROM security_role;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong("id");
		name = rs.getString ("name");

		securityRole = new SecurityRole (key, name);
		objects.add (securityRole);
            }

            connection.close();
        }
        catch (SQLException e) {
	    String logString;
	    EmanagerDatabaseException ede;

	    logString =
		EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
		e.getMessage();

	    logger.log(Priority.ERROR, logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
						 logString);

	    if (connection != null) {
		try {
		    connection.close();
		}
		catch (SQLException ee) {
		}
	    }

	    throw ede;
        }

	return objects;
    }

    public SecurityRole createSecurityRole (String name) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

	SecurityRole securityRole;

        sqlStatement = new StringBuffer();
        newId = getNextSecurityRoleKey();

        securityRole = new SecurityRole (newId, name);

        sqlStatement.append("INSERT INTO security_role " +
			    "(id, name)" +
			    " VALUES (" +
			    newId + ", " +
			    "'" + name + "');");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return securityRole;
    }

    public void updateSecurityRole (SecurityRole securityRole) throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        if (securityRole == null) {
            return;
        }

        sqlStatement = new StringBuffer();

        sqlStatement.append ("UPDATE security_role SET ");

        sqlStatement.append ("name='" + securityRole.getName() + "' ");
        sqlStatement.append (" WHERE id=" + securityRole.getId().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement = connection.createStatement();
            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public void removeSecurityRole (ManagedObjectId id) throws EmanagerDatabaseException
    {
        Collection ids;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.SecurityRole) {
            return;
        }

        ids = new LinkedList();
        ids.add(id);
        removeObjects (ids);
    }

    public SolutionData createSolution(String name,
				       Collection appInstanceIds) throws EmanagerDatabaseException
    {
        long newId;
	Iterator iter;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;
        SolutionData data;

        connection = null;

        sqlStatement = new StringBuffer();
        newId = getNextSolutionKey();
        id = new ManagedObjectId (ManagedObjectIdType.Solution, newId);

	try {
	    data = new SolutionData(id,
                                    name.trim(),
                                    appInstanceIds);
	}
	catch (Exception e) {
            logger.error("Exception caught while creating a SolutionData " +
                         "in db: " + e);
            return null;
        }


        try {
            String sqlStatementString;

	    sqlStatement.append("INSERT INTO solution " +
				"(id, name)" +
				" VALUES (" +
				data.id().getManagedObjectKey() + ", " +
				"'" + data.name() + "');");

	    connection = DatabaseConnectionManager.instance().getConnection();

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);

	    if (appInstanceIds != null) {
		iter = appInstanceIds.iterator();
		while (iter.hasNext()) {
		    sqlStatement = new StringBuffer();

		    id = (ManagedObjectId) iter.next();
		    sqlStatement.append ("INSERT INTO solution_instance_relationship " +
					 "(solution_id, application_instance_id) " +
					 " VALUES (" +
					 newId + ", " +
					 id.getManagedObjectKey() + ");");

		    sqlStatementString = sqlStatement.toString();
		    statement = connection.createStatement();

		    logger.debug("Database SQL statement issued: " + sqlStatement.toString());

		    statement.executeUpdate (sqlStatementString);
		}
	    }

            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return data;
    }

    public void updateSolution(Solution solution) throws EmanagerDatabaseException
    {
        String sqlStatement;
        Connection connection;
        Statement  statement;

        if (solution == null) {
            return;
        }

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
	    sqlStatement = "UPDATE solution SET " +
                           "name='" + solution.name() + "' " +
                           " WHERE id=" + solution.id().getManagedObjectKey() + ";";

            logger.debug("Database SQL statement issued: " + sqlStatement);

            statement = connection.createStatement();
            statement.executeUpdate (sqlStatement);

	    sqlStatement = "DELETE FROM solution_instance_relationship WHERE solution_id=" +
                           solution.id().getManagedObjectKey();

            logger.debug("Database SQL statement issued: " + sqlStatement);

            statement = connection.createStatement();
            statement.executeUpdate (sqlStatement);

	    if (solution.appInstanceIds() != null &&
		solution.appInstanceIds().size() != 0) {
		Iterator iter;
		ManagedObjectId id;

		iter = solution.appInstanceIds().iterator();
		while (iter.hasNext()) {
		    id = (ManagedObjectId) iter.next();

		    sqlStatement = "INSERT INTO solution_instance_relationship " +
                                   "(solution_id, application_instance_id) " +
                                   " VALUES (" +
                                   solution.id().getManagedObjectKey() + ", " +
                                   id.getManagedObjectKey() + ");";

		    statement = connection.createStatement();

		    logger.debug("Database SQL statement issued: " + sqlStatement);

		    statement.executeUpdate (sqlStatement);
		}
	    }

            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public Collection retrieveSolutions() throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

	List names;
	List solnIds;
        Collection objects;
        long key;
        ManagedObjectId id;
        String name;
        Collection appInstanceIds;
        SolutionData data;

	names = new LinkedList();
	solnIds = new LinkedList();
        objects = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name FROM solution;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement);

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                key = rs.getLong("id");
                id = new ManagedObjectId (ManagedObjectIdType.Solution, key);
		name = rs.getString ("name");
		solnIds.add (id);
		names.add (name);
	    }

	    if (solnIds.size() > 0) {
		int i;
		Iterator iter;
		Iterator iter2;

		iter = solnIds.iterator();
		iter2 = names.iterator();
		while (iter.hasNext()) {
		    id = (ManagedObjectId) iter.next();
		    name = (String) iter2.next();

		    sqlStatement ="SELECT application_instance_id FROM solution_instance_relationship " +
				  "WHERE solution_id=" + id.getManagedObjectKey() + ";";

		    statement = connection.createStatement();

		    logger.debug("Database SQL statement issued: " + sqlStatement);

		    rs = statement.executeQuery(sqlStatement);

		    appInstanceIds = new LinkedList();
		    while (rs.next()) {
			key = rs.getLong ("application_instance_id");

			appInstanceIds.add (new ManagedObjectId(ManagedObjectIdType.ApplicationInstance, key));
		    }

                    try {
                        data = new SolutionData(id, name, appInstanceIds);
                        objects.add(data);
                    }
                    catch (Exception e) {
                        logger.warn("Unexpected exception creating SolutionData object: " + e);
                    }
		}
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return objects;
    }

    public SolutionData retrieveSolution(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

	long key;
	String name;
	Collection appInstanceIds;
        SolutionData data;

	if (id.getManagedObjectType() != ManagedObjectIdType.Solution) {
	    return null;
	}

	name = null;
        data = null;
        connection = null;
	appInstanceIds = new LinkedList();

        sqlStatement = "SELECT name FROM solution where id=" + id.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement);

            rs = statement.executeQuery(sqlStatement.toString());
            while (rs.next()) {
                name = rs.getString("name");
            }

	    if (name != null) {
		sqlStatement =
		    "SELECT application_instance_id FROM solution_instance_relationship " +
		    "WHERE solution_id=" + id.getManagedObjectKey() + ";";

		    statement = connection.createStatement();

		    logger.debug("Database SQL statement issued: " + sqlStatement);

		    rs = statement.executeQuery(sqlStatement);

		    while (rs.next()) {
			key = rs.getLong ("application_instance_id");

			appInstanceIds.add (new ManagedObjectId(ManagedObjectIdType.ApplicationInstance, key));
		    }

                    try {
                        data = new SolutionData (id, name, appInstanceIds);
                    }
                    catch (Exception e) {
                        logger.warn("Unexpected exception creating SolutionData object: " + e);
                    }
	    }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.log(Priority.ERROR, logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return data;
    }


    /**
     * @param solution The key of the agent host to remove from the database.
     */
    public void removeSolution(Solution solution) throws EmanagerDatabaseException
    {
        Collection ids;

        if (solution == null) {
            return;
        }

        ids = new LinkedList();
        ids.add(solution.id());
        removeObjects (ids);
    }

    /**
     * @param solutions
     */
    public void removeSolutions(Collection solutions) throws EmanagerDatabaseException
    {
        Iterator iter;
        Collection ids;

        if (solutions == null ||
            solutions.isEmpty()) {
            return;
        }

        ids = new LinkedList();
        iter = solutions.iterator();
        while (iter.hasNext()) {
            ids.add ( ((Solution)iter.next()).id());
        }

        removeObjects (ids);
    }

    /**
     * @param containerNode The ContainerNode object to create in the database.
     * @return The ContainerNode object that contains the database key.
     */
    public ManagedObjectId createSolutionHierarchyNode(ContainerNodeData nodeData) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        ManagedObjectId parentId;

        id = null;
        sqlStatement = new StringBuffer();

        if (nodeData == null) {
            return null;
        }

        name = nodeData.name();
        parentId = nodeData.parentId();

        if (name == null ||
            name.trim().length() == 0) {
            logger.error (EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                         " The name of solution hierarchy node is invalid: " + name);
            throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.IllegalColumnData,
                                                 EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                                                 " The name of solution hierarchy node is invalid: " + name);
        }

        if (parentId == null ||
            parentId.getManagedObjectType() != ManagedObjectIdType.SolutionHierarchyContainer) {
            logger.error (EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                         " The parent id of solution hierarchy node is invalid: " + parentId);
            throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.IllegalColumnData,
                                                 EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                                                 " The parent id of solution hierarchy node is invalid: " + parentId);
        }

        newId = this.getNextSolutionHierarchyKey();
        id = new ManagedObjectId (ManagedObjectIdType.SolutionHierarchyContainer, newId);

        sqlStatement.append ("INSERT INTO solution_hierarchy " +
                             "(id, name, solution_hierarchy_id)" +
                             " VALUES (" +
                             newId + ", " +
                             "'" + name.trim() + "', " +
                             parentId.getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error (logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return id;
    }

    /**
     * @param containerNode The ContainerNode object to update in the database.
     * @roseuid 3F4256F103CE
     */
    public void updateSolutionHierarchyNode(ContainerNodeData nodeData) throws EmanagerDatabaseException
    {
        long newId;
        ManagedObjectId id;
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        String name;
        ManagedObjectId parentId;

        if (nodeData == null) {
            return;
        }

        id = null;
        sqlStatement = new StringBuffer();

        name = nodeData.name();
        parentId = nodeData.parentId();

        if (name == null ||
            name.trim().length() == 0) {
            logger.error (EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                         " The name of solution hierarchy node is invalid: " + name);
            throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.IllegalColumnData,
                                                 EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                                                 " The name of solution hierarchy node is invalid: " + name);
        }

        if (parentId == null ||
            parentId.getManagedObjectType() != ManagedObjectIdType.SolutionHierarchyContainer) {
            logger.error (EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                         " The parent id of solution hierarchy node is invalid: " + parentId);
            throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.IllegalColumnData,
                                                 EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                                                 " The parent id of solution hierarchy node is invalid: " + parentId);
        }

        sqlStatement.append ("UPDATE solution_hierarchy SET " +
                             "name='" + name.trim() + "', " +
                             "solution_hierarchy_id=" + parentId.getManagedObjectKey() +
                             " WHERE " +
                             "id=" + nodeData.id().getManagedObjectKey() + ";" );

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error(logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

        return;
    }

    public Collection retrieveSolutionHierarchyNodes() throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long nodeKey;
        ManagedObjectId id;
        String name;
        long parentKey;
        ManagedObjectId parentId;
        ContainerNodeData nodeData;

        nodes = new LinkedList();
        connection = null;

        sqlStatement = "SELECT id, name, solution_hierarchy_id FROM solution_hierarchy;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                nodeKey = rs.getLong("id");
                id = new ManagedObjectId (ManagedObjectIdType.SolutionHierarchyContainer, nodeKey);
                name = rs.getString("name");
                parentKey = rs.getLong("solution_hierarchy_id");
                parentId = new ManagedObjectId (ManagedObjectIdType.SolutionHierarchyContainer, parentKey);

                nodeData = new ContainerNodeData (id, name, parentId);
                nodes.add (nodeData);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error (logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public ContainerNodeData retrieveSolutionHierarchyNode(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        StringBuffer sqlStatement;

        String name;
        long parentKey;
        ManagedObjectId parentId;
        ContainerNodeData nodeData;

        nodeData = null;
        connection = null;

	if (id.getManagedObjectType() != ManagedObjectIdType.SolutionHierarchyContainer) {
	    return null;
	}

        sqlStatement = new StringBuffer ("SELECT name, solution_hierarchy_id FROM solution_hierarchy WHERE id=");
        sqlStatement.append (id.getManagedObjectKey() + ";");

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement.toString());
            while (rs.next()) {
                name = rs.getString("name");
                parentKey = rs.getLong("solution_hierarchy_id");
                parentId = new ManagedObjectId (ManagedObjectIdType.SolutionHierarchyContainer, parentKey);
                nodeData = new ContainerNodeData (id, name, parentId);
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error (logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodeData;
    }

    /**
     * @param containerNodeKey The ContainerNode key to remove from the database.
     * @roseuid 3F42570A00F0
     */
    public void removeSolutionHierarchyNode(ManagedObjectId id) throws EmanagerDatabaseException
    {
        Collection ids;

        if (id == null ||
	    id.getManagedObjectType() != ManagedObjectIdType.SolutionHierarchyContainer) {
            return;
        }

        ids = new LinkedList();
        ids.add(id);
        removeObjects (ids);
    }

    public void removeSolutionHierarchyNodes (Collection ids) throws EmanagerDatabaseException
    {
	Iterator iter;
	ManagedObjectId id;

        if (ids == null ||
            ids.isEmpty()) {
            return;
        }

	iter = ids.iterator();
	id = (ManagedObjectId) iter.next();
	if (id.getManagedObjectType() != ManagedObjectIdType.SolutionHierarchyContainer) {
	    return;
	}

        removeObjects (ids);
    }

    public void createSolutionHierarchyRelationshipNode(SolutionHierarchyRelationshipData data)
	throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        sqlStatement = new StringBuffer();

        if (data == null) {
            return;
        }

        if (data.solutionHierarchyId() == null ||
            data.solutionHierarchyId().getManagedObjectType() != ManagedObjectIdType.SolutionHierarchyContainer) {
            logger.error (EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                         " The solution hierarchy id of the solution relationship node is invalid: " +
                         data.solutionHierarchyId());
            throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.IllegalColumnData,
                                                 EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                                                 " The solution hierarchy id of the solution relationship node is invalid: " +
                                                 data.solutionHierarchyId());
        }

        if (data.solutionId() == null ||
            data.solutionId().getManagedObjectType() != ManagedObjectIdType.Solution) {
            logger.error (EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                         " The solution id of the solution relationship node is invalid: " +
                         data.solutionId());
            throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.IllegalColumnData,
                                                 EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                                                 " The solution id of the solution relationship node is invalid: " +
                                                 data.solutionId());
        }

        sqlStatement.append ("INSERT INTO solution_hierarchy_relationship " +
                             "(solution_id, solution_hierarchy_id)" +
                             " VALUES (" +
                             data.solutionId().getManagedObjectKey() + ", " +
                             data.solutionHierarchyId().getManagedObjectKey() + ");");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error (logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

	return;
    }

    public void removeSolutionHierarchyRelationshipNode(SolutionHierarchyRelationshipData data)
	throws EmanagerDatabaseException
    {
        StringBuffer sqlStatement;
        Connection connection;
        Statement  statement;

        sqlStatement = new StringBuffer();

        if (data == null) {
            return;
        }

        if (data.solutionHierarchyId() == null ||
            data.solutionHierarchyId().getManagedObjectType() != ManagedObjectIdType.SolutionHierarchyContainer) {
            logger.error (EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                         " The solution hierarchy id of the solution relationship node is invalid: " +
                         data.solutionHierarchyId());
            throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.IllegalColumnData,
                                                 EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                                                 " The solution hierarchy id of the solution relationship node is invalid: " +
                                                 data.solutionHierarchyId());
        }

        if (data.solutionId() == null ||
            data.solutionId().getManagedObjectType() != ManagedObjectIdType.Solution) {
            logger.error (EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                         " The solution id of the solution relationship node is invalid: " +
                         data.solutionId());
            throw new EmanagerDatabaseException (EmanagerDatabaseStatusCode.IllegalColumnData,
                                                 EmanagerDatabaseStatusCode.IllegalColumnData.getStatusCodeDescription() +
                                                 " The solution id of the solution relationship node is invalid: " +
                                                 data.solutionId());
        }

        sqlStatement.append ("DELETE FROM solution_hierarchy_relationship " +
                             "WHERE " +
			     "solution_id=" + data.solutionId().getManagedObjectKey() +
			     " AND " +
			     "solution_hierarchy_id=" + data.solutionHierarchyId().getManagedObjectKey() + ";");

        connection = DatabaseConnectionManager.instance().getConnection();

        try {
            String sqlStatementString;

            sqlStatementString = sqlStatement.toString();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            statement.executeUpdate (sqlStatementString);
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error (logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.rollback();
                        connection.close();
                    }
                    catch (SQLException closeE) {
                    }
                }

                throw ede;
        }

	return;
    }

    public Collection retrieveSolutionHierarchyRelationshipNodes()
	throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long solutionKey;
        ManagedObjectId solutionId;
        long containerKey;
        ManagedObjectId containerId;
	SolutionHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

        sqlStatement = "SELECT solution_id, solution_hierarchy_id FROM solution_hierarchy_relationship;";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                solutionKey = rs.getLong("solution_id");
                solutionId = new ManagedObjectId (ManagedObjectIdType.Solution, solutionKey);
                containerKey = rs.getLong("solution_hierarchy_id");
                containerId = new ManagedObjectId (ManagedObjectIdType.SolutionHierarchyContainer, containerKey);

                try {
                    data = new SolutionHierarchyRelationshipData(solutionId, containerId);
                    nodes.add (data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error (logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public Collection retrieveSolutionHierarchyRelationshipPhysContainerNodes(ManagedObjectId solutionId)
        throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long containerKey;
        ManagedObjectId containerId;
        SolutionHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

	if (solutionId == null ||
            solutionId.getManagedObjectType() != ManagedObjectIdType.Solution) {
	    return nodes;
	}

        sqlStatement =
            "SELECT solution_hierarchy_id FROM solution_hierarchy_relationship WHERE solution_id=" +
            solutionId.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                containerKey = rs.getLong("solution_hierarchy_id");
                containerId = new ManagedObjectId (ManagedObjectIdType.SolutionHierarchyContainer, containerKey);

                try {
                    data = new SolutionHierarchyRelationshipData(solutionId, containerId);
                    nodes.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error (logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }

    public Collection retrieveSolutionHierarchyRelationshipSolutionNodes(ManagedObjectId containerId)
	throws EmanagerDatabaseException
    {
        Connection connection;
        Statement statement;
        ResultSet rs;
        String sqlStatement;

        Collection nodes;
        long solutionKey;
        ManagedObjectId solutionId;
	SolutionHierarchyRelationshipData data;

        nodes = new LinkedList();
        connection = null;

	if (containerId == null ||
	    containerId.getManagedObjectType() != ManagedObjectIdType.SolutionHierarchyContainer) {
	    return nodes;
	}

        sqlStatement =
	    "SELECT solution_id FROM solution_hierarchy_relationship WHERE solution_hierarchy_id=" +
	    containerId.getManagedObjectKey() + ";";

        try {
            connection = DatabaseConnectionManager.instance().getConnection();
            statement = connection.createStatement();

            logger.debug("Database SQL statement issued: " + sqlStatement.toString());

            rs = statement.executeQuery(sqlStatement);
            while (rs.next()) {
                solutionKey = rs.getLong("solution_id");
                solutionId = new ManagedObjectId (ManagedObjectIdType.Solution, solutionKey);

                try {
                    data = new SolutionHierarchyRelationshipData(solutionId, containerId);
                    nodes.add(data);
                }
                catch (Exception e) {
                }
            }

            connection.close();
        }
        catch (SQLException e) {
                String logString;
                EmanagerDatabaseException ede;

                logString =
                    EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered.getStatusCodeDescription() +
                    e.getMessage();

                logger.error (logString);
                ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseSQLExceptionEncountered,
                                                     logString);

                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (SQLException ee) {
                    }
                }

                throw ede;
        }

        return nodes;
    }
}
