package com.freightforge.quotemanager.web.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;
import org.springframework.security.web.authentication.switchuser.SwitchUserAuthorityChanger;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SwitchUserFilter extends GenericFilterBean implements
    ApplicationEventPublisherAware, MessageSourceAware {

    public static final String SECURITY_SWITCH_USERNAME_KEY = "su";
    public static final String ROLE_PREVIOUS_ADMINISTRATOR = "ROLE_PREVIOUS_ADMINISTRATOR";

    private ApplicationEventPublisher eventPublisher;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private String usernameCookie = SECURITY_SWITCH_USERNAME_KEY;
    private SwitchUserAuthorityChanger switchUserAuthorityChanger;
    @Autowired
    private UserDetailsService userDetailsService;
    private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    private AuthenticationFailureHandler failureHandler;

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (requiresSwitchUser(request)) {
            // if set, attempt switch and store original
            try {
                Authentication targetUser = attemptSwitchUser(request);

                // update the current context to the new target user
                SecurityContextHolder.getContext().setAuthentication(targetUser);
            }
            catch (AuthenticationException e) {
                logger.debug("Switch User failed", e);
                failureHandler.onAuthenticationFailure(request, response, e);
            }
        }

        chain.doFilter(request, response);
    }

    protected boolean requiresSwitchUser(HttpServletRequest request) {

        // Check if user has role

        return WebUtils.getCookie(request, usernameCookie) != null;
    }

    protected Authentication attemptSwitchUser(HttpServletRequest request)
        throws AuthenticationException {
        UsernamePasswordAuthenticationToken targetUserRequest;

        String username = WebUtils.getCookie(request, usernameCookie).getValue();

        if (username == null) {
            username = "";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Attempt to switch to user [" + username + "]");
        }

        UserDetails targetUser = userDetailsService.loadUserByUsername(username);
        userDetailsChecker.check(targetUser);

        // OK, create the switch user token
        targetUserRequest = createSwitchUserToken(request, targetUser);

        if (logger.isDebugEnabled()) {
            logger.debug("Switch User Token [" + targetUserRequest + "]");
        }

        // publish event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(
                SecurityContextHolder.getContext().getAuthentication(), targetUser));
        }

        return targetUserRequest;
    }

    private UsernamePasswordAuthenticationToken createSwitchUserToken(
        HttpServletRequest request, UserDetails targetUser) {


        UsernamePasswordAuthenticationToken targetUserRequest;

        // grant an additional authority that contains the original Authentication object
        // which will be used to 'exit' from the current switched user.

        Authentication currentAuth;

        currentAuth = SecurityContextHolder.getContext().getAuthentication();

        GrantedAuthority switchAuthority = new SwitchUserGrantedAuthority(
            ROLE_PREVIOUS_ADMINISTRATOR, currentAuth);

        // get the original authorities
        Collection<? extends GrantedAuthority> orig = targetUser.getAuthorities();

        // Allow subclasses to change the authorities to be granted
        if (switchUserAuthorityChanger != null) {
            orig = switchUserAuthorityChanger.modifyGrantedAuthorities(targetUser,
                currentAuth, orig);
        }

        // add the new switch user authority
        List<GrantedAuthority> newAuths = new ArrayList<GrantedAuthority>(orig);
        newAuths.add(switchAuthority);

        // create the new authentication token
        targetUserRequest = new UsernamePasswordAuthenticationToken(targetUser,
            targetUser.getPassword(), newAuths);

        // set details
        targetUserRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        return targetUserRequest;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void setAuthenticationDetailsSource(
        AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource,
            "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messages = new MessageSourceAccessor(messageSource);
    }

    /**
     * Used to define custom behaviour when a switch fails.
     * <p>
     * Can be used instead of setting <tt>switchFailureUrl</tt>.
     */
    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler cannot be null");
        this.failureHandler = failureHandler;
    }

    /**
     * @param switchUserAuthorityChanger to use to fine-tune the authorities granted to
     * subclasses (may be null if SwitchUserFilter should not fine-tune the authorities)
     */
    public void setSwitchUserAuthorityChanger(
        SwitchUserAuthorityChanger switchUserAuthorityChanger) {
        this.switchUserAuthorityChanger = switchUserAuthorityChanger;
    }

    public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
        this.userDetailsChecker = userDetailsChecker;
    }

    /**
     * Allows the cookie containing the username to be customized.
     *
     * @param usernameCookie the parameter name. Defaults to {@code username}
     */
    public void setUsernameCookie(String usernameCookie) {
        this.usernameCookie = usernameCookie;
    }
}
