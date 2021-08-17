# URL-Shortener-API

URL-Shortener-API is a spring Boot based REST API that takes a URL and returns a shortened URL and persist it in database.
The project is Home assignment for Neueda interview process.

## Used tools: 
* Java 11
* Spring Boot 2.5.3
* Lombok
* JUnit 4.13.1
* MySql 8
* Gradle 7.1.1

# Getting Started

## Dependencies

This project depends on
* Spring MVC (spring-boot-starter-web)
* Spring Data (spring-boot-starter-data-jpa)
* Spring Test (spring-boot-starter-test)
* String Actuator (spring-boot-starter-actuator)
* commons-validator:1.7 (to validate URL)
* InMemory Database H2 (only for tests case)

## Building Project

To build this project, you need to run the shell commands below:

```shell script
git clone https://github.com/JK-Sebastiao/URL-Shortener-API.git
cd url-shortener
gradle clean build
```

## Deployment

Built project can be deployed using docker-compose.yml which sets up two containers for
* MySql Database container named url-db
* REST API container named url-shortener

To deploy the project, you need to run the shell command below:

```shell script
docker-compose up --build
```

**The application will be accessible on http://localhost:8080**

### db.Dockerfile
`db.Dockerfile` builds the docker image for MySql using MySql version 8 as the base image. It uses `schema.sql` at startup to set up the database schema.

### api.Dockerfile
`api.Dockerfile` builds the docker image with OpenJDK 11 to deploy the project's jar file generated above from `build/libs/url-shortener-api-0.0.1-SNAPSHOT.jar`. It exposes the API on port `8080`

### docker-compose.yml
Provides the configuration for containers to host URL shortener API and MySql Database. It sets up two services; `api-server` and `api-db` with container named `url-shortener` and `url-db` respectively.
The datasource url is being set in the `api-server` configuration so that it points to the MySql Database container.
Both `api-server` and `api-db` are linked together through the `neueda-network` docker network. The network enables both the containers to communicate together.

## API Endpoints

You can access following API endpoints at http://localhost:8080

### POST `/shorten-url`
It takes a JSON object in the following format as payload

```json
{
  "fullUrl":"<The full URL to be shortened>"
}
```
Response:

```json
{
  "shortUrl": "<The shortened uURL for the given fullUrl in the request payload>"
}
```

### GET `/<shortened_text>`

This endpoint will redirect you to the corresponding fullUrl.

### GET `/actuator/health`

This endpoint will return the result of health checks.

#### cURL to get shorten URL

```shell script
curl -X POST \
  http://localhost:8080/shorten-url \
  -H 'Content-Type: application/json' \
  -d '{"fullUrl":"https://www.google.com/search?q=java+for+beginners&sxsrf=ALeKk0008eRMwv-UE7OGI9e4sfLHt-wV7g%3A1629193315095&ei=Y4QbYaymBbSC9u8P-OqQ4A0&oq=Java&gs_lcp=Cgxnd3Mtd2l6LXNlcnAQARgAMgQIIxAnMgQIIxAnMgQIIxAnMgUILhCABDIFCAAQgAQyBQgAEIAEMgUIABCABDIFCAAQgAQyBQgAEIAEMggIABCABBCLAzoHCCMQsAMQJzoHCAAQRxCwAzoQCC4QxwEQ0QMQyAMQsAMQQzoKCC4QyAMQsAMQQzoLCC4QgAQQxwEQrwE6DgguEIAEEMcBEK8BEIsDOgsILhCABBDHARCjAjoICC4QgAQQiwM6BwgjEOoCECc6CggjEOoCECcQiwM6BQgAEMsBOggIABDLARCLAzoHCAAQChDLAToFCC4QywE6DgguEIAEEMcBEKMCEIsDSgUIOBIBMUoECEEYAFCmIFjKsAFgl70BaBFwAngBgAGfAogBiBaSAQYwLjE0LjOYAQCgAQGwAQrIAQy4AQPAAQE&sclient=gws-wiz-serp"}'
```

###### Note:
>The is URL validation, the API works only with valid HTTP or HTTPS Urls. In case that you will provide malformed URL in the request, it will return `400 Bad Request` error with response body containing a JSON object in the following format

```json
{
  "field":"fullUrl",
  "value":"<Malformed Url provided in the request>",
  "message":"<Exception message>"
}
```

#### cURL for health checks

```shell script
curl -X GET   http://localhost:8080/actuator/health
```

## Undeploy

To undeploy the containers, you need to run the shell command below:

```shell script
docker-compose down
```

# URL Shortening Algorithm

I thought of two approaches:
1. Using a dependency from Google named Guava
2. Generating hashes for the fullUrl and storing them as key value pairs in redis cache or in mysql database
3. Performing a Base62 conversion from Base10 on the id of stored fullUrl

I tested this three approaches but in case of Guava, murmur_3_32 hash implemented in Guava was generating up to 10 characters long string, 
in case of hashes, sometimes generated hashes were longer than given URL. Another problem was the readability and hard to remember the generated hash . So, I went with the third approach. With the Base conversion approach, even the maximum value of Long produces 10 characters which is still somewhat easy to remember.

>The implemented URL Shortening Algorithm was based on MADRAS MEDLEY blog article with title [CONVERTING FROM BASE10 TO BASE62](https://madrasmedley.blogspot.com/2018/07/converting-from-base10-to-base62.html) 
