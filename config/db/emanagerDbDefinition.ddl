CREATE TABLE application_hierarchy (
        id UNSIGNED INTEGER NOT NULL,
        name VARCHAR ( 64 ) NOT NULL,
        application_hierarchy_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_application_hierarchy1 PRIMARY KEY (id)
        );
CREATE TABLE application_relationship (
        application_type_id UNSIGNED INTEGER NOT NULL,
	application_hierarchy_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_application_relationship1 PRIMARY KEY (application_type_id, application_hierarchy_id)
        );
CREATE TABLE physical_hierarchy (
        id UNSIGNED INTEGER NOT NULL,
        name VARCHAR ( 64 ) NOT NULL,
        physical_hierarchy_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_physical_hierarchy1 PRIMARY KEY (id)
        );
CREATE TABLE physical_relationship (
        host_id UNSIGNED INTEGER NOT NULL,
	physical_hierarchy_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_physical_relationship1 PRIMARY KEY (host_id, physical_hierarchy_id)
        );
CREATE TABLE solution_hierarchy (
        id UNSIGNED INTEGER NOT NULL,
        name VARCHAR ( 64 ) NOT NULL,
        solution_hierarchy_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_solution_hierarchy PRIMARY KEY (id)
        );
CREATE TABLE solution_hierarchy_relationship (
        solution_id UNSIGNED INTEGER NOT NULL,
	solution_hierarchy_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_solution_hierarchy_relationship PRIMARY KEY (solution_id, solution_hierarchy_id)
        );
CREATE TABLE solution (
        id UNSIGNED INTEGER NOT NULL,
	name VARCHAR (64) NOT NULL,
        CONSTRAINT PK_solution PRIMARY KEY (id)
        );
CREATE TABLE solution_instance_relationship (
	solution_id UNSIGNED INTEGER NOT NULL,
	application_instance_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_solution_instance_relationship PRIMARY KEY (solution_id, application_instance_id)
        );
CREATE TABLE application_instance (
        id UNSIGNED INTEGER NOT NULL,
        name VARCHAR ( 128 ) NOT NULL,
        managed SMALLINT NOT NULL,
        logfile_directories VARCHAR ( 512 ),
        logfile_transport_method INTEGER,
        logfile_transport_username VARCHAR ( 16 ),
        logfile_transport_password VARCHAR ( 16 ),
        unix_prompt VARCHAR ( 32 ),
        property_file VARCHAR ( 128 ),
        application_type_id UNSIGNED INTEGER NOT NULL,
        host_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_application_instance8 PRIMARY KEY (id)
        );
CREATE TABLE security_role (
        id UNSIGNED INTEGER NOT NULL,
        name VARCHAR ( 32 ) NOT NULL,
        CONSTRAINT PK_security_role3 PRIMARY KEY (id)
        );
CREATE TABLE user_account (
        id UNSIGNED INTEGER NOT NULL,
        name VARCHAR ( 32 ) NOT NULL,
        password VARCHAR ( 32 ) NOT NULL,
        password_key VARCHAR ( 64 ) NOT NULL,
        security_role_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_user2 PRIMARY KEY (id),
        );
CREATE TABLE host (
        id UNSIGNED INTEGER NOT NULL,
        ip_address_string VARCHAR ( 15 ) NOT NULL,
        tibhawk_domain VARCHAR ( 64 ) NOT NULL,
        dns_hostname VARCHAR ( 64 ) NOT NULL,
        CONSTRAINT PK_agent3 PRIMARY KEY (id)
        );
CREATE TABLE audit_log (
        id UNSIGNED INTEGER NOT NULL,
        log_time DATETIME NOT NULL,
        domain SMALLINT NOT NULL,
        action SMALLINT NOT NULL,
	subject VARCHAR ( 256 ) NOT NULL,
        user_id VARCHAR (32) NOT NULL,
        description VARCHAR ( 512 ) NOT NULL,
        CONSTRAINT PK_audit_log1 PRIMARY KEY (id),
        );
CREATE TABLE application_type (
        id UNSIGNED INTEGER NOT NULL,
        name VARCHAR ( 128 ) NOT NULL,
        version VARCHAR ( 32 ) NOT NULL,
        CONSTRAINT PK_application_type1 PRIMARY KEY (id)
        );
CREATE TABLE management_policy (
        id UNSIGNED INTEGER NOT NULL,
        name VARCHAR ( 128 ) NOT NULL,
        path VARCHAR ( 128 ) NOT NULL,
        application_type_id UNSIGNED INTEGER NOT NULL,
        host_id UNSIGNED INTEGER NOT NULL,
        CONSTRAINT PK_management_policy4 PRIMARY KEY (id)
        );
CREATE TABLE event (
        id UNSIGNED INTEGER NOT NULL,
        event_type SMALLINT NOT NULL,
        post_time DATETIME NOT NULL,
        clear_time DATETIME DEFAULT NULL,
        acknowledgement_user_id VARCHAR (32),
        acknowledgement_time DATETIME DEFAULT NULL,
        acknowledgement_comment VARCHAR ( 255 ),
        severity SMALLINT NOT NULL,
        display_text VARCHAR ( 255 ) NOT NULL,
        managed_object_type SMALLINT NOT NULL,
        managed_object_key UNSIGNED INTEGER NOT NULL,
        event_type_values VARCHAR ( 512 ) DEFAULT NULL,
        CONSTRAINT PK_event2 PRIMARY KEY (id)
        );
CREATE TABLE sequence (
        id VARCHAR ( 32 ) NOT NULL,
        value UNSIGNED INTEGER DEFAULT 0 NOT NULL,
        CONSTRAINT PK_sequence1 PRIMARY KEY (id)
        );

ALTER TABLE application_hierarchy 
        ADD CONSTRAINT FK_application_hierarchy6 
        FOREIGN KEY (application_hierarchy_id) 
        REFERENCES application_hierarchy (id);

ALTER TABLE application_relationship 
        ADD CONSTRAINT FK_application_relationship2 
        FOREIGN KEY (application_hierarchy_id) 
        REFERENCES application_hierarchy (id);

ALTER TABLE application_relationship 
        ADD CONSTRAINT FK_application_relationship3 
        FOREIGN KEY (application_type_id)
        REFERENCES application_type (id);

ALTER TABLE physical_hierarchy 
        ADD CONSTRAINT FK_physical_hierarchy4 
        FOREIGN KEY (physical_hierarchy_id) 
        REFERENCES physical_hierarchy (id);

ALTER TABLE physical_relationship
        ADD CONSTRAINT FK_physical_relationship2
        FOREIGN KEY (physical_hierarchy_id) 
        REFERENCES physical_hierarchy (id);

ALTER TABLE physical_relationship
        ADD CONSTRAINT FK_physical_relationship3
        FOREIGN KEY (host_id)
        REFERENCES host (id);

ALTER TABLE application_instance 
        ADD CONSTRAINT FK_application_instance6 
        FOREIGN KEY (host_id) 
        REFERENCES host (id);

ALTER TABLE application_instance 
        ADD CONSTRAINT FK_application_instance4 
        FOREIGN KEY (application_type_id) 
        REFERENCES application_type (id);

ALTER TABLE user_account 
        ADD CONSTRAINT FK_user_account5 
        FOREIGN KEY (security_role_id) 
        REFERENCES security_role (id);

ALTER TABLE management_policy 
        ADD CONSTRAINT FK_management_policy7 
        FOREIGN KEY (host_id) 
        REFERENCES host (id);

ALTER TABLE management_policy 
        ADD CONSTRAINT FK_management_policy4 
        FOREIGN KEY (application_type_id) 
        REFERENCES application_type (id);

ALTER TABLE solution_instance_relationship 
        ADD CONSTRAINT FK_solution_instance_relationship1 
        FOREIGN KEY (solution_id)
        REFERENCES solution (id);

ALTER TABLE solution_instance_relationship 
        ADD CONSTRAINT FK_solution_instance_relationship2
        FOREIGN KEY (application_instance_id)
        REFERENCES application_instance (id);

ALTER TABLE solution_hierarchy_relationship 
        ADD CONSTRAINT FK_solution_hierarchy_relationship1 
        FOREIGN KEY (solution_id)
        REFERENCES solution (id);

ALTER TABLE solution_hierarchy_relationship 
        ADD CONSTRAINT FK_solution_hierarchy_relationship2
        FOREIGN KEY (solution_hierarchy_id)
        REFERENCE solution_hierarchy (id);

CREATE PROCEDURE get_user_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='user';

        update sequence
        set value = value + 1
        where id='user';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_host_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='host';

        update sequence
        set value = value + 1
        where id='host';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_applicationtype_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='app_type';

        update sequence
        set value = value + 1
        where id='app_type';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_application_instance_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='app_instance';

        update sequence
        set value = value + 1
        where id='app_instance';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_solution_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='solution';

        update sequence
        set value = value + 1
        where id='solution';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_auditLog_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='audit_log';

        update sequence
        set value = value + 1
        where id='audit_log';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_event_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='event';

        update sequence
        set value = value + 1
        where id='event';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_managementpolicy_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='mgmt_policy';

        update sequence
        set value = value + 1
        where id='mgmt_policy';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_physicalhierarchy_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='physical_hierarchy';

        update sequence
        set value = value + 1
        where id='physical_hierarchy';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_applicationhierarchy_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='application_hierarchy';

        update sequence
        set value = value + 1
        where id='application_hierarchy';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_solution_hierarchy_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='solution_hierarchy';

        update sequence
        set value = value + 1
        where id='solution_hierarchy';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_user_account_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='user_account';

        update sequence
        set value = value + 1
        where id='user_account';

        commit;

        RETURN newKey;
END;

CREATE PROCEDURE get_security_role_id ()
BEGIN
        DECLARE newKey bigint;

        select value into newKey
        from sequence
        where id='security_role';

        update sequence
        set value = value + 1
        where id='security_role';

        commit;

        RETURN newKey;
END;

INSERT INTO sequence (id, value) VALUES ('security_role', 4);
INSERT INTO sequence (id, value) VALUES ('user_account', 2);
INSERT INTO sequence (id, value) VALUES ('host', 1);
INSERT INTO sequence (id, value) VALUES ('app_type', 1);
INSERT INTO sequence (id, value) VALUES ('app_instance', 1);
INSERT INTO sequence (id, value) VALUES ('audit_log', 1);
INSERT INTO sequence (id, value) VALUES ('event', 1);
INSERT INTO sequence (id, value) VALUES ('mgmt_policy', 1);
INSERT INTO sequence (id, value) VALUES ('solution', 1);
INSERT INTO sequence (id, value) VALUES ('physical_hierarchy', 2);
INSERT INTO sequence (id, value) VALUES ('application_hierarchy', 2);
INSERT INTO sequence (id, value) VALUES ('solution_hierarchy', 2);

INSERT INTO security_role (id, name) VALUES (1, 'administrator');
INSERT INTO security_role (id, name) VALUES (2, 'operator');
INSERT INTO security_role (id, name) VALUES (3, 'guest');
INSERT INTO user_account (id, name, password, password_key, security_role_id) VALUES (1, 'admin', 'cisco123', 'cisco123', 1);
INSERT INTO physical_hierarchy (id, name, physical_hierarchy_id) VALUES (0, 'Root', 0);
INSERT INTO physical_hierarchy (id, name, physical_hierarchy_id) VALUES (1, 'Discovery', 0);
INSERT INTO application_hierarchy (id, name, application_hierarchy_id) VALUES (0, 'Root', 0);
INSERT INTO application_hierarchy (id, name, application_hierarchy_id) VALUES (1, 'Discovery', 0);
INSERT INTO solution_hierarchy (id, name, solution_hierarchy_id) VALUES (0, 'Root', 0);
INSERT INTO solution_hierarchy (id, name, solution_hierarchy_id) VALUES (1, 'Discovery', 0);

commit;
