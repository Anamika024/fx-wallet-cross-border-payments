package com.fxwallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fxwallet.fraud.FraudRuleEngine;
import com.fxwallet.fx.ExchangeRateService;
import com.fxwallet.transfer.TransferRepository;
import com.fxwallet.user.AppUser;
import com.fxwallet.wallet.CurrencyCode;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FraudRuleEngineTest {
  @Test
  void flagsHighValueTransfers() {
    ExchangeRateService rates = mock(ExchangeRateService.class);
    TransferRepository transfers = mock(TransferRepository.class);
    when(rates.rate(CurrencyCode.INR, CurrencyCode.USD)).thenReturn(new BigDecimal("0.012"));
    FraudRuleEngine engine = new FraudRuleEngine(rates, transfers, new BigDecimal("5000"));
    assertThat(engine.evaluate(new AppUser(), CurrencyCode.INR, new BigDecimal("500000"))).isEqualTo("HIGH_VALUE");
  }
}
