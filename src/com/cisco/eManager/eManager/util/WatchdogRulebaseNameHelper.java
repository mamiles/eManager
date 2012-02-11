package com.cisco.eManager.eManager.util;

import java.util.StringTokenizer;

public class WatchdogRulebaseNameHelper
{
    private static WatchdogRulebaseNameHelper wrnh = null;
    private static String TokenDelimeter = "_";
    private static String NameExtension = "WD";

    private WatchdogRulebaseNameHelper()
    {
    }

    public static synchronized WatchdogRulebaseNameHelper instance()
    {
        if (wrnh == null) {
            wrnh = new WatchdogRulebaseNameHelper();
        }

        return wrnh;
    }

    public static String generateWatchdogRulebaseName (String appTypeName,
						       String appInstanceName)
    {
	return appTypeName + TokenDelimeter + appInstanceName + TokenDelimeter + NameExtension;
    }

    public WatchdogRulebaseNameComponents parseWatchdogRulebaseName (String name)
    {
	String appTypeName;
	String appInstanceName;
	String nameExtension;
	StringTokenizer tokenizer;
	WatchdogRulebaseNameComponents components;

	if (name == null)
	    return null;

	components = new WatchdogRulebaseNameComponents();

	tokenizer = new StringTokenizer (name, TokenDelimeter);
	components.appTypeName = tokenizer.nextToken();

	if (tokenizer.hasMoreTokens() == false)
	    return null;

	components.appInstanceName = tokenizer.nextToken();
	if (tokenizer.hasMoreTokens() == false)
	    return null;

	nameExtension = tokenizer.nextToken();
	if (nameExtension.equals (NameExtension) == false)
	    return null;

	if (tokenizer.hasMoreTokens() == true)
	    return null;

	return components;
    }

    public boolean validWatchdogRulebaseName (String name)
    {
	if (parseWatchdogRulebaseName (name) != null)
	    return true;
	else
	    return false;
    }

    public class WatchdogRulebaseNameComponents
    {
	public String appTypeName;
	public String appInstanceName;
    }
}

