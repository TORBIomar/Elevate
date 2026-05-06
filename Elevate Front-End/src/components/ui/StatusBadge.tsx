import type { ApplicationStatus, InterviewStatus } from '../../types';

interface StatusBadgeProps {
  status: ApplicationStatus | InterviewStatus;
  className?: string;
}

const statusConfig: Record<string, { label: string; styles: string }> = {
  // Application Statuses
  PENDING: { label: 'Pending', styles: 'bg-amber-500/10 text-amber-400 border-amber-500/20' },
  REVIEWING: { label: 'Reviewing', styles: 'bg-blue-500/10 text-blue-400 border-blue-500/20' },
  INTERVIEW_SCHEDULED: { label: 'Interview Scheduled', styles: 'bg-purple-500/10 text-purple-400 border-purple-500/20' },
  ACCEPTED: { label: 'Accepted', styles: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20' },
  REJECTED: { label: 'Rejected', styles: 'bg-red-500/10 text-red-400 border-red-500/20' },
  
  // Interview Statuses
  SCHEDULED: { label: 'Scheduled', styles: 'bg-purple-500/10 text-purple-400 border-purple-500/20' },
  COMPLETED: { label: 'Completed', styles: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20' },
  CANCELED: { label: 'Canceled', styles: 'bg-surface-500/10 text-surface-400 border-surface-500/20' },
};

export default function StatusBadge({ status, className = '' }: StatusBadgeProps) {
  const config = statusConfig[status] || { label: status, styles: 'bg-surface-800 text-surface-300 border-surface-700' };
  
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium border ${config.styles} ${className}`}>
      {config.label}
    </span>
  );
}
