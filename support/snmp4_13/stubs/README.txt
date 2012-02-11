/* 
 * NAME
 *      $RCSfile: README.txt,v $
 * DESCRIPTION
 *      [given below]
 * DELTA
 *      $Revision: 1.1 $
 *      $Author: birgit $
 * CREATED
 *      $Date: 2002/10/29 14:19:34 $
 * COPYRIGHT
 *      Westhawk Ltd
 * TO DO
 */


This directory contains files, scripts, etc to generate a specific 
PDU out off a MIB, where the user can configure which OIDs should be
used.

Our stack does not have a MIB browser, nor do we want to add one. The idea 
is to write specific PDUs or beans for (with) specific OIDs.

The aim of this setup is to auto generate a Java source file of a PDU 
or bean out off a MIB file. We are not sure that the current result is 
the most useful one, but at least it shows what can be done.

This is in experimental phase and we could like to get your comments
back. It is definitely not fool-proof, so please do not complain if it
does not work how you expected it to be. Just change the code,
or email us with suggestions of a different setup.


The procedure uses smidump (libsmi), the Xalan XSLT and Xerces to generate
XML and Java file.

-- libsmi is native code. It is available for Linux, Win, Mac.
-- See http://www.ibr.cs.tu-bs.de/projects/libsmi/
-- 
-- Xalan can be downloaded from the Apache website:
-- See http://xml.apache.org/xalan-j/
-- 
-- Xerces (xml4j) can be downloaded from the IBM website:
-- See http://alphaworks.ibm.com/tech/xml4j 


The user is expected to have a fair understanding of XML, XSLT, XSL,
Java and our SNMP stack. Please do not email us with beginners questions 
about these. There are plenty of good books and website out there, so 
please read them first.


Steps:
1. Select the MIB file(s) (called 'module(s)' in smidump) that contain
   the OIDs you are interested in.
   Amend MIBFILES in mib2java.sh


2. Amend the template 'doYouWantThisOid' in the file myOids.xsl to
   configure the OIDs you are interested in. 
   Only scalar and column nodes are taken into account.


3. Think of a good name for the java source file. Currently
   StorageGetNextPdu is being used.
   Amend JAVAFILE in mib2java.sh
   Amend classname near the top of file xmlTojava.xsl


4. Think of a good package name. Currently uk.co.westhawk.snmp.pdu is
   being used.
   Amend packagename near the top of file xmlTojava.xsl


5. Run ./mib2java.sh
   Keep an eye out for any errors generated; 
   Some of the MIB files are syntactically not correct and will generate 
   errors. If this is the case, the initial XML file will not be
   correct, and error will accumulate.


6. Move the java file to the directory where the package lives and
   compile.


