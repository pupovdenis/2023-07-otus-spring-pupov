package ru.pupov.homework09.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.pupov.homework09.dto.BookCreateUpdateDto;
import ru.pupov.homework09.dto.BookDto;
import ru.pupov.homework09.dto.BookUpdateDto;
import ru.pupov.homework09.entity.Author;
import ru.pupov.homework09.entity.Book;
import ru.pupov.homework09.entity.Genre;

import java.util.Collections;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collections.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "comments", expression = "java(Collections.emptyList())")
    Book toEntity(BookDto bookDto);

    @Mapping(source = "genreId", target = "genre", qualifiedByName = "getGenre")
    @Mapping(source = "authorId", target = "author", qualifiedByName = "getAuthor")
    @Mapping(target = "comments", expression = "java(Collections.emptyList())")
    Book toEntity(BookUpdateDto bookUpdateDto);

    @Named("getAuthor")
    static Author getAuthor(Long authorId) {
        return Author.builder()
                .id(authorId)
                .build();
    }

    @Named("getGenre")
    static Genre getGenre(Long genreId) {
        return Genre.builder()
                .id(genreId)
                .build();
    }

    @Mapping(source = "genreId", target = "genre", qualifiedByName = "getGenre")
    @Mapping(source = "authorId", target = "author", qualifiedByName = "getAuthor")
    @Mapping(target = "comments", expression = "java(Collections.emptyList())")
    Book toEntity(BookCreateUpdateDto bookCreateDto);
}
