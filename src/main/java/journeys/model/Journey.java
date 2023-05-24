package journeys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "test_data")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Journey {

    @JsonIgnore
    private String id;

    private String departureTime;

    private String returnTime;

    private int departureStationId;

    private String departureStationName;

    private int returnStationId;

    private String returnStationName;

    private int distance;

    private int duration;

}
