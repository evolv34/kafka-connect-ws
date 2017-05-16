package com.websockets.connectors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.log4j.Logger;

import com.websockets.connectors.config.ConnectorConstants;
import com.websockets.handler.KafkaWebSocketServer;
import com.websockets.producer.KafkaProducerWebsocket;

public class WebSocketTask extends SourceTask {

	private Logger log = Logger.getLogger(getClass().getName());
	private KafkaWebSocketServer kafkaServer;
	private static final Map<String, String> srcPartition = Collections.singletonMap("cache", null);
	private static final Map<String, Long> offset = Collections.singletonMap("offset", 0L);

	private List<String> topics = null;

	@Override
	public String version() {
		return null;
	}

	@Override
	public void start(Map<String, String> props) {

		try {
			topics = Arrays.asList(props.get(ConnectorConstants.TOPICS).split(ConnectorConstants.DELIMITER));
			int port = 9093;
			kafkaServer = new KafkaWebSocketServer(port);
			kafkaServer.start();
			log.info("WebSocket server started on port: {}" + kafkaServer.getPort());
		} catch (Exception e) {
			log.error("Could not start server ", e);
		}
	}

	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		ArrayList<SourceRecord> records = new ArrayList<>();

		List<String> messages = KafkaProducerWebsocket.messages;

		messages.forEach(message -> {
			topics.forEach(topic -> {
				records.add(new SourceRecord(srcPartition, offset, topic, null, message));
			});
		});
		KafkaProducerWebsocket.messages.clear();
		return records;
	}

	@Override
	public void stop() {
		if (kafkaServer != null) {
			try {
				kafkaServer.stop();
			} catch (IOException e) {
				log.error("Could not stop server ", e);
			} catch (InterruptedException e) {
				log.error("Could not stop server ", e);
			}
		}

	}

}
