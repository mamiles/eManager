#
# NAME
#      $RCSfile: Makefile,v $
# DESCRIPTION
#      [given below in javadoc format]
# DELTA
#      $Revision: 1.3 $
# CREATED
#      $Date: 2002/11/11 15:00:20 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

DISTRIBDIR=$(TOPDISTRIBDIR)/uk/co/westhawk/nothread/trap

SRC=\
	SendTrap.java

SQL=\
	checkLoadResult.sql \
	checkStoredFunc.sql \
	createTrapTable.sql \
	grantPermission.sql \
	insertRow.sql

SCRIPT=\
	loadTrapExample.sh \
	runTrapExample.sh

TOJAR=\
	$(OBJ) 

TOORACLEJAR=$(TOJAR)

TOZIP= \
	$(SRC) \
	$(SCRIPT) \
	$(SQL) \
	Makefile 

include ../../../../../Makefile.general

#CLASSPATH=../../../../../snmpOracle4_12.jar
CLASSPATH=$(CLASSPATH122)

JAVA_HOME=$(JAVA_HOME122)


