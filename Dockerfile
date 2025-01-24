FROM eclipse-temurin:23-jdk-alpine

VOLUME /tmp

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/shop-api.jar"]
