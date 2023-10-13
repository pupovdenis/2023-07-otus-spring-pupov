package ru.pupov.homework06.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.pupov.homework06.entity.Author;
import ru.pupov.homework06.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Author save(Author author) {
        if (author.getId() != null) {
            return entityManager.merge(author);
        }
        entityManager.persist(author);
        return author;
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public List<Author> findAll() {
        var query = entityManager.createQuery(""" 
                select a
                from Author a
                """, Author.class);
        return query.getResultList();
    }

    @Override
    public boolean deleteById(Long id) {
        var author = entityManager.find(Author.class, id);
        entityManager.remove(author);
        return true;
    }
}
