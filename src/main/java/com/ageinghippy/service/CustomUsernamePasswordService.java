package com.ageinghippy.service;

import com.ageinghippy.model.entity.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUsernamePasswordService implements UserDetailsService {

    private final UserPrincipleService userPrincipleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPrinciple userPrinciple = userPrincipleService.loadUserByUsername(username);
        return userPrinciple;
    }
}
