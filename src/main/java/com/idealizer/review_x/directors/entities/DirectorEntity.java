package com.idealizer.review_x.directors.entities;

import com.idealizer.review_x.Person;
import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "directors")
public class DirectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DirectorEntity that = (DirectorEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(person, that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person);
    }

    @Embedded
    private Person person;

}