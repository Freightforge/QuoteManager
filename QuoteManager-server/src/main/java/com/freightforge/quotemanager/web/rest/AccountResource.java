package com.freightforge.quotemanager.web.rest;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;

import com.codahale.metrics.annotation.Timed;
import com.freightforge.quotemanager.model.Role;
import com.freightforge.quotemanager.model.User;
import com.freightforge.quotemanager.repository.UserRepository;
import com.freightforge.quotemanager.security.IdentityUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private IdentityUserDetails authenticatedUser;

    /** The user repository. */
    @Inject
    private UserRepository userRepository;

    /**
     * GET  /account -> get account detail on the authenticated user.
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public User getAccount() {
        log.debug("REST request to get account detail on the authenticated user");

        User user = userRepository.findOneByUsername(authenticatedUser.getUsername()).get();

        if (authenticatedUser.isSudo()) {
            // Add sudo in roles list for informing the client application.
            user.addRoles(new Role("SUDO"));
        }

        return user;
    }

    @PreAuthorize("hasAuthority('ROLE_PREVIOUS_ADMINISTRATOR')")
    @RequestMapping(value = "/account/sudo",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public User getSecured() {
        log.debug("REST request to get account detail on the authenticated user");
        Authentication a = SecurityContextHolder.getContext().getAuthentication();

        SwitchUserGrantedAuthority authority = selectUnique(a.getAuthorities(), having(on(GrantedAuthority.class).getAuthority(), equalTo(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR)));

        return userRepository.findOneByUsername(authority.getSource().getName()).get();
    }

}
