/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/

package com.websockets.handler;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class KafkaWebSocketServer extends WebSocketServer {

	public static ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();

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
		messageQueue.add(message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		conn.close();
	}
}