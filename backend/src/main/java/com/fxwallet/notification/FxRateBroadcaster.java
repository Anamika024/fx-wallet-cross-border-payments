package com.fxwallet.notification;

import com.fxwallet.fx.ExchangeRateService;
import com.fxwallet.wallet.CurrencyCode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FxRateBroadcaster {
  private final SimpMessagingTemplate messagingTemplate;
  private final ExchangeRateService rates;

  public FxRateBroadcaster(SimpMessagingTemplate messagingTemplate, ExchangeRateService rates) {
    this.messagingTemplate = messagingTemplate;
    this.rates = rates;
  }

  @Scheduled(fixedRate = 30000)
  public void publishRates() {
    messagingTemplate.convertAndSend("/topic/fx-rates", rates.rates(CurrencyCode.INR));
  }
}
