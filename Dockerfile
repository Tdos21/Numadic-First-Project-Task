# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . .

# Build the WAR file
RUN mvn clean package

# Stage 2: Run
FROM openjdk:21-jdk-slim

# Copy the built WAR from previous stage
COPY --from=builder /app/target/VehicleTracking02-0.0.1.war /app.war

ENTRYPOINT ["java", "-jar", "/app.war"]
