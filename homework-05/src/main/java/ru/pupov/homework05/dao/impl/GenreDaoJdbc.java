package ru.pupov.homework05.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework05.dao.GenreDao;
import ru.pupov.homework05.domain.Genre;
import ru.pupov.homework05.extractor.GenreMapper;
import ru.pupov.homework05.extractor.GenresRsExtractor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class GenreDaoJdbc implements GenreDao {

    public static final String BOOK_IDS_DELIMITER = ",";

    public static final String SQL_IDS_DELIMITER = ",";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final KeyHolder keyHolder;

    private final GenreMapper genreMapper;

    private final GenresRsExtractor genresRsExtractor;

    public GenreDaoJdbc(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                NamedParameterJdbcOperations namedParameterJdbcOperations,
                        GenreMapper genreMapper,
                        GenresRsExtractor genresRsExtractor) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.genreMapper = genreMapper;
        this.genresRsExtractor = genresRsExtractor;
        keyHolder = new GeneratedKeyHolder();
    }

    @Override
    @Transactional
    public Long insert(Genre genre) {
        if (genre.getId() != null) {
            throw new RuntimeException("before inserting genre must be without id");
        }

        var mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", genre.getName());

        namedParameterJdbcOperations.update("""
                insert into genre (name)
                values (:name)
                """, mapSqlParameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Genre getById(Long id) {
        var params = Map.of("id", id);
        try {
            return namedParameterJdbcOperations.queryForObject("""
                    select g.id, g.name
                    from genre g
                    where g.id = :id
                    """, params, genreMapper);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query("""
                select g.id, g.name
                from genre g
                """, genresRsExtractor);
    }

    @Override
    @Transactional
    public void update(Genre genre, String bookIdsString) {
        var params = new HashMap<String, Object>();
        params.put("id", genre.getId());
        params.put("name", genre.getName());
        if (bookIdsString != null && !bookIdsString.equals("null") && !bookIdsString.isBlank()) {
            List<Long> bookIds;
            try {
                bookIds = Arrays.stream(bookIdsString.split(BOOK_IDS_DELIMITER))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .toList();
            } catch (Exception e) {
                throw new RuntimeException("invalid book ids input data");
            }
            params.put("bookIds", bookIds);
            doUpdateWithBooks(params);
        } else {
            doUpdate(params);
        }
    }

    private void doUpdateWithBooks(Map<String, Object> params) {
        namedParameterJdbcOperations.update("""
                update genre
                set name=:name
                where id=:id;
                """, params);
        namedParameterJdbcOperations.update("""
                update book b
                set b.genre_id = null
                where genre_id=:id
                	and b.id not in (:bookIds);
                """, params);
        namedParameterJdbcOperations.update("""
                update book
                set genre_id=:id
                where id in (:bookIds);
                """, params);
    }

    private void doUpdate(Map<String, Object> params) {
        namedParameterJdbcOperations.update("""
                update genre
                set name=:name
                where id=:id;
                """, params);
    }

    @Override
    public boolean deleteById(Long id) {
        var params = Map.of("id", id);
        namedParameterJdbcOperations.update("""
                update book b
                set b.genre_id = null
                where genre_id=:id;
                """, params);
        var result = namedParameterJdbcOperations.update("""
                delete from genre
                where id=:id;
                """, params);
        return result > 0;
    }
}
