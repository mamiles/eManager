#!/bin/sh
# NAME
#      $RCSfile: treeviewer.sh,v $
# DESCRIPTION
#      [given below]
# DELTA
#      $Revision: 1.3 $
#      $Author: birgit $
# CREATED
#      $Date: 2002/10/29 14:48:46 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

#
# xml4j can be downloaded from the IBM website:
# http://alphaworks.ibm.com/tech/xml4j
#

JAVA_HOME=/opt/jdk1.3
XML=/project/xml4j_2_0_13

CP=${XML}/xml4j.jar:${XML}/xml4jSamples.jar

file=$*
if [ -z "$file" ]; then
    echo "Usage: treeviewer.sh <file>"
else
    echo ${CP}
    ${JAVA_HOME}/bin/java -classpath ${CP} ui.TreeViewer $file
fi


