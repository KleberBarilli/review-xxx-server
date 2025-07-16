package com.idealizer.review_x.application.modules.directors.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.idealizer.review_x.application.Person;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collation = "directors")
public class Director {

    @Id
    private ObjectId id;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Director that = (Director) o;
        return Objects.equals(id, that.id) && Objects.equals(person, that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person);
    }

    @JsonProperty("person")
    private Person person;

}