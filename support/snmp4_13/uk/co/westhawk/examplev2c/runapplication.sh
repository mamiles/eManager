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
#      $Date: 2002/10/21 10:29:38 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

source ./setenv.sh

# JDK 1.3 setup 
JAVA_HOME=/opt/jdk1.3

CLASSPATH=${TABLE}:${SNMPJAR}:${SOURCE}:.
echo CLASSPATH ${CLASSPATH}

OPTIONS=
OPTIONS=-Djava.compiler=NONE

${JAVA_HOME}/bin/java ${OPTIONS} -classpath ${CLASSPATH} uk.co.westhawk.examplev2c.$*
