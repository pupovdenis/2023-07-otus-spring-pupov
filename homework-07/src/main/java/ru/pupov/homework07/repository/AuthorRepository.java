package ru.pupov.homework07.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pupov.homework07.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
