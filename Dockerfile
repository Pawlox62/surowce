# Używamy obrazu z OpenJDK 21 (slim)
FROM openjdk:21-jdk-slim

# Ustawiamy port, na którym aplikacja nasłuchuje
EXPOSE 8080

ARG JAR_FILE=target/surowce-0.0.1-SNAPSHOT.jar

# Kopiujemy zbudowany JAR do katalogu głównego obrazu jako /app.jar
COPY ${JAR_FILE} /app.jar

# Punkt wejścia: uruchamiamy aplikację
ENTRYPOINT ["java","-jar","/app.jar"]
