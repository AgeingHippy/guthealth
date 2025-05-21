package com.ageinghippy.factory;

import com.ageinghippy.service.GitHubOAuth2Service;
import com.ageinghippy.service.GoogleOAuth2Service;
import com.ageinghippy.service.OAuth2Service;

public class OAuth2ServiceFactory {

    private OAuth2ServiceFactory() {};

    public static OAuth2Service getOAuth2Service(String provider) {
        OAuth2Service oAuth2Service;

        switch (provider) {
            case "github":
                oAuth2Service = new GitHubOAuth2Service();
                break;
            case "google":
                oAuth2Service = new GoogleOAuth2Service();
                break;
            default:
                throw new IllegalArgumentException("OAuth2 provider '" + provider + "' not recognised");
        }

        return oAuth2Service;
    }
}
