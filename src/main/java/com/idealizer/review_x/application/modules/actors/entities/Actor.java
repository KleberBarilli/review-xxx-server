package com.idealizer.review_x.application.modules.actors.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idealizer.review_x.application.Person;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "actors")
public class Actor {

    @Id
    private ObjectId id;

    @JsonProperty("person")
    private Person person;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Actor that = (Actor) o;
        return Objects.equals(id, that.id) && Objects.equals(person, that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person);
    }
}
