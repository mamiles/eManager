
import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.agent.nest.*;
import java.util.*;

/**
 * This class demonstrates the microagent plug-in capability.
 *
 * It illustrates how to support async methods.
 */

public class DM2_Dynamic implements MicroAgent, AsyncMethodHandler, DynamicMicroAgent {

    private MicroAgentDescriptor _descriptor;
    private String _value;
    private AsyncDataHandler _asyncValueHandler = null;

    /*
     * Start of MicroAgent implementation
     */
    public void initializeMicroAgent(String[] args) throws MicroAgentException {
	System.out.print("In initializeMicroAgent of DM2_Dynamic, args=[");
	for (int i=0; i<args.length; i++) System.out.print(args[i]+" ");
	System.out.println("]");
	if ((args.length>0)&&(args[0].equals("-exception")))
	    throw new MicroAgentException("test exception");
    }
    public MicroAgentDescriptor describeMicroAgent() {
	if (_descriptor == null) _descriptor = DM2_Dynamic.getMicroAgentDescriptor();
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
     * Start of DynamicMicroAgent implementation
     */
    public Object invokeMethod(MethodInvocation mi) throws MicroAgentException {
	String meth = mi.getMethodName();
	if ( "setValue".equals(meth) ) {
	    DataElement[] args = mi.getArguments();
	    if ( args.length != 1 ) {
		throw new MicroAgentException("Incorrect number of arguments");
	    } 
	    _value = (String)(args[0].getValue());
	    if (_asyncValueHandler != null) {
		_asyncValueHandler.onData(
		  new CompositeData(new DataElement[]{new DataElement("value", _value)})
		  );
	    }

	    return null;
	} else if ( "getValue".equals(meth) || "getValueAsync".equals(meth) ) {
	    return new CompositeData(new DataElement[]{new DataElement("value", _value)});
	} else {
	    throw new MicroAgentException("Unsupported Method: "+meth);
	} 
    }
    /*
     * End of DynamicMicroAgent implementation
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
					"DM2_Dynamic", // name
					"DM2_Dynamic", // display name
					"DM2_Dynamic microagent", // description
					methods
					);

    }
}
