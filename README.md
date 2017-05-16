# kafka-connect-ws

Kafka-connect-ws is a Kafka Source Connector to post data to kafka over websockets.

# Steps
	1. build the jar ./gradlew clean build.
	2. copy the jar to /share/java/<foldername>
	3. use the sample properties file in properties/kafka-connect-ws.
	4. run connect.
	
#### To Run dockerized version
	1. start docker-machine
	2. run run-docker.sh

it builds the jar and builds the docker images.
	

# Limitations

Currently kafka-connect-ws supports only String messages.

# Work in Progress

Kafka-connect-ws will support avro and other message types and keyed messages in upcoming versions.



 