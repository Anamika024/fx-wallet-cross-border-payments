package com.fxwallet.fraud;

import com.fxwallet.fx.ExchangeRateService;
import com.fxwallet.transfer.TransferRepository;
import com.fxwallet.user.AppUser;
import com.fxwallet.wallet.CurrencyCode;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FraudRuleEngine {
  private final ExchangeRateService rates;
  private final TransferRepository transfers;
  private final BigDecimal highValueUsd;

  public FraudRuleEngine(ExchangeRateService rates, TransferRepository transfers, @Value("${app.fraud-high-value-usd}") BigDecimal highValueUsd) {
    this.rates = rates;
    this.transfers = transfers;
    this.highValueUsd = highValueUsd;
  }

  public String evaluate(AppUser user, CurrencyCode sourceCurrency, BigDecimal sourceAmount) {
    BigDecimal usdValue = sourceAmount.multiply(rates.rate(sourceCurrency, CurrencyCode.USD));
    if (usdValue.compareTo(highValueUsd) > 0) {
      return "HIGH_VALUE";
    }
    BigDecimal largeInSource = highValueUsd.divide(rates.rate(sourceCurrency, CurrencyCode.USD), 4, java.math.RoundingMode.HALF_UP);
    long count = transfers.countLargeTransfersSince(user, largeInSource, Instant.now().minusSeconds(3600));
    if (count >= 3) {
      return "VELOCITY";
    }
    return null;
  }
}
