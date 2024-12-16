FROM eclipse-temurin:23-jdk-alpine

VOLUME /tmp

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем исходные файлы в контейнер
COPY . /app

RUN chmod +x ./mvnw

# Скачать зависимости и собрать JAR файл
RUN ./mvnw clean package

# Копируем собранный JAR файл в образ (опционально, если нужно использовать в другом месте)
COPY target/shop-0.0.1-SNAPSHOT.jar app.jar

# Экспозиция порта для приложения (если нужно)
EXPOSE 8080

# Устанавливаем ENTRYPOINT для запуска приложения (опционально)
ENTRYPOINT ["java", "-jar", "app.jar"]
