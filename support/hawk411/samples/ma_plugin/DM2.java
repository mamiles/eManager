
import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.agent.nest.*;
import java.util.*;

/**
 * This class demonstrates the microagent plug-in capability.
 *
 * It illustrates how to support async methods.
 */

public class DM2 implements MicroAgent, AsyncMethodHandler {

    private MicroAgentDescriptor _descriptor;
    private String _value;
    private AsyncDataHandler _asyncValueHandler = null;

    /*
     * Start of MicroAgent implementation
     */
    public void initializeMicroAgent(String[] args) throws MicroAgentException {
	System.out.print("In initializeMicroAgent of DM2, args=[");
	for (int i=0; i<args.length; i++) System.out.print(args[i]+" ");
	System.out.println("]");
	if ((args.length>0)&&(args[0].equals("-exception")))
	    throw new MicroAgentException("test exception");
    }
    public MicroAgentDescriptor describeMicroAgent() {
	if (_descriptor == null) _descriptor = DM2.getMicroAgentDescriptor();
	return _descriptor;
    }
    public void thisMicroAgentAdded( MicroAgentID mid ) {
	System.out.println("In thisMicroAgentAdded, id= "+mid);
    }
    public void thisMicroAgentRemoved() {
	System.out.println("In thisMicroAgentRemoved");
    }
    /*
     * End of MicroAgent implementation
     */


    /*
     * Start of AsyncMethodHandler implementation
     */
    public void startAsyncMethodSubscription(MethodSubscription msub, AsyncDataHandler h) {
	// since there only is one async method without arguments, there is only one
	// possible msub. Thus, no need to track different subscriptions in a table
	if (msub.getMethodName().equals("getValueAsync"))
	    _asyncValueHandler = h;
    }
    public void stopAsyncMethodSubscription(MethodSubscription msub) {
	if (msub.getMethodName().equals("getValueAsync"))
	    _asyncValueHandler = null;
    }

    /*
     * End of AsyncMethodHandler implementation
     */


    /*
     * Start of exposed microagent methods
     */
    public void setValue(String value) {
	_value = value;
	if (_asyncValueHandler != null) {
	    _asyncValueHandler.onData(getValue());
	}
    }
    public CompositeData getValue() {
	return new CompositeData(new DataElement[]{
	    new DataElement("value", _value)
		});
    }
    public CompositeData getValueAsync() {return getValue(); }
    /*
     * End of exposed microagent methods
     */

    public static MicroAgentDescriptor getMicroAgentDescriptor() {
	MethodDescriptor[] methods
	    = new MethodDescriptor[] {
		new MethodDescriptor(
				     "setValue", // method name
				     "sets local value",
				     null, // return data descriptor
				     new DataDescriptor[] {
					 new DataDescriptor(
							    "value",
							    String.class,
							    "local value"
							    )
					     }, // argument data descriptor
				     false, // is async?
				     MethodDescriptor.IMPACT_ACTION, // impact
				     5 // maxTime in seconds
				     ),
		new MethodDescriptor(
				     "getValue", // method name
				     "gets local value",
				     new CompositeDataDescriptor(
								 "return",
								 "",
								 new DataDescriptor[] {
								     new DataDescriptor(
											"value",
											String.class,
											"local value"
											)
									 }
								 ), // return data descriptor
				     null, // argument data descriptor
				     false, // is async?
				     MethodDescriptor.IMPACT_INFO, // impact
				     5 // maxTime in seconds
				     ),
		new MethodDescriptor(
				     "getValueAsync", // method name
				     "gets local value",
				     new CompositeDataDescriptor(
								 "return",
								 "",
								 new DataDescriptor[] {
								     new DataDescriptor(
											"value",
											String.class,
											"local value"
											)
									 }
								 ), // return data descriptor
				     null, // argument data descriptor
				     true, // is async?
				     MethodDescriptor.IMPACT_INFO, // impact
				     5 // maxTime in seconds
				     )
		    };
	
	return new MicroAgentDescriptor(
					"DM2", // name
					"DM2", // display name
					"DM2 microagent", // description
					methods
					);

    }
}
