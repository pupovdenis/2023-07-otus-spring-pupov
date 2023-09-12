package ru.pupov.homework05.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.pupov.homework05.dao.BookDao;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.extractor.BookMapper;
import ru.pupov.homework05.extractor.BooksRsExtractor;

import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Repository
public class BookDaoJdbc implements BookDao {

    public static final long START_ID = 1L;

    private final Long nextId;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final KeyHolder keyHolder;

    private final BookMapper bookMapper;

    private final BooksRsExtractor booksRsExtractor;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations,
                       BookMapper bookMapper,
                       BooksRsExtractor booksRsExtractor) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.bookMapper = bookMapper;
        this.booksRsExtractor = booksRsExtractor;
        keyHolder = new GeneratedKeyHolder();
        nextId = getNextId();
    }

    @Override
    public Long insert(Book book) {
        if (book.getId() != null) {
            throw new RuntimeException("before inserting book must be without id");
        }
        var id = isNull(keyHolder.getKey()) ? nextId : keyHolder.getKey().longValue() + 1;

        var mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", id);
        mapSqlParameterSource.addValue("name", book.getName());
        mapSqlParameterSource.addValue("authorId", book.getAuthor().getId());
        mapSqlParameterSource.addValue("genreId", book.getGenre().getId());

        namedParameterJdbcOperations.update("""
                insert into book (id, name, author_id, genre_id) 
                values (:id, :name, :authorId, :genreId)
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
                """, booksRsExtractor);
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
    public void deleteById(Long id) {
        var params = Map.of("id", id);
        namedParameterJdbcOperations.update("""
                delete from book 
                where id=:id
                """, params);
    }

    private Long getNextId() {
        var currentMaxId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("""
                select max(id) from book
                """, Long.class);
        return isNull(currentMaxId) ? START_ID : currentMaxId + 1;
    }
}
