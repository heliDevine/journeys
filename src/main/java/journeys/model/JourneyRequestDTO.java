package journeys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class JourneyRequestDTO {

        private String departureStationName;
        private String returnStationName;
        private int distance;
        private int duration;
}
