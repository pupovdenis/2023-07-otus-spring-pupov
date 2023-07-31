package ru.pupov.service;

public interface IOService {

    void outputString(String s);

    void outputString(String s, boolean notNewLine);

    int readIntWithPrompt(String prompt);
}
