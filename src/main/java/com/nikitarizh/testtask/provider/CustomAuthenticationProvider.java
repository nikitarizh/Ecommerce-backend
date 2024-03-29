package com.nikitarizh.testtask.provider;

import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.InvalidCredentialsException;
import com.nikitarizh.testtask.exception.UserNotFoundException;
import com.nikitarizh.testtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String nickname = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user;
        try {
            user = userService.findByNickname(nickname);
        }
        catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }

        if (!(bCryptPasswordEncoder.matches(password, user.getPassword()))) {
            throw new InvalidCredentialsException();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new UsernamePasswordAuthenticationToken(nickname, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}