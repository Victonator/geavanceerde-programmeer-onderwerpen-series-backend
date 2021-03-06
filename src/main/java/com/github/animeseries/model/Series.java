package com.github.animeseries.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "series")
public class Series {
    @Id
    private String id;
    private String studio;
    private String name;
    private String genre;
    private Boolean isMovie;
    private Integer episodes;
    private Integer season;
    private Integer yearAired;

    public Series(){};

    public Series(String studio, String name, Boolean isMovie, Integer episodes, Integer yearAired) {
        this.studio = studio;
        this.name = name;
        this.isMovie = isMovie;
        this.episodes = episodes;
        this.yearAired = yearAired;
    }

    public Series(String studio, String name, String genre, Boolean isMovie, Integer episodes, Integer yearAired) {
        this.studio = studio;
        this.name = name;
        this.genre = genre;
        this.isMovie = isMovie;
        this.episodes = episodes;
        this.yearAired = yearAired;
    }

    public Series(String studio, String name, Boolean isMovie, Integer episodes, Integer season, Integer yearAired) {
        this.studio = studio;
        this.name = name;
        this.isMovie = isMovie;
        this.episodes = episodes;
        this.season = season;
        this.yearAired = yearAired;
    }

    public Series(String studio, String name, String genre, Boolean isMovie, Integer episodes, Integer season, Integer yearAired) {
        this.studio = studio;
        this.name = name;
        this.genre = genre;
        this.isMovie = isMovie;
        this.episodes = episodes;
        this.season = season;
        this.yearAired = yearAired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Boolean getMovie() {
        return isMovie;
    }

    public void setMovie(Boolean movie) {
        isMovie = movie;
    }

    public Integer getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Integer episodes) {
        this.episodes = episodes;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getYearAired() {
        return yearAired;
    }

    public void setYearAired(Integer yearAired) {
        this.yearAired = yearAired;
    }
}
