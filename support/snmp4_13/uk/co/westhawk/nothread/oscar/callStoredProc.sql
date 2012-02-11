rem
rem NAME
rem      $RCSfile: callStoredProc.sql,v $
rem DESCRIPTION
rem      [given below]
rem DELTA
rem      $Revision: 1.1 $
rem      $Author: birgit $
rem CREATED
rem      $Date: 2002/10/15 13:37:01 $
rem COPYRIGHT
rem      Westhawk Ltd
rem TO DO
rem

VARIABLE theQuote VARCHAR2(50);
CALL scott.OscarTest.oscar_quote() INTO :theQuote;
PRINT theQuote;
quit;

