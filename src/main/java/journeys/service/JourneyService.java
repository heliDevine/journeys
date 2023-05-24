package journeys.service;

import journeys.model.Journey;
import journeys.model.Station;
import journeys.repository.JourneyRepository;
import journeys.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JourneyService {

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private StationRepository stationRepository;

    public Journey getById(String id) {
        return journeyRepository.findById(id);
    }

    public Page<Journey> getAllJourneys(Integer pageNo, Integer pageSize) {

        Pageable paging = PageRequest.of(pageNo, pageSize);
        return journeyRepository.findAllJourneys(paging);
    }

    public List<Journey> getJourneysByDepartureStation(String departureStationName) {
        return journeyRepository.findByDepartureStationName(departureStationName);
    }

    public Journey createJourney(Journey journey) {
        Station departureStation = stationRepository.findByStationNameEN(journey.getDepartureStationName());
        System.out.println(departureStation);
        Station returnStation = stationRepository.findByStationNameEN(journey.getReturnStationName());
        System.out.println("************************"+returnStation);
        if ((departureStation != null) && (returnStation != null)) {
            journey.setDepartureStationId(departureStation.getStationID());
            journey.setReturnStationId(returnStation.getStationID());
            journeyRepository.save(journey);
        } else {
            return journey;
        }
        return journey;
    }
}


