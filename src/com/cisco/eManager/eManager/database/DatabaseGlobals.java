package com.cisco.eManager.eManager.database;

public class DatabaseGlobals
{
    public static String JNDIRegistryBindingNameKey = "jndi.registry.binding.name";
    public static String JNDIRegistryBindingNameDefault = "EmanagerRegistry";
    public static String JNDIRegistryBindingNameValueMsg = "The JNDI registry binding name is ";
    public static String JNDIRegistryPortKey = "jndi.registry.port";
    public static int    JNDIRegistryPortDefault = 10002;
    public static String DatabaseConnectionUserAccountKey = "database.connection.user.account";
    public static String DatabaseConnectionUserAccountDefault = "DBA";
    public static String UserAccountValueMsg = "The database connection user account is: ";
    public static String DatabaseConnectionPasswordKey = "database.connection.password";
    public static String DatabaseConnectionPasswordDefault = "SQL";
    public static String PasswordValueMsg = "The database connection user account password is: ";
    public static String DatabaseConnectionDatabaseNameKey = "database.connection.database.name";
    public static String DatabaseConnectionDatabaseNameDefault = "emanager";
    public static String DatabaseNameValueMsg = "The database name is: ";
    public static String DatabaseConnectionDatabaseHostKey = "database.connection.database.host";
    public static String DatabaseConnectionDatabaseHostDefault = "MJMATCH-W2K1.amer.cisco.com";
    public static String DatabaseHostValueMsg = "The database host is: ";
    public static String RegistryHostValueMsg = "The connection registry host is: ";
    public static String DatabaseConnectionClientAppNameKey = "database.connection.client.app.name";
    public static String DatabaseConnectionClientAppNameDefault = "emanager";
    public static String ClientAppNameValueMsg = "The database connection client application name is: ";
    public static String DatabaseConnectionUserMetaDataKey = "database.connection.user.meta.data";
    public static String DatabaseConnectionUserMetaDataDefault = "UserMetaDataKey";
    public static String UserMetaDataValueMsg = "The database connection user meta data value is: ";
    public static String DatabaseConnectionUseRepeatReadKey = "database.connection.use.repeat.read";
    public static String DatabaseConnectionUseRepeatReadDefault = "yes";
    public static String UseRepeatReadValueMsg = "The database connection use repeat read value is: ";
    public static String DatabaseConnectionCharsetConverterKey = "database.connection.charset.converter.key";
    public static String DatabaseConnectionCharsetConverterDefault = " ";
    public static String CharsetConverterValueMsg = "The database connection charset converter value is : ";
    public static String DatabaseConnectionConnectionPoolDescriptionKey = "database.connection.connection.pool.description";
    public static String DatabaseConnectionConnectionPoolDescriptionDefault = "Emanager.Connection.Pool";
    public static String ConnectionPoolDescriptionValueMsg = "The database connection pool description: ";
    public static String DatabaseConnectionConnectionPortKey = "database.connection.connection.port";
    public static int    DatabaseConnectionConnectionPortDefault = 2638;
    public static String DatabaseConnectionConnectionPoolSizeKey = "database.connection.connection.pool.size";
    public static int    DatabaseConnectionConnectionPoolSizeDefault = 10;
    public static int    DatabaseMaximumQuerySubsetSize = 4;

    public static String DatabaseEventKeyValueSeparator = "=";
    public static String DatabaseEventParmDelimeter = ";";
    public static String TibcoEventIdKey = "TibcoEventId";
    public static String TibcoRuleTextKey = "TibcoRuleText";
    public static String TibcoRuleTestKey = "TibcoRuleTest";
    public static String TibcoInstrumentationNameKey = "InstrumentationName";
    public static String TibcoEventMgmtPolicyId = "MgmtPolicyId";

    public static int    DatabaseTibcoEventType = 1;
    public static int    DatabaseEmanagerEventType = 2;
    public static int    DatabaseProcessSequencerEventType = 3;
}
