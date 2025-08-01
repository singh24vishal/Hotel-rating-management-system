package com.authService.controllers;

import com.authService.model.User;
import com.authService.payload.LoginRequest;
import com.authService.payload.LoginResponse;
import com.authService.repository.UserRepository;
import com.authService.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired private UserRepository userRepo;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JwtUtils jwtUtils;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody User signupRequest) {
    if (userRepo.findByEmail(signupRequest.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body("Email already taken");
    }
    signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    userRepo.save(signupRequest);
    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Optional<User> userOpt = userRepo.findByEmail(loginRequest.getEmail());
    if (userOpt.isPresent() &&
        passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
      String token = jwtUtils.generateToken(userOpt.get().getEmail());
      return ResponseEntity.ok(new LoginResponse(token));
    }
    return ResponseEntity.status(401).body("Invalid credentials");
  }
}
