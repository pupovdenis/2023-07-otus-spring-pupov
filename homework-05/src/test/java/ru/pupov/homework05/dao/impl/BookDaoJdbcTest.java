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
import ru.pupov.homework05.extractor.BookMapper;
import ru.pupov.homework05.extractor.BooksRsExtractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("класс BookDaoJdbcTest должен")
@JdbcTest
@Import({BookDaoJdbc.class, BookMapper.class, BooksRsExtractor.class})
class BookDaoJdbcTest {

    private static final int EXPECTED_BOOKS_LIST_SIZE = 10;
    private static final long EXISTING_BOOK_1_ID = 1L;
    private static final long EXISTING_BOOK_8_ID = 8L;
    public static final String EXISTING_BOOK_NAME = "Война и мир";
    public static final long EXISTING_AUTHOR_ID = 1L;
    public static final String EXISTING_AUTHOR_FIRST_NAME = "Лев";
    public static final String EXISTING_AUTHOR_LAST_NAME = "Толстой";
    public static final long EXISTING_GENRE_ID = 1L;
    public static final String EXISTING_GENRE_NAME = "Роман";
    public static final String NEW_BOOK_NAME = "Война и мир";
    public static final long EXPECTED_NEW_BOOK_ID = 11L;
    public static final String ID_FIELD_NAME = "id";

    @Autowired
    private BooksRsExtractor booksRsExtractor;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @DisplayName("корректно создавать и возвращать ожидаемую книгу по её id")
    @Test
    void shouldCorrectlyCreateAndReturnExpectedBookById() {
        var newBook = Book.builder()
                .id(null)
                .name(NEW_BOOK_NAME)
                .author(Author.builder()
                        .id(EXISTING_AUTHOR_ID)
                        .firstName(EXISTING_AUTHOR_FIRST_NAME)
                        .lastName(EXISTING_AUTHOR_LAST_NAME)
                        .build())
                .genre(Genre.builder()
                        .id(EXISTING_GENRE_ID)
                        .name(EXISTING_GENRE_NAME)
                        .build())
                .build();
        bookDaoJdbc.insert(newBook);

        newBook.setId(EXPECTED_NEW_BOOK_ID);
        var actualBook = bookDaoJdbc.getById(EXPECTED_NEW_BOOK_ID);

        assertThat(actualBook).usingRecursiveComparison().isEqualTo(newBook);
    }

    @DisplayName("корректно обновлять книгу по её id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectlyUpdateBookById() {
        var bookBefore = bookDaoJdbc.getById(EXISTING_BOOK_8_ID);

        var updatableBook = Book.builder()
                .id(EXISTING_BOOK_8_ID)
                .name(BookDaoJdbcTest.NEW_BOOK_NAME)
                .author(Author.builder()
                        .id(EXISTING_AUTHOR_ID)
                        .firstName(EXISTING_AUTHOR_FIRST_NAME)
                        .lastName(EXISTING_AUTHOR_LAST_NAME)
                        .build())
                .genre(Genre.builder()
                        .id(EXISTING_GENRE_ID)
                        .name(EXISTING_GENRE_NAME)
                        .build())
                .build();

        bookDaoJdbc.update(updatableBook);
        var bookAfter = bookDaoJdbc.getById(EXISTING_BOOK_8_ID);

        assertThat(bookAfter).usingRecursiveComparison().isEqualTo(updatableBook);
        assertThat(bookAfter).usingRecursiveComparison().ignoringFields(ID_FIELD_NAME).isNotEqualTo(bookBefore);
    }

    @DisplayName("возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBooksList() {
        var expectedBook = Book.builder()
                .id(EXISTING_BOOK_1_ID)
                .name(BookDaoJdbcTest.EXISTING_BOOK_NAME)
                .author(Author.builder()
                        .id(EXISTING_AUTHOR_ID)
                        .firstName(EXISTING_AUTHOR_FIRST_NAME)
                        .lastName(EXISTING_AUTHOR_LAST_NAME)
                        .build())
                .genre(Genre.builder()
                        .id(EXISTING_GENRE_ID)
                        .name(EXISTING_GENRE_NAME)
                        .build())
                .build();
        var books = bookDaoJdbc.getAll();
        assertThat(books)
                .contains(expectedBook)
                .hasSize(EXPECTED_BOOKS_LIST_SIZE);
    }

    @DisplayName("удалять определенную книгу по её id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectlyDeleteBookById() {
        assertThatCode(() -> bookDaoJdbc.getById(EXISTING_BOOK_1_ID))
                .doesNotThrowAnyException();

        bookDaoJdbc.deleteById(EXISTING_BOOK_1_ID);

        assertThat(bookDaoJdbc.getById(EXISTING_BOOK_1_ID)).isNull();
    }
}