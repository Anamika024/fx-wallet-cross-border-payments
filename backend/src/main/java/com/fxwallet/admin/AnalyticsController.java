package com.fxwallet.admin;

import com.fxwallet.auth.AuthService;
import com.fxwallet.transfer.TransferRepository;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.Month;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
  private final AuthService authService;
  private final TransferRepository transfers;

  public AnalyticsController(AuthService authService, TransferRepository transfers) {
    this.authService = authService;
    this.transfers = transfers;
  }

  @GetMapping
  Map<String, Object> dashboard(Principal principal) {
    var user = authService.currentUser(principal.getName());
    var rows = transfers.findForUser(user);
    var spendByCurrency = rows.stream().collect(java.util.stream.Collectors.groupingBy(
        transfer -> transfer.getSourceCurrency().name(),
        java.util.stream.Collectors.reducing(BigDecimal.ZERO, transfer -> transfer.getSourceAmount(), BigDecimal::add)));
    List<Map<String, Object>> monthly = java.util.Arrays.stream(Month.values()).limit(6)
        .map(month -> Map.<String, Object>of("month", month.name().substring(0, 3), "volume", rows.stream().map(t -> t.getSourceAmount()).reduce(BigDecimal.ZERO, BigDecimal::add)))
        .toList();
    return Map.of("spendByCurrency", spendByCurrency, "monthlyVolume", monthly);
  }
}
