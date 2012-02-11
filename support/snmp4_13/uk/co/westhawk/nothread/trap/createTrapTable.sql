rem
rem NAME
rem      $RCSfile: createTrapTable.sql,v $
rem DESCRIPTION
rem      [given below]
rem DELTA
rem      $Revision: 1.1 $
rem      $Author: birgit $
rem CREATED
rem      $Date: 2002/10/15 13:37:02 $
rem COPYRIGHT
rem      Westhawk Ltd
rem TO DO
rem
rem USAGE
rem        sqlplus scott/tiger @createTrapTable.sql
rem


SET TERMOUT ON

PROMPT Creating stored function.  Please wait.

CREATE OR REPLACE PACKAGE scott.TrapTest IS
    FUNCTION send(host VARCHAR2, 
                  port NUMBER, 
                  comm VARCHAR2, 
                  upTime NUMBER, 
                  trapOid VARCHAR2)
    RETURN VARCHAR2;
END;
/

CREATE OR REPLACE PACKAGE BODY scott.TrapTest IS
    FUNCTION send(host VARCHAR2, 
                  port NUMBER, 
                  comm VARCHAR2, 
                  upTime NUMBER, 
                  trapOid VARCHAR2)
    RETURN VARCHAR2 AS
    LANGUAGE JAVA 
    NAME 'uk.co.westhawk.nothread.trap.SendTrap.doSendTrap(java.lang.String, int, java.lang.String, long, java.lang.String) return java.lang.String';
END;
/

PROMPT Stored function build.


PROMPT Building trap table.  Please wait.

DROP TABLE scott.Trap;
DROP SEQUENCE scott.TrapSeq;

CREATE TABLE scott.Trap
(
    trap_key      NUMBER(4) PRIMARY KEY,
    hostname      VARCHAR2(50) NOT NULL,
    port          NUMBER(5) NOT NULL,
    community     VARCHAR2(100) NOT NULL,
    sysUpTime     NUMBER(9) NOT NULL,
    snmpTrapOID   VARCHAR2(30) NOT NULL,
    result        VARCHAR2(500),
    insertDate    DATE
)
TABLESPACE SYSTEM;


show errors;

CREATE SEQUENCE scott.TrapSeq
    INCREMENT BY 1
    START WITH 1
    MAXVALUE 9999
    MINVALUE 1
    CYCLE; 


CREATE TRIGGER scott.TrapSeqInc 
BEFORE INSERT ON scott.Trap
    FOR EACH ROW
        BEGIN
            SELECT scott.TrapSeq.nextval INTO :new.trap_key
                    FROM dual;
            :new.result := scott.TrapTest.send(:new.hostname,
                      :new.port,
                      :new.community,
                      :new.sysUpTime,
                      :new.snmpTrapOID);
            :new.insertDate := SYSDATE;
        END;
/

show errors;

COMMIT;

PROMPT scott.Trap build


show errors;

exit;
