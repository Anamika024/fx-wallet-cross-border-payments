package com.fxwallet.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthModels {
  private AuthModels() {}

  public record RegisterRequest(@Email String email, @NotBlank String password, @NotBlank String fullName) {}
  public record LoginRequest(@Email String email, @NotBlank String password) {}
  public record RefreshRequest(@NotBlank String refreshToken) {}
  public record VerifyEmailRequest(@Email String email, @NotBlank String otp) {}
  public record AuthResponse(String accessToken, String refreshToken, UserView user) {}
  public record UserView(String id, String email, String fullName, String role, boolean verified) {}
}
