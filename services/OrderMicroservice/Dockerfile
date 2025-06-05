FROM openjdk:17-jdk-slim
ADD target/order-service.jar order-service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "order-service.jar"]
