# Diff base64
The purpose of this project is to provide the difference between data of a specific format.
<br><br>
It has two main responsibilities:
* Defines two http endpoints that receive and store JSON Base64 encoded binary data.
* Defines a third endpoint that responds upon requests to receive the difference between them.
# Getting Started
## Requirements
- jdk 1.8 or later
- Apache maven (not neccessary since maven wrapper is included)
## Build
``` 
./mvnw clean install
```
## Run
``` 
./mvnw spring-boot:run
```
Alternatively, you may execute the Spring Boot application main method from your IDE:
```
package tobase64;

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
The REST API for this application is described below.

The servlet context path is "/tobase64"
#### Send data on the left endpoint

Request
```
PUT /v1/diff/1/left
```
```
curl -X PUT "http://localhost:8080/tobase64/v1/diff/1/left" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"base64EncodedData\": \"ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9\"}"
```
Response
```
HTTP/1.1 200 
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
curl -X PUT "http://localhost:8080/tobase64/v1/diff/1/right" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"base64EncodedData\": \"ewogInBsYW5ldCIgOiAiZWFydGgiLAogInNpeiIgOiAiMWttIiwKICJtYXNzIiA6ICIxN2tnIiwKICJ0aW1lIiA6ICIxc2Vjb25kIgp9\"}"
```
Response
```
HTTP/1.1 200 
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
curl -X GET "http://localhost:8080/tobase64/v1/diff/1" -H "accept: application/json"
```
Response
```
HTTP/1.1 200
connection: keep-alive 
content-type: application/json 
date: Tue, 18 Aug 2020 20:21:27 GMT 
keep-alive: timeout=60 
transfer-encoding: chunked 

{
  "differentOffsets": "[15, 16, 17, 19, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 68]"
}
```
#### Other Responses
```
GET on non-existing Diff
{
  "error": "Resource Not Found",
  "status": 404,
  "detail": "Diff with id 12 not found",
  "timeStamp": "2020-08-19T08:21:47.918533600Z"
}
```
```
GET on Diff with either side null
{
  "error": "Missing information.",
  "status": 400,
  "detail": "Base64 data values are missing, for diff {1}.",
  "timeStamp": "2020-08-19T08:25:26.500712200Z"
}
```
```
PUT on non-base64 value
{
  "error": "Illegal argument given.",
  "status": 400,
  "detail": "Last encoded character (before the paddings if any) is a valid base 64 alphabet but not a possible value",
  "timeStamp": "2020-08-19T08:27:13.679200100Z"
}
```
```
GET on equal diff values.
{
  "message": "Left and right are the same."
}
```
```
GET on different size diff values.
{
  "message": "Left and right are of different sizes."
}
```
```
PUT on unsupported media type.
{
    "error": "Unsupported media type.",
    "status": 415,
    "detail": "Content type 'application/x-www-form-urlencoded' not supported",
    "timeStamp": "2020-08-19T09:00:09.450484400Z"
}
```
```
PUT with missing base64EncodedData.
{
  "error": "Invalid input detected. Is base64EncodedData value present?",
  "status": 400,
  "timeStamp": "2020-08-19T11:26:55.958180100Z",
  "errors": [
    "must not be null"
  ]
}
```
## Database
H2 in-memory, access through console: http://localhost:8080/tobase64/h2-console
<br><br>
Check the application.properties file for connection information.
## Swagger Documentation
Interactive documentation: http://localhost:8080/tobase64/swagger-ui.html
## Dockerize
```
./mvnw clean install

cd /docker

# create an image using Dockerfile
docker build -t diff-api .

# run it in a container
docker run -d -p 8080:8080 --name diffapi diff-api
```

(merge a pr without code review and get a github achievement omg)
