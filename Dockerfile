#  Build da aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

# gera o JAR
COPY src ./src
RUN mvn clean package -DskipTests

#  Imagem  para execução
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia o JAR gerado
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta
EXPOSE 8080

#  iniciar o Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]