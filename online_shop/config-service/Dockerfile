FROM openjdk:21-oracle

WORKDIR /config-service

COPY target/*.jar /config-service/config-service.jar

CMD ["java", "-jar", "config-service.jar"]
