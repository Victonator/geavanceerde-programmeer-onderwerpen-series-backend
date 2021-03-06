package com.github.animeseries.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.animeseries.model.Series;
import com.github.animeseries.repository.SeriesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class SeriesControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeriesRepository seriesRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Series seriesStudio1Movie1 = new Series("CoMix Wave Films", "Your name", "drama", true, 1, 2016);
    private Series seriesStudio2Series1 = new Series("MAPPA", "Jujutsu Kaisen", "supernatural", false, 24, 1, 2020);
    private Series seriesStudio2Series2 = new Series("MAPPA", "The Idaten Deities Know Only Peace", "action", false, 11, 1, 2021);
    private Series seriesStudio3Series3 = new Series("Ufotable", "Demon Slayer: Kimetsu no Yaiba", "adventure", false, 26, 1, 2019);
    private Series seriesStudio3Series4 = new Series("Ufotable", "Demon Slayer: Kimetsu no Yaiba - Entertainment District Arc", "adventure", false, 5, 2021);

    @Test
    public void givenSeries_whenGetAllSeries_thenReturnJsonSeries() throws Exception {
        List<Series> seriesList = new ArrayList<>();
        seriesList.add(seriesStudio1Movie1);
        seriesList.add(seriesStudio2Series1);
        seriesList.add(seriesStudio2Series2);
        seriesList.add(seriesStudio3Series3);
        seriesList.add(seriesStudio3Series4);

        given(seriesRepository.findAll()).willReturn(seriesList);

        mockMvc.perform(get("/series"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    public void givenSeries_whenGetAllSeriesByIsMovie_thenReturnJsonSeries() throws Exception {

        List<Series> seriesTrueList = new ArrayList<>();
        List<Series> seriesFalseList = new ArrayList<>();
        seriesTrueList.add(seriesStudio1Movie1);
        seriesFalseList.add(seriesStudio2Series1);
        seriesFalseList.add(seriesStudio2Series2);
        seriesFalseList.add(seriesStudio3Series3);
        seriesFalseList.add(seriesStudio3Series4);

        given(seriesRepository.findSeriesByIsMovie(true)).willReturn(seriesTrueList);
        given(seriesRepository.findSeriesByIsMovie(false)).willReturn(seriesFalseList);

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

        List<Series> seriesSuList = new ArrayList<>();
        List<Series> seriesXList = new ArrayList<>();
        seriesSuList.add(seriesStudio2Series1);
        seriesSuList.add(seriesStudio3Series3);
        seriesSuList.add(seriesStudio3Series4);

        given(seriesRepository.findSeriesByNameContainingIgnoreCase("su")).willReturn(seriesSuList);
        given(seriesRepository.findSeriesByNameContainingIgnoreCase("x")).willReturn(seriesXList);

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
    public void givenSeries_whenSearchGetSeriesByExactName_thenReturnJsonSeries() throws Exception {

        given(seriesRepository.getSeriesByName("Your name")).willReturn(seriesStudio1Movie1);
        given(seriesRepository.getSeriesByName("Jujutsu Kaisen")).willReturn(seriesStudio2Series1);

        mockMvc.perform(get("/series/name/Your name"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Your name"));

        mockMvc.perform(get("/series/name/Jujutsu Kaisen"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jujutsu Kaisen"));
    }

    @Test
    public void givenSeries_whenSearchAllSeriesByNameAndIsMovie_thenReturnJsonSeries() throws Exception {
        List<Series> seriesMovieY = new ArrayList<>();
        List<Series> seriesSeriesY = new ArrayList<>();
        seriesMovieY.add(seriesStudio1Movie1);
        seriesSeriesY.add(seriesStudio2Series2);
        seriesSeriesY.add(seriesStudio3Series3);
        seriesSeriesY.add(seriesStudio3Series4);

        given(seriesRepository.findSeriesByNameContainingIgnoreCaseAndIsMovie("y", true)).willReturn(seriesMovieY);
        given(seriesRepository.findSeriesByNameContainingIgnoreCaseAndIsMovie("y", false)).willReturn(seriesSeriesY);

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

        List<Series> seriesStudio1List = new ArrayList<>();
        List<Series> seriesStudio3List = new ArrayList<>();
        seriesStudio1List.add(seriesStudio1Movie1);
        seriesStudio3List.add(seriesStudio3Series3);
        seriesStudio3List.add(seriesStudio3Series4);

        given(seriesRepository.findSeriesByStudioContainingIgnoreCase("comix wave films")).willReturn(seriesStudio1List);
        given(seriesRepository.findSeriesByStudioContainingIgnoreCase("ufotable")).willReturn(seriesStudio3List);

        mockMvc.perform(get("/series/studio/comix wave films"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].studio").value(containsStringIgnoringCase("comix wave films")));

        mockMvc.perform(get("/series/studio/ufotable"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].studio").value(containsStringIgnoringCase("ufotable")))
                .andExpect(jsonPath("$[1].studio").value(containsStringIgnoringCase("ufotable")));
    }

    @Test
    public void givenSeries_whenSearchAllSeriesByYear_thenReturnJsonSeries() throws Exception {

        List<Series> seriesAllYearList = new ArrayList<>();
        seriesAllYearList.add(seriesStudio1Movie1);
        seriesAllYearList.add(seriesStudio2Series1);
        seriesAllYearList.add(seriesStudio2Series2);
        seriesAllYearList.add(seriesStudio3Series3);
        seriesAllYearList.add(seriesStudio3Series4);

        List<Series> seriesLowerYearList = new ArrayList<>();
        seriesLowerYearList.add(seriesStudio2Series1);
        seriesLowerYearList.add(seriesStudio2Series2);
        seriesLowerYearList.add(seriesStudio3Series4);

        List<Series> seriesUpperYearList = new ArrayList<>();
        seriesUpperYearList.add(seriesStudio1Movie1);
        seriesUpperYearList.add(seriesStudio3Series3);

        List<Series> seriesUpperLowerYearList = new ArrayList<>();
        seriesUpperLowerYearList.add(seriesStudio2Series1);

        given(seriesRepository.findAll()).willReturn(seriesAllYearList);
        given(seriesRepository.findSeriesByYearAiredBetween(2019, 99999)).willReturn(seriesLowerYearList);
        given(seriesRepository.findSeriesByYearAiredBetween(0, 2020)).willReturn(seriesUpperYearList);
        given(seriesRepository.findSeriesByYearAiredBetween(2019, 2021)).willReturn(seriesUpperLowerYearList);

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
    public void givenSeries_whenGetAllSeriesByYear_thenReturnJsonSeries() throws Exception {

        List<Series> series2016 = new ArrayList<>();
        List<Series> series2021 = new ArrayList<>();
        series2016.add(seriesStudio1Movie1);
        series2021.add(seriesStudio2Series2);
        series2021.add(seriesStudio3Series4);

        given(seriesRepository.findSeriesByYearAired(2016)).willReturn(series2016);
        given(seriesRepository.findSeriesByYearAired(2021)).willReturn(series2021);

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
    public void givenSeries_whenGetAllSeriesByGenre_thenReturnJsonSeries() throws Exception {

        List<Series> seriesDrama = new ArrayList<>();
        List<Series> seriesAdventure = new ArrayList<>();
        seriesDrama.add(seriesStudio1Movie1);
        seriesAdventure.add(seriesStudio3Series3);
        seriesAdventure.add(seriesStudio3Series4);

        given(seriesRepository.findSeriesByGenre("drama")).willReturn(seriesDrama);
        given(seriesRepository.findSeriesByGenre("adventure")).willReturn(seriesAdventure);

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

        List<Series> seriesSupernatural = new ArrayList<>();
        List<Series> seriesAdventure = new ArrayList<>();
        seriesSupernatural.add(seriesStudio2Series1);
        seriesAdventure.add(seriesStudio3Series3);
        seriesAdventure.add(seriesStudio3Series4);

        given(seriesRepository.findSeriesByGenreContainingIgnoreCase("adv")).willReturn(seriesAdventure);
        given(seriesRepository.findSeriesByGenreContainingIgnoreCase("erna")).willReturn(seriesSupernatural);

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
        Series seriesStudio4Series5 = new Series("Gainax", "Neon Genesis Evangelion", "psychological drama", false, 26, 1, 1995);

        mockMvc.perform(post("/series")
                        .content(mapper.writeValueAsString(seriesStudio4Series5))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studio").value(containsStringIgnoringCase("gainax")))
                .andExpect(jsonPath("$.name").value("Neon Genesis Evangelion"))
                .andExpect(jsonPath("$.genre").value("psychological drama"))
                .andExpect(jsonPath("$.episodes").value(26))
                .andExpect(jsonPath("$.season").value(1))
                .andExpect(jsonPath("$.yearAired").value(1995));
    }

    @Test
    public void whenPutSeries_thenReturnJsonSeries() throws Exception {
        Series seriesStudio1Movie1Mod = new Series("CoMix Wave Films", "Your name", "romance", true, 1, 2016);

        given(seriesRepository.findSeriesById("000011112222333344445555")).willReturn(seriesStudio1Movie1);

        mockMvc.perform(put("/series/{id}", "000011112222333344445555")
                        .content(mapper.writeValueAsString(seriesStudio1Movie1Mod))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genre").value("romance"));


        Series seriesStudio2Series1Mod = new Series("MAPPA", "Jujutsu Kaisen", false, 25, 2020);

        given(seriesRepository.findSeriesById("555544443333222211110000")).willReturn(seriesStudio2Series1);

        mockMvc.perform(put("/series/{id}", "555544443333222211110000")
                        .content(mapper.writeValueAsString(seriesStudio2Series1Mod))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.episodes").value(25))
                .andExpect(jsonPath("$.genre").value(containsStringIgnoringCase("supernatural")))
                .andExpect(jsonPath("$.season").value(1));
    }

    @Test
    public void whenDeleteSeries_thenReturnJsonSeries() throws Exception {

        List<Series> seriesList = new ArrayList<>();
        seriesList.add(seriesStudio2Series1);
        seriesList.add(seriesStudio2Series2);
        seriesList.add(seriesStudio3Series3);
        seriesList.add(seriesStudio3Series4);

        given(seriesRepository.findAll()).willReturn(seriesList);

        mockMvc.perform(delete("/series/{id}", "000011112222333344445555"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/series"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }
}