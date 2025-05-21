package com.ageinghippy.service;

import com.ageinghippy.model.GitHubEmail;
import com.ageinghippy.model.entity.UserMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class GitHubOAuth2Service implements OAuth2Service {

    @Override
    public UserMeta buildUserMeta(ClientRegistration clientRegistration, OAuth2AccessToken accessToken, OAuth2User oauth2User) {
        UserMeta userMeta = new UserMeta();

        if (oauth2User.getAttributes().containsKey("login") &&
            !Objects.requireNonNull(oauth2User.getAttribute("login")).toString().isEmpty()) {
            userMeta.setName(oauth2User.getAttribute("login"));
        } else {
            userMeta.setName(clientRegistration.getRegistrationId() + ":" + oauth2User.getName());
        }

        userMeta.setEmail(getEmail(accessToken));


        return userMeta;
    }

    public String getEmail(OAuth2AccessToken accessToken) {
        GitHubEmail primaryEmail = null;
        String accessTokenValue = accessToken.getTokenValue();

        // Call GitHub API to get emails
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(accessTokenValue);
        headers.set("Accept", "application/vnd.github+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<GitHubEmail>> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<GitHubEmail>>() {
                }
        );

        List<GitHubEmail> emails = response.getBody();
        if (emails != null) {
            primaryEmail = emails.stream().filter(email -> email.primary && email.verified).findFirst().orElse(null);
        }

        return primaryEmail != null ? primaryEmail.email : "";
    }

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authToken) {
        return authorizedClientService.loadAuthorizedClient(
                authToken.getAuthorizedClientRegistrationId(),
                authToken.getName()
        );
    }
}
