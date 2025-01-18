FROM eclipse-temurin:23-jdk-alpine

VOLUME /tmp

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем все исходные файлы
COPY . /app

RUN chmod +x ./mvnw

# Собираем проект
RUN ./mvnw clean package -DskipTests

# Экспозиция порта для приложения (если нужно)
EXPOSE 8080

# Устанавливаем ENTRYPOINT для запуска приложения
ENTRYPOINT ["java", "-jar", "target/shop-0.0.1-SNAPSHOT.jar"]
