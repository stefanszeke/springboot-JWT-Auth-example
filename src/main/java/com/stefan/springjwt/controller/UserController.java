package com.stefan.springjwt.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.stefan.springjwt.model.User;
import com.stefan.springjwt.model.UserPrincipal;
import com.stefan.springjwt.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

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

  @GetMapping("/userinfo")
  public ResponseEntity<?> userinfo(Principal principal) {
    UserPrincipal userPrincipal = (UserPrincipal) ((Authentication) principal).getPrincipal();

    String userName = userPrincipal.getUsername();
    return ResponseEntity.ok(Map.of("userName", userName));
  }

  @GetMapping("/logoutUser")
  public ResponseEntity<?> logoutUser(HttpServletResponse response) {
    ResponseCookieBuilder cookieBuilder = ResponseCookie.from("jwt", null)
        .maxAge(0)
        .path("/")
        .httpOnly(true)
        .sameSite("None")
        .secure(true);
    response.addHeader("Set-Cookie", cookieBuilder.build().toString());

    return ResponseEntity.ok(Map.of("message", "Logout successful!"));
  }

  @GetMapping("/test2")
  public ResponseEntity<?> test2(Principal principal) {
    if (principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Unauthorized: Token is missing or invalid"));
    }

    // Your logic for the protected route here
    UserPrincipal userPrincipal = (UserPrincipal) ((Authentication) principal).getPrincipal();
    String userID = userPrincipal.getUserID();
    String userName = userPrincipal.getUsername();
    System.out.println("userID: " + userID);
    return ResponseEntity.ok(Map.of("message", "Test2 successful! User ID: " + userID + " Username: " + userName));
  }

  @PostMapping("/test")
  public ResponseEntity<String> testPost() {
    System.out.println("Test Post");
    return ResponseEntity.ok("Post Test successful!");
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody User user) {

    if (userRepository.findByEmail(user.getEmail()) != null) {
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
  public ResponseEntity<Map<String, String>> login() {
    System.out.println("loggin in");
    return ResponseEntity.ok(Map.of("message", "login successful!"));
  }

}
