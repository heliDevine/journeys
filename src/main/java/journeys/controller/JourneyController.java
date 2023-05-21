package journeys.controller;

import io.swagger.v3.oas.annotations.Operation;
import journeys.model.Journey;
import journeys.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journeys")
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find all journeys from the database")

    public ResponseEntity<Page<Journey>> getAllJourney(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize) {
        return new ResponseEntity<>(journeyService.getAllJourneys(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find a journey by id")

    public ResponseEntity<Journey> getById(@PathVariable String id) {
        Optional<Journey> journey = Optional.ofNullable(journeyService.getById(id));
        if (journey.isPresent()) {
            return new ResponseEntity<>(journey.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/departureStation/{departureStationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find all journeys by the name of departure station")

    public ResponseEntity<List<Journey>> getJourneyByDepartureStationName(@PathVariable String departureStationName) {
        return new ResponseEntity<>(journeyService.getJourneysByDepartureStation(departureStationName), HttpStatus.OK);
    }
}








