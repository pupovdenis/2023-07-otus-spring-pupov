package ru.pupov.homework04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.pupov.homework04.config.AppProps;
import ru.pupov.homework04.dao.QuestionDao;
import ru.pupov.homework04.domain.Question;
import ru.pupov.homework04.domain.Student;
import ru.pupov.homework04.service.LocalizationIOService;
import ru.pupov.homework04.service.QuizService;
import ru.pupov.homework04.service.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private static final int MIN_ANSWER_NUMBER = 1;

    private static final String SEPARATOR_LINE = "\r";

    private final QuestionDao questionDao;

    private final ConversionService conversionService;

    private final StudentService studentService;

    private final AppProps appProps;

    private final LocalizationIOService localizationIOService;

    public void run(String firstName, String lastName) {
        var student = studentService.getStudent(firstName, lastName);
        var questionList = questionDao.getAll();
        if (questionList.isEmpty()) {
            localizationIOService.outputString("empty.questions.message", true);
            return;
        }
        localizationIOService.outputString("start.message", true, 1);
        int rightAnswersCounter = doQuizAndGetResult(questionList);
        outputFinish(student, rightAnswersCounter);
    }

    private void outputFinish(Student student, int rightAnswersCounter) {
        localizationIOService.outputString(SEPARATOR_LINE, false);
        var studentOutputString = conversionService.convert(student, String.class);
        localizationIOService.outputFormattedStringWithLocalization(
                "end.message.format", studentOutputString, rightAnswersCounter + "");
        if (rightAnswersCounter >= appProps.getPassingNumberOfCorrectAnswers()) {
            localizationIOService.outputString("result.success.message", true);
            return;
        }
        localizationIOService.outputString("result.fail.message", true);
    }

    private int doQuizAndGetResult(List<Question> questionList) {
        int rightAnswersCounter = 0;
        for (var question : questionList) {
            var questionOutputString = conversionService.convert(question, String.class);
            localizationIOService.outputString(questionOutputString, false);
            var answerInt = localizationIOService.readIntWithLocalizedPromptByInterval(
                    MIN_ANSWER_NUMBER, question.getAnswers().size(),
                    "enter.your.answer.message", "incorrect.input.message");
            if (question.getAnswers().get(answerInt - 1).isCorrectAnswer()) {
                rightAnswersCounter++;
            }
        }
        return rightAnswersCounter;
    }
}
