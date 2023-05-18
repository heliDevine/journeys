package journeys.controller;

import io.swagger.v3.oas.annotations.Operation;
import journeys.model.Station;
import journeys.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class StationController {

    @Autowired
    StationRepository stationRepository;

    @GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "all stations")

    public ResponseEntity<List<Station>> getAllStations() {
        return new ResponseEntity<>(stationRepository.findAll(), HttpStatus.OK);
    }
}




