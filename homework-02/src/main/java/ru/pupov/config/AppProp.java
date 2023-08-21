package ru.pupov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:application.properties")
public class AppProp implements FileNameProvider {

    @Value("${csv-path}")
    private String dataResourcePath;

    @Value("${passing-number-of-correct-answers}")
    private int passingNumberOfCorrectAnswers;

    @Override
    public String getDataResourcePath() {
        return dataResourcePath;
    }

    public int getPassingNumberOfCorrectAnswers() {
        return passingNumberOfCorrectAnswers;
    }
}
