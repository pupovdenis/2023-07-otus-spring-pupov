package ru.pupov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pupov.service.IOService;
import ru.pupov.service.impl.ConsoleIOService;

@Configuration
public class AppConfig {

    @Value("${csv-path}")
    private String csvPath;
    @Value("${passing-number-of-correct-answers}")
    private int passingNumberOfCorrectAnswers;

    @Bean
    public IOService ioService() {
        return new ConsoleIOService();
    }

    public String getCsvPath() {
        return csvPath;
    }

    public void setCsvPath(String csvPath) {
        this.csvPath = csvPath;
    }

    public int getPassingNumberOfCorrectAnswers() {
        return passingNumberOfCorrectAnswers;
    }

    public void setPassingNumberOfCorrectAnswers(int passingNumberOfCorrectAnswers) {
        this.passingNumberOfCorrectAnswers = passingNumberOfCorrectAnswers;
    }
}
