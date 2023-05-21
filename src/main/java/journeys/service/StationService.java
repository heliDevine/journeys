package journeys.service;

import journeys.model.Station;
import journeys.repository.JourneyRepository;
import journeys.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private JourneyRepository journeyRepository;

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station getStationByID(String id) {
        return stationRepository.findById(id);

    }
    public Station getStationsByNameEN(String stationNameEN) {
        return stationRepository.findByStationNameEN(stationNameEN);
    }

    public Double totalJourneyDistanceTrial(Station station) {
        int stationID = station.getStationID();
       Double totalDistance = journeyRepository.calculateTotalJourneyDistanceFromStation(stationID);
        return totalDistance != null ? totalDistance : 0.0;
    }
}
