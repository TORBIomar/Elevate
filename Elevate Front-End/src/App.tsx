import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';

// Layouts
import AuthLayout from './layouts/AuthLayout';
import MainLayout from './layouts/MainLayout';

// Guards
import PrivateRoute from './components/guards/PrivateRoute';
import RoleRoute from './components/guards/RoleRoute';

// Auth Pages
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';

// Job Pages
import JobBoardPage from './pages/jobs/JobBoardPage';
import JobDetailsPage from './pages/jobs/JobDetailsPage';

// Recruiter Pages
import RecruiterDashboardPage from './pages/recruiter/RecruiterDashboardPage';
import JobOfferFormPage from './pages/recruiter/JobOfferFormPage';
import ApplicationManagerPage from './pages/recruiter/ApplicationManagerPage';
import RecruiterInterviewsPage from './pages/recruiter/RecruiterInterviewsPage';

// Candidate Pages
import MyApplicationsPage from './pages/candidate/MyApplicationsPage';
import MyInterviewsPage from './pages/candidate/MyInterviewsPage';
import MySavedJobsPage from './pages/candidate/MySavedJobsPage';

// Profile
import ProfilePage from './pages/profile/ProfilePage';

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* ——— Public Auth Routes ——— */}
          <Route element={<AuthLayout />}>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </Route>

          {/* ——— Protected Routes ——— */}
          <Route
            element={
              <PrivateRoute>
                <MainLayout />
              </PrivateRoute>
            }
          >
            {/* Job Board (Candidates only) */}
            <Route
              path="/jobs"
              element={
                <RoleRoute allowedRoles={['CANDIDATE']}>
                  <JobBoardPage />
                </RoleRoute>
              }
            />
            
            {/* Job Details (Available to both to preview jobs) */}
            <Route path="/jobs/:id" element={<JobDetailsPage />} />

            {/* Profile */}
            <Route path="/profile" element={<ProfilePage />} />
            
            {/* Candidate-only routes */}
            <Route
              path="/my-applications"
              element={
                <RoleRoute allowedRoles={['CANDIDATE']}>
                  <MyApplicationsPage />
                </RoleRoute>
              }
            />
            <Route
              path="/my-interviews"
              element={
                <RoleRoute allowedRoles={['CANDIDATE']}>
                  <MyInterviewsPage />
                </RoleRoute>
              }
            />
            <Route
              path="/my-saved-jobs"
              element={
                <RoleRoute allowedRoles={['CANDIDATE']}>
                  <MySavedJobsPage />
                </RoleRoute>
              }
            />

            {/* Recruiter-only routes */}
            <Route
              path="/recruiter/dashboard"
              element={
                <RoleRoute allowedRoles={['RECRUITER', 'ADMIN']}>
                  <RecruiterDashboardPage />
                </RoleRoute>
              }
            />
            <Route
              path="/recruiter/offers"
              element={<Navigate to="/recruiter/dashboard" replace />}
            />
            <Route
              path="/recruiter/offers/new"
              element={
                <RoleRoute allowedRoles={['RECRUITER', 'ADMIN']}>
                  <JobOfferFormPage />
                </RoleRoute>
              }
            />
            <Route
              path="/recruiter/offers/:id/edit"
              element={
                <RoleRoute allowedRoles={['RECRUITER', 'ADMIN']}>
                  <JobOfferFormPage />
                </RoleRoute>
              }
            />
            <Route
              path="/recruiter/offers/:id/applications"
              element={
                <RoleRoute allowedRoles={['RECRUITER', 'ADMIN']}>
                  <ApplicationManagerPage />
                </RoleRoute>
              }
            />
            <Route
              path="/recruiter/interviews"
              element={
                <RoleRoute allowedRoles={['RECRUITER', 'ADMIN']}>
                  <RecruiterInterviewsPage />
                </RoleRoute>
              }
            />
          </Route>

          {/* ——— Fallback ——— */}
          <Route path="*" element={<Navigate to="/jobs" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
