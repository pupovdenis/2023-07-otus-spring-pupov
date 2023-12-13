package ru.pupov.homework08.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.pupov.homework08.model.Author;
import ru.pupov.homework08.model.Book;
import ru.pupov.homework08.model.Comment;
import ru.pupov.homework08.model.Genre;
import ru.pupov.homework08.repository.BookRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Book должен")
class BookRepositoryTest extends AbstractRepositoryTest {

    public static final String EXISTING_BOOK_ID = "1";
    public static final String NEW_BOOK_ID = "10";
    public static final String NEW_BOOK_NAME = "test book name";

    public static final String EXISTING_AUTHOR_ID = "1";
    public static final String NEW_AUTHOR_ID = "1111";
    public static final String NEW_AUTHOR_FIRSTNAME = "Новое имя автора";
    public static final String NEW_AUTHOR_LASTNAME = "Новая фамилия автора";

    public static final String EXISTING_GENRE_ID = "1";
    public static final String NEW_GENRE_ID = "111";
    public static final String NEW_GENRE_NAME = "Новый жанр";

    public static final String EXISTING_COMMENT_TEXT = "Отличное чтиво. Дольше Санта-Барбары";
    public static final String NEW_COMMENT_ID = "100";
    public static final String NEW_COMMENT_TEXT = "test comment text";

    public static final int EXISTING_BOOK_COUNT = 9;
    public static final int EXISTING_BOOK_COMMENT_LIST_SIZE = 1;
    public static final int EXPECTED_BOOK_COMMENT_LIST_SIZE = 2;
    public static final int BOOK_COMMENT_LIST_FIRST_INDEX = 0;
    public static final int BOOK_COMMENT_LIST_SECOND_INDEX = 1;

    public static final int EXISTING_AUTHOR_BOOKS_SIZE = 2;
    public static final int EXISTING_GENRE_BOOKS_SIZE = 5;

    @Autowired
    private BookRepository bookRepository;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("сохранять и считывать новую книгу")
    @Test
    void savingBook_addNewBookWithAuthorAndGenre_expectedData() {
        var author = new Author(NEW_AUTHOR_ID, NEW_AUTHOR_FIRSTNAME, NEW_AUTHOR_LASTNAME);
        var genre = new Genre(NEW_GENRE_ID, NEW_GENRE_NAME);
        var newBook = new Book(NEW_BOOK_ID, NEW_BOOK_NAME, author, genre,
                List.of(new Comment(NEW_COMMENT_ID, NEW_COMMENT_TEXT)));
        bookRepository.save(newBook);

        var expectedBook = bookRepository.findById(NEW_BOOK_ID).orElse(null);
        assertThat(expectedBook).isNotNull()
                .hasFieldOrPropertyWithValue("id", NEW_BOOK_ID)
                .hasFieldOrPropertyWithValue("name", NEW_BOOK_NAME);
        assertThat(expectedBook.getAuthor()).isNotNull()
                .hasFieldOrPropertyWithValue("id", NEW_AUTHOR_ID)
                .hasFieldOrPropertyWithValue("firstname", NEW_AUTHOR_FIRSTNAME)
                .hasFieldOrPropertyWithValue("lastname", NEW_AUTHOR_LASTNAME);
        assertThat(expectedBook.getGenre()).isNotNull()
                .hasFieldOrPropertyWithValue("id", NEW_GENRE_ID)
                .hasFieldOrPropertyWithValue("name", NEW_GENRE_NAME);
        assertThat(expectedBook.getComments()).isNotEmpty().hasSize(EXISTING_BOOK_COMMENT_LIST_SIZE);
        assertThat(expectedBook.getComments().get(BOOK_COMMENT_LIST_FIRST_INDEX))
                .hasFieldOrPropertyWithValue("id", NEW_COMMENT_ID)
                .hasFieldOrPropertyWithValue("text", NEW_COMMENT_TEXT);
    }

    @DisplayName("возвращать книгу по id")
    @Test
    void readingBook_readExistingBook_expectedData() {
        var book = bookRepository.findById(EXISTING_BOOK_ID);
        assertThat(book).isNotNull().get().hasFieldOrPropertyWithValue("id", EXISTING_BOOK_ID);
    }

    @DisplayName("возвращать все книги")
    @Test
    void readingBook_readAllExistingBook_expectedResultListSize() {
        assertThat(bookRepository.findAll()).hasSize(EXISTING_BOOK_COUNT);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("добавлять комментарий к существующей книге")
    @Test
    void updatingBook_addNewCommentToExistingBook_canReadBookWithNewComment() {
        var book = bookRepository.findById(EXISTING_BOOK_ID).orElse(null);
        assertThat(book).isNotNull();
        assertThat(book.getComments()).hasSize(EXISTING_BOOK_COMMENT_LIST_SIZE);

        bookRepository.addComment(EXISTING_BOOK_ID, NEW_COMMENT_TEXT);

        var expectedBook = bookRepository.findById(EXISTING_BOOK_ID).orElse(null);
        assertThat(expectedBook).isNotNull();
        assertThat(expectedBook.getComments()).hasSize(EXPECTED_BOOK_COMMENT_LIST_SIZE);
        assertThat(expectedBook.getComments().get(BOOK_COMMENT_LIST_SECOND_INDEX)).isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("text", NEW_COMMENT_TEXT);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("обновлять текст существующего комментария существующей книги")
    @Test
    void updatingBook_changeTextOfExistingCommentOfExistingBook_canReadBookWithNewCommentText() {
        var book = bookRepository.findById(EXISTING_BOOK_ID).orElse(null);
        assertThat(book).isNotNull();
        assertThat(book.getComments()).hasSize(EXISTING_BOOK_COMMENT_LIST_SIZE);
        var existingComment = book.getComments().get(BOOK_COMMENT_LIST_FIRST_INDEX);
        assertThat(existingComment.getText()).isEqualTo(EXISTING_COMMENT_TEXT);

        bookRepository.updateCommentById(EXISTING_BOOK_ID, existingComment.getId(), NEW_COMMENT_TEXT);

        var expectedBook = bookRepository.findById(EXISTING_BOOK_ID).orElse(null);
        assertThat(expectedBook).isNotNull();
        assertThat(book.getComments()).hasSize(EXISTING_BOOK_COMMENT_LIST_SIZE);
        assertThat(expectedBook.getComments().get(BOOK_COMMENT_LIST_FIRST_INDEX)).isNotNull()
                .hasFieldOrPropertyWithValue("id", existingComment.getId())
                .hasFieldOrPropertyWithValue("text", NEW_COMMENT_TEXT);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("удалять у книги комментарий по id")
    @Test
    void updatingBook_deleteExistingCommentOfExistingBook_notExistingCommentOfBook() {
        var book = bookRepository.findById(EXISTING_BOOK_ID).orElse(null);
        assertThat(book).isNotNull();
        assertThat(book.getComments()).hasSize(EXISTING_BOOK_COMMENT_LIST_SIZE);
        var existingComment = book.getComments().get(BOOK_COMMENT_LIST_FIRST_INDEX);

        bookRepository.deleteCommentByIds(EXISTING_BOOK_ID, List.of(existingComment.getId()));

        var expectedBook = bookRepository.findById(EXISTING_BOOK_ID).orElse(null);
        assertThat(expectedBook).isNotNull();
        assertThat(expectedBook.getComments()).isEmpty();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("удалять автора по id во всех книгах")
    @Test
    void updatingBook_deleteAuthorByIdInAllBooks_authorIsNullInBooks() {
        var books = bookRepository.findByAuthorId(EXISTING_AUTHOR_ID);
        assertThat(books).hasSize(EXISTING_AUTHOR_BOOKS_SIZE);

        bookRepository.deleteAuthor(EXISTING_AUTHOR_ID);

        var expectedBooks = bookRepository.findByAuthorId(EXISTING_AUTHOR_ID);
        assertThat(expectedBooks).isEmpty();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("удалять жанр по id во всех книгах")
    @Test
    void updatingBook_deleteGenreByIdInAllBooks_genreIsNullInBooks() {
        var books = bookRepository.findByGenreId(EXISTING_GENRE_ID);
        assertThat(books).hasSize(EXISTING_GENRE_BOOKS_SIZE);

        bookRepository.deleteGenre(EXISTING_GENRE_ID);

        var expectedBooks = bookRepository.findByGenreId(EXISTING_GENRE_ID);
        assertThat(expectedBooks).isEmpty();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("удалять книгу по id")
    @Test
    void deletingBook_deleteExistingBook_notExistingBook() {
        assertThat(bookRepository.existsById(EXISTING_BOOK_ID)).isTrue();
        bookRepository.deleteById(EXISTING_BOOK_ID);
        assertThat(bookRepository.existsById(EXISTING_BOOK_ID)).isFalse();
    }
}