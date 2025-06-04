# Używamy lekkiego obrazu OpenJDK 21
FROM openjdk:21-jdk-slim

# Port, na którym aplikacja będzie nasłuchiwać
EXPOSE 8080

# Zmienna określa nazwę pliku JAR wygenerowanego przez Maven
ARG JAR_FILE=target/surowce-app-0.0.1-SNAPSHOT.jar

# Kopiujemy zbudowany JAR do obrazu
COPY ${JAR_FILE} /app.jar

# Uruchamiamy aplikację
ENTRYPOINT ["java", "-jar", "/app.jar"]
