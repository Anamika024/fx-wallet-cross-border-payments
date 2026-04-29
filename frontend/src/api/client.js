import axios from 'axios';
import { useAuthStore } from '../store/authStore.js';
import { API_BASE_URL } from './config.js';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});

api.interceptors.request.use((config) => {
  const token = useAuthStore.getState().accessToken;
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) useAuthStore.getState().logout();
    return Promise.reject(error);
  }
);
