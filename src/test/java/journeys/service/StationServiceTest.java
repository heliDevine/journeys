package journeys.service;

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

        Station station = createStation();

        when(stationRepository.findById("123")).thenReturn(station);
        Station actualStation = stationService.getStationByID("123");

        assertThat(actualStation.getStationNameEN()).isEqualTo("Kallio");
    }

    @Test
    void itDoesntReturnNonExistingStation() {

        String id = "123";

        when(stationRepository.findById(id)).thenReturn(null);
        Station actualStation = stationService.getStationByID("123");

        assertThat(actualStation).isNull();
    }

    @Test
    void itFetchesStationByName() {

        Station station = createStation();

        when(stationRepository.findByStationNameEN(station.getStationNameEN())).thenReturn(station);

        Station actualStation = stationService.getStationsByNameEN("Kallio");
        assertThat(actualStation.getId()).isEqualTo("123");
    }

    @Test
    void itDoesntReturnNonExistingStationByName() {

        when(stationRepository.findByStationNameEN("Piccadilly Circus")).thenReturn(null);
        Station actualStation = stationService.getStationsByNameEN("Piccadilly Circus");

        assertThat(actualStation).isNull();
    }

    @Test
    void itReturnsTotalDistanceFromStation() {

        Station station = createStation();

        when(journeyRepository.calculateTotalJourneyDistanceFromStation(station.getStationID()))
                .thenReturn(1500.0);

        Double totalDistance = stationService.totalJourneyDistance(station);
        assertThat(totalDistance).isEqualTo(1500.0);
    }

    @Test
    void itReturnsTotalCountOfDepartingJourneys() {

        Station station = createStation();

        when(journeyRepository.countByDepartureStationId(station.getStationID()))
                .thenReturn(station.getTotalDepartingJourneys());

        Long totalCount = stationService.totalJourneyCountDeparted(station);
        assertThat(totalCount).isEqualTo(2);
    }

    @Test
    void itReturnsTotalCountOfReturningJourneys() {

        Station station = createStation();

        when(journeyRepository.countByReturnStationId(station.getStationID()))
                .thenReturn(station.getTotalReturnedJourneys());

        Long totalCount = stationService.totalJourneyCountReturned(station);
        assertThat(totalCount).isEqualTo(3);
    }

    private Station  createStation() {
        return Station.builder()
                .id("123")
                .stationID(1)
                .stationNameEN("Kallio")
                .totalReturnedJourneys(3)
                .totalDepartingJourneys(2)
                .totalJourneyDistanceFromStation(1500)
                .build();
    }
}