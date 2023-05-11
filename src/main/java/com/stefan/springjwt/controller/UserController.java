package com.stefan.springjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.stefan.springjwt.model.User;
import com.stefan.springjwt.repository.UserRepository;

@RestController
public class UserController {
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;


  @GetMapping("/test")
  public ResponseEntity<String> test() {
    return ResponseEntity.ok("Test successful!");
  }

  @GetMapping("/test2")
  public ResponseEntity<String> test2() {
    return ResponseEntity.ok("Protected: Test2 successful!");
  }

  @PostMapping("/test")
  public ResponseEntity<String> testPost() {
    System.out.println("Test Post");
    return ResponseEntity.ok("Post Test successful!");
  }


  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody User user) {

    if(userRepository.findByEmail(user.getEmail()) != null) {
      return ResponseEntity.badRequest().body("Email already exists!");
    }

    User newUser = new User();
    newUser.setEmail(user.getEmail());
    newUser.setUsername(user.getUsername());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));

    userRepository.save(newUser);

    return ResponseEntity.ok("User registered successfully!");
  }

  @GetMapping("/loginUser")
  public ResponseEntity<String> login() {
    System.out.println("loggin in");
    return ResponseEntity.ok("Login successful!");
  }

}
