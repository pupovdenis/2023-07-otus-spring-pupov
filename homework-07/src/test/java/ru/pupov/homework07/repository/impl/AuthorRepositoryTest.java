package ru.pupov.homework07.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.pupov.homework07.entity.Author;
import ru.pupov.homework07.repository.AuthorRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Author должен")
@DataJpaTest
class AuthorRepositoryTest {

    public static final Long FIRST_AUTHOR_ID = 1L;
    public static final String FIRST_AUTHOR_FIRSTNAME = "Лев";
    public static final String FIRST_AUTHOR_LASTNAME = "Толстой";
    public static final int FIRST_AUTHOR_BOOKS_COUNT = 2;
    public static final String NEW_AUTHOR_FIRSTNAME = "Иван";
    public static final String NEW_AUTHOR_LASTNAME = "тургенев";

    public static final int AUTHORS_COUNT = 5;


    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository authorRepository;

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
}