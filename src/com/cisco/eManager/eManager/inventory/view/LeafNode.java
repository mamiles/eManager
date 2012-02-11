//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\leafNode\\LeafNode.java

//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\leaf\\LeafNode.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.eManager.common.inventory.*;

public abstract class LeafNode extends Node
{
    private static Logger logger = Logger.getLogger(LeafNode.class);

    protected LeafNode()
    {
    }

    /**
     * @return boolean
     * @roseuid 3F872D7902EC
     */
    public final boolean relocatable()
    {
        return false;
    }

    /**
     * @return boolean
     * @roseuid 3F872D79035A
     */
    public final boolean canHaveChildren()
    {
        return false;
    }

    /**
     * @param node
     * @return boolean
     * @roseuid 3F872D790364
     */
    public boolean canHaveChild(Node node)
    {
        return false;
    }
}
