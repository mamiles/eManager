//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\SolutionNode.java

package com.cisco.eManager.eManager.inventory.view;

import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.ManagedObjectId;
import com.cisco.eManager.common.inventory.ManagedObjectIdType;
import com.cisco.eManager.eManager.inventory.solution.Solution;

public class SolutionNode extends AbstractContainerNode
{
    private static Logger logger = Logger.getLogger(SolutionNode.class);
    private Solution m_solution;

    SolutionNode(Solution solution, AbstractContainerNode parent)
        throws Exception
    {
        m_solution = solution;
        if ( parent != null )
        {
            Node.attach(parent, this);
        }
    }

    public ManagedObjectId id()
    {
        return m_solution.id();
    }

    public String name()
    {
        return m_solution.name();
    }

    public Solution solution()
    {
        return m_solution;
    }

    /**
     * @param node
     * @return boolean
     * @roseuid 3F872D7202A6
     */
    public boolean canHaveChild(Node node)
    {
        if ( node instanceof AppInstanceNode )
        {
            return true;
        }
        return false;
    }

    public boolean relocatable()
    {
        return true;
    }
}
