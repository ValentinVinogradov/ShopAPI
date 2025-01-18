FROM eclipse-temurin:23-jdk-alpine

VOLUME /tmp

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем pom.xml для загрузки зависимостей
COPY pom.xml /app
RUN ./mvnw dependency:go-offline

# Копируем все исходные файлы
COPY . /app

# Даем права на выполнение скрипта mvnw
RUN chmod +x ./mvnw

# Собираем проект
RUN ./mvnw clean package -DskipTests

# Копируем собранный JAR файл
COPY target/shop-0.0.1-SNAPSHOT.jar app.jar

# Экспозиция порта для приложения (если нужно)
EXPOSE 8080

# Устанавливаем ENTRYPOINT для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
