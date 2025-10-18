# BUILD STAGE
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY backend/pom.xml backend/pom.xml
COPY infra/pom.xml infra/pom.xml
COPY application/pom.xml application/pom.xml
COPY backend/src backend/src
COPY infra/src infra/src
COPY application/src application/src

RUN mvn clean package -DskipTests

# RUNTIME STAGE
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/backend/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
