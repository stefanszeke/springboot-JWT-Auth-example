package com.stefan.springjwt.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
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
          .claim("username", user.getUsername())
          .setIssuedAt(new Date())
          .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(5)))
          .signWith(key)
          .compact();

      // set the JWT token as a cookie
      // Cookie cookieJwt = new Cookie("jwt", jwt);
      // cookieJwt.setMaxAge(60 * 60 * 24 * 5); // set cookie expiry to 5 days
      // cookieJwt.setPath("/");
      // cookieJwt.setHttpOnly(true);
      // response.addCookie(cookieJwt);

      ResponseCookieBuilder cookieBuilder = ResponseCookie.from("jwt", jwt)
          .maxAge(60 * 60 * 24 * 5)
          .path("/")
          .httpOnly(true)
          .sameSite("None")
          .secure(true);
      response.addHeader("Set-Cookie", cookieBuilder.build().toString());

      ResponseCookieBuilder cookieBuilder2 = ResponseCookie.from("username", user.getUsername())
          .maxAge(60 * 60 * 24 * 5) // set cookie expiry to 5 days
          .path("/")
          .sameSite("None");
      response.addHeader("Set-Cookie", cookieBuilder2.build().toString());

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
