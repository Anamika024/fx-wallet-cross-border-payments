package com.fxwallet.fx;

import com.fxwallet.wallet.CurrencyCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {
  private static final Map<CurrencyCode, BigDecimal> INR_RATES = Map.of(
      CurrencyCode.INR, BigDecimal.ONE,
      CurrencyCode.USD, new BigDecimal("0.0120"),
      CurrencyCode.EUR, new BigDecimal("0.0111"),
      CurrencyCode.GBP, new BigDecimal("0.0095"),
      CurrencyCode.AED, new BigDecimal("0.0441"),
      CurrencyCode.SGD, new BigDecimal("0.0162"));

  public BigDecimal rate(CurrencyCode from, CurrencyCode to) {
    if (from == to) return BigDecimal.ONE.setScale(6, RoundingMode.HALF_UP);
    BigDecimal fromToInr = BigDecimal.ONE.divide(INR_RATES.get(from), 10, RoundingMode.HALF_UP);
    return fromToInr.multiply(INR_RATES.get(to)).setScale(6, RoundingMode.HALF_UP);
  }

  public Map<CurrencyCode, BigDecimal> rates(CurrencyCode base) {
    Map<CurrencyCode, BigDecimal> result = new EnumMap<>(CurrencyCode.class);
    for (CurrencyCode code : CurrencyCode.values()) {
      result.put(code, rate(base, code));
    }
    return result;
  }

  public List<FxModels.HistoryPoint> history(CurrencyCode from, CurrencyCode to) {
    BigDecimal base = rate(from, to);
    return java.util.stream.IntStream.rangeClosed(0, 6).mapToObj(i -> {
      BigDecimal drift = new BigDecimal(i - 3).multiply(new BigDecimal("0.0017"));
      return new FxModels.HistoryPoint(LocalDate.now().minusDays(6L - i).toString(), base.add(drift).max(new BigDecimal("0.0001")));
    }).toList();
  }
}
