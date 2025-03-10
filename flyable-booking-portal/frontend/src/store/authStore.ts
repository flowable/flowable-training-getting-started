import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { ANONYMOUS_USER, User } from '../api/authApi';

type AuthState = {
  jwt: string | null;
  setJwt: (jwt: string) => void;
  clearJwt: () => void;
  userInfos: User;
  setUserInfos: (user: User) => void;
  clearUserInfos: () => void;
};

/**
 * A simple store to manage the authentication state.
 */
export const useAuthStore = create<AuthState>()(
  persist(
    set => ({
      jwt: null,
      setJwt: (jwt: string) => set({ jwt }),
      clearJwt: () => set({ jwt: null }),
      userInfos: ANONYMOUS_USER,
      setUserInfos: (user: User) => set({ userInfos: user }),
      clearUserInfos: () => set({ userInfos: ANONYMOUS_USER }),
    }),
    {
      name: 'auth-storage',
    }
  )
);
