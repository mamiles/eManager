#include "watchdog.h"
#include <unistd.h>
#include <ctype.h>
#include <sys/resource.h>
#include <stdlib.h>
#include <string.h>
#include <pwd.h>
#include <stdarg.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/param.h>
#include <pwd.h>
#include <grp.h>
#include <signal.h>
#include <errno.h>
#include <dlfcn.h>

WatchDog* WatchDog::watchdogInstance = NULL;
Boolean debugFlag = getenv ("WATCHDOG_DEBUG") == NULL ? False : True;

int main (int argc, char** argv)
{
	WatchDog (argc, argv);
}

WatchDog::WatchDog (int argc, char** argv)
{
	watchdogInstance = this;

	mainClass = "";
	logFileName = NULL;
	daemonFlag = True;
	fdLimit = -1;
	classOptionIndex=argc;

	checkEnv ();

	jvmArgs.version = JNI_VERSION_1_2;

	optionIndex = 0; // 0 is reserved for classpath
	options[optionIndex].optionString = NULL;
	++optionIndex;

	options[optionIndex].optionString = "-Djava.compiler=NONE";		 // Disable jit
	++optionIndex;

	options[optionIndex].optionString = "exit";
	options[optionIndex].extraInfo = jvmExitHook;
	++optionIndex;

	options[optionIndex].optionString = "abort";
	options[optionIndex].extraInfo = jvmAbortHook;
	++optionIndex;

	// * We can catch printfs from the jvm
	options[optionIndex].optionString = "vfprintf";
	options[optionIndex].extraInfo = jvmVFPrintfHook;
	++optionIndex;

	runDir = ENV_EM_HOME;

	parseArgs (argc, argv);

	// * Increase any limits
	setLimits ();

	// * Do the standard daemon stuff (detach from tty, create new process group, etc.)
	daemon (0, runDir);

	debug ("Before createJVM: Num Options : %d\n", optionIndex);

	if( debugFlag ) {
		for ( int y=0; y < optionIndex; ++y) {
			debug("%d %s\n", y, options[y].optionString);
		}
	}

	// * Create the Java VM
	jvmArgs.options = options;
	jvmArgs.nOptions = optionIndex;
	jvmArgs.ignoreUnrecognized = JNI_TRUE;

	JavaVM *jvm = NULL;
	JNIEnv *env = NULL;
	jint res = JNI_CreateJavaVM (&jvm, (void **)&env, &jvmArgs);
	if (res < 0) {
		fprintf (stderr, "Can't create Java VM\n");
		exit (1);
	}

	debug ("After createJVM\n");

	// * Locate the main class.
	jclass cls = env->FindClass (mainClass);
	if (cls == 0) {
		fprintf (stderr, "Can't find class '%s'\n", mainClass);
		exit (1);
	}

	// * Locate the "static void main (String[] args)" method.
	jmethodID mid = env->GetStaticMethodID (cls, "main",
						"([Ljava/lang/String;)V");
	if (mid == 0) {
		fprintf (stderr, "Can't find Prog.main\n");
		exit (1);
	}

	debug ("After getStaticMethod\n");

	// * Build the argument list
	jstring jstr = env->NewStringUTF ("");
	if (jstr == 0) {
		fprintf (stderr, "Out of memory\n");
		exit (1);
	}

	int numArgs = argc - classOptionIndex;

	if( numArgs < 0) numArgs = 0;
	else {
		for( int ind = classOptionIndex; ind < argc; ++ind) {
			if(argv[ind] == NULL) --numArgs;
		}
	}
	
	jobjectArray args = env->NewObjectArray (numArgs,
						 env->FindClass ("java/lang/String"),
						 jstr);
	if (args == 0) {
		fprintf (stderr, "Out of memory\n");
		exit (1);
	}

	if( numArgs > 0) {
		int jargIndex=0;

		int argIndex = classOptionIndex;
		for ( ; argIndex < argc; argIndex++) {

			if (argv[argIndex] != NULL) {
				jstr = env->NewStringUTF (argv[argIndex]);
				if (jstr == 0) {
					fprintf (stderr, "Out of memory\n");
					exit (1);
				}
				env->SetObjectArrayElement (args, jargIndex, jstr);
				debug ("argv[%u] = '%s'\n", argIndex, argv[argIndex]);
				jargIndex++;
			}
		}
	}

	debug ("Before callStaticVoidMethod\n");

	// Call the the class' main method.
	env->CallStaticVoidMethod (cls, mid, args);

	debug ("After callStaticVoidMethod\n");

	// Detect any exception that occurred
	jthrowable exc = env->ExceptionOccurred ();
	if (exc) {
		env->ExceptionDescribe ();
	}

	sleep (5000);

	// Clean up
	debug ("Destrying JVM\n");
	jvm->DestroyJavaVM ();
}

void WatchDog::parseArgs (int argc, char** argv) {

	int CLASSPATH_OPTION = 0;

	debug ("Top of parseArgs, argc = %u\n", argc);

	int classOptionsIndex= -1;

	for (int i = 1; i < argc; i++) {

		debug ("** curr ** watchdogArg[%u] = '%s'\n", i, argv[i]);

		if( debugFlag ) {
			if (i + 1 < argc) {
				debug ("** next ** watchdogArg[%u] = '%s'\n", i+1, argv[i+1]);
			}
		}
		
		if (*argv[i] != '-') {
			mainClass = argv[i];
			classOptionIndex = i+1;
			break;

		} else if(strlen(argv[i]) > 2 && 
				(strncmp(argv[i], "-X", 2) ==0) )
		{ 		

			//if it is an extended option.. just pass it on
			debug("*** extended option %s\n", argv[i]);
			options[optionIndex++].optionString = argv[i];

		} else if (strcmp (argv[i], "-fds") == 0) {

			if (i >= argc - 1) {
				fprintf (stderr, "No number give for -fds option");
				exit (1);
			}
			fdLimit = atoi (argv[++i]);

		} else if (strcmp (argv[i], "-wdlog") == 0) {

			if (i >= argc - 1) {
				fprintf (stderr, "No file name given for -wdlog option\n");
				exit (1);
			}
			logFileName = argv[++i];

		} else if (strcmp (argv[i], "-nodaemon") == 0) {
			fprintf (stderr, "Setting daemonFlag=%d\n", False);
			daemonFlag = False;
		} else if (strncmp (argv[i], "-D", 2) == 0) {
			properties.addProperty (argv[i] + 2);
		} else if (strncmp (argv[i], "-verbose", 8) == 0) {
			options[optionIndex++].optionString = argv[i];
		} else if (strcmp (argv[i], "-classpath") == 0) {
			if (i >= argc - 1 || argv[i+1] == NULL) {
				fprintf (stderr, "No class path given for -classpath option\n");
				usage ();
			}

			int allocLength = 32;//for -Djava.class.path etc..
			allocLength += strlen(argv[i+1]);
			char *classPath = new char[allocLength];
			sprintf (classPath, "-Djava.class.path=%s", argv[i + 1]);

			options[CLASSPATH_OPTION].optionString = classPath;
			debug("classpath 1 : %s\n", options[CLASSPATH_OPTION].optionString);
			++i;

		} else if (strcmp (argv[i], "-cp") == 0) {
			if (i >= argc - 1 || argv[i+1] == NULL) {
				fprintf (stderr, "No class path given for -cp option\n");
				usage ();
			}
			if (options[CLASSPATH_OPTION].optionString == NULL) {
				int allocLength = 32;//for -Djava.class.path etc..
				allocLength += strlen(argv[i+1]);
				char *classPath = new char[allocLength];
				sprintf (classPath, "-Djava.class.path=%s", argv[i + 1]);

				options[CLASSPATH_OPTION].optionString = strdup(classPath);
				debug("classpath 2 : %s\n", options[CLASSPATH_OPTION].optionString);
			} else {
				int allocLength = 32; 
				allocLength += strlen (options[CLASSPATH_OPTION].optionString);
				allocLength += strlen(argv[i+1]);

				char* newClassPath = new char [allocLength];
				// Prepend this path to the existing one after dropping the latter's "-Djava.." prefix
				sprintf (newClassPath, "-Djava.class.path=%s%c%s",
		 				argv[i+1], PATH_SEPARATOR, options[CLASSPATH_OPTION].optionString + 18);
				options[CLASSPATH_OPTION].optionString = newClassPath;
				debug("classpath 3 : %s\n", options[CLASSPATH_OPTION].optionString);
			}
			++i;
		} else if (strcmp (argv[i], "-dir") == 0) {
			if (i >= argc - 1) {
				fprintf (stderr, "No directory given for -dir option\n");
				usage ();
			}
			runDir = argv[++i];
		}
	}



	if (!properties.hasProperty ("-Dem.bootdir")) {
		char tmpbuf[1024];
		sprintf (tmpbuf, "em.bootdir=%s%cconfig%cprocessSequencer%c",
			ENV_EM_HOME, DIRECTORY_SEPARATOR, DIRECTORY_SEPARATOR, DIRECTORY_SEPARATOR);
		properties.addProperty (tmpbuf);
	} 

	// * Tell the Java VM what the properties are.

	int numProps = properties.getNumProperties();
	char **allProps = properties.getProperties();
	for(int ind=0; ind < numProps; ++ind)
	{
		options[optionIndex++].optionString = allProps[ind];
	}

	// * Fix up the main class
	for (char* q = mainClass; *q; q++) {
		if (*q == '.') {
			*q = DIRECTORY_SEPARATOR;
		}
	}
	debug ("main class = %s\n", mainClass);
}


/**
 * daemon
 *
 * Do everything necessary to make this a background daemon process.
 *
 * This code is copied almost verbatim from "UNIX Network Programming"
 * by W. Richard Stevens, Section 2.6 entitled "Daemon Processes".
 */

void WatchDog::daemon (int ignsigcld, char* chdirLocation)
{
	if (!daemonFlag) {
		debug ("Not running as a daemon\n");
		return;
	}

	int fd;
	int childPid;

	if (getppid () != 1) {

#ifdef SIGTTOU
		signal (SIGTTOU, SIG_IGN);
#endif
#ifdef SIGTTIN
		signal (SIGTTIN, SIG_IGN);
#endif
#ifdef SIGTSTP
		signal (SIGTSTP, SIG_IGN);
#endif

		/*
		 * If we were not started in the background, fork and let the
		 * parent exit.	This also guarantees the first child is not a
		 * process group leader.
		 */
		childPid = fork ();
		if (childPid < 0) {
			fprintf (stderr, "Can't fork first child");
			exit (1);
		} else if (childPid > 0) {
			exit (0); // parent
		}

		/*
		 * First child process.	Disassociate from controlling terminal
		 * and process group.	Ensure the process can't reacquire a new
		 * controlling terminal.
		 */
		if (setpgrp () == -1) {
			fprintf (stderr, "Can't change process group");
			exit (1);
		}
		signal (SIGHUP, SIG_IGN); // immune from process group leader death

		childPid = fork ();
		if (childPid < 0) {
			fprintf (stderr, "Can't fork second child");
		} else if (childPid > 0) {
			exit (0); // first child
		}
	}

	// Close any open file descriptors.
	for (fd = 0; fd < NOFILE; fd++) {
		close (fd);
	}
	errno = 0; // probably got set to EBADF from a close

	// Create a new stdin, stdout and stderr

	freopen ("/dev/null", "r", stdin);
	if (logFileName) {
		open (logFileName, O_CREAT|O_TRUNC|O_WRONLY, 0666);
		dup (1);
		setbuf (stdout, NULL);
		setbuf (stderr, NULL);
	} else {
		freopen ("/dev/null", "w", stdout);
		freopen ("/dev/null", "w", stderr);
	}

	// * Move the current directory to the specified directory, to make
	// * sure we're not sitting someplace we shouldn't be.
	// *
	// * The code from the book used "/", but I'm going to use ECSP_HOME instead.

	if (chdirLocation != NULL) {
		chdir (chdirLocation);
	}

	// Clear any inherited file mode creation mask.
	umask (0);

	// * See if the caller isn't interested in the exit status of its
	// * children, and doesn't want to have them become zombies and clog
	// * up the system.
	// *
	// * With System V all we need do is ignore the signal.	

	if (ignsigcld) {
		signal (SIGCLD, SIG_IGN);
	}

	debug ("All that daemon stuff is done\n");
}


/**
 * debug
 *
 * Output debug information.
 */
void debug (char* fmt, ...)
{
	if (debugFlag) {
		va_list args;
		va_start (args, fmt);
		vfprintf (stderr, fmt, args);
		va_end (args);
		fflush(stderr);
	}
}


void WatchDog::usage ()
{
	fprintf (stderr,
		 "Usage: watchdog [-options]\n"
		 "Options:\n"
		 "		-?, -help		          print out this message\n"
		 "      \n"
		 "		-nodaemon                 run watchdog as a foreground process\n" 
		 "		-fds N				      set maximum number of file descriptors\n"
		 "      -dir <run directory>      the application home directory\n"
		 "      -wdlog <log file name>    the log file name for watchdog output\n"
		 "      \n"
		 "      -verbose[:class|gc|jni]   verbosity options for the JVM\n"
		 "		-D<name>=<value>	      set a system property\n"
		 "		-classpath <path>         set class path to <path>\n"
		 "		-cp <path>		          prepend <path> to base class path\n"
		 "		-X<option>                any extended option supported by the JVM\n");
	exit (1);
}


void WatchDog::jvmExitHook (jint code)
{
	watchdogInstance->jvmExit (code);
}

void WatchDog::jvmExit (jint code)
{
	shutdownNow ();
}

void WatchDog::jvmAbortHook ()
{
	watchdogInstance->jvmAbort ();
}

void WatchDog::jvmAbort ()
{
	shutdownNow ();
}


jint WatchDog::jvmVFPrintfHook (FILE* fp, const char* format, va_list args)
{
	return watchdogInstance->jvmVFPrintf (fp, format, args);
}

jint WatchDog::jvmVFPrintf (FILE* fp, const char* format, va_list args)
{
	return vfprintf (stderr, format, args);
}


void WatchDog::shutdownNow ()
{
	/*
	 * Send a SIGKILL to all processes in this process group.	Since the
	 * watchdog creates a new process group when it starts, this makes
	 * sure everything is stopped.
	 */
	if (daemonFlag) {
		kill (0, SIGKILL);
		sleep (5);
	}
	exit (1);
}


void WatchDog::checkEnv ()
{
	/*
	 * We've had some problems with ksh scripts reading people's .kshrc
	 * and messing with environment variables.	This will stop that from
	 * happening.
	 */
	putenv ("ENV=");

	ENV_EM_HOME = getenv ("EM_HOME");
	if (ENV_EM_HOME == NULL) {
		fprintf (stderr, "EM_HOME environment variable should  be set\n");
		fprintf (stderr, "Watchdog cannot start without this variable set properly\n");
		exit (1);
	}
	debug ("EM_HOME=%s\n", ENV_EM_HOME);
}


void WatchDog::setLimits ()
{
	struct rlimit limits;
	int rc = getrlimit (RLIMIT_NOFILE, &limits);
	if (rc < 0) {
		int e = errno;
		fprintf (stderr, "Error: getrlimit (RLIMIT_NOFILE) failed: %s\n",
			 strerror (e));
	} else {
		limits.rlim_cur =
			(fdLimit >= 0 && fdLimit <= limits.rlim_max) ? fdLimit : limits.rlim_max;

		// Make sure it doesn't go beyond 1024 because there is a bug with
		// some part of the system.	I don't remember off hand what the
		// problem was, but there's a ddts assigned to Aparna that
		// describes the problem.

		if (limits.rlim_cur > 1024) {
			limits.rlim_cur = 1024;
		}

		rc = setrlimit (RLIMIT_NOFILE, &limits);
		if (rc < 0) {
			int e = errno;
			fprintf (stderr, "Error: setrlimit (RLIMIT_NOFILE) failed: %s\n",
				 strerror (e));
		}
	}
}

