import api from '@/lib/api';
import { AuthResponse } from '@/types';

export const authService = {
  async register(data: {
    email: string;
    motDePasse: string;
    prenom: string;
    nom: string;
    role?: string;
    nomBoutique?: string;
    descriptionBoutique?: string;
  }): Promise<AuthResponse> {
    const response = await api.post('/api/auth/register', data);
    return response.data;
  },

  async login(email: string, motDePasse: string): Promise<AuthResponse> {
    const response = await api.post('/api/auth/login', { email, motDePasse });
    return response.data;
  },

  async logout(refreshToken: string): Promise<void> {
    await api.post('/api/auth/logout', { refreshToken });
  },

  async refresh(refreshToken: string): Promise<AuthResponse> {
    const response = await api.post('/api/auth/refresh', { refreshToken });
    return response.data;
  },
};
