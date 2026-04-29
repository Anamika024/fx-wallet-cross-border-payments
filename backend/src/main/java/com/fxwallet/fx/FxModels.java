package com.fxwallet.fx;

import com.fxwallet.wallet.CurrencyCode;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FxModels {
  private FxModels() {}
  public record RateView(CurrencyCode from, CurrencyCode to, BigDecimal rate, BigDecimal platformFeePercent, Instant expiresAt) {}
  public record QuoteRequest(UUID sourceWalletId, CurrencyCode targetCurrency, BigDecimal amount) {}
  public record QuoteView(UUID quoteId, CurrencyCode from, CurrencyCode to, BigDecimal sourceAmount, BigDecimal targetAmount, BigDecimal rate, BigDecimal fee, Instant expiresAt) {}
  public record ConvertRequest(UUID quoteId) {}
  public record HistoryPoint(String date, BigDecimal rate) {}
  public record RatesResponse(CurrencyCode base, Map<CurrencyCode, BigDecimal> rates) {}
  public record HistoryResponse(CurrencyCode from, CurrencyCode to, List<HistoryPoint> points) {}
}
