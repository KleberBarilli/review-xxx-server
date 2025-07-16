package com.idealizer.review_x.application.modules.series.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

@Document(collection = "series")
public class Serie {

    @Id
    private ObjectId id;

    private String title;

    @Field(value = "alternative_titles")
    private Set<String> alternativeTitles;

    private String synopsis;

    private Integer year;

    private ArrayList<String> genres;

    @Field(value = "poster_url")
    private String posterUrl;

    @Field(value = "actor_ids")
    private Set<ObjectId> actorIds;

    @Field(value = "director_ids")
    private Set<ObjectId> directorIds;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getAlternativeTitles() {
        return alternativeTitles;
    }

    public void setAlternativeTitles(Set<String> alternativeTitles) {
        this.alternativeTitles = alternativeTitles;
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

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Set<ObjectId> getActorIds() {
        return actorIds;
    }

    public void setActorIds(Set<ObjectId> actorIds) {
        this.actorIds = actorIds;
    }

    public Set<ObjectId> getDirectorIds() {
        return directorIds;
    }

    public void setDirectorIds(Set<ObjectId> directorIds) {
        this.directorIds = directorIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Serie that = (Serie) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(alternativeTitles, that.alternativeTitles) &&
                Objects.equals(synopsis, that.synopsis) &&
                Objects.equals(year, that.year) &&
                Objects.equals(genres, that.genres) &&
                Objects.equals(posterUrl, that.posterUrl) &&
                Objects.equals(actorIds, that.actorIds) &&
                Objects.equals(directorIds, that.directorIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, alternativeTitles, synopsis, year, genres, posterUrl, actorIds, directorIds);
    }
}
