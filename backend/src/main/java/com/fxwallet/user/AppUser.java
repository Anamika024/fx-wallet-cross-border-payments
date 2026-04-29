package com.fxwallet.user;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users")
public class AppUser {
  @Id
  private UUID id = UUID.randomUUID();

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(nullable = false)
  private Role role = Role.USER;

  @Column(name = "is_verified", nullable = false)
  private boolean verified;

  @Column(name = "otp_code")
  private String otpCode;

  @Column(name = "refresh_token")
  private String refreshToken;

  @Column(name = "totp_secret")
  private String totpSecret;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }
  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }
  public boolean isVerified() { return verified; }
  public void setVerified(boolean verified) { this.verified = verified; }
  public String getOtpCode() { return otpCode; }
  public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
  public String getRefreshToken() { return refreshToken; }
  public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
  public String getTotpSecret() { return totpSecret; }
  public void setTotpSecret(String totpSecret) { this.totpSecret = totpSecret; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
