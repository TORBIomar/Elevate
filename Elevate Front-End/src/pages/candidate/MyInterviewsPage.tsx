import { useState, useEffect } from 'react';
import { interviewService } from '../../services/interviewService';
import type { InterviewResponse } from '../../types';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import StatusBadge from '../../components/ui/StatusBadge';
import { CalendarIcon, Clock, MapPin, Video } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function MyInterviewsPage() {
  const [interviews, setInterviews] = useState<InterviewResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function fetchInterviews() {
      try {
         const data = await interviewService.getMyInterviews();
         setInterviews(data);
      } catch (err) {
         console.error('Failed to fetch interviews', err);
      } finally {
         setIsLoading(false);
      }
    }
    fetchInterviews();
  }, []);

  if (isLoading) return <LoadingSpinner />;

  return (
    <div className="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
       <div className="mb-8">
        <h1 className="text-3xl font-bold text-surface-100 flex items-center gap-3">
          <CalendarIcon className="w-8 h-8 text-primary-500" />
          My Interviews
        </h1>
        <p className="text-surface-400 mt-1">Upcoming and past interview schedules.</p>
      </div>

      {interviews.length > 0 ? (
         <div className="space-y-4">
            {interviews.map((interview, idx) => {
               const scheduleDate = new Date(interview.scheduledDate);
               const isVideo = interview.interviewLinkOrLocation.includes('http');
               
               return (
                  <div key={interview.id} className={`glass p-6 rounded-2xl flex flex-col md:flex-row md:items-center justify-between gap-6 card-hover animate-slide-up stagger-${Math.min(idx + 1, 6)}`}>
                     <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                           <StatusBadge status={interview.status} />
                        </div>
                        <h3 className="text-xl font-bold text-surface-100">{interview.jobTitle}</h3>
                        <p className="text-sm text-surface-400 mt-1 font-medium">Recruiter Interview</p>
                     </div>
                     
                     <div className="flex flex-col sm:flex-row gap-6 md:gap-12 pl-0 md:pl-6 md:border-l border-surface-700/50">
                        <div>
                           <p className="text-xs font-semibold text-surface-500 uppercase tracking-wider mb-1.5 flex items-center gap-1.5">
                              <Clock className="w-3.5 h-3.5" /> Date & Time
                           </p>
                           <p className="text-sm font-medium text-surface-200">{scheduleDate.toLocaleDateString()}</p>
                           <p className="text-sm text-surface-400">{scheduleDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</p>
                        </div>
                        
                        <div>
                           <p className="text-xs font-semibold text-surface-500 uppercase tracking-wider mb-1.5 flex items-center gap-1.5">
                              {isVideo ? <Video className="w-3.5 h-3.5" /> : <MapPin className="w-3.5 h-3.5" />} Location
                           </p>
                           {isVideo ? (
                              <a href={interview.interviewLinkOrLocation} target="_blank" rel="noreferrer" className="text-sm font-medium text-primary-400 hover:text-primary-300 transition-colors underline underline-offset-4">
                                 Join Video Call
                              </a>
                           ) : (
                              <p className="text-sm font-medium text-surface-200">{interview.interviewLinkOrLocation}</p>
                           )}
                        </div>
                     </div>
                  </div>
               );
            })}
         </div>
      ) : (
         <div className="glass p-12 rounded-3xl text-center mt-12">
            <CalendarIcon className="w-16 h-16 text-surface-600 mx-auto mb-4" />
            <h3 className="text-lg font-bold text-surface-200 mb-2">No interviews yet</h3>
            <p className="text-surface-400 mb-4 max-w-sm mx-auto">You don't have any interviews scheduled currently.</p>
            <Link to="/my-applications" className="text-primary-400 hover:text-primary-300 hover:underline transition-all">View your applications</Link>
         </div>
      )}
    </div>
  )
}
