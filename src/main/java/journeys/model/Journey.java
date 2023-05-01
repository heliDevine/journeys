package journeys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "test_data")
@NoArgsConstructor
@AllArgsConstructor
public class Journey {
    @Id
    private ObjectId id;

    private String departureTime;

    private String returnTime;

    private int departureStationId;

    private String departureStationName;

    private int returnStationId;

    private String returnStationName;

    private int distance;

    private int duration;

}
