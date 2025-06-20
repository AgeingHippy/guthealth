package com.ageinghippy.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final Environment environment;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/js/**", "/css/**", "/images/**", "/webjars/**").permitAll()
                            .requestMatchers("/actuator/health").permitAll()
                            .requestMatchers("/test/**").permitAll()
                            .requestMatchers("/", "/home", "/index").permitAll()
                            .requestMatchers("/user/new", "/user/create", "/login").anonymous()
                            .requestMatchers("/api/**").authenticated();

                    // ✅ Only enable if 'h2' profile is active
                    if (Arrays.asList(environment.getActiveProfiles()).contains("h2")) {
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }

                    auth.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/profile")
                        .permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/profile")
                        .permitAll());

        // ✅ Only disable frame options if 'h2' profile is active
        if (Arrays.asList(environment.getActiveProfiles()).contains("h2")) {
            http.headers().frameOptions().disable();
        }

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
