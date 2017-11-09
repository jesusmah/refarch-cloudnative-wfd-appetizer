FROM openjdk:8-alpine

RUN apk --no-cache update \
 && apk add bash

COPY target/docker/wfd-appetizer-*.jar app.jar

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
