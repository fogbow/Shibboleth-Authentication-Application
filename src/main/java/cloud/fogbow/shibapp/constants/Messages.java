package cloud.fogbow.shibapp.constants;

public class Messages {

    public static class Info {
        public static final String REDIRECTING_REQUEST_S_TO_S = "Redirecting request: [%s] to: %s.";
        public static final String STARTING_REQUEST_S = "Starting the processing of request: [%s].";
    }

    public static class Exception {
        public static final String INEXISTENT_CLIENT_URL = "Client URL not especified in the properties files.";
        public static final String INEXISTENT_PRIVATE_KEY_FILE = "Shibboleth App private key not especified in the properties file.";
        public static final String INEXISTENT_PUBLIC_KEY_FILE = "AS public key not especified in the properties file.";
        public static final String INEXISTENT_SHIB_APP_IP = "Shibboleth App host IP not especified in the properties files.";
    }

    public static class Warn {
        public static final String UNABLE_TO_RETRIEVE_ISSUER = "Unable to retrieve SAML's issuer from the assertion.";
    }

    public static class Fatal {
        public static final String UNABLE_TO_GET_PROPERTIES_S = "Unable to get properties from file: %s.";
        public static final String UNABLE_TO_INITIALIZE_SAML_ASSERTION_HOLDER = "Unable to initialize SAML assertion holder.";
        public static final String UNABLE_TO_START_HTTP_SERVER = "Unable to start HTTP server.";
    }

    public static class Error {
        public static final String INEXISTENT_ISSUER = "No issuer found in the assertion response.";
        public static final String NULL_RESPONSE = "Null assertionResponse received.";
        public static final String UNABLE_TO_ENCRYPT_MESSAGE_WITH_AES = "Unable to encryp the message using AES.";
        public static final String UNABLE_TO_ENCRYPT_MESSAGE_WITH_RSA = "Unable to encryp the message using RSA.";
        public static final String UNABLE_TO_GET_ASSERTIONS = "Unable to get assertions.";
        public static final String UNABLE_TO_GET_PRIVATE_KEY = "Unable to get private key.";
        public static final String UNABLE_TO_GET_PUBLIC_KEY = "Unable to get public key.";
        public static final String UNABLE_TO_PERFORM_HTTP_REQUEST = "Unable to perform HTTP request.";
        public static final String UNABLE_TO_SIGN_MESSAGE = "Unable to sign the message.";
        public static final String UNEXPECTED_STATUS_CODE_D_S = "Unexpected status code: %d; error message: %s.";
    }
}
