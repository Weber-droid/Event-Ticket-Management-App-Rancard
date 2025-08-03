# Start from OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Add dependencies first to leverage Docker cache
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Pre-download dependencies
RUN ./mvnw dependency:go-offline

# Copy the full source and build
COPY src src
RUN ./mvnw package -DskipTests

# Run the Spring Boot app
CMD ["java", "-jar", "target/ticketing-0.0.1-SNAPSHOT.jar"]
