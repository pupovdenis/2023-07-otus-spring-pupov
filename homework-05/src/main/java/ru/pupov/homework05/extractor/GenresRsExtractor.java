package ru.pupov.homework05.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.pupov.homework05.domain.Book;
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
            if (genreMap.containsKey(id)) {
                genreMap.get(id).getBooks().add(Book.builder()
                        .id(rs.getLong("book_id"))
                        .name(rs.getString("book_name"))
                        .build());
            } else {
                var books = new ArrayList<Book>();
                books.add(Book.builder()
                        .id(rs.getLong("book_id"))
                        .name(rs.getString("book_name"))
                        .build());
                genreMap.put(id, Genre.builder()
                        .id(id)
                        .name(rs.getString("name"))
                        .books(books)
                        .build());
            }
        }
        return new ArrayList<>(genreMap.values());
    }
}
