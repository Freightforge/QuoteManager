package com.freightforge.quotemanager.security.swarm;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Implements the org.springframework.security.core.Authentication interface.
 * The constructor is set with the Swarm JWT.
 */
public class SwarmJWTToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;

    private final String jwt;

    private UserDetails principal;

    public SwarmJWTToken(String jwt) {
        super(null);
        this.jwt = jwt;
        setAuthenticated(false);
    }

    public String getJwt() {
        return jwt;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(UserDetails principal) {
        this.principal = principal;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return (Collection<GrantedAuthority>) principal.getAuthorities();
    }
}
