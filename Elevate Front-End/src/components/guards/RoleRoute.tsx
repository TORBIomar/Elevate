import { Navigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import type { UserRole } from '../../types';
import LoadingSpinner from '../ui/LoadingSpinner';

interface RoleRouteProps {
  children: React.ReactNode;
  allowedRoles: UserRole[];
}

export default function RoleRoute({ children, allowedRoles }: RoleRouteProps) {
  const { isAuthenticated, isLoading, user } = useAuth();

  if (isLoading) {
    return <LoadingSpinner fullScreen />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (!user || !allowedRoles.includes(user.role as UserRole)) {
    if (user?.role === 'RECRUITER' || user?.role === 'ADMIN') {
      return <Navigate to="/recruiter/dashboard" replace />;
    }
    return <Navigate to="/jobs" replace />;
  }

  return <>{children}</>;
}
