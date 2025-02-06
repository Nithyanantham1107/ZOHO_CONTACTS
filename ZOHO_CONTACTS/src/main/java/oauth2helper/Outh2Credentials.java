package oauth2helper;

public class Outh2Credentials {

	private static final String clientID = "153306987293-hbjfah0drjm00jubhiobf4053fe7o7lk.apps.googleusercontent.com";
	private static final String clientSecret = "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";
	private static final String redirectURI = "http://localhost:8080/oauthredirect";
	private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.profile  https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/contacts";
	private static final String AUTHORIZATION_URL = "https://accounts.google.com/o/oauth2/v2/auth";
	private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

	public static String getClientID() {
		return clientID;
	}

	public static String getClientSecret() {
		return clientSecret;
	}

	public static String getRedirectURI() {
		return redirectURI;
	}

	public static String getScope() {
		return SCOPE;
	}

	public static String getAuthorizationURL() {
		return AUTHORIZATION_URL;
	}

	public static String getToken() {
		return TOKEN_URL;
	}

}
