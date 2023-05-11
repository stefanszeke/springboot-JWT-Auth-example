package com.stefan.springjwt.filter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException, java.io.IOException {

    String jwt = request.getHeader("Authorization");

    if (null != jwt) {
      try {
        jwt = jwt.replace("Bearer ", "");

        System.out.println("jwt: " + jwt);

        Claims claims = validateToken(jwt);

        System.out.println("claims: " + claims);

        String username = claims.getSubject();
        String authorities = (String) claims.get("authorities");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            username,
            null,
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
        );

            System.out.println("authentication after JWT Validation: " + authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (Exception e) {
        throw new BadCredentialsException("Invalid JWT" + e.getMessage());
      }
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String[] pathsToSkip = { "/register", "/loginUser","/test" };
    return Arrays.stream(pathsToSkip).anyMatch(path -> request.getServletPath().equals(path));
  }

  private Claims validateToken(String jwt) {
    SecretKey key = Keys.hmacShaKeyFor("BananaBananaBananaBananaBananaBanana".getBytes(StandardCharsets.UTF_8));

    Jws<Claims> claimsJws = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(jwt);

    return claimsJws.getBody();
  }
}