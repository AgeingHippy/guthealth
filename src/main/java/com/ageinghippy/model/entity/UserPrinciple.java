package com.ageinghippy.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Entity
@Table(name = "principle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrinciple implements UserDetails {

    public UserPrinciple(
            String username,
            String password,
            List<Role> authorities,
            UserMeta userMeta) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.userMeta = userMeta;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    @JsonIgnore
    private String password;

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

}
