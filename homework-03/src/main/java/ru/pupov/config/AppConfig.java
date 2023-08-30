package ru.pupov.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import ru.pupov.converter.QuestionConverter;
import ru.pupov.converter.StudentConverter;
import ru.pupov.service.IOService;
import ru.pupov.service.impl.IOServiceImpl;

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
