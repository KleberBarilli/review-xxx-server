package com.idealizer.review_x.series.entities;

import com.idealizer.review_x.entities.actors.ActorEntity;
import com.idealizer.review_x.entities.directors.DirectorEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name = "series")
@Data
public class SerieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String synopsis;

    private Integer year;

    private ArrayList<String> genres;

    @Column(name = "poster_url")
    private String posterUrl;

    @ManyToMany
    @JoinTable(
            name = "series_directors",
            joinColumns = @JoinColumn(name = "serie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private Set<DirectorEntity> directors;

    @ManyToMany
    @JoinTable(
            name = "series_actors",
            joinColumns = @JoinColumn(name = "serie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<ActorEntity> actors;

}
