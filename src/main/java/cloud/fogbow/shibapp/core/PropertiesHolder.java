package cloud.fogbow.shibapp.core;

import cloud.fogbow.shibapp.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.shibapp.constants.ConfigurationPropertyKeys;
import cloud.fogbow.shibapp.constants.Messages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.PropertyException;

public class PropertiesHolder {
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
		String httpPortStr = properties.getProperty(ConfigurationPropertyKeys.SHIB_HTTP_PORT_CONF_KEY);
		int port = httpPortStr == null ? ConfigurationPropertyDefaults.DEFAULT_HTTP_PORT : Integer.parseInt(httpPortStr);
		return port;
	}
	
	public static String getDashboardUrl() {
		return properties.getProperty(ConfigurationPropertyKeys.FOGBOW_GUI_URL_CONF_KEY);
	}
	
	public static String getAsPublicKey() {
		return properties.getProperty(ConfigurationPropertyKeys.AS_PUBLIC_KEY_PATH_CONF_KEY);
	}
	
	public static String getShibPrivateKey() {
		return properties.getProperty(ConfigurationPropertyKeys.SHIB_PRIVATE_KEY_PATH_CONF_KEY);
	}
	
	public static String getShibIp() {
		return properties.getProperty(ConfigurationPropertyKeys.SERVICE_PROVIDER_MACHINE_IP_CONF_KEY);
	}	
	
}
