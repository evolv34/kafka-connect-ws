package com.websockets.handler;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.websockets.producer.KafkaProducerWebsocket;

public class KafkaWebSocketServer extends WebSocketServer {

	private KafkaProducerWebsocket kafkaProducer = new KafkaProducerWebsocket();

	public KafkaWebSocketServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public KafkaWebSocketServer(InetSocketAddress address) throws UnknownHostException {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {

	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		conn.close();
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		kafkaProducer.send(message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		conn.close();
	}
}