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
import ru.pupov.config.AppProp;
import ru.pupov.config.DataInfoProvider;
import ru.pupov.dao.impl.QuestionDaoImpl;
import ru.pupov.domain.Answer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс QuestionDaoImpl")
@ExtendWith(MockitoExtension.class)
class QuestionDaoImplTest {

    @Mock
    private DataInfoProvider dataInfoProvider;
    @InjectMocks
    private QuestionDaoImpl questionDao;

    @DisplayName("Корректно извлекает данные")
    @Test
    void shouldExtractCorrectQuestions() {
        Mockito.doReturn("data.csv").when(dataInfoProvider).getDataResourcePath();
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