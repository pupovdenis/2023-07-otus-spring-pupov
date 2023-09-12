package ru.pupov.homework05.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.pupov.homework05.dao.AuthorDao;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.extractor.AuthorMapper;
import ru.pupov.homework05.extractor.AuthorsRsExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

    public static final long START_ID = 1L;

    private final Long nextId;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final KeyHolder keyHolder;

    private final AuthorMapper authorMapper;

    private final AuthorsRsExtractor authorsRsExtractor;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations,
                         AuthorMapper authorMapper,
                         AuthorsRsExtractor authorsRsExtractor) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.authorMapper = authorMapper;
        this.authorsRsExtractor = authorsRsExtractor;
        keyHolder = new GeneratedKeyHolder();
        nextId = getNextId();
    }

    @Override
    public Long insert(Author author) {
        if (author.getId() != null) {
            throw new RuntimeException("before inserting author must be without id");
        }
        var id = isNull(keyHolder.getKey()) ? nextId : keyHolder.getKey().longValue() + 1;

        var mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", id);
        mapSqlParameterSource.addValue("firstName", author.getFirstName());
        mapSqlParameterSource.addValue("lastName", author.getLastName());

        namedParameterJdbcOperations.update("""
                insert into author (id, first_name, last_name) 
                values (:id, :firstName, :lastName)
                """, mapSqlParameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Author getById(Long id) {
        var params = Map.of("id", id);
        try {
            return namedParameterJdbcOperations.queryForObject("""
                    select a.id, a.first_name, a.last_name, b.id book_id, b.name book_name 
                    from author a
                    left join book b on b.author_id=a.id
                    where a.id = :id
                    """, params, authorMapper);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query("""
                select a.id, a.first_name, a.last_name, b.id book_id, b.name book_name 
                from author a
                left join book b on b.author_id=a.id
                """, authorsRsExtractor);
    }

    @Override
    public void update(Author author, boolean withBooks) {
        var params = new HashMap<String, Object>();
        params.put("id", author.getId());
        params.put("firstName", author.getFirstName());
        params.put("lastName", author.getLastName());
        if (withBooks) {
            var bookIds = author.getBooks().stream()
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
                                
                update author
                set first_name=:firstName, last_name=:lastName
                where id=:id;
                                
                update book b
                set b.author_id = null
                where author_id=:id
                	and b.id not in (:bookIds);
                                
                update book
                set author_id=:id
                where id in (:bookIds);
                                
                commit;
                """, params);
    }

    private void doUpdate(Map<String, Object> params) {
        namedParameterJdbcOperations.update("""
                update author
                set first_name=:firstName, last_name=:lastName
                where id=:id;
                """, params);
    }

    @Override
    public void deleteById(Long id) {
        var params = Map.of("id", id);
        namedParameterJdbcOperations.update("""
                begin transaction;
                                
                update book b
                set b.author_id = null
                where author_id=:id;
                                
                delete from author 
                where id=:id;
                                
                commit;
                """, params);
    }

    private Long getNextId() {
        var currentMaxId = namedParameterJdbcOperations.getJdbcOperations().queryForObject("""
                select max(id) from author
                """, Long.class);
        return isNull(currentMaxId) ? START_ID : currentMaxId + 1;
    }
}
