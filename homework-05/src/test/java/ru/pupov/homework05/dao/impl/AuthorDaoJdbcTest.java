package ru.pupov.homework05.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.domain.Genre;
import ru.pupov.homework05.extractor.AuthorMapper;
import ru.pupov.homework05.extractor.AuthorsRsExtractor;
import ru.pupov.homework05.extractor.BookMapper;
import ru.pupov.homework05.extractor.BooksRsExtractor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("класс AuthorDaoJdbc должен")
@JdbcTest
@Import({BookDaoJdbc.class, BookMapper.class, BooksRsExtractor.class,
        AuthorDaoJdbc.class, AuthorsRsExtractor.class, AuthorMapper.class})
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

    public static final long EXISTING_GENRE_1_ID = 1L;
    public static final String EXISTING_GENRE_1_NAME = "Роман";

    public static final long EXISTING_GENRE_2_ID = 2L;
    public static final String EXISTING_GENRE_2_NAME = "Сказка";

    private static final long EXISTING_BOOK_1_ID = 1L;
    public static final String EXISTING_BOOK_1_NAME = "Война и мир";

    private static final long EXISTING_BOOK_2_ID = 2L;
    public static final String EXISTING_BOOK_2_NAME = "Анна Каренина";

    private static final long EXISTING_BOOK_3_ID = 3L;
    public static final String EXISTING_BOOK_3_NAME = "Преступление и наказание";

    private static final long EXISTING_BOOK_4_ID = 4L;
    public static final String EXISTING_BOOK_4_NAME = "Сказка о царе Салтане";
    public static final String NEW_BOOK_IDS_STRING = "3,4";

    @Autowired
    private BooksRsExtractor bookRsExtractor;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookDaoJdbc bookDaoJdbc;
    @Autowired
    private AuthorsRsExtractor authorsRsExtractor;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private AuthorDaoJdbc authorDaoJdbc;

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        var expectedAuthor = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXISTING_AUTHOR_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_LAST_NAME)
                .build();
        var actualAuthor = authorDaoJdbc.getById(EXISTING_AUTHOR_ID);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorsList() {
        var expectedAuthor = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXISTING_AUTHOR_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_LAST_NAME)
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
                .build();
        authorDaoJdbc.insert(author);
        var expectedAuthor = Author.builder()
                .id(EXPECTED_AUTHOR_ID)
                .firstName(EXPECTED_AUTHOR_FIRST_NAME)
                .lastName(EXPECTED_AUTHOR_LAST_NAME)
                .build();
        var actualAuthor = authorDaoJdbc.getById(EXPECTED_AUTHOR_ID);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("корректно обновлять автора в БД вместе с привязкой к книгам")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertAuthorWithBooks() {
        var actualAuthorBefore = authorDaoJdbc.getById(EXISTING_AUTHOR_ID);
        var expectedAuthorBefore = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXISTING_AUTHOR_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_LAST_NAME)
                .build();
        assertThat(actualAuthorBefore).usingRecursiveComparison().isEqualTo(expectedAuthorBefore);

        var actualBookListBefore = bookDaoJdbc.getAllByAuthorId(EXISTING_AUTHOR_ID);
        var authorBefore = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXISTING_AUTHOR_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_LAST_NAME)
                .build();
        var genreBefore = Genre.builder()
                .id(EXISTING_GENRE_1_ID)
                .name(EXISTING_GENRE_1_NAME).build();
        var expectedBookListBefore = List.of(
                Book.builder()
                        .id(EXISTING_BOOK_1_ID)
                        .name(EXISTING_BOOK_1_NAME)
                        .author(authorBefore)
                        .genre(genreBefore)
                        .build(),
                Book.builder()
                        .id(EXISTING_BOOK_2_ID)
                        .name(EXISTING_BOOK_2_NAME)
                        .author(authorBefore)
                        .genre(genreBefore)
                        .build());
        assertThat(actualBookListBefore).usingRecursiveComparison().isEqualTo(expectedBookListBefore);

        var updatableAuthor = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXPECTED_AUTHOR_ANOTHER_FIRST_NAME)
                .lastName(EXPECTED_AUTHOR_ANOTHER_LAST_NAME)
                .build();
        authorDaoJdbc.update(updatableAuthor, NEW_BOOK_IDS_STRING);

        var actualAuthorAfter = authorDaoJdbc.getById(EXISTING_AUTHOR_ID);
        var expectedAuthorAfter = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXPECTED_AUTHOR_ANOTHER_FIRST_NAME)
                .lastName(EXPECTED_AUTHOR_ANOTHER_LAST_NAME)
                .build();
        assertThat(actualAuthorAfter).usingRecursiveComparison().isEqualTo(expectedAuthorAfter);

        var actualBookListAfter = bookDaoJdbc.getAllByAuthorId(EXISTING_AUTHOR_ID);
        var authorAfter = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXPECTED_AUTHOR_ANOTHER_FIRST_NAME)
                .lastName(EXPECTED_AUTHOR_ANOTHER_LAST_NAME)
                .build();
        var genre1After = Genre.builder()
                .id(EXISTING_GENRE_1_ID)
                .name(EXISTING_GENRE_1_NAME).build();
        var genre2After = Genre.builder()
                .id(EXISTING_GENRE_2_ID)
                .name(EXISTING_GENRE_2_NAME).build();
        var expectedBookListAfter = List.of(
                Book.builder()
                        .id(EXISTING_BOOK_3_ID)
                        .name(EXISTING_BOOK_3_NAME)
                        .author(authorAfter)
                        .genre(genre1After)
                        .build(),
                Book.builder()
                        .id(EXISTING_BOOK_4_ID)
                        .name(EXISTING_BOOK_4_NAME)
                        .author(authorAfter)
                        .genre(genre2After)
                        .build());
        assertThat(actualBookListAfter).usingRecursiveComparison().isEqualTo(expectedBookListAfter);
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