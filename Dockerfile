#  Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

#  JAR
COPY src ./src
RUN mvn clean package -DskipTests

#  Imagem
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia do JAR
COPY --from=builder /app/target/*.jar app.jar

# porta
EXPOSE 8080

#  iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]