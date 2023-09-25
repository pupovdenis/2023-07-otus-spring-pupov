package ru.pupov.homework05.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework05.dao.BookDao;
import ru.pupov.homework05.domain.Book;
import ru.pupov.homework05.service.BookService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Override
    @Transactional
    public Long create(Book book) {
        if (book.getId() != null) {
            log.error("before inserting book must be without id");
            return null;
        }
        return bookDao.insert(book);
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Override
    public Book getById(Long id) {
        return bookDao.getById(id);
    }

    @Override
    public List<Book> getAllByAuthorId(Long authorId) {
        return bookDao.getAllByAuthorId(authorId);
    }

    @Override
    public List<Book> getAllByGenreId(Long genreId) {
        return bookDao.getAllByGenreId(genreId);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return bookDao.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Book book) {
        bookDao.update(book);
    }
}
