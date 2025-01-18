# Sử dụng image Java chính thức
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app
COPY . .
RUN mvn clean package

# Sao chép file JAR từ môi trường build vào container
COPY target/*.beautytouch-0.0.1-SNAPSHOT.jar beautytouch-0.0.1-SNAPSHOT.jar

EXPOSE 8080

# Khởi động ứng dụng
ENTRYPOINT ["java", "-jar", "beautytouch-0.0.1-SNAPSHOT.jar"]