package grails.spring.security.oauth2.okta

import com.github.scribejava.core.builder.api.DefaultApi20
import grails.gorm.transactions.Transactional
import grails.util.Holders

@Transactional
class OktaApiService extends DefaultApi20{

    private static final String AUTHORIZE_URL = "/oauth2/v1/authorize";
    private static final String REQUEST_TOKEN_URL = "/oauth2/v1/token";

    private String authorizeUrl;
    private String tokenUrl;

    protected OktaApiService() {

        this.authorizeUrl = Holders.getGrailsApplication().config.getProperty('grails.plugin.springsecurity.oauth2.providers.okta.authorizeUrl')
        log.info "Okta authorizeUrl = " + this.authorizeUrl

        if (!this.authorizeUrl || this.authorizeUrl == null) {
            throw new MissingPropertyException("Please define authorizeUrl for Okta OAuth2 ('grails.plugin.springsecurity.oauth2.providers.okta.authorizeUrl')");
        }

        this.tokenUrl = Holders.getGrailsApplication().config.getProperty('grails.plugin.springsecurity.oauth2.providers.okta.tokenUrl')
        log.info "Okta tokenUrl = " + this.tokenUrl

        if (!this.tokenUrl || this.tokenUrl == null) {
            throw new MissingPropertyException("Please define tokenUrl for Okta OAuth2 ('grails.plugin.springsecurity.oauth2.providers.okta.tokenUrl')");
        }
    }

    private static class InstanceHolder {
        private static final OktaApiService INSTANCE = new OktaApiService();
    }
/**
 * Returns the URL that receives the access token requests.
 *
 * @return access token URL
 */

    @Override
    String getAccessTokenEndpoint() {
        this.tokenUrl
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        this.authorizeUrl
    }
}
