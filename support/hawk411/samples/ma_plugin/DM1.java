
import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.agent.nest.*;
import java.util.*;

/**
 * This class demonstrates the microagent plug-in capability.
 *
 * It is the simplest form of microagent. It implements only two
 * synchronous methods.
 */

public class DM1 implements MicroAgent {

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
	System.out.print("In initializeMicroAgent of DM1, args=[");
	for (int i=0; i<args.length; i++) System.out.print(args[i]+" ");
	System.out.println("]");
	if ((args.length>0)&&(args[0].equals("-exception")))
	    throw new MicroAgentException("test exception");
    }
    public MicroAgentDescriptor describeMicroAgent() {
	if (_descriptor == null) _descriptor = DM1.getMicroAgentDescriptor();
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
     * Start of exposed microagent methods
     */
    public void setValue(String value) {
	_value = value;
    }
    public CompositeData getValue() {
	return new CompositeData(new DataElement[]{new DataElement("value", _value)});
    }
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
				     )
		    };
	
	return new MicroAgentDescriptor(
					"DM1", // name
					"DM1", // display name
					"DM1 microagent", // description
					methods
					);

    }
}







