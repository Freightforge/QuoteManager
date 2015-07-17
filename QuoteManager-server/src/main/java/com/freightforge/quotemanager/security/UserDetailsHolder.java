package com.freightforge.quotemanager.security;

import static ch.lambdaj.Lambda.exists;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * The Class UserDetailsHolder. http://stackoverflow.com/questions/360520/unit-testing-with-spring-security
 * http://whyjava.wordpress.com/2010/10/30/spring-scoped-proxy-beans-an-alternative-to-method-injection/
 */
@Component
public class UserDetailsHolder {

    /** Logger. */
    static Logger logger = LoggerFactory.getLogger(UserDetailsHolder.class);

    /**
     * Gets the user authenticated.
     *
     * @return the user authenticated
     */
    @Bean
    @Qualifier("authenticatedUser")
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
    public IdentityUserDetails getAuthenticatedUserAccount() {
        final Authentication a = SecurityContextHolder.getContext().getAuthentication();

        if (a != null && a.getPrincipal() instanceof IdentityUserDetails) {
            IdentityUserDetails userDetails = (IdentityUserDetails) a.getPrincipal();
            boolean isSudo = exists(a.getAuthorities(),
                having(on(GrantedAuthority.class).getAuthority(), equalTo(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR)));

            /*
            if (isSudo) {
                SwitchUserGrantedAuthority authority =
                (SwitchUserGrantedAuthority) selectUnique(a.getAuthorities(), having(on(GrantedAuthority.class).getAuthority(), equalTo(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR)));

                authority.getSource().getPrincipal();

                logger.info("{} is logged like {}", authority.getSource().getName(), a.getName());
            }
            */

            userDetails.setSudo(isSudo);

            return userDetails;
        } else {
            return null;
        }
    }
}
