package com.fxwallet.transfer;

import static com.fxwallet.transfer.TransferModels.*;

import com.fxwallet.exception.ApiException;
import com.fxwallet.fraud.*;
import com.fxwallet.fx.ExchangeRateService;
import com.fxwallet.notification.EmailService;
import com.fxwallet.notification.RealtimeEventService;
import com.fxwallet.transaction.TransactionStatus;
import com.fxwallet.transaction.TransactionType;
import com.fxwallet.user.AppUser;
import com.fxwallet.wallet.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {
  private final WalletService walletService;
  private final WalletRepository walletRepository;
  private final TransferRepository transfers;
  private final FraudAlertRepository fraudAlerts;
  private final FraudRuleEngine fraudRuleEngine;
  private final ExchangeRateService rates;
  private final EmailService emailService;
  private final RealtimeEventService realtime;
  private final BigDecimal flatFee;
  private final BigDecimal percentFee;

  public TransferService(WalletService walletService, WalletRepository walletRepository, TransferRepository transfers, FraudAlertRepository fraudAlerts, FraudRuleEngine fraudRuleEngine, ExchangeRateService rates, EmailService emailService, RealtimeEventService realtime, @Value("${app.transfer-flat-fee}") BigDecimal flatFee, @Value("${app.transfer-percent-fee}") BigDecimal percentFee) {
    this.walletService = walletService;
    this.walletRepository = walletRepository;
    this.transfers = transfers;
    this.fraudAlerts = fraudAlerts;
    this.fraudRuleEngine = fraudRuleEngine;
    this.rates = rates;
    this.emailService = emailService;
    this.realtime = realtime;
    this.flatFee = flatFee;
    this.percentFee = percentFee;
  }

  @Transactional
  public TransferView initiate(AppUser user, TransferRequest request) {
    Wallet sender = walletService.requireOwned(user, request.senderWalletId());
    Wallet receiver = null;
    if (request.receiverWalletId() != null) {
      receiver = walletRepository.findById(request.receiverWalletId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Receiver wallet not found"));
    }
    BigDecimal fee = flatFee.add(request.amount().multiply(percentFee)).setScale(4, RoundingMode.HALF_UP);
    BigDecimal totalDebit = request.amount().add(fee);
    if (sender.getBalance().compareTo(totalDebit) < 0) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Insufficient balance including fees");
    }
    BigDecimal rate = rates.rate(sender.getCurrencyCode(), request.targetCurrency());
    BigDecimal targetAmount = request.amount().multiply(rate).setScale(4, RoundingMode.HALF_UP);
    String rule = fraudRuleEngine.evaluate(user, sender.getCurrencyCode(), request.amount());
    Transfer transfer = new Transfer();
    transfer.setSenderWallet(sender);
    transfer.setReceiverWallet(receiver);
    transfer.setReceiverEmail(request.receiverEmail());
    transfer.setExternalDestination(request.externalDestination());
    transfer.setSourceCurrency(sender.getCurrencyCode());
    transfer.setTargetCurrency(request.targetCurrency());
    transfer.setSourceAmount(request.amount());
    transfer.setTargetAmount(targetAmount);
    transfer.setExchangeRate(rate);
    transfer.setFee(fee);
    transfer.setState(rule == null ? TransferState.PROCESSING : TransferState.REVIEW);
    transfer.setFraudFlagged(rule != null);
    sender.setBalance(sender.getBalance().subtract(totalDebit));
    transfer = transfers.save(transfer);
    walletService.record(sender, TransactionType.TRANSFER, totalDebit.negate(), rule == null ? TransactionStatus.COMPLETED : TransactionStatus.REVIEW, "Transfer sent", transfer.getId());
    if (rule != null) {
      FraudAlert alert = new FraudAlert();
      alert.setTransfer(transfer);
      alert.setUser(user);
      alert.setRuleTriggered(rule);
      fraudAlerts.save(alert);
      emailService.send("admin@fxwallet.local", "Fraud alert " + rule, "Review transfer " + transfer.getId());
      realtime.publish("/topic/admin/fraud-alerts", "FRAUD_ALERT_CREATED", TransferService.view(transfer));
    } else {
      complete(transfer);
    }
    realtime.publish("/topic/transfers", "TRANSFER_UPDATED", TransferService.view(transfer));
    return view(transfer);
  }

  @Transactional
  public void complete(Transfer transfer) {
    if (transfer.getReceiverWallet() != null) {
      Wallet receiver = transfer.getReceiverWallet();
      receiver.setBalance(receiver.getBalance().add(transfer.getTargetAmount()));
      walletService.record(receiver, TransactionType.CREDIT, transfer.getTargetAmount(), TransactionStatus.COMPLETED, "Transfer received", transfer.getId());
      emailService.send(receiver.getUser().getEmail(), "Transfer received", "You received " + transfer.getTargetAmount() + " " + receiver.getCurrencyCode());
    }
    transfer.setState(TransferState.COMPLETED);
    emailService.send(transfer.getSenderWallet().getUser().getEmail(), "Transfer completed", "Transfer " + transfer.getId() + " completed");
    realtime.publish("/topic/transfers", "TRANSFER_COMPLETED", TransferService.view(transfer));
  }

  public List<TransferView> list(AppUser user) {
    return transfers.findForUser(user).stream().map(TransferService::view).toList();
  }

  public TransferView get(AppUser user, UUID id) {
    Transfer transfer = transfers.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Transfer not found"));
    if (!transfer.getSenderWallet().getUser().getId().equals(user.getId()) &&
        (transfer.getReceiverWallet() == null || !transfer.getReceiverWallet().getUser().getId().equals(user.getId()))) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Transfer does not belong to current user");
    }
    return view(transfer);
  }

  @Transactional
  public void cancel(AppUser user, UUID id) {
    Transfer transfer = transfers.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Transfer not found"));
    if (!transfer.getSenderWallet().getUser().getId().equals(user.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Transfer does not belong to current user");
    }
    if (transfer.getState() != TransferState.INITIATED && transfer.getState() != TransferState.REVIEW) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Only initiated or review transfers can be cancelled");
    }
    transfer.getSenderWallet().setBalance(transfer.getSenderWallet().getBalance().add(transfer.getSourceAmount()).add(transfer.getFee()));
    transfer.setState(TransferState.FAILED);
    realtime.publish("/topic/transfers", "TRANSFER_CANCELLED", TransferService.view(transfer));
  }

  public static TransferView view(Transfer transfer) {
    return new TransferView(transfer.getId(), transfer.getSenderWallet().getId(),
        transfer.getReceiverWallet() == null ? null : transfer.getReceiverWallet().getId(),
        transfer.getReceiverEmail(), transfer.getExternalDestination(), transfer.getSourceCurrency(), transfer.getTargetCurrency(),
        transfer.getSourceAmount(), transfer.getTargetAmount(), transfer.getExchangeRate(), transfer.getFee(), transfer.getState(), transfer.isFraudFlagged(), transfer.getCreatedAt());
  }
}
