package journeys.controller;

import io.swagger.v3.oas.annotations.Operation;
import journeys.model.Station;
import journeys.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Page<Station>stationsWithAddedFields= stationService.processPagedStation(pageable);

        return new ResponseEntity<>(stationsWithAddedFields, HttpStatus.OK);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find a station by id")
    public ResponseEntity<Object> getById(@PathVariable String id) {

        Station station = stationService.getStationByID(id);
        if (station != null) {
            double totalDistance = stationService.totalJourneyDistance(station);
            station.setTotalJourneyDistanceFromStation(totalDistance);

            return new ResponseEntity<>(station, HttpStatus.OK);
        } else {
            String errorMessage = "Station doesn't exist";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(errorMessage));
        }
    }

    @GetMapping(value = "/stationName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find station by name")
    public ResponseEntity<Object> getStationByName(@PathVariable String name) {
      Station station = stationService.getStationsByNameEN(name);

      if(station !=null) {
          return new ResponseEntity<>(station, HttpStatus.OK);
      }
        String errorMessage = "Station name doesn't exists, check spelling";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(errorMessage));
    }
}




