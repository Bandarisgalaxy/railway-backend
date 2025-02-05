# Use a minimal base image with Java 21
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file (Make sure you run 'mvn package' before building the Docker image)
COPY target/Farmer_calculator-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port (Ensure it matches your application.properties)
EXPOSE 8000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
# Use a minimal base image with Java 21
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file (Make sure you run 'mvn package' before building the Docker image)
COPY target/Farmer_calculator-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port (Ensure it matches your application.properties)
EXPOSE 8000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
