package ru.pupov.homework05.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.pupov.homework05.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class AuthorsRsExtractor implements ResultSetExtractor<List<Author>> {
    @Override
    public List<Author> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var authorMap = new HashMap<Long, Author>();
        while (rs.next()) {
            var id = rs.getLong("id");
            authorMap.putIfAbsent(id, Author.builder()
                    .id(id)
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .build());
        }
        return new ArrayList<>(authorMap.values());
    }
}
