import { useState, useEffect, useCallback } from 'react';
import { applicationService } from '../../services/applicationService';
import type { ApplicationResponse, Page } from '../../types';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import Pagination from '../../components/ui/Pagination';
import StatusBadge from '../../components/ui/StatusBadge';
import { Calendar, Briefcase, FileText, ArrowRight, CheckCircle2 } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function MyApplicationsPage() {
  const [applications, setApplications] = useState<Page<ApplicationResponse> | null>(null);
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  const fetchApplications = useCallback(async () => {
    setIsLoading(true);
    try {
      const data = await applicationService.getMyApplications(page, 10);
      setApplications(data);
    } catch (err) {
      console.error('Failed to fetch applications:', err);
    } finally {
      setIsLoading(false);
    }
  }, [page]);

  useEffect(() => {
    fetchApplications();
  }, [fetchApplications]);

  if (isLoading) return <LoadingSpinner />;

  return (
    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-surface-100 flex items-center gap-3">
          <FileText className="w-8 h-8 text-primary-500" />
          My Applications
        </h1>
        <p className="text-surface-400 mt-1">Track the status of roles you have applied for.</p>
      </div>

      {applications && applications.content.length > 0 ? (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {applications.content.map((app, idx) => (
              <div 
                key={app.id} 
                className={`glass p-6 rounded-2xl card-hover animate-slide-up stagger-${Math.min(idx + 1, 6)} flex flex-col justify-between relative overflow-hidden`}
              >
                {app.status === 'ACCEPTED' && (
                  <div className="absolute -top-6 -right-6 text-emerald-500/10 pointer-events-none">
                    <CheckCircle2 className="w-32 h-32" />
                  </div>
                )}
                <div className="relative z-10">
                  <div className="flex items-start justify-between mb-4">
                    <div className="flex items-center gap-2">
                      <StatusBadge status={app.status} />
                      {app.status === 'ACCEPTED' && (
                        <CheckCircle2 className="w-5 h-5 text-emerald-400 animate-scale-in" />
                      )}
                    </div>
                    <span className="text-xs text-surface-400 flex items-center gap-1">
                      <Calendar className="w-3.5 h-3.5" />
                      {new Date(app.appliedAt).toLocaleDateString()}
                    </span>
                  </div>
                  
                  <Link 
                    to={`/jobs/${app.jobOfferId}`} 
                    className="group"
                  >
                    <h2 className="text-xl font-bold text-surface-100 group-hover:text-primary-400 transition-colors">
                      {app.jobOfferTitle}
                    </h2>
                  </Link>
                </div>
                
                <div className="mt-6 pt-4 border-t border-surface-700/50 flex items-center justify-between">
                  <div className="flex gap-3">
                     {app.cvUrl && (
                        <a href={app.cvUrl} target="_blank" rel="noreferrer" className="text-xs font-semibold text-primary-400 hover:text-primary-300 flex items-center gap-1 transition-colors">
                            <FileText className="w-3.5 h-3.5" /> View CV
                        </a>
                     )}
                     {app.coverLetterUrl && (
                        <a href={app.coverLetterUrl} target="_blank" rel="noreferrer" className="text-xs font-semibold text-surface-400 hover:text-surface-300 flex items-center gap-1 transition-colors">
                            <FileText className="w-3.5 h-3.5" /> Cover Letter
                        </a>
                     )}
                  </div>
                  
                  <Link to={`/jobs/${app.jobOfferId}`} className="p-2 -mr-2 rounded-lg text-surface-400 hover:text-primary-400 hover:bg-primary-500/10 transition-all">
                     <ArrowRight className="w-5 h-5" />
                  </Link>
                </div>
              </div>
            ))}
          </div>
          
          <Pagination
             currentPage={applications.number}
             totalPages={applications.totalPages}
             onPageChange={setPage}
          />
        </>
      ) : (
        <div className="glass p-12 rounded-3xl text-center max-w-lg mx-auto mt-12 animate-slide-up">
           <div className="w-20 h-20 bg-surface-800/60 rounded-full flex items-center justify-center mx-auto mb-6">
              <Briefcase className="w-10 h-10 text-surface-500" />
           </div>
           <h3 className="text-xl font-bold text-surface-100 mb-2">No applications yet</h3>
           <p className="text-surface-400 mb-6">You haven't hit apply on any job offers yet. Discover roles that match your skills.</p>
           <Link to="/jobs" className="inline-flex items-center gap-2 font-semibold bg-primary-600 hover:bg-primary-500 text-white px-6 py-2.5 rounded-xl transition-all shadow-lg shadow-primary-500/20 active:scale-95">
              Browse Jobs <ArrowRight className="w-4 h-4" />
           </Link>
        </div>
      )}
    </div>
  );
}
