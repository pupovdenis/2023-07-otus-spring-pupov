package ru.pupov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.pupov.config.AppProps;
import ru.pupov.dao.QuestionDao;
import ru.pupov.domain.Question;
import ru.pupov.domain.Student;
import ru.pupov.service.IOLFacade;
import ru.pupov.service.QuizService;
import ru.pupov.service.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    public static final int MIN_ANSWER_NUMBER = 1;

    private static final String SEPARATOR_LINE = "\r";

    private final QuestionDao questionDao;

    private final ConversionService conversionService;

    private final StudentService studentService;

    private final AppProps appProps;

    private final IOLFacade iolFacade;

    public void run() {
        var student = studentService.getStudent();
        var questionList = questionDao.getAll();
        if (questionList.isEmpty()) {
            iolFacade.outputString("empty.questions.message", true);
            return;
        }
        iolFacade.outputString("start.message", true, 1);
        int rightAnswersCounter = doQuizAndGetResult(questionList);
        outputFinish(student, rightAnswersCounter);
    }

    private void outputFinish(Student student, int rightAnswersCounter) {
        iolFacade.outputString(SEPARATOR_LINE, false);
        var studentOutputString = conversionService.convert(student, String.class);
        iolFacade.outputFormattedStringWithLocalization(
                "end.message.format", studentOutputString, rightAnswersCounter + "");
        if (rightAnswersCounter >= appProps.getPassingNumberOfCorrectAnswers()) {
            iolFacade.outputString("result.success.message", true);
            return;
        }
        iolFacade.outputString("result.fail.message", true);
    }

    private int doQuizAndGetResult(List<Question> questionList) {
        int rightAnswersCounter = 0;
        for (var question : questionList) {
            var questionOutputString = conversionService.convert(question, String.class);
            iolFacade.outputString(questionOutputString, false);
            var answerInt = iolFacade.readIntWithLocalizedPromptByInterval(
                    MIN_ANSWER_NUMBER, question.getAnswers().size(),
                    "enter.your.answer.message", "incorrect.input.message");
            if (question.getAnswers().get(answerInt - 1).isCorrectAnswer()) {
                rightAnswersCounter++;
            }
        }
        return rightAnswersCounter;
    }
}
