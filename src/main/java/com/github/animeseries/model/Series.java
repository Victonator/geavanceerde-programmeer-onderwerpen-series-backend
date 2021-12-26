package com.github.animeseries.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "series")
public class Series {
    @Id
    private String id;
    private Integer studioId;
    private String name;
    private Boolean isMovie;
    private Integer episodes;
    private String dateAired;

    public Series(Integer studioId, String name, Boolean isMovie, Integer episodes, String dateAired) {
        this.studioId = studioId;
        this.name = name;
        this.isMovie = isMovie;
        this.episodes = episodes;
        this.dateAired = dateAired;
    }

    public String getId() {
        return id;
    }

    public Integer getStudioId() {
        return studioId;
    }

    public void setStudioId(Integer studioId) {
        this.studioId = studioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Integer episodes) {
        this.episodes = episodes;
    }

    public String getDateAired() {
        return dateAired;
    }

    public void setDateAired(String dateAired) {
        this.dateAired = dateAired;
    }
}
