# Sử dụng image Java chính thức
FROM openjdk:21-jdk-slim

ARG FILE_JAR=target/beautytouch-0.0.1-SNAPSHOT.jar
ADD ${FILE_JAR} beauty.jar
ENTRYPOINT ["java","-jar","beauty.jar"]
EXPOSE 8080
