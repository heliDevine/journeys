package journeys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import journeys.model.Station;
import journeys.service.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest (StationController.class )
class StationControllerTest {

    @MockBean
    private StationService stationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itShouldReturnAllStationsPaginated() throws Exception {
        List<Station> stations;
        stations = Arrays.asList(
                Station.builder()
                        .id("123")
                        .stationID(1)
                        .stationNameEN("Firswood")
                        .totalReturnedJourneys(3)
                        .totalDepartingJourneys(2)
                        .totalJourneyDistanceFromStation(500)
                        .build(),
                Station.builder()
                        .id("456")
                        .stationID(1)
                        .stationNameEN("Chorlton")
                        .totalReturnedJourneys(3)
                        .totalDepartingJourneys(2)
                        .totalJourneyDistanceFromStation(1500)
                        .build()
        );
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("stationNameEN").ascending());
        Page<Station> stationPage = new PageImpl<>(stations, pageRequest, stations.size());

        when(stationService.getAllStations(Pageable.ofSize((2)))).thenReturn(stationPage);
        mockMvc.perform(get("/stations/?page=1&size=2&sortBy=stationNameEN"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void itShouldReturn200andStationById() throws Exception {
        String id = "123";
        Station station = Station.builder()
                .id(id)
                .stationNameEN("Piccadilly Circus")
                .build();

        when(stationService.getStationByID("123")).thenReturn(station );
        mockMvc.perform(get("/stations/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.stationNameEN")
                        .value(station.getStationNameEN()))
                .andDo(print());
    }

    @Test
    void itShouldReturn404whenIdIsNotFound() throws Exception {

        when(stationService.getStationByID("123")).thenReturn(null);
        mockMvc.perform(get("/stations/123"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void itShouldReturn200andStationByName() throws Exception {

        String stationName = "Old Trafford";
        Station station = Station.builder()
                        .id("testId")
                        .stationNameEN(stationName)
                        .build();

        when(stationService.getStationsByNameEN("Old Trafford")).thenReturn(station);
        mockMvc.perform(get("/stations/stationName/{stationName}", stationName)).andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNameEN")
                        .value(station.getStationNameEN()))
                .andDo(print());
    }

    @Test
    void itShouldReturn404whenStationDoesntExist() throws Exception {

        when(stationService.getStationsByNameEN("London")).thenReturn(null);
        mockMvc.perform(get("/stations/London"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}