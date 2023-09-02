package ru.pupov.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.core.io.ClassPathResource;

import java.util.Locale;

@ConfigurationProperties(prefix = "application")
public class AppProps implements FileNameProvider, LocaleProvider {

    @Setter
    private Locale locale;

    @Setter
    private String fileName;

    @Setter
    @Getter
    private int passingNumberOfCorrectAnswers;

    @ConstructorBinding
    public AppProps(String locale, String fileName, int passingNumberOfCorrectAnswers) {
        this.locale = Locale.forLanguageTag(locale);
        this.fileName = fileName;
        this.passingNumberOfCorrectAnswers = passingNumberOfCorrectAnswers;
    }

    @Override
    public String getFileName() {
        var classloader = Thread.currentThread().getContextClassLoader();
        try {
            var idx = fileName.lastIndexOf('.');
            var actualPath = fileName.substring(0, idx) + "_" + locale + fileName.substring(idx);
            var classPathResource = new ClassPathResource(actualPath, classloader);
            return actualPath;
        } catch (Exception e) {
            return fileName;
        }
    }

    @Override
    public Locale getCurrent() {
        return locale;
    }
}
