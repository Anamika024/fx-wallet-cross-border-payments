package com.fxwallet.fraud;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudAlertRepository extends JpaRepository<FraudAlert, UUID> {
  List<FraudAlert> findByResolutionOrderByCreatedAtDesc(FraudResolution resolution);
}
