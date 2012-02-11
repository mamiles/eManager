rem
rem NAME
rem      $RCSfile: checkStoredProc.sql,v $
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

set hea off;
set tab off;
set wrap off;

rem describe all_source;

SELECT LINE, TEXT
 FROM all_source
WHERE OWNER = 'SCOTT'
  AND TYPE = 'PACKAGE BODY'
;

quit;

