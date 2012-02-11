package com.cisco.eManager.eManager.database;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;

import org.apache.log4j.*;

import com.sybase.jdbc2.jdbc.SybConnectionPoolDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import javax.sql.*;
import java.sql.*;

import com.cisco.eManager.common.database.EmanagerDatabaseException;
import com.cisco.eManager.common.database.EmanagerDatabaseStatusCode;

import com.cisco.eManager.eManager.database.DatabaseGlobals;

import com.cisco.eManager.eManager.util.GlobalProperties;

public class DatabaseConnectionManager implements ConnectionEventListener
{
    private static Logger logger = Logger.getLogger(DatabaseConnectionManager.class);

    /**
     *  Connection pool datasource.
     */
    ConnectionPoolDataSource poolDataSource = null;

    /**
     *  Container of the pooled connections.
     */
    List connectionPool = Collections.synchronizedList(new LinkedList());

    /**
     *  a JNDI context.
     */
    Context jndiContext = null;

    private boolean SHUTTING_DOWN = false;

    // Driver config
    // final static String DRIVER_NAME = "com.sybase.jdbc2.jdbc.SybDriver";

    // private SybDriver sybaseDriver = null;

    /*
     * We are using a single instance to allow for pools now.
     */
    private static DatabaseConnectionManager connectionManager = null;

    private static final String JNDIContextName = "jdbc/emanagerDB";

    public static final long InvalidConnectionId = -1;

    // Database Connection properties
    private String userAccount;
    private String password;
    private String databaseName;
    private String databaseHost;
    private String clientAppName;
    private String userMetaData;
    private String useRepeatRead;
    private String charsetConverter;
    private String connectionPoolDescription;
    private int    connectionPort;
    private static int    connectionPoolSize;

    /**
     *  Constructor for the DatabaseConnectionManager object
     *
     */
    private DatabaseConnectionManager() throws EmanagerDatabaseException
    {
	jndiContext = JNDIRegistryManager.instance().getJNDIContext();
	initializeDatabaseConnectionConfigurationParameters();
	initializeDatabaseContext();
	initializeConnectionPoolConnections();
    }

    private void initializeDatabaseConnectionConfigurationParameters()
    {
	String propertyValue;
	Properties systemProperties;

	systemProperties = GlobalProperties.instance().getProperties();

	userAccount = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionUserAccountKey);
        if (userAccount == null) {
            userAccount = DatabaseGlobals.DatabaseConnectionUserAccountDefault;
	}
	logger.info (DatabaseGlobals.UserAccountValueMsg +
		     userAccount);

	password = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionPasswordKey);
        if (password == null) {
            password = DatabaseGlobals.DatabaseConnectionPasswordDefault;
	}
	logger.info (DatabaseGlobals.PasswordValueMsg +
		     password);

	databaseName = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionDatabaseNameKey);
        if (databaseName == null) {
            databaseName = DatabaseGlobals.DatabaseConnectionDatabaseNameDefault;
	}
	logger.info (DatabaseGlobals.DatabaseNameValueMsg +
		     databaseName);

	databaseHost = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionDatabaseHostKey);
        if (databaseHost == null) {
            databaseHost = DatabaseGlobals.DatabaseConnectionDatabaseHostDefault;
	}
	logger.info (DatabaseGlobals.DatabaseHostValueMsg +
		     databaseHost);

	clientAppName = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionClientAppNameKey);
        if (clientAppName == null) {
            clientAppName = DatabaseGlobals.DatabaseConnectionClientAppNameDefault;
	}
	logger.info (DatabaseGlobals.ClientAppNameValueMsg +
		     clientAppName);

	userMetaData = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionUserMetaDataKey);
        if (userMetaData == null) {
            userMetaData = DatabaseGlobals.DatabaseConnectionUserMetaDataDefault;
	}
	logger.info (DatabaseGlobals.UserMetaDataValueMsg +
		     userMetaData);

	useRepeatRead = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionUseRepeatReadKey);
        if (useRepeatRead == null) {
            useRepeatRead = DatabaseGlobals.DatabaseConnectionUseRepeatReadDefault;
	}
	logger.info (DatabaseGlobals.UseRepeatReadValueMsg +
		     useRepeatRead);

	charsetConverter = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionCharsetConverterKey);
        if (charsetConverter == null) {
            charsetConverter = DatabaseGlobals.DatabaseConnectionCharsetConverterDefault;
	}
	logger.info (DatabaseGlobals.CharsetConverterValueMsg +
		     charsetConverter);

	connectionPoolDescription = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionConnectionPoolDescriptionKey);
        if (connectionPoolDescription == null) {
            connectionPoolDescription = DatabaseGlobals.DatabaseConnectionConnectionPoolDescriptionDefault;
	}
	logger.info (DatabaseGlobals.ConnectionPoolDescriptionValueMsg +
		     connectionPoolDescription);

	propertyValue = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionConnectionPortKey);
        if (propertyValue == null) {
            connectionPort = DatabaseGlobals.DatabaseConnectionConnectionPortDefault;
	    logger.info ("Using default connectionPort value: " +
			 connectionPort);
	} else {
	    try {
		connectionPort = Integer.parseInt (propertyValue);
		logger.info ("Using connectionPort value: " +
			     connectionPort);
	    }
	    catch (NumberFormatException e) {
		connectionPort = DatabaseGlobals.DatabaseConnectionConnectionPortDefault;
		logger.info ("Error converting property value.  Using default connectionPort value: " +
			     connectionPort);

	    }
	}

	propertyValue = systemProperties.getProperty (DatabaseGlobals.DatabaseConnectionConnectionPoolSizeKey);
        if (propertyValue == null) {
            connectionPoolSize = DatabaseGlobals.DatabaseConnectionConnectionPoolSizeDefault;
	    logger.info ("Using default connectionPoolSize value: " +
			 connectionPoolSize);
	} else {
	    try {
		connectionPoolSize = Integer.parseInt (propertyValue);
		logger.info ("Using connectionPoolSize value: " +
			     connectionPoolSize);
	    }
	    catch (NumberFormatException e) {
		connectionPoolSize = DatabaseGlobals.DatabaseConnectionConnectionPoolSizeDefault;
		logger.info ("Error converting property value.  Using default connectionPoolSize value: " +
			     connectionPoolSize);
	    }
	}
    }

    private void initializeDatabaseContext() throws EmanagerDatabaseException
    {
	logger.debug ("Enter");

	Properties properties;
	SybConnectionPoolDataSource poolDataSource;

	properties = new Properties();
	poolDataSource = new com.sybase.jdbc2.jdbc.SybConnectionPoolDataSource();

	poolDataSource.setUser(userAccount);
	poolDataSource.setPassword(password);
	poolDataSource.setDatabaseName(databaseName);
	poolDataSource.setServerName(databaseHost);
	poolDataSource.setPortNumber(connectionPort);
	poolDataSource.setDescription(connectionPoolDescription);

	properties.put("user", userAccount);
	properties.put("password", password);
	properties.put("APPLICATIONNAME", clientAppName);
        // fix
        // hopefully these have defaults
	// properties.put("USE_METADATA", userMetaData);
        // properties.put("REPEAT_READ", useRepeatRead);
	// properties.put("CHARSET_CONVERTER_CLASS", charsetConverter);

	properties.put("server", "jdbc:sybase:Tds:" + databaseHost + ":" + connectionPort);

	try {
            poolDataSource.setConnectionProperties(properties);
	    // jndiContext.bind("jdbc/protoDB", poolDataSource);
	    jndiContext.bind(JNDIContextName, poolDataSource);
	}
	catch (Exception ex) {
	    String logString;
	    EmanagerDatabaseException ede;

	    logString =
		EmanagerDatabaseStatusCode.UnableToBindJNDIContext.getStatusCodeDescription() +
		ex.getMessage();

	    logger.fatal(logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.UnableToBindJNDIContext,
						 logString);
	    throw ede;
	}
    }

    /**
     *  The constructor obtains a ConnectionPoolDataSource reference via JNDI. This
     *  datasource is used when new database connections need to be established and
     *  maintained in some container (pool).
     */
    public void initializeConnectionPoolConnections() throws EmanagerDatabaseException
    {
	logger.debug ("enter");

	try {
	    poolDataSource = (ConnectionPoolDataSource) jndiContext.lookup(JNDIContextName);
	}
	catch (Exception ex) {
	    String logString;
	    EmanagerDatabaseException ede;

	    logString =
		EmanagerDatabaseStatusCode.DatabaseJNDILookupFailure.getStatusCodeDescription() +
		ex.getMessage();

	    logger.fatal (logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseJNDILookupFailure,
						 logString);
	    throw ede;
	}

	for (int i = 0; i < connectionPoolSize; i++) {
	    installConnection();
	}
    }

    /**
     *  Creates a Pooled connection and adds it to the connection pool.
     */
    private void installConnection() throws EmanagerDatabaseException
    {
	logger.debug ("enter");

	PooledConnection connection;

	try {
	    connection = poolDataSource.getPooledConnection();
	    connection.addConnectionEventListener(this);
	    connection.getConnection().setAutoCommit (false);
	    synchronized (connectionPool) {
		connectionPool.add(connection);
		logger.debug ("Database connection added.");
	    }
	}
	catch (SQLException ex) {
            logger.fatal("exception caught while obtaining database " +
                         "connection: ex = " + ex);
            SQLException ex1 = ex.getNextException();
            while ( ex1 != null )
            {
                logger.fatal("chained sql exception ex1 = " + ex1);
                SQLException nextEx = ex1.getNextException();
                ex1 = nextEx;
            }
	    String logString;
	    EmanagerDatabaseException ede;


	    logString =
		EmanagerDatabaseStatusCode.DatabaseConnectionFailure.getStatusCodeDescription() +
		ex.getMessage();

	    logger.fatal (logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.DatabaseConnectionFailure,
						 logString);
	    throw ede;
	}
    }

    /**
     * @return Connection
     * @roseuid 3F3A5FFD0338
     */
    public Connection getConnection() throws EmanagerDatabaseException
    {
	long             connectionId;
        Connection       connection;
	PooledConnection pooledConnection;

	connection = null;
	pooledConnection = null;
	connectionId = InvalidConnectionId;

	try {
	    synchronized (connectionPool) {
		if (!connectionPool.isEmpty()) {
		    try {
			boolean connectionClosed;

			connectionClosed = false;

			pooledConnection = (PooledConnection) connectionPool.remove(0);
			connection = pooledConnection.getConnection();
			connection.clearWarnings();
			connectionId = getConnectionID (connection);
			connectionClosed = connection.isClosed();
			if (connectionId == InvalidConnectionId ||
			    connectionClosed == true) {
                            logger.debug ("Pooled connection closed.");
			    connection = null;
			}
		    }
		    catch (SQLException sqe) {
			logger.debug ("Pooled connection closed.");
			connection = null;
		    }
		}
	    }

	    if (connection == null) {
		logger.debug ("Getting a new connection.");
		pooledConnection = poolDataSource.getPooledConnection();
		pooledConnection.addConnectionEventListener(this);
		connection = pooledConnection.getConnection();
		connection.clearWarnings();
		connectionId = getConnectionID (connection);
	    }
	}
	catch (SQLException sqe) {
	    String logString;
	    EmanagerDatabaseException ede;

	    logString =
		EmanagerDatabaseStatusCode.UnableToGetPooledConnection.getStatusCodeDescription() +
		sqe.getMessage();

	    logger.error (logString);
	    ede = new EmanagerDatabaseException (EmanagerDatabaseStatusCode.UnableToGetPooledConnection,
						 logString);
	    throw ede;
	}

	if (connectionId == InvalidConnectionId) {
	    EmanagerDatabaseException ede;
	    ede = new EmanagerDatabaseException
		(EmanagerDatabaseStatusCode.UnableToGetPooledConnection,
		 EmanagerDatabaseStatusCode.UnableToGetPooledConnection.getStatusCodeDescription());
	    throw ede;
	}

	logger.debug("\n*****************************"
		     + "\nPooled Connection Init"
		     + "\nCon ID:" + connectionId
		     + "\nCon Object:" + pooledConnection
		     + "\nPool Object:" + connection
		     + "\n*****************************");

        return connection;
    }

    /**
     * @return com.cisco.eManager.eManager.database.DatabaseConnectionManager
     * @roseuid 3F3A63E40370
     */
    public static DatabaseConnectionManager instance() throws EmanagerDatabaseException
    {
	if (connectionManager == null) {
	    connectionManager = new DatabaseConnectionManager ();
	}

	return connectionManager;
    }


    /**
     * @roseuid 3F3A88C50359
     */
    private void addConnectionToPool()
    {

    }

    /**
     * @param maximumConnections
     * @roseuid 3F3A891C02B3
     */
    public static void setMaximumPooledConnections(int maximumConnections)
    {
	connectionPoolSize = maximumConnections;
    }

    /**
     * @return int
     * @roseuid 3F3A8939033A
     */
    public static int getMaximumPooledConnections()
    {
	return connectionPoolSize;
    }

    /**
     * @roseuid 3F3A89D40175
     */
    public void shutdown()
    {
	logger.debug ("Enter");

	Iterator iter;
	PooledConnection pooledConnection;

	SHUTTING_DOWN = true;
	iter = connectionPool.iterator();
	while (iter.hasNext()) {
	    pooledConnection = (PooledConnection) iter.next();

	    try {
		if (!pooledConnection.getConnection().isClosed()) {
		    pooledConnection.getConnection().close();
		}
	    }
	    catch (Exception ex) {
		// We don't care what happens here, we're on the way out!
	    }
	}

	connectionPool.clear();
    }

    /**
     * @param arg0
     * @roseuid 3F4E5F400123
     */
    public void connectionClosed(ConnectionEvent event)
    {
	logger.debug ("Enter: " + event.getSource());

	synchronized (connectionPool) {
            if (!SHUTTING_DOWN) {
                if (connectionPool.size() < connectionPoolSize) {
                    logger.debug ("Reading Connection: " + event.getSource());

                    connectionPool.add(event.getSource());
                }
	    }
	}
    }

    /**
     *  Gets the ConnectionID attribute of the SybaseConnector class
     *
     *@param  connection
     *@return      The ConnectionID value
     */
    public static long getConnectionID(Connection connection) {
	try {
	    Statement stmt = connection.createStatement();
	    ResultSet res = stmt.executeQuery("select connection_property('Number')");
	    res.next();
	    return res.getLong(1);
	}
	catch (SQLException ex) {
	    logger.error("SQL exception retrieving connection ID:" + ex.getMessage());
	}

	return InvalidConnectionId;
    }

    /**
     *  Checks if a lock is left behind in the DB.
     *
     *@param  con
     *@param  conID
     *@return        true if a lock is found.
     */
    public static boolean checkLocks(Connection connection, long conID)
    {
	try {
	    Statement stmt = connection.createStatement();
	    ResultSet res = stmt.executeQuery("exec sa_locks " + conID);
	    while (res.next()) {
		return true;
	    }
	}
	catch (SQLException ex) {
	    logger.error ("Unable to retrieve connection ID", ex);
	}
	return false;
    }

    /**
     * @param arg0
     * @roseuid 3F4E5F40012D
     */
    public void connectionErrorOccurred(ConnectionEvent event)
    {
	logger.debug ("Connection Error " + event.getSQLException().getMessage());

	synchronized (connectionPool) {
	    if (connectionPool.size() <= connectionPoolSize) {
		connectionPool.remove(event.getSource());
		if (!SHUTTING_DOWN) {
                    try {
                        installConnection();
                    }
                    catch (EmanagerDatabaseException e) {
                        // noop.  can't throw an exception here, so we'll ignore.
                        // It will surface later.
                    }
		}
	    }
	}
    }

    /**
     *  Resets the con pool.
     *
     *@exception  NamingException  Description of Exception
     */
    public void reset() throws NamingException, EmanagerDatabaseException {
	shutdown();
	jndiContext.unbind(JNDIContextName);
	instance();
    }

    public String getDatabaseConnectionDatabaseHost()
    {
        return databaseHost;
    }
}
