package com.ageinghippy.service;

import com.ageinghippy.model.entity.UserMeta;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Service {

    UserMeta buildUserMeta(ClientRegistration clientRegistration, OAuth2AccessToken accessToken, OAuth2User oAuth2User);

}
