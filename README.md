## Work planning system

- a worker has shifts
- a shift is 8h long
- a worker never has two shifts same day
- shifts are: 0-8, 8-16, 16-24

## Run (locally)

Using maven:
1) from root folder  
   - mvn spring-boot: run   
2) or
   from root folder  
   - mvn clean package  
   - cd target
   - java -jar my-schedule-0.0.1-SNAPSHOT.jar  

## API (local)
Swagger  
http://localhost:8081/api/swagger-ui/

## API (AWS hosted)
Swagger  
http://myschedule-env.eba-mdk3raap.eu-central-1.elasticbeanstalk.com/api/swagger-ui/
