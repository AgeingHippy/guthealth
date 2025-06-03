package com.ageinghippy.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/js/**", "/css/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
//                        .requestMatchers("/h2-console/**").permitAll() //todo remove!!!
                        .requestMatchers("/test/**").permitAll() //todo remove!!!
                        .requestMatchers("/", "/home", "/index").permitAll()
                        .requestMatchers("/user/new", "/user/create", "/login").anonymous()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/profile")
                        .permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/profile")
                        .permitAll());

//        http.headers().frameOptions().disable(); //todo remove - for h2 console access!!!

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
