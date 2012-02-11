#
# NAME
#      $RCSfile: Makefile,v $
# DESCRIPTION
#      [given below in javadoc format]
# DELTA
#      $Revision: 1.11 $
# CREATED
#      $Date: 2002/10/22 16:25:35 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

DISTRIBDIR=$(TOPDISTRIBDIR)/uk/co/westhawk/test

SRC=\
	TestSuite.java \
	TrapTestSuite.java \
	SnmpTarget.java \
	XMLtoDOM.java \
	Util.java \
	DOMWriter.java 

XSL_FILES=\
	testToHtml.xsl \
	trapToHtml.xsl

XML_FILES=\
	test.dtd \
	test.xml \
	trap.dtd \
	trap.xml

HTML= \
	TestSuite.html \
	TrapTestSuite.html \
	test.html \
	trap.html

SCRIPT=\
	setenv.bat \
	runapplication.bat \
	setenv.sh \
	runapplication.sh \
	treeviewer.sh \
	xml_to_html.sh \
	xslt.sh

TOJAR=\
	$(OBJ) \
	$(XML_FILES)

TOORACLEJAR=

TOZIP= \
	$(SRC) \
	$(HTML) \
	$(SCRIPT) \
	$(XML_FILES) \
	$(XSL_FILES) \
	Makefile 

include ../../../../Makefile.general

#
# xerces.jar can be downloaded from the IBM website:
# http://alphaworks.ibm.com/tech/xml4j
# 
CLASSPATH=$(.CLASSPATH):$(XML)

test.html: test.xml testToHtml.xsl
	./xml_to_html.sh
	
trap.html: trap.xml trapToHtml.xsl
	./xml_to_html.sh
	
