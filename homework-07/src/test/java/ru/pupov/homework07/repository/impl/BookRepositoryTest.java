package ru.pupov.homework07.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.pupov.homework07.entity.Author;
import ru.pupov.homework07.entity.Book;
import ru.pupov.homework07.entity.Genre;
import ru.pupov.homework07.repository.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий Book должен")
@DataJpaTest
class BookRepositoryTest {

    public static final int BOOKS_COUNT = 10;
    public static final Long FIRST_BOOK_ID = 1L;
    private static final String FIRST_BOOK_NAME = "Война и мир";
    public static final int FIRST_BOOK_COMMENTS_COUNT = 1;

    public static final Long FIRST_AUTHOR_ID = 1L;
    public static final Long FIRST_GENRE_ID = 1L;
    public static final int FIRST_AUTHOR_BOOKS_COUNT = 2;
    public static final int FIRST_GENRE_BOOKS_COUNT = 5;

    public static final String NEW_BOOK_NAME = "Кулинарные рецепты";
    private static final Long EXISTING_AUTHOR_ID = 1L;
    private static final Long EXISTING_GENRE_ID = 1L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = em.find(Author.class, EXISTING_AUTHOR_ID);
        var genre = em.find(Genre.class, EXISTING_GENRE_ID);
        var actualBook = Book.builder()
                .id(null)
                .name(NEW_BOOK_NAME)
                .author(author)
                .genre(genre)
                .build();
        var savedBook = bookRepository.save(actualBook);

        em.flush();
        em.clear();

        var expectedBook = bookRepository.findById(savedBook.getId()).orElse(null);
        assertThat(expectedBook).isNotNull()
                .hasFieldOrPropertyWithValue("name", NEW_BOOK_NAME);
        assertThat(expectedBook.getAuthor()).isNotNull();
        assertThat(expectedBook.getGenre()).isNotNull();
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldFindAllBooks() {
        var books = bookRepository.findAll();
        assertThat(books).hasSize(BOOKS_COUNT);
    }

    @DisplayName("возвращать книгу по ее id")
    @Test
    void shouldFindBookById() {
        var book = bookRepository.findById(FIRST_BOOK_ID)
                .orElse(null);
        assertThat(book).isNotNull();
        assertEquals(FIRST_BOOK_NAME, book.getName());
        assertThat(book.getAuthor()).isNotNull();
        assertThat(book.getGenre()).isNotNull();
        assertThat(book.getComments()).hasSize(FIRST_BOOK_COMMENTS_COUNT);
    }

    @DisplayName("возвращать список всех книг по id автора")
    @Test
    void shouldFindAllBooksByAuthorId() {
        var books = bookRepository.findByAuthorId(FIRST_AUTHOR_ID);
        assertThat(books).isNotNull().hasSize(FIRST_AUTHOR_BOOKS_COUNT);
    }

    @DisplayName("возвращать список всех книг по id жанра")
    @Test
    void shouldFindAllBooksByGenreId() {
        var books = bookRepository.findByGenreId(FIRST_GENRE_ID);
        assertThat(books).hasSize(FIRST_GENRE_BOOKS_COUNT);
    }
}