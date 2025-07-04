FROM openjdk:21-jdk

WORKDIR /app

COPY target/*.jar /app/demo-3.4.1.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo-3.4.1.jar"]
