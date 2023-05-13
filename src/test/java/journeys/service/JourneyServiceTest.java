package journeys.service;

import journeys.model.Journey;
import journeys.repository.JourneyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JourneyServiceTest {

    @Mock
    private JourneyRepository journeyRepository;

    @InjectMocks
    private JourneyService journeyService;


    @Test
    void itFetchesAllJourneys() {

        List<Journey> journeys = createJourneys();

        when(journeyRepository.findAllJourneys()).thenReturn(journeys);

        List<Journey> actualJourneys = journeyService.getAllJourneys();
        assertThat(actualJourneys).hasSize(2);
    }

    @Test
    void itFetchesAllJourneysByDepartureStationName() {
        List<Journey> journeys = createJourneys();

        when(journeyRepository.findByDepartureStationName("Manchester"))
                .thenReturn(Collections.singletonList(journeys.get(1)));

        List<Journey> actualJourneys = journeyService.getJourneysByDepartureStation("Manchester");
        assertThat(actualJourneys).hasSize(1);
    }
    @Test
    void itDoesntReturnNonExistingStation() {
        List<Journey> journeys = createJourneys();

        when(journeyRepository.findByDepartureStationName("London"))
                .thenReturn(Collections.emptyList());

        List<Journey> actualJourneys = journeyService.getJourneysByDepartureStation("London");
        assertThat(actualJourneys).hasSize(0);
    }

    @Test
    void itFetchesJourneyById() {
        List<Journey> journeys = createJourneys();

        when(journeyRepository.findById("1234")).thenReturn(journeys.get(0));
        Journey actualJourney = journeyService.getById("1234");
        assertThat(actualJourney.getDepartureStationName()).isEqualTo("DepartStation");
    }

    private List<Journey> createJourneys() {
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
        return journeys;
    }
}