package ru.pupov.homework04.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import ru.pupov.homework04.converter.QuestionConverter;
import ru.pupov.homework04.converter.StudentConverter;
import ru.pupov.homework04.service.IOService;
import ru.pupov.homework04.service.impl.IOServiceImpl;

import java.util.Set;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class AppConfig {

    @Bean
    public IOService ioService() {
        return new IOServiceImpl(System.out, System.in);
    }

    @Bean
    public ConversionServiceFactoryBean conversionService() {
        var conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(Set.of(
                new QuestionConverter(),
                new StudentConverter()));
        return conversionServiceFactoryBean;
    }
}
