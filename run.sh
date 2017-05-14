#!/bin/bash
#!/bin/sh

sed -i -e "s/BOOTSTRAP_SERVERS_URL/${BOOTSTRAP_SERVERS}/g" etc/kafka-connect-ws/source-ws.properties
sed -i -e "s/TOPICS/${TOPICS}/g" etc/kafka-connect-ws/source-ws.properties

sed -i -e "s/BOOTSTRAP_SERVERS_URL/${BOOTSTRAP_SERVERS}/g" etc/schema-registry/connect-avro-standalone.properties
sed -i -e "s/SCHEMA_REGISTRY_URL/"${SCHEMA_REGISTRY}"/g" etc/schema-registry/connect-avro-standalone.properties

./usr/bin/connect-standalone etc/schema-registry/connect-avro-standalone.properties etc/kafka-connect-ws/source-ws.properties