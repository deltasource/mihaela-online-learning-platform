FROM openjdk:21-jdk

WORKDIR /app

COPY target/*.jar /app/elearning.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "elearning.jar"]
