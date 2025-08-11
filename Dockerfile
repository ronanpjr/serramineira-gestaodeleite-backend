FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests

FROM openjdk:21-jre-slim

WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/serramineira-sistemadeleite-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]