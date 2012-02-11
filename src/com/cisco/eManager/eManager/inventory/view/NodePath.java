//Source file: D:\\vws\\root\\wstubb-emanager-main2-snap\\emanager\\src\\com\\cisco\\eManager\\eManager\\inventory\\view\\node\\Node.java

package com.cisco.eManager.eManager.inventory.view;

import java.util.*;
import org.apache.log4j.Logger;

public class NodePath
{
    private static Logger logger = Logger.getLogger(Node.class);
    private List m_pathComponents;

    public NodePath()
    {
        logger.debug("enter");
        m_pathComponents = new LinkedList();
    }

    public NodePath(String[] path)
    {
        logger.debug("enter");
        m_pathComponents = new LinkedList();
        for (int i = 0; i < path.length; i++)
        {
            m_pathComponents.add(path[i]);
        }
    }

    public void add(String component)
    {
        m_pathComponents.add(component);
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        Iterator iter = m_pathComponents.iterator();
        // build up our return string
        // - we provide the '/' separator for all cases except for the special
        //   first case (where if the first component is "/", we don't)
        boolean isFirst = true;
        boolean firstIsSlash = false;
        String component = null;
        for ( int i = 0; i < m_pathComponents.size(); i++ )
        {
            component = (String)m_pathComponents.get(i);

            if ( i == 0 ) // if this is the first component...
            {
                // ...then omit the prefixing separator
            }
            else if ( i == 1 ) // ...else if this is the second component...
            {
                // ...and the first component isn't "/" ...
                if ( !((String)m_pathComponents.get(0)).equals("/") )
                {
                    // ...then throw in the separator
                    sb.append("/");
                }
                // else omit the separator (since this is the second component
                // and the first one is a "/"
            }
            else
            {
                sb.append("/");
            }
            sb.append(component);
        }
        return sb.toString();
    }

    public boolean equals(NodePath np)
    {
        String localStr = toString();
        String remoteStr = np.toString();
        if ( localStr.equals(remoteStr) )
        {
            return true;
        }
        return false;
    }
}
