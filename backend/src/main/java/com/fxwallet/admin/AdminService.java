package com.fxwallet.admin;

import static com.fxwallet.admin.AdminModels.*;

import com.fxwallet.exception.ApiException;
import com.fxwallet.fraud.*;
import com.fxwallet.notification.RealtimeEventService;
import com.fxwallet.transfer.*;
import com.fxwallet.user.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
  private final FraudAlertRepository alerts;
  private final TransferRepository transfers;
  private final TransferService transferService;
  private final UserRepository users;
  private final RealtimeEventService realtime;

  public AdminService(FraudAlertRepository alerts, TransferRepository transfers, TransferService transferService, UserRepository users, RealtimeEventService realtime) {
    this.alerts = alerts;
    this.transfers = transfers;
    this.transferService = transferService;
    this.users = users;
    this.realtime = realtime;
  }

  public List<FraudAlertView> pending() {
    return alerts.findByResolutionOrderByCreatedAtDesc(FraudResolution.PENDING).stream().map(AdminService::view).toList();
  }

  @Transactional
  public void approve(UUID alertId, UUID adminId) {
    FraudAlert alert = alerts.findById(alertId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Fraud alert not found"));
    alert.setResolution(FraudResolution.APPROVED);
    alert.setReviewedBy(adminId);
    Transfer transfer = alert.getTransfer();
    transfer.setFraudFlagged(false);
    transferService.complete(transfer);
    realtime.publish("/topic/admin/fraud-alerts", "FRAUD_ALERT_APPROVED", view(alert));
  }

  @Transactional
  public void reject(UUID alertId, UUID adminId) {
    FraudAlert alert = alerts.findById(alertId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Fraud alert not found"));
    alert.setResolution(FraudResolution.REJECTED);
    alert.setReviewedBy(adminId);
    Transfer transfer = alert.getTransfer();
    transfer.getSenderWallet().setBalance(transfer.getSenderWallet().getBalance().add(transfer.getSourceAmount()).add(transfer.getFee()));
    transfer.setState(TransferState.FAILED);
    realtime.publish("/topic/admin/fraud-alerts", "FRAUD_ALERT_REJECTED", view(alert));
    realtime.publish("/topic/transfers", "TRANSFER_REJECTED", TransferService.view(transfer));
  }

  public List<UserAdminView> users() {
    return users.findAll().stream().map(user -> new UserAdminView(user.getId(), user.getEmail(), user.getFullName(), user.getRole().name(), user.isVerified(), user.getCreatedAt())).toList();
  }

  public void suspend(UUID id) {
    users.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
  }

  private static FraudAlertView view(FraudAlert alert) {
    return new FraudAlertView(alert.getId(), alert.getUser().getId(), alert.getUser().getEmail(), alert.getRuleTriggered(), alert.getResolution(), TransferService.view(alert.getTransfer()), alert.getCreatedAt());
  }
}
