package com.springproject.goodz.user.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Getter;

@Getter
public class SocialUser extends DefaultOAuth2User implements UserDetails {

    private Users user;

    public SocialUser(Users user, OAuthAttributes oAuthAttributes) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")), oAuthAttributes.getAttributes(), oAuthAttributes.getNameAttributeKey());
        this.user = user;
    }

    @Override
    public String getName() {
        return user != null ? user.getUsername() : null;
    }

    public String profile() {
        return user != null ? user.getProfilePictureUrl() : null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthList().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuth()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
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
    public boolean isEnabled() {
        return user.getEnabled() == 1;
    }
}
