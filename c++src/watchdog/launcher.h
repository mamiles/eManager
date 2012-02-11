/*
 * @(#)java.h	1.15 00/07/19
 *
 * Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2000 Sun Microsystems, Inc. Tous droits réservés.
 *
 * This software is the proprietary information of Sun Microsystems, Inc.
 * Use is subject to license terms.
 */

#ifndef _LAUNCHER_H
#define _LAUNCHER_H

/*
 * Get system specific defines.
 */
#include "jni.h"
#include <limits.h>

#define PATH_SEPARATOR		':'
#define FILE_SEPARATOR		'/'
#define MAXPATHLEN		PATH_MAX

#define JVM_CFG "/lib/jvm.cfg"

#ifdef JAVA_ARGS
/*
 * ApplicationHome is prepended to each of these entries; the resulting
 * strings are concatenated (seperated by PATH_SEPARATOR) and used as the
 * value of -cp option to the launcher.
 */
#ifndef APP_CLASSPATH
#define APP_CLASSPATH        { "/lib/tools.jar", "/lib/tools_l10n.jar", "/classes" }
#endif
#endif

#ifdef HAVE_GETHRTIME
/*
 * Support for doing cheap, accurate interval timing.
 */
#include <sys/time.h>
#define CounterGet()           	  (gethrtime()/1000)
#define Counter2Micros(counts) 	  (counts)
#else
#define CounterGet()		  (0)
#define Counter2Micros(counts)	  (1)
#endif /* HAVE_GETHRTIME */

/*
 * Pointers to the needed JNI invocation API, initialized by LoadJavaVM.
 */
typedef jint (JNICALL *CreateJavaVM_t)(JavaVM **pvm, void **env, void *args);
typedef jint (JNICALL *GetDefaultJavaVMInitArgs_t)(void *args);

typedef struct {
    CreateJavaVM_t CreateJavaVM;
    GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs;
} InvocationFunctions;

/*
 * Protoypes for launcher functions in the system specific java_md.c.
 */
jboolean
GetJVMPath(const char *jrepath, const char *jvmtype,
	   char *jvmpath, jint jvmpathsize);

const char *
ReadJVMLink(const char *jrepath, const char *jvmtype,
	    char* knownVMs[], int knownVMsCount);

jboolean
GetJREPath(char *path, jint pathsize);

jboolean
LoadJavaVM(const char *jvmpath, InvocationFunctions *ifn);

void
GetXUsagePath(char *buf, jint bufsize);

jboolean
GetApplicationHome(char *buf, jint bufsize);

/*
 * Make launcher spit debug output.
 */
extern jboolean debug;

#endif /* _JAVA_H_ */
