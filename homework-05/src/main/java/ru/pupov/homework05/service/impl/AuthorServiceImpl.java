package ru.pupov.homework05.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pupov.homework05.dao.AuthorDao;
import ru.pupov.homework05.domain.Author;
import ru.pupov.homework05.service.AuthorService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    @Transactional
    public Long insert(Author author) {
        if (author.getId() != null) {
            log.error("before inserting author must be without id");
            return null;
        }
        return authorDao.insert(author);
    }

    @Override
    public List<Author> getAll() {
        return authorDao.getAll();
    }

    @Override
    public Author getById(Long id) {
        return authorDao.getById(id);
    }

    @Override
    @Transactional
    public void update(Author author) {
        authorDao.update(author);
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return authorDao.deleteById(id);
    }
}
