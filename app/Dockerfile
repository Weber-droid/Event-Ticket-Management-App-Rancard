FROM openjdk:17-jdk-slim

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw package -DskipTests

CMD ["java", "-jar", "target/ticketing-0.0.1-SNAPSHOT.jar"]
