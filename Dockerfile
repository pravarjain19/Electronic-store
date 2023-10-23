FROM openjdk:18-alpine
LABEL authors="pravar"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar


ENTRYPOINT ["java", "-jar" , "/app.jar"]