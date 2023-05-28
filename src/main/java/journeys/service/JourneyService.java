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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

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
        try {
            return journeyRepository.findByDepartureStationName(departureStationName);
        } catch (NoSuchElementException e) {
            return Collections.emptyList();
        }
    }

    public Journey createJourney(Journey journey) {

        Station departureStation = stationRepository.findByStationNameEN(journey.getDepartureStationName());
        Station returnStation = stationRepository.findByStationNameEN(journey.getReturnStationName());

        LocalDateTime departureTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String departureTimeString = departureTime.format(formatter);

        int durationInSeconds = journey.getDuration();
        LocalDateTime returnTime = departureTime.plusSeconds(durationInSeconds);
        String returnTimeString = returnTime.format(formatter);

        if ((departureStation != null) && (returnStation != null)) {
            journey.setDepartureStationId(departureStation.getStationID());
            journey.setReturnStationId(returnStation.getStationID());
            journey.setDepartureTime(departureTimeString);
            journey.setReturnTime(returnTimeString);
            journeyRepository.save(journey);
        } else {
            return null;
        }
        return journey;
    }
}


