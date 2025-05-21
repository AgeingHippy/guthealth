package com.ageinghippy.service;

import com.ageinghippy.factory.OAuth2ServiceFactory;
import com.ageinghippy.model.entity.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserPrincipleService userPrincipleService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        UserPrinciple userPrinciple;
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);
        String provider = request.getClientRegistration().getRegistrationId();
        String oauthId = oauth2User.getName();
        String username = provider + ":" + oauthId;

        // Load or register
        try {
            userPrinciple = userPrincipleService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            userPrinciple = createUserPrinciple(oauth2User, provider, username);
            userPrinciple.setUserMeta(
                    OAuth2ServiceFactory.getOAuth2Service(provider)
                            .buildUserMeta(
                                    request.getClientRegistration(),
                                    request.getAccessToken(),
                                    oauth2User));
            userPrinciple = userPrincipleService.createOauth2User(userPrinciple);
        }

        return userPrinciple;
    }

    private UserPrinciple createUserPrinciple(OAuth2User oauth2User, String provider, String userName) {
        return UserPrinciple.builder()
                .username(userName)
                .oauth2Provider(provider)
                .attributes(oauth2User.getAttributes())
                .build();
    }


}
