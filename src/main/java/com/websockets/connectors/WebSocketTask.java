package com.websockets.connectors;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.log4j.Logger;

import com.websockets.handler.KafkaWebSocketServer;

public class WebSocketTask extends SourceTask {

	private Logger log = Logger.getLogger(getClass().getName());
	private KafkaWebSocketServer kafkaServer;

	@Override
	public String version() {
		return null;
	}

	@Override
	public void start(Map<String, String> props) {
		try {
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
		return null;
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
