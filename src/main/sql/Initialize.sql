DROP TABLE document;
DROP TABLE user;

CREATE TABLE document
(
    id     INT primary key,
    name   VARCHAR(100) NULL,
    author VARCHAR(100) NULL
);

CREATE TABLE user
(
    username VARCHAR(20) primary key,
    password VARCHAR(20) NULL,
    type     INT         NULL
);

-- Add admin user (type 0)
INSERT INTO user(username, password, type) VALUES ('admin', 'admin', 0);

-- Add 3 sample users (type 1)
INSERT INTO user(username, password, type) VALUES ('23021497', '23021497', 1);
INSERT INTO user(username, password, type) VALUES ('23021501', '23021501', 1);
INSERT INTO user(username, password, type) VALUES ('23021521', '23021521', 1);

SELECT * FROM user;