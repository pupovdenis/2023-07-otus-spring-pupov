package ru.pupov.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.pupov.converter.QuestionConverter;
import ru.pupov.domain.Answer;
import ru.pupov.domain.Question;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс QuestionConverterTest")
class QuestionConverterTest {

    private Question question;

    private final QuestionConverter questionConverter = new QuestionConverter();

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
}