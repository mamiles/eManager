//Source file: /vob/emanager/src/com/cisco/eManager/eManager/event/AbstractWorker.java

//Source file: D:\\vws\\root\\mjmatch-emanager-main-snapshot\\emanager\\src\\com\\cisco\\eManager\\eManager\\event\\AbstractWorker.java

package com.cisco.eManager.eManager.event;


public abstract class AbstractWorker implements Runnable
{
    private State currentState = new State (State.Idle);
    private State desiredState = new State (State.Idle);
    private boolean stopWorker = false;
    private Boolean sleepingObject = new Boolean(true);

    /**
     * @roseuid 3F53AA7B0028
     */
    public AbstractWorker()
    {

    }

    /**
     *
     */
    public AbstractWorker(State aCurrentState,
			  State aDesiredState,
			  boolean aStopWorker)
    {
	currentState = aCurrentState;
	desiredState = aDesiredState;
	stopWorker = aStopWorker;
    }

    /**
     * Access method for the desiredState property.
     *
     * @return   the current value of the desiredState property
     */
    public State getDesiredState()
    {
        return desiredState;
    }

    /**
     * Sets the value of the desiredState property.
     *
     * @param aDesiredState the new value of the desiredState property
     */
    public void setDesiredState(State aDesiredState)
    {
        desiredState = aDesiredState;
    }

    /**
     * Determines if the stopWorker property is true.
     *
     * @return   <code>true<code> if the stopWorker property is true
     */
    public boolean getStopWorker()
    {
        return stopWorker;
    }

    /**
     * Sets the value of the stopWorker property.
     *
     * @param aStopWorker the new value of the stopWorker property
     */
    public void setStopWorker(boolean aStopWorker)
    {
        stopWorker = aStopWorker;
    }

    /**
     * @return com.cisco.eManager.eManager.event.State
     * @roseuid 3F252F010171
     */
    public State getCurrentState()
    {
	return currentState;
    }

    /**
     * @roseuid 3F252F1602C9
     */
    public void wakeup()
    {

    }

    /**
     * @roseuid 3F2530270006
     */
    public void sleep()
    {

    }

    public String workerIdentifier()
    {
        String id;

        id = "Thread: " + getThread().getName() + " - ";
        return id;
    }

    /**
     * @param state
     * @roseuid 3F29831203B3
     */
    protected void setCurrentState(State state)
    {
	currentState = state;
        notifyStateChangeListeners();
    }

    protected abstract Thread getThread();

    /**
     * @roseuid 3F53B05A0183
     */
    public abstract void run();

    public abstract void notifyStateChangeListeners();
}
