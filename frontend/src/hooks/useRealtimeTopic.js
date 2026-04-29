import { useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { WS_BASE_URL } from '../api/config.js';

export function useRealtimeTopic(topic, onMessage) {
  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(`${WS_BASE_URL}/ws`),
      reconnectDelay: 5000,
      onConnect: () => client.subscribe(topic, (message) => onMessage(JSON.parse(message.body)))
    });
    client.activate();
    return () => client.deactivate();
  }, [topic, onMessage]);
}
