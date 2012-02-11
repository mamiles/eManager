package com.cisco.eManager.eManager.processSequencer.common;

import java.io.*;
import java.util.Properties;
import java.net.*;

public class PSInetAddress {
   private static boolean haEnabled;
   private static String logicalHostname;
   public static boolean firstRead = true;

   public static boolean isHA () {
      return haEnabled;
   }

   public static void initHA () {

      try {
         String filePath = System.getProperty("em.home") +
	     "/etc/HA.properties";

         File propFile = new File(filePath);
         // check to see if the file exists.
         if (propFile.exists())
         {
             initHA(new FileInputStream(propFile));
         }
         else
         {
           // this is OK - can happen for Web Start Apps
           // try to get it from the jar file vpnscetc.jar
           URL propURL = PSInetAddress.class.
               getClassLoader().getResource("/HA.properties");

           if (propURL != null)
           {
               URLConnection conn = propURL.openConnection();
               InputStream in = conn.getInputStream();
               initHA(in);
	   }
           else
           {
               haEnabled = false;
               logicalHostname = new String();
           }
         }
      } catch (IOException e) {

        haEnabled = false;
        logicalHostname = new String();
      }
   }

    protected static void initHA(InputStream in) throws IOException
    {
	 Properties props = new Properties();
	 props.load(in);

	 String prop1 = "isHA";
	 String isHA = props.getProperty(prop1);

	 if (isHA != null)
	 {
	     String isTrue = "true";
	     if (isHA.equalsIgnoreCase(isTrue))
	     {
		 String prop2 = "logicalHostname";
		 logicalHostname = props.getProperty(prop2);

		 if (logicalHostname != null)
		 {
		     haEnabled = true;
		 }
		 else
		 {
		     haEnabled = false;
		     logicalHostname = new String();
		 }
	     }
	     else
	     {
		 haEnabled = false;
		 logicalHostname = new String();
	     }
	 }
	 else
	 {
	     haEnabled = false;
	     logicalHostname = new String();
	 }
    }

   public static String getLogicalHostname () {
      return logicalHostname;
   }

   public static InetAddress getLocalHost()
      throws UnknownHostException {

      if (firstRead) {
         initHA();
         firstRead = false;
      }

      if (isHA()) {
	 String hostname = getLogicalHostname();
	 InetAddress host = InetAddress.getByName(hostname);
	 return host;
      }
      else {
	 InetAddress host = InetAddress.getLocalHost();
	 return host;
      }
   }
}
