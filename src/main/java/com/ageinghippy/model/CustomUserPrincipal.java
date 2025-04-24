package com.ageinghippy.model;

import com.ageinghippy.model.entity.UserPrinciple;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomUserPrincipal implements OAuth2User, UserDetails {
    private final UserPrinciple userPrinciple;
    private final Map<String, Object> attributes;

    public CustomUserPrincipal(UserPrinciple userPrinciple, Map<String, Object> attributes ) {
        this.userPrinciple = userPrinciple;
        this.attributes = attributes;
    }

    public CustomUserPrincipal(UserPrinciple userPrinciple ) {
        this.userPrinciple = userPrinciple;
        this.attributes = Collections.EMPTY_MAP;
    }

    public UserPrinciple getUserPrinciple() {
        return this.userPrinciple;
    }

    @Override
    public String getName() {
        return userPrinciple.getUsername();
    }

//    @Override
//    public <A> A getAttribute(String name) {
//        return OAuth2User.super.getAttribute(name);
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userPrinciple.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getPassword() {
        return userPrinciple.getPassword();
    }

    @Override
    public String getUsername() {
        return userPrinciple.getUsername();
    }
}
