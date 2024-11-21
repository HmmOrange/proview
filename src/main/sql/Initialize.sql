DROP TABLE IF EXISTS issue;
DROP TABLE IF EXISTS book_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS favourite;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS user;

CREATE TABLE book
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NULL,
    author      VARCHAR(100) NULL,
    description VARCHAR(500) NULL,
    time_added  TIMESTAMP    NULL,
    copies      INT          NULL
);

CREATE TABLE user
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(20) UNIQUE,
    password  VARCHAR(20) NULL,
    type      INT         NULL,
    firstName VARCHAR(20) NULL,
    lastName  VARCHAR(20) NULL,
    email     VARCHAR(20) NULL,
    registration_date TIMESTAMP,
    card_view TINYINT(1)  NOT NULL DEFAULT 0
);

-- TODO: Remove the username field
CREATE TABLE issue
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    start_date TIMESTAMP,
    duration   INT,
    user_id    INT,
    username   VARCHAR(20),
    book_id    INT,
    status     VARCHAR(30),
    end_date   TIMESTAMP NULL,
    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (username) REFERENCES user (username) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tag
(
    name           VARCHAR(100) PRIMARY KEY,
    bg_color_hex   VARCHAR(10),
    text_color_hex VARCHAR(10)
);

CREATE TABLE book_tag
(
    book_id INT,
    tag     VARCHAR(100),
    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (tag) REFERENCES tag (name) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE rating
(
    user_id    INT,
    book_id    INT,
    rating     INT,
    time_added TIMESTAMP,

    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_user_book (user_id, book_id)
);

CREATE TABLE review
(
    book_id    INT,
    user_id    INT,
    review     VARCHAR(1000),
    time_added TIMESTAMP,

    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE favourite
(
    book_id    INT,
    user_id    INT,
    time_added TIMESTAMP,

    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Add admin user (type 0)
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('admin', 'admin', 0, 'UET', 'VNU', 'abc@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1000 DAY), 1);

-- Add 3 sample users (type 1)
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021497', '23021497', 1, 'Quang Dung', 'Nguyen', '23021497@vnu.edu.vn', CURRENT_TIMESTAMP, 1);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021501', '23021501', 1, 'Anh Duy', 'Le', '23021501@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021521', '23021521', 1, 'Tien Dat', 'Nguyen', '23021521@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY), 1);

-- Add 2 sample books
INSERT INTO book(name, author, description, time_added, copies)
    VALUES ('Book Number 1', 'Author #1', 'Very long description #1', CURRENT_TIMESTAMP(), 10);
INSERT INTO book(name, author, description, time_added, copies)
    VALUES ('Book Number 2', 'Author #2', 'Very long description #2', CURRENT_TIMESTAMP(), 5);
INSERT INTO book(name, author, description, time_added, copies)
    VALUES ('Book Number 3', 'Author #3', 'Very long description #3', CURRENT_TIMESTAMP(), 1);

-- Add sample tags
INSERT INTO tag (name, bg_color_hex, text_color_hex) VALUES
('Fiction', '#FFB3BA', '#5A5A5A'),
('Non-Fiction', '#BAE1FF', '#4A4A4A'),
('Science', '#BFFCC6', '#3A3A3A'),
('History', '#FFDFBA', '#5A5A5A'),
('Fantasy', '#D5BAFF', '#4A4A4A'),
('Biography', '#FFFFBA', '#3A3A3A'),
('Mystery', '#BAFFC9', '#4A4A4A'),
('Romance', '#FFC2BA', '#5A5A5A'),
('Horror', '#C1C1C1', '#2A2A2A'),
('Adventure', '#FFFAC8', '#3A3A3A');

-- Add sample book tags
INSERT INTO book_tag(book_id, tag) VALUES
(1, 'Fiction'),
(1, 'Science'),
(1, 'Mystery'),
(2, 'Non-fiction'),
(2, 'Biography'),
(2, 'Science'),
(3, 'Fiction'),
(3, 'Adventure'),
(3, 'Horror');

-- Add sample ratings
INSERT INTO rating(user_id, book_id, rating, time_added)
VALUES (1, 1, 4, CURRENT_TIMESTAMP);

INSERT INTO rating(user_id, book_id, rating, time_added)
VALUES (2, 1, 1, CURRENT_TIMESTAMP);

-- Add sample reviews
INSERT INTO review(book_id, user_id, review, time_added) VALUES (1, 2, 'This good', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MINUTE));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (1, 3, 'This sucks', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (2, 1, 'This meh', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (3, 1, 'This ok', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));

-- Add sample request issues
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (CURRENT_TIMESTAMP, 3, 2, '23021497', 1, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (CURRENT_TIMESTAMP, 7, 3, '23021501', 1, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (CURRENT_TIMESTAMP, 1, 3, '23021501', 2, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (CURRENT_TIMESTAMP, 7, 4, '23021521', 1, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (CURRENT_TIMESTAMP, 2, 4, '23021521', 2, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (CURRENT_TIMESTAMP, 10, 4, '23021521', 3, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), 10, 4, '23021521', 3, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 1, 3, '23021501', 2, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 3, 2, '23021497', 1, 'Missing');

SELECT * FROM user;