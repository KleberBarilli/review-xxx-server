package com.idealizer.review_x.application.modules.series.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "series_episodes")
public class Episode {

    @Id
    private ObjectId id;

    private String name;
    private String synopsis;
    private Integer season;
    private Integer episode;
    @Field(value = "poster_url")
    private String posterUrl;
    @Field(value = "video_url")
    private String videoUrl;

    @Field(value = "serie_id")
    private ObjectId serieId;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public ObjectId getSerieId() {
        return serieId;
    }

    public void setSerieId(ObjectId serieId) {
        this.serieId = serieId;
    }
}
