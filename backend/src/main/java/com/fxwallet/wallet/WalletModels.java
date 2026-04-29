package com.fxwallet.wallet;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class WalletModels {
  private WalletModels() {}

  public record CreateWalletRequest(CurrencyCode currency) {}
  public record WalletView(UUID id, CurrencyCode currencyCode, BigDecimal balance, boolean defaultWallet, WalletStatus status) {}
  public record TransactionView(UUID id, String type, BigDecimal amount, CurrencyCode currencyCode, String status, String description, Instant createdAt) {}
  public record WalletDetail(WalletView wallet, List<TransactionView> transactions) {}
}
