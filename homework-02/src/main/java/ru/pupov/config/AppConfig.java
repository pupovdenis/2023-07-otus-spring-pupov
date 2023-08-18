package ru.pupov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import ru.pupov.converter.QuestionConverter;
import ru.pupov.service.IOService;
import ru.pupov.service.impl.ConsoleIOService;

import java.util.Set;

@Configuration
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
}
