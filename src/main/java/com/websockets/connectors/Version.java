package com.websockets.connectors;

import java.util.Properties;

public class Version {

	private static String version = "unknown";

	static {
		try {
			Properties props = new Properties();
			props.load(Version.class.getResourceAsStream("/kafka-connect-ws.properties"));
			version = props.getProperty("version", version).trim();
		} catch (Exception e) {
		}
	}

	public static String getVersion() {
		return version;
	}

}
