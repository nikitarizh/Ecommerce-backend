package com.nikitarizh.testtask.config;

import com.nikitarizh.testtask.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/carts/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/shops/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/shops/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/tags/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/tags/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
}