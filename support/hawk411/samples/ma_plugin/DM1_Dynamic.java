
import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.agent.nest.*;
import java.util.*;

/**
 * This class demonstrates the microagent plug-in capability.
 *
 * It is the simplest form of microagent. It implements only two 
 * synchronous methods.
 */

public class DM1_Dynamic implements MicroAgent, DynamicMicroAgent {
    
    /**
     * holds the descriptor
     */
    private MicroAgentDescriptor _descriptor;

    /**
     * holds the current value
     */
    private String _value;

    /*
     * Start of MicroAgent implementation
     */
    public void initializeMicroAgent(String[] args) throws MicroAgentException {
	System.out.print("In initializeMicroAgent of DM1_Dynamic, args=[");
	for (int i=0; i<args.length; i++) System.out.print(args[i]+" ");
	System.out.println("]");
	if ((args.length>0)&&(args[0].equals("-exception")))
	    throw new MicroAgentException("test exception");
    }
    public MicroAgentDescriptor describeMicroAgent() {
	if (_descriptor == null) _descriptor = DM1_Dynamic.getMicroAgentDescriptor();
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
	    return null;
	} else if ( "getValue".equals(meth) ) {
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
				     )
		    };
	
	return new MicroAgentDescriptor(
					"DM1_Dynamic", // name
					"DM1_Dynamic", // display name
					"DM1_Dynamic microagent", // description
					methods
					);

    }
}







