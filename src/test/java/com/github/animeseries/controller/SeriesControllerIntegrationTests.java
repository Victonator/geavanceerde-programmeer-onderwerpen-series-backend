package com.github.animeseries.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.animeseries.model.Series;
import com.github.animeseries.repository.SeriesRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

@SpringBootTest
@AutoConfigureMockMvc
class SeriesControllerIntegrationTests {

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
        mockMvc.perform(get("/series"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    public void givenSeries_whenGetAllSeriesByIsMovie_thenReturnJsonSeries() throws Exception {
        mockMvc.perform(get("/series?isMovie=true"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].movie").value(true));

        mockMvc.perform(get("/series?isMovie=false"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].movie").value(false));
    }

    @Test
    public void givenSeries_whenSearchAllSeriesByName_thenReturnJsonSeries() throws Exception {
        mockMvc.perform(get("/series?q=su"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value(containsStringIgnoringCase("su")))
                .andExpect(jsonPath("$[1].name").value(containsStringIgnoringCase("su")))
                .andExpect(jsonPath("$[2].name").value(containsStringIgnoringCase("su")));

        mockMvc.perform(get("/series?q=x"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void givenSeries_whenSearchAllSeriesByNameAndIsMovie_thenReturnJsonSeries() throws Exception {
        mockMvc.perform(get("/series?q=y&isMovie=true"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].movie").value(true))
                .andExpect(jsonPath("$[0].name").value(containsStringIgnoringCase("y")));

        mockMvc.perform(get("/series?q=y&isMovie=false"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].movie").value(false))
                .andExpect(jsonPath("$[0].name").value(containsStringIgnoringCase("y")))
                .andExpect(jsonPath("$[1].movie").value(false))
                .andExpect(jsonPath("$[1].name").value(containsStringIgnoringCase("y")))
                .andExpect(jsonPath("$[2].movie").value(false))
                .andExpect(jsonPath("$[2].name").value(containsStringIgnoringCase("y")));
    }

    @Test
    public void givenSeries_whenGetAllSeriesByStudio_thenReturnJsonSeries() throws Exception {
        mockMvc.perform(get("/series/studio/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].studioId").value(1));

        mockMvc.perform(get("/series/studio/3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].studioId").value(3))
                .andExpect(jsonPath("$[1].studioId").value(3));
    }

    @Test
    public void givenSeries_whenGetAllSeriesByYear_thenReturnJsonSeries() throws Exception {
        mockMvc.perform(get("/series/year/2016"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].yearAired").value(2016));

        mockMvc.perform(get("/series/year/2021"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].yearAired").value(2021))
                .andExpect(jsonPath("$[1].yearAired").value(2021));
    }

    @Test
    public void givenSeries_whenSearchAllSeriesByYear_thenReturnJsonSeries() throws Exception {
        mockMvc.perform(get("/series/year"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

        mockMvc.perform(get("/series/year?lowerBound=2019"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        mockMvc.perform(get("/series/year?upperBound=2020"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(get("/series/year?lowerBound=2019&upperBound=2021"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void givenSeries_whenGetAllSeriesByGenre_thenReturnJsonSeries() throws Exception {
        mockMvc.perform(get("/series/genre/adventure"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].genre").value(containsStringIgnoringCase("adventure")))
                .andExpect(jsonPath("$[1].genre").value(containsStringIgnoringCase("adventure")));

        mockMvc.perform(get("/series/genre/drama"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].genre").value(containsStringIgnoringCase("drama")));
    }

    @Test
    public void givenSeries_whenSearchAllSeriesByGenre_thenReturnJsonSeries() throws Exception {
        mockMvc.perform(get("/series/genre?q=adv"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].genre").value(containsStringIgnoringCase("adv")))
                .andExpect(jsonPath("$[1].genre").value(containsStringIgnoringCase("adv")));

        mockMvc.perform(get("/series/genre?q=erna"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].genre").value(containsStringIgnoringCase("erna")));
    }

    @Test
    public void whenPostSeries_thenReturnJsonSeries() throws Exception {
        Series seriesStudio4Series5 = new Series(4, "Neon Genesis Evangelion", "psychological drama", false, 26, 1, 1995);

        mockMvc.perform(post("/series")
                .content(mapper.writeValueAsString(seriesStudio4Series5))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.studioId").value(4))
                .andExpect(jsonPath("$.name").value("Neon Genesis Evangelion"))
                .andExpect(jsonPath("$.genre").value("psychological drama"))
                .andExpect(jsonPath("$.episodes").value(26))
                .andExpect(jsonPath("$.season").value(1))
                .andExpect(jsonPath("$.yearAired").value(1995));
    }

    @Test
    public void whenPutSeries_thenReturnJsonSeries() throws Exception {
        Series seriesStudio1Movie1 = new Series(1, "Your name", "romance", true, 1, 2016);

        MvcResult result = mockMvc.perform(get("/series?isMovie=true")).andReturn();
        String seriesId = JsonPath.read(result.getResponse().getContentAsString(), "$[0].id");

        mockMvc.perform(put("/series/{id}", seriesId)
                .content(mapper.writeValueAsString(seriesStudio1Movie1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genre").value("romance"));
    }

    @Test
    public void whenDeleteSeries_thenReturnJsonSeries() throws Exception {
        MvcResult result = mockMvc.perform(get("/series")).andReturn();
        String seriesId = JsonPath.read(result.getResponse().getContentAsString(), "$[2].id");

        mockMvc.perform(delete("/series/{id}", seriesId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/series"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

}
