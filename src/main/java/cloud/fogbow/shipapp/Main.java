package cloud.fogbow.shipapp;

import cloud.fogbow.common.util.HomeDir;
import cloud.fogbow.shipapp.core.PropertiesHolder;
import cloud.fogbow.shipapp.core.constants.Messages;
import cloud.fogbow.shipapp.core.constants.SystemConstants;
import cloud.fogbow.shipapp.core.saml.SAMLAssertionHolder;
import org.apache.log4j.Logger;
import org.opensaml.xml.ConfigurationException;
import org.restlet.Component;
import org.restlet.data.Protocol;

public class Main {
	
	private static final Logger LOGGER = Logger.getLogger(Main.class);
	private static final int EXIT = 1;
	
	public static void main(String[] args) {
		String propertiesPath = (args.length >= 1 ? args[0] : (HomeDir.getPath() + SystemConstants.CONF_FILE_NAME));
		
		initProperties(propertiesPath);		
		initSAMLAssertion();		
		startHTTPServer();
	}

	private static void startHTTPServer() {
		try {
			Component http = new Component();		
			int httpPort = PropertiesHolder.getShipHttpPort();
			http.getServers().add(Protocol.HTTP, httpPort);
			http.getDefaultHost().attach(new ShibApplication());
			http.start();
		} catch (Exception e) {
			LOGGER.fatal(Messages.Fatal.UNABLE_TO_START_HTTP_SERVER, e);
			System.exit(EXIT);
		}
	}

	private static void initProperties(String propertiesPath) {
		try {
			PropertiesHolder.init(propertiesPath);
		} catch (Exception e) {
			LOGGER.fatal(String.format(Messages.Fatal.UNABLE_TO_GET_PROPERTIES_S, propertiesPath), e);
			System.exit(EXIT);
		}
	}

	private static void initSAMLAssertion() {
		try {
			SAMLAssertionHolder.init();
		} catch (ConfigurationException e) {
			LOGGER.fatal(Messages.Fatal.UNABLE_TO_INITIALIZE_SAML_ASSERTION_HOLDER, e);
			System.exit(EXIT);
		}
	}

}
