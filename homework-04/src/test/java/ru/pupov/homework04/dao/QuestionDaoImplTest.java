package ru.pupov.homework04.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.pupov.homework04.config.FileNameProvider;
import ru.pupov.homework04.dao.impl.QuestionDaoImpl;
import ru.pupov.homework04.domain.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@DisplayName("Класс QuestionDaoImpl")
@SpringBootTest(classes = {QuestionDaoImpl.class})
class QuestionDaoImplTest {

    @MockBean
    private FileNameProvider fileNameProvider;
    @Autowired
    private QuestionDaoImpl questionDao;

    @DisplayName("Корректно извлекает данные")
    @Test
    void shouldExtractCorrectQuestions() {
        doReturn("data.csv").when(fileNameProvider).getFileName();
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