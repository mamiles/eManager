#!/bin/sh
# NAME
#      $RCSfile: xslt.sh,v $
# DESCRIPTION
#      [given below]
# DELTA
#      $Revision: 1.1 $
#      $Author: birgit $
# CREATED
#      $Date: 2002/10/29 14:19:34 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

#
# Xalan can be downloaded from the Apache website:
# http://xml.apache.org/xalan-j/
#
# Xerces (xml4j) can be downloaded from the IBM website:
# http://alphaworks.ibm.com/tech/xml4j
#

JAVA_HOME=/opt/jdk1.3

XML=/project/xml4j_3_1_1
XALAN=/project/classes1.2/xalan-j_2_4_0/bin

CP=${XML}/xml4j.jar:${XALAN}/xalan.jar

file=$*
if [ -z "$file" ]; then
    echo "Usage: xslt.sh <xml file> <xsl file> <out file>"
else
    #echo ${CP}
    ${JAVA_HOME}/bin/java -classpath ${CP} org.apache.xalan.xslt.Process -in $1 -xsl $2 -out $3 
fi

