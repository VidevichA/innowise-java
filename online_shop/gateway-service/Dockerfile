FROM openjdk:21-oracle

WORKDIR /gateway-service

COPY target/*.jar /gateway-service/gateway-service.jar

CMD ["java", "-jar", "gateway-service.jar"]
