FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:latest
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/chat.jar chat.jar
ENTRYPOINT java -jar $JAVA_OPTS chat.jar