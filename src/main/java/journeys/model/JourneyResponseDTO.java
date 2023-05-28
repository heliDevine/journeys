package journeys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class JourneyResponseDTO {

    private String departureTime;
    private String returnTime;
    private int departureStationId;
    private String departureStationName;
    private int returnStationId;
    private String returnStationName;
    private int distance;
    private int duration;
}
