package journeys.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import journeys.model.Journey;
import journeys.model.JourneyRequestDTO;
import journeys.service.JourneyService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JourneyController.class)
class JourneyControllerTest {

    @MockBean
    private JourneyService journeyService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itReturnsAllJourneysPaginated() throws Exception {
        List<Journey> journeys;
        journeys = Arrays.asList(
                Journey.builder()
                        .id("1234")
                        .departureStationName("Firswood")
                        .returnStationName("Chorlton")
                        .distance(1000)
                        .duration(90)
                        .build(),
                Journey.builder()
                        .id("5678")
                        .departureStationName("Manchester")
                        .returnStationName("Helsinki")
                        .distance(2000)
                        .duration(120)
                        .build()
        );
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Journey> journeysPage = new PageImpl<>(journeys, pageRequest, journeys.size());

        when(journeyService.getAllJourneys((anyInt()), anyInt())).thenReturn(journeysPage);
        mockMvc.perform(get("/journeys/?page=1&size=2"))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].departureStationName" ).value("Firswood"))
                .andExpect(jsonPath("$.content[0].returnStationName" ).value("Chorlton"))
                .andExpect(jsonPath("$.content[1].departureStationName").value("Manchester"))
                .andExpect(jsonPath("$.content[1].returnStationName").value("Helsinki"))
                .andExpect(status().isOk());
    }

    @Test
    void itReturns200andJourneyById() throws Exception {
        String id = "123";
        Journey journey = Journey.builder()
                .id(id)
                .departureStationName("Firswood")
                .returnStationName("Market Street")
                .distance(5000)
                .duration(4000)
                .build();

        when(journeyService.getById("123")).thenReturn(journey);
        mockMvc.perform(get("/journeys/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.departureStationName").value((journey.getDepartureStationName())))
                .andExpect(jsonPath("$.returnStationName").value((journey.getReturnStationName())))
                .andExpect(jsonPath("$.distance").value((journey.getDistance())))
                .andExpect(jsonPath("$.duration").value((journey.getDuration())));
    }

    @Test
    void itReturns404whenIdIsNotFound() throws Exception {

        when(journeyService.getById("123")).thenReturn(null);
        mockMvc.perform(get("/journeys/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void itReturns200andJourneysByDepartureStationName() throws Exception {

        String departureStationName = "Firswood";

        List<Journey> journeys;
        journeys = Arrays.asList(
                Journey.builder()
                        .id("1234")
                        .departureStationName(departureStationName)
                        .returnStationName("Market Street")
                        .distance(1000)
                        .duration(90)
                        .build(),
                Journey.builder()
                        .id("5678")
                        .departureStationName("Manchester")
                        .returnStationName("Helsinki")
                        .distance(2000)
                        .duration(120)
                        .build()
        );

        when(journeyService.getJourneysByDepartureStation(departureStationName))
                .thenReturn(Collections.singletonList(journeys.get(0)));
        mockMvc.perform(get("/journeys/departureStation/{departureStationName}", departureStationName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].returnStationName").value("Market Street"));
    }

    @Test
    void itReturns404IfNoJourneysFound() throws Exception {

        String departureStationName = "London";

        when(journeyService.getJourneysByDepartureStation("London")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/journeys/{departureStationName}", departureStationName))
                .andExpect(status().isNotFound());
    }

    @Test
    void itReturns201andCreatesNewJourney() throws Exception {

        JourneyRequestDTO journeyInput = JourneyRequestDTO.builder()
                .departureStationName("Firswood")
                .returnStationName("Market Street")
                .distance(5000)
                .duration(4000)
                .build();

        Journey createdJourney = Journey.builder()
                .id("123")
                .departureStationName("Firswood")
                .departureStationId(1)
                .returnStationName("Market Street")
                .returnStationId(2)
                .distance(5000)
                .duration(4000)
                .build();

        when(journeyService.createJourney(any(Journey.class))).thenReturn(createdJourney);

        mockMvc.perform(post("/journeys/journey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(journeyInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.departureStationName").value("Firswood"))
                .andExpect(jsonPath("$.departureStationId").value(1))
                .andExpect(jsonPath("$.returnStationName").value("Market Street"))
                .andExpect(jsonPath("$.returnStationId").value(2))
                .andExpect(jsonPath("$.distance").value(5000))
                .andExpect(jsonPath("$.duration").value(4000));

        verify(journeyService, times(1)).createJourney(any(Journey.class));
    }

    @Test
    void itReturns400ifDistanceIsUnder10MetresAndSendsErrorMessage() throws Exception {

        JourneyRequestDTO journeyInput = JourneyRequestDTO.builder()
                .departureStationName("Firswood")
                .returnStationName("Market Street")
                .distance(9)
                .duration(4000)
                .build();

        when(journeyService.createJourney(any(Journey.class))).thenReturn(null);

        mockMvc.perform(post("/journeys/journey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(journeyInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void itReturns400ifDurationIsUnder10SecondsAndSendsErrorMessage() throws Exception {

        JourneyRequestDTO journeyInput = JourneyRequestDTO.builder()
                .departureStationName("Firswood")
                .returnStationName("Market Street")
                .distance(1000)
                .duration(9)
                .build();

        String errorMessage = "Distance needs to be longer than " +
                "10 metres and duration needs to be more than 10 seconds";

        when(journeyService.createJourney(any(Journey.class))).thenReturn(null);

        mockMvc.perform(post("/journeys/journey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(journeyInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}

