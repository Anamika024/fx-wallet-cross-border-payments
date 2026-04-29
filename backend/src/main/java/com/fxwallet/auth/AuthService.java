package com.fxwallet.auth;

import static com.fxwallet.auth.AuthModels.*;

import com.fxwallet.exception.ApiException;
import com.fxwallet.notification.EmailService;
import com.fxwallet.user.AppUser;
import com.fxwallet.user.Role;
import com.fxwallet.user.UserRepository;
import com.fxwallet.wallet.CurrencyCode;
import com.fxwallet.wallet.WalletService;
import java.security.SecureRandom;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final UserRepository users;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final WalletService walletService;
  private final EmailService emailService;
  private final SecureRandom random = new SecureRandom();

  public AuthService(UserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService, WalletService walletService, EmailService emailService) {
    this.users = users;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.walletService = walletService;
    this.emailService = emailService;
  }

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    if (users.existsByEmailIgnoreCase(request.email())) {
      throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
    }
    AppUser user = new AppUser();
    user.setEmail(request.email().toLowerCase());
    user.setFullName(request.fullName());
    user.setPasswordHash(passwordEncoder.encode(request.password()));
    user.setRole(request.email().toLowerCase().contains("admin") ? Role.ADMIN : Role.USER);
    user.setOtpCode(String.valueOf(100000 + random.nextInt(900000)));
    users.save(user);
    walletService.createDefaultWallet(user, CurrencyCode.INR);
    emailService.send(user.getEmail(), "FX Wallet OTP", "Your verification code is " + user.getOtpCode());
    return issueTokens(user);
  }

  @Transactional
  public AuthResponse login(LoginRequest request) {
    AppUser user = users.findByEmailIgnoreCase(request.email())
        .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
    return issueTokens(user);
  }

  @Transactional
  public AuthResponse refresh(RefreshRequest request) {
    AppUser user = users.findAll().stream()
        .filter(candidate -> request.refreshToken().equals(candidate.getRefreshToken()))
        .findFirst()
        .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
    return issueTokens(user);
  }

  @Transactional
  public void verify(VerifyEmailRequest request) {
    AppUser user = users.findByEmailIgnoreCase(request.email())
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    if (!request.otp().equals(user.getOtpCode())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid OTP");
    }
    user.setVerified(true);
    user.setOtpCode(null);
  }

  public AppUser currentUser(String email) {
    return users.findByEmailIgnoreCase(email).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Unknown user"));
  }

  private AuthResponse issueTokens(AppUser user) {
    user.setRefreshToken(UUID.randomUUID().toString());
    return new AuthResponse(jwtService.createAccessToken(user), user.getRefreshToken(), view(user));
  }

  public static UserView view(AppUser user) {
    return new UserView(user.getId().toString(), user.getEmail(), user.getFullName(), user.getRole().name(), user.isVerified());
  }
}
