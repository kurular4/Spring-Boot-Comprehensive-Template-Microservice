package com.ofk.template.authenticationservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ofk.template.authenticationservice.config.JwtConstant;
import com.ofk.template.authenticationservice.dto.UserDTO;
import com.ofk.template.authenticationservice.exception.BadCredentialsException;
import com.ofk.template.authenticationservice.util.JsonUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConstant jwtConstant;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtConstant jwtConstant) {
        this.authenticationManager = authenticationManager;
        this.jwtConstant = jwtConstant;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDTO userAuthenticationData = mapper.readValue(request.getInputStream(), UserDTO.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userAuthenticationData.getUsername(), userAuthenticationData.getPassword());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TimeUnit.DAYS.toMillis(jwtConstant.getExpirationInDays()));

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim(jwtConstant.getHeader(), authResult.getAuthorities())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(jwtConstant.getSecretKey().getBytes()))
                .compact();
        response.addHeader(jwtConstant.getHeader(), jwtConstant.getPrefix() + " " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        response.getWriter().write(JsonUtil.convertObjectToJson(ResponseEntity.status(HttpStatus.BAD_REQUEST)));
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        new ObjectMapper().writeValue(response.getOutputStream(), new ResponseEntity<>("", HttpStatus.BAD_REQUEST));
    }


}
