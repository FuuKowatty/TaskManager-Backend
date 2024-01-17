FROM openjdk:17.0.1-jdk-slim as build

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN ./mvnw -B dependency:go-offline

COPY src src

RUN ./mvnw -B package

FROM openjdk:17.0.1-jdk-slim

COPY --from=build target/TaskManager-Backend-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "TaskManager-Backend-0.0.1-SNAPSHOT.jar"]
