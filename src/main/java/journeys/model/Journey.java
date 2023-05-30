package journeys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "journey_data")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Journey {

    @JsonIgnore
    private String id;

    @Field("departureTime")
    private String departureTime;

    @Field("returnTime")
    private String returnTime;

    @JsonIgnore
    @Field("departureStationId")
    private int departureStationId;

    @Field("departureStationName")
    private String departureStationName;

    @JsonIgnore
    @Field("returnStationId")
    private int returnStationId;

    @Field("returnStationName")
    private String returnStationName;

    @Field("distance")
    @Min(10)
    private int distance;

    @Field("duration")
    @Min(10)
    private int duration;

}
