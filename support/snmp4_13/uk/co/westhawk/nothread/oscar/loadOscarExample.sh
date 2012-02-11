#!/bin/sh
#
# NAME
#      $RCSfile: loadOscarExample.sh,v $
# DESCRIPTION
#      [given below]
# DELTA
#      $Revision: 1.1 $
#      $Author: birgit $
# CREATED
#      $Date: 2002/10/15 13:37:01 $
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

echo "*** Compile the java file, if necessary ***"
make

echo "*** Drop the previous copy of the class ***"
dropjava -user scott/tiger -verbose Oscar.class

echo "*** Load and resolve the class ***"
loadjava -user scott/tiger -verbose -andresolve Oscar.class

echo "*** Check the result by querying the database ***"
sqlplus scott/tiger @checkLoadResult.sql

echo 
echo "*** "
echo "*** Only when the status is VALID can you run the example  ***"
echo "*** "

echo "*** Create the stored procedure (well, actually function) ***"
sqlplus scott/tiger @createStoredProc.sql

#echo "*** if the stored procedure produces any errors, run ***"
# sqlplus scott/tiger @checkStoredProc.sql

echo 
echo "*** "
echo "*** If all is well, you can call runOscarExample.sh *** "
echo "*** "
