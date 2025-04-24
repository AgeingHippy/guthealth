package com.ageinghippy.service;

import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.RoleRepository;
import com.ageinghippy.repository.UserMetaRepository;
import com.ageinghippy.repository.UserPrincipleRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPrincipleService {
    private final UserPrincipleRepository userPrincipleRepository;
    private final UserMetaRepository userMetaRepository;
    private final RoleRepository roleRepository;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public UserPrinciple loadUserByUsername(String username) throws UsernameNotFoundException {

        return userPrincipleRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }

    @Transactional
    public UserPrinciple createPasswordUser(UserPrinciple userPrinciple) {
        userPrinciple.setPassword(passwordEncoder.encode(userPrinciple.getPassword()));

        return createUser(userPrinciple);
    }

    @Transactional
    public UserPrinciple createOauth2User(UserPrinciple userPrinciple) {

        return createUser(userPrinciple);
    }

    @Transactional
    public UserPrinciple createUser(UserPrinciple userPrinciple) {
        userPrinciple.setAuthorities(List.of(roleRepository.findByAuthority("ROLE_USER").orElseThrow()));

        userMetaRepository.save(userPrinciple.getUserMeta());
        userPrincipleRepository.save(userPrinciple);

        entityManager.flush();
        entityManager.refresh(userPrinciple);

        return userPrinciple;
    }
}
