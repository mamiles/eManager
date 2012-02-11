#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
#include <com_cisco_eManager_eManager_processSequencer_common_IOUtil.h>


/*
 * Class:		 com_cisco_vpnsc_util_IOUtil
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT jint JNICALL Java_com_cisco_eManager_eManager_processSequencer_common_IOUtil_RedirectIO
  (JNIEnv *env, jobject thisObj, jstring _fileName, jint _curFD)
{
	const char* fileName = env->GetStringUTFChars (_fileName, 0);
	freopen ("/dev/null", "r", stdin); 

	int toFile = 1;

	if( strcmp(fileName,"") == 0) {
		toFile = 0;
	}	

	if( _curFD != -1 ) {
		//fprintf(stderr, "Will not close : %d\n", _curFD);
		close((int)_curFD);
	}

	int newFD = -1;

	if (toFile == 1) {
		int curFileFd = open ( fileName, O_RDONLY);
		
		if( curFileFd != -1)  {
			char path[PATH_MAX];
			sprintf(path, "%s.bak", fileName);
			rename(fileName, path);
			close(curFileFd);
		}

		newFD = open (fileName, O_CREAT|O_TRUNC|O_WRONLY, 0666);
		dup2 (newFD, fileno(stdout));
		setbuf(stdout, NULL);

		dup2 (newFD, fileno(stderr));
		setbuf(stderr, NULL);

		//fprintf(stderr, "Will not close : %d\n", _curFD);
		//fprintf(stderr, "Curfile fd: %d\n", curFileFd);
		//fprintf(stderr, "++New fd: %d\n", newFD);

	} else {
		freopen ("/dev/null", "w", stdout);
		freopen ("/dev/null", "w", stderr);

		newFD = -1;
		//fprintf(stderr, "--Will not close : %d\n", _curFD);
		//fprintf(stderr, "--New fd: %d\n", newFD);
	}

	fprintf(stderr, "Completed redirection of stdout\n");
	fprintf(stdout, "Completed redirection of stderr\n");
	fflush(stdout); fflush(stderr);

	env->ReleaseStringUTFChars (_fileName, fileName);

	return (jint) newFD;
}
