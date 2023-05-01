package journeys.controller;

import io.swagger.v3.oas.annotations.Operation;
import journeys.model.Journey;
import journeys.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class JourneyController {

    @Autowired
    private JourneyService journeyService;


    @GetMapping(value = "/journeys/{departureStationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "get all journeys by the name of departure station")

    public ResponseEntity<List<Journey>> getJourney(@PathVariable String departureStationName) {
        return new ResponseEntity<>(journeyService.findJourneysByDepartureStation(departureStationName), HttpStatus.OK);
    }

    @GetMapping(value = "/journeys", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "all journeys")

    public ResponseEntity<List<Journey>> getAllJourneys() {
        return new ResponseEntity<>(journeyService.getAllJourneys(), HttpStatus.OK);
    }
}


