package com.shoghlana.backend.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user" ,schema = "tshoghlana")
@Builder
@NoArgsConstructor @AllArgsConstructor  @Getter @Setter
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ; // TODO -- make it as a Foreign Key in the Actual User Entity

    @Column(name = "username" , unique = true , nullable = false)
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    // will ONLY used for security >> UserDetails.loadUserByUsername() + Jwt Subject
    private String username;

    @Column(name = "email" , nullable = true , unique = true)
    private String email;

    @Column(name = "phone" , nullable = true , unique = true)
    private String phone;

    private String password;


    private boolean emailVerified;
    private boolean phoneVerified;


    @Enumerated(EnumType.STRING)
    private AppAuthProvider appAuthProvider;

    private String providerId;

    // Authorization
    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Roles> roles = new HashSet<>();



    @Override
    public boolean isEnabled() {
        return
                switch (appAuthProvider)
                {
                    case EMAIL -> isEmailVerified();
                    case PHONE -> isPhoneVerified();
                    default -> true; // OAuth2 (GOogle or faceBook)
                } ;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO-- Change this to return a list of eachOf(Roles)
//        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name() ));
        return roles
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName() ))
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {

        return this.username;
    }
}
