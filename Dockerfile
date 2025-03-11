# Используем образ Maven с Java 21
FROM maven:3.9-amazoncorretto-21 AS builder

# Копируем исходники
WORKDIR /app
COPY . .

# Собираем приложение
RUN mvn clean package -DskipTests

# Используем минимальный образ Java 21 для запуска
FROM amazoncorretto:21-alpine

# Создаем директорию для приложения
WORKDIR /app

# Копируем собранный jar из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

# Определяем точку входа
ENTRYPOINT ["java", "-jar", "app.jar"]