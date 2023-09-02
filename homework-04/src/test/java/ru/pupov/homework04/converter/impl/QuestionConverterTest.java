package ru.pupov.homework04.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pupov.homework04.converter.QuestionConverter;
import ru.pupov.homework04.converter.StudentConverter;
import ru.pupov.homework04.domain.Answer;
import ru.pupov.homework04.domain.Question;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс QuestionConverterTest")
@SpringBootTest
class QuestionConverterTest {

    private Question question;

    @Autowired
    private QuestionConverter questionConverter;

    @BeforeEach
    void setUp() {
        var answers = new ArrayList<Answer>();
        answers.add(new Answer("тестовый неправильный ответ №1", false));
        answers.add(new Answer("тестовый неправильный ответ №2", false));
        answers.add(new Answer("тестовый неправильный ответ №3", false));
        answers.add(new Answer("тестовый неправильный ответ №4", false));
        answers.add(new Answer("тестовый правильный ответ №5", true));
        question = new Question("Тестовый вопрос", answers);
    }

    @DisplayName("Корректно конвертирует вопрос в строку для вывода")
    @Test
    void shouldConvertQuestionToQuizString() {
        var result = questionConverter.convert(question);
        assertThat(result).isEqualTo(getCorrectResult());
    }

    private String getCorrectResult() {
        return """
                            
                Тестовый вопрос
                1) тестовый неправильный ответ №1
                2) тестовый неправильный ответ №2
                3) тестовый неправильный ответ №3
                4) тестовый неправильный ответ №4
                5) тестовый правильный ответ №5
                            """;
    }

    @Configuration
    static class TestConfiguration {
        @Bean
        public QuestionConverter questionConverter() {
            return new QuestionConverter();
        }
    }
}