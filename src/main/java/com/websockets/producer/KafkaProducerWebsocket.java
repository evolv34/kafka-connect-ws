package com.websockets.producer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import com.websockets.connectors.config.Configs;
import com.websockets.connectors.config.ConnectorConstants;

public class KafkaProducerWebsocket {

	private static Map<String, Object> props = new HashMap<String, Object>();
	private static Producer<String, String> producer = null;
	private static Map<String, String> properties = Configs.getInstance().getProperties();
	private Logger log = Logger.getLogger(getClass().getName());

	static {
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", "0");
		props.put("batch.size", "16384");
		props.put("linger.ms", "1");
		props.put("buffer.memory", "33554432");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producer = new KafkaProducer<String, String>(props);
	}

	public void send(String message) {
		List<String> topics = Arrays.asList(properties.get(ConnectorConstants.TOPICS).split(","));

		topics.parallelStream().forEach(topic -> {
			log.info("sending message to topic " + topic);
			producer.send(new ProducerRecord<String, String>(topic, message));
		});
	}
}