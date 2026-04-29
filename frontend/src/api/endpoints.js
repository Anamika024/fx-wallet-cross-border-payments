import { api } from './client.js';

export const authApi = {
  login: (payload) => api.post('/api/auth/login', payload).then((r) => r.data),
  register: (payload) => api.post('/api/auth/register', payload).then((r) => r.data),
  verify: (payload) => api.post('/api/auth/verify-email', payload)
};

export const walletApi = {
  list: () => api.get('/api/wallets').then((r) => r.data),
  create: (currency) => api.post('/api/wallets', { currency }).then((r) => r.data),
  detail: (id) => api.get(`/api/wallets/${id}`).then((r) => r.data)
};

export const fxApi = {
  rates: (base = 'INR') => api.get(`/api/fx/rates?base=${base}`).then((r) => r.data),
  pair: (from, to) => api.get(`/api/fx/rates/${from}/${to}`).then((r) => r.data),
  quote: (payload) => api.post('/api/fx/quote', payload).then((r) => r.data),
  convert: (quoteId) => api.post('/api/fx/convert', { quoteId }).then((r) => r.data),
  history: (from, to) => api.get(`/api/fx/history/${from}/${to}`).then((r) => r.data)
};

export const transferApi = {
  send: (payload) => api.post('/api/transfers', payload).then((r) => r.data),
  list: () => api.get('/api/transfers').then((r) => r.data)
};

export const adminApi = {
  alerts: () => api.get('/api/admin/fraud-alerts').then((r) => r.data),
  approve: (id) => api.post(`/api/admin/fraud-alerts/${id}/approve`),
  reject: (id) => api.post(`/api/admin/fraud-alerts/${id}/reject`),
  users: () => api.get('/api/admin/users').then((r) => r.data)
};

export const analyticsApi = {
  dashboard: () => api.get('/api/analytics').then((r) => r.data)
};
