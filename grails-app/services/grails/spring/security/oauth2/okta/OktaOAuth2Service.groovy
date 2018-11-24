package grails.spring.security.oauth2.okta

import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Response
import com.github.scribejava.core.model.Verb
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugin.springsecurity.oauth2.service.OAuth2AbstractProviderService
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken
import grails.util.Holders

@Transactional
class OktaOAuth2Service extends OAuth2AbstractProviderService  {

    def serviceMethod() {

    }
    String userInfoUrl

    OktaOAuth2Service() {

        this.userInfoUrl = Holders.getGrailsApplication().config.getProperty('grails.plugin.springsecurity.oauth2.providers.okta.userInfoUrl')
        log.info "Okta userInfoUrl = " + this.userInfoUrl


        if (!this.userInfoUrl || this.userInfoUrl == null) {
            throw new MissingPropertyException("Please define userInfoUrl for Okta OAuth2 ('grails.plugin.springsecurity.oauth2.providers.okta.userInfoUrl')");
        }
    }
/**
 * @return The ProviderID
 */

    @Override
    String getProviderID() {
        'okta'
    }

    /**
     * A scribeJava API class to use for the oAuth Request or any other class that extends the @link{DefaultApi20}* @return The ApiClass that is to use
     */
    @Override
    Class<? extends DefaultApi20> getApiClass() {
        OktaApiService.class
    }

    /**
     * Path to the OAuthScope that is returning the UserIdentifier
     * i.e 'https://graph.facebook.com/me' for facebook
     */
    @Override
    String getProfileScope() {
        this.userInfoUrl
    }

    /**
     * The scopes that are at least required by the oauth2 provider, to get an email-address
     * Additional scopes can be configured in the application.yml
     */
    @Override
    String getScopes() {
        'openid profile email'
    }

    /**
     * Get separator string for concatenating the mandatory and the optional scopes
     */
    @Override
    String getScopeSeparator() {
        ''
    }

    Response getResponse(OAuth2AccessToken accessToken) {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.POST, getProfileScope(), authService)
        String header =  "Bearer " + accessToken.getAccessToken()
        oAuthRequest.addHeader("Authorization",  header);
        oAuthRequest.send()
    }

    /**
     * @param accessToken
     * @return
     */
    @Override
    OAuth2SpringToken createSpringAuthToken(OAuth2AccessToken accessToken) {
        def user
        def response = getResponse(accessToken)
        try {
            log.debug("JSON response body: {}", accessToken.rawResponse)
            String responseBody = response.getBody();
            user = JSON.parse(responseBody)
        }
        catch (Exception e) {
            log.error("Error parsing response from {}. Response:\n{}", providerID, response.body)
            throw new OAuth2Exception("Error parsing response from " + providerID, e)
        }

        if (user && !user['email']) {
            log.error("No user email from {}. Response was:\n{}", providerID, response.body)
            throw new OAuth2Exception("No user id from " + providerID)
        }

        new OktaOauth2SpringToken(accessToken, (String) user['email'], providerID)
    }
}
