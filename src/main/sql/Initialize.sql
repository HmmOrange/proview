DROP TABLE IF EXISTS issue;
DROP TABLE IF EXISTS book_tag;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS favourite;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS game_history;

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
VALUES ('23021597', '23021597', 1, 'An Khanh', 'Pham', '23021597@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021645', '23021645', 1, 'Thanh Nam', 'Quach', '23021645@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021721', '23021721', 1, 'Phuong Thao', 'Do', '23021721@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY), 0);
INSERT INTO user(username, password, type, firstName, lastName, email, registration_date, card_view)
VALUES ('23021733', '23021733', 1, 'Hoai Thuong', 'Nguyen', '23021733@vnu.edu.vn', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY), 0);



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
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 5, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 6, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 7, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 8, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 9, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 12, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 14, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 16, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 18, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 19, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 20, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 21, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 24, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 25, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 28, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 31, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 32, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 33, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 35, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 37, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 38, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 39, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 43, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (2, 49, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 1, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 3, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 7, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 10, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 11, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 12, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 13, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 14, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 16, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 19, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 20, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 22, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 24, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 28, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 30, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 32, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 34, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 35, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 36, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 37, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 39, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 40, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 43, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 45, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 46, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 47, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (3, 49, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 1, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 2, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 4, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 5, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 6, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 7, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 9, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 10, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 11, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 13, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 15, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 16, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 17, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 20, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 26, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 27, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 30, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 31, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 32, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 34, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 35, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 36, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 38, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 40, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 42, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 43, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 44, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 45, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 46, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 47, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (4, 50, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 1, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 3, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 4, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 6, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 8, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 9, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 11, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 12, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 13, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 18, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 20, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 21, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 24, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 28, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 29, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 30, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 31, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 33, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 35, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 38, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 40, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 41, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 46, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 48, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 49, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (5, 50, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 1, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 2, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 4, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 7, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 11, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 12, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 13, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 14, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 15, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 18, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 19, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 26, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 27, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 28, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 29, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 31, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 33, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 36, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 38, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 39, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 41, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 42, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 43, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 46, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 48, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 49, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (6, 50, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 1, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 2, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 3, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 5, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 7, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 8, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 9, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 13, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 14, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 19, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 24, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 25, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 27, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 29, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 36, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 38, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 40, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 41, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (7, 47, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 3, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 5, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 7, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 8, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 9, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 10, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 12, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 14, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 21, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 24, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 25, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 26, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 27, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 30, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 31, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 32, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 33, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 35, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 38, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 39, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 40, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 41, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 42, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 45, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 46, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 48, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (8, 50, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 1, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 2, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 3, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 4, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 6, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 9, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 10, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 12, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 14, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 15, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 16, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 17, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 18, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 19, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 21, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 22, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 24, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 25, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 26, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 27, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 32, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 33, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 34, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 35, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 36, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 38, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 39, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 41, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 42, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 44, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 45, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 48, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 49, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (9, 50, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 1, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 2, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 3, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 4, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 7, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 8, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 10, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 11, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 12, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 13, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 14, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 18, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 19, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 20, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 23, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 24, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 26, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 27, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 29, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 32, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 34, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 35, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 43, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 44, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 46, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 47, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (10, 49, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 1, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 3, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 6, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 7, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 9, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 12, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 14, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 15, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 17, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 21, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 22, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 23, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 24, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 27, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 29, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 31, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 32, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 33, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 35, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 36, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 39, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 40, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 41, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 43, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 44, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 45, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (11, 46, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 1, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 6, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 7, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 9, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 11, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 12, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 14, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 15, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 17, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 18, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 20, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 23, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 24, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 27, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 28, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 30, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 32, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 34, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 36, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 37, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 38, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 39, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 40, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 41, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 42, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 43, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 46, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 47, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (12, 49, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 1, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 3, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 4, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 5, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 7, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 10, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 13, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 15, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 19, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 25, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 30, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 31, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 34, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 36, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO rating (user_id, book_id, rating, time_added) VALUES (13, 48, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));



-- Add sample reviews
INSERT INTO review(book_id, user_id, review, time_added) VALUES (1, 2, 'An exceptionally well-written book that provides deep insights into its subject matter. A highly recommended read.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MINUTE));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (1, 3, 'A fantastic book that combines thorough research with engaging storytelling. Truly a great addition to any library.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (2, 2, 'The book covers important topics but lacks depth in some areas. Overall, it is informative but not particularly engaging.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (3, 3, 'A solid read with practical insights and a clear structure. While not groundbreaking, it offers value to its audience.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (5, 1, 'This book provides a well-rounded perspective on the topic, and the author’s writing style is both engaging and informative.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (12, 2, 'An exceptional piece of literature that delves deeply into its subject matter. Highly recommended for curious minds.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 WEEK));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (22, 3, 'The narrative flows effortlessly, making it an enjoyable and thought-provoking read. A must-have for enthusiasts.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (8, 4, 'A thoroughly researched and well-written book that offers fresh insights and practical advice on the topic.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 MINUTE));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (17, 5, 'This book stands out for its clarity and depth, making complex ideas accessible to a wider audience.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (33, 6, 'The author has done an excellent job of blending compelling storytelling with valuable insights.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (24, 7, 'An insightful read with actionable takeaways, presented in a manner that is both engaging and educational.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 SECOND));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (19, 8, 'This book challenges conventional thinking and offers a refreshing perspective on the subject matter.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (15, 9, 'A captivating read that combines thorough research with a clear and concise writing style.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (41, 10, 'A masterfully written book that leaves a lasting impression. The depth of knowledge is truly remarkable.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (3, 11, 'The author’s expertise shines through in this work, making it a valuable resource for readers.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (26, 12, 'This book strikes the perfect balance between entertainment and information, making it a pleasure to read.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (50, 13, 'An enlightening book that manages to both educate and inspire, leaving the reader with a sense of purpose.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 SECOND));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (2, 1, 'A fantastic read that goes beyond surface-level analysis to uncover deeper truths.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (18, 3, 'The writing style is so engaging that it’s hard to put this book down. A true masterpiece.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (30, 4, 'A thought-provoking book that opens up new ways of thinking and approaches to life.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 MINUTE));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (46, 6, 'This book is filled with practical advice and fresh ideas that make it stand out from the crowd.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (35, 9, 'The depth of research and the clarity of presentation make this book a valuable addition to any library.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (7, 11, 'The way the author weaves facts with compelling narratives makes this book a joy to read.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 HOUR));
INSERT INTO review(book_id, user_id, review, time_added) VALUES (13, 8, 'This book presents a unique perspective that is both refreshing and intellectually stimulating.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 MINUTE));


-- Add sample request issues

INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY), 58, 1, (SELECT username FROM user WHERE id = 1), 1, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY), 67, 2, (SELECT username FROM user WHERE id = 2), 1, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY), 61, 3, (SELECT username FROM user WHERE id = 3), 1, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), 68, 4, (SELECT username FROM user WHERE id = 4), 1, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 DAY), 6, 5, (SELECT username FROM user WHERE id = 5), 1, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY), 41, 6, (SELECT username FROM user WHERE id = 6), 1, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 61 DAY), 53, 8, (SELECT username FROM user WHERE id = 8), 1, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 82 DAY), 6, 9, (SELECT username FROM user WHERE id = 9), 1, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY), 11, 10, (SELECT username FROM user WHERE id = 10), 1, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY), 45, 11, (SELECT username FROM user WHERE id = 11), 1, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY), 34, 12, (SELECT username FROM user WHERE id = 12), 1, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 69 DAY), 80, 13, (SELECT username FROM user WHERE id = 13), 1, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 49 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 65, 3, (SELECT username FROM user WHERE id = 3), 2, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY), 77, 4, (SELECT username FROM user WHERE id = 4), 2, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 53 DAY), 19, 10, (SELECT username FROM user WHERE id = 10), 2, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 63 DAY), 23, 11, (SELECT username FROM user WHERE id = 11), 2, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 88, 12, (SELECT username FROM user WHERE id = 12), 2, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 79 DAY), 66, 13, (SELECT username FROM user WHERE id = 13), 2, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 79 DAY), 89, 1, (SELECT username FROM user WHERE id = 1), 3, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 85 DAY), 24, 3, (SELECT username FROM user WHERE id = 3), 3, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 33 DAY), 27, 4, (SELECT username FROM user WHERE id = 4), 3, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY), 13, 5, (SELECT username FROM user WHERE id = 5), 3, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 49 DAY), 24, 7, (SELECT username FROM user WHERE id = 7), 3, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 43 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 47 DAY), 57, 9, (SELECT username FROM user WHERE id = 9), 3, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 73 DAY), 5, 2, (SELECT username FROM user WHERE id = 2), 4, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 90 DAY), 23, 3, (SELECT username FROM user WHERE id = 3), 4, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY), 63, 9, (SELECT username FROM user WHERE id = 9), 4, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 74 DAY), 64, 10, (SELECT username FROM user WHERE id = 10), 4, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 43 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 53 DAY), 20, 11, (SELECT username FROM user WHERE id = 11), 4, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 59 DAY), 73, 13, (SELECT username FROM user WHERE id = 13), 4, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 90 DAY), 59, 1, (SELECT username FROM user WHERE id = 1), 5, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY), 71, 3, (SELECT username FROM user WHERE id = 3), 5, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 44 DAY), 20, 4, (SELECT username FROM user WHERE id = 4), 5, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 43 DAY), 64, 6, (SELECT username FROM user WHERE id = 6), 5, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY), 75, 8, (SELECT username FROM user WHERE id = 8), 5, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 62 DAY), 81, 9, (SELECT username FROM user WHERE id = 9), 5, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 49 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 4, 11, (SELECT username FROM user WHERE id = 11), 5, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 75 DAY), 11, 2, (SELECT username FROM user WHERE id = 2), 6, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 27, 4, (SELECT username FROM user WHERE id = 4), 6, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 55 DAY), 33, 5, (SELECT username FROM user WHERE id = 5), 6, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY), 57, 7, (SELECT username FROM user WHERE id = 7), 6, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 68 DAY), 1, 8, (SELECT username FROM user WHERE id = 8), 6, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 85 DAY), 12, 9, (SELECT username FROM user WHERE id = 9), 6, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 53 DAY), 10, 10, (SELECT username FROM user WHERE id = 10), 6, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 61 DAY), 44, 11, (SELECT username FROM user WHERE id = 11), 6, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 71 DAY), 84, 1, (SELECT username FROM user WHERE id = 1), 7, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 82 DAY), 24, 2, (SELECT username FROM user WHERE id = 2), 7, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 61 DAY), 54, 5, (SELECT username FROM user WHERE id = 5), 7, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY), 28, 6, (SELECT username FROM user WHERE id = 6), 7, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY), 50, 8, (SELECT username FROM user WHERE id = 8), 7, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 50 DAY), 85, 10, (SELECT username FROM user WHERE id = 10), 7, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY), 73, 11, (SELECT username FROM user WHERE id = 11), 7, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY), 10, 12, (SELECT username FROM user WHERE id = 12), 7, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY), 66, 13, (SELECT username FROM user WHERE id = 13), 7, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 50 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 59 DAY), 44, 3, (SELECT username FROM user WHERE id = 3), 8, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 51 DAY), 54, 5, (SELECT username FROM user WHERE id = 5), 8, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 75 DAY), 78, 6, (SELECT username FROM user WHERE id = 6), 8, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 87 DAY), 23, 7, (SELECT username FROM user WHERE id = 7), 8, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 54 DAY), 55, 8, (SELECT username FROM user WHERE id = 8), 8, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 63 DAY), 21, 9, (SELECT username FROM user WHERE id = 9), 8, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 59 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY), 46, 10, (SELECT username FROM user WHERE id = 10), 8, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 44 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY), 41, 13, (SELECT username FROM user WHERE id = 13), 8, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 77 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 72 DAY), 27, 1, (SELECT username FROM user WHERE id = 1), 9, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY), 30, 4, (SELECT username FROM user WHERE id = 4), 9, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 83 DAY), 50, 10, (SELECT username FROM user WHERE id = 10), 9, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 70 DAY), 44, 11, (SELECT username FROM user WHERE id = 11), 9, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 56 DAY), 70, 13, (SELECT username FROM user WHERE id = 13), 9, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 49 DAY), 23, 1, (SELECT username FROM user WHERE id = 1), 10, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 81 DAY), 25, 2, (SELECT username FROM user WHERE id = 2), 10, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 61 DAY), 37, 7, (SELECT username FROM user WHERE id = 7), 10, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY), 43, 8, (SELECT username FROM user WHERE id = 8), 10, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 81 DAY), 49, 13, (SELECT username FROM user WHERE id = 13), 10, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 71 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY), 78, 2, (SELECT username FROM user WHERE id = 2), 11, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 59 DAY), 46, 6, (SELECT username FROM user WHERE id = 6), 11, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 5, 7, (SELECT username FROM user WHERE id = 7), 11, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 50 DAY), 51, 9, (SELECT username FROM user WHERE id = 9), 11, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY), 12, 12, (SELECT username FROM user WHERE id = 12), 11, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 52 DAY), 50, 3, (SELECT username FROM user WHERE id = 3), 12, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY), 86, 4, (SELECT username FROM user WHERE id = 4), 12, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 28, 5, (SELECT username FROM user WHERE id = 5), 12, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY), 78, 9, (SELECT username FROM user WHERE id = 9), 12, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY), 79, 12, (SELECT username FROM user WHERE id = 12), 12, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY), 84, 1, (SELECT username FROM user WHERE id = 1), 13, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY), 18, 3, (SELECT username FROM user WHERE id = 3), 13, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 62 DAY), 51, 5, (SELECT username FROM user WHERE id = 5), 13, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 81 DAY), 85, 9, (SELECT username FROM user WHERE id = 9), 13, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY), 85, 11, (SELECT username FROM user WHERE id = 11), 13, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 54 DAY), 45, 12, (SELECT username FROM user WHERE id = 12), 13, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 41 DAY), 85, 5, (SELECT username FROM user WHERE id = 5), 14, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY), 30, 8, (SELECT username FROM user WHERE id = 8), 14, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 77 DAY), 32, 11, (SELECT username FROM user WHERE id = 11), 14, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY), 78, 12, (SELECT username FROM user WHERE id = 12), 14, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY), 76, 13, (SELECT username FROM user WHERE id = 13), 14, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 64 DAY), 56, 1, (SELECT username FROM user WHERE id = 1), 15, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY), 79, 8, (SELECT username FROM user WHERE id = 8), 15, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY), 33, 9, (SELECT username FROM user WHERE id = 9), 15, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 60, 10, (SELECT username FROM user WHERE id = 10), 15, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY), 75, 12, (SELECT username FROM user WHERE id = 12), 15, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY), 89, 13, (SELECT username FROM user WHERE id = 13), 15, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY), 36, 2, (SELECT username FROM user WHERE id = 2), 16, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY), 38, 3, (SELECT username FROM user WHERE id = 3), 16, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY), 50, 4, (SELECT username FROM user WHERE id = 4), 16, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 11, 5, (SELECT username FROM user WHERE id = 5), 16, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 71 DAY), 65, 9, (SELECT username FROM user WHERE id = 9), 16, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY), 24, 12, (SELECT username FROM user WHERE id = 12), 16, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY), 49, 1, (SELECT username FROM user WHERE id = 1), 17, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 33 DAY), 39, 2, (SELECT username FROM user WHERE id = 2), 17, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY), 52, 3, (SELECT username FROM user WHERE id = 3), 17, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY), 86, 5, (SELECT username FROM user WHERE id = 5), 17, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY), 86, 6, (SELECT username FROM user WHERE id = 6), 17, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY), 38, 7, (SELECT username FROM user WHERE id = 7), 17, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 41 DAY), 33, 8, (SELECT username FROM user WHERE id = 8), 17, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 88 DAY), 29, 2, (SELECT username FROM user WHERE id = 2), 18, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY), 10, 6, (SELECT username FROM user WHERE id = 6), 18, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 59, 8, (SELECT username FROM user WHERE id = 8), 18, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY), 3, 10, (SELECT username FROM user WHERE id = 10), 18, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY), 70, 12, (SELECT username FROM user WHERE id = 12), 18, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY), 84, 13, (SELECT username FROM user WHERE id = 13), 18, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY), 13, 1, (SELECT username FROM user WHERE id = 1), 19, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 71 DAY), 2, 6, (SELECT username FROM user WHERE id = 6), 19, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY), 49, 9, (SELECT username FROM user WHERE id = 9), 19, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 70 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY), 13, 11, (SELECT username FROM user WHERE id = 11), 19, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY), 35, 2, (SELECT username FROM user WHERE id = 2), 20, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 70 DAY), 78, 4, (SELECT username FROM user WHERE id = 4), 20, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY), 80, 5, (SELECT username FROM user WHERE id = 5), 20, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 85 DAY), 59, 6, (SELECT username FROM user WHERE id = 6), 20, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 57 DAY), 48, 7, (SELECT username FROM user WHERE id = 7), 20, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 56 DAY), 44, 10, (SELECT username FROM user WHERE id = 10), 20, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 34 DAY), 12, 13, (SELECT username FROM user WHERE id = 13), 20, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 71 DAY), 51, 3, (SELECT username FROM user WHERE id = 3), 21, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY), 41, 8, (SELECT username FROM user WHERE id = 8), 21, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 89 DAY), 49, 10, (SELECT username FROM user WHERE id = 10), 21, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 89 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 74 DAY), 71, 11, (SELECT username FROM user WHERE id = 11), 21, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 55 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 53 DAY), 26, 12, (SELECT username FROM user WHERE id = 12), 21, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 42 DAY), 17, 13, (SELECT username FROM user WHERE id = 13), 21, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY), 17, 8, (SELECT username FROM user WHERE id = 8), 22, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 73 DAY), 50, 9, (SELECT username FROM user WHERE id = 9), 22, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY), 22, 10, (SELECT username FROM user WHERE id = 10), 22, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 88, 11, (SELECT username FROM user WHERE id = 11), 22, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 63 DAY), 34, 12, (SELECT username FROM user WHERE id = 12), 22, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 48 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 75 DAY), 3, 13, (SELECT username FROM user WHERE id = 13), 22, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 DAY), 78, 1, (SELECT username FROM user WHERE id = 1), 23, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 71 DAY), 20, 3, (SELECT username FROM user WHERE id = 3), 23, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 48 DAY), 42, 4, (SELECT username FROM user WHERE id = 4), 23, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 72 DAY), 33, 6, (SELECT username FROM user WHERE id = 6), 23, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 27, 8, (SELECT username FROM user WHERE id = 8), 23, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 57 DAY), 61, 12, (SELECT username FROM user WHERE id = 12), 23, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY), 45, 13, (SELECT username FROM user WHERE id = 13), 23, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY), 8, 1, (SELECT username FROM user WHERE id = 1), 24, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY), 87, 3, (SELECT username FROM user WHERE id = 3), 24, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY), 67, 4, (SELECT username FROM user WHERE id = 4), 24, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 84 DAY), 60, 9, (SELECT username FROM user WHERE id = 9), 24, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 DAY), 10, 10, (SELECT username FROM user WHERE id = 10), 24, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 55 DAY), 54, 13, (SELECT username FROM user WHERE id = 13), 24, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 43 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY), 9, 1, (SELECT username FROM user WHERE id = 1), 25, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 50 DAY), 16, 2, (SELECT username FROM user WHERE id = 2), 25, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 30, 3, (SELECT username FROM user WHERE id = 3), 25, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY), 37, 6, (SELECT username FROM user WHERE id = 6), 25, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY), 87, 8, (SELECT username FROM user WHERE id = 8), 25, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY), 8, 9, (SELECT username FROM user WHERE id = 9), 25, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 59 DAY), 48, 1, (SELECT username FROM user WHERE id = 1), 26, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 44 DAY), 52, 2, (SELECT username FROM user WHERE id = 2), 26, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 72, 4, (SELECT username FROM user WHERE id = 4), 26, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY), 66, 5, (SELECT username FROM user WHERE id = 5), 26, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY), 43, 6, (SELECT username FROM user WHERE id = 6), 26, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 75 DAY), 71, 7, (SELECT username FROM user WHERE id = 7), 26, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 DAY), 18, 9, (SELECT username FROM user WHERE id = 9), 26, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY), 43, 12, (SELECT username FROM user WHERE id = 12), 26, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 43 DAY), 74, 13, (SELECT username FROM user WHERE id = 13), 26, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 54 DAY), 31, 2, (SELECT username FROM user WHERE id = 2), 27, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 47 DAY), 73, 4, (SELECT username FROM user WHERE id = 4), 27, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 40 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 67 DAY), 71, 5, (SELECT username FROM user WHERE id = 5), 27, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY), 82, 7, (SELECT username FROM user WHERE id = 7), 27, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 52 DAY), 70, 8, (SELECT username FROM user WHERE id = 8), 27, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 49 DAY), 5, 11, (SELECT username FROM user WHERE id = 11), 27, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 34 DAY), 47, 13, (SELECT username FROM user WHERE id = 13), 27, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 61 DAY), 56, 4, (SELECT username FROM user WHERE id = 4), 28, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 54 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 72 DAY), 46, 5, (SELECT username FROM user WHERE id = 5), 28, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 40 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 52 DAY), 6, 7, (SELECT username FROM user WHERE id = 7), 28, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 87 DAY), 43, 8, (SELECT username FROM user WHERE id = 8), 28, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 56 DAY), 67, 12, (SELECT username FROM user WHERE id = 12), 28, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 76 DAY), 57, 13, (SELECT username FROM user WHERE id = 13), 28, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY), 23, 2, (SELECT username FROM user WHERE id = 2), 29, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 61 DAY), 28, 3, (SELECT username FROM user WHERE id = 3), 29, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY), 4, 4, (SELECT username FROM user WHERE id = 4), 29, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 41 DAY), 35, 6, (SELECT username FROM user WHERE id = 6), 29, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 70 DAY), 53, 7, (SELECT username FROM user WHERE id = 7), 29, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY), 32, 9, (SELECT username FROM user WHERE id = 9), 29, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 40 DAY), 60, 10, (SELECT username FROM user WHERE id = 10), 29, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY), 22, 13, (SELECT username FROM user WHERE id = 13), 29, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY), 48, 2, (SELECT username FROM user WHERE id = 2), 30, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY), 35, 10, (SELECT username FROM user WHERE id = 10), 30, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 70 DAY), 22, 11, (SELECT username FROM user WHERE id = 11), 30, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY), 87, 12, (SELECT username FROM user WHERE id = 12), 30, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 54 DAY), 72, 13, (SELECT username FROM user WHERE id = 13), 30, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY), 71, 3, (SELECT username FROM user WHERE id = 3), 31, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 48 DAY), 55, 4, (SELECT username FROM user WHERE id = 4), 31, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 9, 8, (SELECT username FROM user WHERE id = 8), 31, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 45 DAY), 22, 10, (SELECT username FROM user WHERE id = 10), 31, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 82 DAY), 55, 12, (SELECT username FROM user WHERE id = 12), 31, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 76 DAY), 44, 1, (SELECT username FROM user WHERE id = 1), 32, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 67 DAY), 71, 2, (SELECT username FROM user WHERE id = 2), 32, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 34, 3, (SELECT username FROM user WHERE id = 3), 32, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 49 DAY), 28, 7, (SELECT username FROM user WHERE id = 7), 32, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 84 DAY), 56, 8, (SELECT username FROM user WHERE id = 8), 32, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY), 66, 13, (SELECT username FROM user WHERE id = 13), 32, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY), 74, 4, (SELECT username FROM user WHERE id = 4), 33, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 87 DAY), 55, 5, (SELECT username FROM user WHERE id = 5), 33, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 80 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY), 14, 9, (SELECT username FROM user WHERE id = 9), 33, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY), 26, 10, (SELECT username FROM user WHERE id = 10), 33, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 48 DAY), 50, 11, (SELECT username FROM user WHERE id = 11), 33, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 44 DAY), 53, 13, (SELECT username FROM user WHERE id = 13), 33, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 47 DAY), 83, 2, (SELECT username FROM user WHERE id = 2), 34, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 63 DAY), 52, 4, (SELECT username FROM user WHERE id = 4), 34, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY), 59, 7, (SELECT username FROM user WHERE id = 7), 34, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 48 DAY), 13, 9, (SELECT username FROM user WHERE id = 9), 34, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 34 DAY), 62, 12, (SELECT username FROM user WHERE id = 12), 34, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 36, 1, (SELECT username FROM user WHERE id = 1), 35, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 75 DAY), 68, 2, (SELECT username FROM user WHERE id = 2), 35, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 43 DAY), 25, 3, (SELECT username FROM user WHERE id = 3), 35, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY), 20, 4, (SELECT username FROM user WHERE id = 4), 35, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 56 DAY), 52, 5, (SELECT username FROM user WHERE id = 5), 35, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 48 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 63 DAY), 18, 6, (SELECT username FROM user WHERE id = 6), 35, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 88 DAY), 10, 8, (SELECT username FROM user WHERE id = 8), 35, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 83 DAY), 86, 11, (SELECT username FROM user WHERE id = 11), 35, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 69 DAY), 2, 12, (SELECT username FROM user WHERE id = 12), 35, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY), 56, 13, (SELECT username FROM user WHERE id = 13), 35, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 83 DAY), 83, 1, (SELECT username FROM user WHERE id = 1), 36, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 44 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY), 51, 2, (SELECT username FROM user WHERE id = 2), 36, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 87 DAY), 82, 4, (SELECT username FROM user WHERE id = 4), 36, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 50 DAY), 55, 6, (SELECT username FROM user WHERE id = 6), 36, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 33 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY), 33, 8, (SELECT username FROM user WHERE id = 8), 36, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 55 DAY), 13, 12, (SELECT username FROM user WHERE id = 12), 36, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 79 DAY), 28, 13, (SELECT username FROM user WHERE id = 13), 36, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 33 DAY), 28, 3, (SELECT username FROM user WHERE id = 3), 37, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 57 DAY), 5, 5, (SELECT username FROM user WHERE id = 5), 37, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY), 25, 7, (SELECT username FROM user WHERE id = 7), 37, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY), 36, 11, (SELECT username FROM user WHERE id = 11), 37, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 43 DAY), 54, 3, (SELECT username FROM user WHERE id = 3), 38, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY), 8, 5, (SELECT username FROM user WHERE id = 5), 38, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY), 27, 6, (SELECT username FROM user WHERE id = 6), 38, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 64 DAY), 66, 8, (SELECT username FROM user WHERE id = 8), 38, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 49 DAY), 2, 10, (SELECT username FROM user WHERE id = 10), 38, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY), 90, 11, (SELECT username FROM user WHERE id = 11), 38, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 64 DAY), 72, 12, (SELECT username FROM user WHERE id = 12), 38, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 21, 13, (SELECT username FROM user WHERE id = 13), 38, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 42 DAY), 28, 1, (SELECT username FROM user WHERE id = 1), 39, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 80 DAY), 19, 4, (SELECT username FROM user WHERE id = 4), 39, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 55 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 57, 5, (SELECT username FROM user WHERE id = 5), 39, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY), 32, 7, (SELECT username FROM user WHERE id = 7), 39, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 51 DAY), 21, 8, (SELECT username FROM user WHERE id = 8), 39, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY), 26, 9, (SELECT username FROM user WHERE id = 9), 39, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 67 DAY), 89, 2, (SELECT username FROM user WHERE id = 2), 40, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 34 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 85 DAY), 16, 3, (SELECT username FROM user WHERE id = 3), 40, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 43 DAY), 23, 5, (SELECT username FROM user WHERE id = 5), 40, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 47 DAY), 49, 7, (SELECT username FROM user WHERE id = 7), 40, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY), 8, 8, (SELECT username FROM user WHERE id = 8), 40, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY), 14, 13, (SELECT username FROM user WHERE id = 13), 40, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY), 20, 2, (SELECT username FROM user WHERE id = 2), 41, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY), 16, 3, (SELECT username FROM user WHERE id = 3), 41, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY), 50, 4, (SELECT username FROM user WHERE id = 4), 41, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY), 53, 5, (SELECT username FROM user WHERE id = 5), 41, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY), 78, 7, (SELECT username FROM user WHERE id = 7), 41, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY), 36, 8, (SELECT username FROM user WHERE id = 8), 41, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 64 DAY), 9, 12, (SELECT username FROM user WHERE id = 12), 41, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 4, 1, (SELECT username FROM user WHERE id = 1), 42, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 41 DAY), 89, 2, (SELECT username FROM user WHERE id = 2), 42, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 64 DAY), 84, 4, (SELECT username FROM user WHERE id = 4), 42, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY), 60, 5, (SELECT username FROM user WHERE id = 5), 42, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 48 DAY), 85, 7, (SELECT username FROM user WHERE id = 7), 42, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 70, 12, (SELECT username FROM user WHERE id = 12), 42, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY), 8, 1, (SELECT username FROM user WHERE id = 1), 43, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 58, 2, (SELECT username FROM user WHERE id = 2), 43, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY), 84, 3, (SELECT username FROM user WHERE id = 3), 43, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY), 85, 5, (SELECT username FROM user WHERE id = 5), 43, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 78 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 61 DAY), 73, 6, (SELECT username FROM user WHERE id = 6), 43, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 80 DAY), 53, 8, (SELECT username FROM user WHERE id = 8), 43, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 54 DAY), 38, 9, (SELECT username FROM user WHERE id = 9), 43, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY), 79, 10, (SELECT username FROM user WHERE id = 10), 43, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 9, 12, (SELECT username FROM user WHERE id = 12), 43, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY), 18, 13, (SELECT username FROM user WHERE id = 13), 43, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY), 15, 1, (SELECT username FROM user WHERE id = 1), 44, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY), 84, 2, (SELECT username FROM user WHERE id = 2), 44, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY), 88, 3, (SELECT username FROM user WHERE id = 3), 44, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY), 34, 4, (SELECT username FROM user WHERE id = 4), 44, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 38, 5, (SELECT username FROM user WHERE id = 5), 44, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 64 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY), 4, 11, (SELECT username FROM user WHERE id = 11), 44, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY), 49, 13, (SELECT username FROM user WHERE id = 13), 44, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 75 DAY), 61, 2, (SELECT username FROM user WHERE id = 2), 45, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY), 12, 5, (SELECT username FROM user WHERE id = 5), 45, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY), 53, 8, (SELECT username FROM user WHERE id = 8), 45, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY), 73, 9, (SELECT username FROM user WHERE id = 9), 45, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY), 32, 10, (SELECT username FROM user WHERE id = 10), 45, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY), 74, 11, (SELECT username FROM user WHERE id = 11), 45, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 40 DAY), 19, 12, (SELECT username FROM user WHERE id = 12), 45, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 89 DAY), 1, 13, (SELECT username FROM user WHERE id = 13), 45, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 84, 6, (SELECT username FROM user WHERE id = 6), 46, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 74, 7, (SELECT username FROM user WHERE id = 7), 46, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 81 DAY), 31, 9, (SELECT username FROM user WHERE id = 9), 46, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 48 DAY), 3, 10, (SELECT username FROM user WHERE id = 10), 46, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 84 DAY), 28, 2, (SELECT username FROM user WHERE id = 2), 47, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 84 DAY), 89, 5, (SELECT username FROM user WHERE id = 5), 47, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 65 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY), 84, 9, (SELECT username FROM user WHERE id = 9), 47, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY), 23, 11, (SELECT username FROM user WHERE id = 11), 47, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY), 2, 13, (SELECT username FROM user WHERE id = 13), 47, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY), 84, 2, (SELECT username FROM user WHERE id = 2), 48, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY), 45, 3, (SELECT username FROM user WHERE id = 3), 48, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY), 57, 4, (SELECT username FROM user WHERE id = 4), 48, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 64 DAY), 53, 5, (SELECT username FROM user WHERE id = 5), 48, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 82 DAY), 21, 7, (SELECT username FROM user WHERE id = 7), 48, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 66 DAY), 75, 8, (SELECT username FROM user WHERE id = 8), 48, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY), 13, 9, (SELECT username FROM user WHERE id = 9), 48, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 73 DAY), 37, 10, (SELECT username FROM user WHERE id = 10), 48, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 81 DAY), 84, 13, (SELECT username FROM user WHERE id = 13), 48, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 50 DAY), 13, 1, (SELECT username FROM user WHERE id = 1), 49, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY), 82, 2, (SELECT username FROM user WHERE id = 2), 49, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 73 DAY), 69, 3, (SELECT username FROM user WHERE id = 3), 49, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY), 88, 4, (SELECT username FROM user WHERE id = 4), 49, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY), 13, 7, (SELECT username FROM user WHERE id = 7), 49, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 64 DAY), 11, 10, (SELECT username FROM user WHERE id = 10), 49, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 46 DAY), 45, 12, (SELECT username FROM user WHERE id = 12), 49, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 74 DAY), 21, 13, (SELECT username FROM user WHERE id = 13), 49, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY), 26, 2, (SELECT username FROM user WHERE id = 2), 50, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status, end_date) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 56 DAY), 77, 5, (SELECT username FROM user WHERE id = 5), 50, 'Returned', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 44 DAY));
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 58 DAY), 52, 8, (SELECT username FROM user WHERE id = 8), 50, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 42 DAY), 77, 10, (SELECT username FROM user WHERE id = 10), 50, 'Not picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY), 43, 11, (SELECT username FROM user WHERE id = 11), 50, 'Missing');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY), 50, 12, (SELECT username FROM user WHERE id = 12), 50, 'Picked up');
INSERT INTO issue(start_date, duration, user_id, username, book_id, status) VALUES (DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 59 DAY), 45, 13, (SELECT username FROM user WHERE id = 13), 50, 'Not picked up');

-- Add sample favourites
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (1, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (2, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (4, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (6, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (9, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (11, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (12, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (14, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (16, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (20, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (22, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (23, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (25, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (29, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (30, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (31, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (32, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (33, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (34, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (37, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (41, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (45, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (46, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (48, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (49, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (50, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (2, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 34 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (4, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (10, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (11, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (12, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (13, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (18, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (21, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (25, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (27, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (30, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (31, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (33, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (34, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (35, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 33 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (39, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (45, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (47, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (48, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (49, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (50, 3, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (1, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (2, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (3, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (5, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (7, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (9, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (10, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (11, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (13, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (14, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (15, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (19, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (22, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (23, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (27, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (30, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (32, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (33, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (39, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (43, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (44, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (48, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (49, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (1, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (6, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (7, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (8, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (13, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (14, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (15, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (16, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (26, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (27, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (28, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (32, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (35, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (37, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (39, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (43, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (45, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (49, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (50, 5, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (2, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (3, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (5, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (6, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (13, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (18, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (19, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (20, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (22, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (23, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (29, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (31, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 33 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (40, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (42, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (43, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (45, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (46, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (47, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (49, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (50, 6, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (1, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (2, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (4, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (5, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (8, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (9, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (10, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (13, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (18, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (19, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (20, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (21, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (24, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 33 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (25, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (30, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (31, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (32, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (34, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (39, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (40, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (42, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (43, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (46, 7, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (2, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (3, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (4, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (5, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (6, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (7, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (8, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (11, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (12, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (13, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (18, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (19, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (20, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (23, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (24, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (25, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (26, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (27, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (29, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (30, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (32, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (33, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (35, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (36, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (41, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (42, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (44, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (46, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (50, 8, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (1, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (2, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 34 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (3, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (6, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (7, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (8, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (9, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (13, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (14, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 17 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (19, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 34 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (20, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (21, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (22, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (23, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (24, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (25, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (26, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (28, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (29, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (30, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (32, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (35, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (36, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (37, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (40, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (43, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (46, 9, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (1, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (5, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (9, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (10, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (12, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (14, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 31 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (15, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (21, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (22, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (24, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (25, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (30, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (31, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (32, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (34, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (36, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 23 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (39, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 36 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (40, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 21 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (45, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (47, 10, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (5, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 6 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (6, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (9, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (10, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (11, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (14, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 39 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (15, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (16, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (19, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (23, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (26, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (27, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (29, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (36, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 40 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (40, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 24 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (41, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (43, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (46, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 38 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (47, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (50, 11, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 40 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (1, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (3, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (6, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 12 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (7, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (12, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 7 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (14, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (16, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 22 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (18, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (19, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (20, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (21, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (22, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 32 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (23, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (26, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (27, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (29, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (32, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (33, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (34, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (35, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (41, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 37 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (45, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (47, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (48, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 29 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (49, 12, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (4, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (7, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (9, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (10, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 25 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (11, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (15, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 9 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (16, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 14 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (17, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (23, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 18 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (24, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (27, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (28, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 19 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (29, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 11 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (30, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (31, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (35, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 26 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (36, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 27 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (38, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 13 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (42, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 35 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (43, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 16 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (49, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));
INSERT IGNORE INTO favourite (book_id, user_id, time_added) VALUES (50, 13, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY));

SELECT * FROM user;