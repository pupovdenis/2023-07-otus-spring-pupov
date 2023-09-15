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
import ru.pupov.homework05.extractor.GenreMapper;
import ru.pupov.homework05.extractor.GenresRsExtractor;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("класс GenreDaoJdbc должен")
@JdbcTest
@Import({BookDaoJdbc.class, BookMapper.class, BooksRsExtractor.class,
        GenreDaoJdbc.class, GenresRsExtractor.class, GenreMapper.class})
class GenreDaoJdbcTest {

    private static final long EXISTING_GENRE_ID = 6L;
    private static final String EXISTING_GENRE_NAME = "Тест";
    public static final String EXPECTED_GENRE_ANOTHER_NAME = "Ода";

    private static final long EXISTING_BOOK_10_ID = 10L;
    public static final String EXISTING_BOOK_10_NAME = "Тестирование";

    private static final long EXISTING_BOOK_1_ID = 1L;
    public static final String EXISTING_BOOK_1_NAME = "Война и мир";

    private static final long EXISTING_BOOK_2_ID = 2L;
    public static final String EXISTING_BOOK_2_NAME = "Анна Каренина";

    public static final long EXISTING_AUTHOR_ID = 1L;
    public static final String EXISTING_AUTHOR_FIRST_NAME = "Лев";
    public static final String EXISTING_AUTHOR_LAST_NAME = "Толстой";

    public static final long EXISTING_AUTHOR_5_ID = 5L;
    public static final String EXISTING_AUTHOR_5_FIRST_NAME = "Тест";
    public static final String EXISTING_AUTHOR_5_LAST_NAME = "Тестов";

    public static final int EXPECTED_GENRES = 6;

    public static final Long EXPECTED_GENRE_ID = 7L;
    public static final String EXPECTED_GENRE_NAME = "Проза";
    private static final String NEW_BOOK_IDS_STRING = "1,2";

    @Autowired
    private BooksRsExtractor bookRsExtractor;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookDaoJdbc bookDaoJdbc;
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

        var actualBookListBefore = bookDaoJdbc.getAllByGenreId(EXISTING_GENRE_ID);
        var authorBefore = Author.builder()
                .id(EXISTING_AUTHOR_5_ID)
                .firstName(EXISTING_AUTHOR_5_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_5_LAST_NAME)
                .build();
        var genreBefore = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXISTING_GENRE_NAME).build();
        var expectedBookListBefore = List.of(
                Book.builder()
                        .id(EXISTING_BOOK_10_ID)
                        .author(authorBefore)
                        .genre(genreBefore)
                        .name(EXISTING_BOOK_10_NAME)
                        .build());
        assertThat(actualBookListBefore).usingRecursiveComparison().isEqualTo(expectedBookListBefore);

        var updatableGenre = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXPECTED_GENRE_ANOTHER_NAME)
                .build();
        genreDaoJdbc.update(updatableGenre, NEW_BOOK_IDS_STRING);

        var actualGenreAfter = genreDaoJdbc.getById(EXISTING_GENRE_ID);
        var expectedGenreAfter = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXPECTED_GENRE_ANOTHER_NAME)
                .build();
        assertThat(actualGenreAfter).usingRecursiveComparison().isEqualTo(expectedGenreAfter);

        var actualBookListAfter = bookDaoJdbc.getAllByGenreId(EXISTING_GENRE_ID);
        var authorAfter = Author.builder()
                .id(EXISTING_AUTHOR_ID)
                .firstName(EXISTING_AUTHOR_FIRST_NAME)
                .lastName(EXISTING_AUTHOR_LAST_NAME)
                .build();
        var genreAfter = Genre.builder()
                .id(EXISTING_GENRE_ID)
                .name(EXPECTED_GENRE_ANOTHER_NAME).build();
        var expectedBookListAfter = List.of(
                Book.builder()
                        .id(EXISTING_BOOK_1_ID)
                        .name(EXISTING_BOOK_1_NAME)
                        .author(authorAfter)
                        .genre(genreAfter)
                        .build(),
                Book.builder()
                        .id(EXISTING_BOOK_2_ID)
                        .name(EXISTING_BOOK_2_NAME)
                        .author(authorAfter)
                        .genre(genreAfter)
                        .build());
        assertThat(actualBookListAfter).usingRecursiveComparison().isEqualTo(expectedBookListAfter);
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