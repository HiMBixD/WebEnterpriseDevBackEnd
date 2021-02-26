DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT
ALL
ON SCHEMA public TO postgres;
GRANT ALL
ON SCHEMA public TO public;
COMMENT
ON SCHEMA public IS 'standard public schema';

CREATE TABLE ROLES
(
    role_id     INTEGER primary key,
    role_name   varchar(30),
    description varchar(100)
);

CREATE TABLE USERS
(
    USERNAME   VARCHAR(50) PRIMARY KEY,
    PASSWORD   VARCHAR(255),
    FIRST_NAME VARCHAR(255),
    LAST_NAME  VARCHAR(255),
    PHONE      VARCHAR(255),
    EMAIL      VARCHAR(255),
    ROLE_ID    INTEGER references ROLES (role_id)
);
-- auto_increment