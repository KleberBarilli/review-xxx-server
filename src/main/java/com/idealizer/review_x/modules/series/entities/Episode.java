package com.idealizer.review_x.modules.series.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "series_episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String synopsis;

    private Integer season;

    private Integer episode;

    @Column(name = "poster_url", nullable = true)
    private String posterUrl;

    @Column(name = "video_url")
    private String videoUrl;


    @ManyToOne()
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serie;
}
