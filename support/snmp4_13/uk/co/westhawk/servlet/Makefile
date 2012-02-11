#
# NAME
#      $RCSfile: Makefile,v $
# DESCRIPTION
#      [given below in javadoc format]
# DELTA
#      $Revision: 3.4 $
# CREATED
#      $Date: 2002/10/15 12:59:32 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#


DISTRIBDIR=$(TOPDISTRIBDIR)/uk/co/westhawk/servlet

SRC=\
	JeevesInterfaces.java \
	JeevesUpSince.java \
	ReachHostServlet.java \
	Interfaces.java

TOJAR=\
	$(OBJ) 

TOORACLEJAR=

TOZIP= \
	$(SRC) \
	Makefile

include ../../../../Makefile.general

#
# The Java Servlet jar(s) can be downloaded from:
# http://java.sun.com/products/servlet/download.html
#
CLASSPATH=$(.CLASSPATH):$(HTTPD)
