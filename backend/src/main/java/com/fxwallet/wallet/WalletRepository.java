package com.fxwallet.wallet;

import com.fxwallet.user.AppUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
  List<Wallet> findByUserOrderByDefaultWalletDescCurrencyCodeAsc(AppUser user);
  Optional<Wallet> findByUserAndCurrencyCode(AppUser user, CurrencyCode currencyCode);
}
