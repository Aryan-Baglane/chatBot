# Step 1: Build stage (Maven + Java 17)
FROM maven:3.9.6-eclipse-temurin-17 as builder

WORKDIR /app

# Copy the whole project
COPY . .

# Build your Spring Boot app
RUN mvn clean package -DskipTests

# Step 2: Runtime stage (only JDK 17, slim image)
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=builder /app/target/chatBot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
