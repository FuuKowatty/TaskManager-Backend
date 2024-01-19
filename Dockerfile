FROM eclipse-temurin:17-jre-alpine
COPY /target/taskmanager.jar /taskmanager.jar
ENTRYPOINT ["java","-jar","/taskmanager.jar"]