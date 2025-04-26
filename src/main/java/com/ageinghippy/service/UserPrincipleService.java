package com.ageinghippy.service;

import com.ageinghippy.model.entity.UserMeta;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.RoleRepository;
import com.ageinghippy.repository.UserMetaRepository;
import com.ageinghippy.repository.UserPrincipleRepository;
import com.ageinghippy.util.Util;
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
    public UserPrinciple updatePassword(UserPrinciple userPrinciple, String password) {
        userPrinciple.setPassword(passwordEncoder.encode(password));

        return saveUserPrinciple(userPrinciple);
    }

    @Transactional
    public UserPrinciple createOauth2User(UserPrinciple userPrinciple) {

        return createUser(userPrinciple);
    }

    @Transactional
    public UserPrinciple createUser(UserPrinciple userPrinciple) {
        userPrinciple.setAuthorities(List.of(roleRepository.findByAuthority("ROLE_USER").orElseThrow()));

        return saveUserPrinciple(userPrinciple);
    }

    private UserPrinciple saveUserPrinciple(UserPrinciple userPrinciple) {
        boolean newUser = userPrinciple.getId() == null;
        userMetaRepository.save(userPrinciple.getUserMeta());
        userPrincipleRepository.save(userPrinciple);

        entityManager.flush();
        if (!newUser) {
            entityManager.refresh(entityManager.merge(userPrinciple));
        } else {
            entityManager.refresh(userPrinciple);
        }

        return userPrinciple;
    }

    @Transactional
    public UserPrinciple updateUserMeta(UserPrinciple userPrinciple, UserMeta userMeta) {
        userPrinciple.getUserMeta().setName(Util.valueIfNull(userMeta.getName(), userPrinciple.getUserMeta().getName()));
        userPrinciple.getUserMeta().setEmail(Util.valueIfNull(userMeta.getEmail(), userPrinciple.getUserMeta().getEmail()));
        userPrinciple.getUserMeta().setBio(Util.valueIfNull(userMeta.getBio(), userPrinciple.getUserMeta().getBio()));

        return saveUserPrinciple(userPrinciple);
    }
}
