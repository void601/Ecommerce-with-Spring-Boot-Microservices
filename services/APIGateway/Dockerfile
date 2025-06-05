FROM openjdk:17-jdk-slim
ADD target/api-gateway.jar api-gateway.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
