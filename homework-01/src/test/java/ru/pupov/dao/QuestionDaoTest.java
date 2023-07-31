package ru.pupov.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.pupov.domain.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Класс QuestionDao")
class QuestionDaoTest {

    public static final String CSV_PATH = "data.csv";

    @DisplayName("Корректно извлекает данные")
    @Test
    void shouldExtractCorrectQuestions() {
        var questionDao = new QuestionDao(CSV_PATH);
        var questions = questionDao.getAll();
        assertEquals(questions.size(), 5);
        questions.forEach(question -> {
            assertFalse(question.getQuestion().isBlank());
            assertFalse(question.getAnswers().isEmpty());
            assertEquals(1, question.getAnswers().stream()
                    .filter(Answer::isCorrectAnswer)
                    .toList().size());
        });
    }
}