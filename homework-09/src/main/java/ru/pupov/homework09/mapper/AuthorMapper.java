package ru.pupov.homework09.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.pupov.homework09.dto.AuthorCreateDto;
import ru.pupov.homework09.dto.AuthorDto;
import ru.pupov.homework09.entity.Author;

import java.util.Collections;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collections.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthorMapper {
    AuthorDto toDto(Author author);

    @Mapping(target = "books", expression = "java(Collections.emptyList())")
    Author toEntity(AuthorDto authorDto);

    @Mapping(target = "books", expression = "java(Collections.emptyList())")
    Author toEntity(AuthorCreateDto authorCreateDto);
}
