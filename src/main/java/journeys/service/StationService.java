package journeys.service;

import journeys.model.Station;
import journeys.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station getStationByID(String id) {
        return stationRepository.findById(id);

    }
    public List<Station> getStationsByNameEN(String stationNameEN) {
        return stationRepository.findByStationNameEN(stationNameEN);
    }
}
