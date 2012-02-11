package com.cisco.eManager.eManager.processSequencer.common;

/**
 * <p>Title: Process Sequencer / Watchdog</p>
 * <p>Description: eManger</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Marvin Miles
 * @version 1.0
 */

import com.cisco.eManager.eManager.processSequencer.common.PSRuntimeException;

/**
 * Exception  thrown when the component or a property in
 * a component do not exist in the configuration data.
 * to be implemented by the callback objects for DCPLibrary.
 * When components register with DCPLibrary, they have to provide a
 * callback object that implements this interface.
 * @see DCPLib#registerComponent(String, DCPCallback)
 */

public class NoSuchProperty extends PSRuntimeException {
        private String mPropertyPath;
        private String mReason;

        public NoSuchProperty(String path, String reason) {
                super(reason);
                mPropertyPath = path;
                mReason = reason;
        }

        public NoSuchProperty(String path)
        {
                this(path, "Not found");
        }

        public String getMessage() {
                return mReason + " : " + mPropertyPath;
        }

        public String getPropertyName() {
                return mPropertyPath;
        }

        public String toString()
        {
                return getMessage();
        }
}


