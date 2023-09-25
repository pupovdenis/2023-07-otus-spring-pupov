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
import ru.pupov.homework05.extractor.GenreMapper;
import ru.pupov.homework05.extractor.GenresRsExtractor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("класс GenreDaoJdbc должен")
@JdbcTest
@Import({GenreDaoJdbc.class, GenresRsExtractor.class, GenreMapper.class})
class GenreDaoJdbcTest {

    private static final long EXISTING_GENRE_ID = 6L;
    private static final String EXISTING_GENRE_NAME = "Тест";
    public static final String EXPECTED_GENRE_ANOTHER_NAME = "Ода";

    public static final int EXPECTED_GENRES = 6;

    public static final Long EXPECTED_GENRE_ID = 7L;
    public static final String EXPECTED_GENRE_NAME = "Проза";

    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    GenresRsExtractor genresRsExtractor;
    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        var expectedGenre = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME)
                .build();
        var actualGenre = genreDaoJdbc.getById(EXISTING_GENRE_ID);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать ожидаемый список жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        var expectedGenre = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME)
                .build();
        var actualPersonList = genreDaoJdbc.getAll();
        assertThat(actualPersonList)
                .contains(expectedGenre)
                .hasSize(EXPECTED_GENRES);
    }

    @DisplayName("добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        var genre = Genre.builder()
                .id(null)
                .name(EXPECTED_GENRE_NAME)
                .build();
        genreDaoJdbc.insert(genre);
        var expectedGenre = Genre.builder()
                .id(EXPECTED_GENRE_ID)
                .name(EXPECTED_GENRE_NAME)
                .build();
        var actualGenre = genreDaoJdbc.getById(EXPECTED_GENRE_ID);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("корректно обновлять жанр в БД вместе с привязкой к книгам")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertGenreWithBooks() {
        var actualGenreBefore = genreDaoJdbc.getById(EXISTING_GENRE_ID);
        var expectedGenreBefore = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME)
                .build();
        assertThat(actualGenreBefore).usingRecursiveComparison().isEqualTo(expectedGenreBefore);

        var updatableGenre = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXPECTED_GENRE_ANOTHER_NAME)
                .build();
        genreDaoJdbc.update(updatableGenre);

        var actualGenreAfter = genreDaoJdbc.getById(EXISTING_GENRE_ID);
        var expectedGenreAfter = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXPECTED_GENRE_ANOTHER_NAME)
                .build();
        assertThat(actualGenreAfter).usingRecursiveComparison().isEqualTo(expectedGenreAfter);
    }

    @DisplayName("удалять определенный жанр по его id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteGenreById() {
        assertThatCode(() -> genreDaoJdbc.getById(EXISTING_GENRE_ID))
                .doesNotThrowAnyException();

        genreDaoJdbc.deleteById(EXISTING_GENRE_ID);

        assertThat(genreDaoJdbc.getById(EXISTING_GENRE_ID)).isNull();
    }
}