package com.fxwallet.transfer;

import com.fxwallet.wallet.CurrencyCode;
import com.fxwallet.wallet.Wallet;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "transfers")
public class Transfer {
  @Id
  private UUID id = UUID.randomUUID();

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_wallet_id")
  private Wallet senderWallet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_wallet_id")
  private Wallet receiverWallet;

  @Column(name = "receiver_email")
  private String receiverEmail;

  @Column(name = "external_destination")
  private String externalDestination;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "source_currency", nullable = false, length = 3)
  private CurrencyCode sourceCurrency;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "target_currency", nullable = false, length = 3)
  private CurrencyCode targetCurrency;

  @Column(name = "source_amount", nullable = false, precision = 18, scale = 4)
  private BigDecimal sourceAmount;

  @Column(name = "target_amount", nullable = false, precision = 18, scale = 4)
  private BigDecimal targetAmount;

  @Column(name = "exchange_rate", nullable = false, precision = 18, scale = 6)
  private BigDecimal exchangeRate;

  @Column(nullable = false, precision = 18, scale = 4)
  private BigDecimal fee;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(nullable = false)
  private TransferState state;

  @Column(name = "fraud_flagged", nullable = false)
  private boolean fraudFlagged;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public Wallet getSenderWallet() { return senderWallet; }
  public void setSenderWallet(Wallet senderWallet) { this.senderWallet = senderWallet; }
  public Wallet getReceiverWallet() { return receiverWallet; }
  public void setReceiverWallet(Wallet receiverWallet) { this.receiverWallet = receiverWallet; }
  public String getReceiverEmail() { return receiverEmail; }
  public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }
  public String getExternalDestination() { return externalDestination; }
  public void setExternalDestination(String externalDestination) { this.externalDestination = externalDestination; }
  public CurrencyCode getSourceCurrency() { return sourceCurrency; }
  public void setSourceCurrency(CurrencyCode sourceCurrency) { this.sourceCurrency = sourceCurrency; }
  public CurrencyCode getTargetCurrency() { return targetCurrency; }
  public void setTargetCurrency(CurrencyCode targetCurrency) { this.targetCurrency = targetCurrency; }
  public BigDecimal getSourceAmount() { return sourceAmount; }
  public void setSourceAmount(BigDecimal sourceAmount) { this.sourceAmount = sourceAmount; }
  public BigDecimal getTargetAmount() { return targetAmount; }
  public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }
  public BigDecimal getExchangeRate() { return exchangeRate; }
  public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
  public BigDecimal getFee() { return fee; }
  public void setFee(BigDecimal fee) { this.fee = fee; }
  public TransferState getState() { return state; }
  public void setState(TransferState state) { this.state = state; }
  public boolean isFraudFlagged() { return fraudFlagged; }
  public void setFraudFlagged(boolean fraudFlagged) { this.fraudFlagged = fraudFlagged; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
