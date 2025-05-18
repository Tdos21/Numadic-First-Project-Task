# Use a base image
FROM openjdk:21-jdk-slim

# Define the build argument
ARG JAR_FILE=target/*.jar

# Copy the application JAR file to the container
COPY ${JAR_FILE} VehicleTracking02-0.0.1.war

# Define the entry point
ENTRYPOINT ["java", "-jar", "/VehicleTracking02-0.0.1.war"]
