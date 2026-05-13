import type { JobOfferResponse } from '../types';
import { authService } from './authService';

const SAVED_JOBS_KEY = 'elevate_saved_jobs';

export const savedJobsService = {
  // Get all saved jobs for current user
  getSavedJobs: (): JobOfferResponse[] => {
    const user = authService.getStoredUser();
    if (!user) return [];
    
    const allSaved = JSON.parse(localStorage.getItem(SAVED_JOBS_KEY) || '{}');
    return allSaved[user.userId] || [];
  },

  // Check if a specific job is saved by the user
  isJobSaved: (jobId: number): boolean => {
    const savedJobs = savedJobsService.getSavedJobs();
    return savedJobs.some(job => job.id === jobId);
  },

  // Add a job to user's saved list
  saveJob: (job: JobOfferResponse): void => {
    const user = authService.getStoredUser();
    if (!user) return;

    const allSaved = JSON.parse(localStorage.getItem(SAVED_JOBS_KEY) || '{}');
    const userSaved = allSaved[user.userId] || [];
    
    if (!userSaved.some((j: JobOfferResponse) => j.id === job.id)) {
      userSaved.push(job);
      allSaved[user.userId] = userSaved;
      localStorage.setItem(SAVED_JOBS_KEY, JSON.stringify(allSaved));
    }
  },

  // Remove a job from user's saved list
  removeJob: (jobId: number): void => {
    const user = authService.getStoredUser();
    if (!user) return;

    const allSaved = JSON.parse(localStorage.getItem(SAVED_JOBS_KEY) || '{}');
    let userSaved = allSaved[user.userId] || [];
    
    userSaved = userSaved.filter((j: JobOfferResponse) => j.id !== jobId);
    allSaved[user.userId] = userSaved;
    
    localStorage.setItem(SAVED_JOBS_KEY, JSON.stringify(allSaved));
  }
};
