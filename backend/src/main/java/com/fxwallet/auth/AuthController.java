package com.fxwallet.auth;

import static com.fxwallet.auth.AuthModels.*;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @PostMapping("/login")
  AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }

  @PostMapping("/refresh")
  AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
    return authService.refresh(request);
  }

  @PostMapping("/verify-email")
  ResponseEntity<Void> verify(@Valid @RequestBody VerifyEmailRequest request) {
    authService.verify(request);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/logout")
  ResponseEntity<Void> logout() {
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/2fa/enable")
  ResponseEntity<java.util.Map<String, String>> twoFactor() {
    return ResponseEntity.ok(java.util.Map.of("secret", "DEMO-TOTP-SECRET", "issuer", "FX Wallet"));
  }
}
