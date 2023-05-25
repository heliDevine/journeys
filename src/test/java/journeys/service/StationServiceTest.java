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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private JourneyRepository journeyRepository;

    @InjectMocks
    private StationService stationService = new StationService();

    @Test
    void itFetchesAllStations() {

        List<Station> stations;
        stations = Arrays.asList(
                Station.builder()
                        .id("testId")
                        .stationNameEN("Test station")
                        .build(),
                Station.builder()
                        .id("TestId2")
                        .stationNameEN("Test station 2")
                        .build()
        );
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Station> stationPagesPage = new PageImpl<>(stations, pageRequest, stations.size());
        when(stationRepository.findAllStationsWithFilteredFields(pageRequest)).thenReturn(stationPagesPage);
        Page<Station> actualStations  = stationService.getAllStations(pageRequest);

        assertThat(actualStations).hasSize(2);
    }

    @Test
    void itFetchesStationById() {
        String id = "123";
        Station station = Station.builder()
                .id(id)
                .stationNameEN("Piccadilly Circus")
                .build();
        when(stationRepository.findById("123")).thenReturn(station);
        Station actualStation = stationService.getStationByID("123");

        assertThat(actualStation.getStationNameEN()).isEqualTo("Piccadilly Circus");
    }

    @Test
    void itDoesntReturnNonExistingStation() {

        String id = "123";
        Station station = Station.builder()
                .id(id)
                .stationNameEN("Piccadilly Circus")
                .build();
        when(stationRepository.findById("123")).thenReturn(null);
        Station actualStation = stationService.getStationByID("123");

        assertThat(actualStation).isNull();
    }

    @Test
    void itFetchesStationByName() {
        String id = "123";
        Station station = Station.builder()
                .id(id)
                .stationNameEN("Piccadilly Circus")
                .build();
        when(stationRepository.findByStationNameEN("Piccadilly Circus")).thenReturn(station);

        Station actualStation = stationService.getStationsByNameEN("Piccadilly Circus");
        assertThat(actualStation.getId()).isEqualTo("123");

    }

    @Test
    void itDoesntReturnNonExistingStationByName() {

        String id = "123";
        Station station = Station.builder()
                .id(id)
                .stationNameEN("Piccadilly Circus")
                .build();
        when(stationRepository.findByStationNameEN("Piccadilly Circus")).thenReturn(null);
        Station actualStation = stationService.getStationsByNameEN("Piccadilly Circus");

        assertThat(actualStation).isNull();
    }

    @Test
    void itReturnsTotalDistanceFromStation() {

        Station station = Station.builder()
                .id("123")
                .StationID(1)
                .stationNameEN("Kallio")
                .build();
        when(journeyRepository.calculateTotalJourneyDistanceFromStation(station.getStationID()))
                .thenReturn(1500.0);

        Double totalDistance = stationService.totalJourneyDistance(station);
        assertThat(totalDistance).isEqualTo(1500.0);

    }

    @Test
    void itReturnsTotalCountOfDepartingJourneys() {

        Station station = Station.builder()
                .id("123")
                .StationID(1)
                .stationNameEN("Kallio")
                .totalReturnedJourneys(3)
                .totalDepartingJourneys(2)
                .totalJourneyDistanceFromStation(1500)
                .build();
        when(journeyRepository.countByDepartureStationId(station.getStationID()))
                .thenReturn(station.getTotalDepartingJourneys());

        Long totalCount = stationService.totalJourneyCountDeparted(station);
        assertThat(totalCount).isEqualTo(2);

    }

    @Test
    void itReturnsTotalCountOfReturningJourneys() {

        Station station = Station.builder()
                .id("123")
                .StationID(1)
                .stationNameEN("Kallio")
                .totalReturnedJourneys(3)
                .totalDepartingJourneys(2)
                .totalJourneyDistanceFromStation(1500)
                .build();
        when(journeyRepository.countByReturnStationId(station.getStationID()))
                .thenReturn(station.getTotalReturnedJourneys());

        Long totalCount = stationService.totalJourneyCountReturned(station);
        assertThat(totalCount).isEqualTo(3);

    }

    private List<Journey> createJourneys() {
        List<Journey> journeys;
        journeys = Arrays.asList(
                Journey.builder()
                        .id("123")
                        .departureStationId(1)
                        .departureStationName("Kallio")
                        .distance(1000)
                        .build(),
                Journey.builder()
                        .id("456")
                        .departureStationId(2)
                        .departureStationName("Manchester")
                        .distance(2000)
                        .build(),
                Journey.builder()
                        .id("789")
                        .departureStationId(1)
                        .departureStationName("Kallio")
                        .distance(500)
                        .build()
        );
        return journeys;
    }
}