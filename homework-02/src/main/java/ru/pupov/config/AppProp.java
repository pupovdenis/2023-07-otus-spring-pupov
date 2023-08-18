package ru.pupov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class AppProp implements DataInfoProvider {

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
