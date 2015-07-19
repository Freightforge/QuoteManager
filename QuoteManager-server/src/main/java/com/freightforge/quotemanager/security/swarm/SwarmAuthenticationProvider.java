package com.freightforge.quotemanager.security.swarm;

import com.freightforge.quotemanager.security.UserDetailsService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class SwarmAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SwarmAuthenticationProvider.class);

    private static final AuthenticationException AUTH_ERROR = new SwarmTokenException("Authentication error occured");

    @Value("#swarm.clientId")
    private String clientId;

    @Value("#swarm.clientSecret")
    private String clientSecret;

    @Autowired(required = false)
    private UserDetailsService userDetailsService;

    private JwtParser parser;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((SwarmJWTToken) authentication).getJwt();

        log.info("Trying to authenticate with token: " + token);

        Jws<Claims> decoded;
        try {

            SwarmJWTToken tokenAuth = ((SwarmJWTToken) authentication);
            decoded = parser.parseClaimsJws(token);
            log.debug("Decoded JWT token" + decoded);
            tokenAuth.setAuthenticated(true);

            UserDetails userDetails;
            if (userDetailsService != null) {
                userDetails = userDetailsService.loadUserByUsername(decoded.getBody().getSubject());
            } else {
                userDetails = new SwarmUserDetails(decoded);
            }

            tokenAuth.setPrincipal(userDetails);
            tokenAuth.setDetails(decoded);
            return authentication;

        } catch (ExpiredJwtException e) {
            log.debug("ExpiredJwtException thrown while decoding JWT token "
                + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (UnsupportedJwtException e) {
            log.debug("UnsupportedJwtException thrown while decoding JWT token "
                + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (MalformedJwtException e) {
            log.debug("MalformedJwtException thrown while decoding JWT token "
                + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (SignatureException e) {
            log.debug("SignatureException thrown while decoding JWT token "
                + e.getLocalizedMessage());
            throw AUTH_ERROR;
        } catch (IllegalArgumentException e) {
            log.debug("IllegalArgumentException thrown while decoding JWT token "
                + e.getLocalizedMessage());
            throw AUTH_ERROR;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SwarmJWTToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.parser = Jwts.parser().setSigningKey(clientSecret);
    }
}
