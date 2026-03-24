package com.biblioteca.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Collections;

public class ApiKeyAuthentication implements Authentication {

    private final String apiKey;
    private boolean authenticated;

    public ApiKeyAuthentication(String apiKey) {
        this.apiKey = apiKey;
        this.authenticated = false;
    }

    @Override public Object getPrincipal() { return this.apiKey; }
    @Override public boolean isAuthenticated() { return this.authenticated; }
    @Override public void setAuthenticated(boolean b) { this.authenticated = b; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    @Override public Object getCredentials() { return null; }
    @Override public Object getDetails() { return null; }
    @Override public String getName() { return this.apiKey; }
}
