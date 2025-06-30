# Use OpenJDK base image
FROM eclipse-temurin:21-jdk

# Set app directory
WORKDIR /app

# Copy built JAR into container
COPY target/event-tracker-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
