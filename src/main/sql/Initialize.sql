DROP TABLE IF EXISTS issue;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS user;

CREATE TABLE book
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NULL,
    author      VARCHAR(100) NULL,
    description VARCHAR(500) NULL,
    time_added  TIMESTAMP    NULL,
    issue_count INT          NULL,
    copies      INT          NULL
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

CREATE TABLE tag
(
    book_id INT,
    tag     VARCHAR(100),
    FOREIGN KEY (book_id) REFERENCES book (id)
);

CREATE TABLE review
(
    book_id INT,
    rating DOUBLE,
    FOREIGN KEY (book_id) REFERENCES book (id)
);


-- Add admin user (type 0)
INSERT INTO user(username, password, type) VALUES ('admin', 'admin', 0);

-- Add 3 sample users (type 1)
INSERT INTO user(username, password, type) VALUES ('23021497', '23021497', 1);
INSERT INTO user(username, password, type) VALUES ('23021501', '23021501', 1);
INSERT INTO user(username, password, type) VALUES ('23021521', '23021521', 1);

-- Add 2 sample books
g
    VALUES ('Book #1', 'Author #1', 'Very long description #1', CURRENT_TIMESTAMP(), 10, 3);

INSERT INTO book(name, author, description, time_added, issue_count, copies)
    VALUES ('Book #2', 'Author #2', 'Very long description #2', CURRENT_TIMESTAMP(), 3, 5);

-- Add sample tags
INSERT INTO tag(book_id, tag) VALUES (1, 'Drama');
INSERT INTO tag(book_id, tag) VALUES (1, 'Slice of Life');

INSERT INTO tag(book_id, tag) VALUES (2, 'Action');
INSERT INTO tag(book_id, tag) VALUES (2, 'Comedy');
INSERT INTO tag(book_id, tag) VALUES (2, 'Guns');

-- Add sample reviews
INSERT INTO review(book_id, rating) VALUES (1, 5);
INSERT INTO review(book_id, rating) VALUES (1, 5);
INSERT INTO review(book_id, rating) VALUES (1, 4.1);

INSERT INTO review(book_id, rating) VALUES (2, 5);
INSERT INTO review(book_id, rating) VALUES (2, 4);
INSERT INTO review(book_id, rating) VALUES (2, 4.2);

SELECT * FROM user;