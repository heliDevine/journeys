package journeys.repository;

import journeys.model.Station;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends MongoRepository<Station, ObjectId> {

    Station findById(String id);

    Station findByStationNameEN(String stationNameEN);

    @Query(value = "{}", fields = "{FID:0, nimi: 0, namn:0, osoite: 0, city: 0, stad:0, operator:0, capacity:0, x:0, y:0 }")
    List<Station> findAll();

    @Query(value = "{}", fields = "{FID:0, nimi: 0, namn:0, address: 0, city: 0, stad:0, operator:0, x:0, y:0 }")
    Page<Station> findAllStationsWithFilteredFields(Pageable pageable);

}
