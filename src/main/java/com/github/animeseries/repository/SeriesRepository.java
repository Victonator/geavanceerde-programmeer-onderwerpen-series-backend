package com.github.animeseries.repository;

import com.github.animeseries.model.Series;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends MongoRepository<Series, String> {
    Series findSeriesById(String id);
    List<Series> findSeriesByStudioId(Integer studioId);
    List<Series> findSeriesByIsMovie(Boolean isMovie);
    List<Series> findSeriesByGenre(String genre);
    List<Series> findSeriesByGenreContainingIgnoreCase(String genreQuery);
    List<Series> findSeriesByYearAired(Integer yearAired);
    List<Series> findSeriesByYearAiredBetween(Integer lowerBound, Integer upperBound);
    List<Series> findSeriesByNameContainingIgnoreCase(String nameQuery);
    List<Series> findSeriesByNameContainingIgnoreCaseAndIsMovie(String nameQuery, Boolean isMovie);
}
