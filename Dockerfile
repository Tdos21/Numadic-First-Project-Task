# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.war app.war

# Optional: expose port used by Spring Boot
EXPOSE 9093

ENTRYPOINT ["java", "-jar", "app.war"]
 