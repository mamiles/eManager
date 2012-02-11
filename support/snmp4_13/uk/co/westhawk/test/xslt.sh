#!/bin/sh

#
# xsql_servlet can be downloaded from:
# http://technet.oracle.com/tech/xml/xsql_servlet/
#

JAVA_HOME=/opt/jdk1.2.2
XSQL_HOME=/project/packages1.1/xsql_servlet

# Java JDK Runtime Classes
CP=${JAVA_HOME}/lib/classes.zip

# Oracle JDBC Driver Archive
CP=${XSQL_HOME}/lib/classes111.zip:${CP}

# Oracle XML Parser V2 (with XSLT Engine) Archive
CP=${XSQL_HOME}/lib/xmlparserv2.jar:${CP}

# Oracle XML SQL Utility for Java Archive
CP=${XSQL_HOME}/lib/oraclexmlsql.jar:${CP}

# Oracle XSQL Servlet Archive
CP=${XSQL_HOME}/lib/oraclexsql.jar:${CP}

# XSQLConnections.xml connection definition file
CP=${XSQL_HOME}/lib:${CP}

${JAVA_HOME}/bin/java -classpath ${CP} oracle.xml.parser.v2.oraxsl $*

