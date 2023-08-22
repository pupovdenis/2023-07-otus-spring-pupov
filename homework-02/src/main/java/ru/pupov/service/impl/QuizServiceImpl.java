package ru.pupov.service.impl;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.pupov.config.AppProp;
import ru.pupov.dao.QuestionDao;
import ru.pupov.domain.Question;
import ru.pupov.domain.Student;
import ru.pupov.service.IOService;
import ru.pupov.service.QuizService;
import ru.pupov.service.StudentService;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    public static final String INCORRECT_INPUT_MESSAGE = "Incorrect input. Please, enter a number of answer";

    public static final int MIN_ANSWER_NUMBER = 1;

    private static final String START_MESSAGE = "\n\nLet's start a quiz!";

    private static final String SEPARATOR_LINE = "\r";

    private static final String EMPTY_QUESTIONS_MESSAGE = "Failed to get any question";

    private static final String ENTER_YOUR_ANSWER_MESSAGE = "Enter your answer: ";

    private static final String END_MESSAGE_FORMAT = "%s, you got %d correct answers. ";

    private static final String RESULT_SUCCESS_MESSAGE = "You passed the test\n";

    private static final String RESULT_FAIL_MESSAGE = "You did not passed the test\n";

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final ConversionService conversionService;

    private final StudentService studentService;

    private final AppProp appProp;

    public QuizServiceImpl(IOService ioService,
                           QuestionDao questionDao,
                           ConversionService conversionService,
                           StudentService studentService,
                           AppProp appProp) {
        this.ioService = ioService;
        this.questionDao = questionDao;
        this.conversionService = conversionService;
        this.studentService = studentService;
        this.appProp = appProp;
    }

    public void run() {
        var student = studentService.getStudent();
        var questionList = questionDao.getAll();
        if (questionList.isEmpty()) {
            ioService.outputString(EMPTY_QUESTIONS_MESSAGE);
            return;
        }
        ioService.outputString(START_MESSAGE);
        int rightAnswersCounter = doQuizAndGetResult(questionList);
        outputFinish(student, rightAnswersCounter);
    }

    private void outputFinish(Student student, int rightAnswersCounter) {
        ioService.outputString(SEPARATOR_LINE);
        ioService.outputString(String.format(END_MESSAGE_FORMAT, student, rightAnswersCounter));
        if (rightAnswersCounter >= appProp.getPassingNumberOfCorrectAnswers()) {
            ioService.outputString(RESULT_SUCCESS_MESSAGE);
            return;
        }
        ioService.outputString(RESULT_FAIL_MESSAGE);
    }

    private int doQuizAndGetResult(List<Question> questionList) {
        int rightAnswersCounter = 0;
        for (var question : questionList) {
            var questionOutputString = conversionService.convert(question, String.class);
            ioService.outputString(questionOutputString, true);
            var answerInt = ioService.readIntWithPromptByInterval(MIN_ANSWER_NUMBER, question.getAnswers().size(),
                    ENTER_YOUR_ANSWER_MESSAGE, INCORRECT_INPUT_MESSAGE);
            if (question.getAnswers().get(answerInt - 1).isCorrectAnswer()) {
                rightAnswersCounter++;
            }
        }
        return rightAnswersCounter;
    }
}
