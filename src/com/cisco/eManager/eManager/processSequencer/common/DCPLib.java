package com.cisco.eManager.eManager.processSequencer.common;
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

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import com.tibco.tibrv.*;

import com.cisco.eManager.eManager.processSequencer.common.ConfigStoreException;
import com.cisco.eManager.eManager.processSequencer.common.ConfigStore;
import com.cisco.eManager.eManager.processSequencer.common.ConfigInitListener;

import com.cisco.eManager.eManager.processSequencer.common.PSRuntimeException;
import com.cisco.eManager.eManager.processSequencer.common.PropertiesConstants;
import com.cisco.eManager.eManager.processSequencer.common.EventUtils;

/**
 * DCPLib provides its users access to the configuration information of
 * the system. It keeps track of the changes made to the configuration data
 * at runtime and can notify the listeners about such changes.
 * <p>
 *
 * In the very simplistic use, the users can just query the underlying
 * configuration information using various getXXX() methods.
 *
 * <p>
 * One of the possible ways of using DCPLib is :
 * <p>
 * <code>
 *  String namesHost = DCPLib.getProperty("DistributionFramework.NamingHost", null)
 * </code>
 * <p>
 * Here, if the config data has the property DistributionFramework.NamingHost, it
 * is retrieved and returned. If it is not present, null is returned  (default value).
 *
 * <p>
 * Another way of working with DCPLib would be :
 * <p>
 * <code>
 * String nameHost = null;
 * try {
 *     namesHost = DCPLib.getProperty("DistributionFramework.NamingHost");
 * } catch (NoSuchProperty nsp) {
 *    //set up default here??? or throw this nback if this is a fatal case...
 * }
 * if(namesHost != null) { ..... }
 * </code>
 *
 */

public final class DCPLib implements TibrvMsgCallback, ConfigInitListener
{
        static final String CHANGE_MSG_TAG = "DCPChange.";
        static final String ALL_PROPERTIES = "--VER_CHANGE--*";

        private static final String DELIMS = " ,\t\n";

        private static DCPLib msInstance;
        private DCPEventChannel mEventChannel;
        private Properties mBootProperties;
        private Properties mProperties;
        private Map mComponentCallbacks;

        private int mInitState;
        private Object mInitStateLock = new Object();
        private Object mEvcLock = new Object();

        private static final int START = 0;
        private static final int PARTIAL = 1;
        private static final int COMPLETE = 2;

        private static String removeChangeMsgTag(String propertyPath) {
                if( propertyPath == null) return propertyPath;
                if( propertyPath.startsWith(CHANGE_MSG_TAG)) {
                        return propertyPath.substring(CHANGE_MSG_TAG.length());
                }
                return propertyPath;
        }

        static String removeSuffix(String propertyPath) {
                int lastSeparator = propertyPath.lastIndexOf('.');
                if (lastSeparator == -1) return "SYSTEM";
                return propertyPath.substring(0, lastSeparator);
        }

        static String getSuffix(String propertyPath) {
                int lastSeperator = propertyPath.lastIndexOf('.');
                if (lastSeperator == -1) return propertyPath;
                return propertyPath.substring(lastSeperator+1);
        }

        /**
         * Builds (if necessary) and returns the DCPLib object.
         * @return instance of DCPLib
         * @throws EventChannelException if the communication with Tibco cannot be setup
         */
        private static DCPLib getInstance() {
                if( msInstance == null) {
                        return _getInstance();
                }

                /****
                if ( msInstance.getStateOfInitialization() != COMPLETE ) {
                        synchronized (msInstance.mInitLock) {
                                try {
                                        msInstance.mInitStateLock.wait(3000);
                                } catch (Exception ex){}
                        }
                }
                ****/
                return msInstance;
        }

        private synchronized static DCPLib _getInstance()
        {
                if(msInstance == null) {
                        try {
                                Properties bootProps = ConfigStore.readBootConfig();

                                msInstance = new DCPLib(bootProps);

                                ConfigStore configStore = ConfigStore.getInstance();

                                if( ! configStore.isInited()) {
                                        configStore.addConfigInitListener(msInstance);
                                } else {
                                        try {
                                                msInstance.setupMainConfig(configStore.readConfigForHost(null), false);
                                        } catch (EventChannelException ece) {
                                                throw new PSRuntimeException("Cannot instantiate DCPLib", ece);
                                        }
                                }
                        } catch (ConfigStoreException ex) {
                                throw new PSRuntimeException("Cannot read boot configuration", ex);
                        }
                }
                return msInstance;
        }

        private DCPLib(Properties bootProperties){
                mBootProperties = bootProperties;
                mComponentCallbacks = Collections.synchronizedMap(new HashMap());
                setStateOfInitialization(START);
        }

        private void setStateOfInitialization(int state) {
                synchronized( mInitStateLock ) {
                        mInitState = state;
                        if( mInitState == COMPLETE) {
                                mInitStateLock.notifyAll();
                        }
                }
        }

        private int getStateOfInitialization() {
                synchronized ( mInitStateLock ) {
                        return mInitState;
                }
        }

        private void setupMainConfig(Properties props, boolean reInit)
                throws EventChannelException
        {
                mProperties = props;
                setStateOfInitialization(PARTIAL);

                if (! reInit ) {
                        Thread t = new Thread() {
                                public void run() {
                                        createEventChannel();
                                        setStateOfInitialization(COMPLETE);
                                }
                        };
                        t.start();
                }
        }

        private void createEventChannel() {
                synchronized ( mEvcLock ) {
                        if( mEventChannel != null) return;

                        String tibcoPort = retrieveTibcoPropertyFromConfig("port");

                        try {
                                if( tibcoPort != null) {
                                        String network = retrieveTibcoPropertyFromConfig("network");
                                        String daemon = retrieveTibcoPropertyFromConfig("daemon");
                                        mEventChannel = new DCPEventChannel(tibcoPort, network, daemon);
                                        mEventChannel.getComponentEvents(null, DCPLib.this);
                                }
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }
                }
        }

        public void configInited(Properties props) {
                if( props == null) {
                        throw new PSRuntimeException("Null properties from config store.");
                }
                try {
                        setupMainConfig(props, false);
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
        }

        private String retrieveTibcoPropertyFromConfig(String property) {
                String tibcoProperty = PropertiesConstants.SYSTEM_TIBCO + "." + property;

                String propValue =
                                (mBootProperties == null) ?
                                                null : (String) mBootProperties.getProperty(tibcoProperty);

                if(propValue == null) {
                        if( mProperties != null) {
                                propValue = mProperties.getProperty(tibcoProperty);
                        }
                }
                return propValue;
        }

        private void handlePropertiesVersionChange()
                throws Exception
        {
                //System.out.println("About to handle version change");
                Properties newProps = ConfigStore.getInstance().readConfigForHost(null);
                setupMainConfig(newProps, true);
                //System.out.println("Handled version change");
        }

        /**
         * Implementation of the onMsg() method in the TibrvMessageCallback interface.
         * The changes made to the configuration file are sent out as TIBCO events (by the
         * client -- UI). Those changes are received and processed here.
         */
        public void onMsg( TibrvListener listener, TibrvMsg msg )
        {
                try {
                        TibrvMsgField hostField = msg.getField("host");

                        if( HostMsgField.isLocalHostTargetted(hostField))
                        {
                                String subject = msg.getSendSubject();
                                subject = EventUtils.removeSubjectPrefix(subject);

                                // Remove CHANGE_MSG_TAG to get componentPath
                                String componentPath = removeChangeMsgTag(subject);

                                if( componentPath.equals(ALL_PROPERTIES)) {
                                        handlePropertiesVersionChange();
                                        return;
                                }


                                TibrvMsgField propertyField = msg.getField("property");
                                TibrvMsgField valueField = msg.getField("value");

                                String property = (String) propertyField.data;
                                String propertyPath = componentPath + "." + property;
                                String value = (String)valueField.data;

                                mProperties.put(propertyPath, value);

                                String componentStr = componentPath;
                                while ( componentStr != null) {
                                        Vector cbs = (Vector) mComponentCallbacks.get(componentStr);
                                        if( cbs != null ) {
                                                synchronized (cbs) {
                                                        int cbLen = cbs.size();
                                                        for(int i=0; i < cbLen; ++i) {
                                                                DCPCallback cb = (DCPCallback) cbs.elementAt(i);
                                                                cb.handleChange(propertyPath, value);
                                                        }
                                                }
                                        }
                                        int index = componentStr.lastIndexOf('.');
                                        if( index == -1) componentStr = null;
                                        else componentStr = componentStr.substring(0, index);
                                }
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }

        /**
         * @deprecated This method serves no purpose and will be removed.
         * @see #registerComponent(String, DCPCallback)
         */
        public static void registerComponent(String componentPath)
                throws EventChannelException
        {
        }

        /**
         * Components that are interested in receiving the confgiuration
         * changes need to call this method. A default component is created for this
         * the events are stored there, which can be queried on demand
         * using the full path
         *
         * @param componentPath a full dot separated name of the component
         * @param callback the callback implementation that has to be notified in case of a configuration change.
         * throws EventChannelException
         */

        public static void registerComponent(String componentPath, DCPCallback callback)
                throws EventChannelException
        {
                DCPLib.getInstance()._registerComponent(componentPath, callback);
        }

        public static void unregisterComponent(String componentPath, DCPCallback callback)
                throws EventChannelException
        {
                DCPLib.getInstance()._unregisterComponent(componentPath, callback);
        }


        private synchronized void _registerComponent(String componentPath, DCPCallback callback)
                throws EventChannelException
        {
                if( componentPath == null) {
                        return;
                }

                Vector v = (Vector) mComponentCallbacks.get(componentPath);
                if( v != null) {
                        v.add(callback);
                } else {
                        v = new Vector();
                        mComponentCallbacks.put(componentPath, v);
                        v.add(callback);
                }
        }

        private synchronized void _unregisterComponent(String componentPath, DCPCallback callback)
                throws EventChannelException
        {
                if( componentPath == null) {
                        return;
                }

                Vector v = (Vector) mComponentCallbacks.get(componentPath);
                if( v != null) {
                        v.remove(callback);
                        if ( v.size() == 0) mComponentCallbacks.remove(componentPath);
                }
        }

        /**
         * Retrieves all the properties of a component.
         * @param componentPath full path of the component
         * @param recurse if true, retrieves the properties of the sub components as well.
         * @return the Map object with the properties for the given component
         */
        public static Map getPropertyMap(String componentPath, boolean recurse)
        {
                try {
                        return DCPLib.getInstance()._getPropertyMap(componentPath, recurse);
                } catch (EventChannelException ex) {
                        return null;
                }
        }

        private synchronized Map _getPropertyMap(String componentPath, boolean recurse)
        {
                if ( getStateOfInitialization() <= START || mProperties == null) {
                        return null;
                }

                if( componentPath == null ) return null;

                Map map = new TreeMap();

                Iterator entries = mProperties.entrySet().iterator();

                if( ! componentPath.endsWith(".")) {
                        componentPath = componentPath + ".";
                }

                int cLen = componentPath.length();

                while( entries.hasNext()) {
                        Entry e = (Entry) entries.next();
                        String key = (String) e.getKey();
                        if( recurse ) {
                                if( key.startsWith(componentPath)) {
                                        map.put(key, e.getValue());
                                }
                        } else {
                                if( key.startsWith(componentPath) &&
                                                key.lastIndexOf('.') == (cLen -1))
                                {
                                        map.put(key, e.getValue());
                                }
                        }
                }

                return map;
        }

        /**
         * Retrieves the given property. Tokenizes the property value on <code> space, tab, comma and newline </code>
         * and returns the resultant array of properties.
         * @param propertyPath full path of the property
         * @return an array containing the tokens in the original property value
         * @throws NoSuchProperty runtime exception if the property is not found
         * @see #getProperties(String, String)
         * @see #getProperties(String, String[])
         * @see #getProperties(String, String[], String)
         */

        public static String[] getProperties(String propertyPath) {
                return DCPLib.getInstance()._getProperties(propertyPath);
        }

        private String[] _getProperties(String propertyPath) {
                String str = _getProperty(propertyPath);
                return tokenize(str, DELIMS);
        }

        /**
         * Retrieves the given property. Tokenizes the property value on the given delimiter set
         * and returns the resultant array of properties.
         * @param propertyPath full path of the property
         * @param delimiters the string containing the delimiters on which the property value should be tokenized
         * @return an array containing the tokens in the original property value
         * @throws NoSuchProperty runtime exception if the property is not found
         * @see #getProperties(String)
         * @see #getProperties(String, String[])
         * @see #getProperties(String, String[], String)
         */
        public static String[] getProperties(String propertyPath, String delimiters) {
                return DCPLib.getInstance()._getProperties(propertyPath, delimiters);
        }

        private String[] _getProperties(String propertyPath, String delimiters) {
                String str = _getProperty(propertyPath);
                return tokenize(str, delimiters);
        }

        /**
         * Retrieves the given property. Tokenizes the property value on <code> space, tab, comma and newline </code>
         * and returns the resultant array of properties. If the property is not found, returns the default
         * array passed in as a parameter. As opposed to the getProperties() methods that do not expect the
         * default values array, this method does not throw the NoSuchProperty exception if the value is not found.
         * @param propertyPath full path of the property
         * @param defValue an array with the default values to be returned, if the property is not found.
         * @return an array containing the tokens in the original property value or the default if the property is not found.
         * @see #getProperties(String)
         * @see #getProperties(String, String)
         * @see #getProperties(String, String[], String)
         */
        public static String[] getProperties(String propertyPath, String[] defValue) {
                return DCPLib.getInstance()._getProperties(propertyPath, defValue);
        }

        private String[] _getProperties(String componentPath, String[] defValue) {
                String str = _getProperty(componentPath, null);
                if(str == null) return defValue;
                return tokenize(str, DELIMS);
        }

        /**
         * Retrieves the given property. Tokenizes the property value on the given delimiter set
         * and returns the resultant array of properties. If the property is not found, returns the default
         * array passed in as a parameter. As opposed to the getProperties() methods that do not expect the
         * default values array, this method does not throw the NoSuchProperty exception if the value is not found.
         * @param propertyPath full path of the property
         * @param  defValue an array with the default values to be returned, if the property is not found.
         * @param delimiters the string containing the delimiters on which the property value should be tokenized
         * @return an array containing the tokens in the original property value or the default if the property is not found.
         * @see #getProperties(String)
         * @see #getProperties(String, String)
         * @see #getProperties(String, String[])
         */
        public static String[] getProperties(String componentPath, String[] defValue, String delimiters) {
                return DCPLib.getInstance()._getProperties(componentPath, defValue, delimiters);
        }

        private String[] _getProperties(String componentPath, String[] defValue, String delimiters) {
                String str = _getProperty(componentPath, null);
                if(str == null) return defValue;
                return tokenize(str, delimiters);
        }

        private String[] tokenize(String str, String delim) {
                if(str == null || str.trim().equals("")) return null;

                StringTokenizer toks = new StringTokenizer(str, delim);
                String[] arr = new String[toks.countTokens()];
                int i = 0;
                while(toks.hasMoreTokens()) {
                        arr[i++] = toks.nextToken();
                }
                return arr;
        }

        /**
         * Retrieves the property value for the given propertyPath.
         * Throws NoSuchProperty exception if the value is not found.
         * @param propertyPath full path of the property
         * @return the property value
         * @throws NoSuchPropertyException if the property is not found.
         * @see #getProperty(String, String)
         *
         */

        public static String getProperty(String propertyPath) {
                return DCPLib.getInstance()._getProperty(propertyPath);
        }

        /**
         * Retrieves the property value for the given propertyPath. If the property is not found this method
         * does not throw NoSuchProperty exception. Instead, it returns the default
         * value passed in as a parameter.
         * @param propertyPath full path of the property
         * @param defaultValue the defaultValue to be returned if the property is not found.
         * @return the property value or the default value (if the property is not found)
         * @see #getProperty(String)
         */
        public static String getProperty(String propertyPath, String defaultValue) {
                return DCPLib.getInstance()._getProperty(propertyPath, defaultValue);
        }

        private String _getProperty(String propertyPath, String defaultValue) {
                try {
                        return _getProperty(propertyPath);
                } catch (NoSuchProperty nsp) {
                        return defaultValue;
                }
        }


        private String _getProperty(String propertyPath) {

                if( propertyPath == null) throw new NoSuchProperty("<null>");

                if(mBootProperties != null) {
                        String str = mBootProperties.getProperty(propertyPath);
                        if(str != null) {
                                return str;
                        }
                }

                if ( getStateOfInitialization() <= START || mProperties == null) {
                        throw new NoSuchProperty(propertyPath);
                }

                String value = mProperties.getProperty(propertyPath);

                if( value == null) {
                        throw new NoSuchProperty(propertyPath);
                }

                return value;
        }

        /**
         * Retrieves the boolean property value for the given propertyPath. If the property is not
         * found this method throws NoSuchProperty exception.
         * @param propertyPath full path of the property
         * @return the property value converted to boolean
         * @see #getBooleanProperty(String, boolean)
         */
        public static boolean getBooleanProperty(String propertyPath) {
                return DCPLib.getInstance()._getBooleanProperty(propertyPath);
        }

        /**
         * Retrieves the boolean property value for the given propertyPath. If the property is not
         * found this method does not throw NoSuchProperty exception. Instead, it returns the default
         * value passed in as a parameter.
         * @param propertyPath full path of the property
         * @param defaultValue the defaultValue to be returned if the property is not found.
         * @return the property value or the default value (if the property is not found)
         * @see #getBooleanProperty(String)
         */

        public static boolean getBooleanProperty(String propertyPath, boolean defaultValue) {
                return DCPLib.getInstance()._getBooleanProperty(propertyPath, defaultValue);
        }

        private boolean _getBooleanProperty(String propertyPath, boolean defaultValue)
        {
                try {
                        return _getBooleanProperty(propertyPath);
                } catch (NoSuchProperty nsp) {
                        return defaultValue;
                }
        }

        private boolean _getBooleanProperty(String propertyPath) {
                String str = _getProperty(propertyPath);
                try {
                        return Boolean.valueOf(str).booleanValue();
                } catch ( Exception ex) {
                        ex.printStackTrace();
                        throw new NoSuchProperty(propertyPath, "Incorrect type");
                }
        }

        /**
         * Retrieves the integer property value for the given propertyPath. If the property is not
         * found this method throws NoSuchProperty exception.
         * @param propertyPath full path of the property
         * @return the property value converted to boolean
         * @see #getIntProperty(String, int)
         */
        public static int getIntProperty(String propertyPath) {
                return DCPLib.getInstance()._getIntProperty(propertyPath);
        }

        /**
         * Retrieves the integer property value for the given propertyPath. If the property is not
         * found this method does not throw NoSuchProperty exception. Instead, it returns the default
         * value passed in as a parameter.
         * @param propertyPath full path of the property
         * @param defaultValue the defaultValue to be returned if the property is not found.
         * @return the property value or the default value (if the property is not found)
         * @see #getIntProperty(String)
         */


        public static int getIntProperty(String propertyPath, int defaultValue) {
                return DCPLib.getInstance()._getIntProperty(propertyPath, defaultValue);
        }

        private int _getIntProperty(String propertyPath, int defaultValue)
        {
                try {
                        return _getIntProperty(propertyPath);
                } catch (NoSuchProperty nsp) {
                        return defaultValue;
                }
        }

        private int _getIntProperty(String propertyPath)
        {
                String str = _getProperty(propertyPath);
                try {
                        return Integer.valueOf(str).intValue();
                } catch ( Exception ex) {
                        throw new NoSuchProperty(propertyPath, "Incorrect type");
                }
        }

        /**
         *  Same as getProperty(String propertyPath, String defaultValue), except that
         *  the propertyPath is prefixed with "SYSTEM."
         *  @see #getProperty(String, String)
         */

        public static String getSystemProperty(String propertyPath, String defaultValue)
        {
                if( propertyPath == null) {
                        return defaultValue;
                }
                return DCPLib.getInstance()._getSystemProperty(propertyPath, defaultValue);
        }

        private String _getSystemProperty(String propertyPath, String defaultValue)
        {
                return _getProperty("SYSTEM." + propertyPath, defaultValue);
        }

        /**
         *  Same as getProperty(String propertyPath, String defaultValue), except that
         *  the propertyPath is prefixed with "SYSTEM."
         *  @see #getProperty(String, String)
         */
        public static String getSystemProperty(String propertyPath)
        {
                if( propertyPath == null) {
                        throw new NoSuchProperty("<null>");
                }
                return DCPLib.getInstance()._getSystemProperty(propertyPath);
        }

        private String _getSystemProperty(String propertyPath)
        {
                return _getProperty("SYSTEM." + propertyPath);
        }

        /**
         *  Same as getIntProperty(String propertyPath, String defaultValue), except that
         *  the propertyPath is prefixed with "SYSTEM."
         *  @see #getIntProperty(String, int)
         */
        public static int getIntSystemProperty(String propertyPath, int defValue)
        {
                if( propertyPath == null) {
                        return defValue;
                }
                return DCPLib.getInstance()._getIntSystemProperty(propertyPath, defValue);
        }

        private int _getIntSystemProperty(String propertyPath, int defaultValue)
        {
                return _getIntProperty("SYSTEM." + propertyPath, defaultValue);
        }

        /**
         *  Same as getIntProperty(String propertyPath), except that
         *  the propertyPath is prefixed with "SYSTEM."
         *  @see #getIntProperty(String)
         */
        public static int getIntSystemProperty(String propertyPath)
        {
                if( propertyPath == null) {
                        throw new NoSuchProperty("<null>");
                }
                return DCPLib.getInstance()._getIntSystemProperty(propertyPath);
        }

        private int _getIntSystemProperty(String propertyPath)
        {
                return _getIntProperty("SYSTEM." + propertyPath);
        }

        /**
         *  Same as getBooleanProperty(String propertyPath, boolean defaultValue), except that
         *  the propertyPath is prefixed with "SYSTEM."
         *  @see #getBooleanProperty(String, boolean)
         */

        public static boolean getBooleanSystemProperty(String propertyPath, boolean defaultValue)
        {
                if( propertyPath == null) {
                        return defaultValue;
                }
                return DCPLib.getInstance()._getBooleanSystemProperty(propertyPath, defaultValue);
        }

        private boolean _getBooleanSystemProperty(String propertyPath, boolean defaultValue)
        {
                return _getBooleanProperty("SYSTEM." + propertyPath, defaultValue);
        }

        /**
         *  Same as getBooleanProperty(String propertyPath), except that
         *  the propertyPath is prefixed with "SYSTEM."
         *  @see #getBooleanProperty(String)
         */
        public static boolean getBooleanSystemProperty(String propertyPath)
        {
                if( propertyPath == null) {
                        throw new NoSuchProperty("<null>");
                }
                return DCPLib.getInstance()._getBooleanSystemProperty(propertyPath);
        }

        private boolean _getBooleanSystemProperty(String propertyPath)
        {
                return _getBooleanProperty("SYSTEM." + propertyPath);
        }
}


