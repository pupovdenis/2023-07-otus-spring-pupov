package ru.pupov.homework05.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.extractor.AuthorMapper;
import ru.pupov.homework05.extractor.AuthorsRsExtractor;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("класс AuthorDaoJdbc должен")
@JdbcTest
@Import({AuthorDaoJdbc.class, AuthorsRsExtractor.class, AuthorMapper.class})
class AuthorDaoJdbcTest {

    public static final long EXPECTED_AUTHOR_ID = 6L;
    public static final String EXPECTED_AUTHOR_FIRST_NAME = "Test";
    public static final String EXPECTED_AUTHOR_LAST_NAME = "Testov";
    public static final String EXPECTED_AUTHOR_ANOTHER_FIRST_NAME = "Dobro";
    public static final String EXPECTED_AUTHOR_ANOTHER_LAST_NAME = "Dobrov";

    public static final long EXISTING_AUTHOR_ID = 1L;
    public static final String EXISTING_AUTHOR_FIRST_NAME = "Лев";
    public static final String EXISTING_AUTHOR_LAST_NAME = "Толстой";
    public static final int EXPECTED_AUTHORS = 5;

    private static final long EXISTING_BOOK_1_ID = 1L;
    public static final String EXISTING_BOOK_1_NAME = "Война и мир";

    private static final long EXISTING_BOOK_2_ID = 2L;
    public static final String EXISTING_BOOK_2_NAME = "Анна Каренина";

    private static final long EXISTING_BOOK_3_ID = 3L;
    public static final String EXISTING_BOOK_3_NAME = "Преступление и наказание";

    private static final long EXISTING_BOOK_4_ID = 4L;
    public static final String EXISTING_BOOK_4_NAME = "Сказка о царе Салтане";

    @Autowired
    private AuthorsRsExtractor authorsRsExtractor;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    AuthorDaoJdbc authorDaoJdbc;

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        var books = List.of(
                Book.builder()
                        .id(EXISTING_BOOK_1_ID)
                        .name(EXISTING_BOOK_1_NAME)
                        .build(),
                Book.builder()
                        .id(EXISTING_BOOK_2_ID)
                        .name(EXISTING_BOOK_2_NAME)
                        .build()
        );
        var expectedAuthor = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXISTING_AUTHOR_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_LAST_NAME)
                .books(books)
                .build();
        var actualAuthor = authorDaoJdbc.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedGenresList() {
        var books = List.of(
                Book.builder()
                        .id(EXISTING_BOOK_1_ID)
                        .name(EXISTING_BOOK_1_NAME)
                        .build(),
                Book.builder()
                        .id(EXISTING_BOOK_2_ID)
                        .name(EXISTING_BOOK_2_NAME)
                        .build()
        );
        var expectedAuthor = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXISTING_AUTHOR_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_LAST_NAME)
                .books(books)
                .build();
        var actualPersonList = authorDaoJdbc.getAll();
        assertThat(actualPersonList)
                .contains(expectedAuthor)
                .hasSize(EXPECTED_AUTHORS);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        var author = Author.builder()
                .id(null)
                .firstName(EXPECTED_AUTHOR_FIRST_NAME)
                .lastName(EXPECTED_AUTHOR_LAST_NAME)
                .books(Collections.emptyList())
                .build();
        authorDaoJdbc.insert(author);
        var expectedAuthor = Author.builder()
                .id(EXPECTED_AUTHOR_ID)
                .firstName(EXPECTED_AUTHOR_FIRST_NAME)
                .lastName(EXPECTED_AUTHOR_LAST_NAME)
                .books(Collections.emptyList())
                .build();
        var actualAuthor = authorDaoJdbc.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("корректно обновлять автора в БД вместе с привязкой к книгам")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertAuthorWithBooks() {
        var actualAuthorBefore = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXISTING_AUTHOR_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_LAST_NAME)
                .books(List.of(
                        Book.builder()
                                .id(EXISTING_BOOK_1_ID)
                                .name(EXISTING_BOOK_1_NAME)
                                .build(),
                        Book.builder()
                                .id(EXISTING_BOOK_2_ID)
                                .name(EXISTING_BOOK_2_NAME)
                                .build()))
                .build();
        var expectedAuthorBefore = authorDaoJdbc.getById(EXISTING_AUTHOR_ID);
        assertThat(actualAuthorBefore).usingRecursiveComparison().isEqualTo(expectedAuthorBefore);

        var actualAuthorAfter = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXPECTED_AUTHOR_ANOTHER_FIRST_NAME)
                .lastName(EXPECTED_AUTHOR_ANOTHER_LAST_NAME)
                .books(List.of(
                        Book.builder()
                                .id(EXISTING_BOOK_3_ID)
                                .name(EXISTING_BOOK_3_NAME)
                                .build(),
                        Book.builder()
                                .id(EXISTING_BOOK_4_ID)
                                .name(EXISTING_BOOK_4_NAME)
                                .build()))
                .build();
        authorDaoJdbc.update(actualAuthorAfter, true);

        var expectedAuthorAfter = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXPECTED_AUTHOR_ANOTHER_FIRST_NAME)
                .lastName(EXPECTED_AUTHOR_ANOTHER_LAST_NAME)
                .books(List.of(
                        Book.builder()
                                .id(EXISTING_BOOK_3_ID)
                                .name(EXISTING_BOOK_3_NAME)
                                .build(),
                        Book.builder()
                                .id(EXISTING_BOOK_4_ID)
                                .name(EXISTING_BOOK_4_NAME)
                                .build()))
                .build();
        var actualAuthor = authorDaoJdbc.getById(EXISTING_AUTHOR_ID);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthorAfter);
    }

    @DisplayName("удалять определенного автора по его id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectlyDeleteActualAuthorById() {
        assertThatCode(() -> authorDaoJdbc.getById(EXISTING_AUTHOR_ID))
                .doesNotThrowAnyException();

        authorDaoJdbc.deleteById(EXISTING_AUTHOR_ID);

        assertThat(authorDaoJdbc.getById(EXISTING_AUTHOR_ID)).isNull();
    }
}