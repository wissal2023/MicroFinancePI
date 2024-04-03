package com.example.microfinancepi.security;

import com.example.microfinancepi.entities.LoginRequest;
import com.example.microfinancepi.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.microfinancepi.security.SecurityConstants.TOKEN_HEADER;
import static com.example.microfinancepi.security.SecurityConstants.TOKEN_PREFIX;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    @Autowired
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        setFilterProcessesUrl("/api/user/login");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("Attempting authentication for URL: " + request.getRequestURL());
        try {
            LoginRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getUser_password()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User userDetails = (User) authResult.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);
        response.addHeader(TOKEN_HEADER, TOKEN_PREFIX + token);
    }

}
