package ru.pupov.homework08.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import ru.pupov.homework08.converter.AuthorDtoListConverter;
import ru.pupov.homework08.converter.BookDtoListConverter;
import ru.pupov.homework08.converter.CommentDtoListConverter;
import ru.pupov.homework08.converter.GenreDtoListConverter;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ConversionServiceFactoryBean conversionService() {
        var conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(Set.of(
                new BookDtoListConverter(objectMapper()),
                new GenreDtoListConverter(objectMapper()),
                new AuthorDtoListConverter(objectMapper()),
                new CommentDtoListConverter(objectMapper())));
        return conversionServiceFactoryBean;
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
