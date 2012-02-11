#include <jni.h>

#define PATH_SEPARATOR_STR ":"
#define PATH_SEPARATOR ':'
#define DIRECTORY_SEPARATOR_STR "/"
#define DIRECTORY_SEPARATOR '/'

enum Boolean {
		False = 0,
		True = !False
};

void debug (char* fmt, ...);

class Properties {
	private:

		int maxProperties;
		char** properties;
		int numProperties;

		void increaseProperties ();

	public:
		Properties ();
		~Properties ();
		void addProperty (char* property);
		char** getProperties ();
		void printProperties ();
		Boolean hasProperty (char* propertyName);
		int getNumProperties();
};

class WatchDog {
	private:
		static WatchDog* watchdogInstance;

		Properties properties;
		char* mainClass;
		char* logFileName;
		Boolean daemonFlag;
		char* ENV_EM_HOME;
		int fdLimit;
		char* runDir;

		JavaVMInitArgs jvmArgs;
		JavaVMOption options[64];
		int optionIndex;
		int classOptionIndex;

	public:
		WatchDog (int, char**);

	private:
		void daemon (int ignsigcld, char* chdirLocation);
		void launch (char*, int argc, char** argv);
		void parseArgs (int argc, char** argv);
		void usage ();
		void checkEnv ();
		static void jvmExitHook (jint code);
		static void jvmAbortHook ();
		static jint jvmVFPrintfHook (FILE* fp, const char* format, va_list args);
		void jvmExit (jint code);
		void jvmAbort ();
		jint jvmVFPrintf (FILE* fp, const char* format, va_list args);
		void shutdownNow ();
		void setLimits ();
};

extern Boolean debugFlag;

