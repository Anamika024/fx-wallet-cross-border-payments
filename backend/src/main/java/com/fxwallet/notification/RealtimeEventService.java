package com.fxwallet.notification;

import java.time.Instant;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RealtimeEventService {
  private final SimpMessagingTemplate messagingTemplate;

  public RealtimeEventService(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void publish(String topic, String type, Object payload) {
    messagingTemplate.convertAndSend(topic, Map.of(
        "type", type,
        "payload", payload,
        "publishedAt", Instant.now().toString()));
  }
}
