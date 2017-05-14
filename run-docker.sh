#!/bin/bash

./gradlew clean build 

docker build -t evolv/kakfa-websocket .

docker rm -f $(docker ps -aq)

docker-compose -f docker-compose-kafka.yml up