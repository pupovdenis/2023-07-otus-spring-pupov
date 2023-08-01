package ru.pupov.service.impl;

import ru.pupov.converter.QuestionConverter;
import ru.pupov.dao.QuestionDao;
import ru.pupov.domain.Question;
import ru.pupov.service.IOService;
import ru.pupov.service.QuizService;

import java.util.List;

public class QuizServiceImpl implements QuizService {

    private static final String START_MESSAGE = "\n\nLet's start a quiz!";
    private static final String END_MESSAGE = "You got correct answers: ";
    private static final String SEPARATOR_LINE = "---------------------------------------------------";
    private static final String EMPTY_QUESTIONS_MESSAGE = "Failed to get any question";
    private static final String ENTER_YOUR_ANSWER_MESSAGE = "Enter your answer: ";
    public static final int MIN_ANSWER_NUMBER = 1;

    private final IOService ioService;
    private final QuestionDao questionDao;
    private final QuestionConverter questionConverter;

    public QuizServiceImpl(IOService ioService, QuestionDao questionDao, QuestionConverter questionConverter) {
        this.ioService = ioService;
        this.questionDao = questionDao;
        this.questionConverter = questionConverter;
    }

    public void run() {
        var questionList = questionDao.getAll();
        if (questionList.isEmpty()) {
            ioService.outputString(EMPTY_QUESTIONS_MESSAGE);
            return;
        }
        ioService.outputString(START_MESSAGE);
        int rightAnswersCounter = doQuizAndGetResult(questionList);
        ioService.outputString(SEPARATOR_LINE);
        ioService.outputString(END_MESSAGE + rightAnswersCounter);
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
                    ioService.outputString("Incorrect input. Please, enter a number of answer");
                }
            }
        }
        return rightAnswersCounter;
    }
}
