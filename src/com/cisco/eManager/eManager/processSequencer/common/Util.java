package com.cisco.eManager.eManager.processSequencer.common;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Title: Process Sequencer / Watchdog</p>
 * <p>Description: eManger</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Marvin Miles
 * @version 1.0
 */

public class Util {
  /**
   * Reads the stream fully and stores the text in a string.
   *
   * @param is  a input stream
   * @return    a string containing the text read from the stream
   */
  public static String streamToString(InputStream is) throws IOException {
    return readerToString(new InputStreamReader(is));
  }

  /**
   * Reads the stream fully and stores the text in a string.
   *
   * @param r  a reader
   * @return   a string containing the text read from the reader
   */
  public static String readerToString(Reader r) throws IOException {
    StringBuffer sb = new StringBuffer(1000);
    char[] buf = new char[1000];
    int l = 0;
    while ( (l = r.read(buf)) > 0) {
      sb.append(buf, 0, l);

    }
    try {
      r.close();
    }
    catch (IOException ioe) {}
    return sb.toString();

  }

  /**
   * Converts an array of string to a comma separated string.
   *
   * @param array  the array of strings.
   * @return       the comma separated string.
   */
  public static String stringArrayToString(String[] array) {
    if (array == null) {
      return null;
    }

    StringBuffer buf = new StringBuffer(128);
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        buf.append(", ");
      }
      buf.append(array[i]);
    }
    return buf.toString();
  }

  /**
   * Converts a collection to a comma separated string.
   *
   * @param coll   the collection.
   * @return       the comma separated string.
   */
  public static String collectionToString(Collection coll) {
    if (coll == null) {
      return null;
    }

    StringBuffer buf = new StringBuffer(128);
    Iterator i = coll.iterator();
    while (i.hasNext()) {
      if (buf.length() > 0) {
        buf.append(", ");
      }
      buf.append(i.next());
    }
    return buf.toString();
  }

  public static String stackTraceToString(Throwable t) {
    StringWriter sw = new StringWriter(2000);
    PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);

    Throwable cause = t.getCause();
    while (cause != null) {
      pw.println("\nNested Exception: " + cause.getMessage());
      cause.printStackTrace(pw);

      cause = cause.getCause();
    }

    pw.close();
    return sw.toString();
  }

  /**
   * Copies the contents of a map into an array. The array must be at least the
   * size of the collection. The elements of the collection must be assignment
   * compatible with the element type of the array.
   *
   * @param c  the source collection.
   * @param a  the destinatin array.
   */
  public static void collectionToArray(Collection c, Object[] a) {
    Iterator iter = c.iterator();
    int i = 0;
    while (iter.hasNext()) {
      a[i++] = iter.next();
    }
  }

  /**
   * Copies the contents of an array into a Collection.
   *
   * @param a  the source array.
   * @param c  the destination collection.
   */
  public static void arrayToCollection(Object[] a, Collection c) {
    if (a == null) {
      return;
    }
    for (int i = 0; i < a.length; i++) {
      c.add(a[i]);
    }
  }

  public static void listThreadInfo() {
    ThreadGroup tg = Thread.currentThread().getThreadGroup();
    while (tg.getParent() != null) {
      tg = tg.getParent();
    }
    tg.list();
  }

  public static void listThreadInfo2() {
    ThreadGroup tg = Thread.currentThread().getThreadGroup();
    while (tg.getParent() != null) {
      tg = tg.getParent();
    }
    Thread[] threads = new Thread[tg.activeCount()];
    tg.enumerate(threads);
    for (int i = 0; i < threads.length; i++) {
      if (threads[i] != null) {
        System.out.println(threads[i].getName() + ": " +
                           threads[i].getClass().getName() + ", " +
                           threads[i].getThreadGroup().getName() +
                           (threads[i].isDaemon() ? ", daemon" : ""));

      }
    }
  }

  /**
   * Builds a map of (key,value) pairs from a string in the following format:
   * <b><code>key1=value1;key2=value2...</code>
   *
   * @param s  the source string
   * @return   the map containing (key,value) pairs
   */
  public static Map stringToProperties(String s) {
    int start = 0;
    int equal = 0;
    int end = -1;

    String key;
    String value;

    Map result = new HashMap();
    if (s == null) {
      return result;
    }

    do {
      start = end + 1;
      equal = s.indexOf("=", start);
      if (equal != -1) {
        end = s.indexOf(";", equal);
        if (end == -1) {
          end = s.length();
        }
        key = s.substring(start, equal);
        value = s.substring(equal + 1, end);
        result.put(key, value);
      }
    }
    while (equal != -1);
    return result;
  }

  public static void testStringToProperties() {
    String[] strings = {
        "",
        "key=value",
        "k1=v1;k2=v2",
        "k1=v1;k2=v2;"};

    for (int i = 0; i < strings.length; i++) {
      System.out.println(strings[i]);
      Map m = stringToProperties(strings[i]);
      Iterator iter = m.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry) iter.next();
        System.out.println("\t" + entry.getKey() + "=" + entry.getValue());
      }
    }
  }

  public static void main(String[] args) {
    try {
      String methodName = args[0];
      java.lang.reflect.Method method = Util.class.getMethod(methodName, null);
      method.invoke(null, null);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
