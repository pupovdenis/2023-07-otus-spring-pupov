package ru.pupov.homework05.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.pupov.homework05.dao.AuthorDao;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.mapper.AuthorMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final KeyHolder keyHolder;

    private final AuthorMapper authorMapper;

    public AuthorDaoJdbc(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                 NamedParameterJdbcOperations namedParameterJdbcOperations,
                         AuthorMapper authorMapper) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.authorMapper = authorMapper;
        keyHolder = new GeneratedKeyHolder();
    }

    @Override
    public Long insert(Author author) {
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
                """, authorMapper);
    }

    @Override
    public void update(Author author) {
        var params = new HashMap<String, Object>();
        params.put("id", author.getId());
        params.put("firstName", author.getFirstName());
        params.put("lastName", author.getLastName());
        namedParameterJdbcOperations.update("""
                update author
                set first_name=:firstName, last_name=:lastName
                where id=:id;
                """, params);
    }

    @Override
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
