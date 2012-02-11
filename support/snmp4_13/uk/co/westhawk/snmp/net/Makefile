#
# NAME
#	  $RCSfile: Makefile,v $
# DESCRIPTION
#	  [given below in javadoc format]
# DELTA
#	  $Revision: 1.4 $
# CREATED
#	  $Date: 2002/10/22 16:24:03 $
# COPYRIGHT
#	  Westhawk Ltd
# TO DO
#


DISTRIBDIR=$(TOPDISTRIBDIR)/uk/co/westhawk/snmp/net

SRC=\
	ContextSocketFace.java \
	StandardSocket.java \
	NetscapeSocket.java 

TOJAR=\
	$(OBJ) 

TOZIP= \
	$(SRC) \
	Makefile

TOORACLEJAR=\
	ContextSocketFace.class \
	StandardSocket.class

include ../../../../../Makefile.general

CLASSPATH=$(.CLASSPATH):$(NETSCAPE)

