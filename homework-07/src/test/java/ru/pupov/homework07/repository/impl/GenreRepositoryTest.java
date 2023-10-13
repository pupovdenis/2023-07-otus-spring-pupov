package ru.pupov.homework07.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.pupov.homework07.entity.Genre;
import ru.pupov.homework07.repository.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий Genre должен")
@DataJpaTest
class GenreRepositoryTest {

    public static final Long FIRST_GENRE_ID = 1L;
    public static final String FIRST_GENRE_NAME = "Роман";
    public static final int FIRST_GENRE_BOOKS_COUNT = 5;
    public static final String NEW_GENRE_NAME = "Триллер";

    public static final int GENRES_COUNT = 6;

    @Autowired
    private GenreRepository genreRepository;

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
}