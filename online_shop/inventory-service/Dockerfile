FROM openjdk:21-oracle

WORKDIR /inventory-service

COPY target/*.jar /inventory-service/inventory-service.jar

CMD ["java", "-jar", "inventory-service.jar"]
