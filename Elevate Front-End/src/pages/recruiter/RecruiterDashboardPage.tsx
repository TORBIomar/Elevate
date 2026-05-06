import { useState, useEffect } from 'react';
import { dashboardService } from '../../services/dashboardService';
import type { RecruiterStatsResponse } from '../../types';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import StatCard from '../../components/ui/StatCard';
import { Briefcase, FileText, Clock, CalendarIcon, LayoutDashboard, ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function RecruiterDashboardPage() {
  const [stats, setStats] = useState<RecruiterStatsResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function fetchStats() {
      try {
        const data = await dashboardService.getRecruiterStats();
        setStats(data);
      } catch (err) {
        console.error('Failed to fetch stats:', err);
      } finally {
        setIsLoading(false);
      }
    }
    fetchStats();
  }, []);

  if (isLoading) return <LoadingSpinner />;

  return (
    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-surface-100 flex items-center gap-3">
          <LayoutDashboard className="w-8 h-8 text-primary-500" />
          Dashboard Overview
        </h1>
        <p className="text-surface-400 mt-1">At a glance metrics for your recruitment efforts.</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
        <StatCard
          icon={<Briefcase />}
          label="Active Jobs"
          value={stats?.totalJobsOffered || 0}
        />
        <StatCard
          icon={<FileText />}
          label="Total Applications"
          value={stats?.totalApplicationsReceived || 0}
        />
        <StatCard
          icon={<Clock />}
          label="Pending Review"
          value={stats?.totalPendingApplications || 0}
          trend={stats?.totalPendingApplications ? `${stats.totalPendingApplications} need action` : undefined}
          trendPositive={false}
        />
        <StatCard
          icon={<CalendarIcon />}
          label="Upcoming Interviews"
          value={stats?.totalInterviewsScheduled || 0}
        />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="glass p-6 rounded-2xl">
           <h3 className="text-xl font-bold text-surface-100 mb-4">Quick Actions</h3>
           <div className="space-y-3">
              <Link to="/recruiter/offers/new" className="flex items-center justify-between p-4 rounded-xl bg-surface-800/50 hover:bg-surface-800 transition-colors group">
                 <span className="font-medium text-surface-200">Post a new job offer</span>
                 <ArrowRight className="w-5 h-5 text-surface-400 group-hover:text-primary-400 transition-colors" />
              </Link>
              <Link to="/recruiter/offers" className="flex items-center justify-between p-4 rounded-xl bg-surface-800/50 hover:bg-surface-800 transition-colors group">
                 <span className="font-medium text-surface-200">Review My Offers</span>
                 <ArrowRight className="w-5 h-5 text-surface-400 group-hover:text-primary-400 transition-colors" />
              </Link>
              <Link to="/recruiter/interviews" className="flex items-center justify-between p-4 rounded-xl bg-surface-800/50 hover:bg-surface-800 transition-colors group">
                 <span className="font-medium text-surface-200">View Interview Schedule</span>
                 <ArrowRight className="w-5 h-5 text-surface-400 group-hover:text-primary-400 transition-colors" />
              </Link>
           </div>
        </div>
        
        <div className="glass p-6 rounded-2xl flex flex-col justify-center items-center text-center">
           <div className="w-16 h-16 bg-primary-500/10 rounded-full flex items-center justify-center mb-4">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="text-primary-400">
                 <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                 <path d="M12 6V12L16 14" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
           </div>
           <h3 className="text-lg font-bold text-surface-100 mb-2">More insights coming soon</h3>
           <p className="text-surface-400 max-w-sm text-sm">We are actively building advanced analytics to help you understand your candidate funnel better.</p>
        </div>
      </div>
    </div>
  );
}
