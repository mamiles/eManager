/*
 * Copyright (c) 2000 TIBCO Software, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * TIBCO Software Inc.
 */
package COM.TIBCO.hawk.security.test;

import java.io.*;

import COM.TIBCO.hawk.console.security.*;

public class TestOperation implements Serializable
{
		public byte[] id;
		public byte[] operation;

		public TestOperation(byte[] id, byte[] operation)
		{
			this.id = id;
			this.operation = operation;
		}

}


