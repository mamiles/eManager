package com.cisco.eManager.eManager.processSequencer.common;

import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Vector;
import java.io.PrintWriter;

/**
 * <p>A thread pool.  Example usage is as follows.</p>
 *
 * <xmp>
 *  import com.cisco.eManager.eManager.processSequencer.common.ThreadPool;
 *
 *     class SomeClass {
 *       ThreadPool mThreadPool;
 *
 *       public SomeClass () {
 *         // The context is some string that identifies this thread
 *         // pool within the system.  All worker threads created by
 *         // the pool are given a name of "context#N" so they can
 *         // be easily identified.
 *         String context = "context";
 *         mThreadPool = new ThreadPool (context);
 *
 *         // Set the maximum number of thread in the thread pool
 *         mThreadPool.maxThreads (10);
 *
 *         // Set the maximum number of waiting threads in the thread pool.
 *         // If there are more than this number of idle threads, the extras
 *         // will die.
 *         mThreadPool.maxWaitingThreads (5);
 *       }
 *
 *       public void doit (int context1, int context2) {
 *         mThreadPool.addRequest (new Job (context1, context2));
 *       }
 *
 *       class Job implements Runnable {
 *         // some context to identify what needs to be done
 *         int mContext1;
 *         int mContext2;
 *
 *         public Job (int context1, int context2) {
 *           mContext1 = context1;
 *           mContext2 = context2;
 *         }
 *
 *         public void run () {
 *           // make use of the context information to do whatever needs
 *           // to be done to actually make the job happen
 *         }
 *       }
 *     }
 * </xmp>
 *
 * @author <a href="mailto:waf@cisco.com">Wayne Feick</a>
 */
public class ThreadPool {
  private static int DEFAULT_MAX_THREADS = 100;
  private static int DEFAULT_MAX_WAITING_THREADS = 10;

  /**
   * The current number of threads.
   */
  private int mNumThreads = 0;

  /**
   * The current number of threads waiting to service a request.
   */
  private int mWaitingThreads = 0;

  /**
   * The maximum number of threads that may be waiting for a request.
   * If this limit is exceeded, some threads will die.
   */
  private int mMaxWaitingThreads = DEFAULT_MAX_WAITING_THREADS;

  /**
   * The maximum number of threads that may be created.
   */
  private int mMaxThreads = DEFAULT_MAX_THREADS;

  /**
   * Queue of requests.
   * @associates Runnable
   * @supplierRole mRequests
   */
  //private Deque mRequests = new Deque ();
  private List mRequests = Collections.synchronizedList(new LinkedList());

  /**
   * High water mark for mRequests.
   */
  private int mRequestHighWater = 0;

  /**
   * Low water mark for mRequests.
   */
  private int mRequestLowWater = 0;

  /**
   * True if request processing is active.
   */
  private boolean mActive = true;

  /**
   * The number of requests processed by this thread pool.  This is
   * incremented each time a new request is taken off the queue and
   * therefore includes requests that are currently executing.
   */
  private int mServicedRequests = 0;

  /**
   * The context string for threads in this pool.
   */
  private String mContext;

  /**
   * The number of the next created worker thread.  Each time a worker
   * thread is created, this is incremented.
   */
  private int mThreadNumber = 0;

  /**
   * Maximum size of the request queue at any given point of time.
   * If this is zer0, max size is unlimited. If this is non-zero,
   * the queue growth is managed depending on the mDiscardOldest flag.
   */
  private int mMaxRequestBufferSize = 0;

  /**
   * If mMaxRequestBufferSize is greater than zero and the number of queued requests is
   * same as mMaxRequestBufferSize : <p>
   * if mDiscardOldest is true, then  the first element in the request queue is dropped
   * and the new request is added at the end of the queue <p>
   * if mDiscardOldest is false, then  the new element is dropped (not added anywhere) <p>
   * <p>
   * If mMaxRequestBufferSize is zero or the number of queued requests is less than
   * mMaxRequestBufferSize, then this flag does not come into play.
   */
  private boolean mDiscardOldest = true;

  /**
   * If there are different processors for different classes, then
   * this table is used to hold the associations
   */
  private Hashtable mProcessors = null;

  /**
   * The processor for the requests that are not associated
   * with any
   */
  private TPRequestProcessor mDefaultRequestProcessor = null;

  /**
   * Create a new thread pool.
   *
   * @param context A context string for threads in this pool.  Each
   *   thread is created with a name made up of this context string
   *   and an appended integer identifier.
   */
  public ThreadPool (String context) {
    mContext = context;
    debug ("created");
  }

  public void setProcessor (TPRequestProcessor tprp) {
    mDefaultRequestProcessor = tprp;
  }

  public synchronized void setProcessor (TPRequestProcessor tprp, Class typeOfRequestObject) {
    if( mProcessors == null) {
      mProcessors = new Hashtable();
    }
    mProcessors.put(typeOfRequestObject.getName(), tprp);
  }

  public TPRequestProcessor defaultProcessor() {
    return mDefaultRequestProcessor;
  }

  public TPRequestProcessor processorForRequest(Object request) {
    if( request == null) return null;

    if ( mProcessors != null && mProcessors.size() > 0) {
	TPRequestProcessor p = (TPRequestProcessor) mProcessors.get(request.getClass().getName());
	if( p != null) return p;
    }

    return mDefaultRequestProcessor;
  }

  public void discardOldest(boolean b) {
    mDiscardOldest = b;
  }

  public boolean discardOldest() {
    return mDiscardOldest;
  }

  public void maxRequestBufferSize(int size) {
    mMaxRequestBufferSize= size;
  }

  public int maxRequestBufferSize() {
    return mMaxRequestBufferSize;
  }


  StatusThread mStatusThread = null;

  /**
   * Start a status thread to periodically write status information
   * about this thread pool to the performance log.
   */
  public synchronized void startStatusThread (long period) {
    if (mStatusThread == null) {
      mStatusPeriod = period;
      mStatusThread = new StatusThread (mContext + ".status");
      mStatusThread.start ();
    }
  }

  /**
   * <p>Set the maximum number of theads.  The thread pool will not
   * allow more than this number of threads to be created.</p>
   *
   * <p>If the maximum is lowered below the current number of threads,
   * waiting threads will die until the current number is equal to the
   * maximum number.</p>
   *
   * <p>If the maximum is raised and there are queued requests,
   * additional threads are created to service those requests.</p>
   *
   * @param n The maximum number of threads.
   */
  public void setMaxThreads (int n) {
    synchronized (mRequests) {
      debug ("setting maxThreads to " + n);

      mMaxThreads = n;

      if (mNumThreads < mMaxThreads) {
	if (mRequests.size () > 0) {
	  // The maximum has been raised and there are outstanding
	  // requests, so start some more workers if possible.
	  createWorkers (mRequests.size ());
	}
      } else if (mNumThreads > mMaxThreads) {
	// There are too many threads, so wake up enough waiting
	// threads (if possible) so they will notice there are too
	// many and die.
	notifyWorkers (mNumThreads - mMaxThreads);
      }
    }
  }

  /**
   * <P>Set the maximum number of threads with specified priority. The
   *  thread pool will not  allow more than this number of threads to
   *  be created.</p>
   *
   *  @param n The maxinum number of threads
   *  @param priority The priorty of worker threads to be created.
   */
  public void setMaxThreads(int n, int priority)
    {
      synchronized (mRequests) {
	debug ("setting maxThreads to " + n);

	mMaxThreads = n;

	if (mNumThreads < mMaxThreads) {
	  if (mRequests.size () > 0) {
	    // The maximum has been raised and there are outstanding
	    // requests, so start some more workers if possible.
	    createWorkers (mRequests.size (), priority);
	  }
	} else if (mNumThreads > mMaxThreads) {
	  // There are too many threads, so wake up enough waiting
	  // threads (if possible) so they will notice there are too
	  // many and die.
	  notifyWorkers (mNumThreads - mMaxThreads);
	}
      }
    }

  /**
   * Get the current maximum number of threads.
   *
   * @return The maximum number of threads.
   */
  public int getMaxThreads () {
    return mMaxThreads;
  }

  /**
   * <p>Set the maximum number of waiting threads.  If more than this
   * number of threads are waiting for something to do, some will die
   * to reduce resource usage.<p>
   *
   * <p>If the maximum is lowered and there are extra waiting threads,
   * they will die immediately.<p>
   *
   * @param n The maximum nuber of waiting threads.
   */
  public void setMaxWaitingThreads (int n) {
    synchronized (mRequests) {
      debug ("setting maxWaitingThreads to " + n);
      mMaxWaitingThreads = n;

      // Wake extra waiting threads (if any) so they will notice they
      // are no longer needed and die.
      notifyWorkers (mWaitingThreads - mMaxWaitingThreads);
    }
  }

  /**
   * Get the maximum allowable number of waiting threads.
   *
   * @return The maximum allowable number of waiting threads.
   */
  public int getMaxWaitingThreads () {
    return mMaxWaitingThreads;
  }

  /**
   * Get the total number of requests serviced so far.  This value is
   * incremented each time a request is taken off the queue and
   * therefore includes requests that are currently executing.
   *
   * @return The total number of requests serviced so far.
   */
  public int getServicedRequests () {
    return mServicedRequests;
  }

  /**
   * Get the current number of threads.  This can be greater than the
   * maximum number of threads if the maximum was lowered while some
   * threads are outstanding.
   *
   * @return The current number of threads.
   */
  public int getNumThreads () {
    return mNumThreads;
  }

  /**
   * Get the current number of threads waiting for something to do.
   *
   * @return The current number of waiting threads.
   */
  public int getWaitingThreads () {
    return mWaitingThreads;
  }

  /**
   * Get the current size of the request queue.
   *
   * @return The current queue size.
   */
  public int getQueueSize () {
    synchronized (mRequests) {
      return mRequests.size ();
    }
  }

  /**
   * Get the queue size high water mark.  This is the maximum number
   * of requests that were pending in the queue at any one point in
   * time.
   *
   * @return The high water mark for the queue.
   */
  public int getQueueHighWater () {
    return mRequestHighWater;
  }

  /**
   * Reset the queue's high water mark to whatever the queue's current
   * size is.
   */
  public void resetQueueHighWater () {
    synchronized (mRequests) {
      mRequestHighWater = mRequests.size ();
    }
  }

  /**
   * Get the queue size low water mark.  This is the minimum number
   * of requests that were pending in the queue at any one point in
   * time.
   *
   * @return The low water mark for the queue.
   */
  public int getQueueLowWater () {
    return mRequestLowWater;
  }

  /**
   * Reset the queue's low water mark to whatever the queue's current
   * size is.
   */
  public void resetQueueLowWater () {
    synchronized (mRequests) {
      mRequestLowWater = mRequests.size ();
    }
  }

  private int numReq = 1;

  /**
   * Add a request to the thread pool.  The request is added to a FIFO
   * queue and serviced by the next available worker thread.
   *
   * @param request The request to add.
   */
  public void addRequest (Object request) {
    synchronized (mRequests) {
	  //System.out.println("Queueing : " + (numReq++));
      if (mWaitingThreads == 0 && mNumThreads < mMaxThreads) {
	// There are no waiting threads and the maximum number of threads
	// hasn't been created yet, so go ahead and create another
	// one.
	createWorkers (1);
      }

      mRequests.add (request);
      //mRequests.addElement (request);
      mRequestHighWater = Math.max (mRequestHighWater, mRequests.size ());
      mRequests.notify ();
    }
  }

  public void addRequestToQueue (Object request) {

    boolean shdAdd = true;

    if( mMaxRequestBufferSize > 0) {
      int sz = mRequests.size();
      if ( sz >= mMaxRequestBufferSize ) {
        if( mDiscardOldest ) {
          mRequests.remove(0);
        } else {
          shdAdd = false;
	}
      }
    }

    if ( shdAdd) {
      mRequests.add( request);
    }
  }

  /**
   * Debugging and status information is written here.
   */
  PrintWriter mLog = new PrintWriter (System.out);

  /**
   * Specify where debugging and status information is to be sent.  By
   * default, debugging information is sent to System.out.
   *
   * @param log Where to send debugging information to
   */
  public void log (PrintWriter log) {
    mLog = log;
  }

  /**
   * Flush the request queue by discarding any unprocessed requests.
   * Any requests that have already begun processing but have not yet
   * finished will continue normally.
   */
  public void flush () {
    synchronized (mRequests) {
      mRequests.clear ();
      //mRequests.removeAllElements ();
    }
  }

  /**
   * True if the thread pool is enabled.  The request queue is only
   * serviced when the thread pool is enabled.
   */
  boolean mEnabled = true;

  /**
   * <p>Change the enabled flag.  If the flag is set to true, the
   * thread pool will process requests.  If the flag is set to false,
   * requests will queue until the pool is enabled again.</p>
   *
   * <p>A newly create pool is enabled.</p>
   *
   * @param flag The value to set the enabled flag to.
   */
  public void enabled (boolean flag) {
    synchronized (mRequests) {
      if (flag != mEnabled) {
	mEnabled = flag;
	if (mEnabled) {
	  // The pool was just enabled, so wake up any waiting threads
	  // and let them know.  Also, if there are more requests than
	  // worker threads and we're allowed to add more, create some
	  // more.
	  notifyWorkers (mRequests.size ());

	  // Create more threads if necessary to clear out the request
	  // queue.
	  createWorkers (mRequests.size () - mWaitingThreads);
	}
      }
    }
  }

  /**
   * Wake up a bunch of worker threads.
   *
   * @param notificationCount The number of workers to notify.
   */
  void notifyWorkers (int notificationCount) {
    synchronized (mRequests) {
      notificationCount = Math.min (notificationCount, mWaitingThreads);
      while (notificationCount > 0) {
	mRequests.notify ();
	--notificationCount;
      }
    }
  }

  /**
   * Create a bunch of worker threads, subject to the maximum
   * allowable.
   *
   * @param createCount The number of workers to create.
   */
  void createWorkers (int createCount) {
    synchronized (mRequests) {
      createCount = Math.min (createCount, mMaxThreads - mNumThreads);
      while (createCount > 0) {
	createCount--;

	mNumThreads++;
	String context = mContext + "#" + mThreadNumber++;
	new WorkerThread (context)
	  .start ();
	debug ("started a new worker thread");
      }
    }
  }

  /**
   * Create a bunch of worker threads, subject to the maximum allowable.
   * All thread are run at specified priority.
   *
   * @param createCount The number of workers to create.
   * @param priority The thread priority to set
   */
  void createWorkers(int createCount, int priority) {
    int p;

    if(priority != Thread.MIN_PRIORITY &&
       priority != Thread.NORM_PRIORITY &&
       priority != Thread.MAX_PRIORITY )
      p = Thread.MIN_PRIORITY;
    else
      p = priority;

    synchronized (mRequests) {
      createCount = Math.min (createCount, mMaxThreads - mNumThreads);

      while (createCount > 0) {
	createCount--;

	mNumThreads++;
	String context = mContext + "#" + mThreadNumber++;

	WorkerThread w = new WorkerThread (context);
	w.setPriority(p);
	w.start();

	debug ("started a new worker thread");
      }
    }
  }

  /**
   * Test if the thread pool is enabled.  If the pool is disabled,
   * requests will queue until the pool is enabled.
   *
   * @return True if the pool is enabled.
   */
  public boolean enabled () {
    return mEnabled;
  }

  /**
   * Stop this thread pool from running any more.  The request queue
   * is flushed and all worker threads are terminated.  Any requests
   * that have already begun processing are finished.
   */
  public void stop () {
    synchronized (mRequests) {
      flush ();
      enabled (false);
      setMaxWaitingThreads (0);
    }
  }

  /**
   * True if debugging is enabled.
   */
  boolean mDebug = false;

  /**
   * Set the debugging flag.
   * @param flag The new value of the flag.
   */
  public void debug (boolean flag) {
    mDebug = flag;
  }

  /**
   * Test the debugging flag.
   * @return The current value.
   */
  public boolean debug () {
    return mDebug;
  }

  /**
   * Called to output a debug message.  If the mDebug flag is set, the
   * message is written to mLog.
   */
  void debug (String msg) {
    if (mDebug) {
      mLog.println ("Debug(ThreadPool:" + mContext + "): " + msg);
      mLog.flush ();
    }
  }

  /**
   * Print out the status of the thread pool, if the debug flag is
   * set.
   */
  public void printStatus () {
    synchronized (mRequests) {
      debug ("num=" + mNumThreads
	     + ", max=" + mMaxThreads
	     + ", waiting=" + mWaitingThreads
	     + ", maxWaiting=" + mMaxWaitingThreads
	     + ", queue=" + mRequests.size ()
	     + ", serviced=" + mServicedRequests
	     + ", high=" + mRequestHighWater
	     + ", low=" + mRequestLowWater
	     );
    }
  }

  /**
   * The ThreadPool's worker thread.
   */
  class WorkerThread extends Thread {
    /**
     * Create a new WorkerThread.
     */
    public WorkerThread (String name) {
      super (name);
      setDaemon (true);
    }

    /**
     * Loop, retrieving requests and servicing them.  If sufficient
     * threads are waiting for requests, this thread will exit.
     */
    public void run () {
      debug ("running");
      Object request;

      while (true) {
	try {
	  request = null;

	  synchronized (mRequests) {
	    if (mWaitingThreads >= mMaxWaitingThreads
		|| mNumThreads > mMaxThreads) {
	      // There are plenty of threads waiting to service requests,
	      // so let this one die.
	      mNumThreads--;
	      debug ("dieing");
	      return;
	    } else {
	      mWaitingThreads++;
	      while (mRequests.size () == 0 && mEnabled) {
		try {
		  debug ("waiting for something to do");
		  mRequests.wait (5000);
		}
		catch (InterruptedException ie) {
		  // ignore
		}

		if (mNumThreads > mMaxThreads) {
		  mNumThreads--;
		  mWaitingThreads--;
		  debug ("dieing");
		  return;
		}
	      }

	      if (mEnabled) {
		mWaitingThreads--;
		request = mRequests.remove (0);
		mServicedRequests++;
		mRequestLowWater = Math.min (mRequestLowWater,
					     mRequests.size ());
		if (mDebug) {
		  debug ("found something to do, queue size is "
			 + mRequests.size ());
		}
	      } else {
		mNumThreads--;
		mWaitingThreads--;
		debug ("dieing");
		return;
	      }
	    }
	  }

	  try {
	    if (request != null) {

	      TPRequestProcessor tp = processorForRequest(request);
	      if( tp != null) {
	        tp.process(request);
	      } else if ( request instanceof Runnable) {
	        ((Runnable)request).run ();
          }
	    }
	  }
	  catch (Exception e) {
	    e.printStackTrace ();
	    System.err.println ("Warning: ThreadPool pool worker thread"
				+ " encountered an exception while"
				+ " processing request.");
	    System.err.println ("Request.toString() = "
				+ request.toString ());
	  }
	}
	catch (Exception e) {
	  e.printStackTrace ();
	  System.err.println ("Warning: Unexpected exception in Thread Pool"
			      + " worker thread");
	}
      }
    }

    void debug (String msg) {
      if (mDebug) {
	mLog.println ("Debug(" + getName () + "): " + msg);
	mLog.flush ();
      }
    }
  }

  /**
   * The period of status messages, when mDebug is enabled.  Default
   * value is 30000ms (30 seconds).
   */
  long mStatusPeriod = 30*1000;

  /**
   * Status thread.  When debugging is enabled, a status thread is
   * created to periodically output status information on the thread
   * pool.
   */
  class StatusThread extends Thread {
    public StatusThread (String name) {
      super (name);
    }

    public void run () {
      while (true) {
	synchronized (mRequests) {
	  System.out.println ("threadpool"
			      + " " + mContext
			      + " max=" + mMaxThreads
			      + " waiting=" + mWaitingThreads
			      + " maxWaiting=" + mMaxWaitingThreads
			      + " queue=" + mRequests.size ()
				   + " serviced=" + mServicedRequests
			      + " high=" + mRequestHighWater
			      + " low=" + mRequestLowWater
			       );
	}

	try {
	  Thread.sleep (mStatusPeriod);
	}
	catch (InterruptedException ie) {
	  // ignore
	}
      }
    }
  }
}

