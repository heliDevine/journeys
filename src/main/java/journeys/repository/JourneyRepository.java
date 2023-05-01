package journeys.repository;

import journeys.model.Journey;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends MongoRepository<Journey, ObjectId> {

        List<Journey> findByDepartureStationName(String departureStationName);

}
