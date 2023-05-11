package com.stefan.springjwt.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stefan.springjwt.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    System.out.println(" *** token generation ***");
    // get the authentication object from the security context
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    System.out.println("authentication: " + authentication);

    if (null != authentication) {

      User user = (User) authentication.getPrincipal();

      SecretKey key = Keys.hmacShaKeyFor("BananaBananaBananaBananaBananaBanana".getBytes(StandardCharsets.UTF_8));
      String jwt = Jwts.builder()
          .setIssuer("springboot security app")
          .setSubject(String.valueOf(user.getId()))
          .setIssuedAt(new Date())
          .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(5)))
          .signWith(key)
          .compact();

      // set the JWT token as a cookie
      Cookie cookieJwt = new Cookie("jwt", jwt);
      cookieJwt.setMaxAge(60 * 60 * 24 * 5); // set cookie expiry to 5 days
      cookieJwt.setPath("/");
      cookieJwt.setHttpOnly(true);
      response.addCookie(cookieJwt);

      Cookie cookieUsername = new Cookie("username", user.getUsername());
      cookieUsername.setMaxAge(60 * 60 * 24 * 5); // set cookie expiry to 5 days
      cookieUsername.setPath("/");


    } else {
      System.out.println("authentication is null");
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return !request.getServletPath().equals("/loginUser");
  }

}
