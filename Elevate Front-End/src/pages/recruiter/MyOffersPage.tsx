import { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { jobService } from '../../services/jobService';
import type { JobOfferResponse, Page } from '../../types';
import Pagination from '../../components/ui/Pagination';
import Button from '../../components/ui/Button';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import {
  Plus,
  Edit3,
  Trash2,
  MapPin,
  Banknote,
  Briefcase,
  LayoutDashboard,
  Users,
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

export default function MyOffersPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [jobs, setJobs] = useState<Page<JobOfferResponse> | null>(null);
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [deletingId, setDeletingId] = useState<number | null>(null);

  const fetchJobs = useCallback(async () => {
    if (!user) return;
    setIsLoading(true);
    try {
      const data = await jobService.getJobsByRecruiter(user.userId, page, 10);
      setJobs(data);
    } catch (err) {
      console.error('Failed to fetch recruiter jobs:', err);
    } finally {
      setIsLoading(false);
    }
  }, [user, page]);

  useEffect(() => {
    fetchJobs();
  }, [fetchJobs]);

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this job offer?')) return;
    setDeletingId(id);
    try {
      await jobService.deleteJob(id);
      fetchJobs();
    } catch (err) {
      console.error('Failed to delete job:', err);
      alert('Failed to delete the job offer.');
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8 animate-fade-in">
        <div>
          <h1 className="text-3xl font-bold text-surface-100 flex items-center gap-3">
            <LayoutDashboard className="w-8 h-8 text-primary-500" />
            My Job Offers
          </h1>
          <p className="text-surface-400 mt-1">
            Manage your posted job opportunities
          </p>
        </div>
        <Button
          icon={<Plus className="w-4 h-4" />}
          onClick={() => navigate('/recruiter/offers/new')}
        >
          Post New Job
        </Button>
      </div>

      {/* Job List */}
      {isLoading ? (
        <LoadingSpinner />
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
                <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-2 shrink-0">
                  <Button
                    variant="primary"
                    size="sm"
                    icon={<Users className="w-3.5 h-3.5" />}
                    onClick={() => navigate(`/recruiter/offers/${job.id}/applications`)}
                  >
                    View Applicants
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
                    onClick={() => handleDelete(job.id)}
                  >
                    Delete
                  </Button>
                </div>
              </div>
            ))}
          </div>

          <Pagination
            currentPage={jobs.number}
            totalPages={jobs.totalPages}
            onPageChange={setPage}
          />
        </>
      ) : (
        <div className="flex flex-col items-center justify-center py-20 animate-fade-in">
          <div className="w-20 h-20 rounded-2xl bg-surface-800/60 flex items-center justify-center mb-6">
            <Briefcase className="w-10 h-10 text-surface-600" />
          </div>
          <h3 className="text-lg font-semibold text-surface-300 mb-2">No offers yet</h3>
          <p className="text-sm text-surface-500 text-center max-w-sm mb-6">
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
  );
}
