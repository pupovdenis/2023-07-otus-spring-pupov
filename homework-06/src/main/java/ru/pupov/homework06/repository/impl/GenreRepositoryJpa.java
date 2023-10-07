package ru.pupov.homework06.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.pupov.homework06.entity.Genre;
import ru.pupov.homework06.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() != null) {
            return entityManager.merge(genre);
        }
        entityManager.persist(genre);
        return genre;
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    public List<Genre> findAll() {
        var query = entityManager.createQuery(""" 
                select g
                from Genre g
                """, Genre.class);
        return query.getResultList();
    }

    @Override
    public boolean deleteById(Long id) {
        var genre = entityManager.find(Genre.class, id);
        entityManager.remove(genre);
        return true;
    }
}
