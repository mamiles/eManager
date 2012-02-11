#
# NAME
#      $RCSfile: Makefile,v $
# DESCRIPTION
#      [given below in javadoc format]
# DELTA
#      $Revision: 1.1 $
# CREATED
#      $Date: 2002/10/15 13:37:01 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

DISTRIBDIR=$(TOPDISTRIBDIR)/uk/co/westhawk/nothread/oscar

SRC=\
	Oscar.java 

SQL=\
	checkLoadResult.sql \
	createStoredProc.sql \
	checkStoredProc.sql \
	callStoredProc.sql

SCRIPT=\
	loadOscarExample.sh \
	runOscarExample.sh

TOJAR=\
	$(OBJ) 

TOORACLEJAR=$(TOJAR)

TOZIP= \
	$(SRC) \
	$(SCRIPT) \
	$(SQL) \
	Makefile 

include ../../../../../Makefile.general

CLASSPATH=$(CLASSPATH122)
JAVA_HOME=$(JAVA_HOME122)

