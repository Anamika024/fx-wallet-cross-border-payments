package com.fxwallet.fx;

import static com.fxwallet.fx.FxModels.*;

import com.fxwallet.exception.ApiException;
import com.fxwallet.transaction.TransactionStatus;
import com.fxwallet.transaction.TransactionType;
import com.fxwallet.user.AppUser;
import com.fxwallet.wallet.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FxQuoteService {
  private final ExchangeRateService rates;
  private final WalletService wallets;
  private final WalletRepository walletRepository;
  private final BigDecimal feePercent;
  private final Map<UUID, QuoteView> quoteStore = new ConcurrentHashMap<>();

  public FxQuoteService(ExchangeRateService rates, WalletService wallets, WalletRepository walletRepository, @Value("${app.platform-fee-percent}") BigDecimal feePercent) {
    this.rates = rates;
    this.wallets = wallets;
    this.walletRepository = walletRepository;
    this.feePercent = feePercent;
  }

  public QuoteView quote(AppUser user, QuoteRequest request) {
    Wallet source = wallets.requireOwned(user, request.sourceWalletId());
    BigDecimal rate = rates.rate(source.getCurrencyCode(), request.targetCurrency());
    BigDecimal fee = request.amount().multiply(feePercent).setScale(4, RoundingMode.HALF_UP);
    BigDecimal target = request.amount().subtract(fee).multiply(rate).setScale(4, RoundingMode.HALF_UP);
    QuoteView quote = new QuoteView(UUID.randomUUID(), source.getCurrencyCode(), request.targetCurrency(), request.amount(), target, rate, fee, Instant.now().plusSeconds(30));
    quoteStore.put(quote.quoteId(), quote);
    return quote;
  }

  @Transactional
  public QuoteView convert(AppUser user, UUID quoteId) {
    QuoteView quote = quoteStore.get(quoteId);
    if (quote == null || quote.expiresAt().isBefore(Instant.now())) {
      throw new ApiException(HttpStatus.GONE, "Quote expired");
    }
    Wallet source = walletRepository.findByUserAndCurrencyCode(user, quote.from()).orElseThrow();
    Wallet target = walletRepository.findByUserAndCurrencyCode(user, quote.to()).orElseGet(() -> {
      Wallet wallet = new Wallet();
      wallet.setUser(user);
      wallet.setCurrencyCode(quote.to());
      return walletRepository.save(wallet);
    });
    BigDecimal totalDebit = quote.sourceAmount();
    if (source.getBalance().compareTo(totalDebit) < 0) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Insufficient balance");
    }
    UUID reference = UUID.randomUUID();
    source.setBalance(source.getBalance().subtract(totalDebit));
    target.setBalance(target.getBalance().add(quote.targetAmount()));
    wallets.record(source, TransactionType.FX_CONVERSION, totalDebit.negate(), TransactionStatus.COMPLETED, "FX conversion to " + quote.to(), reference);
    wallets.record(target, TransactionType.FX_CONVERSION, quote.targetAmount(), TransactionStatus.COMPLETED, "FX conversion from " + quote.from(), reference);
    quoteStore.remove(quoteId);
    return quote;
  }
}
