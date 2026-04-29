import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useAuthStore = create(
  persist(
    (set) => ({
      accessToken: null,
      refreshToken: null,
      user: null,
      setSession: (session) => set({ accessToken: session.accessToken, refreshToken: session.refreshToken, user: session.user }),
      logout: () => set({ accessToken: null, refreshToken: null, user: null })
    }),
    { name: 'fx-wallet-session' }
  )
);
