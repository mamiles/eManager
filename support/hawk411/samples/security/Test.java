/*
 * Copyright (c) 2000 TIBCO Software, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */
package COM.TIBCO.hawk.security.test;

import java.lang.*;
import java.io.*;

import COM.TIBCO.hawk.console.security.*;

public class Test implements HsConsoleInterface, HsAgentInterface {

	public void Test() {
		System.out.println("PLUGIN: Test.constructor()");
	}

	public void initialize() throws HsException {
		System.out.println("PLUGIN: Test.initialize()");
	}

	public void shutdown() throws HsException {
		System.out.println("PLUGIN: Test.shutdown()");
	}

	public String initialize(int context) throws HsException {
		System.out.println("PLUGIN: Test.initialize(" + context + ")");
		
		return null;
	}

	public void shutdown(int context) throws HsException {
		System.out.println("PLUGIN: Test.shutdown(" + context + ")");
	}

	public HsIdentifier createId(HsOperation operation)
		throws HsException
	{
		if (operation instanceof HsNodeOperation)
			System.out.println("PLUGIN: createId(" +
					  ((HsNodeOperation)operation).microagent() + ":" +
					  ((HsNodeOperation)operation).method() + ")");
		else if (operation instanceof HsGroupOperation)
			System.out.println("PLUGIN: createId(" +
					  ((HsGroupOperation)operation).microagent() + ":" +
					  ((HsGroupOperation)operation).method() + ")");
		else
			System.out.println("PLUGIN: Unknown request");

        HsIdentifier id = null;
        try {
			String name = new String("Test Plug-In");
            id = new HsIdentifier(name.getBytes());
        } catch (HsFrameworkException hsfe) {
            throw new HsException(hsfe.toString());
        }

        return(id);
	}

	public HsPackedOperation pack(HsIdentifier id, HsOperation operation)
		throws HsException
	{
		if (operation instanceof HsNodeOperation)
			System.out.println("PLUGIN: pack("+ new String(id.contents)+ "," +
					  ((HsNodeOperation)operation).microagent() + ":" +
					  ((HsNodeOperation)operation).method() + ")");
		else if (operation instanceof HsGroupOperation)
			System.out.println("PLUGIN: pack(" + new String(id.contents)+ "," +
					  ((HsGroupOperation)operation).microagent() + ":" +
					  ((HsGroupOperation)operation).method() + ")");
		else
			System.out.println("PLUGIN: Unknown request");

		TestOperation trustme =
			new TestOperation(id.contents, operation.contents);

		byte[] packed = null;
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(buffer);
			
			out.writeObject(trustme);
			out.flush();
			out.close();
		
			packed = buffer.toByteArray();

		} catch (IOException ioe) {
		}

        HsPackedOperation packedOperation = null;
        try {
            packedOperation = new HsPackedOperation(packed);
        } catch (HsFrameworkException hsfe) {
            throw new HsException(hsfe.toString());
        }

        return (packedOperation);
	}

	public HsUnpackedOperation unpack(HsPackedOperation operation)
		throws HsException
	{
		System.out.println("PLUGIN: unpack(operation)");

		TestOperation trustme = null;
		try {
			ByteArrayInputStream buffer =
				new ByteArrayInputStream(operation.contents);
			ObjectInputStream in = new ObjectInputStream(buffer);

			trustme = (TestOperation)in.readObject();
			in.close();
		} catch (ClassNotFoundException cnfe) {
			throw new HsException(cnfe.toString());
		} catch (IOException ioe) {
			throw new HsException(ioe.toString());
		}

        HsUnpackedOperation unpacked = null;
        try {
            unpacked = new HsUnpackedOperation
              (new HsIdentifier(trustme.id),new HsOperation(trustme.operation));
        } catch (HsFrameworkException hsfe) {
            throw new HsException(hsfe.toString());
        }

        return unpacked;
	}

	public boolean validateId(HsIdentifier id, HsOperation operation)
	{
		if (operation instanceof HsNodeOperation)
		   System.out.println("PLUGIN: validateId("+new String(id.contents)+","+
					  ((HsNodeOperation)operation).microagent() + ":" +
					  ((HsNodeOperation)operation).method() + ")");
		else if (operation instanceof HsGroupOperation)
		   System.out.println("PLUGIN: validateId("+new String(id.contents)+","+
					  ((HsGroupOperation)operation).microagent() + ":" +
					  ((HsGroupOperation)operation).method() + ")");
		else
			System.out.println("PLUGIN: Unknown request");

		String name = new String(id.contents);

		if (name.equals("Test Plug-In"))
			return(true);
		else
			return(false);
	}

	public String describe() {
		System.out.println("PLUGIN: Test.describe()");

		return(new String("TIBCO Hawk Test security model."));
	}
}

