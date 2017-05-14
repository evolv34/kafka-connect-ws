FROM confluentinc/cp-kafka-connect:3.1.1

RUN mkdir -p usr/share/java/kafka-connect-ws

RUN mkdir -p etc/kafka-connect-ws

ADD /build/libs/kafka-connect-websocket-0.1.0.jar usr/share/java/kafka-connect-ws/kafka-connect-websocket-0.1.0.jar

ADD /properties/kafka-connect-ws/source-ws.properties etc/kafka-connect-ws/source-ws.properties

ADD /properties/connect-avro-standalone.properties etc/schema-registry/connect-avro-standalone.properties

ADD run.sh /run.sh

RUN chmod +x run.sh

EXPOSE 9093

CMD ["/run.sh"]