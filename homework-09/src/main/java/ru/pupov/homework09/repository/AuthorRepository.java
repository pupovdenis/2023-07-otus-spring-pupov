package ru.pupov.homework09.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pupov.homework09.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
