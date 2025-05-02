package com.ageinghippy.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "principle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrinciple implements UserDetails, OAuth2User {

    public UserPrinciple(
            String username,
            String password,
            String oauth2Provider,
            List<Role> authorities,
            UserMeta userMeta) {
        this.username = username;
        this.password = password;
        this.oauth2Provider = oauth2Provider;
        this.authorities = authorities;
        this.userMeta = userMeta;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    //todo - add custom validation that exactly one of password and oauthProvider must be not null
    @JsonIgnore
    private String password;

    @JsonIgnore
    @Column(name = "oauth2_provider")
    private String oauth2Provider;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private UserMeta userMeta;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "principle_roles",
            joinColumns = @JoinColumn(name = "principle_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> authorities;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Transient
    @Builder.Default
    private Map<String, Object> attributes = Collections.emptyMap();;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
