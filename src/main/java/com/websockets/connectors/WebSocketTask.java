/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/

package com.websockets.connectors;

import com.websockets.connectors.config.ConnectorConstants;
import com.websockets.handler.KafkaWebSocketServer;
import com.websockets.utils.Zookeeper;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class WebSocketTask extends SourceTask {

    private Logger log = Logger.getLogger(getClass().getName());
    private KafkaWebSocketServer kafkaServer;
    private static final Map<String, String> srcPartition = Collections.singletonMap("cache", null);
    private static final Map<String, Long> offset = Collections.singletonMap("offset", 0L);
    private Zookeeper zookeeper = new Zookeeper("localhost", 2181);

    private List<String> topics = null;

    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> props) {

        try {
            topics = Arrays.asList(props.get(ConnectorConstants.TOPICS).split(ConnectorConstants.DELIMITER));
            int port = zookeeper.getPort();
            kafkaServer = new KafkaWebSocketServer(port);
            kafkaServer.start();

            log.info("WebSocket server started on port: {}" + kafkaServer.getPort());

            zookeeper.register();
        } catch (Exception e) {
            log.error("Could not start server ", e);
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        List<SourceRecord> records = new ArrayList<SourceRecord>();
        String message = KafkaWebSocketServer.messageQueue.poll();
        if (message != null) {
            topics.forEach(topic -> {
                records.add(new SourceRecord(srcPartition, offset, topic, Schema.STRING_SCHEMA, message));
            });
        }
        return records;
    }

    @Override
    public void stop() {
        if (kafkaServer != null) {
            try {
                kafkaServer.stop();
                zookeeper.unregister();
            } catch (IOException e) {
                log.error("Could not stop server ", e);
            } catch (InterruptedException e) {
                log.error("Could not stop server ", e);
            }
        }

    }
}
