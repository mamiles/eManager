#!/bin/sh
#
# NAME
#      $RCSfile: runTrapExample.sh,v $
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
# This script will only work if the previous, loadTrapExample.sh,
# works.
#

echo "*** Call the stored function ***"
echo "*** This should generate a trap ***"
echo "*** If you are using XSQL, you can try xsql/insert.html as well ***"

sqlplus scott/tiger @insertRow.sql

