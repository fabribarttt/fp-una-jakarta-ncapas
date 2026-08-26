# ETAPA 1: Compilar el proyecto con Maven sobre JDK 25
FROM maven:3.9-eclipse-temurin-25-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Servidor Tomcat 11 con JDK 25
FROM tomcat:jdk25-temurin
WORKDIR /usr/local/tomcat/webapps/

# Limpiar aplicaciones por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el .war generado
COPY --from=builder /app/target/*.war ./ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]