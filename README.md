# Scalable Web
The purpose of this project is to provide the difference between data of a specific format.
<br><br>
It has two main responsibilities:
* Defines two http endpoints that receive and store JSON Base64 encoded binary data.
* Defines a third endpoint that responds upon requests to receive the difference between them.
## Getting Started
### Requirements
- jdk 1.8
- Apache maven
## Build
``` mvn clean install -DskipTests ```
<br>
## Run
Execute the Spring Boot application main method:
```
package scalableweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DifferenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DifferenceApplication.class, args);
    }
}
```
## REST API

#### Send data on the left endpoint

Request
```
PUT /v1/diff/1/left
```
```
curl -X PUT "http://localhost:8080/scalableweb/v1/diff/1/left" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"base64EncodedData\": \"ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9\"}"
```
Response
```
connection: keep-alive 
content-type: application/json 
date: Tue, 18 Aug 2020 20:17:03 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked

{
  "id": 1,
  "left": "{\n \"planet\" : \"pluto\",\n \"size\" : \"6km\",\n \"mass\" : \"3kg\",\n \"time\" : \"7second\"\n}",
  "right": null
}
```
#### Send data on the right endpoint
Request
```
PUT /v1/diff/1/right
```
```
curl -X PUT "http://localhost:8080/scalableweb/v1/diff/1/right" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"base64EncodedData\": \"ewogInBsYW5ldCIgOiAiZWFydGgiLAogInNpeiIgOiAiMWttIiwKICJtYXNzIiA6ICIxN2tnIiwKICJ0aW1lIiA6ICIxc2Vjb25kIgp9\"}"
```
Response
```
connection: keep-alive 
content-type: application/json 
date: Tue, 18 Aug 2020 20:20:10 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked

{
  "id": 1,
  "left": "{\n \"planet\" : \"pluto\",\n \"size\" : \"6km\",\n \"mass\" : \"3kg\",\n \"time\" : \"7second\"\n}",
  "right": "{\n \"planet\" : \"earth\",\n \"siz\" : \"1km\",\n \"mass\" : \"17kg\",\n \"time\" : \"1second\"\n}"
}
```
#### Last, retrieve the diff between them...
Request
```
GET /v1/diff/1
```
```
curl -X GET "http://localhost:8080/scalableweb/v1/diff/1" -H "accept: */*"
```
Response
```
connection: keep-alive 
content-type: application/json 
date: Tue, 18 Aug 2020 20:21:27 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked 

{
  "differentOffsets": "[15, 16, 17, 19, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 68]"
}
```
## Database
H2 in-memory console: http://localhost:8080/scalableweb/h2-console

## Swagger Documentation
http://localhost:8080/scalableweb/swagger-ui.html
