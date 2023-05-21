package journeys.controller;

import io.swagger.v3.oas.annotations.Operation;
import journeys.model.Station;
import journeys.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    @Autowired
    StationService stationService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find all stations from the database")

    public ResponseEntity<List<Station>> getAllStations() {
        List<Station> stations = stationService.getAllStations();

        List<Station> stationsWithTotalDistance = new ArrayList<>();

        for (Station station : stations) {
            double totalDistance = stationService.totalJourneyDistanceTrial(station);
            station.setTotalJourneyDistanceFromStation(totalDistance);
            stationsWithTotalDistance.add(station);
        } return new ResponseEntity<>(stationsWithTotalDistance, HttpStatus.OK);

    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find a station by id")
    public ResponseEntity<Station> getById(@PathVariable String id) {

        Station station = stationService.getStationByID(id);
        if(station != null) {
            double totalDistance = stationService.totalJourneyDistanceTrial(station);
            station.setTotalJourneyDistanceFromStation(totalDistance);
            return new ResponseEntity<>(station, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/stationName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find all stations by name")

    public ResponseEntity<Station> getStationByName(@PathVariable String name) {
        return new ResponseEntity<>(stationService.getStationsByNameEN(name), HttpStatus.OK
        );
    }

    @GetMapping(value = "/totalDistance{ID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "total travel distance")

    public  ResponseEntity<Double> totalDistance(@PathVariable Station station) {
        return new ResponseEntity<>(stationService.totalJourneyDistanceTrial(station), HttpStatus.OK);
    }
}




