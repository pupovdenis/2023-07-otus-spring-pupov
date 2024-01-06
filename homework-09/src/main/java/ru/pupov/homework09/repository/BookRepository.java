package ru.pupov.homework09.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pupov.homework09.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genre"})
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();

    @EntityGraph(attributePaths = "genre")
    List<Book> findByAuthorId(Long authorId);

    @EntityGraph(attributePaths = "author")
    List<Book> findByGenreId(Long genreId);
}
