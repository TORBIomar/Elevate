import api from './api';
import type { LoginRequest, LoginResponse, RegisterRequest, UserResponse } from '../types';

const AUTH_BASE = '/api/auth';

export const authService = {
  async login(data: LoginRequest): Promise<LoginResponse> {
    const response = await api.post<LoginResponse>(`${AUTH_BASE}/login`, data);
    const loginData = response.data;

    // Persist token and user info
    localStorage.setItem('elevate_token', loginData.token);
    localStorage.setItem('elevate_user', JSON.stringify({
      userId: loginData.userId,
      email: loginData.email,
      firstName: loginData.firstName,
      lastName: loginData.lastName,
      role: loginData.role,
    }));

    return loginData;
  },

  async register(data: RegisterRequest): Promise<UserResponse> {
    const response = await api.post<UserResponse>(`${AUTH_BASE}/register`, data);
    return response.data;
  },

  logout(): void {
    localStorage.removeItem('elevate_token');
    localStorage.removeItem('elevate_user');
  },

  getStoredToken(): string | null {
    return localStorage.getItem('elevate_token');
  },

  getStoredUser() {
    const raw = localStorage.getItem('elevate_user');
    if (!raw) return null;
    try {
      return JSON.parse(raw);
    } catch {
      return null;
    }
  },

  isAuthenticated(): boolean {
    return !!localStorage.getItem('elevate_token');
  },
};
