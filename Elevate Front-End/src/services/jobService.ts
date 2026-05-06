import api from './api';
import type { JobOfferRequest, JobOfferResponse, Page } from '../types';

const JOBS_BASE = '/api/jobs';

export const jobService = {
  async searchJobs(
    keyword?: string,
    category?: string,
    page: number = 0,
    size: number = 9
  ): Promise<Page<JobOfferResponse>> {
    const params = new URLSearchParams();
    if (keyword) params.append('keyword', keyword);
    if (category) params.append('category', category);
    params.append('page', page.toString());
    params.append('size', size.toString());

    const response = await api.get<Page<JobOfferResponse>>(
      `${JOBS_BASE}/search?${params.toString()}`
    );
    return response.data;
  },

  async getJobById(id: number): Promise<JobOfferResponse> {
    const response = await api.get<JobOfferResponse>(`${JOBS_BASE}/${id}`);
    return response.data;
  },

  async getJobsByRecruiter(
    recruiterId: number,
    page: number = 0,
    size: number = 10
  ): Promise<Page<JobOfferResponse>> {
    const params = new URLSearchParams();
    params.append('page', page.toString());
    params.append('size', size.toString());

    const response = await api.get<Page<JobOfferResponse>>(
      `${JOBS_BASE}/recruiter/${recruiterId}?${params.toString()}`
    );
    return response.data;
  },

  async createJob(data: JobOfferRequest): Promise<JobOfferResponse> {
    const response = await api.post<JobOfferResponse>(JOBS_BASE, data);
    return response.data;
  },

  async updateJob(id: number, data: JobOfferRequest): Promise<JobOfferResponse> {
    const response = await api.put<JobOfferResponse>(`${JOBS_BASE}/${id}`, data);
    return response.data;
  },

  async deleteJob(id: number): Promise<void> {
    await api.delete(`${JOBS_BASE}/${id}`);
  },
};
