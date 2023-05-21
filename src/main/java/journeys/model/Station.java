package journeys.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "station_data")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Station {

    @Id
    private String id;

    private int FID;
    @Field("ID")
    private int StationID;
    @Field("nimi")
    private String stationNameFI;
    @Field("namn")
    private String stationNameSE;
    @Field("name")
    private String stationNameEN;
    @Field("osoite")
    private String stationAddressFI;
    @Field("address")
    private String stationAddressSE;
    private String city;
    @Field("stad")
    private String citySE;
    @Field("operator")
    private String bikeOperator;
    @Field("capacity")
    private int stationCapacity;
    @Field("x")
    private double coordinateX;
    @Field("y")
    private double coordinateY;

    private double totalJourneyDistanceFromStation;

    private int averageJourneyDistanceFromStation;

}
