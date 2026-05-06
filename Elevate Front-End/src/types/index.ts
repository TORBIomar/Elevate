// ═══════════════════════════════════════════════════
//  Auth & User Types (matching Spring Boot DTOs)
// ═══════════════════════════════════════════════════

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  role: 'CANDIDATE' | 'RECRUITER';
}

export interface LoginResponse {
  token: string;
  type: string;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
}

export interface UserResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string | null;
  role: string;
  profilePictureUrl: string | null;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface UpdateProfileRequest {
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  profilePictureUrl?: string;
}

// ═══════════════════════════════════════════════════
//  Job Offer Types
// ═══════════════════════════════════════════════════

export interface JobOfferRequest {
  title: string;
  description: string;
  location: string;
  jobType: string;
  category: string;
  salary: number;
}

export interface JobOfferResponse {
  id: number;
  title: string;
  description: string;
  location: string;
  jobType: string;
  category: string;
  salary: number;
  recruiterId: number;
  createdAt: string;
  updatedAt: string;
}

// ═══════════════════════════════════════════════════
//  Spring Boot Page Wrapper
// ═══════════════════════════════════════════════════

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;       // current page (0-indexed)
  first: boolean;
  last: boolean;
  empty: boolean;
}

// ═══════════════════════════════════════════════════
//  Error
// ═══════════════════════════════════════════════════

export interface ErrorResponse {
  message: string;
  details: string;
}

// ═══════════════════════════════════════════════════
//  Auth Context State
// ═══════════════════════════════════════════════════

export interface AuthUser {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
}

export type UserRole = 'CANDIDATE' | 'RECRUITER' | 'ADMIN';

export const JOB_TYPES = [
  'FULL_TIME',
  'PART_TIME',
  'CONTRACT',
  'INTERNSHIP',
  'REMOTE',
] as const;

export const JOB_CATEGORIES = [
  'TECHNOLOGY',
  'FINANCE',
  'MARKETING',
  'DESIGN',
  'ENGINEERING',
  'SALES',
  'HEALTHCARE',
  'EDUCATION',
  'OTHER',
] as const;

// ═══════════════════════════════════════════════════
//  Application & Interview Types
// ═══════════════════════════════════════════════════

export type ApplicationStatus = 'PENDING' | 'REVIEWING' | 'INTERVIEW_SCHEDULED' | 'ACCEPTED' | 'REJECTED';
export type InterviewStatus = 'SCHEDULED' | 'COMPLETED' | 'CANCELED';

export interface ApplicationResponse {
  id: number;
  candidateId: number;
  candidateName: string;
  jobOfferId: number;
  jobOfferTitle: string;
  status: ApplicationStatus;
  cvUrl: string;
  coverLetterUrl: string;
  appliedAt: string;
}

export interface InterviewRequest {
  applicationId: number;
  scheduledDate: string;
  interviewLinkOrLocation: string;
}

export interface InterviewResponse {
  id: number;
  applicationId: number;
  candidateName: string;
  jobTitle: string;
  scheduledDate: string;
  interviewLinkOrLocation: string;
  status: InterviewStatus;
}

// ═══════════════════════════════════════════════════
//  Dashboard Types
// ═══════════════════════════════════════════════════

export interface RecruiterStatsResponse {
  totalJobsOffered: number;
  totalApplicationsReceived: number;
  totalPendingApplications: number;
  totalInterviewsScheduled: number;
}

// ═══════════════════════════════════════════════════
//  Notifications
// ═══════════════════════════════════════════════════

export interface NotificationResponse {
  id: number;
  message: string;
  isRead: boolean;
  createdAt: string;
}
