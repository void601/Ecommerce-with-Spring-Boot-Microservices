FROM openjdk:17-jdk-slim
ADD target/inventory-service.jar inventory-service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "inventory-service.jar"]
