package journeys.service;

import journeys.model.Journey;
import journeys.repository.JourneyRepository;
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

    public Journey getById(String id) {
        return journeyRepository.findById(id);
    }

    public Page<Journey> getAllJourneys(Integer pageNo, Integer pageSize) {

        Pageable paging = PageRequest.of(pageNo, pageSize);
        return  journeyRepository.findAllJourneys(paging);
    }

    public List<Journey> getJourneysByDepartureStation(String departureStationName) {
        return journeyRepository.findByDepartureStationName(departureStationName);
    }
}

