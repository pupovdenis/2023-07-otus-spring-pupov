package ru.pupov.homework05.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.pupov.homework05.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class GenresRsExtractor implements ResultSetExtractor<List<Genre>> {
    @Override
    public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var genreMap = new HashMap<Long, Genre>();
        while (rs.next()) {
            var id = rs.getLong("id");
            genreMap.putIfAbsent(id, Genre.builder()
                    .id(id)
                    .name(rs.getString("name"))
                    .build());
        }
        return new ArrayList<>(genreMap.values());
    }
}
