# Copyright 1994-2000 by TIBCO Software, Inc.
# ALL RIGHTS RESERVED
# @(#)Makefile	1.6

# TIB/Rendezvous C++ library source.
#
# A few notes...
#
# If the provided librvcpp library for you system does not fit your needs
# because of compiler versioning or you have other needs, we provide the
# source to our C++ library so that you may re-compile the library to fit
# your needs.
# 
# We provide the basics here, which you can modify to suit your own
# needs.
#
# To compile the examples, please uncomment the sections that represent
# your environment then built it!
#
#
#

########################################################################
# UNIX Specific items
########################################################################
#
# for the make programs provided with Tru64UNIX and AIX you need to
# uncomment the following:
#.SUFFIXES: .cpp
#
#O=.o
#LIB=.a
#RM=rm -f
#OUT=-o
#RV_DIR=/usr/tibco/tibrv
#DASH_C=-c
#
# change the following to match the C++ compiler you use.
#
#CXX=CC
#
#OPT_DBG_FLAGS=-g
#AR=ar
#ARFLAGS=cruv 
#INCFILES=-I$(RV_DIR)/include/tibrv
#CXXFLAGS=$(INCFILES) $(OPT_DBG_FLAGS)
#

########################################################################
# Windows Specific Items
########################################################################
#O=.obj
#LIB=.lib
#RM=del
#RV_DIR=c:\TIBCO\TIBRV
#AR=lib
#INCFILES=/I$(RV_DIR)\include\tibrv
#OUT=/Fe
#DASH_C=-c
#CXX=cl
#OPT_DBG_FLGS=/nologo /MT
#CXXFLAGS=$(OPT_DBG_FLGS) $(INCFILES)
#ARFLAGS=/NOLOGO /OUT:


########################################################################
# Not all makes define a default target for all the various C++ file 
# suffixes.
#
# Uncommend the target rules that best match the make program you are
# using
########################################################################
# NMAKE users uncomment the next two lines
#.cpp.obj:
#        $(CXX) $(CXXFLAGS) $(DASH_C) %s
#
# UNIX users get to pick from one of these choices depending on which
# system and which make they are using.
#
# The make that comes with with Solaris and  GNU make prefer this.
#%.o: %.cpp
#	$(CXX) $(CXXFLAGS) $< $(DASH_C)
#
# HP-UX / AIX provided make
#.cpp.o:
#	$(CXX) $(CXXFLAGS) $< $(DASH_C)
#

LIBS= 



all:	libtibrvcpp$(LIB)

clean: 	
	$(RM) *$(O)
	$(RM) *$(LIB)


BASE_MODULES=tibrv$(O) disp$(O) event$(O) tport$(O) queue$(O) qgroup$(O) \
             msg$(O)
             
FT_MODULES=ft$(O)
CM_MODULES=cm$(O) cmq$(O)

#
# Secure Daemon Connection
#
# Uncomment this if you want to be able to use the secure daemon
# connectivity.  Note may not be supported on all platforms.
#
#SD_MODULE=sd$(O)

libtibrvcpp$(LIB): $(BASE_MODULES) $(FT_MODULES) $(CM_MODULES) $(SD_MODULE)
	$(AR) $(ARFLAGS)$@ $(BASE_MODULES) $(FT_MODULES) $(CM_MODULES) $(SD_MODLUE)

