package grails.spring.security.oauth2.okta;

import com.github.scribejava.core.model.OAuth2AccessToken;
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken;

public class OktaOauth2SpringToken extends OAuth2SpringToken {


    /**
     * Initialises the token from an access token.
     *
     * @param accessToken
     * @param forceJsonExtractor
     */
    public OktaOauth2SpringToken(OAuth2AccessToken accessToken, boolean forceJsonExtractor) {
        super(accessToken, forceJsonExtractor);
    }

    /**
     * Returns the name of the OAuth provider for this token.
     */
    @Override
    public String getProviderName() {
        return null;
    }

    @Override
    public String getSocialId() {
        return null;
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
