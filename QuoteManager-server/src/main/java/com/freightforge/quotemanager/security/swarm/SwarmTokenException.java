package com.freightforge.quotemanager.security.swarm;

import org.springframework.security.core.AuthenticationException;

/**
 * Implementation of UserDetails in compliance with the decoded object returned by the Swarm JWT.
 */
public class SwarmTokenException  extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public SwarmTokenException(String msg) {
        super(msg);
    }

    public SwarmTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public SwarmTokenException(Exception e) {
        super(e.getMessage(), e);
    }
}
