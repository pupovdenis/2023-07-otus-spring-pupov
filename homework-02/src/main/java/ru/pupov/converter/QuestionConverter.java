package ru.pupov.converter;

import ru.pupov.domain.Question;

public interface QuestionConverter {

    String toQuizString(Question question);
}
