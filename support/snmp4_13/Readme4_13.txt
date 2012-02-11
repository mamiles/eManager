Version 4.13:
20/11/2002

This is version 4.13 of the simple Java SNMPv1, SNMPv2c and SNMPv3 stack.
It can be collected via our homepage:
<URL http://www.westhawk.co.uk/resources/snmp/snmp4_13.zip> (1.5 Mb)

Read the file StackUsage.html for general information how to use the stack.
The frequently asked questions can be found in faq.html.

****** CHANGES / FIXES ****
- Fixed major performance problem due to synchronized(this) block in
  AbstractSnmpContext.run(). Removed the block.

- Removed the same block from DefaultTrapContext.run() as well.

- included stubs directory in the zip file.




Version 4.12:
11/11/2002

This is version 4.12 of the simple Java SNMPv1, SNMPv2c and SNMPv3 stack.
It can be collected via our homepage:
<URL http://www.westhawk.co.uk/resources/snmp/snmp4_12.zip> (1.5 Mb)

Read the file StackUsage.html for general information how to use the stack.
The frequently asked questions can be found in faq.html.

We would like to thank everyone that was kind enough to send us bug fixes
and/or useful suggestions:
- Balakrishnan <bala_tbn@yahoo.com>
- Eli Bishop <eli@graphesthesia.com>
- Ernest Jones <EJones@netopia.com>
- Michael(Mike) Waters, ERG Group
- Pete Kazmier <pete@kazmier.com>
- Seon Lee <slee@virtc.com>

Sorry if we've left anyone out.


****** BUG FIXES ****

- The trap listener can now be stopped.

- Using Pdu.waitForSelf() or the BlockPdu will handle a timeout correctly.
  Some times no timeout exception was passed or the isTimedOut flag was not
  set due to timing issues.

- SnmpContextPool/SnmpContextv2cPool no longer will throw 
  a ClassCastException when both SNMP versions were used with 
  the same parameters.

- Improved thread safety in the SnmpContext*Pool classes.

- Fixed problem that '1' was not recognised as OID, while it is valid.



****** NEW ****
- Experimented with the auto generation of Java code out off MIB file, using
  libsmi, XML, XSL, XSLT.
  See stubs directory.

- New class uk.co.westhawk.snmp.stack.SnmpContextBasisFace
  Forms the basis of all SnmpContext classes and interfaces.

- New class uk.co.westhawk.snmp.stack.SnmpContextPoolItem
  This class contains one context and one reference counter. The reference
  counter maintains how many objects reference this context. It is a helper
  class for the context pools, to improve its synchronisation.

** New Inform classes:
- uk.co.westhawk.snmp.stack.InformPdu
- uk.co.westhawk.snmp.pdu.OneInformPdu
- uk.co.westhawk.snmp.pdu.InformPdu_vec
  New classes to support sending (only!) an Inform.
  Note, Receiving an Inform and replying with a Response is NOT yet supported!

** New Passive classes:
- uk.co.westhawk.snmp.pdu.PassiveTrapPduv1
- uk.co.westhawk.snmp.pdu.PassiveTrapPduv2
- uk.co.westhawk.snmp.stack.PassiveSnmpContext
- uk.co.westhawk.snmp.stack.PassiveSnmpContextv2c
  The purpose of the Passive classes is to allow the stack to be used
  in environments where thread creation is unwanted, eg database JVMs such as
  Oracle JServer. See below for new package uk.co.westhawk.nothread.trap

** New package uk.co.westhawk.nothread.oscar:
- Simple example how to use Java in Oracle8i (tm).
  Written this as preparation to next new package (see below).

** New package uk.co.westhawk.nothread.trap:
- Added code, scripts and SQL files explaining how the stack can be used to
  send v1 and v2c traps in Oracle8i (tm).

** Distribution generates (also) snmpOracle<no>.jar
- This is a cut-down jar file of the stack that contains just the files 
  necessary to send v1 and v2c traps in Oracle8i (tm).



****** CHANGES ****

** General, overall changes:
- No longer using SnmpContextBasisFace.MSS
  The maximum number of bytes to receive (maxRecvSize) is a new parameter to
  the context. MSS is only used as default value for maxRecvSize.
  Note, this is not supported in the SnmpContextXXXPool classes!

- Added a flag isDestroyed to the SnmpContextXXXX classes. 
  An EncodingException will be thrown when the context is used after 
  being destroyed. 

- Using StringBuffer instead of String in a most of the toString() methods.



** Compiler:
  No longer using JDK 1.1.8. Stack now uses JDK 1.3.0 to compile code,
  generate javadoc and test the stack.

** Changes in package uk.co.westhawk.applet*:
- Moved applet1_0, applet1_1, appletv2c, appletv3 to
  examplev1, examplev2c, examplev3.
  Rewritten all applets into application.
  Application parameters can be configured in .properties file.

** Changes in package uk.co.westhawk.snmp.stack:
- varbind:
  + added constructor
    # public varbind(AsnObjectId Oid)
    # public varbind(AsnObjectId Oid, AsnObject val)
    Saves converting from String -> OID -> String

- Pdu:
  + fixed bug so waitForSelf() handles a timeout always correctly.
  + made sendme() protected so the PassivePdus can call it
    directly in addToTrans().
  + added methods
    # addOid(AsnObjectId oid)
    # addOid(AsnObjectId oid, AsnObject val)
    Saves converting from String -> OID -> String
  + amended method toString():
    # uses StringBuffer instead of String.
  + amended method fillin():
    # fills in the request id.
- SetPdu:
  + moved method addOid(String oid, AsnObject val) to Pdu
- GetBulkPdu:
  + amended method toString():
    # uses StringBuffer instead of String.

- SnmpContextFace:
- SnmpContextv2cFace:
- SnmpContextv3Face:
  + now extend SnmpContextBasisFace.

- AbstractSnmpContext:
  + added flag isDestroyed. EncodingException is thrown when Context is used
    after being destroyed.
  + revised destroy() 
  + made stop() deprecated.
  + renamed toString to getDebugString().
    Due to overloading, the user could not access this toString().
  + added methods:
    # protected void activate(): 
      will be overwritten by the PassiveContexts, see section "NEW".
    # public int getMaxRecvSize()
    # public void setMaxRecvSize(int no)
      replaces the use of SnmpContextBasisFace.MSS. maxRecvSize is passed 
      as parameter to the socket.receive() method.
  + run() catches all exceptions so that it keeps running no matter what.

- SnmpContext:
- SnmpContextv2c:
- SnmpContextv3:
  + maxRecvSize is passed as parameter to the socket.receive() method.
    It is no longer using SnmpContextBasisFace.MSS
  + amended method toString():
    # uses StringBuffer instead of String.
  + an EncodingException is thrown when the context is used after 
    being destroyed.

- DefaultTrapContext:
  + added methods:
    # public int getMaxRecvSize()
    # public void setMaxRecvSize(int no)
      replaces the use of SnmpContextBasisFace.MSS. maxRecvSize is passed 
      as parameter to the socket.receive() method.
    # public void destroy():
      fixes bug that the trap listener could not be stopped
  + run() catches all exceptions so that it keeps running no matter what.

- SnmpContextPool:
- SnmpContextv2cPool:
  + fixed bug that caused ClassCastException by adding version number to
    hashKey.
  + improved thread safety by only using 1 hashtable (and no longer 2).
    Using SnmpContextPoolItem object (a new class) to store in
    hashtable contextPool.
  + added constructor that take the community name:
    # public SnmpContextPool(String host, int port, String comm, String typeSocket)
- SnmpContextv3Pool:
  + added version number to hashKey.
  + improved thread safety by only using 1 hashtable (and no longer 2).
    Using SnmpContextPoolItem object (a new class) to store in
    hashtable contextPool.
  + fixed problem that the proper context was not always created after
    parameters were changed, but before it was being used.

- AsnObject: 
  + added methods:
    # public String getRespTypeString(): 
      returns the response type as a String.

- AsnNull:
- AsnPrimitive:
- AsnInteger:
- AsnUnsInteger:
- AsnUnsInteger64:
  + added methods:
    # public boolean equals(Object anObject)
    # public int hashCode()

- AsnOctets:
  + added methods:
    # public AsnOctets(java.net.InetAddress iad)
      This will generate an object of ASN IPAddress type.
    # public boolean equals(Object anObject)
    # public int hashCode()
    # public long [] toSubOid(boolean length_implied)
    # private long getPositiveValue(int index)
  + amended method toString():
    # uses StringBuffer instead of String.

- AsnObjectId:
  + fixed problem that '1' was not recognised as OID, while it is valid.
  + added methods:
    # public void add(long sub_oid)
    # public void add(long[] sub_oid)
    # public void add(String s)
    # public boolean equals(Object anObject)
    # public int hashCode()
    # private long [] toArrayOfLongs(String s)
  + removed method same().
  + amended methods size() and write() so it deals with an empty OID
    or an OID of length 1.
  + amended method toString():
    # uses StringBuffer instead of String.
    # only prints the first 100 characters. If the array is longer then
      100, it concatenates "[.. cut ..]."

- Transmitter
  + amended method toString():
    # uses StringBuffer instead of String.

- TrapPduv1
  + amended method toString():
    # uses StringBuffer instead of String.
    # prints request id
  + amended method fillin():
    # try/catch DecodingException


** Changes in package uk.co.westhawk.snmp.pdu:
- BlockPdu: added methods
  + addOid(AsnObjectId oid)
  + addOid(AsnObjectId oid, AsnObject val)

- InterfacePdu: added methods to get sysUpTime, inOctects and outOctets.


** Changes in package org.bouncycastle.crypto.*:
- Using version 1.15 (instead of 1.08) of the Bouncy Castle crypto API


