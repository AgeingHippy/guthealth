package com.ageinghippy.service;

import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.UserMetaRepository;
import com.ageinghippy.repository.UserPrincipleRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPrincipleService implements UserDetailsService {
    private final UserPrincipleRepository userPrincipleRepository;
    private final UserMetaRepository userMetaRepository;
    private final EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userPrincipleRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }

    @Transactional
    public UserPrinciple createUser(UserPrinciple userPrinciple) {
        userMetaRepository.save(userPrinciple.getUserMeta());
        userPrincipleRepository.save(userPrinciple);

        entityManager.flush();
        entityManager.refresh(userPrinciple);

        return userPrinciple;
    }
}
