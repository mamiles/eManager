Version 4.10:
06/09/2001

This is version 4.10 of the simple Java SNMPv1, SNMPv2c and SNMPv3 stack.
It can be collected via our homepage:
<URL http://www.westhawk.co.uk/resources/snmp/snmp4_10.zip> (1.3 Mb)

Read the file StackUsage.html for general information how to use the stack.
The stack still uses JDK 1.1.X . 
The javadoc is generated with JDK 1.2.

The changes in this release are almost all bug fixes.
The most important change is that of AsnUnsInteger; using type long (instead
of type int). This does probably mean that you have to recompile and change
your code where it concerns TimeTick, Counter (i.e. Counter32) and Gauge from
type int to type long.

We would like to thank Visionael Corp (http://www.visionael.com) for
their support.

We would like to thank everyone that was kind enough to send us bug fixes and
useful suggestions:
- Donnie Love (dlove@idsonline.com)
- Jin Huang (jhuang@infiniswitch.com)
- Julien Conan (jconan@protego.net)
- Ken Swanson (gks@navcomm1.dnrc.bell-labs.com)
- Maga Hegde (mhegde@zumanetworks.com)
- Steven Bolton (sbolton@cereva.com)

Sorry if we've left anyone out.

****** CHANGES ****

** Changes (mostly bug fixes) in package uk.co.westhawk.snmp.stack:
- AsnObject: moved all the constants to interface SnmpConstants
- AsnObject: improved converting bytes to length in getLengthPacket()
- AsnObjectId: the class is now represented as an array of type long (instead
  of type int)
- AsnInteger: improved converting bytes into int in bytesToInteger()
- AsnUnsInteger: using type long (instead of type int). int is too small for 
  all Counter32 values.
- AsnUnsInteger: improved converting bytes into unsigned int in bytesToLong()
- AsnUnsInteger64: improved converting bytes into unsigned int in
  bytesToLong()
- AsnOctets: when the value is an empty, it no longer causes a
  ArrayOutOfBoundsIndexException in toIpAddress() and in toHex()
- AsnOctets: added the method toDisplayString()
- Pdu: retry_intervals is no longer static
- Pdu: changed what error status is set in fillin() in case of a decoding
  problem, see getErrorStatus()
- TrapPduv1: because of AsnUnsInteger, timeticks is now a type long as well
- AsnEncoder.EncodeTrap1Pdu(): the IPAddress in a TrapPduv1 is now being
  correctly encoded as an IPADDRESS type (and no longer as the default
  TIMETICKS type) 
- TrapPduv2: fixed a bug so now the trap pdu will get filled in properly when
  being decoded


** Changes in package uk.co.westhawk.snmp.pdu:
- BlockPdu: added method getErrorIndex()

** Changes in package org.bouncycastle.crypto.*:
- Using version 1.08 (instead of 1.05) of the Bouncy Castle crypto API

** Overall changes:
- because of AsnUnsInteger, some variables are now of type long as well
  (instead of type int)
- changed some typos (whilest -> whilst)

*** NEW
- interface uk.co.westhawk.snmp.stack.SnmpConstants


Version 4.8
27/04/2001

We would like to thank Visionael Corp (http://www.visionael.com) for
their support.


****** CHANGES ****

- org/bouncycastle/crypto - Updated bouncycastle version to 105
- catch exception in pdu.fillin that can happen when agent makes an
  error in encoding the varbind list


Version 4.7:
13/04/2001

We would like to thank West Consulting BV (http://www.west.nl), 
Adherent Systems Ltd (http://www.adherent.com) and 
Visionael Corp (http://www.visionael.com) for their support. 

****** NEW ****

** Privacy to SNMPv3 

The stack now supports SNMPv3 privacy. Please read the
StackUsage.html and the javadoc for more information.

** Packages:
- org/bouncycastle/crypto - Using bouncycastle crypto instead of cryptix


****** CHANGES ****

- Fixed a number of problems with SNMPv2c 
- Fixed a number of problems with the GetBulkRequest
- Agent- and DecodingExceptions that occurs whilest decoding, will now
  be passed to 'arg' in you update(obs, arg).
- Moved around some debuging levels.

*** uk/co/westhawk/snmp/stack/GetBulkPdu.java
- Added setMaxRepetitions(int no)
- Made setMaxRepititions(int no) deprecated, use setMaxRepetitions(int no).
  
*** uk/co/westhawk/snmp/stack/SnmpContextv3Face.java
*** uk/co/westhawk/snmp/stack/SnmpContextv3.java
*** uk/co/westhawk/snmp/stack/SnmpContextv3Pool.java
- Added methods for privacy


Version 4.4:
07/03/2001

We would like to thank West Consulting BV (http://www.west.nl), 
Adherent Systems Ltd (http://www.adherent.com) and 
Visionael Corp (http://www.visionael.com) for their support. 

****** NEW ****

** Sending and Receiving Traps (v1, v2c, v3)

The stack now supports sending and receiving traps. Please read the
StackUsage.html and the javadoc for more information.

** Packages:
- uk.co.westhawk.snmp.event

** Classes:
- uk.co.westhawk.snmp.stack.TrapPduv1
- uk.co.westhawk.snmp.stack.TrapPduv2
- uk.co.westhawk.snmp.stack.DefaultTrapContext
- uk.co.westhawk.snmp.stack.DefaultUsmAgent
- uk.co.westhawk.snmp.stack.UsmAgent
- uk.co.westhawk.snmp.stack.AsnTrapPduv1Sequence
- uk.co.westhawk.snmp.pdu.OneTrapPduv1
- uk.co.westhawk.snmp.pdu.OneTrapPduv2


Version 4.3:
15/02/2001

****** NEW ****

** SNMPv3

We have implemented SNMPv3. It does not (yet) contain privacy and traps. 
For authentication the MD5 and SHA1 protocols can be used.

** Exceptions:
- PduException
- AgentException (extend PduException)
- DecodingException (extend PduException)
- EncodingException (extend PduException)

** Classes:
- We have implemented AsnUnsInteger64. We have not tested this though,
  since I couldn't access any MIB who implemented that.
- We have implemented GetBulkPdu.
- We have implemented BlockPdu that performs a sends a request and
  blocks until it receives a response.

** Packages:
- uk.co.westhawk.snmp.util
- uk.co.westhawk.snmp.net
- uk.co.westhawk.appletv3

** The Context structure:
- New interface SnmpContextFace and SnmpContextv3Face
- New class AbstractSnmpContext implements SnmpContextFace
- New class SnmpContextPool that shares SnmpContext objects
- New class SnmpContextv3 extends AbstractSnmpContext
- New class SnmpContextv3Pool that shares SnmpContextv3 objects


** SNMPv2c

We have implemented SNMPv2c. It does not (yet) contain traps.

** Packages: 
- uk.co.westhawk.appletv2c

** The Context structure:
- New class SnmpContextv2c extends SnmpContext
- New class SnmpContextv2cPool that shares SnmpContextv3 objects

****** CHANGES ****
1.
The community name has moved from Pdu to SnmpContext. Instead of using:
  Pdu pdu;
  pdu.send(community);

use:
  SnmpContext context;
  Pdu pdu;
  context.setCommunity(community);
  pdu.send();

Almost all Pdu constructors with the community name in it have been removed.

2.
Other Pdu methods have changed:
- send() may throw a PduException.
- added method getResponseVarbinds() throws PduException
- added method getRequestVarbinds() 
- The Pdu constructor uses SnmpContextFace (see above).

3.
- Changed class SnmpContext so it extends AbstractSnmpContext (instead of 
  Object)

4.
- Changed debug setup. See AsnObject.setDebug().

5.
- Besides the mandatory SNMPv3 objects (like COUNTER64), added
  NSAP_ADDRESS, UINTEGER32 to AsnObject.

6.
- Added AsnObject.getRespType().

7.
- AsnOctets.toString() will prefix any hexidecimal number with '0x'.
- AsnOctets.toString() has improved the test to see if the Octet is
  printable. 
- AsnOctets will handle an OPAQUE type as well.

8.
- Pdu.getErrorStatusString() will return 'timed out' instead of 'general
  error' when the request has timed out.


Version 3.3:
10/02/2000 - Manchester (UK), Westhawk Ltd.

We have fixed some thread problems in the pdu package.

Tim Panton (snmp@westhawk.co.uk)
Birgit Arkesteijn (snmp@westhawk.co.uk)



Version 3.0:
01/07/1999 - Manchester (UK), Westhawk Ltd.

We have moved to the UK, note our change of email address and webpages.

The setup of the classes has changed; 
- package names contain our company domain
    uk.co.westhawk.applet1_0
    uk.co.westhawk.applet1_1
    uk.co.westhawk.servlet
    uk.co.westhawk.snmp.beans (NEW)
    uk.co.westhawk.visual (NEW)
    uk.co.westhawk.snmp.pdu
    uk.co.westhawk.snmp.stack

Some methods names has changed to match the JDK 1.1 convention. No
backwards compatibility is provided!

The documentation (in javadoc format) has been improved. 

***** uk.co.westhawk.snmp.beans (JDK1.1) NEW

The package uk.co.westhawk.snmp.beans contains beans that can be used in
any Java Builder. It is written to ease the use of our SNMP stack.
They are simple enough to serve as example to write your own beans.

The (toplevel) bean SNMPBean contains some general information, that is
worthwhile reading.

Some Windows NT specific beans have been written, using the NT vendor
MIB.
There is an Ascend Router bean and an IBM Network Computer bean.

***** uk.co.westhawk.visual (JDK1.1) NEW

The package uk.co.westhawk.visual contains a graph that can be used to
represent numerical data. The graph can draw the 10log() representation
or the normal representation.

***** uk.co.westhawk.snmp.stack (JDK1.1)

Not much has changed since the previous version. Documentation has been
added. Some classes and methods have been restricted to access by this
class only. Some method names have changed to match the JDK 1.1 style.

***** uk.co.westhawk.snmp.pdu (JDK1.1)

Not much has changed since the previous version. Documentation has been
added. Some classes and methods have been restricted to access by this
class only. Some method names have changed to match the JDK 1.1 style.

We have added the Pdu:
- OneGetPdu
  This class is added to be consists with the other classes, like
  OneSetPdu, OneGetNextPdu.


***** uk.co.westhawk.servlet (JDK1.1)

We have added a servlet:
- ReachHostServlet
  This servlet returns a HTML form that can be used to find out if a
  host can be reached on a certain port.

The servlet Interfaces contains some general information, that is
worthwhile reading.


***** uk.co.westhawk.applet1.0 (JDK1.0)

These applets show how to use the pdu's and the servlets.
Documentation is improved.

***** uk.co.westhawk.applet1.1 (JDK1.1)

Every aspect of our SNMP stack can be found in one of the applets. These 
applets show how to use the pdus, the beans, the graph. The documentation 
is improved.

The html files may contain our (test) configuration, so you may have 
change that to your own.

Some applets can be used as applications as well. 

We have added an applet for every bean that is written:
- testAscendActiveSessionBean
- testDialogChannelStatusBean
- testInterfaceBean
- testNTPrintQBean
- testNTServiceNamesBean
- testNTSharedResBean
- testNTUserNamesBean
- testNcdPerfDataBean (contains the graph as well)

We have added an applet that used the SetPdu request:
- set_one



***** How to use the stack

The stack can be used in various ways, this makes it flexible on one
hand, but confusing on the other hand.

The easiest way to use the stack, is to use one of the beans from the
uk.co.westhawk.snmp.beans package. There should be enough documentation
in the code to help you. 

The beans form a bridge between the uk.co.westhawk.snmp.pdu (and
uk.co.westhawk.snmp.stack) package any applet or application you may
want to write.

Our bean gathers specific MIB information, i.e. the OID (Object
Identifier) is hard coded. The beans should be easy enough to understand, and
with little effort you can write a bean for your own purpose.

You can use, of course, the Pdu's from the uk.co.westhawk.snmp.pdu package 
directly, which offers you more flexibility. Our beans and applets will 
show you how, although this will require some basic knowledge of SNMP.



We also provide you with a couple of servlets. The benefits of servlets
are described in the package documentation. Servlets can be combined with
(simple) HTML or with an applet on the client side.


For more background on SNMP we recommend:
- The Simple Book by Marshall T. Rose (Prentice Hall, 1994).
- SNMP, SNMPv2 and RMON by William Stallings (Addison-Wesley, 1996).

Tim Panton (snmp@westhawk.co.uk)
Birgit Arkesteijn (snmp@westhawk.co.uk)


Version 2.0:
21/08/97

In this version two stacks have been joined:
- Version 1.1 of West Consulting BV 
  (see <URL: http://www.west.nl/archive/java/snmp/>)
- Our version Alpha2 of the stack
  (see <URL http://www.westhawk.co.uk/nblue4.html>)


The setup of the classes has changed; 
- package snmp has been split into
    snmp.pdu
    snmp.stack


***** snmp.stack (JDK1.1)

The package snmp.stack contains only those classes that are necessary to
send/receive SNMP requests.
For that purpose the classes:
- GetNextPdu
- SetPdu
  have been made abstract so they are consistent with the Pdu class.
  See snmp.pdu package for non-abstract versions.

The thread communication between Pdu, SnmpContext and Transmitter is
improved.

***** snmp.pdu (JDK1.1)

All Pdu's that did not belong strictly to the stack have been moved to
the package snmp.pdu.
We have added some Pdu's:
- OneGetNextPdu
- OneSetPdu
  These should be used instead of the old GetNextPdu and SetPdu.
  GetNextPdu and SetPdu are made abstract like Pdu to be more
  consistent.

- GetNextPdu_vec
- SetPdu_vec
  These Pdu are the vector versions of OneGetNextPdu and OneSetPdu. They
  request/set a vector of variables instead of one.

- InterfaceGetNextPdu
  This class does a GetNext on the interfaces (ifTable) of a host. It can be 
  used to loop over all interfaces in the ifTable.
  See for that purpose 
  * the servlet Interfaces or 
  * the applet1.1 getAllInterfaces


***** servlet (JDK1.1)

We have added a servlet:
- Interfaces
  This servlet loops at a regular interval over all interfaces of a
  certains host. The HOST, PORT and INTERVAL should be configured in the
  JavaServer.
  The applet1.0 getAllInterfaces is written to communicate with this
  servlet. 


***** applet1.0 (JDK1.0)

The applets are split up into the ones written with JDK1.0 and with
JDK1.1 This is done because at this time there are hardly any 1.1
Browsers.

The applet:
- getAllInterfaces
  This a very thin applet.
  It displays all the interface usage (bandwidth) of a particular host. It
  gets its information of a servlet (configurable), the so-called 3-tier
  setup. The host is configured in the servlet.


***** applet1.1 (JDK1.1)

The applets are split up into the ones written with JDK1.0 and with
JDK1.1 This is done because at this time there are hardly no 1.1
Browsers.

The applet:
- getAllInterfaces
  is the applet version of the servlet Interfaces. There is no display
  like applet1.0, just printing.




Tim Panton (snmp@westhawk.nl)
Birgit Arkesteijn (snmp@westhawk.nl)


Version 1.1:
30/10/96
Note the additional copyright, I changed jobs.

Second version of stack. Changes are:
	Bug in ASNInteger fixed.
	Smarter use of threads, separate Transmitter class which is re-useable.
	Application specific PDUs:
		 InterfacePdu	: stats on an interface
		 UpSincePdu	: gives the date that the agent started.
	Sync mode for PDUs, you can send a PDU and then block waiting for an 
	answer by calling Pdu.waitForSelf(), returns a boolean indicating if 
	an answer was recieved.
	More demo programs:
		get_one		: an applet which repeatedly gets a single 
				  ASNInteger (sysUptime is good) and displays 
				  thread activity.
		Interface	: an applet that shows the speed of an 
				  interface as calcuated from two sucessive 
				  measurements.
		Interfaces	: an applet that shows the state of all (if 
				  they fit) the interfaces on an agent.
		JeevesUpSince	: a servlet (for use in the Jeeves web server 
				  (SUN)) that displays the date a host was 
				  rebooted.
		JeevesInterfaces: a servlet that displays the status of the 
				  interfaces on an agent.
	(The demo directory contains some examples for use with the Jigsaw
	webserver (from W3C) )

	Note that the Interfaces demos assume that the interfaces are 
	contiguous and numbered from one. Livingstone Portmasters don't do this.
	The applets can all be seen by running the html file of the same name,
	the default hostname won't work, so you will have to edit them.

	To use the Jeeves and Jigsaw demos you will have to install the server
	of your choice and then add this directory to the server's classpath.
	Start the server and then follow the instructions for adding servlets.

The status of the stack is unchanged, it is still a work-in-progress, and
certainly not a robust commercial product. The code's none to clean either.

I'll try to keep releasing new versions and putting them on 
www.westhawk.co.uk

tpanton@ibm.net




Version 1.0:
11/03/96

This is a simple SNMP Version 1 stack.

Background.

It was written to learn Java and to see if it could be done.
The target application is _small_ applets that indicate the state of
a system. For example the throughput of a router or the battery power
remaining of a UPS. It is not intended to be used in a general purpose 
network management station. It has no MIB browsing capabilities, you have
to know the OID of what you want to monitor. There are _many_ other packages
that do MIB browsing and general network management. I wanted something lighter
that I could just pop in a (netscape) frame  and leave there all day.
In practice this means that it will start to become inefficient once you
have more than 10 or so active PDUs at a time, each PDU owns (is) a thread,
and holds it for ~20 seconds after the request is sent, so if your update
period is short or the number of PDUs is large then the number of threads
will rise rapidly. 

Copyright.

See the header of each file: Basically you can use it for whatever you want,
but you can't blame us for anything wrong with it, and you can't rewrite history
by removing our copyright text.

Status.

It is a work in progress, don't assume it is tested, or that it represents 
typical West quality, it was done as a prototype, and seemed to have some
value of it's own so we set it free :-).

Use.

The idea is that for each class of problem you should subclass the abstract
Pdu class. This design is based on the observation that most realworld numbers
you want to extract from an SNMP agent are based on two or more values from the
MIB. Throughput for example is caluculated from five numbers, so a throughput
Pdu would request all five, calculate the single throughput percentage and
then pass it back to whoever wanted to know. I include a trivial example - 
OneIntPdu which gets a single integer value from an Snmp agent.


Once you have such a Pdu class - or are happy with OneInt then uage is simple,
see get_one.java
The steps you have to follow are :
	1) create an SnmpContext , giving it the host name and port number
of your SNMP agent (If this is run in an applet then you an only 
connect back to that server - but you can watch the throughput on that,
which is what I use it for.)
	2) create a PDU, passing it the SnmpContext, add the OIDs you are
interested in with pdu.add_oid(oid). A better way would be to have the
PDU constructor add what it needs, but OnIntPdu is too dumb. 
	3) add an observer with pdu.addObserver(this), the argument is the
object that wishes to be notified when the new value arrives. It must implement
Observer, that is it must have a 	
	public void update(Observable obs, Object ov)
method. This method will get called when the value arrives.
	4) call
		 pdu.send("public");		
Actually the community name should really belong with the context, but I have 
not got arround to fixing that.
	5) sleep, or do something else, your update method will get called
automatically when the data has arrived, both the Context and PDU objects
spawn their own threads, so there is nothing further to do.


*** Contact snmp@westhawk.co.uk ***
