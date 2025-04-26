package com.ageinghippy.service;

import com.ageinghippy.model.CustomUserPrincipal;
import com.ageinghippy.model.entity.UserMeta;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.RoleRepository;
import com.ageinghippy.repository.UserPrincipleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserPrincipleRepository userPrincipleRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);
        String provider = request.getClientRegistration().getRegistrationId();
        String oauthId = oauth2User.getName();
        String username = provider + ":" + oauthId;

        // Load or register
        UserPrinciple userPrinciple = userPrincipleRepository.findByUsername(username)
                .orElseGet(() -> userPrincipleRepository.save(createOAuth2User(oauth2User,provider, username)));

        // Wrap with domain-aware principal
        return new CustomUserPrincipal(userPrinciple, oauth2User.getAttributes());
    }

    private UserPrinciple createOAuth2User(OAuth2User oauth2User,String provider, String userName) {
        return UserPrinciple.builder()
                .username(userName)
                .oauth2Provider(provider)
                .authorities(List.of(roleRepository.findByAuthority("ROLE_USER").orElseThrow()))
                .userMeta(UserMeta.builder().name(userName).build())
                .build();
    }


}
