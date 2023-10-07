package ru.pupov.homework06.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.pupov.homework06.entity.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Genre должен")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {

    public static final Long FIRST_GENRE_ID = 1L;
    public static final String FIRST_GENRE_NAME = "Роман";
    public static final int FIRST_GENRE_BOOKS_COUNT = 5;
    public static final Long THIRD_GENRE_ID = 3L;
    public static final String NEW_GENRE_NAME = "Триллер";

    public static final int GENRES_COUNT = 6;

    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("сохранять новый жанр")
    @Test
    void shouldSaveNewGenre() {
        var actualGenre = Genre.builder()
                .id(null)
                .name(NEW_GENRE_NAME)
                .build();
        var savedGenre = genreRepository.save(actualGenre);

        em.flush();
        em.clear();

        var expectedGenre = genreRepository.findById(savedGenre.getId());
        assertThat(expectedGenre).isNotNull().get()
                .hasFieldOrPropertyWithValue("name", NEW_GENRE_NAME);
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void shouldFindAllGenres() {
        var genres = genreRepository.findAll();
        assertThat(genres).hasSize(GENRES_COUNT);
    }

    @DisplayName("возвращать жанр по его id")
    @Test
    void shouldFindGenreById() {
        var genre = genreRepository.findById(FIRST_GENRE_ID)
                .orElse(null);
        assertThat(genre).isNotNull()
                .hasFieldOrPropertyWithValue("name", FIRST_GENRE_NAME);
        assertThat(genre.getBooks()).hasSize(FIRST_GENRE_BOOKS_COUNT);
    }

    @DisplayName("обновлять существующий жанр")
    @Test
    void shouldUpdateExistingGenre() {
        var actualGenre = genreRepository.findById(THIRD_GENRE_ID).orElse(null);
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre.getName()).isNotEqualTo(NEW_GENRE_NAME);

        actualGenre.setName(NEW_GENRE_NAME);
        genreRepository.save(actualGenre);

        em.flush();
        em.clear();

        var expectedGenre = genreRepository.findById(THIRD_GENRE_ID).orElse(null);
        assertThat(expectedGenre).isNotNull()
                .hasFieldOrPropertyWithValue("name", NEW_GENRE_NAME);
    }

    @DisplayName("удалять жанр по его id")
    @Test
    void shouldDeleteGenreById() {
        var genre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(genre).isNotNull();

        genre.getBooks().forEach(book -> em.remove(book));
        genreRepository.deleteById(FIRST_GENRE_ID);

        em.flush();
        em.clear();

        genre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(genre).isNull();
    }
}