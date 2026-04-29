package com.fxwallet.admin;

import com.fxwallet.fraud.FraudResolution;
import com.fxwallet.transfer.TransferModels.TransferView;
import java.time.Instant;
import java.util.UUID;

public final class AdminModels {
  private AdminModels() {}
  public record FraudAlertView(UUID id, UUID userId, String userEmail, String ruleTriggered, FraudResolution resolution, TransferView transfer, Instant createdAt) {}
  public record UserAdminView(UUID id, String email, String fullName, String role, boolean verified, Instant createdAt) {}
}
