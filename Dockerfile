FROM maven:3.8-jdk-11 AS build
COPY src /usr/src/rest-application/src
COPY pom.xml /usr/src/rest-application
RUN mvn -f /usr/src/rest-application/pom.xml clean package

FROM openjdk:11
COPY --from=build /usr/src/rest-application/target/rest-application.jar rest-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "rest-application.jar"]