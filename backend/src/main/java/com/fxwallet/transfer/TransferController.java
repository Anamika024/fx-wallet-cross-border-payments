package com.fxwallet.transfer;

import static com.fxwallet.transfer.TransferModels.*;

import com.fxwallet.auth.AuthService;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
  private final TransferService transferService;
  private final AuthService authService;

  public TransferController(TransferService transferService, AuthService authService) {
    this.transferService = transferService;
    this.authService = authService;
  }

  @PostMapping
  TransferView initiate(Principal principal, @RequestBody TransferRequest request) {
    return transferService.initiate(authService.currentUser(principal.getName()), request);
  }

  @GetMapping
  List<TransferView> list(Principal principal) {
    return transferService.list(authService.currentUser(principal.getName()));
  }

  @GetMapping("/{id}")
  TransferView get(Principal principal, @PathVariable UUID id) {
    return transferService.get(authService.currentUser(principal.getName()), id);
  }

  @PostMapping("/{id}/cancel")
  ResponseEntity<Void> cancel(Principal principal, @PathVariable UUID id) {
    transferService.cancel(authService.currentUser(principal.getName()), id);
    return ResponseEntity.noContent().build();
  }
}
