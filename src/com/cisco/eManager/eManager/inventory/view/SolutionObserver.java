//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\SolutionObserver.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.eManager.inventory.solution.*;

public class SolutionObserver implements Observer
{
    private static Logger logger = Logger.getLogger(SolutionObserver.class);
    private SolutionViewManager m_svm;

    public SolutionObserver()
        throws Exception
    {
        logger.debug("enter");
        m_svm = SolutionViewManager.instance();
        SolutionManager.instance().addObserver(this);
    }

    public void update()
    {
        logger.debug("enter");
    }

    public void update(Observable solutionManager, Object obj)
    {
        logger.debug("enter");
        if ( obj instanceof BasicSolutionNotification )
        {
            BasicSolutionNotification bsn = (BasicSolutionNotification)obj;
            Solution solution = bsn.solution();
            BasicSolutionNotificationType type = bsn.ntfcnType();
            if ( type.equals(BasicSolutionNotificationType.CREATE) )
            {
                m_svm.newSolution(solution);
            }
            else if ( type.equals(BasicSolutionNotificationType.PRE_DELETE) )
            {
                m_svm.deletingSolution(solution);
            }
            else if ( type.equals(BasicSolutionNotificationType.UNDELETE) )
            {
                m_svm.undeletedSolution(solution);
            }
            else if ( type.equals(BasicSolutionNotificationType.RESTORE) )
            {
                m_svm.undeletedSolution(solution);
            }
            else
            {
                logger.debug("notification is of a type not acted upon by SVM");
            }
        }
        else
        {
            logger.warn("object of unexpected class received: " +
                        obj.getClass().getName());
        }
    }
}