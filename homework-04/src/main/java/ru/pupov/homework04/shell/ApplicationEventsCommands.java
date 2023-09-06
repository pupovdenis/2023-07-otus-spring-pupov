package ru.pupov.homework04.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.pupov.homework04.service.QuizService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final QuizService quizService;

    private String firstName;

    private String lastName;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(help = "Give the fist name") String firstName,
                        @ShellOption(help = "Give the last name") String lastName) {
        if (firstName == null || lastName ==  null) {
            return "Please, enter firstname and lastname";
        }
        this.firstName = firstName;
        this.lastName = lastName;
        return "%s %s, you have successfully logged in".formatted(this.firstName, this.lastName);
    }

    @ShellMethod(value = "Start quiz command", key = {"start"})
    @ShellMethodAvailability(value = "isStartQuizCommandAvailable")
    public void startQuiz() {
        quizService.run(firstName, lastName);
    }

    private Availability isStartQuizCommandAvailable() {
        return firstName == null || lastName == null
                ? Availability.unavailable("need to login") : Availability.available();
    }
}
