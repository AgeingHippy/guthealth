package com.ageinghippy.service;

import com.ageinghippy.model.entity.UserMeta;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleOAuth2Service implements OAuth2Service {

    @Override
    public UserMeta buildUserMeta(ClientRegistration clientRegistration, OAuth2AccessToken accessToken, OAuth2User oAuth2User) {
        UserMeta userMeta = new UserMeta();

        if (oAuth2User.getAttributes().containsKey("email")  && oAuth2User.getAttributes().containsKey("email_verified") &&
            (Boolean) oAuth2User.getAttribute("email_verified")
            ) {
            userMeta.setEmail(oAuth2User.getAttribute("email"));
            userMeta.setName(oAuth2User.getAttribute("email"));
        } else {
            userMeta.setName(clientRegistration.getRegistrationId() + ":" + oAuth2User.getName());
        }
        
        return userMeta;
    }
}
