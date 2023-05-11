package com.stefan.springjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.stefan.springjwt.model.User;
import com.stefan.springjwt.repository.UserRepository;

@Component
public class BcryptUserProvider implements AuthenticationProvider {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    System.out.println("username: " + username);
    System.out.println("password: " + password);

    User user = userRepository.findByEmail(username);

    if(user != null && passwordEncoder.matches(password, user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(user, password);
    } else {
      throw new BadCredentialsException("Authentication failed!");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return(UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
  
}
