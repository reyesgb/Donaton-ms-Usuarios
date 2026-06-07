FROM eclipse-temurin:17-jdk-alpine

# Exponemos el puerto interno (asumiendo que es el 8080 por defecto de Spring Boot)
EXPOSE 8080

# Copiamos la wallet (Si no usas la wallet en este MS, puedes borrar esta línea)
COPY Wallet_donatonBD /app/wallet

# Copiamos el archivo .jar compilado
COPY target/*.jar app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]