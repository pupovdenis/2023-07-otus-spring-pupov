package ru.pupov.homework05.extractor;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.pupov.homework05.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BooksRsExtractor implements ResultSetExtractor<List<Book>> {

    private final BookMapper bookMapper;

    @Override
    public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var books = new ArrayList<Book>();
        while (rs.next()) {
            books.add(bookMapper.mapRow(rs, rs.getRow()));
        }
        return books;
    }
}
