package com.fxwallet.wallet;

import com.fxwallet.user.AppUser;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "wallets", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "currency_code"}))
public class Wallet {
  @Id
  private UUID id = UUID.randomUUID();

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private AppUser user;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "currency_code", nullable = false, length = 3)
  private CurrencyCode currencyCode;

  @Column(nullable = false, precision = 18, scale = 4)
  private BigDecimal balance = BigDecimal.ZERO;

  @Column(name = "is_default", nullable = false)
  private boolean defaultWallet;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(nullable = false)
  private WalletStatus status = WalletStatus.ACTIVE;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public AppUser getUser() { return user; }
  public void setUser(AppUser user) { this.user = user; }
  public CurrencyCode getCurrencyCode() { return currencyCode; }
  public void setCurrencyCode(CurrencyCode currencyCode) { this.currencyCode = currencyCode; }
  public BigDecimal getBalance() { return balance; }
  public void setBalance(BigDecimal balance) { this.balance = balance; }
  public boolean isDefaultWallet() { return defaultWallet; }
  public void setDefaultWallet(boolean defaultWallet) { this.defaultWallet = defaultWallet; }
  public WalletStatus getStatus() { return status; }
  public void setStatus(WalletStatus status) { this.status = status; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
