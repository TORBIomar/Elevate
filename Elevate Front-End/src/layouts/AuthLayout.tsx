import { Outlet, Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Briefcase } from 'lucide-react';

export default function AuthLayout() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) return null;
  if (isAuthenticated) return <Navigate to="/jobs" replace />;

  return (
    <div className="min-h-screen flex gradient-mesh">
      {/* Left decorative panel — hidden on mobile */}
      <div className="hidden lg:flex lg:w-1/2 xl:w-[55%] relative overflow-hidden items-center justify-center">
        {/* Animated background orbs */}
        <div className="absolute top-1/4 left-1/4 w-72 h-72 bg-primary-600/20 rounded-full blur-3xl animate-pulse-slow" />
        <div className="absolute bottom-1/3 right-1/4 w-96 h-96 bg-accent-500/15 rounded-full blur-3xl animate-pulse-slow" style={{ animationDelay: '1.5s' }} />
        <div className="absolute top-1/2 left-1/2 w-64 h-64 bg-blue-500/10 rounded-full blur-3xl animate-float" />

        <div className="relative z-10 max-w-lg px-12 animate-fade-in">
          <div className="flex items-center gap-3 mb-8">
            <div className="w-14 h-14 rounded-2xl bg-gradient-to-br from-primary-500 to-accent-500 flex items-center justify-center shadow-2xl shadow-primary-500/30">
              <Briefcase className="w-8 h-8 text-white" />
            </div>
            <h1 className="text-4xl font-black tracking-tight">
              <span className="gradient-text">Elevate</span>
            </h1>
          </div>
          <h2 className="text-3xl font-bold text-surface-100 mb-4 leading-tight">
            Your career,{' '}
            <span className="gradient-text">elevated</span>.
          </h2>
          <p className="text-lg text-surface-400 leading-relaxed">
            Connect with top employers, discover opportunities that match your ambitions, and take the next step in your professional journey.
          </p>

          {/* Stats row */}
          <div className="flex gap-8 mt-10">
            {[
              { value: '10K+', label: 'Active Jobs' },
              { value: '5K+', label: 'Companies' },
              { value: '50K+', label: 'Candidates' },
            ].map((stat) => (
              <div key={stat.label}>
                <div className="text-2xl font-black gradient-text">{stat.value}</div>
                <div className="text-sm text-surface-500">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Right content panel */}
      <div className="flex-1 flex items-center justify-center p-6 sm:p-10">
        <div className="w-full max-w-md animate-scale-in">
          {/* Mobile logo */}
          <div className="lg:hidden flex items-center gap-2.5 mb-8">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-primary-500 to-accent-500 flex items-center justify-center shadow-lg shadow-primary-500/20">
              <Briefcase className="w-5 h-5 text-white" />
            </div>
            <span className="text-2xl font-black tracking-tight gradient-text">Elevate</span>
          </div>

          <Outlet />
        </div>
      </div>
    </div>
  );
}
