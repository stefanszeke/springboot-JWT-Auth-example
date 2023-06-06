package com.stefan.springjwt.config;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
        if (authException instanceof BadCredentialsException) {
        System.out.println(" *** JwtAuthenticationEntryPoint BadCredentialsException***");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response.getWriter().write("{\"error\": \"Invalid JWT: " + authException.getMessage() + "\"}");
      } else {
        System.out.println(" *** JwtAuthenticationEntryPoint else ***");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response.getWriter().write("{\"error\": \"Unauthorized xD: " + authException.getMessage() + "\"}");

      }
  }
  
}
