import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { fxApi } from '../api/endpoints.js';
import { WS_BASE_URL } from '../api/config.js';

export function useFxTicker() {
  const [rates, setRates] = useState({});

  useEffect(() => {
    let timer;
    fxApi.rates('INR').then((data) => setRates(data.rates)).catch(() => {});
    timer = setInterval(() => fxApi.rates('INR').then((data) => setRates(data.rates)).catch(() => {}), 30000);
    const client = new Client({
      webSocketFactory: () => new SockJS(`${WS_BASE_URL}/ws`),
      reconnectDelay: 5000,
      onConnect: () => client.subscribe('/topic/fx-rates', (message) => setRates(JSON.parse(message.body)))
    });
    client.activate();
    return () => {
      clearInterval(timer);
      client.deactivate();
    };
  }, []);

  return rates;
}
