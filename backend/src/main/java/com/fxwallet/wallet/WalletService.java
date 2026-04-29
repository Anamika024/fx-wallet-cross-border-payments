package com.fxwallet.wallet;

import static com.fxwallet.wallet.WalletModels.*;

import com.fxwallet.exception.ApiException;
import com.fxwallet.transaction.*;
import com.fxwallet.user.AppUser;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
  private final WalletRepository wallets;
  private final TransactionRepository transactions;

  public WalletService(WalletRepository wallets, TransactionRepository transactions) {
    this.wallets = wallets;
    this.transactions = transactions;
  }

  @Transactional
  public Wallet createDefaultWallet(AppUser user, CurrencyCode currency) {
    Wallet wallet = new Wallet();
    wallet.setUser(user);
    wallet.setCurrencyCode(currency);
    wallet.setDefaultWallet(true);
    wallet.setBalance(new BigDecimal("250000.0000"));
    wallet = wallets.save(wallet);
    record(wallet, TransactionType.CREDIT, wallet.getBalance(), TransactionStatus.COMPLETED, "Welcome funding", UUID.randomUUID());
    return wallet;
  }

  @Transactional
  public WalletView create(AppUser user, CurrencyCode currency) {
    wallets.findByUserAndCurrencyCode(user, currency).ifPresent(existing -> {
      throw new ApiException(HttpStatus.CONFLICT, "Wallet already exists for " + currency);
    });
    Wallet wallet = new Wallet();
    wallet.setUser(user);
    wallet.setCurrencyCode(currency);
    wallet.setDefaultWallet(false);
    return view(wallets.save(wallet));
  }

  public List<WalletView> list(AppUser user) {
    return wallets.findByUserOrderByDefaultWalletDescCurrencyCodeAsc(user).stream().map(WalletService::view).toList();
  }

  public Wallet requireOwned(AppUser user, UUID walletId) {
    Wallet wallet = wallets.findById(walletId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Wallet not found"));
    if (!wallet.getUser().getId().equals(user.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Wallet does not belong to current user");
    }
    if (wallet.getStatus() != WalletStatus.ACTIVE) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Wallet is not active");
    }
    return wallet;
  }

  public WalletDetail detail(AppUser user, UUID walletId) {
    Wallet wallet = requireOwned(user, walletId);
    return new WalletDetail(view(wallet), statement(wallet, 50));
  }

  public List<TransactionView> statement(Wallet wallet, int limit) {
    return transactions.findByWalletOrderByCreatedAtDesc(wallet, PageRequest.of(0, limit)).stream().map(tx ->
        new TransactionView(tx.getId(), tx.getType().name(), tx.getAmount(), tx.getCurrencyCode(), tx.getStatus().name(), tx.getDescription(), tx.getCreatedAt())
    ).toList();
  }

  @Transactional
  public void close(AppUser user, UUID walletId) {
    Wallet wallet = requireOwned(user, walletId);
    if (wallet.isDefaultWallet()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Default wallet cannot be closed");
    }
    if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Wallet balance must be zero before closing");
    }
    wallet.setStatus(WalletStatus.CLOSED);
  }

  public void record(Wallet wallet, TransactionType type, BigDecimal amount, TransactionStatus status, String description, UUID referenceId) {
    LedgerTransaction tx = new LedgerTransaction();
    tx.setWallet(wallet);
    tx.setType(type);
    tx.setAmount(amount);
    tx.setCurrencyCode(wallet.getCurrencyCode());
    tx.setStatus(status);
    tx.setDescription(description);
    tx.setReferenceId(referenceId);
    transactions.save(tx);
  }

  public static WalletView view(Wallet wallet) {
    return new WalletView(wallet.getId(), wallet.getCurrencyCode(), wallet.getBalance(), wallet.isDefaultWallet(), wallet.getStatus());
  }
}
