package com.fxwallet.fx;

import static com.fxwallet.fx.FxModels.*;

import com.fxwallet.auth.AuthService;
import com.fxwallet.wallet.CurrencyCode;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fx")
public class FxController {
  private final ExchangeRateService rates;
  private final FxQuoteService quotes;
  private final AuthService authService;
  private final java.math.BigDecimal feePercent;

  public FxController(ExchangeRateService rates, FxQuoteService quotes, AuthService authService, @Value("${app.platform-fee-percent}") java.math.BigDecimal feePercent) {
    this.rates = rates;
    this.quotes = quotes;
    this.authService = authService;
    this.feePercent = feePercent;
  }

  @GetMapping("/rates")
  RatesResponse rates(@RequestParam(defaultValue = "INR") CurrencyCode base) {
    return new RatesResponse(base, rates.rates(base));
  }

  @GetMapping("/rates/{from}/{to}")
  RateView pair(@PathVariable CurrencyCode from, @PathVariable CurrencyCode to) {
    return new RateView(from, to, rates.rate(from, to), feePercent, java.time.Instant.now().plusSeconds(300));
  }

  @PostMapping("/quote")
  QuoteView quote(Principal principal, @RequestBody QuoteRequest request) {
    return quotes.quote(authService.currentUser(principal.getName()), request);
  }

  @PostMapping("/convert")
  QuoteView convert(Principal principal, @RequestBody ConvertRequest request) {
    return quotes.convert(authService.currentUser(principal.getName()), request.quoteId());
  }

  @GetMapping("/history/{from}/{to}")
  HistoryResponse history(@PathVariable CurrencyCode from, @PathVariable CurrencyCode to) {
    return new HistoryResponse(from, to, rates.history(from, to));
  }
}
