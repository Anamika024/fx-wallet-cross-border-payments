FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY backend/pom.xml backend/pom.xml
COPY backend/src backend/src
RUN mvn -pl backend -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/backend/target/fx-wallet-backend-0.1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
