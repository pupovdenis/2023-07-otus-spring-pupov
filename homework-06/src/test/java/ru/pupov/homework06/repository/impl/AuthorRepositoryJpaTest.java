package ru.pupov.homework06.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.pupov.homework06.entity.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Author должен")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    public static final Long FIRST_AUTHOR_ID = 1L;
    public static final String FIRST_AUTHOR_FIRSTNAME = "Лев";
    public static final String FIRST_AUTHOR_LASTNAME = "Толстой";
    public static final int FIRST_AUTHOR_BOOKS_COUNT = 2;
    public static final Long THIRD_AUTHOR_ID = 3L;
    public static final String NEW_AUTHOR_FIRSTNAME = "Иван";
    public static final String NEW_AUTHOR_LASTNAME = "тургенев";

    public static final int AUTHORS_COUNT = 5;


    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @DisplayName("сохранять нового автора")
    @Test
    void shouldSaveNewAuthor() {
        var actualAuthor = Author.builder()
                .id(null)
                .firstname(NEW_AUTHOR_FIRSTNAME)
                .lastname(NEW_AUTHOR_LASTNAME)
                .build();
        var savedAuthor = authorRepository.save(actualAuthor);

        em.flush();
        em.clear();

        var expectedAuthor = authorRepository.findById(savedAuthor.getId());
        assertThat(expectedAuthor).isNotNull().get()
                .hasFieldOrPropertyWithValue("firstname", NEW_AUTHOR_FIRSTNAME)
                .hasFieldOrPropertyWithValue("lastname", NEW_AUTHOR_LASTNAME);
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldFindAllAuthors() {
        var authors = authorRepository.findAll();
        assertThat(authors).hasSize(AUTHORS_COUNT);
    }

    @DisplayName("возвращать автора по его id")
    @Test
    void shouldFindAuthorById() {
        var author = authorRepository.findById(FIRST_AUTHOR_ID)
                .orElse(null);
        assertThat(author).isNotNull()
                .hasFieldOrPropertyWithValue("firstname", FIRST_AUTHOR_FIRSTNAME)
                .hasFieldOrPropertyWithValue("lastname", FIRST_AUTHOR_LASTNAME);
        assertThat(author.getBooks()).hasSize(FIRST_AUTHOR_BOOKS_COUNT);
    }

    @DisplayName("обновлять существующего автора")
    @Test
    void shouldUpdateExistingAuthor() {
        var actualAuthor = authorRepository.findById(THIRD_AUTHOR_ID).orElse(null);
        assertThat(actualAuthor).isNotNull();
        assertThat(actualAuthor.getFirstname()).isNotEqualTo(NEW_AUTHOR_FIRSTNAME);
        assertThat(actualAuthor.getLastname()).isNotEqualTo(NEW_AUTHOR_LASTNAME);

        actualAuthor.setFirstname(NEW_AUTHOR_FIRSTNAME);
        actualAuthor.setLastname(NEW_AUTHOR_LASTNAME);
        authorRepository.save(actualAuthor);

        em.flush();
        em.clear();

        var expectedAuthor = authorRepository.findById(THIRD_AUTHOR_ID).orElse(null);
        assertThat(expectedAuthor).isNotNull()
                .hasFieldOrPropertyWithValue("firstname", NEW_AUTHOR_FIRSTNAME)
                .hasFieldOrPropertyWithValue("lastname", NEW_AUTHOR_LASTNAME);
    }

    @DisplayName("удалять автора по его id")
    @Test
    void shouldDeleteAuthorById() {
        var author = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(author).isNotNull();

        author.getBooks().forEach(book -> em.remove(book));
        authorRepository.deleteById(FIRST_AUTHOR_ID);

        em.flush();
        em.clear();

        author = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(author).isNull();
    }
}