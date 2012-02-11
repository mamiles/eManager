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


import com.tibco.tibrv.*;


/**
 * A common message class from which other messages are derived.  This
 * message gives two standard fields, a version number and a
 * timestamp.
 **/
public class MsgCommon {
  /**
   * Name of the version field in a message.
   **/
  public static final String VERSION_FIELD = "ver";

  /**
   * Name of the timestamp field in a message.
   **/
  public static final String TIMESTAMP_FIELD = "time";

  /**
   * Version of this message.
   **/
  int mVersion;

  /**
   * Timestamp of this message.
   **/
  long mTimestamp;

  /**
   * Construct a new MsgCommon.
   *
   * @param version The version of this message.
   * @param timestamp The timestamp of this message.
   **/
  public MsgCommon (int version, long timestamp) {
    mVersion = version;
    mTimestamp = timestamp;
  }

  /**
   * Construct a new MsgCommon.  The timestamp is set to the current
   * time.
   *
   * @param version The version of this message.
   **/
  public MsgCommon (int version) {
    this (version, System.currentTimeMillis ());
  }

  /**
   * Construct a new MsgCommon.
   *
   * @param msg The message to construct it from.
   **/
  public MsgCommon (TibrvMsg msg) throws TibrvException {
    mVersion = getVersion (msg);
    mTimestamp = getTimestamp (msg);
  }

  /**
   * Convert this object to a TibrvMsg object.
   *
   * @return The TibrvMsg object.
   **/
  public TibrvMsg toTibrvMsg () throws TibrvException {
    return toTibrvMsg (mVersion, mTimestamp);
  }

  /**
   * Construct a new TibrvMsg object from individual fields.
   *
   * @param version The version number of the message.
   * @param timestamp The timestamp of the message.
   **/
  public static TibrvMsg toTibrvMsg (int version, long timestamp)
    throws TibrvException {

    TibrvMsg msg = new TibrvMsg ();
    msg.add (VERSION_FIELD, version);
    msg.add (TIMESTAMP_FIELD, timestamp);
    return msg;
  }

  /**
   * Construct a new TibrvMsg object from individual fields.  The
   * timestamp is set to the current time.
   *
   * @param version The version number of the message.
   **/
  public static TibrvMsg toTibrvMsg (int version)
    throws TibrvException {

    return toTibrvMsg (version, System.currentTimeMillis ());
  }

  /**
   * Get the version for this object.
   *
   * @return The version.
   **/
  public int getVersion () {
    return mVersion;
  }

  /**
   * Get the timestamp for this object.
   *
   * @return The timestamp.
   **/
  public long getTimestamp () {
    return mTimestamp;
  }

  /**
   * Get a version number from a message.
   *
   * @param msg The message.
   * @return The version number.
   **/
  public static int getVersion (TibrvMsg msg) throws TibrvException {
    return getIntField (msg, 0);
  }

  /**
   * Get a timestamp from a message.
   *
   * @param msg The message.
   * @return The timestamp.
   **/
  public static long getTimestamp (TibrvMsg msg) throws TibrvException {
    return getLongField (msg, 1);
  }

  /**
   * Extract an integer field from a message.
   *
   * @param msg The message.
   * @param index The field index.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static int getIntField (TibrvMsg msg, int index)
    throws TibrvException {

    TibrvMsgField field = msg.getFieldByIndex (index);
    return ((Integer) field.data).intValue ();
  }

  /**
   * Extract an integer field from a message.
   *
   * @param msg The message.
   * @param name The field name.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static int getIntField (TibrvMsg msg, String name)
    throws TibrvException {

    TibrvMsgField field = msg.getField (name);
    return ((Integer) field.data).intValue ();
  }

  /**
   * Extract an integer array field from a message.
   *
   * @param msg The message
   * @param name The field name.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static int[] getIntArrayField (TibrvMsg msg, String name)
    throws TibrvException {

    TibrvMsgField field = msg.getField (name);
    return (int[]) field.data;
  }

  /**
   * Extract a long field from a message.
   *
   * @param msg The message.
   * @param index The field index.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static long getLongField (TibrvMsg msg, int index)
    throws TibrvException {

    TibrvMsgField field = msg.getFieldByIndex (index);
    return ((Long) field.data).longValue ();
  }

  /**
   * Extract a long field from a message.
   *
   * @param msg The message.
   * @param name The field name.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static long getLongField (TibrvMsg msg, String name)
    throws TibrvException {

    TibrvMsgField field = msg.getField (name);
    return ((Long) field.data).longValue ();
  }

  /**
   * Extract a float field from a message.
   *
   * @param msg The message.
   * @param index The field index.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static float getFloatField (TibrvMsg msg, int index)
    throws TibrvException {

    TibrvMsgField field = msg.getFieldByIndex (index);
    return ((Float) field.data).floatValue ();
  }

  /**
   * Extract a float field from a message.
   *
   * @param msg The message.
   * @param name The field name.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static float getFloatField (TibrvMsg msg, String name)
    throws TibrvException {

    TibrvMsgField field = msg.getField (name);
    return ((Float) field.data).floatValue ();
  }

  /**
   * Extract a double field from a message.
   *
   * @param msg The message.
   * @param index The field index.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static double getDoubleField (TibrvMsg msg, int index)
    throws TibrvException {

    TibrvMsgField field = msg.getFieldByIndex (index);
    return ((Double) field.data).doubleValue ();
  }

  /**
   * Extract a double field from a message.
   *
   * @param msg The message.
   * @param name The field name.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static double getDoubleField (TibrvMsg msg, String name)
    throws TibrvException {

    TibrvMsgField field = msg.getField (name);
    return ((Double) field.data).doubleValue ();
  }

  /**
   * Extract a String field from a message.
   *
   * @param msg The message.
   * @param index The field index.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static String getStringField (TibrvMsg msg, int index)
    throws TibrvException {

    TibrvMsgField field = msg.getFieldByIndex (index);
    return (String) field.data;
  }

  /**
   * Extract a String field from a message.
   *
   * @param msg The message.
   * @param name The field name.
   * @return The field value.
   * @exception TibrvException if the field doesn't exist or is not of
   *   the specified type.
   **/
  public static String getStringField (TibrvMsg msg, String name)
    throws TibrvException {

    TibrvMsgField field = msg.getField (name);
    return (String) field.data;
  }
}
