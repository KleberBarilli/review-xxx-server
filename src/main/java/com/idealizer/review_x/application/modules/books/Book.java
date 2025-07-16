package com.idealizer.review_x.application.modules.books;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Document(collection = "books")
public class Book {

    @Id
    private ObjectId id;

    private String isbn;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate publicationAt;


    @Field(value = "author_id")
    private ObjectId authorId;

    private Set<BookGenre> genres;

    @Field(value = "alternative_titles")
    private String alternativeTitles;



    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublicationAt() {
        return publicationAt;
    }

    public void setPublicationAt(LocalDate publicationAt) {
        this.publicationAt = publicationAt;
    }

    public ObjectId getAuthorId() {
        return authorId;
    }

    public void setAuthorId(ObjectId authorId) {
        this.authorId = authorId;
    }

    public Set<BookGenre> getGenres() {
        return genres;
    }

    public void setGenres(Set<BookGenre> genres) {
        this.genres = genres;
    }

    public String getAlternativeTitles() {
        return alternativeTitles;
    }

    public void setAlternativeTitles(String alternativeTitles) {
        this.alternativeTitles = alternativeTitles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book that = (Book) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(isbn, that.isbn) &&
                Objects.equals(title, that.title) &&
                Objects.equals(publicationAt, that.publicationAt) &&
                Objects.equals(authorId, that.authorId) &&
                Objects.equals(genres, that.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, title, publicationAt, authorId, genres);
    }
}
