/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_cisco_eManager_eManager_processSequencer_common_UnixStdlib */

#ifndef _Included_com_cisco_eManager_eManager_processSequencer_common_UnixStdlib
#define _Included_com_cisco_eManager_eManager_processSequencer_common_UnixStdlib
#ifdef __cplusplus
extern "C" {
#endif
#undef com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_SIGTERM
#define com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_SIGTERM 15L
#undef com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_SIGUSR1
#define com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_SIGUSR1 16L
#undef com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_SIGUSR2
#define com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_SIGUSR2 17L
/*
 * Class:     com_cisco_eManager_eManager_processSequencer_common_UnixStdlib
 * Method:    waitForSignal
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL Java_com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_waitForSignal
  (JNIEnv *, jclass, jintArray);

/*
 * Class:     com_cisco_eManager_eManager_processSequencer_common_UnixStdlib
 * Method:    nativeKill
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_nativeKill
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_cisco_eManager_eManager_processSequencer_common_UnixStdlib
 * Method:    getProcessID
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_cisco_eManager_eManager_processSequencer_common_UnixStdlib_getProcessID
  (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif
#endif
