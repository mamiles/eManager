#!/bin/sh
#
# NAME
#      $RCSfile: loadTrapExample.sh,v $
# DESCRIPTION
#      [given below]
# DELTA
#      $Revision: 1.1 $
#      $Author: birgit $
# CREATED
#      $Date: 2002/10/15 13:37:02 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#


#
# This script will only work if all the ORACLE environment variables
# are set properly, like ORACLE_HOME and ORACLE_SID
#
# scott/tiger is the account where most of Oracle's examples
# live. This has to have been installed during installation.
#

JARFILE=../../../../../snmpOracle4_12.jar

echo "*** Drop the previous copy of the class ***"
dropjava -user scott/tiger -verbose ${JARFILE}

echo "*** Load and resolve the class ***"
loadjava -user scott/tiger -verbose ${JARFILE}
loadjava -user scott/tiger -verbose -resolve ${JARFILE}

echo "*** Grant the SocketPermission to SCOTT ***"
sqlplus system/manager @grantPermission.sql

echo "*** Check the result by querying the database ***"
sqlplus scott/tiger @checkLoadResult.sql

echo 
echo "*** "
echo "*** Only when the status is VALID can you run the example  ***"
echo "*** "

echo "*** Create the stored function ***"
sqlplus scott/tiger @createTrapTable.sql

#echo "*** if the stored function produces any errors, run ***"
# sqlplus scott/tiger @checkStoredFunc.sql

echo 
echo "*** "
echo "*** If all is well, you can call runTrapExample.sh *** "
echo "*** "
