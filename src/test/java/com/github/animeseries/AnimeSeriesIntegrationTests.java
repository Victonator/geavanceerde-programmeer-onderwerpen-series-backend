package com.github.animeseries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.animeseries.model.Series;
import com.github.animeseries.repository.SeriesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AnimeSeriesApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SeriesRepository seriesRepository;

    private Series seriesStudio1Movie1 = new Series(1, "Your name", "drama", true, 1, 2016);
    private Series seriesStudio2Series1 = new Series(2, "Jujutsu Kaisen", "supernatural", false, 24, 1, 2020);
    private Series seriesStudio2Series2 = new Series(2, "The Idaten Deities Know Only Peace", "action", false, 11, 1, 2021);
    private Series seriesStudio3Series3 = new Series(3, "Demon Slayer: Kimetsu no Yaiba", "adventure", false, 26, 1, 2019);
    private Series seriesStudio3Series4 = new Series(3, "Demon Slayer: Kimetsu no Yaiba - Entertainment District Arc", "adventure", false, 5, 2021);

    @BeforeEach
    public void beforeAllTests() {
        seriesRepository.deleteAll();
        seriesRepository.save(seriesStudio1Movie1);
        seriesRepository.save(seriesStudio2Series1);
        seriesRepository.save(seriesStudio2Series2);
        seriesRepository.save(seriesStudio3Series3);
        seriesRepository.save(seriesStudio3Series4);
    }

    @AfterEach
    public void afterAllTests() {
        seriesRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenSeries_whenGetAllSeries_thenReturnJsonSeries() throws Exception {
        mockMvc.perform()
    }

}
