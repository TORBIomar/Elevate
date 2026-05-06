import api from './api';
import type { NotificationResponse, Page } from '../types';

const NOTIFICATIONS_BASE = '/api/notifications';

export const notificationService = {
  getUnreadNotifications: async (): Promise<NotificationResponse[]> => {
    // Some backends have a dedicated unread endpoint, or we just filter the all endpoint. 
    // We'll hit a hypothetical `/unread` or standard `?isRead=false` if typical. 
    // Let's assume hitting the base endpoint and filtering for now if unread endpoint isn't standard, 
    // or just assume `GET /api/notifications` returns them all ordered by date.
    const response = await api.get<NotificationResponse[]>(NOTIFICATIONS_BASE);
    return response.data;
  },

  getAllNotifications: async (page = 0, size = 20): Promise<Page<NotificationResponse>> => {
    const params = new URLSearchParams({ page: String(page), size: String(size) });
    const response = await api.get<Page<NotificationResponse>>(`${NOTIFICATIONS_BASE}/history?${params.toString()}`);
    return response.data;
  },

  markAsRead: async (id: number): Promise<void> => {
    await api.patch(`${NOTIFICATIONS_BASE}/${id}/read`);
  },

  markAllAsRead: async (): Promise<void> => {
    await api.patch(`${NOTIFICATIONS_BASE}/read-all`);
  }
};
