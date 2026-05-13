import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { jobService } from '../../services/jobService';
import type { JobOfferResponse } from '../../types';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import FileUpload from '../../components/ui/FileUpload';
import { useAuth } from '../../contexts/AuthContext';
import { applicationService } from '../../services/applicationService';
import { savedJobsService } from '../../services/savedJobsService';
import {
  MapPin,
  Banknote,
  Clock,
  Tag,
  Briefcase,
  ArrowLeft,
  Send,
  Calendar,
  Building2,
  Bookmark,
} from 'lucide-react';

function formatJobType(type: string): string {
  return type.replace(/_/g, ' ').replace(/\b\w/g, (l) => l.toUpperCase());
}

function formatSalary(salary: number): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    maximumFractionDigits: 0,
  }).format(salary);
}

function formatDate(dateStr: string): string {
  return new Date(dateStr).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
}

const typeColors: Record<string, string> = {
  FULL_TIME: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20',
  PART_TIME: 'bg-blue-500/10 text-blue-400 border-blue-500/20',
  CONTRACT: 'bg-amber-500/10 text-amber-400 border-amber-500/20',
  INTERNSHIP: 'bg-purple-500/10 text-purple-400 border-purple-500/20',
  REMOTE: 'bg-cyan-500/10 text-cyan-400 border-cyan-500/20',
};

export default function JobDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [job, setJob] = useState<JobOfferResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  // Application State
  const [isApplyModalOpen, setIsApplyModalOpen] = useState(false);
  const [cvFile, setCvFile] = useState<File | null>(null);
  const [coverLetterFile, setCoverLetterFile] = useState<File | null>(null);
  const [isApplying, setIsApplying] = useState(false);
  const [applyError, setApplyError] = useState('');
  const [hasApplied, setHasApplied] = useState(false);
  const [isCheckingApplication, setIsCheckingApplication] = useState(false);
  const [isSaved, setIsSaved] = useState(false);

  useEffect(() => {
    if (!id) return;
    const fetchJob = async () => {
      setIsLoading(true);
      try {
        const data = await jobService.getJobById(Number(id));
        setJob(data);
      } catch {
        setError('Job offer not found.');
      } finally {
        setIsLoading(false);
      }
    };
    fetchJob();
    setIsSaved(savedJobsService.isJobSaved(Number(id)));
  }, [id]);

  useEffect(() => {
    if (!user || user.role !== 'CANDIDATE' || !id) return;
    const checkApplicationStatus = async () => {
      setIsCheckingApplication(true);
      try {
        const alreadyApplied = await applicationService.checkApplicationStatus(Number(id));
        setHasApplied(alreadyApplied);
      } catch (err) {
        console.error('Failed to check application status', err);
      } finally {
        setIsCheckingApplication(false);
      }
    };
    checkApplicationStatus();
  }, [user, id]);

  const toggleSaveJob = () => {
    if (!job) return;
    if (isSaved) {
      savedJobsService.removeJob(job.id);
      setIsSaved(false);
    } else {
      savedJobsService.saveJob(job);
      setIsSaved(true);
    }
  };

  const handleApply = async () => {
    if (!cvFile || !job) {
      setApplyError('Please upload your CV.');
      return;
    }
    
    setIsApplying(true);
    setApplyError('');
    try {
      await applicationService.apply(job.id, cvFile, coverLetterFile || undefined);
      setIsApplyModalOpen(false);
      navigate('/my-applications', { state: { message: 'Application submitted successfully!' } });
    } catch (err: unknown) {
      const axiosErr = err as { response?: { status?: number; data?: { message?: string } } };
      setApplyError(axiosErr.response?.data?.message || 'Failed to submit application. You may have already applied.');
    } finally {
      setIsApplying(false);
    }
  };

  if (isLoading) return <LoadingSpinner />;

  if (error || !job) {
    return (
      <div className="mx-auto max-w-3xl px-4 py-20 text-center animate-fade-in">
        <div className="w-20 h-20 rounded-2xl bg-surface-800/60 flex items-center justify-center mb-6 mx-auto">
          <Briefcase className="w-10 h-10 text-surface-600" />
        </div>
        <h2 className="text-xl font-bold text-surface-200 mb-2">Job not found</h2>
        <p className="text-surface-400 mb-6">{error}</p>
        <Button variant="secondary" onClick={() => navigate('/jobs')}>
          Back to Job Board
        </Button>
      </div>
    );
  }

  const typeStyle = typeColors[job.jobType] || typeColors.FULL_TIME;

  return (
    <div className="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      {/* Back link */}
      <Link
        to="/jobs"
        className="inline-flex items-center gap-1.5 text-sm text-surface-400 hover:text-surface-200 transition-colors mb-6"
      >
        <ArrowLeft className="w-4 h-4" />
        Back to listings
      </Link>

      {/* Job Header Card */}
      <div className="glass rounded-2xl p-6 sm:p-8 mb-6">
        <div className="flex flex-col sm:flex-row sm:items-start justify-between gap-4 mb-6">
          <div>
            <div className="flex items-center gap-3 mb-3">
              <span className={`text-xs font-semibold px-3 py-1 rounded-full border ${typeStyle}`}>
                {formatJobType(job.jobType)}
              </span>
              <span className="text-xs font-medium px-2.5 py-1 rounded-full bg-surface-800 text-surface-400 border border-surface-700/50">
                {job.category}
              </span>
            </div>
            <h1 className="text-2xl sm:text-3xl font-bold text-surface-100 mb-2 flex items-center gap-3">
              {job.title}
              {user?.role === 'CANDIDATE' && (
                <button
                  onClick={toggleSaveJob}
                  className={`p-2 rounded-full transition-all ${
                    isSaved 
                      ? 'bg-rose-500/20 text-rose-400 hover:bg-rose-500/30 shadow-[0_0_10px_rgba(244,63,94,0.3)]' 
                      : 'bg-surface-800 text-surface-400 hover:text-rose-400 hover:bg-rose-500/10'
                  }`}
                  title={isSaved ? "Remove from saved jobs" : "Save this job"}
                >
                  <Bookmark className={`w-5 h-5 ${isSaved ? 'fill-current' : ''}`} />
                </button>
              )}
            </h1>
          </div>
          <div className="text-right shrink-0">
            <div className="text-2xl font-bold gradient-text">{formatSalary(job.salary)}</div>
            <span className="text-xs text-surface-500">per year</span>
          </div>
        </div>

        {/* Meta pills */}
        <div className="flex flex-wrap gap-3">
          <div className="flex items-center gap-2 text-sm text-surface-300 bg-surface-800/60 rounded-lg px-3 py-2">
            <MapPin className="w-4 h-4 text-primary-400" />
            {job.location}
          </div>
          <div className="flex items-center gap-2 text-sm text-surface-300 bg-surface-800/60 rounded-lg px-3 py-2">
            <Briefcase className="w-4 h-4 text-primary-400" />
            {formatJobType(job.jobType)}
          </div>
          <div className="flex items-center gap-2 text-sm text-surface-300 bg-surface-800/60 rounded-lg px-3 py-2">
            <Tag className="w-4 h-4 text-primary-400" />
            {job.category}
          </div>
          <div className="flex items-center gap-2 text-sm text-surface-300 bg-surface-800/60 rounded-lg px-3 py-2">
            <Banknote className="w-4 h-4 text-primary-400" />
            {formatSalary(job.salary)}
          </div>
          <div className="flex items-center gap-2 text-sm text-surface-300 bg-surface-800/60 rounded-lg px-3 py-2">
            <Calendar className="w-4 h-4 text-primary-400" />
            {formatDate(job.createdAt)}
          </div>
          <div className="flex items-center gap-2 text-sm text-surface-300 bg-surface-800/60 rounded-lg px-3 py-2">
            <Building2 className="w-4 h-4 text-primary-400" />
            Recruiter #{job.recruiterId}
          </div>
        </div>
      </div>

      {/* Description */}
      <div className="glass rounded-2xl p-6 sm:p-8 mb-6">
        <h2 className="text-lg font-bold text-surface-100 mb-4 flex items-center gap-2">
          <Clock className="w-5 h-5 text-primary-400" />
          Job Description
        </h2>
        <div className="text-surface-300 leading-relaxed whitespace-pre-wrap text-sm">
          {job.description}
        </div>
      </div>

      {/* Apply CTA */}
      <div className="glass rounded-2xl p-6 sm:p-8">
        <div className="flex flex-col sm:flex-row items-center justify-between gap-4">
          <div>
            <h3 className="text-lg font-semibold text-surface-100">Interested in this role?</h3>
            <p className="text-sm text-surface-400 mt-1">
              Submit your application and take the next step.
            </p>
          </div>
          {user?.role === 'CANDIDATE' ? (
            <Button
              size="lg"
              icon={!hasApplied && !isCheckingApplication ? <Send className="w-4 h-4" /> : undefined}
              className="shrink-0"
              onClick={() => setIsApplyModalOpen(true)}
              disabled={hasApplied || isCheckingApplication}
            >
              {isCheckingApplication ? 'Checking...' : hasApplied ? 'Already Applied' : 'Apply Now'}
            </Button>
          ) : !user ? (
            <Button
              size="lg"
              className="shrink-0"
              onClick={() => navigate('/login')}
            >
              Log in to Apply
            </Button>
          ) : (
             <p className="text-sm text-surface-500 italic">Recruiters cannot apply.</p>
          )}
        </div>
      </div>

      {/* Apply Modal */}
      <Modal
        isOpen={isApplyModalOpen}
        onClose={() => setIsApplyModalOpen(false)}
        title="Apply for this Job"
      >
        <div className="space-y-6">
          {applyError && (
            <div className="rounded-xl bg-red-500/10 border border-red-500/20 px-4 py-3 text-sm text-red-400">
              {applyError}
            </div>
          )}

          <FileUpload
            label="Upload Resume/CV"
            required
            value={cvFile}
            onChange={setCvFile}
            helperText="PDF or DOCX max 5MB"
          />

          <FileUpload
            label="Cover Letter (Optional)"
            value={coverLetterFile}
            onChange={setCoverLetterFile}
            helperText="PDF or DOCX max 5MB"
          />

          <div className="flex items-center justify-end gap-3 pt-4 border-t border-surface-700/60 mt-6">
             <Button variant="ghost" onClick={() => setIsApplyModalOpen(false)} disabled={isApplying}>
               Cancel
             </Button>
             <Button onClick={handleApply} isLoading={isApplying}>
               Submit Application
             </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
