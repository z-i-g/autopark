FROM openjdk:17-jdk-slim-buster
COPY ../target/autopark-0.0.1-SNAPSHOT.jar app/autopark/autopark.jar
WORKDIR app/autopark
LABEL authors="Айрат"

ENTRYPOINT ["java", "-jar", "autopark.jar"]