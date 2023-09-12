package ru.pupov.homework05.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.domain.Genre;
import ru.pupov.homework05.extractor.GenreMapper;
import ru.pupov.homework05.extractor.GenresRsExtractor;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("класс GenreDaoJdbc должен")
@JdbcTest
@Import({GenreDaoJdbc.class, GenresRsExtractor.class, GenreMapper.class})
class GenreDaoJdbcTest {

    private static final long EXISTING_GENRE_ID = 6L;
    private static final String EXISTING_GENRE_NAME = "Тест";

    private static final long EXISTING_BOOK_10_ID = 10L;
    public static final String EXISTING_BOOK_10_NAME = "Тестирование";

    private static final long EXISTING_BOOK_1_ID = 1L;
    public static final String EXISTING_BOOK_1_NAME = "Война и мир";

    private static final long EXISTING_BOOK_2_ID = 2L;
    public static final String EXISTING_BOOK_2_NAME = "Анна Каренина";

    public static final int EXPECTED_GENRES = 6;

    public static final Long EXPECTED_GENRE_ID = 7L;
    public static final String EXPECTED_GENRE_NAME = "Проза";
    public static final String EXPECTED_GENRE_ANOTHER_NAME = "Ода";


    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    GenresRsExtractor genresRsExtractor;
    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        var books = List.of(
                Book.builder()
                        .id(EXISTING_BOOK_10_ID)
                        .name(EXISTING_BOOK_10_NAME)
                        .build()
        );
        var expectedGenre = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME)
                .books(books)
                .build();
        var actualGenre = genreDaoJdbc.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать ожидаемый список жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        var books = List.of(
                Book.builder()
                        .id(EXISTING_BOOK_10_ID)
                        .name(EXISTING_BOOK_10_NAME)
                        .build()
        );
        var expectedGenre = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME)
                .books(books)
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
                .name(EXPECTED_GENRE_NAME)
                .books(Collections.emptyList())
                .build();
        genreDaoJdbc.insert(genre);
        var expectedGenre = Genre.builder()
                .id(EXPECTED_GENRE_ID)
                .name(EXPECTED_GENRE_NAME)
                .books(Collections.emptyList())
                .build();
        var actualGenre = genreDaoJdbc.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("корректно обновлять жанр в БД вместе с привязкой к книгам")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertAuthorWithBooks() {
        var actualGenreBefore = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME)
                .books(List.of(
                        Book.builder()
                                .id(EXISTING_BOOK_10_ID)
                                .name(EXISTING_BOOK_10_NAME)
                                .build()))
                .build();
        var expectedGenreBefore = genreDaoJdbc.getById(EXISTING_GENRE_ID);
        assertThat(actualGenreBefore).usingRecursiveComparison().isEqualTo(expectedGenreBefore);

        var actualGenreAfter = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXPECTED_GENRE_ANOTHER_NAME)
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
        genreDaoJdbc.update(actualGenreAfter, true);

        var expectedGenreAfter = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXPECTED_GENRE_ANOTHER_NAME)
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
        var actualGenre = genreDaoJdbc.getById(EXISTING_GENRE_ID);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenreAfter);
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