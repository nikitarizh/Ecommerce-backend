package com.nikitarizh.testtask.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/carts/**").authenticated()
                .antMatchers("/users/admins/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/shops/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/shops/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/shops/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/tags/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/tags/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/tags/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }
}