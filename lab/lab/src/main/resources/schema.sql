CREATE DATABASE USER_DB;
USE USER_DB;
CREATE TABLE USERS (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL
);

CREATE TABLE PROPERTIES (
    id VARCHAR(100) PRIMARY KEY,
    value VARCHAR(500) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
 INSERT INTO PROPERTIES (ID, VALUE, DESCRIPTION) VALUES ('PATH_CSV', '/home/victorhugolgr/git/lab-spring-batch/data/csv', 'Caminho da pasta com arquivos CSV');

INSERT INTO properties (id, value, description) VALUES ('PATH_CSV_PROCESSED', '/home/victorhugolgr/git/lab-spring-batch/data/csv/processed', 'Caminho para arquivos processados');

