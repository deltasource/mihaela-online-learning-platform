ALTER TABLE lessons
DROP
FOREIGN KEY FK17ucc7gjfjddsyi0gvstkqeat;

ALTER TABLE courses
DROP
FOREIGN KEY FK1kswo6qqebbdy2kq0kx6udof7;

ALTER TABLE options
DROP
FOREIGN KEY FK5bmv46so2y5igt9o9n9w4fh6y;

ALTER TABLE analytics
DROP
FOREIGN KEY FK5kb1hoxkm89l8rebntn3p2c5x;

ALTER TABLE enrollment
DROP
FOREIGN KEY FK7ofybdo2o0ngc4de3uvx4dxqv;

ALTER TABLE sections
DROP
FOREIGN KEY FK7ty9cevpq04d90ohtso1q8312;

ALTER TABLE student_progress
DROP
FOREIGN KEY FKa8c004k1us8ptjo5vnrmh0ixc;

ALTER TABLE answer_options
DROP
FOREIGN KEY FKfapodm8kfiu9a9a4o2r43rcgp;

ALTER TABLE enrollment
DROP
FOREIGN KEY FKgpuyid9pbfpxghv9vyhb0ictd;

ALTER TABLE lessons
DROP
FOREIGN KEY FKgt4502q9pklwr02uqh3qnrppi;

ALTER TABLE enrollments
DROP
FOREIGN KEY FKho8mcicp4196ebpltdn9wl6co;

ALTER TABLE course_students
DROP
FOREIGN KEY FKj5fbpmgy0y0es0gvk0311jor3;

ALTER TABLE quizzes
DROP
FOREIGN KEY FKlkd3uq7kwr9b4npktlx5mj8sg;

ALTER TABLE course_students
DROP
FOREIGN KEY FKm3befe0jxxln54ulu74nn9gr0;

ALTER TABLE questions
DROP
FOREIGN KEY FKn3gvco4b0kewxc0bywf1igfms;

ALTER TABLE quizzes
DROP
FOREIGN KEY FKpxdnhxeppxx606nhyjtjyharp;

ALTER TABLE course_student_ids
DROP
FOREIGN KEY FKqeuceygl4hqxv9xo7s59ta09i;

ALTER TABLE lessons
DROP
FOREIGN KEY FKqjkxpuk6fwsynn3qiqt0haybp;

ALTER TABLE videos
DROP
FOREIGN KEY FKrccvc1a5qx2dqsyienwhuk66j;

ALTER TABLE student_courses
DROP
FOREIGN KEY FKsfpq78oyrqua1h0obpl7ulc18;

ALTER TABLE student_courses
DROP
FOREIGN KEY FKwj1l0mta35u161acdl2tupoo;

CREATE TABLE lesson
(
    id            BINARY(16)   NOT NULL,
    course_id     BINARY(16)   NULL,
    title         VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    content       VARCHAR(255) NULL,
    video_url     VARCHAR(255) NULL,
    duration      INT    NOT NULL,
    `order`       INT    NOT NULL,
    is_published  BIT(1) NOT NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    CONSTRAINT pk_lesson PRIMARY KEY (id)
);

CREATE TABLE `option`
(
    id          BINARY(16)   NOT NULL,
    question_id BINARY(16)   NULL,
    text        VARCHAR(255) NULL,
    is_correct  BIT(1) NOT NULL,
    CONSTRAINT pk_option PRIMARY KEY (id)
);

CREATE TABLE question
(
    id            BINARY(16)   NOT NULL,
    quiz_id       BINARY(16)   NULL,
    question      VARCHAR(255) NULL,
    question_type VARCHAR(255) NULL,
    points        INT NOT NULL,
    `order`       INT NOT NULL,
    CONSTRAINT pk_question PRIMARY KEY (id)
);

CREATE TABLE quiz
(
    id            BINARY(16)   NOT NULL,
    course_id     BINARY(16)   NULL,
    title         VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    passing_score INT    NOT NULL,
    time_limit    INT    NOT NULL,
    `order`       INT    NOT NULL,
    is_published  BIT(1) NOT NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    CONSTRAINT pk_quiz PRIMARY KEY (id)
);

ALTER TABLE instructors
    ADD CONSTRAINT uc_instructors_username UNIQUE (username);

ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);

ALTER TABLE `option`
    ADD CONSTRAINT FK_OPTION_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE question
    ADD CONSTRAINT FK_QUESTION_ON_QUIZ FOREIGN KEY (quiz_id) REFERENCES quiz (id);

ALTER TABLE quiz
    ADD CONSTRAINT FK_QUIZ_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);

DROP TABLE answer_options;

DROP TABLE course_student_ids;

DROP TABLE course_students;

DROP TABLE courses;

DROP TABLE enrollment;

DROP TABLE lessons;

DROP TABLE options;

DROP TABLE persons;

DROP TABLE questions;

DROP TABLE quizzes;

DROP TABLE sections;

DROP TABLE student_courses;

DROP TABLE user;

ALTER TABLE instructors
DROP
COLUMN password;

ALTER TABLE students
DROP
COLUMN password;

ALTER TABLE students
DROP
COLUMN username;

ALTER TABLE analytics
DROP
COLUMN event_type;

ALTER TABLE analytics
    ADD event_type VARCHAR(255) NOT NULL;

ALTER TABLE notifications
DROP
COLUMN priority;

ALTER TABLE notifications
DROP
COLUMN related_entity_type;

ALTER TABLE notifications
DROP
COLUMN type;

ALTER TABLE notifications
    ADD priority VARCHAR(255) NOT NULL;

ALTER TABLE notifications
    ADD related_entity_type VARCHAR(255) NULL;

ALTER TABLE users
DROP
COLUMN `role`;

ALTER TABLE users
    ADD `role` VARCHAR(255) NOT NULL;

ALTER TABLE enrollments
DROP
COLUMN status;

ALTER TABLE enrollments
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE notifications
    ADD type VARCHAR(255) NOT NULL;

ALTER TABLE instructors
    MODIFY username VARCHAR (255);
