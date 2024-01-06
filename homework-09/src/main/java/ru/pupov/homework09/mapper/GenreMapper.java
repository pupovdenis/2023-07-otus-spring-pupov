package ru.pupov.homework09.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.pupov.homework09.dto.GenreCreateDto;
import ru.pupov.homework09.dto.GenreDto;
import ru.pupov.homework09.entity.Genre;

import java.util.Collections;

@Mapper(componentModel = "spring",
        imports = {Collections.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GenreMapper {
    GenreDto toDto(Genre genre);

    @Mapping(target = "books", expression = "java(Collections.emptyList())")
    Genre toEntity(GenreDto genreDto);

    @Mapping(target = "books", expression = "java(Collections.emptyList())")
    Genre toEntity(GenreCreateDto genreCreateDto);
}
