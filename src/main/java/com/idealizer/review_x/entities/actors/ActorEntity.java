package com.idealizer.review_x.entities.actors;

import com.idealizer.review_x.entities.PersonAbstract;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "actors")
@Data
public class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PersonAbstract person;

}
