package ru.pupov.homework04.service;

public interface LocalizationIOService {

    void outputString(String str, boolean withLocalization);

    void outputString(String str, boolean withLocalization, int numOfEmptyLinesBefore);

    void outputFormattedStringWithLocalization(String key, Object... strings);

    int readIntWithLocalizedPromptByInterval(int from, int to, String message1, String message2);

    String readStringWithPrompt(String str, boolean withLocalization, boolean notNewLine);
}
