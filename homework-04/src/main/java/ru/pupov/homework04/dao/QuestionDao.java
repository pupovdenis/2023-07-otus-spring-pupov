package ru.pupov.homework04.dao;

import ru.pupov.homework04.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> getAll();
}
