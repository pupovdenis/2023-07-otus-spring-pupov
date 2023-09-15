package ru.pupov.homework05.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework05.dao.AuthorDao;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.extractor.AuthorMapper;
import ru.pupov.homework05.extractor.AuthorsRsExtractor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class AuthorDaoJdbc implements AuthorDao {

    public static final String BOOK_IDS_DELIMITER = ",";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final KeyHolder keyHolder;

    private final AuthorMapper authorMapper;

    private final AuthorsRsExtractor authorsRsExtractor;

    public AuthorDaoJdbc(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                 NamedParameterJdbcOperations namedParameterJdbcOperations,
                         AuthorMapper authorMapper,
                         AuthorsRsExtractor authorsRsExtractor) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.authorMapper = authorMapper;
        this.authorsRsExtractor = authorsRsExtractor;
        keyHolder = new GeneratedKeyHolder();
    }

    @Override
    @Transactional
    public Long insert(Author author) {
        if (author.getId() != null) {
            throw new RuntimeException("before inserting author must be without id");
        }
        var mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("firstName", author.getFirstName());
        mapSqlParameterSource.addValue("lastName", author.getLastName());

        namedParameterJdbcOperations.update("""
                insert into author (first_name, last_name)
                values (:firstName, :lastName)
                """, mapSqlParameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Author getById(Long id) {
        var params = Map.of("id", id);
        try {
            return namedParameterJdbcOperations.queryForObject("""
                    select a.id, a.first_name, a.last_name
                    from author a
                    where a.id = :id
                    """, params, authorMapper);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query("""
                select a.id, a.first_name, a.last_name
                from author a
                """, authorsRsExtractor);
    }

    @Override
    @Transactional
    public void update(Author author, String bookIdsString) {
        var params = new HashMap<String, Object>();
        params.put("id", author.getId());
        params.put("firstName", author.getFirstName());
        params.put("lastName", author.getLastName());
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
                update author
                set first_name=:firstName, last_name=:lastName
                where id=:id;
                """, params);
        namedParameterJdbcOperations.update("""
                update book b
                set b.author_id = null
                where author_id=:id
                	and b.id not in (:bookIds);
                """, params);
        namedParameterJdbcOperations.update("""
                update book
                set author_id=:id
                where id in (:bookIds);
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
    @Transactional
    public boolean deleteById(Long id) {
        var params = Map.of("id", id);
        namedParameterJdbcOperations.update("""
                update book b
                set b.author_id = null
                where author_id=:id;
                """, params);
        var result = namedParameterJdbcOperations.update("""
                delete from author
                where id=:id;
                """, params);
        return result > 0;
    }
}
