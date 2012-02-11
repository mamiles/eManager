package com.cisco.eManager.eManager.log;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FilenameFilter;
import org.apache.log4j.Logger;
import java.util.Timer;
import java.util.TimerTask;
/**
 * <p>Title: eManager</p>
 * <p>Description: Cisco's "Systems Management" Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems, Inc.</p>
 * @author not attributable
 * @version 1.0
 */

public class SchedulePurgeServerCache {

        private static Logger logger = Logger.getLogger(SchedulePurgeServerCache.class);
        private Timer purgeServerTimer;

        public SchedulePurgeServerCache() {

                Date schedDate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, +1);
                schedDate = cal.getTime();
                SimpleDateFormat debugFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss zzz");
                logger.info("Scheduling Purge Server Log Cache for " + debugFormat.format(schedDate));
                purgeServerTimer = new Timer();
                purgeServerTimer.schedule(new PurgeServerCacheTask(), schedDate);
        }

        public void cancleCurrentSchedule() {
                purgeServerTimer.cancel();
        }

        class PurgeServerCacheTask
                extends TimerTask {

                public void run() {

                        try {
                                PurgeServerCache psc = PurgeServerCache.getInstance();
                                psc.runPurgeServerCache();
                                purgeServerTimer.cancel();
                        }
                        catch (Exception ex) {
                                logger.error("Error running Purge Server Log Cache Scheduled Process");
                                logger.error(ex);
                        }
                }
        }

}
