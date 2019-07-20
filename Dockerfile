# build stage
FROM gradle:jdk AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean bootJar

# deploy stage
FROM openjdk:8-jdk-alpine

ENV SPRING_PROFILE="docker"
ENV APP_NAME="photos"

COPY --from=build /home/gradle/src/build/libs/${APP_NAME}*-*.jar /app.jar
ENTRYPOINT java -server \
    -Dspring.profiles.active=${SPRING_PROFILE} \
    -jar /app.jar