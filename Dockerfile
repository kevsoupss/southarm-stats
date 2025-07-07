FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /target/southarmstats-0.0.1-SNAPSHOT.jar southarmstats.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "southarmstats.jar"]