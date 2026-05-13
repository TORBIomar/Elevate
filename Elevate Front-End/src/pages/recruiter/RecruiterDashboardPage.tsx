import { useState, useEffect, useCallback } from 'react';
import { dashboardService } from '../../services/dashboardService';
import { jobService } from '../../services/jobService';
import { useAuth } from '../../contexts/AuthContext';
import { Link, useNavigate } from 'react-router-dom';
import type { RecruiterStatsResponse, JobOfferResponse, Page } from '../../types';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import StatCard from '../../components/ui/StatCard';
import Pagination from '../../components/ui/Pagination';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import { 
  Briefcase, 
  FileText, 
  Clock, 
  CalendarIcon, 
  LayoutDashboard, 
  Plus, 
  Edit3, 
  Trash2, 
  MapPin, 
  Banknote, 
  Users 
} from 'lucide-react';

function formatSalary(salary: number): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    maximumFractionDigits: 0,
  }).format(salary);
}

function formatJobType(type: string): string {
  return type.replace(/_/g, ' ').replace(/\b\w/g, (l) => l.toUpperCase());
}

export default function RecruiterDashboardPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  // Dashboard Stats State
  const [stats, setStats] = useState<RecruiterStatsResponse | null>(null);
  const [isStatsLoading, setIsStatsLoading] = useState(true);

  // Jobs List State
  const [jobs, setJobs] = useState<Page<JobOfferResponse> | null>(null);
  const [page, setPage] = useState(0);
  const [isJobsLoading, setIsJobsLoading] = useState(true);

  // Delete Job State
  const [deletingId, setDeletingId] = useState<number | null>(null);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [jobToDelete, setJobToDelete] = useState<number | null>(null);

  useEffect(() => {
    async function fetchStats() {
      try {
        const data = await dashboardService.getRecruiterStats();
        setStats(data);
      } catch (err) {
        console.error('Failed to fetch stats:', err);
      } finally {
        setIsStatsLoading(false);
      }
    }
    fetchStats();
  }, []);

  const fetchJobs = useCallback(async () => {
    if (!user) return;
    setIsJobsLoading(true);
    try {
      const data = await jobService.getJobsByRecruiter(user.userId, page, 10);
      setJobs(data);
    } catch (err) {
      console.error('Failed to fetch recruiter jobs:', err);
    } finally {
      setIsJobsLoading(false);
    }
  }, [user, page]);

  useEffect(() => {
    fetchJobs();
  }, [fetchJobs]);

  const promptDelete = (id: number) => {
    setJobToDelete(id);
    setDeleteModalOpen(true);
  };

  const handleDeleteConfirm = async () => {
    if (!jobToDelete) return;
    setDeletingId(jobToDelete);
    try {
      await jobService.deleteJob(jobToDelete);
      fetchJobs();
      
      // Also refresh stats to reflect the deleted job
      setIsStatsLoading(true);
      const data = await dashboardService.getRecruiterStats();
      setStats(data);
    } catch (err) {
      console.error('Failed to delete job:', err);
      alert('Failed to delete the job offer. Ensure no applications are associated with it.');
    } finally {
      setDeleteModalOpen(false);
      setDeletingId(null);
      setJobToDelete(null);
      setIsStatsLoading(false);
    }
  };

  if (isStatsLoading && isJobsLoading) return <LoadingSpinner />;

  return (
    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
        <div>
          <h1 className="text-3xl font-bold text-surface-100 flex items-center gap-3">
            <LayoutDashboard className="w-8 h-8 text-primary-500" />
            Recruiter Dashboard
          </h1>
          <p className="text-surface-400 mt-1">Metrics and management for your recruitment efforts.</p>
        </div>
        <Button
          icon={<Plus className="w-4 h-4" />}
          onClick={() => navigate('/recruiter/offers/new')}
        >
          Post New Job
        </Button>
      </div>

      {/* Stats Overview */}
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

      {/* My Job Offers List */}
      <div>
        <h2 className="text-xl font-bold text-surface-100 mb-6 flex items-center gap-2">
          <Briefcase className="w-5 h-5 text-primary-400" />
          My Job Offers
        </h2>

        {isJobsLoading ? (
          <div className="py-10"><LoadingSpinner /></div>
        ) : jobs && jobs.content.length > 0 ? (
          <>
            <div className="space-y-4">
              {jobs.content.map((job, index) => (
                <div
                  key={job.id}
                  className={`glass rounded-2xl p-5 sm:p-6 flex flex-col sm:flex-row sm:items-center gap-4 card-hover animate-slide-up stagger-${Math.min(index + 1, 6)}`}
                >
                  {/* Job info */}
                  <div className="flex-1 min-w-0">
                    <Link
                      to={`/jobs/${job.id}`}
                      className="text-lg font-bold text-surface-100 hover:text-primary-400 transition-colors truncate block"
                    >
                      {job.title}
                    </Link>
                    <div className="flex flex-wrap items-center gap-3 mt-2 text-sm text-surface-400">
                      <span className="flex items-center gap-1.5">
                        <MapPin className="w-3.5 h-3.5" />
                        {job.location}
                      </span>
                      <span className="flex items-center gap-1.5">
                        <Briefcase className="w-3.5 h-3.5" />
                        {formatJobType(job.jobType)}
                      </span>
                      <span className="flex items-center gap-1.5 text-primary-400 font-semibold">
                        <Banknote className="w-3.5 h-3.5" />
                        {formatSalary(job.salary)}
                      </span>
                    </div>
                  </div>

                  {/* Actions */}
                  <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-2 shrink-0 mt-4 sm:mt-0">
                    <Button
                      variant="primary"
                      size="sm"
                      icon={<Users className="w-3.5 h-3.5" />}
                      onClick={() => navigate(`/recruiter/offers/${job.id}/applications`)}
                    >
                      Applicants
                    </Button>
                    <Button
                      variant="secondary"
                      size="sm"
                      icon={<Edit3 className="w-3.5 h-3.5" />}
                      onClick={() => navigate(`/recruiter/offers/${job.id}/edit`)}
                    >
                      Edit
                    </Button>
                    <Button
                      variant="danger"
                      size="sm"
                      icon={<Trash2 className="w-3.5 h-3.5" />}
                      isLoading={deletingId === job.id}
                      onClick={() => promptDelete(job.id)}
                    >
                      Delete
                    </Button>
                  </div>
                </div>
              ))}
            </div>

            <div className="mt-6">
              <Pagination
                currentPage={jobs.number}
                totalPages={jobs.totalPages}
                onPageChange={setPage}
              />
            </div>
          </>
        ) : (
          <div className="glass p-12 rounded-3xl text-center max-w-lg mx-auto animate-slide-up">
            <div className="w-16 h-16 bg-surface-800/60 rounded-full flex items-center justify-center mx-auto mb-4">
              <Briefcase className="w-8 h-8 text-surface-500" />
            </div>
            <h3 className="text-lg font-bold text-surface-100 mb-2">No offers yet</h3>
            <p className="text-sm text-surface-400 mb-6">
              You haven't posted any job offers. Create your first one and start attracting talent.
            </p>
            <Button
              icon={<Plus className="w-4 h-4" />}
              onClick={() => navigate('/recruiter/offers/new')}
            >
              Post Your First Job
            </Button>
          </div>
        )}
      </div>

      <Modal
        isOpen={deleteModalOpen}
        onClose={() => setDeleteModalOpen(false)}
        title="Delete Job Offer"
        size="sm"
      >
        <p className="text-surface-300 mb-6">
          Are you sure you want to delete this job offer? This action cannot be undone.
        </p>
        <div className="flex items-center justify-end gap-3 pt-4 border-t border-surface-700/60 mt-2">
          <Button variant="ghost" onClick={() => setDeleteModalOpen(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteConfirm} isLoading={deletingId === jobToDelete}>
            Confirm Delete
          </Button>
        </div>
      </Modal>
    </div>
  );
}
