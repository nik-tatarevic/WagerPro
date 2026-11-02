# ==========================================
# Stage 1: Build the application JAR
# ==========================================
FROM eclipse-temurin:21-jdk-jammy AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and configuration files first (optimizes layer caching)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies offline to cache them and speed up subsequent builds
RUN ./mvnw dependency:go-offline

# Copy the source code and build the executable JAR (skipping tests for speed)
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==========================================
# Stage 2: Run the application
# ==========================================
FROM eclipse-temurin:21-jre-jammy

# Set the working directory for the runtime container
WORKDIR /app

# Copy only the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "app.jar"]