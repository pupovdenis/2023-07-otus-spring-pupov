package ru.pupov.service.impl;

import ru.pupov.service.IOService;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleIOService implements IOService {

    private final Scanner userInput;
    private final PrintStream userOutput;

    public ConsoleIOService() {
        userInput = new Scanner(System.in);
        this.userOutput = System.out;
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
}
