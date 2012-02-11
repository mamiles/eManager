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
#include <sys/types.h>
#include <procfs.h>
#include <dirent.h>
#include <sys/stat.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>
#include <stdio.h>
#include <pwd.h>
#include <grp.h>
#include <ctype.h>
#include <string.h>
#include <com_cisco_eManager_eManager_processSequencer_common_UnixStdlib.h>

#define NUM_SIGNALS 3

static int signalNumbers[] = {
		SIGTERM,		/* software termination signal from kill */
		SIGUSR1,		/* user defined signal 1 */
		SIGUSR2		    /* user defined signal 2 */
};
static int signalCounts[NUM_SIGNALS];
static int noticedCounts[NUM_SIGNALS];
static int usedSignals[NUM_SIGNALS];

static struct sigaction action;

static void sigHandler (int signum) {
	printf("Caught signal.... %d\n", signum);	
	for (int i = 0; i < NUM_SIGNALS; i++) {
		if (signalNumbers[i] == signum) {
			signalCounts[i]++;
		}
	}
}

extern "C" {

	int isCmdLineMatch (const char* cmdLine1, const char* cmdLine2);

	/*
	 * Class:		 com_cisco_vpnsc_util_UnixStdlib
	 * Method:		waitForSignal
	 * Signature: ()I
	 */

	JNIEXPORT jint JNICALL Java_com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_waitForSignal
		(JNIEnv *env, jclass clazz, jintArray sigArr)
    {
		static int virgin = 1;
		if (virgin) {
			virgin = 0;

			action.sa_handler = sigHandler;

			jsize len = env->GetArrayLength(sigArr);

			if( len == 0 ) {
				for (int i = 0; i < NUM_SIGNALS; i++) {
					signalCounts[i] = 0;
					noticedCounts[i] = 0;
					usedSignals[i] = 1;
					int result = sigaction(signalNumbers[i], &action, (struct sigaction*) NULL);
					if( result < 0) {
						printf("Failed to register signal : %d -- %d\n", i, signalNumbers[i]);
					}
				}
			} else {
  				jint *sigs = env->GetIntArrayElements(sigArr, 0);

				for (int i = 0; i < NUM_SIGNALS; i++) {
					signalCounts[i] = 0;
					noticedCounts[i] = 0;
					usedSignals[i] = 0;
				}

				for (int j = 0; j < len; j++) {
					int signum = sigs[j];
					for (int i = 0; i < NUM_SIGNALS; i++) {
						if (signalNumbers[i] == signum) {
							//printf("Registering signal : %d -- %d\n", i, signalNumbers[i]);
							int result = sigaction(signalNumbers[i], &action, (struct sigaction*) NULL);
							if( result < 0) {
								printf("Failed to register signal : %d -- %d\n", i, signalNumbers[i]);
							} else {
								usedSignals[i]=1;
							}
							break;
						}
					}
				}
  				env->ReleaseIntArrayElements(sigArr, sigs, 0);
			}
		}

		while (1) {
			for (int i = 0; i < NUM_SIGNALS; i++) {
				if( usedSignals[i] == 1) {
					if (signalCounts[i] != noticedCounts[i]) {
						noticedCounts[i] = signalCounts[i];
						return signalNumbers[i];
					}
				}
			}
			sleep (1);
		}
	}

	JNIEXPORT void JNICALL 
	    Java_com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_nativeKill (JNIEnv *env, jclass,
	    jlong _processID)
    {
		pid_t mainPID = (pid_t) _processID;
		uid_t uid = getuid ();
		gid_t gid = getgid ();

		char mainPIDDir[255];
		sprintf (mainPIDDir, "/proc/%lu", mainPID);
		struct stat mainPIDStatBuf;
		int status = stat(mainPIDDir, &mainPIDStatBuf);
		if( status != 0) {
			kill(mainPID, SIGTERM);
			return;
		}

		int INIT_CAP = 16;

		pid_t *childPIDs = (pid_t* ) calloc(INIT_CAP, sizeof (pid_t)) ;
		int allocated = INIT_CAP;
		int index = 0;

		// Step through the current set of processes

	    DIR* dirp = opendir ("/proc");
		if (dirp != NULL) {
			struct dirent* dp;
			char direntBuf[10240];

			while ((dp = readdir_r (dirp, (struct dirent*) &direntBuf)) != NULL) {

				// Look for processes with same user and group permissions
			    char dirname[80];
				sprintf (dirname, "/proc/%s", dp->d_name);
				struct stat statbuf;
				int rc = stat (dirname, &statbuf);
				if (isdigit (dp->d_name[0])
				    && statbuf.st_uid == uid
				    && statbuf.st_gid == gid) 
				{
				    pid_t pid = atol (dp->d_name);
					if( pid == mainPID ) continue;

					char buf[10240];
					sprintf (buf, "%s/psinfo", dirname);
					int psinfoFd = open (buf, O_RDONLY);

					if (psinfoFd >= 0) {
						psinfo_t psinfo;
						if (read (psinfoFd, &psinfo, sizeof (psinfo)) == sizeof (psinfo)
						    && psinfo.pr_ppid == mainPID) 
						{
							if( (index+1) > allocated) {
								childPIDs = (pid_t* ) realloc(childPIDs, sizeof (pid_t) * (allocated + INIT_CAP));		
								allocated += INIT_CAP;
							}
							childPIDs[index++] = pid;
							//printf("index : %d ; allocated : %lu; pid : %ul\n", index, allocated, pid);
						}
					}
					close(psinfoFd);
				}
			}
			closedir (dirp);
		}

		for(int i=0; i < index; ++i) {
			kill(childPIDs[i], SIGTERM);
			printf("killing  : %lu \n", childPIDs[i]);
		}
	
		kill(mainPID, SIGTERM);
		printf("killing  : %lu \n", mainPID);
		free ( childPIDs );
	}


	/*
	 * Class:		 com_cisco_vpnsc_util_UnixStdlib
	 * Method:		getProcessID
	 * Signature: (Ljava/lang/String;)Ljava/lang/String;
	 */

	JNIEXPORT jlong JNICALL 
	    Java_com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_getProcessID (JNIEnv *env, jclass,
	    jstring _cmdLine)
    {
		const char* cmdLine = env->GetStringUTFChars (_cmdLine, 0);

		pid_t myPid = getpid ();
		uid_t uid = getuid ();
		gid_t gid = getgid ();

		// Step through the current set of processes

	    DIR* dirp = opendir ("/proc");
		if (dirp != NULL) {
			struct dirent* dp;
			char direntBuf[10240];

			while ((dp = readdir_r (dirp, (struct dirent*) &direntBuf)) != NULL) {

				// Look for processes with same user and group permissions
			    char dirname[80];
				sprintf (dirname, "/proc/%s", dp->d_name);
				struct stat statbuf;
				int rc = stat (dirname, &statbuf);
				if (isdigit (dp->d_name[0])
				    && statbuf.st_uid == uid
				    && statbuf.st_gid == gid) 
				{
				    pid_t pid = atol (dp->d_name);

					char pidCmdLine[10240];
					char buf[10240];
					sprintf (buf, "%s/psinfo", dirname);
					int psinfoFd = open (buf, O_RDONLY);

					if (psinfoFd >= 0) {
						psinfo_t psinfo;
						if (read (psinfoFd, &psinfo, sizeof (psinfo)) == sizeof (psinfo)
						    && psinfo.pr_ppid == myPid) 
						{
							// This process is a child of mine, so identify its command line
						    int argc = psinfo.pr_argc;
							uintptr_t argvPtr = psinfo.pr_argv;

							// Open the address space file
						    sprintf (buf, "%s/as", dirname);
							int asFd = open (buf, O_RDONLY);
							if (asFd >= 0) {
								pidCmdLine[0] = 0;
								char* argv[100];
								int argvSize = argc * sizeof argv[0];
								if (lseek (asFd, (long) argvPtr, SEEK_SET) == (long) argvPtr
								    && read (asFd, (void*) &argv, argvSize) == argvSize) 
								{
									// argv now contains pointers into the address space
	    							for (int i = 0; i < argc; i++) {
										// read next string
		    							if (lseek (asFd, (long) argv[i], SEEK_SET) == (long) argv[i]
		    								&& read (asFd, (void*) buf, sizeof (buf)) > 0) 
										{
											// concat into command line
			    							if (i > 0) {
												strcat (pidCmdLine, " ");
											}
											strcat (pidCmdLine, buf);
											
										}
									}
								}
								
								if (isCmdLineMatch (cmdLine, pidCmdLine)) {
									close (asFd);
									close (psinfoFd);
									env->ReleaseStringUTFChars (_cmdLine, cmdLine);
									closedir (dirp);
									return pid;
								}

								close (asFd);
							}
						}
						close (psinfoFd);
					}
				}
			}
			closedir (dirp);
		}

		env->ReleaseStringUTFChars (_cmdLine, cmdLine);
		return 0;
	}

	int isCmdLineMatch (const char* cmdLine1, const char* cmdLine2)
	{
		// Remove any path component from the beginnings and compare
	    const char* p1 = cmdLine1;
		while (*p1 && *p1 != ' ') {
			p1++;
		}
		while (p1 > cmdLine1 && *p1 != '/') {
			p1--;
		}
		if (*p1 == '/') {
			p1++;
		}

		const char* p2 = cmdLine2;
		while (*p2 && *p2 != ' ') {
			p2++;
		}
		while (p2 > cmdLine2 && *p2 != '/') {
			p2--;
		}
		if (*p2 == '/') {
			p2++;
		}
		if (strcmp (p1, p2) == 0) {
			return 1;
		}

		// If one is longer than the other, try comparing just the common
	    // last part.	This handles the cases where the user's shell is
		// prefixed to the command line.
		p1 = cmdLine1;
		p2 = cmdLine2;
		int len1 = strlen (cmdLine1);
		int len2 = strlen (cmdLine2);
		if (len1 < len2) {
			p2 += (len2 - len1);
		} else {
			p1 += (len1 - len2);
		}
		if (strcmp (p1, p2) == 0) {
				return 1;
		}
			
		return 0;
	}
}
		
