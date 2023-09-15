package ru.pupov.homework05.extractor;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.pupov.homework05.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        var id = rs.getLong("id");
        return Genre.builder()
                .id(id)
                .name(rs.getString("name"))
                .build();
    }
}
