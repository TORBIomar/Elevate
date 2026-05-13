import { MapPin, Banknote, Clock, Tag, Bookmark } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { savedJobsService } from '../../services/savedJobsService';
import type { JobOfferResponse } from '../../types';

interface JobCardProps {
  job: JobOfferResponse;
  index?: number;
}

const typeColors: Record<string, string> = {
  FULL_TIME: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20',
  PART_TIME: 'bg-blue-500/10 text-blue-400 border-blue-500/20',
  CONTRACT: 'bg-amber-500/10 text-amber-400 border-amber-500/20',
  INTERNSHIP: 'bg-purple-500/10 text-purple-400 border-purple-500/20',
  REMOTE: 'bg-cyan-500/10 text-cyan-400 border-cyan-500/20',
};

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

function timeAgo(dateStr: string): string {
  const now = new Date();
  const date = new Date(dateStr);
  const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);
  if (seconds < 60) return 'Just now';
  const minutes = Math.floor(seconds / 60);
  if (minutes < 60) return `${minutes}m ago`;
  const hours = Math.floor(minutes / 60);
  if (hours < 24) return `${hours}h ago`;
  const days = Math.floor(hours / 24);
  if (days < 30) return `${days}d ago`;
  return date.toLocaleDateString();
}

export default function JobCard({ job, index = 0 }: JobCardProps) {
  const { user } = useAuth();
  const [isSaved, setIsSaved] = useState(false);
  const typeStyle = typeColors[job.jobType] || typeColors.FULL_TIME;

  useEffect(() => {
    setIsSaved(savedJobsService.isJobSaved(job.id));
  }, [job.id]);

  const toggleSave = (e: React.MouseEvent) => {
    e.preventDefault(); // Prevent navigating to the job details page
    if (isSaved) {
      savedJobsService.removeJob(job.id);
      setIsSaved(false);
    } else {
      savedJobsService.saveJob(job);
      setIsSaved(true);
    }
  };

  return (
    <Link
      to={`/jobs/${job.id}`}
      className={`
        group block glass rounded-2xl p-6 card-hover animate-slide-up
        stagger-${Math.min(index + 1, 6)}
      `}
    >
      {/* Header */}
      <div className="flex items-start justify-between gap-3 mb-4">
        <div className="min-w-0 flex-1">
          <h3 className="text-lg font-bold text-surface-100 truncate group-hover:text-primary-400 transition-colors">
            {job.title}
          </h3>
          <div className="flex items-center gap-1.5 mt-1 text-surface-400 text-sm">
            <MapPin className="w-3.5 h-3.5 shrink-0" />
            <span className="truncate">{job.location}</span>
          </div>
        </div>
        <div className="flex items-center gap-2">
          {user?.role === 'CANDIDATE' && (
            <button
              onClick={toggleSave}
              className={`p-1.5 rounded-full transition-all shrink-0 ${
                isSaved 
                  ? 'bg-rose-500/20 text-rose-400 hover:bg-rose-500/30' 
                  : 'text-surface-500 hover:text-rose-400 hover:bg-rose-500/10'
              }`}
              title={isSaved ? "Remove from saved jobs" : "Save this job"}
            >
              <Bookmark className={`w-4 h-4 ${isSaved ? 'fill-current' : ''}`} />
            </button>
          )}
          <span className={`shrink-0 text-xs font-semibold px-3 py-1 rounded-full border ${typeStyle}`}>
            {formatJobType(job.jobType)}
          </span>
        </div>
      </div>

      {/* Description preview */}
      <p className="text-sm text-surface-400 line-clamp-2 mb-4 leading-relaxed">
        {job.description}
      </p>

      {/* Footer */}
      <div className="flex items-center justify-between pt-4 border-t border-surface-700/40">
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-1.5 text-sm font-semibold text-primary-400">
            <Banknote className="w-4 h-4" />
            {formatSalary(job.salary)}
          </div>
          <div className="flex items-center gap-1.5 text-xs text-surface-500">
            <Tag className="w-3.5 h-3.5" />
            {job.category}
          </div>
        </div>
        <div className="flex items-center gap-1 text-xs text-surface-500">
          <Clock className="w-3.5 h-3.5" />
          {timeAgo(job.createdAt)}
        </div>
      </div>
    </Link>
  );
}
