FROM openjdk:11-jdk-slim-buster AS jar-builder
COPY /tmp/secret/* ./
RUN apt update && apt install git -y && \
    git submodule update --recursive --init && \
    ./mvnw package

FROM openjdk:11-jre-slim-buster
COPY --from=jar-builder target/*.jar /app.jar
RUN apt update -y && apt install netcat -y && rm -rf /var/lib/apt/lists/*
EXPOSE 80
CMD ["java", "-jar", "/app.jar"]
