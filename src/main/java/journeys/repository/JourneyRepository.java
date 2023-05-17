package journeys.repository;

import journeys.model.Journey;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends MongoRepository<Journey, ObjectId> {

    Journey findById(String id);

    @Query(value = "{}", fields = "{ departureTime : 0, returnTime : 0, departureStationId : 0, returnStationId : 0 }")
    Page<Journey> findAllJourneys(Pageable pageable);

    List<Journey> findByDepartureStationName(String departureStationName);
}
