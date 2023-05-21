package journeys.repository;

import journeys.model.Station;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends MongoRepository<Station, ObjectId> {

    Station findById(String id);

    List<Station> findByStationNameEN(String stationNameEN);

    List<Station> findAll();

}
