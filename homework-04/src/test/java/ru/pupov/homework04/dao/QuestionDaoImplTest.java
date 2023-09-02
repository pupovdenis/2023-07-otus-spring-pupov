package ru.pupov.homework04.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pupov.homework04.config.FileNameProvider;
import ru.pupov.homework04.dao.impl.QuestionDaoImpl;
import ru.pupov.homework04.domain.Answer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс QuestionDaoImpl")
@ExtendWith(MockitoExtension.class)
class QuestionDaoImplTest {

    @Mock
    private FileNameProvider fileNameProvider;
    @InjectMocks
    private QuestionDaoImpl questionDao;

    @DisplayName("Корректно извлекает данные")
    @Test
    void shouldExtractCorrectQuestions() {
        Mockito.doReturn("data.csv").when(fileNameProvider).getFileName();
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