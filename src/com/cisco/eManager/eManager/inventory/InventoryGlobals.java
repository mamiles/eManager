package com.cisco.eManager.eManager.inventory;

public class InventoryGlobals
{
    // common instrumentation globals
    private static String MethodGetAppTypeName = "getAppType";
    public static String methodGetAppTypeName()
    {
        return MethodGetAppTypeName;
    }

    private static String MethodGetAppInstanceName = "getAppInstance";
    public static String methodGetAppInstanceName()
    {
        return MethodGetAppInstanceName;
    }

    private static String MethodGetPidName = "getPid";
    public static String methodGetPidName()
    {
        return MethodGetPidName;
    }

    private static String MethodGetLogLevelName = "getLogLevel";
    public static String methodGetLogLevelName()
    {
        return MethodGetLogLevelName;
    }

    private static String MethodSetLogLevelName = "setLogLevel";
    public static String methodSetLogLevelName()
    {
        return MethodSetLogLevelName;
    }

    // process sequencer globals
    private static String PsAppTypeName = "ProcessSequencer";
    public static String psAppTypeName()
    {
        return PsAppTypeName;
    }

    private static String PsAppInstanceName = "ProcessSequencer";
    public static String psAppInstanceName()
    {
        return PsAppInstanceName;
    }

    private static String PsInstrumentationName = "ProcessSequencer";
    public static String psInstrumentationName()
    {
        return PsInstrumentationName;
    }

    // - registration method globals
    private static String PsRegisterMethodName = "register";
    public static String psRegisterMethodName()
    {
        return PsRegisterMethodName;
    }

    private static String PsRegisterMethodAppTypeArgName = "appType";
    public static String psRegisterMethodAppTypeArgName()
    {
        return PsRegisterMethodAppTypeArgName;
    }

    private static String PsRegisterMethodAppInstanceArgName = "appInstance";
    public static String psRegisterMethodAppInstanceArgName()
    {
        return PsRegisterMethodAppInstanceArgName;
    }
    private static String PsRegisterMethodPropertiesFileArgName = "propertiesFile";
    public static String psRegisterMethodPropertiesFileArgName()
    {
        return PsRegisterMethodPropertiesFileArgName;
    }

    // - reregistration method globals
    private static String PsReRegisterMethodName = "reregister";
    public static String psReRegisterMethodName()
    {
        return PsReRegisterMethodName;
    }

    // - deregistration method globals
    private static String PsDeRegisterMethodName = "deregister";
    public static String psDeRegisterMethodName()
    {
        return PsDeRegisterMethodName;
    }

    // - solution registration method arguments
    private static String PsSolutionRegisterMethodName = "solutionRegister";
    public static String psSolutionRegisterMethodName()
    {
        return PsSolutionRegisterMethodName;
    }

    private static String PsSolutionRegisterArgNameParms = "parms";
    public static String psSolutionRegisterArgNameParms()
    {
        return PsSolutionRegisterArgNameParms;
    }

    // "system" globals
    private static String SysAppTypeName = "HostResources";
    public static String sysAppTypeName()
    {
        return SysAppTypeName;
    }

    private static String SysAppInstanceName = SysAppTypeName;
    public static String sysAppInstanceName()
    {
        return SysAppInstanceName;
    }

    private static String SysEventLogInstrumentationName =
        "COM.TIBCO.hawk.hma.EventLog";
    public static String sysEventLogInstrumentationName()
    {
        return SysEventLogInstrumentationName;
    }

    private static String SysFileStatInstrumentationName =
        "COM.TIBCO.hawk.hma.FileStat";
    public static String sysFileStatInstrumentationName()
    {
        return SysFileStatInstrumentationName;
    }

    private static String SysFileSysInstrumentationName =
        "COM.TIBCO.hawk.hma.FileSystem";
    public static String sysFileSysInstrumentationName()
    {
        return SysFileSysInstrumentationName;
    }

    private static String SysNetworkInstrumentationName =
        "COM.TIBCO.hawk.hma.Network";
    public static String sysNetworkInstrumentationName()
    {
        return SysNetworkInstrumentationName;
    }

    private static String SysPerformanceInstrumentationName =
        "COM.TIBCO.hawk.hma.Performance";
    public static String sysPerformanceInstrumentationName()
    {
        return SysPerformanceInstrumentationName;
    }

    private static String SysProcessInstrumentationName =
        "COM.TIBCO.hawk.hma.Process";
    public static String sysProcessInstrumentationName()
    {
        return SysProcessInstrumentationName;
    }

    private static String SysRegistryInstrumentationName =
        "COM.TIBCO.hawk.hma.Registry";
    public static String sysRegistryInstrumentationName()
    {
        return SysRegistryInstrumentationName;
    }

    private static String SysServicesInstrumentationName =
        "COM.TIBCO.hawk.hma.Services";
    public static String sysServicesInstrumentationName()
    {
        return SysServicesInstrumentationName;
    }

    private static String SysSystemInstrumentationName =
        "COM.TIBCO.hawk.hma.System";
    public static String sysSystemInstrumentationName()
    {
        return SysSystemInstrumentationName;
    }

    private static String SysTibRendezvousInstrumentationName =
        "COM.TIBCO.hawk.hma.TibRendezvous";
    public static String sysTibRendezvousInstrumentationName()
    {
        return SysTibRendezvousInstrumentationName;
    }

    private static String SysCustomInstrumentationName =
        "COM.TIBCO.hawk.microagent.Custom";
    public static String sysCustomInstrumentationName()
    {
        return SysCustomInstrumentationName;
    }

    private static String SysLogfileInstrumentationName =
        "COM.TIBCO.hawk.microagent.Logfile";
    public static String sysLogfileInstrumentationName()
    {
        return SysLogfileInstrumentationName;
    }

    private static String SysRuleBaseEngineInstrumentationName =
        "COM.TIBCO.hawk.microagent.RuleBaseEngine";
    public static String sysRuleBaseEngineInstrumentationName()
    {
        return SysRuleBaseEngineInstrumentationName;
    }

    private static String SysSelfInstrumentationName =
        "COM.TIBCO.hawk.microagent.Self";
    public static String sysSelfInstrumentationName()
    {
        return SysSelfInstrumentationName;
    }

    private static String SysSysInfoInstrumentationName =
        "COM.TIBCO.hawk.microagent.SysInfo";
    public static String sysSysInfoInstrumentationName()
    {
        return SysSysInfoInstrumentationName;
    }

    private static String SysLinux2xRulebaseName = "Linux2x";
    public static String sysLinux2xRulebaseName()
    {
        return SysLinux2xRulebaseName;
    }

    private static String SysSolaris2xRulebaseName = "Solaris2x";
    public static String sysSolaris2xRulebaseName()
    {
        return SysSolaris2xRulebaseName;
    }

    private static String SysWin2000RulebaseName = "Win2000";
    public static String sysWin2000RulebaseName()
    {
        return SysWin2000RulebaseName;
    }

    private static String SysWinXPRulebaseName = "WinXP";
    public static String sysWinXPRulebaseName()
    {
        return SysWinXPRulebaseName;
    }

    private static String DisplayInventoryDebuggerFrames = "DisplayInventoryDebuggerFrames";
    public static String displayInventoryDebuggerFrames()
    {
        return DisplayInventoryDebuggerFrames;
    }
}
