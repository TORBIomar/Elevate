import api from './api';
import type { ApplicationResponse, ApplicationStatus, Page } from '../types';

const APPLICATIONS_BASE = '/api/applications';

export const applicationService = {
  apply: async (jobId: number, cv: File, coverLetter?: File): Promise<ApplicationResponse> => {
    const formData = new FormData();
    formData.append('cv', cv);
    if (coverLetter) {
      formData.append('coverLetter', coverLetter);
    }
    
    // Crucial Note: This endpoint receives data as multipart/form-data
    // Axios will automatically set the correct Content-Type with the boundary if we omit it
    // But since our api interceptor sets 'application/json' by default, we need to override it here.
    const response = await api.post<ApplicationResponse>(`${APPLICATIONS_BASE}/${jobId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  getMyApplications: async (page: number = 0, size: number = 10): Promise<Page<ApplicationResponse>> => {
    const params = new URLSearchParams({ page: String(page), size: String(size) });
    const response = await api.get<Page<ApplicationResponse>>(`${APPLICATIONS_BASE}/my-applications?${params.toString()}`);
    return response.data;
  },

  getApplicationsForJob: async (jobId: number, page: number = 0, size: number = 10): Promise<Page<ApplicationResponse>> => {
    const params = new URLSearchParams({ page: String(page), size: String(size) });
    const response = await api.get<Page<ApplicationResponse>>(`${APPLICATIONS_BASE}/job/${jobId}?${params.toString()}`);
    return response.data;
  },

  updateApplicationStatus: async (applicationId: number, status: ApplicationStatus): Promise<ApplicationResponse> => {
    const response = await api.patch<ApplicationResponse>(`${APPLICATIONS_BASE}/${applicationId}/status?status=${status}`);
    return response.data;
  },
};
