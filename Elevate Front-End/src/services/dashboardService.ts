import api from './api';
import type { RecruiterStatsResponse } from '../types';

const DASHBOARD_BASE = '/api/dashboard';

export const dashboardService = {
  getRecruiterStats: async (): Promise<RecruiterStatsResponse> => {
    const response = await api.get<RecruiterStatsResponse>(`${DASHBOARD_BASE}/recruiter/stats`);
    return response.data;
  },
};
