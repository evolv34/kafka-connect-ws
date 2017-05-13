package com.websockets.connectors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import com.websockets.connectors.config.Configs;

public class WebSocketSourceConnector extends SourceConnector {

	private Map<String, String> config;

	@Override
	public String version() {
		return Version.getVersion();
	}

	@Override
	public void start(Map<String, String> props) {
		config = props;
		Configs.getInstance().setProperties(props);
	}

	public Class<? extends Task> taskClass() {
		return WebSocketTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		final List<Map<String, String>> configs = new LinkedList<>();

		Map<String, String> taskProps = new HashMap<>();
		taskProps.putAll(config);
		for (int i = 0; i < maxTasks; i++) {
			configs.add(taskProps);
		}
		return configs;
	}

	@Override
	public void stop() {
	}

	@Override
	public ConfigDef config() {
		final ConfigDef configDef = new ConfigDef();

		return configDef;

	}

}
