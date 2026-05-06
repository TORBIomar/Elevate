import { useState, useEffect, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import { applicationService } from '../../services/applicationService';
import { interviewService } from '../../services/interviewService';
import { jobService } from '../../services/jobService';
import type { ApplicationResponse, ApplicationStatus, JobOfferResponse, Page } from '../../types';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import Pagination from '../../components/ui/Pagination';
import StatusBadge from '../../components/ui/StatusBadge';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import Input from '../../components/ui/Input';
import { FileText, ArrowLeft, Calendar, Video, Download, CheckCircle2 } from 'lucide-react';

export default function ApplicationManagerPage() {
  const { id } = useParams<{ id: string }>();
  const [job, setJob] = useState<JobOfferResponse | null>(null);
  const [applications, setApplications] = useState<Page<ApplicationResponse> | null>(null);
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  // Interview Modal State
  const [isInterviewModalOpen, setIsInterviewModalOpen] = useState(false);
  const [selectedApp, setSelectedApp] = useState<ApplicationResponse | null>(null);
  const [interviewDate, setInterviewDate] = useState('');
  const [interviewTime, setInterviewTime] = useState('');
  const [interviewLink, setInterviewLink] = useState('');
  const [isScheduling, setIsScheduling] = useState(false);

  const fetchApplicationsAndJob = useCallback(async () => {
    if (!id) return;
    setIsLoading(true);
    try {
      const [jobData, appsData] = await Promise.all([
        jobService.getJobById(Number(id)),
        applicationService.getApplicationsForJob(Number(id), page, 10)
      ]);
      setJob(jobData);
      setApplications(appsData);
    } catch (err) {
      console.error('Failed to fetch data:', err);
    } finally {
      setIsLoading(false);
    }
  }, [id, page]);

  useEffect(() => {
    fetchApplicationsAndJob();
  }, [fetchApplicationsAndJob]);

  const handleStatusChange = async (appId: number, status: ApplicationStatus) => {
    try {
      await applicationService.updateApplicationStatus(appId, status);
      fetchApplicationsAndJob(); // Refresh
    } catch (err) {
      console.error('Failed to update status:', err);
      alert('Failed to update application status.');
    }
  };

  const handleScheduleInterview = async () => {
    if (!selectedApp || !interviewDate || !interviewTime || !interviewLink) return;
    
    setIsScheduling(true);
    try {
      // Combine date and time
      const datetime = new Date(`${interviewDate}T${interviewTime}`).toISOString();
      await interviewService.scheduleInterview({
        applicationId: selectedApp.id,
        scheduledDate: datetime,
        interviewLinkOrLocation: interviewLink
      });
      setIsInterviewModalOpen(false);
      fetchApplicationsAndJob(); // Refresh
    } catch (err) {
      console.error('Failed to schedule interview:', err);
      alert('Failed to schedule interview.');
    } finally {
      setIsScheduling(false);
    }
  };

  const openInterviewModal = (app: ApplicationResponse) => {
    setSelectedApp(app);
    // Reset form
    setInterviewDate('');
    setInterviewTime('');
    setInterviewLink('');
    setIsInterviewModalOpen(true);
  };

  if (isLoading) return <LoadingSpinner />;

  return (
    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <Link
        to="/recruiter/offers"
        className="inline-flex items-center gap-1.5 text-sm text-surface-400 hover:text-surface-200 transition-colors mb-6"
      >
        <ArrowLeft className="w-4 h-4" />
        Back to Offers
      </Link>

      <div className="mb-8">
        <h1 className="text-3xl font-bold text-surface-100 flex items-center gap-3">
          <FileText className="w-8 h-8 text-primary-500" />
          Application Manager
        </h1>
        <p className="text-surface-400 mt-1">Viewing applicants for: <strong className="text-surface-200">{job?.title}</strong></p>
      </div>

      <div className="bg-surface-900 border border-surface-800 rounded-2xl overflow-hidden shadow-xl">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm text-surface-300">
            <thead className="text-xs uppercase text-surface-400 bg-surface-800/50">
              <tr>
                <th className="px-6 py-4 font-semibold">Candidate Name</th>
                <th className="px-6 py-4 font-semibold">Applied At</th>
                <th className="px-6 py-4 font-semibold">Status</th>
                <th className="px-6 py-4 font-semibold">Documents</th>
                <th className="px-6 py-4 font-semibold text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-surface-800">
              {applications?.content.map((app) => (
                <tr key={app.id} className={`transition-colors ${app.status === 'ACCEPTED' ? 'bg-emerald-500/5 hover:bg-emerald-500/10 border-l-2 border-emerald-500' : 'hover:bg-surface-800/30'}`}>
                  <td className="px-6 py-4 font-medium text-surface-100 whitespace-nowrap flex items-center gap-2">
                    {app.candidateName}
                    {app.status === 'ACCEPTED' && <CheckCircle2 className="w-4 h-4 text-emerald-400" />}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {new Date(app.appliedAt).toLocaleDateString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap w-48">
                    <StatusBadge status={app.status} />
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex gap-3">
                      {app.cvUrl && (
                        <a href={app.cvUrl} target="_blank" rel="noreferrer" className="text-primary-400 hover:text-primary-300 flex items-center gap-1 transition-colors">
                           <Download className="w-4 h-4" /> CV
                        </a>
                      )}
                      {app.coverLetterUrl && (
                        <a href={app.coverLetterUrl} target="_blank" rel="noreferrer" className="text-surface-400 hover:text-surface-300 flex items-center gap-1 transition-colors">
                           <Download className="w-4 h-4" /> Cover Letter
                        </a>
                      )}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right">
                    <div className="flex items-center justify-end gap-2">
                       <select
                         className="bg-surface-800 border border-surface-700 text-surface-200 text-xs rounded-lg focus:ring-primary-500 focus:border-primary-500 block p-2"
                         value={app.status}
                         onChange={(e) => handleStatusChange(app.id, e.target.value as ApplicationStatus)}
                       >
                         <option value="PENDING">Pending</option>
                         <option value="REVIEWING">Reviewing</option>
                         <option value="ACCEPTED">Accepted</option>
                         <option value="REJECTED">Rejected</option>
                         <option value="INTERVIEW_SCHEDULED" disabled>Interview Scheduled</option>
                       </select>
                       
                       {app.status !== 'INTERVIEW_SCHEDULED' && app.status !== 'REJECTED' && (
                         <Button size="sm" onClick={() => openInterviewModal(app)}>
                           Schedule Interview
                         </Button>
                       )}
                    </div>
                  </td>
                </tr>
              ))}
              {applications?.content.length === 0 && (
                <tr>
                  <td colSpan={5} className="px-6 py-12 text-center text-surface-500">
                    No applications received for this job offer yet.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
      
      {applications && applications.totalPages > 1 && (
        <Pagination
           currentPage={applications.number}
           totalPages={applications.totalPages}
           onPageChange={setPage}
        />
      )}

      {/* Schedule Interview Modal */}
      <Modal
        isOpen={isInterviewModalOpen}
        onClose={() => setIsInterviewModalOpen(false)}
        title="Schedule Interview"
      >
         <div className="space-y-4">
            <p className="text-sm text-surface-300 mb-4">
              Schedule an interview with <strong className="text-surface-100">{selectedApp?.candidateName}</strong>.
            </p>
            
            <div className="grid grid-cols-2 gap-4">
               <Input
                  label="Date"
                  type="date"
                  value={interviewDate}
                  onChange={(e) => setInterviewDate(e.target.value)}
                  icon={<Calendar className="w-4 h-4" />}
                  required
               />
               <Input
                  label="Time"
                  type="time"
                  value={interviewTime}
                  onChange={(e) => setInterviewTime(e.target.value)}
                  icon={<Calendar className="w-4 h-4" />}
                  required
               />
            </div>
            
            <Input
               label="Meeting Link / Location"
               type="text"
               placeholder="Google Meet link or Office Address"
               value={interviewLink}
               onChange={(e) => setInterviewLink(e.target.value)}
               icon={<Video className="w-4 h-4" />}
               required
            />
            
            <div className="flex items-center justify-end gap-3 pt-4 border-t border-surface-700/60 mt-6">
               <Button variant="ghost" onClick={() => setIsInterviewModalOpen(false)} disabled={isScheduling}>
                 Cancel
               </Button>
               <Button onClick={handleScheduleInterview} isLoading={isScheduling} disabled={!interviewDate || !interviewTime || !interviewLink}>
                 Confirm & Schedule
               </Button>
            </div>
         </div>
      </Modal>
    </div>
  );
}
