FROM openjdk:17-jdk-slim
ADD target/eureka-server.jar eureka-server.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
