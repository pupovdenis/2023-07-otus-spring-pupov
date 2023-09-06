package ru.pupov.homework04.service.impl;

import ru.pupov.homework04.service.IOService;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IOServiceImpl implements IOService {

    private final Scanner userInput;

    private final PrintStream userOutput;

    public IOServiceImpl(PrintStream ps, InputStream is) {
        userInput = new Scanner(is);
        this.userOutput = ps;
    }

    @Override
    public void outputString(String s) {
        userOutput.println(s);
    }

    @Override
    public void outputString(String s, boolean notNewLine) {
        if (notNewLine) {
            userOutput.print(s);
        } else {
            outputString(s);
        }
    }

    @Override
    public int readIntWithPrompt(String prompt) {
        outputString(prompt, true);
        return Integer.parseInt(userInput.nextLine());
    }

    @Override
    public String readStringWithPrompt(String prompt, boolean notNewLine) {
        outputString(prompt, notNewLine);
        return userInput.nextLine();
    }

    @Override
    public int readIntWithPromptByInterval(int min, int max, String prompt, String errorMessage) {
        while (true) {
            try {
                var answerInt = readIntWithPrompt(prompt);
                if (answerInt > max || answerInt < min) {
                    throw new NumberFormatException();
                }
                return answerInt;
            } catch (NumberFormatException nfe) {
                outputString(errorMessage);
            }
        }
    }
}
