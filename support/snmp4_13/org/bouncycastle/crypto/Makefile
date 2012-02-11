#
# NAME
#      $RCSfile: Makefile,v $
# DESCRIPTION
#      [given below in javadoc format]
# DELTA
#      $Revision: 1.2 $
# CREATED
#      $Date: 2002/10/15 12:59:32 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

DISTRIBDIR=$(TOPDISTRIBDIR)/org/bouncycastle/crypto

SRC=\
	BlockCipher.java \
	CipherKeyGenerator.java \
	CipherParameters.java \
	DataLengthException.java \
	Digest.java \
	KeyGenerationParameters.java \
	RuntimeCryptoException.java

TOJAR= \
	$(OBJ) 

TOORACLEJAR=$(TOJAR)

TOZIP=\
	$(SRC) \
	Makefile

DIRS=\
	engines \
	params \
	digests \
	generators

JARDIRS=\
	$(DIRS) 

ORACLEJARDIRS=$(JARDIRS)

ZIPDIRS= $(DIRS)

include ../../../Makefile.general


