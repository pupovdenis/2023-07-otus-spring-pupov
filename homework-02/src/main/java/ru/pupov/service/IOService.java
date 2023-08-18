package ru.pupov.service;

public interface IOService {

    void outputString(String s);

    void outputString(String s, boolean notNewLine);

    int readIntWithPrompt(String prompt);

    String readStringWithPrompt(String prompt, boolean notNewLine);

    int readIntWithPromptByInterval(int min, int max, String prompt, String errorMessage);
}
