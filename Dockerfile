FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/motunui.jar ./motunui.jar
COPY orders orders