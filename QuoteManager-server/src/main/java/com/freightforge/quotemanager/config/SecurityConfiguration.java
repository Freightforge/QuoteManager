package com.freightforge.quotemanager.config;

import com.freightforge.quotemanager.security.*;
import com.freightforge.quotemanager.security.swarm.SwarmAuthenticationEntryPoint;
import com.freightforge.quotemanager.security.swarm.SwarmAuthenticationFilter;
import com.freightforge.quotemanager.security.swarm.SwarmAuthenticationProvider;
import com.freightforge.quotemanager.web.filter.SwitchUserFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private UserDetailsService userDetailsService;

    @Bean
    public SwarmAuthenticationEntryPoint authenticationEntryPoint() {
        return new SwarmAuthenticationEntryPoint();
    }

    @Bean
    public SwarmAuthenticationFilter authenticationFilter() {
        return new SwarmAuthenticationFilter();
    }

    @Bean
    public SwarmAuthenticationProvider authenticationProvider() {
        return new SwarmAuthenticationProvider();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/scripts/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/assets/**")
            .antMatchers("/swagger-ui.html")
            .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterAfter(switchUserProcessingFilter(userDetailsService), FilterSecurityInterceptor.class)
            .addFilterAfter(authenticationFilter(), SecurityContextPersistenceFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
        .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/login/impersonate").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/login").permitAll()
            .antMatchers("/logout/impersonate").hasAuthority(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR)
            .antMatchers("/logout").authenticated()
            .antMatchers("/api/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/**").authenticated()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/configuration/security").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/configuration/ui").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/swagger-ui.html").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/protected/**").authenticated();

    }

    @Bean
    public SwitchUserFilter switchUserProcessingFilter(UserDetailsService userDetailsService) {
        SwitchUserFilter switchUserFilter = new SwitchUserFilter();
        return switchUserFilter;
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
