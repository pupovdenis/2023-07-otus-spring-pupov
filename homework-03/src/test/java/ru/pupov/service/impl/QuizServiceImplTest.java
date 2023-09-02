package ru.pupov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import ru.pupov.config.AppProps;
import ru.pupov.dao.QuestionDao;
import ru.pupov.domain.Answer;
import ru.pupov.domain.Question;
import ru.pupov.domain.Student;
import ru.pupov.service.LocalizationIOService;
import ru.pupov.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Класс QuizServiceImpl")
@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    private static final String RESULT_SUCCESS_MESSAGE = "result.success.message";
    private static final String RESULT_FAIL_MESSAGE = "result.fail.message";
    private int correctAnswerTest = 5;

    @Mock
    private QuestionDao questionDao;
    @Mock
    private StudentService studentService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private AppProps appProps;
    @Mock
    private LocalizationIOService localizationIOService;
    @InjectMocks
    private QuizServiceImpl quizService;

    private Question question;

    @BeforeEach
    void setUp() {
        var answers = new ArrayList<Answer>();
        answers.add(new Answer("тестовый неправильный ответ №1", false));
        answers.add(new Answer("тестовый неправильный ответ №2", false));
        answers.add(new Answer("тестовый неправильный ответ №3", false));
        answers.add(new Answer("тестовый неправильный ответ №4", false));
        answers.add(new Answer("тестовый правильный ответ №5", true));
        question = new Question("Тестовый вопрос", answers);
        correctAnswerTest = 5;
    }

    @DisplayName("Без ошибок проводит тестирование")
    @Test
    void shouldNotThrowAnyException() {
        doReturn(1).when(appProps).getPassingNumberOfCorrectAnswers();
        doReturn(List.of(question)).when(questionDao).getAll();
        doNothing().when(localizationIOService).outputString(any(), anyBoolean());
        doNothing().when(localizationIOService).outputString(any(), anyBoolean(), anyInt());
        doReturn(correctAnswerTest).when(localizationIOService)
                .readIntWithLocalizedPromptByInterval(anyInt(), anyInt(), anyString(), anyString());
        doReturn(new Student("Bruno", "Traven")).when(studentService).getStudent();
        assertDoesNotThrow(() -> quizService.run());
    }

    @DisplayName("Проводит успешную сдачу")
    @Test
    void shouldMakeSuccessResult() {
        doReturn(1).when(appProps).getPassingNumberOfCorrectAnswers();
        doReturn(List.of(question)).when(questionDao).getAll();
        doNothing().when(localizationIOService).outputString(any(), anyBoolean());
        doNothing().when(localizationIOService).outputString(any(), anyBoolean(), anyInt());
        doReturn(correctAnswerTest).when(localizationIOService)
                .readIntWithLocalizedPromptByInterval(anyInt(), anyInt(), anyString(), anyString());
        doReturn(new Student("Bruno", "Traven")).when(studentService).getStudent();
        quizService.run();
        verify(localizationIOService, times(1)).outputString(RESULT_SUCCESS_MESSAGE, true);
    }

    @DisplayName("Проводит провальную сдачу")
    @Test
    void shouldMakeFailResult() {
        doReturn(2).when(appProps).getPassingNumberOfCorrectAnswers();
        doReturn(List.of(question)).when(questionDao).getAll();
        doNothing().when(localizationIOService).outputString(any(), anyBoolean());
        doNothing().when(localizationIOService).outputString(any(), anyBoolean(), anyInt());
        doReturn(correctAnswerTest).when(localizationIOService)
                .readIntWithLocalizedPromptByInterval(anyInt(), anyInt(), anyString(), anyString());
        doReturn(new Student("Bruno", "Traven")).when(studentService).getStudent();
        quizService.run();
        verify(localizationIOService, times(1)).outputString(RESULT_FAIL_MESSAGE, true);
    }
}