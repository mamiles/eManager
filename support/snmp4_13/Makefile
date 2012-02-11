#
# NAME
#      $RCSfile: Makefile,v $
# DESCRIPTION
#      [given below in javadoc format]
# DELTA
#      $Revision: 1.36 $
# CREATED
#      $Date: 2002/11/11 15:04:09 $
# COPYRIGHT
#      Westhawk Ltd
# TO DO
#

#
#see also Makefile.general
#

SRC=

TOJAR= 
TOORACLEJAR=

TOZIP= \
	$(JARFILE) \
	$(ORACLEJARFILE) \
	tablelayout.jar \
	Makefile \
	Makefile.general \
	Readme4_10.txt \
	Readme$(NO).txt \
	StackUsage.html \
	TOC.html \
	Contents.html \
	faq.html

DIRS=\
	uk/co/westhawk \
	org/bouncycastle/crypto 

JARDIRS= $(DIRS)
ORACLEJARDIRS= $(DIRS)

ZIPDIRS= $(DIRS) stubs


include Makefile.general


DISTRIBDIR=$(TOPDISTRIBDIR)

jar:
	make alljar
	make oraclejar

alljar:
	rm -f $(JARFILE)
	rm -rf jardir
	mkdir -p $(JARDIR)
	make TOPDISTRIBDIR=$(JARDIR) DISTRIBDIR=$(JARDIR) .jar ; 
	rm -rf $(JARDIR)/uk/co/westhawk/example*
	rm -rf $(JARDIR)/uk/co/westhawk/servlet
	rm -rf $(JARDIR)/uk/co/westhawk/test
	(cd $(JARDIR) ; $(JAVA_HOME)/bin/jar cfm ../$(JARFILE) ../beansManifestIn.mf $(TOJAR) uk org > /dev/null)
	$(JAVA_HOME)/bin/jar xf $(JARFILE) META-INF


oraclejar:
	rm -f $(ORACLEJARFILE)
	rm -rf jardir
	mkdir -p $(JARDIR)
	make TOPDISTRIBDIR=$(ORACLEJARDIR) DISTRIBDIR=$(JARDIR) .oraclejar ; 
	rm -rf $(JARDIR)/uk/co/westhawk/example*
	rm -rf $(JARDIR)/uk/co/westhawk/servlet
	rm -rf $(JARDIR)/uk/co/westhawk/test
	rm -rf $(JARDIR)/uk/co/westhawk/visual
	(cd $(JARDIR) ; $(JAVA_HOME)/bin/jar cf ../$(ORACLEJARFILE) $(TOORACLEJAR) uk org > /dev/null)
	$(JAVA_HOME)/bin/jar xf $(ORACLEJARFILE) META-INF


tablelayout.jar:
	cp /project/classes1.1/tablelayout.jar .

zip:
	rm -f $(ZIPFILE)
	rm -rf zipdir
	mkdir -p $(ZIPDIR) ;
	make TOPDISTRIBDIR=$(ZIPDIR) DISTRIBDIR=$(ZIPDIR) .zip ; 
	cp -r javadoc $(ZIPDIR);
	rm -rf $(ZIPDIR)/javadoc/CVS ;
	rm -rf $(ZIPDIR)/javadoc/images ;
	@(cd $(ZIPDIR) ; zip -r ../$(ZIPFILE) $(TOZIP) stubs javadoc uk org > /dev/null)

DOC_DIR=$(SNMP)/javadoc
PACKAGES= \
	uk.co.westhawk.examplev1 \
	uk.co.westhawk.examplev2c \
	uk.co.westhawk.examplev3 \
	uk.co.westhawk.nothread.oscar \
	uk.co.westhawk.nothread.trap \
	uk.co.westhawk.servlet \
	uk.co.westhawk.visual \
	uk.co.westhawk.test \
	uk.co.westhawk.snmp.pdu \
	uk.co.westhawk.snmp.net \
	uk.co.westhawk.snmp.stack \
	uk.co.westhawk.snmp.util \
	uk.co.westhawk.snmp.beans \
	uk.co.westhawk.snmp.event \
	org.bouncycastle.crypto \
	org.bouncycastle.crypto.digests \
	org.bouncycastle.crypto.engines \
	org.bouncycastle.crypto.generators \
	org.bouncycastle.crypto.params

TITLE=SNMP Stack $(NO)
jdoc: 
	@rm -f javadoc/*.* 
	@rm -rf javadoc/uk
	@rm -rf javadoc/org
	$(JAVA_HOME130)/bin/javadoc -protected -version -author -header "<i>$(TITLE)</i>" -doctitle "$(TITLE)" -windowtitle "$(TITLE)" -classpath $(CLASSPATH130):$(XML):$(HTTPD):$(NETSCAPE) -d $(DOC_DIR) $(PACKAGES)
