# # Use a lightweight JDK base image
# FROM eclipse-temurin:24-jdk AS builder

# # Set working directory
# WORKDIR /app

# # Copy Maven build output JAR into container
# COPY target/salaryAquittance-0.0.1-SNAPSHOT.jar app.jar

# # Expose Spring Boot's default port
# EXPOSE 8081

# # Run the JAR
# ENTRYPOINT ["java", "-jar", "app.jar"]



# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/salaryAquittance-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

