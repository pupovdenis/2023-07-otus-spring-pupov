package ru.pupov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import ru.pupov.config.AppProp;
import ru.pupov.dao.QuestionDao;
import ru.pupov.domain.Answer;
import ru.pupov.domain.Question;
import ru.pupov.domain.Student;
import ru.pupov.service.IOService;
import ru.pupov.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@DisplayName("Класс QuizServiceImpl")
@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    private static final String RESULT_SUCCESS_MESSAGE = "You passed the test\n";
    private static final String RESULT_FAIL_MESSAGE = "You did not passed the test\n";
    private int correctAnswerTest = 5;

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @Mock
    private StudentService studentService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private AppProp appProp;
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
        Mockito.doReturn(1).when(appProp).getPassingNumberOfCorrectAnswers();
        Mockito.doReturn(List.of(question)).when(questionDao).getAll();
        Mockito.doNothing().when(ioService).outputString(anyString());
        Mockito.doReturn(correctAnswerTest).when(ioService)
                .readIntWithPromptByInterval(anyInt(), anyInt(), anyString(), anyString());
        Mockito.doReturn(new Student("Bruno", "Traven")).when(studentService).getStudent();
        assertDoesNotThrow(() -> quizService.run());
    }

    @DisplayName("Проводит успешную сдачу")
    @Test
    void shouldMakeSuccessResult() {
        Mockito.doReturn(1).when(appProp).getPassingNumberOfCorrectAnswers();
        Mockito.doReturn(List.of(question)).when(questionDao).getAll();
        Mockito.doNothing().when(ioService).outputString(anyString());
        Mockito.doReturn(correctAnswerTest).when(ioService)
                .readIntWithPromptByInterval(anyInt(), anyInt(), anyString(), anyString());
        Mockito.doReturn(new Student("Bruno", "Traven")).when(studentService).getStudent();
        quizService.run();
        Mockito.verify(ioService, times(1)).outputString(RESULT_SUCCESS_MESSAGE);
        Mockito.verify(ioService, times(0)).outputString(RESULT_FAIL_MESSAGE);
    }

    @DisplayName("Проводит провальную сдачу")
    @Test
    void shouldMakeFailResult() {
        Mockito.doReturn(2).when(appProp).getPassingNumberOfCorrectAnswers();
        Mockito.doReturn(List.of(question)).when(questionDao).getAll();
        Mockito.doNothing().when(ioService).outputString(anyString());
        Mockito.doReturn(correctAnswerTest).when(ioService)
                .readIntWithPromptByInterval(anyInt(), anyInt(), anyString(), anyString());
        Mockito.doReturn(new Student("Bruno", "Traven")).when(studentService).getStudent();
        quizService.run();
        Mockito.verify(ioService, times(0)).outputString(RESULT_SUCCESS_MESSAGE);
        Mockito.verify(ioService, times(1)).outputString(RESULT_FAIL_MESSAGE);
    }
}