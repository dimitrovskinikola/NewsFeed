#News Feed App

## Implementation
- https://start.spring.io/ was used to generate the app
- ide intellij idea 
## Build & Deploy
### No need of additional App server. The application starts on Embedded Tomcat in Spring Boot  
- mvn clean install
- java -jar target/newsfeed-0.0.1-SNAPSHOT.jar
## Logging
- log4j2 is used the configuration is located on /main/java/resources/log4j2.xml
## Configuration
- all configuration set up such as news feed url and scheduler cron are located: /main/java/resources/application.yaml
## DB
- schema name, username and password are located: /main/java/resources/application.yaml
- H2 console http://localhost:8080/h2-console
- data.sql is used to generate the in memory table
- query to fetch all results SELECT * FROM NEWSFEED;
## Testing 
- The application was manually tested
- There are no JUnit tests
## Additional settings
- Scheduler was used to poll requests every 5 minutes
- lombok was used for getters and setters
- rest template was used for sending requests for fetching data
- rome library was used to parse the RSS Feed
- Commented code snipped is present which was used to create image from the byte[] downloaded grom the image url. This was done for testing purpose.




