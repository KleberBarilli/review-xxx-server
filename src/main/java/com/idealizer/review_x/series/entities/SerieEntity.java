package com.idealizer.review_x.series.entities;

import com.idealizer.review_x.actors.entities.ActorEntity;
import com.idealizer.review_x.directors.entities.DirectorEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "series")

public class SerieEntity {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SerieEntity that = (SerieEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(synopsis, that.synopsis) && Objects.equals(year, that.year) && Objects.equals(genres, that.genres) && Objects.equals(posterUrl, that.posterUrl) && Objects.equals(directors, that.directors) && Objects.equals(actors, that.actors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, synopsis, year, genres, posterUrl, directors, actors);
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public Set<DirectorEntity> getDirectors() {
        return directors;
    }

    public void setDirectors(Set<DirectorEntity> directors) {
        this.directors = directors;
    }

    public Set<ActorEntity> getActors() {
        return actors;
    }

    public void setActors(Set<ActorEntity> actors) {
        this.actors = actors;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
