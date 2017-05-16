package com.websockets.producer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class KafkaProducerWebsocket {

	public static List<String> messages = Collections.synchronizedList(new LinkedList<String>());

	public void send(String message) {
		messages.add(message);
	}
}