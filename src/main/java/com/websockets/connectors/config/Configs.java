package com.websockets.connectors.config;

import java.util.Map;

public class Configs {
	private Map<String, String> properties;

	private Configs() {
	}

	private static Configs configs;

	public static Configs getInstance() {
		if (configs == null) {
			configs = new Configs();
		}
		return configs;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

}
