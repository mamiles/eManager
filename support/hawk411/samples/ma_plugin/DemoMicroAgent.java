
import COM.TIBCO.hawk.talon.*;
import COM.TIBCO.hawk.agent.nest.*;
import java.util.*;

/**
 * This class demonstrates the microagent plug-in capability.
 *
 * It is a ServiceMicroAgent that demonstrates the abilities
 * to load/unload other microagents, invoke methods of other
 * microagents, and listen to MicroAgentContainer events.
 */
public class DemoMicroAgent implements ServiceMicroAgent, ContainerEventListener {

    /**
     * holds a ref to the container
     */
    private MicroAgentContainer _mac;
    /**
     * holds the microagent ids of all microagents in the agent
     */
    private Vector _addedMAs = new Vector();
    /**
     * holds the actual microagents, indexed by id, of the
     * microagents loaded into the agent as a result of calling
     * the loadMicroAgent method of this microagent
     */
    private Hashtable _loadedMAs = new Hashtable();

    /**
     * holds the descriptor
     */
    private MicroAgentDescriptor _descriptor;

    /*
     * Start of ServiceMicroAgent implementation
     */
    public void initializeMicroAgent(String[] args) throws MicroAgentException {
	System.out.print("In initializeMicroAgent, args=[");
	for (int i=0; i<args.length; i++) System.out.print(args[i]+" ");
	System.out.println("]");
	if ((args.length>0)&&(args[0].equals("-exception")))
	    throw new MicroAgentException("test exception");
    }
    public void setMicroAgentContainer(MicroAgentContainer c) {
	_mac=c;
	c.addContainerEventListener(this);
    }
    public MicroAgentDescriptor describeMicroAgent() {
	if (_descriptor == null) _descriptor = DemoMicroAgent.getMicroAgentDescriptor();
	return _descriptor;
    }
    public void thisMicroAgentAdded( MicroAgentID mid ) {
	System.out.println("In thisMicroAgentAdded, id= "+mid);
    }
    public void thisMicroAgentRemoved() {
	System.out.println("In thisMicroAgentRemoved");
    }
    /*
     * End of ServiceMicroAgent implementation
     */

    /*
     * Start of ContainerEventListener implementation
     */
    public void onInitial(MicroAgentID[] ids) {
	for (int i=0; i<ids.length; i++)
	    _addedMAs.addElement(ids[i]);
    }
    public void onMicroAgentAdded(MicroAgentID id) {
	_addedMAs.addElement(id);
    }
    public void onMicroAgentRemoved(MicroAgentID id) {
	_addedMAs.removeElement(id);
    }
    /*
     * End of ContainerEventListener implementation
     */

    /*
     * Start of exposed microagent methods
     */
    public void loadMicroAgent(String className, String argString)
	throws ClassNotFoundException, IllegalAccessException, InstantiationException, MicroAgentException
    {
	Object ma = Class.forName(className).newInstance();
	if (!(ma instanceof MicroAgent))
	    throw new IllegalArgumentException(ma.getClass().getName()+ " doesn't implement MicroAgent");
	String[] args;
	if ((argString == null)||(argString.equals(""))) {
	    args = new String[0];
	} else {
	    args = parseString(argString);
	}
	MicroAgentID mid = _mac.addMicroAgent((MicroAgent)ma, args);
	_loadedMAs.put(mid, ma);
    }
    public void unloadMicroAgent(String maName, String instance) throws MicroAgentException {
	MicroAgentID mid, removedID = null;
	for (Enumeration e = _loadedMAs.keys(); e.hasMoreElements();) {
	    mid = (MicroAgentID)e.nextElement();
	    if (mid.getName().equals(maName)&&mid.getInstance().equals(instance)) {
		removedID = mid;
		break;
	    }
	}
	if (removedID != null) {
	    _mac.removeMicroAgent((MicroAgent)_loadedMAs.get(removedID));
	    _loadedMAs.remove(removedID);
	} else
	    throw new MicroAgentException("microagent not found");
	
    }
    public TabularData getMicroAgentTable() {
	String[] columns = new String[]{"name", "display name", "instance"};
	String[] indexes = new String[]{"name", "instance"};
	String[][] data = new String[_addedMAs.size()][3];
	MicroAgentID mid;
	for (int i=0; i<_addedMAs.size(); i++) {
	    mid = (MicroAgentID)_addedMAs.elementAt(i);
	    data[i][0] = mid.getName();
	    data[i][1] = mid.getDisplayName();
	    data[i][2] = mid.getInstance();
	}
	return new TabularData(columns, indexes, data);
    }

    public CompositeData invokeCustomExecuteForString(String cmd)
    	throws MicroAgentException
    {
	MicroAgentID mid;
	Object result;
	for (int i=0; i<_addedMAs.size(); i++) {
	    mid = (MicroAgentID)_addedMAs.elementAt(i);
	    if (mid.getName().equals("COM.TIBCO.hawk.microagent.Custom")) {
		result =_mac.invoke(mid, new MethodInvocation("executeForString",
							     new DataElement[]{
								 new DataElement("command", cmd)
								     }
							     )
		    ).getData();
		if (result instanceof CompositeData) {
		    return (CompositeData)result;
		} else if (result instanceof MicroAgentException) {
		    throw (MicroAgentException)result;
		} else if (result == null) {
		    throw new MicroAgentException("executeForString invocation returned null");
		} else {
		    throw new MicroAgentException("executeForString return unhandled type: "+result.getClass());
		}
	    }
	}
	throw new MicroAgentException("Custom microagent not found");
    }

    public CompositeData invokeProcessGetInstanceCount(String cmd)
    	throws MicroAgentException
    {
	MicroAgentID mid;
	Object result;
	for (int i=0; i<_addedMAs.size(); i++) {
	    mid = (MicroAgentID)_addedMAs.elementAt(i);
	    if (mid.getName().equals("COM.TIBCO.hawk.hma.Process")) {
		result =_mac.invoke(mid, new MethodInvocation("getInstanceCount",
							     new DataElement[]{
								 new DataElement("Process Name", cmd)
								     }
							     )
		    ).getData();
		if (result instanceof CompositeData) {
		    return (CompositeData)result;
		} else if (result instanceof MicroAgentException) {
		    throw (MicroAgentException)result;
		} else if (result == null) {
		    throw new MicroAgentException("getInstanceCount invocation returned null");
		} else {
		    throw new MicroAgentException("getInstanceCount return unhandled type: "+result.getClass());
		}
	    }
	}
	throw new MicroAgentException("Process microagent not found");
    }
    /*
     * End of exposed microagent methods
     */

    public static MicroAgentDescriptor getMicroAgentDescriptor() {
	MethodDescriptor[] methods
	    = new MethodDescriptor[] {
		new MethodDescriptor(
				     "loadMicroAgent", // method name
				     "Loads a microagent into the microagent container given the microagent's class.", // method description
				     null, // return data descriptor
				     new DataDescriptor[] {
					 new DataDescriptor(
							    "class",
							    String.class,
							    "the class name of the microagent"
							    ),
					 new DataDescriptor(
							    "args",
							    String.class,
							    "the arguments for the microagent"
							    )
					     }, // argument data descriptor
				     false, // is async?
				     MethodDescriptor.IMPACT_ACTION, // impact
				     5 // maxTime in seconds
				     ),
		new MethodDescriptor(
				     "unloadMicroAgent", // method name
				     "unloads a microagent into the microagent container given the microagent's name.", // method description
				     null, // return data descriptor
				     new DataDescriptor[] {
					 new DataDescriptor(
							    "name",
							    String.class,
							    "the name of the microagent to unload"
							    ),
					 new DataDescriptor(
							    "instance",
							    String.class,
							    "the instance of the microagent to unload"
							    )
					     }, // argument data descriptor
				     false, // is async?
				     MethodDescriptor.IMPACT_ACTION, // impact
				     5 // maxTime in seconds
				     ),
		new MethodDescriptor(
				     "getMicroAgentTable", // method name
				     "returns a table listing all loaded microagents", // method description
				     new TabularDataDescriptor(
							       "microagents",
							       "currently loaded microagents",
							       new DataDescriptor[] {
								   new DataDescriptor(
										      "name",
										      String.class,
										      "microagent name"
										      ),
								   new DataDescriptor(
										      "display name",
										      String.class,
										      "microagent display name"
										      ),
								   new DataDescriptor(
										      "instance",
										      String.class,
										      "microagent instance"
										      )
								       },
							       new String[]{"name", "instance"}
				     ), // return data descriptor
				     null, // argument data descriptor
				     false, // is async?
				     MethodDescriptor.IMPACT_INFO, // impact
				     5 // maxTime in seconds
				     ),
		new MethodDescriptor(
				     "invokeCustomExecuteForString", // method name
				     "Invokes the executeForString method of the custom microagent with the supplied argument.", // method description
				     new CompositeDataDescriptor(
								 "return",
								 "",
								 new DataDescriptor[] {
								     new DataDescriptor(
											"returnString",
											String.class,
											"the return"
											)
									 }
								 ), // return data descriptor
				     new DataDescriptor[] {
					 new DataDescriptor(
							"command",
							String.class,
							"the name of the command to execute"
							)
					     }, // argument data descriptor
				     false, // is async?
				     MethodDescriptor.IMPACT_ACTION_INFO, // impact
				     5 // maxTime in seconds
				     ),
		new MethodDescriptor(
				     "invokeProcessGetInstanceCount", // method name
				     "Invokes the getInstanceCount method of the Process microagent with the supplied argument.", // method description
				     new CompositeDataDescriptor(
								 "return",
								 "",
								 new DataDescriptor[] {
								     new DataDescriptor(
											"Process Name",
											String.class,
											""
											),
								     new DataDescriptor(
											"Process Count",
											Integer.class,
											""
											)
									 }
								 ), // return data descriptor
				     new DataDescriptor[] {
					 new DataDescriptor(
							"Process Name",
							String.class,
							"the name of the process instance"
							)
					     }, // argument data descriptor
				     false, // is async?
				     MethodDescriptor.IMPACT_INFO, // impact
				     5 // maxTime in seconds
				     )
		    };
	
	return new MicroAgentDescriptor(
					"DemoMicroAgent", // name
					"DemoMicroAgent", // display name
					"This micro agent is used to demonstrate the agent's microagent loading capabilities", // description
					methods
					);

    }


    public static String[] parseString(String argString) {
	Vector argsVector = new Vector();
	String s = argString.trim();
	int start=0;
	int end;
	while ((end=s.indexOf(' ',start))!=-1) {
	    if (start != end)
		argsVector.addElement(s.substring(start,end));
	    start = end+1;
	}
	argsVector.addElement(s.substring(start,s.length()));
	String[] args = new String[argsVector.size()];
	argsVector.copyInto(args);
	return args;
    }


}
