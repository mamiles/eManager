#
# NAME
#      $RCSfile: Makefile,v $
# DESCRIPTION
#      [given below in javadoc format]
# DELTA
#      $Revision: 1.2 $
# CREATED
#      $Date: 2002/10/22 16:22:23 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

DISTRIBDIR=$(TOPDISTRIBDIR)/uk/co/westhawk/examplev3

SRC=\
	OneBlockOperation2.java \
	ReceiveTrap.java \
	SendInform.java \
	SendTrap.java \
	SnmpTarget.java \
	StreamEventMonitor.java \
	WestSpeedTests.java \
	get_next.java \
	set_one.java \
	propertyDialog2.java \
	Util.java

PROP=\
	ReceiveTrap.properties \
	SendInform.properties \
	SendTrap.properties \
	get_next.properties \
	mgsoft.properties \
	mgsoftMD5.properties \
	mgsoftMD5_DES.properties \
	mgsoftSHA1.properties \
	mgsoftSHA1_DES.properties \
	setSnmpEnableAuthenTraps.properties \
	set_one.properties


SCRIPT=\
	setenv.bat \
	runapplication.bat \
	setenv.sh \
	runapplication.sh

TOJAR=\
	$(OBJ)

TOORACLEJAR=

TOZIP= \
	$(SRC) \
	$(PROP) \
	$(SCRIPT) \
	Makefile 

include ../../../../Makefile.general

