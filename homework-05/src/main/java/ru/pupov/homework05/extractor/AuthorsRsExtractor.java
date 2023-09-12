package ru.pupov.homework05.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.domain.Book;

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
            if (authorMap.containsKey(id)) {
                authorMap.get(id).getBooks().add(Book.builder()
                        .id(rs.getLong("book_id"))
                        .name(rs.getString("book_name"))
                        .build());
            } else {
                var books = new ArrayList<Book>();
                books.add(Book.builder()
                        .id(rs.getLong("book_id"))
                        .name(rs.getString("book_name"))
                        .build());
                authorMap.put(id, Author.builder()
                        .id(id)
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .books(books)
                        .build());
            }
        }
        return new ArrayList<>(authorMap.values());
    }
}
