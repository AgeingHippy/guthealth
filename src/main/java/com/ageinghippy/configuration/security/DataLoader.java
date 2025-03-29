package com.ageinghippy.configuration.security;

import com.ageinghippy.model.entity.Role;
import com.ageinghippy.model.entity.UserMeta;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.RoleRepository;
import com.ageinghippy.repository.UserPrincipleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("h2")
public class DataLoader implements CommandLineRunner {

    private final UserPrincipleRepository userPrincipleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    //Setup default users if none exist. To be replaced by SQL...
    public void run(String[] args) {
        if (userPrincipleRepository.findAll().isEmpty()) {
            //create roles
            Role guestRole = roleRepository.save(Role.builder().authority("ROLE_GUEST").build());
            Role userRole = roleRepository.save(Role.builder().authority("ROLE_USER").build());
            Role adminRole = roleRepository.save(Role.builder().authority("ROLE_ADMIN").build());

            //create users
            UserPrinciple adminUser = userPrincipleRepository.save(
                    UserPrinciple.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("admin"))
                            .authorities(List.of(userRole, adminRole))
                            .userMeta(UserMeta.builder().name("Bob").email("bob@home.com").build())
                            .build());
            UserPrinciple basicUser = userPrincipleRepository.save(
                    UserPrinciple.builder()
                            .username("user")
                            .password(passwordEncoder.encode("user"))
                            .authorities(List.of(userRole))
                            .userMeta(UserMeta.builder().name("Bill").email("bill@home.com").build())
                            .build());
            UserPrinciple guestUser = userPrincipleRepository.save(
                    UserPrinciple.builder()
                            .username("guest")
                            .password(passwordEncoder.encode("guest"))
                            .authorities(List.of(guestRole))
                            .userMeta(UserMeta.builder().name("Betty").email("betty@home.com").build())
                            .build());
        }
    }
}
