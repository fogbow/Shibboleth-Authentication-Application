package cloud.fogbow.shipapp.api.http;

import java.util.Random;

import cloud.fogbow.shipapp.core.ShibController;
import cloud.fogbow.shipapp.core.constants.Messages;
import cloud.fogbow.shipapp.core.saml.SAMLAssertionHolder;
import org.apache.log4j.Logger;
import org.restlet.engine.adapter.HttpRequest;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ShibResource extends ServerResource {
		
	private static final Logger LOGGER = Logger.getLogger(ShibResource.class);
	
	@Get
	public String fetch() throws Exception {
		String requestIdentifier = getRequestIdentifier();
		HttpRequest httpRequest = (HttpRequest) getRequest();
		
		LOGGER.info(String.format(Messages.Info.STARTING_REQUEST_S, requestIdentifier));
		String assertionUrl = getAssertionUrl(httpRequest);
		
		ShibController shibController = new ShibController();
		
		String asToken = shibController.createToken(assertionUrl);
		String aesKey = shibController.createAESkey();
		String asTokenEncryptedAes = shibController.encrypAESAsToken(asToken, aesKey);
		String keyEncrypted = shibController.encrypRSAKey(aesKey);
		String keySigned = shibController.signKey(aesKey);
		
		String targetURL = shibController.createTargetUrl(asTokenEncryptedAes, keyEncrypted, keySigned);
		LOGGER.info(String.format(Messages.Info.REDIRECTING_REQUEST_S_TO_S, requestIdentifier, targetURL));
		getResponse().redirectPermanent(targetURL);
		
		return new String();
	}

	private String getAssertionUrl(HttpRequest httpRequest) {
		return httpRequest.getHeaders().getFirstValue(SAMLAssertionHolder.SHIB_ASSERTION_HEADER);
	}

	private String getRequestIdentifier() {
		return String.valueOf(new Random().nextInt());
	}

}
