package journeys.service;

import journeys.model.Journey;
import journeys.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JourneyService {

    @Autowired
    private JourneyRepository journeyRepository;

    public List<Journey> findJourneysByDepartureStation(String departureStationName) {
        return journeyRepository.findByDepartureStationName(departureStationName);
    }

    public List<Journey> getAllJourneys() {
        return journeyRepository.findAll();
    }

}
