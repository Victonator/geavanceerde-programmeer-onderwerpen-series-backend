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
        if(seriesRepository.count()==0) {
            seriesRepository.save(new Series(1, "Your name", false, 12, "2012"));
        }
    }

    @GetMapping("/series/{id}")
    public Series getSeriesById(@PathVariable String id) {
        return seriesRepository.findSeriesById(id);
    }

    @GetMapping("/series/studio/{studioId}")
    public List<Series> getSeriesByStudioId(@PathVariable Integer studioId) {
        return seriesRepository.findSeriesByStudioId(studioId);
    }

    @GetMapping("/series")
    public List<Series> getSeries() {
        System.out.println("You requested all series...");
        List<Series> series = seriesRepository.findAll();
        System.out.println(series.size());
        if(series.size()>0) {
            Series serie = ((Series) series.get(0));
            System.out.println(serie.getId());
            System.out.println(serie.getName());
        }
        return series;
    }

    @PostMapping("/series")
    public Series addSeries(@RequestBody Series series) {
        seriesRepository.save(series);
        return series;
    }
}
