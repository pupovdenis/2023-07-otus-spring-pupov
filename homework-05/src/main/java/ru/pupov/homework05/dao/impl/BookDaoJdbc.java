package ru.pupov.homework05.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.pupov.homework05.dao.BookDao;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.mapper.BookMapper;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final KeyHolder keyHolder;

    private final BookMapper bookMapper;

    public BookDaoJdbc(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                               NamedParameterJdbcOperations namedParameterJdbcOperations,
                       BookMapper bookMapper) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.bookMapper = bookMapper;
        keyHolder = new GeneratedKeyHolder();
    }



    @Override
    public Long insert(Book book) {
        var mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", book.getName());
        mapSqlParameterSource.addValue("authorId", book.getAuthor().getId());
        mapSqlParameterSource.addValue("genreId", book.getGenre().getId());

        namedParameterJdbcOperations.update("""
                insert into book (name, author_id, genre_id)
                values (:name, :authorId, :genreId)
                """, mapSqlParameterSource, keyHolder);
        return requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Book getById(Long id) {
        var params = Map.of("id", id);
        try {
            return namedParameterJdbcOperations.queryForObject("""
                    select b.id, b.name, b.author_id, a.first_name, a.last_name, b.genre_id, g.name genre_name
                    from book b
                    left join author a on a.id = b.author_id
                    left join genre g on g.id = b.genre_id
                    where b.id = :id
                    """, params, bookMapper);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query("""
                select b.id, b.name, b.author_id, a.first_name, a.last_name, b.genre_id, g.name genre_name
                from book b
                left join author a on a.id = b.author_id
                left join genre g on g.id = b.genre_id
                """, bookMapper);
    }

    @Override
    public void update(Book book) {
        Map<String, Object> params = Map.of(
                "id", book.getId(),
                "name", book.getName(),
                "authorId", book.getAuthor().getId(),
                "genreId", book.getGenre().getId());
        namedParameterJdbcOperations.update("""
                update book
                set name=:name, author_id=:authorId, genre_id=:genreId
                where id=:id
                """, params);
    }

    @Override
    public boolean deleteById(Long id) {
        var params = Map.of("id", id);
        var result = namedParameterJdbcOperations.update("""
                delete from book
                where id=:id
                """, params);
        return result != 0;
    }

    @Override
    public List<Book> getAllByAuthorId(Long authorId) {
        var params = Map.of("authorId", authorId);
        return namedParameterJdbcOperations.query("""
                select b.id, b.name, b.author_id, a.first_name, a.last_name, b.genre_id, g.name genre_name
                from book b
                join author a on a.id = b.author_id
                left join genre g on g.id = b.genre_id
                where a.id=:authorId
                """, params, bookMapper);
    }

    @Override
    public List<Book> getAllByGenreId(Long genreId) {
        var params = Map.of("genreId", genreId);
        return namedParameterJdbcOperations.query("""
                select b.id, b.name, b.author_id, a.first_name, a.last_name, b.genre_id, g.name genre_name
                from book b
                left join author a on a.id = b.author_id
                join genre g on g.id = b.genre_id
                where g.id=:genreId
                """, params, bookMapper);
    }
}
