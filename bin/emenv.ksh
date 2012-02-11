##
## Directory where eManager is installed
EM_HOME=EMANAGER_HOME
export EM_HOME

##
## JDK 1.4 location
##
JAVA_HOME=JAVA_HOME_DIR
export JAVA_HOME
JRE_ROOT=$JAVA_HOME/jre
export JRE_ROOT
EM_TMP=$EM_HOME/tmp
export EM_TMP

##
## Tibco home directory
##
RV_ROOT=RV_ROOT_DIR
HAWK_ROOT=HAWK_ROOT_DIR
export RV_ROOT
export HAWK_ROOT

##
## -Xbootclasspath is not passed on to watchdog if this set
## to y
##
NO_BOOT_CLASSPATH=y
export NO_BOOT_CLASSPATH

##
## Don't start RVA by default
## 
##
NO_USE_RVA=y
export NO_USE_RVA

##
##  Sybase Databaes location
##
DBDATADIR=DBDATADIR_LOCATION
DBLOGDIR=DBLOGDIR_LOCATION
export DBDATADIR
export DBLOGDIR

##
## Sybase Install Directory
##
ASANY8="$EM_HOME/3rdparty/sybase/SYBSsa8"
export ASANY8

##
## Sybase shared directory location.
##
ASANYSH8="$EM_HOME/3rdparty/sybase/shared"
export ASANYSH8

##
## Function to get property values
##
get_system_property() {
    grep $1 $EM_HOME/config/em.properties | /usr/bin/nawk -F= '{print $2}'
}

## get the system property values
DBNAME=`get_system_property database.connection.database.name`
DBUSER=`get_system_property database.connection.user.account`
DBPASSWD=`get_system_property database.connection.password`
DBPORT=`get_system_property database.connection.connection.port`
DBHOSTNAME=`get_system_property database.connection.database.host`
export DBNAME
export DBUSER
export DBPASSWD
export DBPORT
export DBHOSTNAME

##
## PATH
##
PATH=\
$JAVA_HOME/bin:\
$EM_HOME/bin:\
$EM_HOME/bin/solaris:\
$RV_ROOT/bin:\
$EM_HOME/3rdparty/sybase/shared/sybcentral41:\
$EM_HOME/3rdparty/sybase/SYBSsa8/bin:\
${PATH}

export PATH

##
## LD_LIBRARY_PATH
##
LD_LIBRARY_PATH=\
$EM_HOME/lib:\
$RV_ROOT/lib:\
$EM_HOME/3rdparty/sybase/SYBSsa8/lib:\
$EM_HOME/3rdparty/sybase/SYBSsa8/jre/lib/sparc/client:\
$EM_HOME/3rdparty/sybase/SYBSsa8/jre/lib/sparc:\
$EM_HOME/3rdparty/sybase/SYBSsa8/jre/lib/sparc/native_threads:\
${LD_LIBRARY_PATH}

export LD_LIBRARY_PATH

##
## If you do not have CLASSPATH set in your environment, please add to this
## so that the Process Sequencer / Watchdog can pick up the classes
##

CLASSPATH=\
$RV_ROOT/lib/tibrvj.jar:\
$HAWK_ROOT/java/ami.jar:\
$HAWK_ROOT/java/config.jar:\
$HAWK_ROOT/java/console.jar:\
$HAWK_ROOT/java/talon.jar:\
$HAWK_ROOT/java/util.jar:\
$HAWK_ROOT/java/utilities.jar:\
$EM_HOME/tomcat/common/lib/servlet.jar:\
$EM_HOME/3rdparty/sybase/SYBSsa8/java/jlogon.jar:\
$EM_HOME/3rdparty/sybase/SYBSsa8/java/jodbc.jar:\
$EM_HOME/3rdparty/sybase/shared/jConnect-5_5/classes/jconn2.jar:\
$EM_HOME/lib/castor-0.9.5.2.jar:\
$EM_HOME/lib/exrces.jar:\
$EM_HOME/tomcat/webapps/emanager/WEB-INF/lib/log4j-1.2.8.jar:\
$EM_HOME/jars/emgmt-process-mgt.jar:\
${CLASSPATH}

export CLASSPATH