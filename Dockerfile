# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-24 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:24-jdk-slim
WORKDIR /app
COPY --from=build /app/target/salaryAquittance-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
