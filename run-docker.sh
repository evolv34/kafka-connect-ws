#!/bin/bash

./gradlew clean build 

docker rm -f $(docker ps -aq)

docker-compose build

docker-compose up -d