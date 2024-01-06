package ru.pupov.homework09.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pupov.homework09.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
