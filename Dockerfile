FROM eclipse-temurin:23-jdk-alpine

VOLUME /tmp

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем все исходные файлы
COPY . /app

RUN chmod +x ./mvnw

# Собираем проект
RUN ./mvnw clean package -DskipTests

# Копируем собранный JAR файл
COPY target/shop-0.0.1-SNAPSHOT.jar app.jar

# Экспозиция порта для приложения (если нужно)
EXPOSE 8080

# Устанавливаем ENTRYPOINT для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
