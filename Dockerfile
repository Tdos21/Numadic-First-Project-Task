# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:21-jdk-slim
COPY --from=builder /app/target/*.war VehicleTracking02-0.0.1.war
ENTRYPOINT ["java", "-jar", "VehicleTracking02-0.0.1.war"]
