package com.freightforge.quotemanager.security.swarm;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Implementation of UserDetails in compliance with the decoded object returned by the Swarm JWT.
 */
public class SwarmUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(SwarmUserDetails.class);

    private Map<String, Object> details;

    private String username;

    private Collection<GrantedAuthority> authorities = null;

    public SwarmUserDetails(Jws<Claims> jws) {
        Claims body  = jws.getBody();

        if (body.getSubject() != null) {
            this.username = body.getSubject();
        } else if (body.containsKey("username")) {
            this.username = body.get("username").toString();
        } else if (body.containsKey("user_id")) {
            this.username = body.get("user_id").toString();
        } else if (body.containsKey("email")) {
            this.username = body.get("email").toString();
        } else {
            this.username = "UNKNOWN_USER";
        }

        // Set authorities
        authorities = new ArrayList<>();
        if (body.containsKey("roles")) {
            ArrayList<String> roles = null;
            try {
                roles = (ArrayList<String>) body.get("roles");
                for(String role : roles){
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            } catch (java.lang.ClassCastException e){
                e.printStackTrace();
                log.error("Error in casting the roles object");
            }

            //By default if nothing is added
            if(authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            this.details = body;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Will return UnsupportedOperationException.
     */
    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("Password is protected");
    }

    /**
     * Gets the claim subject, username, email or user_id if it exists otherwise it returns "UNKNOWN_USER".
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Will return false.
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * Will return false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * Will return false.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * Will return true.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Will return the details of the attribute of JWT decoded token if it exists or null otherwise.
     * Example getSwarmAttribute("email"), getSwarmAttribute("picture")....
     *
     * @return return the details of the JWT decoded token if it exists  or null otherwise
     */
    public Object getSwarmAttribute(String attributeName) {
        return details.get(attributeName);
    }
}
