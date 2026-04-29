package com.fxwallet.wallet;

import static com.fxwallet.wallet.WalletModels.*;

import com.fxwallet.auth.AuthService;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
  private final WalletService walletService;
  private final AuthService authService;

  public WalletController(WalletService walletService, AuthService authService) {
    this.walletService = walletService;
    this.authService = authService;
  }

  @GetMapping
  List<WalletView> list(Principal principal) {
    return walletService.list(authService.currentUser(principal.getName()));
  }

  @PostMapping
  WalletView create(Principal principal, @RequestBody CreateWalletRequest request) {
    return walletService.create(authService.currentUser(principal.getName()), request.currency());
  }

  @GetMapping("/{id}")
  WalletDetail get(Principal principal, @PathVariable UUID id) {
    return walletService.detail(authService.currentUser(principal.getName()), id);
  }

  @GetMapping("/{id}/transactions")
  List<TransactionView> transactions(Principal principal, @PathVariable UUID id) {
    return walletService.detail(authService.currentUser(principal.getName()), id).transactions();
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> close(Principal principal, @PathVariable UUID id) {
    walletService.close(authService.currentUser(principal.getName()), id);
    return ResponseEntity.noContent().build();
  }
}
