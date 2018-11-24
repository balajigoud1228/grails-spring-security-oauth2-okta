package grails.spring.security.oauth2.okta

class User {

    static constraints = {
    }
    static hasMany = [oAuthIDs: OAuthID]
}
