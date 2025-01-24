package com.idealizer.review_x.series.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "series_episodes")
@Data
public class EpisodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private SerieEntity serie;
}
