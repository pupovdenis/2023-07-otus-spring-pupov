package ru.pupov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import ru.pupov.converter.QuestionConverter;
import ru.pupov.service.IOService;
import ru.pupov.service.LocalizationService;
import ru.pupov.service.impl.ConsoleIOService;
import ru.pupov.service.impl.LocalizationServiceImpl;

import java.util.Set;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class AppConfig {

    @Bean
    public IOService ioService() {
        return new ConsoleIOService();
    }

    @Bean
    public ConversionServiceFactoryBean conversionService() {
        var conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(Set.of(new QuestionConverter()));
        return conversionServiceFactoryBean;
    }

    @Bean
    public LocaleProvider localeProvider(@Value("${application.locale}") String locale) {
        return new DefaultLocaleProvider(locale);
    }

    @Bean
    public LocalizationService localizationService(LocaleProvider localeProvider, MessageSource messageSource) {
        return new LocalizationServiceImpl(localeProvider, messageSource);
    }
}
