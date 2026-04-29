package com.fxwallet.fraud;

import com.fxwallet.transfer.Transfer;
import com.fxwallet.user.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "fraud_alerts")
public class FraudAlert {
  @Id
  private UUID id = UUID.randomUUID();

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "transfer_id")
  private Transfer transfer;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private AppUser user;

  @Column(name = "rule_triggered", nullable = false)
  private String ruleTriggered;

  @Column(name = "reviewed_by")
  private UUID reviewedBy;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(nullable = false)
  private FraudResolution resolution = FraudResolution.PENDING;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public Transfer getTransfer() { return transfer; }
  public void setTransfer(Transfer transfer) { this.transfer = transfer; }
  public AppUser getUser() { return user; }
  public void setUser(AppUser user) { this.user = user; }
  public String getRuleTriggered() { return ruleTriggered; }
  public void setRuleTriggered(String ruleTriggered) { this.ruleTriggered = ruleTriggered; }
  public UUID getReviewedBy() { return reviewedBy; }
  public void setReviewedBy(UUID reviewedBy) { this.reviewedBy = reviewedBy; }
  public FraudResolution getResolution() { return resolution; }
  public void setResolution(FraudResolution resolution) { this.resolution = resolution; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
