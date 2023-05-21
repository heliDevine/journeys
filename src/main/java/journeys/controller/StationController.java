package journeys.controller;

import io.swagger.v3.oas.annotations.Operation;
import journeys.model.Station;
import journeys.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    @Autowired
    StationService stationService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find all stations from the database")
    public ResponseEntity<Page<Station>> getAllStations(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Station> stationsPage = stationService.getAllStations(pageable);

        List<Station> stationsWithTotalDistance = new ArrayList<>();

        for (Station station : stationsPage.getContent()) {
            double totalDistance = stationService.totalJourneyDistance(station);
            station.setTotalJourneyDistanceFromStation(totalDistance);
            stationsWithTotalDistance.add(station);
        }

        Page<Station> stationsWithTotalDistancePage =
                new PageImpl<>(stationsWithTotalDistance, pageable, stationsPage.getTotalElements());

        return new ResponseEntity<>(stationsWithTotalDistancePage, HttpStatus.OK);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find a station by id")
    public ResponseEntity<Station> getById(@PathVariable String id) {

        Station station = stationService.getStationByID(id);
        if(station != null) {
            double totalDistance = stationService.totalJourneyDistance(station);
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
        return new ResponseEntity<>(stationService.totalJourneyDistance(station), HttpStatus.OK);
    }
}




