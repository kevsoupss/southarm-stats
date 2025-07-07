FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the application
CMD ["java", "-jar", "target/southarmstats-0.01-SNAPSHOT.jar"]