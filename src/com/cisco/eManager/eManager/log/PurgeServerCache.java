package com.cisco.eManager.eManager.log;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FilenameFilter;
import org.apache.log4j.Logger;

/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class PurgeServerCache {

        private int eventPurgeDur = 0;
        private Calendar purgeDate = null;
        private String logCache = null;
        private String fileName = null;
        private String dirName = null;
        private static PurgeServerCache instance = null;
        private static Logger logger = Logger.getLogger(PurgeServerCache.class);
        private SchedulePurgeServerCache schedule = null;

        public static PurgeServerCache getInstance() {
                return instance;
        }

        public PurgeServerCache(String catalinaHome) throws LogManagerException {
                super();
                if (instance != null) {
                        //singleton object
                        logger.error("More than one instance of PurgeServerCache created");
                        throw new LogManagerException(LogManagerException.SINGLETON_VIOLATION);
                }

                instance = this;
                logCache = new String(catalinaHome + "/webapps/emanager/logDisplay");

                logger.info("PurgeServerCache initialized successfully...");

                runPurgeServerCache();
        }

        public void runPurgeServerCache() {
                purgeDate = new GregorianCalendar();
                purgeDate.add(Calendar.DATE, -2);
                SimpleDateFormat debugFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss zzz");
                logger.info("Purging Log Display Cache files older than " + debugFormat.format(purgeDate.getTime()));
                doName(logCache);
                schedule = new SchedulePurgeServerCache();
        }

        private void doName(String s) {
                //logger.debug("doName(" + s + ")");
                File f = new File(s);
                if (!f.exists()) {
                        logger.debug(s + " does not exist");
                        return;
                }
                if (f.isFile()) {
                        doFile(f);
                }
                else if (f.isDirectory()) {
                        //logger.debug("d " + f.getPath() + " " + f.lastModified());
                        String objects[] = f.list();

                        for (int i = 0; i < objects.length; i++) {
                                doName(s + f.separator + objects[i]);
                        }
                }
                else {
                        logger.error("Unknown type: " + s);
                }
        }

        private void doFile(File f) {
                long t = f.lastModified();
                Calendar modifiedTime = new GregorianCalendar();
                modifiedTime.setTimeInMillis(t);
                //SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyy hh:mm:ss a");
                //logger.debug(formatter.format(modifiedTime.getTime()) + " " + f.getPath());
                if (purgeDate.after(modifiedTime)) {
                        logger.debug("Purging " + f.getPath());
                        if (!f.delete()) {
                                logger.debug("Unable to purge file " + f.getPath());
                        }
                }
        }

        public static void main(String[] args) {
                try {
                        PurgeServerCache psc = new PurgeServerCache("/opt/ettx/jakarta-tomcat-4.0.6");
                        psc.runPurgeServerCache();
                }
                catch (Exception ex) {
                        logger.error(ex);
                }
        }
}
