# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Expose port (Render will override this with PORT env var)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/*.jar"]