package com.github.animeseries;

import com.github.animeseries.repository.SeriesRepository;
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

}
