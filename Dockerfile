# Usamos la imagen oficial de Eclipse Temurin para Java 21 con Alpine Linux
FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el JAR generado por Maven
# Importante: El nombre debe coincidir con lo que genera tu pom.xml
COPY target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]