From openjdk:11
ADD build/libs/url-shortener-api-0.0.1-SNAPSHOT.jar url-shortener-api.jar
EXPOSE 8080
CMD ["java","-jar","url-shortener-api.jar"]