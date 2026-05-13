import { useState, useEffect } from 'react';
import { savedJobsService } from '../../services/savedJobsService';
import type { JobOfferResponse } from '../../types';
import JobCard from '../../components/ui/JobCard';
import { Bookmark, Search } from 'lucide-react';
import { Link } from 'react-router-dom';
import Button from '../../components/ui/Button';

export default function MySavedJobsPage() {
  const [savedJobs, setSavedJobs] = useState<JobOfferResponse[]>([]);

  useEffect(() => {
    setSavedJobs(savedJobsService.getSavedJobs());
  }, []);

  return (
    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-surface-100 flex items-center gap-3">
          <Bookmark className="w-8 h-8 text-rose-500 fill-current" />
          My Saved Jobs
        </h1>
        <p className="text-surface-400 mt-1">Keep track of the opportunities you are interested in.</p>
      </div>

      {savedJobs.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {savedJobs.map((job, index) => (
            <JobCard key={job.id} job={job} index={index} />
          ))}
        </div>
      ) : (
        <div className="flex flex-col items-center justify-center py-20 animate-fade-in">
          <div className="w-20 h-20 rounded-2xl bg-surface-800/60 flex items-center justify-center mb-6">
            <Bookmark className="w-10 h-10 text-surface-600" />
          </div>
          <h3 className="text-lg font-semibold text-surface-300 mb-2">No saved jobs</h3>
          <p className="text-sm text-surface-500 text-center max-w-sm mb-6">
            You haven't saved any job opportunities yet. Browse the job board and bookmark roles you like.
          </p>
          <Link to="/jobs">
            <Button icon={<Search className="w-4 h-4" />}>
              Explore Jobs
            </Button>
          </Link>
        </div>
      )}
    </div>
  );
}
