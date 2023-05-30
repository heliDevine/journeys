# JOURNEYS 🚴

**Full stack application with Java Springboot backend and [React frontend](https://github.com/heliDevine/journeys-frontend)**

The application fetches and processes some data from journeys made with city bikes and the bike stations. 
Head to the frontend repo [readme](https://github.com/heliDevine/journeys-frontend/blob/main/README.md) to read more about the concept for the web client.
This backend was developed with the core concept in mind to service the frontend with relevant data but leaving enough 
abstraction when/if frontend client's needs grow and change. 

## Technology 

### Stack and tools
- Java 17
- Springboot 3
- Testing : JUnit & Mockito
- Libraries: Lombok to reduce boilerplate and Jackson to serialize/deserialize.
- Database : MongoAtlas, remote cloud hosted NoSQL database
- SonarLint for code analysis and IntelliJ's default settings for code formatting.
- Swagger for manual endpoint testing and API documentation.

I chose Java, Springboot and Maven for this application as I'm familiar with them in backend development. 
I'm constantly learning more and my goal is to develop a deeper understanding of the APIs, microservices, systems design, etc. 
Java and Springboot have been a great base to start and are widely used in different types of systems. 

### Data validation

Data import and validation: Mongo Compass was used to import the datasets. I changed a few keys for clarity and 
I only imported May 2021 journeys datasets as MongoAtlas free tier has size limit. 

I used following jsonSchema for the validation to exclude short journeys and excluded empty strings.

`{ $jsonSchema: { properties: { distance: { minimum: 10 }, duration: { minimum: 10 } } } }`'

yeStations and journeys use separate collections from same database.

### System design

Application follows Springboot convention of separating the concerns. Repository layer handles database queries and aggregation,
service layer business logic and controllers the REST requests.

#### Repositories 

I excluded unnecessary fields on database query which made it clearer for dedicated endpoints to provide relevant data and reduced bulk of
data which was sent to the frontend. To calculate total journey distance from a given station I used aggregation pipeline which worked well and reduced code on service layer.

#### Services

The service layer contains logic of processing journeys and stations. Most methods are straightforward calls to repository, 
processStation and createJourney methods have more complexity. CreateJourney method adds remaining fields 
to the journey. Reasoning behind this decision was to reduce the input fields which user would need know about their journey. 
StationID's can be pulled from the database and "in real world" timestamps would be generated when bike leaves or returned to the station.
Therefore, I decided to add these programmatically by using LocalDateTime and calculate return time. 
This generated complete journey will be saved to the database. ProcessStation method adds
total distance of departed journeys and total counts of journeys departing from and to the station.

#### Controllers

At the moment the linked frontend client uses 2 endpoints GET/journeys and GET/stations. GetAllJourneys method fetches 
all journeys from database, the results are paginated with optional page sizes. GetAllStations method fetches all stations with 
added fields, results are paginated and sorted alphabetically. 

I added POST journey endpoint with a request body for departure and return stations, duration and distance. 
I used  DTOs for Journey to decouple the database entity from the HTTP request body and the response. 

The other endpoints are shown on the below table and mainly used during the development and testing but can be 
used via Swagger endpoints and would be useful for future features.

| HTTP | endpoint                                          | method                            | params               |
|------|---------------------------------------------------|-----------------------------------|----------------------|
| GET  | /journeys                                         | getAllJourneys                    | pageNo,pageSize      |
| GET  | /journeys {id}                                    | getById                           | id                   |
| GET  | /journeys/departureStation/{departureStationName} | getJourneysByDepartureStationName | departureStationName |
| POST | /journeys/journey                                 | createJourney                     | journeyRequestDTO    |
| GET  | /stations                                         | getAllStations                    | pageNo,pageSize      |
| GET  | /stations/{id}                                    | getById                           | id                   |
| GET  | /stationName/{name}                               | getStationByName                  | stationName          |


## Run the application

- clone this repo by command `git clone git@github.com:heliDevine/journeys.git` 
- navigate to the directory `cd journeys`
- Database username and password are not in the GitHub repo, so they need to be added manually. 
- Amend secrets.properties file: remove ".template" form the file name and replace username and password, "mongo.username=USERNAME_PLACEHOLDER" and
  "mongo.password=PASSWORD_PLACEHOLDER" change placeholder values after "=" sign to real values. The best way to do this is in IDE (See images folder for reference)

### Docker container
- You will need [Docker](https://docs.docker.com/get-docker/) installed machine. Check if you already have it by running
`docker --version` command on CLI. If not, download it from the Docker site.
- build the image from docker file with command `docker build -t journeys-app .` 
- run the container with command `docker run -d -p 8080:8080 journeys-app` -d flag will run the container in de-attached mode, so it won't block the terminal window whilst application is running. 
- The application takes a few moments to start but head to your default browser and to this url: http://localhost:8080/swagger-ui/index.html and you should see Swagger UI. (see images folder for reference)

### On your own machine

If you go for this option I assume you have development environment set up already, such as Homebrew for Mac.
This is Java Springboot application built with Maven, so it needs following to be installed 
- Java 17 JDK, follow the guide from this 
[link](https://docs.oracle.com/en/java/javase/17/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A)
- Maven build tool, download and install latest from [here](https://maven.apache.org/download.cgi)
- From command line `mvn spring-boot:run` to run the application or in IDE click green arrow in `JourneysApplication.java` class. This will start Springboot and takes a few moments. Head to your default browser
http://localhost:8080/swagger-ui/index.html, and you should see Swagger UI. (See image in images folder for reference)

### Testing

Junit and Mockito are used to test the service methods and controllers. The tests can be run with command `mvn test`.
The test coverage is 85% and all tests should pass but some methods are not fully tested. 

## Project status and comments

The project is currently MVP, it works as intended and as described above. However, several features could be added to improve it. Some are listed
in issues in this repo which I kept as to-do list whilst working. If/when I have time I'd prioritise developing following:
- improve error/exception handling, the backend should inform the client clearly when things go wrong.
- add functionality that Users can add, delete and amend their journeys and Admin users can add, delete and amend both journeys and stations. So login
and security features are needed to give permissions for different users.
- fix inconsistencies in the code and tests. For example create DTOs for GET endpoints. 
- improve testing and run database in test container

**Author: Heli Devine**