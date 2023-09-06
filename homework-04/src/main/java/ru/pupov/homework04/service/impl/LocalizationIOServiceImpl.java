package ru.pupov.homework04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pupov.homework04.service.IOService;
import ru.pupov.homework04.service.LocalizationIOService;
import ru.pupov.homework04.service.LocalizationService;

@Service
@RequiredArgsConstructor
public class LocalizationIOServiceImpl implements LocalizationIOService {

    private final LocalizationService localizationService;

    private final IOService ioService;

    @Override
    public void outputString(String str, boolean withLocalization) {
        var message = withLocalization ? localizationService.getMessage(str) : str;
        ioService.outputString(message);
    }

    @Override
    public void outputString(String str, boolean withLocalization, int numOfEmptyLinesBefore) {
        var sb = new StringBuilder();
        if (numOfEmptyLinesBefore > 0) {
            sb.append("\n".repeat(numOfEmptyLinesBefore));
        }
        if (withLocalization) {
            sb.append(localizationService.getMessage(str));
            ioService.outputString(sb.toString());
            return;
        }
        ioService.outputString(sb.toString());
    }

    @Override
    public void outputFormattedStringWithLocalization(String key, Object... strings) {
        var message = localizationService.getMessage(key, strings);
        ioService.outputString(message);
    }

    @Override
    public int readIntWithLocalizedPromptByInterval(int from, int to, String askMessageKey, String errorMessageKey) {
        var enterYourAnswerMessage = localizationService.getMessage(askMessageKey);
        var incorrectInputMessage = localizationService.getMessage(errorMessageKey);
        return ioService.readIntWithPromptByInterval(from, to,
                enterYourAnswerMessage, incorrectInputMessage);
    }

    @Override
    public String readStringWithPrompt(String str, boolean withLocalization, boolean notNewLine) {
        var message = withLocalization ? localizationService.getMessage(str) : str;
        return ioService.readStringWithPrompt(message, true);
    }
}
