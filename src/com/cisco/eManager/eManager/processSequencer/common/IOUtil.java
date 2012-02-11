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

/**
 * Native methods for IO redirect
 */
public class IOUtil {

	private static IOUtil msInstance;

	private static final int INIT = 0;
	private static final int FILE = 1;
	private static final int DEV_NULL = 2;

	private static final int REINIT_OP = 100;
	private static final int TOGGLE_OP = 200;

	private int mState;
	private String mFileName;
	private SignalWatcher mSignalWatcher;

	private int mFileDes = -1;

	private IOUtil() {
		System.loadLibrary("RedirectIOJNI");
		mState = INIT;
	}

	private synchronized void changeState(int op) {
		if( mState == INIT ) {
			return;
		}

		if (op == TOGGLE_OP ) {

			switch ( mState ) {

				case FILE :	//currently redirected to some file
					System.out.println("Will stop logging now... Use SIGUSR2 to start again");
					redirect(""); //redirect to null
					break;
				case DEV_NULL : //currently redirected to /dev/null
					if ( mFileName != null) { //if a file name was set up
						redirect(mFileName);
						System.out.println("Started logging again... Use SIGUSR2 to stop");
					}
					break;
			}
		} else if (op == REINIT_OP ) {

			if( mState == FILE ) { //re-init makes sense only for FILEs
				if ( mFileName != null) { //if a file name was set up
					redirect(mFileName);
				}
			}
		}
	}

	private class SignalWatcher extends Thread {

		SignalWatcher() {
			super("Signal-Watcher");
		}

		public void run() {
			//int sigs[] = { UnixStdlib.SIGUSR1, UnixStdlib.SIGUSR2 };
			int sigs[] = { UnixStdlib.SIGUSR2 };
			while ( true ) {
				int signal = UnixStdlib.waitForSignal(sigs);
				if( signal == UnixStdlib.SIGUSR2) {
					changeState(TOGGLE_OP);
				}
				//else {
					//changeState(REINIT_OP);
				//}
			}
		}
	}

	private synchronized void redirect(String fileName) {
		if (fileName == null) fileName = ""; ///dev/null
		if(! fileName.equals("")) mFileName = fileName; //store for later use

		mFileDes = RedirectIO(fileName, mFileDes);
		mState = (fileName.equals("")) ? DEV_NULL : FILE;

		if ( mSignalWatcher == null) {
			mSignalWatcher = new SignalWatcher();
			mSignalWatcher.start();
		}
	}

	private synchronized int getState() {
		return mState;
	}

	private native int RedirectIO(String fileName, int curFD);

	/**
	 * Redirects stderr/stdout to the file name passed in
	 * @param fileName name of the file to redirect the out/err streams to
	 */
	public synchronized static void redirectIOStreams(String fileName) {
		IOUtil iou = getInstance();
		int state = iou.getState();
		iou.redirect(fileName);
		if( state == INIT ) {
			System.out.println("Started logging... Use SIGUSR2 to stop");
		/**** To start in non-logging mode uncomment this...
			iou.changeState(TOGGLE_OP);
		***/
		}
	}

	private synchronized static IOUtil getInstance() {
		if( msInstance == null) msInstance = new IOUtil();
		return msInstance;
	}

	public static void main(String args[]) {

		try {
			IOUtil.getInstance().redirectIOStreams("ioutil.test");

			long counter = 0;

			while(true) {

				System.out.println("Foo bar...." + counter);
				++counter;
				try {
					Thread.sleep(500);
				} catch (Exception ex){}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

