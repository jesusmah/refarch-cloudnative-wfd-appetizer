#!/bin/bash

./gradlew clean build

cp build/libs/wfd-appetizer-0.0.1-SNAPSHOT.jar docker/app.jar

cd docker/

docker build -t wfd-appetizer .
