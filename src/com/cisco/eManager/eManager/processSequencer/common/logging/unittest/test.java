package com.cisco.eManager.eManager.processSequencer.common.logging.unittest;

import java.util.*;
import java.util.logging.*;
import com.cisco.eManager.eManager.processSequencer.common.logging.CiscoLogger;


public class test {

  public static void main(String args[]) {

    //System.out.println("SEVERE = " + (Level.SEVERE).intValue());
    //System.out.println("FINEST = " + (Level.FINEST).intValue());
    //System.out.println("ALL = " + (Level.ALL).intValue());
    //System.out.println("OFF = " + (Level.OFF).intValue());

    CiscoLogger l = CiscoLogger.getCiscoLogger("test");

    while (true) {
      l.severe("this is severe");
      l.warning("this is warning");
      l.info("this is info");
      l.config("this is config");
      l.fine("this is fine");
      l.finer("this is finer");
      l.finest("this is finest");

      try {
        Thread.sleep(5000);
      } catch (Exception ex) {
        l.severe("Exception sleeping", ex);
      }
    }
  }

  public static void sub1() {
    sub2();
  }

  public static void sub2() {
    CiscoLogger l = CiscoLogger.getCiscoLogger("test.sub2");

    l.severe("this is severe");
    l.warning( "logging an exception", new Exception("exp"));
  }
}

