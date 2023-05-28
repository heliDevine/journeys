package journeys.service;

import journeys.model.Station;
import journeys.repository.JourneyRepository;
import journeys.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private JourneyRepository journeyRepository;

    public Page<Station> getAllStations(Pageable pageable) {
        return stationRepository.findAllStationsWithFilteredFields(pageable);
    }
    public Station getStationByID(String id) {
        return stationRepository.findById(id);
    }

    public Station getStationsByNameEN(String stationNameEN) {
        return stationRepository.findByStationNameEN(stationNameEN);
    }

    public Double totalJourneyDistance(Station station) {
        int stationID = station.getStationID();
       Double totalDistance = journeyRepository.calculateTotalJourneyDistanceFromStation(stationID);
        return totalDistance != null ? totalDistance : 0.0;
    }

    public long totalJourneyCountDeparted(Station station) {
        int stationID = station.getStationID();
        return journeyRepository.countByDepartureStationId(stationID);
    }

    public long totalJourneyCountReturned(Station station) {
        int stationID = station.getStationID();
        return journeyRepository.countByReturnStationId(stationID);
    }

    public Page<Station> processPagedStation(Pageable pageable) {

        Page<Station> stationsPage = stationRepository.findAllStationsWithFilteredFields(pageable);

        List<Station> stationsWithTotalDistance = new ArrayList<>();

        for (Station station : stationsPage.getContent()) {

            double totalDistance = totalJourneyDistance(station);
            long countDepartedJourneys = totalJourneyCountDeparted(station);
            long countReturnedJourneys = totalJourneyCountReturned(station);

            if(totalDistance !=0.0) {
                station.setTotalJourneyDistanceFromStation(totalDistance);
                station.setTotalDepartingJourneys(countDepartedJourneys);
                station.setTotalReturnedJourneys(countReturnedJourneys);
            }else {
                station.setTotalJourneyDistanceFromStation(0.0);
            }
            stationsWithTotalDistance.add(station);
        }
        return new PageImpl<>(stationsWithTotalDistance, pageable, stationsPage.getTotalElements());
    }



}
