//Source file: /vob/emanager/src/com/cisco/eManager/common/event/SynchronizationPriority.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\common\\event\\SynchronizationPriority.java

package com.cisco.eManager.common.event;


public class SynchronizationPriority
{
    private int priority;
    public static final SynchronizationPriority Low = new SynchronizationPriority (1);
    public static final SynchronizationPriority Medium = new SynchronizationPriority (2);
    public static final SynchronizationPriority High = new SynchronizationPriority (3);

    // fix
    // Workaround for SOAP
    public SynchronizationPriority()
    {
        priority = 1;
    }

    /**
     * @param priority
     * @roseuid 3F4E5D4E00CD
     */
    private SynchronizationPriority(int priority)
    {
	this.priority = priority;
    }

    private int intValue()
    {
	return priority;
    }

    public boolean equals (Object object)
    {
        if (object instanceof SynchronizationPriority) {
            if ( ( (SynchronizationPriority) object).intValue() == this.intValue()) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        if (this.equals(SynchronizationPriority.Low)) {
            return "Low";
        } else if (this.equals(SynchronizationPriority.Medium)) {
            return "Medium";
        }
        return "High";
    }
}
