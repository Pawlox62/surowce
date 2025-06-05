FROM openjdk:21-jdk-slim

EXPOSE 8080

ARG 0.0.1JAR_FILE=target/surowce--SNAPSHOT.jar

COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
