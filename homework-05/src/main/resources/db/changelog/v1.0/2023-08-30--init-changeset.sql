--liquibase formatted sql

--changeset pupov-dm:20230830 failOnError:true
--comment:  Initial changeset.
--preconditions onFail:HALT onError:HALT

CREATE TABLE author
(
    id         BIGINT
        constraint author_pkey PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

CREATE TABLE genre
(
    id   BIGINT
        constraint genre_pkey PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE book
(
    id        BIGINT
        constraint book_pkey PRIMARY KEY,
    name      VARCHAR(255),
    author_id BIGINT
        CONSTRAINT book_author_id_fk REFERENCES author (id),
    genre_id  BIGINT
        CONSTRAINT book_genre_id_fk REFERENCES genre (id)
);

