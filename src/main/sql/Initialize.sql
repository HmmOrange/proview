DROP TABLE IF EXISTS issue;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS user;

CREATE TABLE book
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NULL,
    author      VARCHAR(100) NULL,
    description VARCHAR(500) NULL,
    time_added  TIMESTAMP    NULL,
    rating      DOUBLE       NULL,
    issue_count INT          NULL
);

CREATE TABLE user
(
    username VARCHAR(20) PRIMARY KEY,
    password VARCHAR(20) NULL,
    type     INT         NULL
);

CREATE TABLE issue
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    duration    INT,
    username    VARCHAR(20),
    book_id INT,
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (username) REFERENCES user (username)
);

-- Add admin user (type 0)
INSERT INTO user(username, password, type) VALUES ('admin', 'admin', 0);

-- Add 3 sample users (type 1)
INSERT INTO user(username, password, type) VALUES ('23021497', '23021497', 1);
INSERT INTO user(username, password, type) VALUES ('23021501', '23021501', 1);
INSERT INTO user(username, password, type) VALUES ('23021521', '23021521', 1);

-- Add 2 sample books
INSERT INTO book(id, name, author, description, time_added, rating, issue_count)
    VALUES (1, 'Book #1', 'Author #1', 'Very long description #1', CURRENT_TIMESTAMP(), 4.9, 10);

INSERT INTO book(id, name, author, description, time_added, rating, issue_count)
    VALUES (2, 'Book #2', 'Author #2', 'Very long description #2', CURRENT_TIMESTAMP(), 4.5, 5);

SELECT * FROM user;