package com.fxwallet.transfer;

import com.fxwallet.wallet.CurrencyCode;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class TransferModels {
  private TransferModels() {}
  public record TransferRequest(UUID senderWalletId, String receiverEmail, UUID receiverWalletId, String externalDestination, CurrencyCode targetCurrency, BigDecimal amount) {}
  public record TransferView(UUID id, UUID senderWalletId, UUID receiverWalletId, String receiverEmail, String externalDestination, CurrencyCode sourceCurrency, CurrencyCode targetCurrency, BigDecimal sourceAmount, BigDecimal targetAmount, BigDecimal exchangeRate, BigDecimal fee, TransferState state, boolean fraudFlagged, Instant createdAt) {}
}
