package journeys.service;

import journeys.model.Station;
import journeys.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

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

        when(stationRepository.findAll()).thenReturn(stations);

        List<Station> actualStations  = stationService.getAllStations();
        assertThat(stations).hasSize(2);
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
}