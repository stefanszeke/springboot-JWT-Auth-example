package com.stefan.springjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.stefan.springjwt.filter.JwtTokenGeneratorFilter;
import com.stefan.springjwt.filter.JwtTokenValidationFilter;
import com.stefan.springjwt.repository.UserRepository;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

@Configuration
public class ProjectSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable())

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/register", "/loginUser","/test").permitAll()
            .anyRequest().authenticated())

        .addFilterBefore(new JwtTokenValidationFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)

        .formLogin(withDefaults())
        .httpBasic(withDefaults());

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}

// .cors(cors -> {
// cors.configurationSource(request -> {
// CorsConfiguration config = new CorsConfiguration();
// config.addAllowedOrigin("*");
// config.addAllowedMethod("*");
// config.addAllowedHeader("*");
// config.setAllowCredentials(true);
// config.setMaxAge(3600L);
// config.setExposedHeaders(Arrays.asList("Authorization")); // for JWT
// return config;
// });
// })