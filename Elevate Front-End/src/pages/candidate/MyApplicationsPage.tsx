import { useState, useEffect, useCallback } from 'react';
import { applicationService } from '../../services/applicationService';
import type { ApplicationResponse, Page } from '../../types';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import Pagination from '../../components/ui/Pagination';
import StatusBadge from '../../components/ui/StatusBadge';
import Modal from '../../components/ui/Modal';
import Button from '../../components/ui/Button';
import { Calendar, Briefcase, FileText, ArrowRight, CheckCircle2, Trash2, Download, Eye } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function MyApplicationsPage() {
  const [applications, setApplications] = useState<Page<ApplicationResponse> | null>(null);
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  // Withdraw State
  const [isWithdrawModalOpen, setIsWithdrawModalOpen] = useState(false);
  const [appToWithdraw, setAppToWithdraw] = useState<number | null>(null);
  const [isWithdrawing, setIsWithdrawing] = useState(false);

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

  const handleWithdrawConfirm = async () => {
    if (!appToWithdraw) return;
    setIsWithdrawing(true);
    try {
      await applicationService.withdrawApplication(appToWithdraw);
      setIsWithdrawModalOpen(false);
      setAppToWithdraw(null);
      fetchApplications();
    } catch (err) {
      console.error('Failed to withdraw application:', err);
      alert('Failed to withdraw application. Please try again later.');
    } finally {
      setIsWithdrawing(false);
    }
  };

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
                className={`glass p-6 rounded-2xl card-hover animate-slide-up stagger-${Math.min(idx + 1, 6)} flex flex-col justify-between relative overflow-hidden ${
                  app.status === 'ACCEPTED' ? 'border-2 border-emerald-500 shadow-[0_0_20px_rgba(16,185,129,0.15)] bg-emerald-500/5' : ''
                }`}
              >
                {app.status === 'ACCEPTED' && (
                  <div className="absolute -top-6 -right-6 text-emerald-500/10 pointer-events-none">
                    <CheckCircle2 className="w-32 h-32" />
                  </div>
                )}
                <div className="relative z-10">
                  <div className="flex items-start justify-between mb-4">
                    <div className="flex items-center gap-2">
                      {app.status === 'ACCEPTED' ? (
                        <div className="flex items-center gap-2 px-3 py-1 rounded-full bg-emerald-500/20 border border-emerald-500/30 text-emerald-400 font-bold shadow-[0_0_10px_rgba(16,185,129,0.2)]">
                          <CheckCircle2 className="w-4 h-4 animate-scale-in" />
                          Application Accepted!
                        </div>
                      ) : (
                        <StatusBadge status={app.status} />
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
                  <div className="flex gap-2">
                     {app.cvUrl && (
                        <div className="flex items-center gap-1 bg-primary-500/10 rounded-lg px-2 py-1">
                          <button 
                            onClick={() => applicationService.viewResume(app.cvUrl!)} 
                            className="text-primary-400 hover:text-primary-300 flex items-center gap-1 transition-colors p-1"
                            title="Preview CV"
                          >
                             <Eye className="w-3.5 h-3.5" /> <span className="text-xs font-semibold">CV</span>
                          </button>
                          <span className="text-primary-500/30">|</span>
                          <button 
                            onClick={() => applicationService.downloadResume(app.cvUrl!, 'my_cv.pdf')} 
                            className="text-primary-400 hover:text-primary-300 flex items-center gap-1 transition-colors p-1"
                            title="Download CV"
                          >
                             <Download className="w-3.5 h-3.5" />
                          </button>
                        </div>
                     )}
                     {app.coverLetterUrl && (
                        <div className="flex items-center gap-1 bg-surface-700/30 rounded-lg px-2 py-1">
                          <button 
                            onClick={() => applicationService.viewResume(app.coverLetterUrl!)} 
                            className="text-surface-400 hover:text-surface-300 flex items-center gap-1 transition-colors p-1"
                            title="Preview Cover Letter"
                          >
                             <Eye className="w-3.5 h-3.5" /> <span className="text-xs font-semibold">Letter</span>
                          </button>
                          <span className="text-surface-500/30">|</span>
                          <button 
                            onClick={() => applicationService.downloadResume(app.coverLetterUrl!, 'my_cover_letter.pdf')} 
                            className="text-surface-400 hover:text-surface-300 flex items-center gap-1 transition-colors p-1"
                            title="Download Cover Letter"
                          >
                             <Download className="w-3.5 h-3.5" />
                          </button>
                        </div>
                     )}
                  </div>
                  
                  <div className="flex items-center gap-2">
                    {app.status === 'PENDING' && (
                      <button
                        onClick={() => {
                          setAppToWithdraw(app.id);
                          setIsWithdrawModalOpen(true);
                        }}
                        className="p-2 rounded-lg text-surface-400 hover:text-red-400 hover:bg-red-500/10 transition-all"
                        title="Withdraw Application"
                      >
                         <Trash2 className="w-5 h-5" />
                      </button>
                    )}
                    <Link to={`/jobs/${app.jobOfferId}`} className="p-2 -mr-2 rounded-lg text-surface-400 hover:text-primary-400 hover:bg-primary-500/10 transition-all">
                       <ArrowRight className="w-5 h-5" />
                    </Link>
                  </div>
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

      {/* Withdraw Modal */}
      <Modal
        isOpen={isWithdrawModalOpen}
        onClose={() => setIsWithdrawModalOpen(false)}
        title="Withdraw Application"
      >
        <div className="space-y-6">
          <div className="flex items-center gap-4 text-surface-300">
            <div className="w-12 h-12 rounded-full bg-red-500/10 flex items-center justify-center shrink-0">
              <Trash2 className="w-6 h-6 text-red-400" />
            </div>
            <p>
              Are you sure you want to withdraw this application? This action cannot be undone.
            </p>
          </div>
          
          <div className="flex items-center justify-end gap-3 pt-4 border-t border-surface-700/60">
             <Button variant="ghost" onClick={() => setIsWithdrawModalOpen(false)} disabled={isWithdrawing}>
               Cancel
             </Button>
             <Button 
               onClick={handleWithdrawConfirm} 
               isLoading={isWithdrawing}
               className="bg-red-500 hover:bg-red-600 text-white shadow-lg shadow-red-500/20"
             >
               Withdraw
             </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
