package ru.pupov.homework07.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pupov.homework07.dto.BookDto;
import ru.pupov.homework07.entity.Book;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    private final CommentMapper commentMapper;

    public List<BookDto> toDtoList(List<Book> books) {
        return books.stream()
                .map(this::toDto)
                .toList();
    }

    public BookDto toDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor() == null ? null : authorMapper.toDto(book.getAuthor()))
                .genre(book.getGenre() == null ? null : genreMapper.toDto(book.getGenre()))
                .comments(book.getComments() == null ? null : commentMapper.toDtoList(book.getComments()))
                .build();
    }

    public List<BookDto> toDtoListWithoutAuthor(List<Book> books) {
        return books.stream()
                .map(this::toDtoWithoutAuthor)
                .toList();
    }

    public BookDto toDtoWithoutAuthor(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(null)
                .genre(book.getGenre() == null ? null : genreMapper.toDto(book.getGenre()))
                .comments(book.getComments() == null ? null : commentMapper.toDtoList(book.getComments()))
                .build();
    }

    public List<BookDto> toDtoListWithoutGenre(List<Book> books) {
        return books.stream()
                .map(this::toDtoWithoutGenre)
                .toList();
    }

    public BookDto toDtoWithoutGenre(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor() == null ? null : authorMapper.toDto(book.getAuthor()))
                .genre(null)
                .comments(book.getComments() == null ? null : commentMapper.toDtoList(book.getComments()))
                .build();
    }
}
