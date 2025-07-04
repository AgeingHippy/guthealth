package com.ageinghippy.service;

import com.ageinghippy.model.DTOMapper;
import com.ageinghippy.model.dto.UserPrincipleDTOSimple;
import com.ageinghippy.model.entity.Role;
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
    private final DTOMapper dtoMapper;

    public UserPrinciple castToUserPrinciple(Object principle) {
        return (UserPrinciple) principle;
    }

    public UserPrinciple loadUserByUsername(String username) throws UsernameNotFoundException {

        return userPrincipleRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }

    @Transactional
    public UserPrinciple createPasswordUser(UserPrinciple userPrinciple) {
        userPrinciple.setPassword(passwordEncoder.encode(userPrinciple.getPassword()));
        assert (userPrinciple.getOauth2Provider() == null);

        return createUser(userPrinciple);
    }

    @Transactional
    public UserPrinciple updatePassword(UserPrinciple userPrinciple, String password) {
        userPrinciple.setPassword(passwordEncoder.encode(password));

        return saveUserPrinciple(userPrinciple);
    }

    @Transactional
    //If user already has the role, do nothing
    public UserPrinciple registerActiveUser(UserPrinciple userPrinciple) {
        Role userRole = roleRepository.findByAuthority("ROLE_USER").orElseThrow();

        if (!userPrinciple.getAuthorities().contains(userRole)) {
            userPrinciple.getAuthorities().add(userRole);

            userPrinciple = saveUserPrinciple(userPrinciple);
        }

        return userPrinciple;
    }

    @Transactional
    public UserPrinciple createOauth2User(UserPrinciple userPrinciple) {
        assert (userPrinciple.getPassword() == null);
        assert (userPrinciple.getOauth2Provider() != null);

        return createUser(userPrinciple);
    }

    private UserPrinciple createUser(UserPrinciple userPrinciple) {
        userPrinciple.setAuthorities(List.of(roleRepository.findByAuthority("ROLE_GUEST").orElseThrow()));

        return saveUserPrinciple(userPrinciple);
    }

    protected UserPrinciple saveUserPrinciple(UserPrinciple userPrinciple) {
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

    public UserPrinciple getUserPrincipleById(Long id) {
        return userPrincipleRepository.findById(id).orElseThrow();
    }

    @Transactional
    public UserPrinciple updateUserPrinciple(Long id, UserPrinciple updateUserPrinciple) {
        UserPrinciple userPrinciple = userPrincipleRepository.findById(id).orElseThrow();

        userPrinciple.setAccountNonExpired(updateUserPrinciple.isAccountNonExpired());
        userPrinciple.setAccountNonLocked(updateUserPrinciple.isAccountNonLocked());
        userPrinciple.setCredentialsNonExpired(updateUserPrinciple.isCredentialsNonExpired());
        userPrinciple.setEnabled(updateUserPrinciple.isEnabled());

        userPrinciple.getUserMeta().setName(updateUserPrinciple.getUserMeta().getName());
        userPrinciple.getUserMeta().setEmail(updateUserPrinciple.getUserMeta().getEmail());
        userPrinciple.getUserMeta().setBio(updateUserPrinciple.getUserMeta().getBio());

        //todo - update roles

        return saveUserPrinciple(userPrinciple);
    }

    public List<UserPrincipleDTOSimple> getUsers() {
        List<UserPrinciple> userPrinciples = userPrincipleRepository.findAll();
        return dtoMapper.mapList(userPrinciples, UserPrincipleDTOSimple.class);
    }
}
