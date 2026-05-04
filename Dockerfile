# --- Build-vaihe ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Kopioidaan pom ja ladataan riippuvuudet
COPY pom.xml .
RUN mvn dependency:go-offline

# Kopioidaan lähdekoodi ja rakennetaan sovellus
COPY src ./src
RUN mvn clean package -DskipTests

# --- Runtime-vaihe ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# Kopioidaan rakennettu jar build-vaiheesta
COPY --from=build /app/target/*.jar app.jar

# Spring Boot käyttää porttia 8080
EXPOSE 8080

# Käynnistetään sovellus
ENTRYPOINT ["java", "-jar", "app.jar"]