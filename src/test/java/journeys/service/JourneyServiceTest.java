package journeys.service;

import journeys.model.Journey;
import journeys.model.Station;
import journeys.repository.JourneyRepository;
import journeys.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JourneyServiceTest {

    @Mock
    private JourneyRepository journeyRepository;

    @Mock
    StationRepository stationRepository;

    @InjectMocks
    private JourneyService journeyService;


    @Test
    void itFetchesAllJourneys() {

        List<Journey> journeys = createJourneys();
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Journey> journeysPage = new PageImpl<>(journeys, pageRequest, journeys.size());

        when(journeyRepository.findAllJourneys(pageRequest))
                .thenReturn(journeysPage);

        Page<Journey> actualJourneys = journeyService.getAllJourneys(0,2);
        assertThat(actualJourneys).hasSize(2);
    }

    @Test
    void itFetchesAllJourneysByDepartureStationName() {
        List<Journey> journeys = createJourneys();

        when(journeyRepository.findByDepartureStationName("Manchester"))
                .thenReturn(Collections.singletonList(journeys.get(1)));

        List<Journey> actualJourneys = journeyService
                .getJourneysByDepartureStation("Manchester");
        assertThat(actualJourneys).hasSize(1);
    }
    @Test
    void itDoesntReturnJourneysIfStationDoesntExist() {
        List<Journey> journeys = createJourneys();

        when(journeyRepository.findByDepartureStationName("London"))
                .thenReturn(Collections.emptyList());

        List<Journey> actualJourneys = journeyService
                .getJourneysByDepartureStation("London");
        assertThat(actualJourneys).isEmpty();
    }

    @Test
    void itFetchesJourneyById() {
        List<Journey> journeys = createJourneys();

        when(journeyRepository.findById("1234")).thenReturn(journeys.get(0));
        Journey actualJourney = journeyService.getById("1234");
        assertThat(actualJourney.getDepartureStationName()).isEqualTo("DepartStation");
    }

    @Test
    void itSavesNewJourneyWithStationIdsWhenIdsExist() {

        Station departureStation = Station.builder().stationID(1).build();
        Station returnStation = Station.builder().stationID(2).build();
        when(stationRepository.findByStationNameEN("Firswood")).thenReturn(departureStation);
        when(stationRepository.findByStationNameEN("Market Street")).thenReturn(returnStation);

        Journey journey = Journey.builder()
                .id("123")
                .departureStationName("Firswood")
                .returnStationName("Market Street")
                .distance(5000)
                .duration(4000)
                .build();

            Journey savedJourney = journeyService.createJourney(journey);
            verify(journeyRepository, times(1)).save(journey);
            assertThat(journey.getDepartureStationId()).isEqualTo(savedJourney.getDepartureStationId());
            assertThat(journey.getReturnStationId()).isEqualTo(savedJourney.getReturnStationId());
        }

    @Test
    void itDoesntSaveJourneyWithoutStationIds() {

        when(stationRepository.findByStationNameEN("Firswood")).thenReturn(null);
        when(stationRepository.findByStationNameEN("Market Street")).thenReturn(null);

        Journey journey = Journey.builder()
                .id("123")
                .departureStationName("Firswood")
                .returnStationName("Market Street")
                .distance(5000)
                .duration(4000)
                .build();

        Journey savedJourney = journeyService.createJourney(journey);
        verify(journeyRepository, never()).save(journey);
        assertNull(savedJourney);
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