package ru.pupov.homework08.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.pupov.homework08.model.Author;
import ru.pupov.homework08.model.Book;
import ru.pupov.homework08.model.Comment;
import ru.pupov.homework08.model.Genre;
import ru.pupov.homework08.repository.AuthorRepository;
import ru.pupov.homework08.repository.BookRepository;
import ru.pupov.homework08.repository.GenreRepository;

import java.util.List;

@ChangeLog
public class InitChangelog {

    private Author author1;

    private Author author2;

    private Author author3;

    private Author author4;

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    private Genre genre4;

    private Genre genre5;

    @ChangeSet(order = "001", id = "dropDb", author = "dpupov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "dpupov")
    public void initAuthors(AuthorRepository repository) {
        author1 = repository.save(new Author("1", "Лев", "Толстой"));
        author2 = repository.save(new Author("2", "Фёдор", "Достоевский"));
        author3 = repository.save(new Author("3", "Александр", "Пушкин"));
        author4 = repository.save(new Author("4", "Николай", "Гоголь"));
    }

    @ChangeSet(order = "003", id = "initGenres", author = "dpupov")
    public void initGenres(GenreRepository repository) {
        genre1 = repository.save(new Genre("1", "Роман"));
        genre2 = repository.save(new Genre("2", "Сказка"));
        genre3 = repository.save(new Genre("3", "Поэма"));
        genre4 = repository.save(new Genre("4", "Комедия"));
        genre5 = repository.save(new Genre("5", "Повесть"));
    }

    @ChangeSet(order = "004", id = "initBooks", author = "dpupov")
    public void initBooks(BookRepository repository) {
        var comment1 = new Comment("1", "Отличное чтиво. Дольше Санта-Барбары");
        var comment2 = new Comment("2", "Вот к чему приводят женские капризы");
        var comment3 = new Comment("3", "Бедняжка, так и не нашла счастье");
        var comment4 = new Comment("4", "Жесткий чувак этот Раскольников");
        var comment5 = new Comment("5", "Достоевский ерунды не напишет");
        var comment6 = new Comment("6", "Я б на месте бабки с револьвером ходил");
        var comment7 = new Comment("7", "Уснул на первой странице");

        var books = List.of(
                getBookToSaving("1", author1, genre1,"Война и мир", comment1),
                getBookToSaving("2", author1, genre1,"Анна Каренина", comment2, comment3),
                getBookToSaving("3", author2, genre1,"Преступление и наказание",
                        comment4, comment5, comment6, comment7),
                getBookToSaving("4", author3, genre2,"Сказка о царе Салтане"),
                getBookToSaving("5", author3, genre1,"Евгений Онегин"),
                getBookToSaving("6", author3, genre1,"Капитанская дочка"),
                getBookToSaving("7", author3, genre3,"Кавказский пленник"),
                getBookToSaving("8", author4, genre4,"Ревизор"),
                getBookToSaving("9", author4, genre5,"Записки сумасшедшего")
        );
        repository.saveAll(books);
    }

    private Book getBookToSaving(String id, Author author, Genre genre,
                                 String bookName, Comment... comments) {
        return Book.builder()
                .id(id)
                .author(author)
                .genre(genre)
                .name(bookName)
                .comments(List.of(comments))
                .build();
    }
}
