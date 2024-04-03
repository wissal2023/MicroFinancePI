package com.example.microfinancepi.appConfig;

import com.example.microfinancepi.security.JWTAuthenticationFilter;
import com.example.microfinancepi.security.JWTAuthorizationFilter;
import com.example.microfinancepi.security.JWTUtils;
import com.example.microfinancepi.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JWTUtils jwtUtils;



    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(jwtUtils, userService);
        httpSecurity.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/**/admin").hasAuthority("ADMIN")
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/api/user/addUser","/api/user/login").permitAll()
//                .antMatchers("/api/user/deleteUser/{id}").access("hasAuthority('ADMIN') or @userService.isCurrentUser(#id)")


                .anyRequest().authenticated()
                .and()
                .addFilterAfter(new JWTAuthenticationFilter(authenticationManager(), jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
