package ru.pupov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.pupov.config.AppProps;
import ru.pupov.dao.QuestionDao;
import ru.pupov.domain.Question;
import ru.pupov.domain.Student;
import ru.pupov.service.IOService;
import ru.pupov.service.LocalizationService;
import ru.pupov.service.QuizService;
import ru.pupov.service.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    public static final int MIN_ANSWER_NUMBER = 1;

    private static final String SEPARATOR_LINE = "\r";

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final ConversionService conversionService;

    private final StudentService studentService;

    private final AppProps appProps;

    private final LocalizationService localizationService;

    public void run() {
        var student = studentService.getStudent();
        var questionList = questionDao.getAll();
        if (questionList.isEmpty()) {
            var emptyQuestionsMessage = localizationService.getMessage("empty.questions.message");
            ioService.outputString(emptyQuestionsMessage);
            return;
        }
        var startMessage = "\n" + localizationService.getMessage("start.message");
        ioService.outputString(startMessage);
        int rightAnswersCounter = doQuizAndGetResult(questionList);
        outputFinish(student, rightAnswersCounter);
    }

    private void outputFinish(Student student, int rightAnswersCounter) {
        ioService.outputString(SEPARATOR_LINE);
        var endMessageFormat = localizationService.getMessage("end.message.format");
        var fullEndMessageFormat = endMessageFormat.formatted(student.toString(), rightAnswersCounter + "");
        ioService.outputString(fullEndMessageFormat);
        if (rightAnswersCounter >= appProps.getPassingNumberOfCorrectAnswers()) {
            var resultSuccessMessage = localizationService.getMessage("result.success.message") + "\n";
            ioService.outputString(resultSuccessMessage);
            return;
        }
        var resultFailMessage = localizationService.getMessage("result.fail.message") + "\n";
        ioService.outputString(resultFailMessage);
    }

    private int doQuizAndGetResult(List<Question> questionList) {
        int rightAnswersCounter = 0;
        for (var question : questionList) {
            var questionOutputString = conversionService.convert(question, String.class);
            ioService.outputString(questionOutputString, true);
            var enterYourAnswerMessage = localizationService.getMessage("enter.your.answer.message") + " ";
            var incorrectInputMessage = localizationService.getMessage("incorrect.input.message");
            var answerInt = ioService.readIntWithPromptByInterval(MIN_ANSWER_NUMBER, question.getAnswers().size(),
                    enterYourAnswerMessage, incorrectInputMessage);
            if (question.getAnswers().get(answerInt - 1).isCorrectAnswer()) {
                rightAnswersCounter++;
            }
        }
        return rightAnswersCounter;
    }
}
