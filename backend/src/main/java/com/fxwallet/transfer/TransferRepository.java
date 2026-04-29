package com.fxwallet.transfer;

import com.fxwallet.user.AppUser;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
  @Query("select t from Transfer t where t.senderWallet.user = :user or t.receiverWallet.user = :user order by t.createdAt desc")
  List<Transfer> findForUser(@Param("user") AppUser user);

  @Query("select count(t) from Transfer t where t.senderWallet.user = :user and t.sourceAmount >= :minAmount and t.createdAt >= :since")
  long countLargeTransfersSince(@Param("user") AppUser user, @Param("minAmount") java.math.BigDecimal minAmount, @Param("since") Instant since);
}
