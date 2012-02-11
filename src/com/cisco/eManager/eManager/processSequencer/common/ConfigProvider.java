/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2001 by Cisco Systems, Inc.
 * All rights reserved.
 *
 *********************************************************************/
package com.cisco.eManager.eManager.processSequencer.common;

import java.util.Map;

/**
 * ConfigProvider provides its users access to the configuration information of
 * the system.
 */

public interface ConfigProvider
{
	/**
	 * Retrieves all the properties of a component.
	 * @param componentPath full path of the component
	 * @param recurse if true, retrieves the properties of the sub components as well.
	 * @return the Map object with the properties for the given component
	 */
	public Map getPropertyMap(String componentPath, boolean recurse);

	/**
	 * Retrieves the given property. Tokenizes the property value on <code> space, tab, comma and newline </code>
	 * and returns the resultant array of properties.
	 * @param propertyPath full path of the property
	 * @return an array containing the tokens in the original property value
	 * @throws ConfigException runtime exception if the property is not found
	 * @see #getProperties(String, String)
	 * @see #getProperties(String, String[])
	 * @see #getProperties(String, String[], String)
	 */

	public String[] getProperties(String propertyPath);

	/**
	 * Retrieves the given property. Tokenizes the property value on the given delimiter set
	 * and returns the resultant array of properties.
	 * @param propertyPath full path of the property
	 * @param delimiters the string containing the delimiters on which the property value should be tokenized
	 * @return an array containing the tokens in the original property value
	 * @throws ConfigException runtime exception if the property is not found
	 * @see #getProperties(String)
	 * @see #getProperties(String, String[])
	 * @see #getProperties(String, String[], String)
	 */
	public String[] getProperties(String propertyPath, String delimiters);

	/**
	 * Retrieves the given property. Tokenizes the property value on <code> space, tab, comma and newline </code>
	 * and returns the resultant array of properties. If the property is not found, returns the default
	 * array passed in as a parameter. As opposed to the getProperties() methods that do not expect the
	 * default values array, this method does not throw the ConfigException exception if the value is not found.
	 * @param propertyPath full path of the property
	 * @param defValue an array with the default values to be returned, if the property is not found.
	 * @return an array containing the tokens in the original property value or the default if the property is not found.
	 * @see #getProperties(String)
	 * @see #getProperties(String, String)
	 * @see #getProperties(String, String[], String)
	 */
	public String[] getProperties(String propertyPath, String[] defValue);

	/**
	 * Retrieves the given property. Tokenizes the property value on the given delimiter set
	 * and returns the resultant array of properties. If the property is not found, returns the default
	 * array passed in as a parameter. As opposed to the getProperties() methods that do not expect the
	 * default values array, this method does not throw the ConfigException exception if the value is not found.
	 * @param propertyPath full path of the property
	 * @param  defValue an array with the default values to be returned, if the property is not found.
	 * @param delimiters the string containing the delimiters on which the property value should be tokenized
	 * @return an array containing the tokens in the original property value or the default if the property is not found.
	 * @see #getProperties(String)
	 * @see #getProperties(String, String)
	 * @see #getProperties(String, String[])
	 */
	public String[] getProperties(String componentPath, String[] defValue, String delimiters);

	/**
	 * Retrieves the property value for the given propertyPath.
	 * @see #getProperty(String, String)
	 */
	public String getProperty(String propertyPath);

	/**
	 * Retrieves the property value for the given propertyPath. If the property is not found this method
	 * does not throw ConfigException exception. Instead, it returns the default
	 * value passed in as a parameter.
	 * @param propertyPath full path of the property
	 * @param defaultValue the defaultValue to be returned if the property is not found.
	 * @return the property value or the default value (if the property is not found)
	 * @see #getProperty(String)
	 */
	public String getProperty(String propertyPath, String defaultValue);


	/**
	 * Retrieves the boolean property value for the given propertyPath. If the property is not
	 * found this method throws ConfigException exception.
	 * @param propertyPath full path of the property
	 * @return the property value converted to boolean
	 * @see #getBooleanProperty(String, boolean)
	 */
	public boolean getBooleanProperty(String propertyPath);

	/**
	 * Retrieves the boolean property value for the given propertyPath. If the property is not
	 * found this method does not throw ConfigException exception. Instead, it returns the default
	 * value passed in as a parameter.
	 * @param propertyPath full path of the property
	 * @param defaultValue the defaultValue to be returned if the property is not found.
	 * @return the property value or the default value (if the property is not found)
	 * @see #getBooleanProperty(String)
	 */

	public boolean getBooleanProperty(String propertyPath, boolean defaultValue);

	/**
	 * Retrieves the integer property value for the given propertyPath. If the property is not
	 * found this method throws ConfigException exception.
	 * @param propertyPath full path of the property
	 * @return the property value converted to boolean
	 * @see #getIntProperty(String, int)
	 */
	public int getIntProperty(String propertyPath);

	/**
	 * Retrieves the integer property value for the given propertyPath. If the property is not
	 * found this method does not throw ConfigException exception. Instead, it returns the default
	 * value passed in as a parameter.
	 * @param propertyPath full path of the property
	 * @param defaultValue the defaultValue to be returned if the property is not found.
	 * @return the property value or the default value (if the property is not found)
	 * @see #getIntProperty(String)
	 */
	public int getIntProperty(String propertyPath, int defaultValue);

}

