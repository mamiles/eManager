//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\common\\inventory\\LogLevel.java

package com.cisco.eManager.common.inventory;


/**
 * @author wstubb
 * @version 1.0
 */
public class LogLevel
{
    private String m_logLevel;

    private static String LOG_LEVEL_SEVERE = "SEVERE";
    private static String LOG_LEVEL_WARNING = "WARNING";
    private static String LOG_LEVEL_INFO = "INFO";
    private static String LOG_LEVEL_CONFIG = "CONFIG";
    private static String LOG_LEVEL_FINE = "FINE";
    private static String LOG_LEVEL_FINER = "FINER";
    private static String LOG_LEVEL_FINEST = "FINEST";

    public static LogLevel SEVERE = new LogLevel(LOG_LEVEL_SEVERE);
    public static LogLevel WARNING = new LogLevel(LOG_LEVEL_WARNING);
    public static LogLevel INFO = new LogLevel(LOG_LEVEL_INFO);
    public static LogLevel CONFIG = new LogLevel(LOG_LEVEL_CONFIG);
    public static LogLevel FINE = new LogLevel(LOG_LEVEL_FINE);
    public static LogLevel FINER = new LogLevel(LOG_LEVEL_FINER);
    public static LogLevel FINEST = new LogLevel(LOG_LEVEL_FINEST);

    public LogLevel()
    {
        // this class shouldn't have a default ctor, except that it needs one
        // because it's part of a SOAP/http interface described via wsdl
        m_logLevel = LOG_LEVEL_INFO;
    }

    /**
     * @param logLevel
     * @roseuid 3F6226AE013B
     */
    private LogLevel(String logLevel)
    {
        m_logLevel = logLevel;
    }

    /**
     * @return boolean
     * @roseuid 3F6226E30232
     */
    public boolean isSevere()
    {
        return (m_logLevel.equals(LOG_LEVEL_SEVERE));
    }

    /**
     * @return boolean
     * @roseuid 3F6226EC0393
     */
    public boolean isWarning()
    {
        return (m_logLevel.equals(LOG_LEVEL_WARNING));
    }

    /**
     * @return boolean
     * @roseuid 3F6226F40196
     */
    public boolean isInfo()
    {
        return (m_logLevel.equals(LOG_LEVEL_INFO));
    }

    public boolean isConfig()
    {
        return (m_logLevel.equals(LOG_LEVEL_CONFIG));
    }

    public boolean isFine()
    {
        return (m_logLevel.equals(LOG_LEVEL_FINE));
    }

    public boolean isFiner()
    {
        return (m_logLevel.equals(LOG_LEVEL_FINER));
    }

    public boolean isFinest()
    {
        return (m_logLevel.equals(LOG_LEVEL_FINEST));
    }

    public String toString()
    {
        return m_logLevel;
    }

    public void fromString(String str)
        throws EmanagerInventoryException
    {
        if ( str.equals(LOG_LEVEL_SEVERE) ||
             str.equals(LOG_LEVEL_WARNING) ||
             str.equals(LOG_LEVEL_INFO) ||
             str.equals(LOG_LEVEL_CONFIG) ||
             str.equals(LOG_LEVEL_FINE) ||
             str.equals(LOG_LEVEL_FINER) ||
             str.equals(LOG_LEVEL_FINEST) )
        {
            m_logLevel = str;
        }
        else
        {
            throw new EmanagerInventoryException(
                EmanagerInventoryStatusCode.InvalidLogLevel,
                "unrecognized log level: " + str);
        }
    }
}
