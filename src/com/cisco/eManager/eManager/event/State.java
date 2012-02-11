//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\State.java

package com.cisco.eManager.eManager.event;


/**
 * @author wstubb
 * @version 1.0
 */
public class State
{
    private int state;
    public static final State Processing = new State (1);
    public static final State Idle = new State (2);
    public static final State Stop = new State (3);

    /**
     * @param state
     * @roseuid 3F4F7E87006C
     */
    public State(State state)
    {
        this.state = state.getState();
    }

    private State (int stateValue)
    {
        this.state = stateValue;
    }
    /**
     * @return int
     * @roseuid 3F5762DF0362
     */
    private int getState()
    {
     return state;
    }

    public boolean equals (Object object)
    {
        State state;
        if (object.getClass().equals(this.getClass())) {
            if ( ( (State) object).getState() == this.getState()) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        if (this.equals(State.Idle)) {
            return "Idle";
        } else if (this.equals(State.Processing)) {
            return "Processing";
        } else {
            return "Stop";
        }
    }
}
