#!/bin/sh
#
# NAME
#      $RCSfile: runapplication.sh,v $
# DESCRIPTION
#      [given below]
# DELTA
#      $Revision: 1.1 $
#      $Author: birgit $
# CREATED
#      $Date: 2002/11/11 15:02:07 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

source ./setenv.sh

# on Solaris
# do '.' instead of 'source'
# ./runappltion.sh TrapTestSuite > trap.roke.out 2>&1 
#

# JDK 1.3.0 setup 
JAVA_HOME=/opt/jdk1.3
CLASSPATH=${TABLE}:${SNMPJAR}:${SOURCE}:.:${XML}

OPTIONS=
OPTIONS=-Djava.compiler=NONE

echo OPTIONS "${OPTIONS}"
echo CLASSPATH "${CLASSPATH}"

${JAVA_HOME}/bin/java -classpath ${CLASSPATH} ${OPTIONS} uk.co.westhawk.test.$*
