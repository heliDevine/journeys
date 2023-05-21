package journeys.controller;

import io.swagger.v3.oas.annotations.Operation;
import journeys.model.Station;
import journeys.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stations")
public class StationController {

    @Autowired
    StationService stationService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find all stations from the database")

    public ResponseEntity<List<Station>> getAllStations() {
        return new ResponseEntity<>(stationService.getAllStations(), HttpStatus.OK);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find a station by id")
    public ResponseEntity<Station> getById(@PathVariable String id) {

        Optional<Station> station = Optional.ofNullable(stationService.getStationByID(id));
        return station.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/stationName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find all stations by name")

    public ResponseEntity<List<Station>> getStationByName(@PathVariable String name) {
        return new ResponseEntity<>(stationService.getStationsByNameEN(name), HttpStatus.OK
        );
    }


}




