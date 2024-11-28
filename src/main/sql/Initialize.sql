DROP TABLE IF EXISTS issue;
DROP TABLE IF EXISTS book_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS favourite;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS game_history;
DROP TABLE IF EXISTS questions;

CREATE TABLE book
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100)  NULL,
    author      VARCHAR(100)  NULL,
    description VARCHAR(5000) NULL,
    time_added  TIMESTAMP     NULL,
    copies      INT           NULL
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
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_user_book (user_id, book_id)
);

CREATE TABLE favourite
(
    book_id    INT,
    user_id    INT,
    time_added TIMESTAMP,

    FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_user_book (user_id, book_id)
);

CREATE TABLE game_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    score INT NOT NULL,
    question_answered INT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);

CREATE TABLE questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(20),
    difficulty VARCHAR(20),
    question VARCHAR(200),
    correct_answer VARCHAR(200),
    incr_ans1 VARCHAR(200),
    incr_ans2 VARCHAR(200),
    incr_ans3 VARCHAR(200)
);

-- Add admin user (type 0)
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('admin', 'admin', 0, 'UET', 'VNU', 'adminuet@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1000 DAY), 1);

-- Add 3 sample users (type 1)
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021497', '23021497', 1, 'Quang Dung', 'Nguyen', '23021497@vnu.edu.vn', CURRENT_TIMESTAMP, 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021501', '23021501', 1, 'Anh Duy', 'Le', '23021501@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021521', '23021521', 1, 'Tien Dat', 'Nguyen', '23021521@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021485', '23021485', 1, 'Duc Cuong', 'Le', '23021485@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021589', '23021589', 1, 'Trung Kien', 'Nguyen', '23021589@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021465', '23021465', 1, 'Nguyen Anh', 'Le', '23021465@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021541', '23021541', 1, 'Viet Ha', 'Nguyen', '23021541@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021553', '23021553', 1, 'Trung Hieu', 'Pham', '23021553@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021597', '23021597', 1, 'An Khanh', 'Pham Hoang', '23021597@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021645', '23021645', 1, 'Thanh Nam', 'Quach', '23021645@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021721', '23021721', 1, 'Phuong Thao', 'Do', '23021721@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021733', '23021733', 1, 'Hoai Thuong', 'Nguyen Doan', '23021733@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY), 0);



-- Add sample books
INSERT INTO book(name, author, description, time_added, copies) VALUES
('A Brief History of Time', 'Stephen Hawking', 'A landmark volume in popular science.', CURRENT_TIMESTAMP(), 15),
('The Great Gatsby', 'F. Scott Fitzgerald', 'A story of the Jazz Age and lost dreams.', CURRENT_TIMESTAMP(), 10),
('1984', 'George Orwell', 'A dystopian novel about totalitarianism.', CURRENT_TIMESTAMP(), 8),
('Pride and Prejudice', 'Jane Austen', 'A classic tale of love and societal expectations.', CURRENT_TIMESTAMP(), 12),
('To Kill a Mockingbird', 'Harper Lee', 'A profound reflection on racism and justice.', CURRENT_TIMESTAMP(), 9),
('The Catcher in the Rye', 'J.D. Salinger', 'A story of teenage rebellion and angst.', CURRENT_TIMESTAMP(), 11),
('The Road', 'Cormac McCarthy', 'A haunting tale of a post-apocalyptic world.', CURRENT_TIMESTAMP(), 7),
('Dune', 'Frank Herbert', 'An epic saga of politics and survival on a desert planet.', CURRENT_TIMESTAMP(), 10),
('Sapiens', 'Yuval Noah Harari', 'A sweeping history of humankind.', CURRENT_TIMESTAMP(), 14),
('Becoming', 'Michelle Obama', 'A memoir by the former First Lady.', CURRENT_TIMESTAMP(), 18),
('The Hobbit', 'J.R.R. Tolkien', 'A fantasy adventure in Middle-earth.', CURRENT_TIMESTAMP(), 20),
('The Da Vinci Code', 'Dan Brown', 'A mystery thriller involving symbology and secrets.', CURRENT_TIMESTAMP(), 13),
('Frankenstein', 'Mary Shelley', 'A gothic novel about ambition and consequences.', CURRENT_TIMESTAMP(), 6),
('Dracula', 'Bram Stoker', 'A tale of vampires and gothic horror.', CURRENT_TIMESTAMP(), 8),
('Moby Dick', 'Herman Melville', 'An epic tale of obsession and revenge at sea.', CURRENT_TIMESTAMP(), 10),
('War and Peace', 'Leo Tolstoy', 'A monumental work on war and human nature.', CURRENT_TIMESTAMP(), 5),
('The Picture of Dorian Gray', 'Oscar Wilde', 'A philosophical exploration of vanity and morality.', CURRENT_TIMESTAMP(), 9),
('The Shining', 'Stephen King', 'A chilling tale of isolation and madness.', CURRENT_TIMESTAMP(), 7),
('The Alchemist', 'Paulo Coelho', 'A journey to self-discovery and destiny.', CURRENT_TIMESTAMP(), 20),
('Animal Farm', 'George Orwell', 'A satirical tale about power and corruption.', CURRENT_TIMESTAMP(), 15),
('Gone Girl', 'Gillian Flynn', 'A thriller with twists and dark secrets.', CURRENT_TIMESTAMP(), 12),
('The Silent Patient', 'Alex Michaelides', 'A psychological thriller about a silent artist.', CURRENT_TIMESTAMP(), 10),
('It', 'Stephen King', 'A terrifying story of childhood fears.', CURRENT_TIMESTAMP(), 8),
('Brave New World', 'Aldous Huxley', 'A dystopian vision of a future society.', CURRENT_TIMESTAMP(), 12),
('The Kite Runner', 'Khaled Hosseini', 'A poignant tale of friendship and redemption.', CURRENT_TIMESTAMP(), 16),
('Circe', 'Madeline Miller', 'A retelling of Greek mythology centered on Circe.', CURRENT_TIMESTAMP(), 14),
('Les Misérables', 'Victor Hugo', 'A sweeping story of love and revolution.', CURRENT_TIMESTAMP(), 6),
('The Fault in Our Stars', 'John Green', 'A love story between two teens facing challenges.', CURRENT_TIMESTAMP(), 18),
('The Hunger Games', 'Suzanne Collins', 'A dystopian story of survival and rebellion.', CURRENT_TIMESTAMP(), 20),
('A Lost Lady', 'Willa Cather', 'Marian Forrester is the symbolic flower of the Old American West. She draws her strength from that solid foundation, bringing delight and beauty to her elderly husband, to the small town of Sweet Water where they live, to the prairie land itself, and to the young narrator of her story, Neil Herbert.', CURRENT_TIMESTAMP(), 15),
('The Martian', 'Andy Weir', 'A gripping survival story set on Mars.', CURRENT_TIMESTAMP(), 12),
('Fahrenheit 451', 'Ray Bradbury', 'A dystopian novel about censorship and knowledge.', CURRENT_TIMESTAMP(), 10),
('The Girl on the Train', 'Paula Hawkins', 'A psychological thriller with an unreliable narrator.', CURRENT_TIMESTAMP(), 9),
('Little Women', 'Louisa May Alcott', 'A classic tale of sisterhood and growth.', CURRENT_TIMESTAMP(), 14),
('The Handmaid\'s Tale', 'Margaret Atwood', 'A dystopian story of a totalitarian regime.', CURRENT_TIMESTAMP(), 8),
('Crime and Punishment', 'Fyodor Dostoevsky', 'A psychological exploration of guilt and redemption.', CURRENT_TIMESTAMP(), 7),
('The Outsiders', 'S.E. Hinton', 'A coming-of-age story about societal divisions.', CURRENT_TIMESTAMP(), 11),
('Percy Jackson & the Olympians', 'Rick Riordan', 'An adventurous blend of modern life and Greek myths.', CURRENT_TIMESTAMP(), 20),
('The Night Circus', 'Erin Morgenstern', 'A magical tale of a mysterious circus.', CURRENT_TIMESTAMP(), 10),
('The Goldfinch', 'Donna Tartt', 'A coming-of-age story with art and tragedy.', CURRENT_TIMESTAMP(), 8),
('The Book Thief', 'Markus Zusak', 'A story narrated by Death during World War II.', CURRENT_TIMESTAMP(), 13),
('Educated', 'Tara Westover', 'A memoir of overcoming isolation through education.', CURRENT_TIMESTAMP(), 9),
('The Shadow of the Wind', 'Carlos Ruiz Zafón', 'A literary mystery in post-war Barcelona.', CURRENT_TIMESTAMP(), 7),
('Where the Crawdads Sing', 'Delia Owens', 'A story of survival and mystery in the marsh.', CURRENT_TIMESTAMP(), 12),
('The Seven Husbands of Evelyn Hugo', 'Taylor Jenkins Reid', 'A story of fame, love, and secrets.', CURRENT_TIMESTAMP(), 16),
('The Name of the Wind', 'Patrick Rothfuss', 'The epic tale of a legendary musician and magician.', CURRENT_TIMESTAMP(), 15),
('The House of the Spirits', 'Isabel Allende', 'A multi-generational story with magical realism.', CURRENT_TIMESTAMP(), 10),
('The Secret History', 'Donna Tartt', 'A psychological exploration of crime and consequence.', CURRENT_TIMESTAMP(), 11),
('The Midnight Library', 'Matt Haig', 'A novel about all the lives we could live and the choices we make. A magical journey through parallel lives.', CURRENT_TIMESTAMP(), 12),
('The 48 Laws of Power', 'Robert Greene', '"The 48 Laws of Power" by Robert Greene outlines 48 principles for gaining and maintaining power, based on the tactics of historical leaders. The book covers strategies like manipulation and influence, offering controversial yet practical advice on navigating power dynamics in business and life.', CURRENT_TIMESTAMP(), 20);

-- Add sample tags
INSERT INTO tag (name, bg_color_hex, text_color_hex) VALUES
('Fiction', '#FFB3BA', '#5A5A5A'),
('Non-fiction', '#BAE1FF', '#4A4A4A'),
('Science', '#BFFCC6', '#3A3A3A'),
('History', '#FFDFBA', '#5A5A5A'),
('Fantasy', '#D5BAFF', '#4A4A4A'),
('Biography', '#FFFFBA', '#3A3A3A'),
('Mystery', '#BAFFC9', '#4A4A4A'),
('Romance', '#FFC2BA', '#5A5A5A'),
('Horror', '#C1C1C1', '#2A2A2A'),
('Adventure', '#FFFAC8', '#3A3A3A'),
('Classic', '#FFD6A5', '#5A5A5A'),
('Thriller', '#FFB4D9', '#4A4A4A'),
('Dystopian', '#B9FBC0', '#3A3A3A'),
('Drama', '#FFCCD5', '#5A5A5A'),
('Philosophy', '#C5B4E3', '#4A4A4A'),
('Inspirational', '#FFC4B8', '#5A5A5A'),
('Self-help', '#FFF2A0', '#4A4A4A'),
('Psychological', '#FFABAB', '#5A5A5A'),
('Literary Fiction', '#B5EAD7', '#4A4A4A'),
('Young Adult', '#FFC8A2', '#5A5A5A'),
('Post-apocalyptic', '#E2F0CB', '#3A3A3A'),
('Epic', '#FFE1A8', '#4A4A4A'),
('Mythology', '#B4A7E7', '#4A4A4A'),
('Humor', '#FFCEB5', '#5A5A5A'),
('Suspense', '#BAE8F9', '#4A4A4A'),
('Anthropology', '#F4CCF4', '#4A4A4A'),
('Political', '#D3ECA7', '#4A4A4A'),
('Literature', '#FFDAC1', '#5A5A5A'),
('Magical Realism', '#D5E1DD', '#3A3A3A'),
('Historical', '#FFDFBA', '#5A5A5A'),
('Coming of Age', '#FFE1A8', '#4A4A4A'),
('Survival', '#C1C1C1', '#2A2A2A'),
('Memoir', '#FFFAC8', '#3A3A3A'),
('Crime', '#D3ECA7', '#4A4A4A'),
('Friendship', '#FFC2BA', '#5A5A5A'),
('Gothic', '#C1C1C1', '#2A2A2A'),
('Philosophical', '#C5B4E3', '#4A4A4A'),
('Physics', '#B9FBC0', '#3A3A3A'),
('Satire', '#FFB4D9', '#4A4A4A'),
('Science Fiction', '#BFFCC6', '#3A3A3A');

-- Add sample book tags
INSERT INTO book_tag (book_id, tag) VALUES
(1, 'Science'),
(1, 'Non-fiction'),
(1, 'Physics'),
(2, 'Classic'),
(2, 'Literary Fiction'),
(2, 'Romance'),
(3, 'Dystopian'),
(3, 'Classic'),
(3, 'Political'),
(4, 'Classic'),
(4, 'Romance'),
(4, 'Drama'),
(5, 'Classic'),
(5, 'Historical'),
(5, 'Drama'),
(6, 'Classic'),
(6, 'Coming of Age'),
(6, 'Psychological'),
(7, 'Post-apocalyptic'),
(7, 'Drama'),
(7, 'Survival'),
(8, 'Science Fiction'),
(8, 'Fantasy'),
(8, 'Epic'),
(9, 'Non-fiction'),
(9, 'History'),
(9, 'Anthropology'),
(10, 'Memoir'),
(10, 'Inspirational'),
(10, 'Non-fiction'),
(11, 'Fantasy'),
(11, 'Adventure'),
(11, 'Classic'),
(12, 'Mystery'),
(12, 'Thriller'),
(12, 'Adventure'),
(13, 'Gothic'),
(13, 'Science Fiction'),
(13, 'Horror'),
(14, 'Gothic'),
(14, 'Classic'),
(14, 'Horror'),
(15, 'Classic'),
(15, 'Adventure'),
(15, 'Epic'),
(16, 'Classic'),
(16, 'Historical'),
(16, 'Philosophical'),
(17, 'Philosophy'),
(17, 'Classic'),
(17, 'Fantasy'),
(18, 'Horror'),
(18, 'Psychological'),
(18, 'Suspense'),
(19, 'Philosophy'),
(19, 'Inspirational'),
(19, 'Self-help'),
(20, 'Satire'),
(20, 'Political'),
(20, 'Classic'),
(21, 'Thriller'),
(21, 'Psychological'),
(21, 'Mystery'),
(22, 'Psychological'),
(22, 'Thriller'),
(22, 'Suspense'),
(23, 'Horror'),
(23, 'Adventure'),
(23, 'Coming of Age'),
(24, 'Dystopian'),
(24, 'Science Fiction'),
(24, 'Political'),
(25, 'Drama'),
(25, 'Friendship'),
(25, 'Historical'),
(26, 'Mythology'),
(26, 'Fantasy'),
(26, 'Adventure'),
(27, 'Classic'),
(27, 'Romance'),
(27, 'Drama'),
(28, 'Romance'),
(28, 'Drama'),
(28, 'Young Adult'),
(29, 'Dystopian'),
(29, 'Science Fiction'),
(29, 'Adventure'),
(30, 'Fantasy'),
(30, 'Adventure'),
(30, 'Epic'),
(31, 'Science Fiction'),
(31, 'Adventure'),
(31, 'Humor'),
(32, 'Classic'),
(32, 'Science Fiction'),
(32, 'Philosophical'),
(33, 'Mystery'),
(33, 'Thriller'),
(33, 'Psychological'),
(34, 'Classic'),
(34, 'Drama'),
(34, 'Historical'),
(35, 'Dystopian'),
(35, 'Science Fiction'),
(35, 'Political'),
(36, 'Classic'),
(36, 'Coming of Age'),
(36, 'Drama'),
(37, 'Psychological'),
(37, 'Drama'),
(37, 'Crime'),
(38, 'Fantasy'),
(38, 'Adventure'),
(38, 'Young Adult'),
(39, 'Fantasy'),
(39, 'Romance'),
(39, 'Mystery'),
(40, 'Literature'),
(40, 'Drama'),
(40, 'Psychological'),
(41, 'Historical'),
(41, 'Drama'),
(41, 'Romance'),
(42, 'Memoir'),
(42, 'Non-fiction'),
(42, 'Inspirational'),
(43, 'Literary Fiction'),
(43, 'Mystery'),
(43, 'Drama'),
(44, 'Mystery'),
(44, 'Psychological'),
(44, 'Thriller'),
(45, 'Fantasy'),
(45, 'Epic'),
(45, 'Adventure'),
(46, 'Magical Realism'),
(46, 'Drama'),
(46, 'Historical'),
(47, 'Psychological'),
(47, 'Thriller'),
(47, 'Mystery'),
(48, 'Fantasy'),
(48, 'Philosophical'),
(48, 'Magical Realism'),
(48, 'Drama'),
(49, 'Philosophy'),
(49, 'Adventure'),
(49, 'Inspirational'),
(49, 'Literary Fiction'),
(50, 'Mythology'),
(50, 'Fantasy'),
(50, 'Epic'),
(50, 'Drama');

-- Add sample ratings
INSERT INTO rating(user_id, book_id, rating, time_added) VALUES
(1, 1, 4, CURRENT_TIMESTAMP),
(2, 1, 1, CURRENT_TIMESTAMP);

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