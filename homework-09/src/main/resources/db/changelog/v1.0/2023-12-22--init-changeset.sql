--liquibase formatted sql

--changeset pupov-dm:20232212 failOnError:true
--comment:  Initial changeset.
--preconditions onFail:HALT onError:HALT

CREATE SEQUENCE author_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE genre_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE book_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE comment_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE author
(
    id         BIGINT
        DEFAULT author_id_seq.nextval
        CONSTRAINT author_pkey PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

CREATE TABLE genre
(
    id   BIGINT
        DEFAULT genre_id_seq.nextval
        CONSTRAINT genre_pkey PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE book
(
    id        BIGINT
        DEFAULT book_id_seq.nextval
        CONSTRAINT book_pkey PRIMARY KEY,
    name      VARCHAR(255),
    author_id BIGINT
        CONSTRAINT book_author_id_fk REFERENCES author (id),
    genre_id  BIGINT
        CONSTRAINT book_genre_id_fk REFERENCES genre (id)
);

CREATE TABLE comment
(
    id      BIGINT
        DEFAULT comment_id_seq.nextval
        CONSTRAINT comment_pkey PRIMARY KEY,
    text    VARCHAR(1024),
    book_id BIGINT
        CONSTRAINT comment_book_id_fk REFERENCES book (id)
);

