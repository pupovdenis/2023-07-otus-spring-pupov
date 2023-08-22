package ru.pupov.dao;

import ru.pupov.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> getAll();
}
