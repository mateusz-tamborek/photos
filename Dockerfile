# build stage
FROM gradle:jdk AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean bootJar

# deploy stage
FROM openjdk:8-jdk

ENV SPRING_PROFILE="docker"
ENV APP_NAME="photo-gallery"

COPY --from=build /home/gradle/src/build/libs/${APP_NAME}*-*.jar /${APP_NAME}.jar

EXPOSE 8080
CMD java -server \
    -Dspring.profiles.active=${SPRING_PROFILE} \
    -jar /${APP_NAME}.jar
