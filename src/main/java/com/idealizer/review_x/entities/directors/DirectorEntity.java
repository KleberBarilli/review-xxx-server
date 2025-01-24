package com.idealizer.review_x.entities.directors;

import com.idealizer.review_x.entities.PersonAbstract;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "directors")
@Data
public class DirectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PersonAbstract person;

}
