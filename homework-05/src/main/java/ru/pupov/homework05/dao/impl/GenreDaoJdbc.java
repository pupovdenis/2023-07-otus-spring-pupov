package ru.pupov.homework05.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.pupov.homework05.dao.GenreDao;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.domain.Genre;
import ru.pupov.homework05.extractor.GenreMapper;
import ru.pupov.homework05.extractor.GenresRsExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

@Repository
public class GenreDaoJdbc implements GenreDao {

    public static final long START_ID = 1L;

    private final Long nextId;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final KeyHolder keyHolder;

    private final GenreMapper genreMapper;

    private final GenresRsExtractor genresRsExtractor;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations,
                        GenreMapper genreMapper,
                        GenresRsExtractor genresRsExtractor) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.genreMapper = genreMapper;
        this.genresRsExtractor = genresRsExtractor;
        keyHolder = new GeneratedKeyHolder();
        nextId = getNextId();
    }

    @Override
    public Long insert(Genre genre) {
        if (genre.getId() != null) {
            throw new RuntimeException("before inserting genre must be without id");
        }
        var id = isNull(keyHolder.getKey()) ? nextId : keyHolder.getKey().longValue() + 1;

        var mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", id);
        mapSqlParameterSource.addValue("name", genre.getName());

        namedParameterJdbcOperations.update("""
                insert into genre (id, name) 
                values (:id, :name)
                """, mapSqlParameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Genre getById(Long id) {
        var params = Map.of("id", id);
        try {
            return namedParameterJdbcOperations.queryForObject("""
                    select g.id, g.name, b.id book_id, b.name book_name 
                    from genre g
                    left join book b on b.genre_id=g.id
                    where g.id = :id
                    """, params, genreMapper);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query("""
                select g.id, g.name, b.id book_id, b.name book_name 
                from genre g
                left join book b on b.genre_id=g.id
                """, genresRsExtractor);
    }

    @Override
    public void update(Genre genre, boolean withBooks) {
        var params = new HashMap<String, Object>();
        params.put("id", genre.getId());
        params.put("name", genre.getName());
        if (withBooks) {
            var bookIds = genre.getBooks().stream()
                    .map(Book::getId)
                    .toList();
            params.put("bookIds", bookIds);
            doUpdateWithBooks(params);
        } else {
            doUpdate(params);
        }
    }

    private void doUpdateWithBooks(Map<String, Object> params) {
        namedParameterJdbcOperations.update("""
                begin transaction;
                                
                update genre
                set name=:name
                where id=:id;
                                
                update book b
                set b.genre_id = null
                where genre_id=:id
                	and b.id not in (:bookIds);
                                
                update book
                set genre_id=:id
                where id in (:bookIds);
                                
                commit;
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
    public void deleteById(Long id) {
        var params = Map.of("id", id);
        namedParameterJdbcOperations.update("""
                begin transaction;
                                
                update book b
                set b.genre_id = null
                where genre_id=:id;
                                
                delete from genre 
                where id=:id;
                                
                commit;
                """, params);
    }

    private Long getNextId() {
        var currentMaxId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("""
                select max(id) from genre
                """, Long.class);
        return isNull(currentMaxId) ? START_ID : currentMaxId + 1;
    }
}
