package ru.pupov.homework06.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.pupov.homework06.entity.Book;
import ru.pupov.homework06.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Book save(Book book) {
        if (book.getId() != null) {
            return entityManager.merge(book);
        }
        entityManager.persist(book);
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        var entityGraph = entityManager.getEntityGraph("book-entity-graph");
        try {
            var book = entityManager.createQuery("""
                            select b
                            from Book b
                            where b.id = :id
                            """, Book.class)
                    .setParameter("id", id)
                    .setHint(FETCH.getKey(), entityGraph)
                    .getSingleResult();
            return Optional.ofNullable(book);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        var entityGraph = entityManager.getEntityGraph("book-entity-graph");
        return entityManager.createQuery(""" 
                        select b
                        from Book b
                        """, Book.class)
                .setHint(FETCH.getKey(), entityGraph)
                .getResultList();
    }

    @Override
    public boolean deleteById(Long id) {
        var book = entityManager.find(Book.class, id);
        entityManager.remove(book);
        return true;
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) {
        var entityGraph = entityManager.getEntityGraph("books-by-author-entity-graph");
        return entityManager.createQuery("""
                        select b
                        from Book b
                        where b.author.id = :author_id
                        """, Book.class)
                .setParameter("author_id", authorId)
                .setHint(FETCH.getKey(), entityGraph)
                .getResultList();
    }

    @Override
    public List<Book> findByGenreId(Long genreId) {
        var entityGraph = entityManager.getEntityGraph("books-by-genre-entity-graph");
        var query = entityManager.createQuery("""
                select b
                from Book b
                where b.genre.id = :genre_id
                """, Book.class);
        query.setParameter("genre_id", genreId);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }
}
