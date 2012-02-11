#
# NAME
#	  $RCSfile: Makefile,v $
# DESCRIPTION
#	  [given below in javadoc format]
# DELTA
#	  $Revision: 3.18 $
# CREATED
#	  $Date: 2002/10/22 16:24:31 $
# COPYRIGHT
#	  Westhawk Ltd
# TO DO
#



DISTRIBDIR=$(TOPDISTRIBDIR)/uk/co/westhawk/snmp/stack

ORACLESRC=\
	AbstractSnmpContext.java \
	AgentException.java \
	AsnEncoder.java \
	AsnDecoder.java \
	AsnInteger.java \
	AsnNull.java \
	AsnObject.java \
	AsnObjectId.java \
	AsnOctets.java \
	AsnPduSequence.java \
	AsnPrimitive.java \
	AsnSequence.java \
	AsnUnsInteger.java \
	AsnUnsInteger64.java \
	AsnTrapPduv1Sequence.java \
	EncodingException.java \
	DecodingException.java \
	DefaultTrapContext.java \
	Pdu.java \
	PduException.java \
	SnmpContextBasisFace.java \
	SnmpContextFace.java \
	SnmpContextv2cFace.java \
	SnmpContext.java \
	SnmpContextv2c.java \
	SnmpContextPoolItem.java \
	SnmpConstants.java \
	varbind.java \
	TrapPduv1.java \
	TrapPduv2.java \
	PassiveSnmpContext.java \
	PassiveSnmpContextv2c.java \
	Transmitter.java 


SRC=\
	$(ORACLESRC) \
	GetBulkPdu.java \
	GetNextPdu.java \
	InformPdu.java \
	SetPdu.java \
	SnmpContextPool.java \
	SnmpContextv2cPool.java \
	SnmpContextv3Face.java \
	SnmpContextv3.java \
	SnmpContextv3Pool.java \
	TimeWindow.java \
	TimeWindowNode.java \
	DefaultUsmAgent.java \
	UsmAgent.java \
	usmStatsConstants.java 


TOJAR=\
	$(OBJ) 

TOZIP= \
	$(SRC) \
	Makefile

TOORACLEJAR=$(ORACLEOBJ)

include ../../../../../Makefile.general

