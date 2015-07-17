package com.freightforge.quotemanager.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component
public class DefaultPostUserAccountChecker implements UserDetailsChecker {

    /** The logger. */
    static Logger log = LoggerFactory.getLogger(DefaultPostUserAccountChecker.class);

    @Override
    public void check(UserDetails toCheck) {
        log.info("Post check");
    }
}
