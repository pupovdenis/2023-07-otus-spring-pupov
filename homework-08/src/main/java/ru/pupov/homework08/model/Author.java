package ru.pupov.homework08.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "author")
public class Author {

    @Id
    private String id;

    private String firstname;

    private String lastname;

    public Author(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author author)) {
            return false;
        }
        if (!id.equals(author.id)) {
            return false;
        }
        if (!firstname.equals(author.firstname)) {
            return false;
        }
        return lastname.equals(author.lastname);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstname.hashCode();
        result = 31 * result + lastname.hashCode();
        return result;
    }
}
