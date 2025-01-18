# Sử dụng image Java chính thức
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file JAR từ môi trường build vào container
COPY target/*.jar app.jar

EXPOSE 8080

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]