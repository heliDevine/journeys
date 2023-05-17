package journeys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import journeys.model.Journey;
import journeys.service.JourneyService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JourneyController.class)
public class JourneyControllerTest {

    @MockBean
    private JourneyService journeyService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itShouldReturnListOfJourneys() throws Exception{
        List<Journey> journeys;
        journeys = Arrays.asList(
                Journey.builder()
                        .id("1234")
                        .departureStationName("DepartStation")
                        .returnStationName("ReturnStation")
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

       when(journeyService.getAllJourneys(anyInt(), anyInt()))
               .thenReturn(journeysPage);
       mockMvc.perform(get("/journeys/?page=1&size=2"))
               .andExpect(status().isOk())
               .andDo(print());

    }

    @Test
    void itShouldReturn200andJourneyById() throws Exception {
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
                .andExpect(jsonPath("$.id").value((id)))
                .andExpect(jsonPath("$.departureStationName").value((journey.getDepartureStationName())))
                .andExpect(jsonPath("$.returnStationName").value((journey.getReturnStationName())))
                .andExpect(jsonPath("$.distance").value((journey.getDistance())))
                .andExpect(jsonPath("$.duration").value((journey.getDuration())))
                .andDo(print());

    }

    @Test
    void itShouldReturn404whenIdIsNotFound() throws Exception {

        when(journeyService.getById("123")).thenReturn(null);
        mockMvc.perform(get("/journeys/123"))
                .andExpect(status().isNotFound())
                .andDo(print());

    }
    @Test
    void itShouldReturn200andJourneyByDepartureStationName() throws Exception {

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

        when(journeyService.getJourneysByDepartureStation("Firswood")).thenReturn(Collections.singletonList(journeys.get(0)));
        mockMvc.perform(get("/journeys/station/{departureStationName}", departureStationName)).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andDo(print());
    }

    @Test
    void itShouldReturn404whenDepartureStationDoesntExist() throws Exception {

        when(journeyService.getJourneysByDepartureStation("London")).thenReturn(null);
        mockMvc.perform(get("/journeys/London"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
