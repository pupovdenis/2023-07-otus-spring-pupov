--liquibase formatted sql

--changeset pupov-dm:20230918 failOnError:true
--comment:  Base data changeset.
--preconditions onFail:HALT onError:HALT

INSERT INTO author (id, first_name, last_name)
VALUES (nextval('author_id_seq'), 'Лев', 'Толстой'),
       (nextval('author_id_seq'), 'Фёдор', 'Достоевский'),
       (nextval('author_id_seq'), 'Александр', 'Пушкин'),
       (nextval('author_id_seq'), 'Николай', 'Гоголь');

INSERT INTO genre (id, name)
VALUES (nextval('genre_id_seq'), 'Роман'),
       (nextval('genre_id_seq'), 'Сказка'),
       (nextval('genre_id_seq'), 'Поэма'),
       (nextval('genre_id_seq'), 'Комедия'),
       (nextval('genre_id_seq'), 'Повесть');

INSERT INTO book (id, name, author_id, genre_id)
VALUES (nextval('book_id_seq'), 'Война и мир', 1, 1),
       (nextval('book_id_seq'), 'Анна Каренина', 1, 1),
       (nextval('book_id_seq'), 'Преступление и наказание', 2, 1),
       (nextval('book_id_seq'), 'Сказка о царе Салтане', 3, 2),
       (nextval('book_id_seq'), 'Евгений Онегин', 3, 1),
       (nextval('book_id_seq'), 'Капитанская дочка', 3, 1),
       (nextval('book_id_seq'), 'Кавказский пленник', 3, 3),
       (nextval('book_id_seq'), 'Ревизор', 4, 4),
       (nextval('book_id_seq'), 'Записки сумасшедшего', 4, 5);
