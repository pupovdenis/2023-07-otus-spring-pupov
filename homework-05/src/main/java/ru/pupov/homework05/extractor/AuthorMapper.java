package ru.pupov.homework05.extractor;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        var id = rs.getLong("id");
        return Author.builder()
                .id(id)
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .books(getBooks(rs, id))
                .build();
    }

    private List<Book> getBooks(ResultSet rs, Long id) throws SQLException {
        var books = new ArrayList<Book>();
        if (rs.getLong("book_id") > 0) {
            do {
                books.add(getBook(rs));
            } while (rs.next() && id == rs.getLong("id"));
        }
        return books;
    }

    private Book getBook(ResultSet rs) throws SQLException {
        return Book.builder()
                .id(rs.getLong("book_id"))
                .name(rs.getString("book_name"))
                .build();
    }
}
