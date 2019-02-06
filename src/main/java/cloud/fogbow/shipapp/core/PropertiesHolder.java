package cloud.fogbow.shipapp.core;

import cloud.fogbow.shipapp.core.constants.DefaultConfigurationConstants;
import cloud.fogbow.shipapp.core.constants.Messages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.PropertyException;

public class PropertiesHolder {
	public static final String FOGBOW_GUI_URL_CONF = "fogbow_gui_url";
	public static final String AS_PUBLIC_KEY_PATH_CONF = "as_public_key_path";
	public static final String SHIP_PRIVATE_KEY_PATH_CONF = "ship_private_key_path";
	private static final String SHIB_HTTP_PORT_CONF = "shib_http_port";
	public static final String SERVICE_PROVIDER_MACHINE_IP_CONF = "service_provider_machine_ip";
	
	public static Properties properties;
	
	public static void init(String propertiesPath) throws IOException, PropertyException {
		properties = new Properties();
		File file = new File(propertiesPath);
		FileInputStream fileInputStream = new FileInputStream(file);
		properties.load(fileInputStream);
		
		checkProperties(properties);
	}
	
	// TODO remove this. This is used only for tests because we had problem with Power Mockito
	public static void setProperties(Properties properties) {
		PropertiesHolder.properties = properties;
	}

	protected static void checkProperties(Properties properties) throws PropertyException {
		if (getShibPrivateKey() == null || getShibPrivateKey().isEmpty()) {
			throw new PropertyException(Messages.Exception.INEXISTENT_PRIVATE_KEY_FILE);
		}
		
		if (getAsPublicKey() == null || getAsPublicKey().isEmpty()) {
			throw new PropertyException(Messages.Exception.INEXISTENT_PUBLIC_KEY_FILE);
		}
		
		if (getDashboardUrl() == null || getDashboardUrl().isEmpty()) {
			throw new PropertyException(Messages.Exception.INEXISTENT_CLIENT_URL);
		}
		
		if (getShibIp() == null || getShibIp().isEmpty()) {
			throw new PropertyException(Messages.Exception.INEXISTENT_SHIB_APP_IP);
		}
	}

	public static int getShipHttpPort() {
		String httpPortStr = properties.getProperty(SHIB_HTTP_PORT_CONF);
		int port = httpPortStr == null ? DefaultConfigurationConstants.DEFAULT_HTTP_PORT : Integer.parseInt(httpPortStr);
		return port;
	}
	
	public static String getDashboardUrl() {
		return properties.getProperty(FOGBOW_GUI_URL_CONF);
	}
	
	public static String getAsPublicKey() {
		return properties.getProperty(AS_PUBLIC_KEY_PATH_CONF);
	}
	
	public static String getShibPrivateKey() {
		return properties.getProperty(SHIP_PRIVATE_KEY_PATH_CONF);
	}
	
	public static String getShibIp() {
		return properties.getProperty(SERVICE_PROVIDER_MACHINE_IP_CONF);
	}	
	
}
