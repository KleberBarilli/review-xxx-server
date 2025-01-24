package com.idealizer.review_x.actors.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idealizer.review_x.Person;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "actors")
public class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @JsonProperty("person")
    private Person person;

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ActorEntity that = (ActorEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(person, that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person);
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

}