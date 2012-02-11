/*********************************************************************
 * This computer program is the confidential information and proprietary
 * trade secret of Cisco Systems, Inc.  Possessions and use of this
 * program must conform strictly to the license agreement between the user
 * and Cisco Systems, Inc., and receipt or possession does not convey
 * any rights to divulge, reproduce, or allow others to use this program
 * without specific written authorization of Cisco Systems, Inc.
 *
 * Copyright (c) 2001 by Cisco Systems, Inc.
 * All rights reserved.
 *
 *********************************************************************/
package com.cisco.eManager.eManager.processSequencer.common;

import java.io.*;
import java.net.*;
import java.util.*;

//for HA
import com.cisco.eManager.eManager.processSequencer.common.PSInetAddress;

/**
 * SMTPMailer class handles sending an email message using SMTP.
 * The host for the SMTP connection is set using the setHost() method.
 * It allows the client to set message parameters such as From, To, Subject
 * in any order using the supported methods. The startSend() method returns a
 * PrintWriter object which the client can use to write the contents of the
 * message. When the PrintWriter is closed, the message is sent.
 * The connection on the host is closed once the message is sent.
 */

public final class SMTPMailer {

	/**
	 * The information to be written to host
	 */
	private PrintWriter mOutput = null;

	/**
	 * The information returned from host
	 */
	private BufferedReader mInput = null;

	private String mResponse;

	/**
	 * The name of the server
	 */
	private String mServerName = "";
	private String mSubject;
	private Vector mRcpts;
	private String mFrom;
	private InetAddress mLocalHost=null;
	private Socket mServer=null;

	/**
	 * Basic constructor
	 */
	public SMTPMailer() {
		mRcpts = new Vector();
	}

	/**
	 * Constructor requiring SMTP server name.
	 * Stores server's name until connection opened.
	 */
	public SMTPMailer(String serverName){
		mServerName = serverName;
		mRcpts = new Vector();
	}

	/**
	 * Set the name of the server
	 */
	public void setServer(String serverName) {
		mServerName=serverName;
	}

	/**
	 * Set the From field
	 */
	public void setFrom(String from) {
		mFrom=from;
	}


	/**
	 * Set the Subject field
	 */
	public void setSubject(String subject) {
		mSubject=subject;
	}

	/**
	 * Adds the particular recipient to the To field.
	 * Repeated calls to setTo okay as long as the number of calls
	 * does not exceed MAX_RCPTS
	 */
	public void addRecepient(String Rcpt) {
		mRcpts.add(Rcpt);
	}

	/**
	 * startSend() method initiates the contact to the server
	 * It then relays all the SMTP commands to setup the fields:
	 * To, From, Subject
	 @return PrintWriter which is used to write the contents of the message
	 @throws Exception
	*/
	public PrintWriter startSend() throws Exception{
		int code; //code returned by server

		try {
			mServer = new Socket(mServerName, 25); //initialize socket
			mOutput = new PrintWriter(mServer.getOutputStream());
			mInput = new BufferedReader(new InputStreamReader(mServer.getInputStream()));
		}
		catch(IOException e) {
			throw e;
		}

		code = getCode();
		if(code != 2) {
			throw new Exception("Invalid SMTP server mResponse before HELO:"+mResponse);
		}

		//send HELO to server
		mOutput.println("HELO "+PSInetAddress.getLocalHost().getHostName());
		mOutput.flush();

		code = getCode();
		if(code != 2) {
			throw new Exception("SMTP server Error after HELO:"+mResponse);
		}

		//send MAIL FROM to server
		mOutput.println("MAIL FROM: <" + mFrom + ">");
		mOutput.flush();

		code = getCode();
		if(code != 2) {
			throw new Exception("SMTP server Error after MAIL FROM:"+mResponse);

		}

		for (int i=0; i < mRcpts.size(); i++) {
			mOutput.println("RCPT TO: <"+(String)mRcpts.elementAt(i)+">");
			mOutput.flush();

			code = getCode();
			if(code != 2) {
				throw new Exception("SMTP server Error after RCPT TO:" +
						(String)mRcpts.elementAt(i)+ ":"+mResponse);
			}
		}

		mOutput.println("DATA ");
		mOutput.flush();

		code = getCode();
		if(code != 3) {
			throw new Exception("SMTP server Error after DATA:"+mResponse);
		}


		mOutput.println("Subject: " + mSubject);
		mOutput.print("To: ");
		for (int i=0;i< mRcpts.size();i++) {
			if(i>0) mOutput.print(", ");
			mOutput.print((String)mRcpts.elementAt(i));
		}
		mOutput.print("\n");

		mOutput.flush();
		return new PrintWriter (new Writer ());
	}

	protected int getCode() throws Exception {
		int code;
		String mess;
		mResponse = "";

		do{
			// read the mResponse from the server
			try {
				mess=mInput.readLine();
			}
			catch(IOException e){
				throw e;
			}
			mResponse+=mess;

		} while(mess.charAt(3)=='-');

		// The first digit is the main error code to worry about
		String c="" + mess.charAt(0);
		try{
			code=Integer.parseInt(c);
		}
		catch(NumberFormatException e){
			code=5;
		}

		return code;
	}



	/**
	 * Class Writer ensures the output to server maintains SMTP
	 * standards
	 */
	class Writer extends OutputStream {
		private int numChars;
		private int marked = 0;

		/**
		 * send the message
		 */
		public void close () {
			int code;

			mOutput.println (".");
			mOutput.println("QUIT ");
			mOutput.flush();

			try {
				code = getCode();
				if(code != 2) {
					System.out.println("SMTP server Error after QUIT:" + mResponse);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * write what is in the buffer to the server
		 */
		public void flush() {
			mOutput.flush();
		}

		/*
		 * write buf to server
		 */
		public void write (char[] buf) {
			numChars = 0;
			mOutput.write (buf);
		}

		/**
		 * write an int to server
		 */
		public void write (int b) {
			mOutput.write(b);
		}

		/**
		 * write a byte array to server
		 */
		public void write(byte[] b) {
			numChars = 0;
			char ch[] = new char[b.length];
			int i = 0;
			int count = 0;

			while(b[i] != '\0') { ch[count++] = (char)b[i]; i++;}

			mOutput.write(ch, 0, count);
		}

		/**
		 * write a byte array with an offset off and length len to server
		 */
		public void write(byte[] b, int off, int len) {
			int retCode;
			numChars = 0;

			char ch[] = new char[len];
			int count=0;

			for(int i = 0; i < len; i++) {
				retCode = checkChar((char)b[off + i]);
				if(retCode != -1) {
					ch[count++] = (char)b[off + i];
				} else {
					ch[count++] = (char)' ';
				}
			}
			mOutput.write(ch, 0, count);
		}

		private int checkChar(char c) {
			if(1 == marked) {
				if(c == '\n') {
					numChars = 0;
					marked = 0;
					return -1;
				}
				marked = 0;
			}

			if(c == '.') {
				if(0 == numChars) {
					marked = 1;
				}
			}


			if(c == '\n') 	numChars = 0;
			else numChars++;

			return 0;
		}
	}

}

