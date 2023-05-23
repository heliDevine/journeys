package journeys;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mongo")
@Data
public class MongoConfiguration {

    @Value("${mongo.username}")
    private String mongoUsername;

    @Value("${mongo.password}")
    private String mongoPassword;

}