FROM openjdk:11-jdk-slim-buster AS jar-builder
RUN apt update && \
    apt install git -y && \
    git clone https://github.com/Judge-Girl/Judge-Girl.git && \
    cd Judge-Girl && \
    git submodule update --recursive --init
WORKDIR Judge-Girl
COPY secret/ .
RUN ./mvnw package

FROM openjdk:11-jre-slim-buster
COPY --from=jar-builder target/*.jar /app.jar
EXPOSE 80
CMD ["java", "-jar", "/app.jar"]
