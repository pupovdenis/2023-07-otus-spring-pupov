package ru.pupov.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.pupov.config.AppConfig;
import ru.pupov.dao.impl.QuestionDaoImpl;
import ru.pupov.domain.Answer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс QuestionDaoImpl")
@ExtendWith(MockitoExtension.class)
class QuestionDaoImplTest {

    @Spy
    private AppConfig appConfig;
    @InjectMocks
    private QuestionDaoImpl questionDao;

    @DisplayName("Корректно извлекает данные")
    @Test
    void shouldExtractCorrectQuestions() {
        Mockito.doReturn("data.csv").when(appConfig).getCsvPath();
        var questions = questionDao.getAll();
        assertThat(questions)
                .filteredOn(question ->
                        !question.getQuestion().isBlank()
                                && question.getAnswers().stream()
                                .filter(Answer::isCorrectAnswer)
                                .toList().size() == 1)
                .hasSize(5);
    }
}