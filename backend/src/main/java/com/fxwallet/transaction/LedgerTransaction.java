package com.fxwallet.transaction;

import com.fxwallet.wallet.CurrencyCode;
import com.fxwallet.wallet.Wallet;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "transactions")
public class LedgerTransaction {
  @Id
  private UUID id = UUID.randomUUID();

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "wallet_id")
  private Wallet wallet;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(nullable = false)
  private TransactionType type;

  @Column(nullable = false, precision = 18, scale = 4)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "currency_code", nullable = false, length = 3)
  private CurrencyCode currencyCode;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(nullable = false)
  private TransactionStatus status;

  @Column(name = "reference_id")
  private UUID referenceId;

  @Column(nullable = false)
  private String description;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public Wallet getWallet() { return wallet; }
  public void setWallet(Wallet wallet) { this.wallet = wallet; }
  public TransactionType getType() { return type; }
  public void setType(TransactionType type) { this.type = type; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public CurrencyCode getCurrencyCode() { return currencyCode; }
  public void setCurrencyCode(CurrencyCode currencyCode) { this.currencyCode = currencyCode; }
  public TransactionStatus getStatus() { return status; }
  public void setStatus(TransactionStatus status) { this.status = status; }
  public UUID getReferenceId() { return referenceId; }
  public void setReferenceId(UUID referenceId) { this.referenceId = referenceId; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
