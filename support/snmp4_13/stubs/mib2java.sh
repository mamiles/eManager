#!/bin/sh
#
# NAME
#      $RCSfile: mib2java.sh,v $
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
# smidump is part of libsmi. It is available for Linux, Win, Mac
# See http://www.ibr.cs.tu-bs.de/projects/libsmi/
#
# The xml format in libsmi 0.4.0 is still in experimental mode, but
# hopefully it works well enough.
#

# The directory where SMI is installed.
SMIPATH=/opt/libsmi-0.4.0/

# The mib files that are being used, see ${SMIPATH}/share/mibs/
MIBFILES="\
  HOST-RESOURCES-MIB \
"

# The java source file that is being generated
JAVAFILE=StorageGetNextPdu.java


#
# Generate a xml file out of a MIB definition file.
#
# Some of the MIB files are syntactically not correct and will generate
# errors. If this is the case, the xml file will not be correct either.
#

echo "Generating xml file out off " ${MIBFILES} " with smidump ..."
${SMIPATH}/bin/smidump -f xml ${MIBFILES} > smi.xml



#
# Generate a xml file with only the OIDs I'm interested in.
#
# You have to amend the template 'doYouWantThisOid' in the file myOids.xsl 
# to configure the OIDs.
# Only scalar and column nodes are taken into account.
#

echo "Generating (sub) xml file with specific OIDs ... " 
./xslt.sh smi.xml myOids.xsl subsmi.xml



#
# Generate a java file with stubs in them.
#
# The name of the package and the class can be (have to be) configured
# in xmlTojava.xsl.
#
# Please make sure that 
# 1. the name of the java file is the same name as the xsl 
#    variable 'classname' in xmlTojava.xsl.
# 2. the java file is moved to the directory where this package lives.
#
# If these 2 things are not done, this class will not compile
# correctly!!
#

echo "Generating java source file " ${JAVAFILE} ...

./xslt.sh subsmi.xml xmlTojava.xsl ${JAVAFILE}

echo "Please move " ${JAVAFILE} " to it's correct location"

