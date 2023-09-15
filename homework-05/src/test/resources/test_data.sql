INSERT INTO author (id, first_name, last_name)
VALUES (nextval('author_id_seq'), 'Тест', 'Тестов');

INSERT INTO genre (id, name)
VALUES (nextval('genre_id_seq'), 'Тест');

INSERT INTO book (id, name, author_id, genre_id)
VALUES (nextval('book_id_seq'), 'Тестирование', 5, 6);