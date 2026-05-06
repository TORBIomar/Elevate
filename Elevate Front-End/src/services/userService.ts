import api from './api';
import type { UserResponse, UpdateProfileRequest } from '../types';

const USERS_BASE = '/api/users';

export const userService = {
  async getProfile(): Promise<UserResponse> {
    const response = await api.get<UserResponse>(`${USERS_BASE}/profile`);
    return response.data;
  },

  async getUserById(id: number): Promise<UserResponse> {
    const response = await api.get<UserResponse>(`${USERS_BASE}/${id}`);
    return response.data;
  },

  async updateProfile(data: UpdateProfileRequest): Promise<UserResponse> {
    const response = await api.put<UserResponse>(`${USERS_BASE}/profile`, data);
    return response.data;
  },
};
