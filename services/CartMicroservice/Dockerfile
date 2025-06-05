FROM openjdk:17-jdk-slim
ADD target/cart-service.jar cart-service.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "cart-service.jar"]
