package com.github.animeseries.controller;

import com.github.animeseries.model.Series;
import com.github.animeseries.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class SeriesController {

    @Autowired
    private SeriesRepository seriesRepository;

    @PostConstruct
    public void fillDB() {
        seriesRepository.deleteAll();

        seriesRepository.save(new Series("CoMix Wave Films", "Your name", "drama", true, 1, 2016));
        seriesRepository.save(new Series("MAPPA", "Jujutsu Kaisen", "supernatural", false, 24, 1, 2020));
        seriesRepository.save(new Series("MAPPA", "The Idaten Deities Know Only Peace", "action", false, 11, 1, 2021));
        seriesRepository.save(new Series("Ufotable", "Demon Slayer: Kimetsu no Yaiba", "adventure", false, 26, 1, 2019));
        seriesRepository.save(new Series("Ufotable", "Demon Slayer: Kimetsu no Yaiba - Entertainment District Arc", "adventure", false, 5, 2021));

    }

    @GetMapping("/series")
    @ResponseBody
    public List<Series> getSeriesByIsMovie(
            @RequestParam(required = false) Boolean isMovie,
            @RequestParam(required = false, name = "q") String nameQuery
    ) {
        int caseSelector = ((isMovie == null) ? 0 : 1) + ((nameQuery == null) ? 0 : 2);

        switch(caseSelector) {
            case 1: return seriesRepository.findSeriesByIsMovie(isMovie);
            case 2: return seriesRepository.findSeriesByNameContainingIgnoreCase(nameQuery);
            case 3: return seriesRepository.findSeriesByNameContainingIgnoreCaseAndIsMovie(nameQuery, isMovie);
            default: return seriesRepository.findAll();
        }
    }

    @GetMapping("/series/studio/{studioName}")
    public List<Series> getSeriesByStudioId(@PathVariable String studioName) {
        return seriesRepository.findSeriesByStudioContainingIgnoreCase(studioName);
    }

    @GetMapping("/series/year")
    @ResponseBody
    public List<Series> getSeriesByYearBetween(
            @RequestParam(required = false) Integer lowerBound,
            @RequestParam(required = false) Integer upperBound) {
        int caseSelector = ((lowerBound == null) ? 0 : 1) + ((upperBound == null) ? 0 : 2);

        switch (caseSelector) {
            case 1: return seriesRepository.findSeriesByYearAiredBetween(lowerBound, 99999);
            case 2: return seriesRepository.findSeriesByYearAiredBetween(0, upperBound);
            case 3: return seriesRepository.findSeriesByYearAiredBetween(lowerBound, upperBound);
            default: return seriesRepository.findAll();
        }
    }

    @GetMapping("/series/year/{year}")
    public List<Series> getSeriesByYear(@PathVariable Integer year) {
        return seriesRepository.findSeriesByYearAired(year);
    }

    @GetMapping("/series/genre/{genre}")
    public List<Series> getSeriesByGenre(@PathVariable String genre) {
        return seriesRepository.findSeriesByGenre(genre);
    }

    @GetMapping("/series/genre")
    @ResponseBody
    public List<Series> searchSeriesByGenre(@RequestParam(name = "q") String genreQuery) {
        return seriesRepository.findSeriesByGenreContainingIgnoreCase(genreQuery);
    }

    @PostMapping("/series")
    public Series addSeries(@RequestBody Series series) {
        seriesRepository.save(series);
        return series;
    }

    @PutMapping("/series/{id}")
    public Series modifySeries(@RequestBody Series series, @PathVariable String id) {
        Series oldSeries = seriesRepository.findSeriesById(id);

        oldSeries.setStudio(series.getStudio());
        oldSeries.setName(series.getName());
        oldSeries.setMovie(series.getMovie());
        oldSeries.setEpisodes(series.getEpisodes());
        oldSeries.setYearAired(series.getYearAired());

        if(series.getGenre() != null) oldSeries.setGenre(series.getGenre());
        if(series.getSeason() != null) oldSeries.setSeason(series.getSeason());

        seriesRepository.save(oldSeries);
        return oldSeries;
    }

    @DeleteMapping("/series/{id}")
    public void deleteSeries(@PathVariable String id) {
        seriesRepository.deleteById(id);
    }
}
