package ru.pupov.service.impl;

import org.springframework.stereotype.Service;
import ru.pupov.config.AppConfig;
import ru.pupov.converter.QuestionConverter;
import ru.pupov.dao.QuestionDao;
import ru.pupov.dao.StudentDao;
import ru.pupov.domain.Question;
import ru.pupov.domain.Student;
import ru.pupov.service.IOService;
import ru.pupov.service.QuizService;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private static final String START_MESSAGE = "\n\nLet's start a quiz!";
//    private static final String SEPARATOR_LINE = "---------------------------------------------------";
    private static final String SEPARATOR_LINE = "\r";
    private static final String EMPTY_QUESTIONS_MESSAGE = "Failed to get any question";
    private static final String ENTER_YOUR_ANSWER_MESSAGE = "Enter your answer: ";
    public static final int MIN_ANSWER_NUMBER = 1;
    public static final String INCORRECT_INPUT_MESSAGE = "Incorrect input. Please, enter a number of answer";
    private static final String END_MESSAGE_FORMAT = "%s, you got %d correct answers. ";
    private static final String RESULT_SUCCESS_MESSAGE = "You passed the test\n";
    private static final String RESULT_FAIL_MESSAGE = "You did not passed the test\n";

    private final IOService ioService;
    private final QuestionDao questionDao;
    private final QuestionConverter questionConverter;
    private final StudentDao studentDao;
    private final AppConfig appConfig;

    public QuizServiceImpl(IOService ioService,
                           QuestionDao questionDao,
                           QuestionConverter questionConverter,
                           StudentDao studentDao,
                           AppConfig appConfig) {
        this.ioService = ioService;
        this.questionDao = questionDao;
        this.questionConverter = questionConverter;
        this.studentDao = studentDao;
        this.appConfig = appConfig;
    }

    public void run() {
        var student = studentDao.getStudent();
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
        if (rightAnswersCounter >= appConfig.getPassingNumberOfCorrectAnswers()) {
            ioService.outputString(RESULT_SUCCESS_MESSAGE);
        }
        else {
            ioService.outputString(RESULT_FAIL_MESSAGE);
        }
    }

    private int doQuizAndGetResult(List<Question> questionList) {
        int rightAnswersCounter = 0;
        for (var question : questionList) {
            ioService.outputString(questionConverter.toQuizString(question), true);
            while (true) {
                try {
                    var answerInt = ioService.readIntWithPrompt(ENTER_YOUR_ANSWER_MESSAGE);
                    if (answerInt > question.getAnswers().size() || answerInt < MIN_ANSWER_NUMBER) {
                        throw new NumberFormatException();
                    }
                    if (question.getAnswers().get(answerInt - 1).isCorrectAnswer()) {
                        rightAnswersCounter++;
                    }
                    break;
                } catch (NumberFormatException nfe) {
                    ioService.outputString(INCORRECT_INPUT_MESSAGE);
                }
            }
        }
        return rightAnswersCounter;
    }
}
