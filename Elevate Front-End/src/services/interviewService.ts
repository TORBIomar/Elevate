import api from './api';
import type { InterviewRequest, InterviewResponse, InterviewStatus } from '../types';

const INTERVIEWS_BASE = '/api/interviews';

export const interviewService = {
  scheduleInterview: async (data: InterviewRequest): Promise<InterviewResponse> => {
    const response = await api.post<InterviewResponse>(INTERVIEWS_BASE, data);
    return response.data;
  },

  getMyInterviews: async (): Promise<InterviewResponse[]> => {
    const response = await api.get<InterviewResponse[]>(INTERVIEWS_BASE);
    return response.data;
  },

  updateInterviewStatus: async (interviewId: number, status: InterviewStatus): Promise<InterviewResponse> => {
    const response = await api.patch<InterviewResponse>(`${INTERVIEWS_BASE}/${interviewId}/status?status=${status}`);
    return response.data;
  },
};
