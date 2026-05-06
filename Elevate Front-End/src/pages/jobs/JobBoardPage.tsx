import { useState, useEffect, useCallback } from 'react';
import { jobService } from '../../services/jobService';
import type { JobOfferResponse, Page } from '../../types';
import { JOB_CATEGORIES } from '../../types';
import JobCard from '../../components/ui/JobCard';
import Pagination from '../../components/ui/Pagination';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import { Search, SlidersHorizontal, Briefcase, X } from 'lucide-react';

export default function JobBoardPage() {
  const [jobs, setJobs] = useState<Page<JobOfferResponse> | null>(null);
  const [keyword, setKeyword] = useState('');
  const [category, setCategory] = useState('');
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [searchInput, setSearchInput] = useState('');

  const fetchJobs = useCallback(async () => {
    setIsLoading(true);
    try {
      const data = await jobService.searchJobs(
        keyword || undefined,
        category || undefined,
        page,
        9
      );
      setJobs(data);
    } catch (err) {
      console.error('Failed to fetch jobs:', err);
    } finally {
      setIsLoading(false);
    }
  }, [keyword, category, page]);

  useEffect(() => {
    fetchJobs();
  }, [fetchJobs]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setKeyword(searchInput);
    setPage(0);
  };

  const clearFilters = () => {
    setKeyword('');
    setCategory('');
    setSearchInput('');
    setPage(0);
  };

  const hasFilters = keyword || category;

  return (
    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-8">
      {/* Page header */}
      <div className="mb-8 animate-fade-in">
        <h1 className="text-3xl font-bold text-surface-100">
          Discover <span className="gradient-text">opportunities</span>
        </h1>
        <p className="text-surface-400 mt-2">
          Browse {jobs?.totalElements ?? '...'} job openings matching your skills
        </p>
      </div>

      {/* Search & Filters */}
      <div className="glass rounded-2xl p-4 sm:p-6 mb-8 animate-slide-up">
        <form onSubmit={handleSearch} className="flex flex-col sm:flex-row gap-3">
          {/* Search input */}
          <div className="relative flex-1">
            <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-surface-500" />
            <input
              id="job-search-input"
              type="text"
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              placeholder="Search by title, keyword..."
              className="w-full pl-10 pr-4 py-3 rounded-xl bg-surface-900/60 border border-surface-700/50 text-sm text-surface-100 placeholder:text-surface-500 focus:outline-none focus:ring-2 focus:ring-primary-500/40 focus:border-primary-500 transition-all"
            />
          </div>

          {/* Category dropdown */}
          <div className="relative sm:w-52">
            <SlidersHorizontal className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-surface-500 pointer-events-none" />
            <select
              id="job-category-filter"
              value={category}
              onChange={(e) => {
                setCategory(e.target.value);
                setPage(0);
              }}
              className="w-full pl-10 pr-4 py-3 rounded-xl bg-surface-900/60 border border-surface-700/50 text-sm text-surface-100 focus:outline-none focus:ring-2 focus:ring-primary-500/40 focus:border-primary-500 transition-all appearance-none cursor-pointer"
            >
              <option value="" className="bg-surface-900">All Categories</option>
              {JOB_CATEGORIES.map((cat) => (
                <option key={cat} value={cat} className="bg-surface-900">
                  {cat.charAt(0) + cat.slice(1).toLowerCase()}
                </option>
              ))}
            </select>
          </div>

          {/* Search button */}
          <button
            type="submit"
            className="px-6 py-3 rounded-xl bg-gradient-to-r from-primary-600 to-primary-500 text-white text-sm font-semibold shadow-lg shadow-primary-500/20 hover:shadow-primary-500/40 hover:from-primary-500 hover:to-primary-400 transition-all active:scale-[0.98] cursor-pointer"
          >
            Search
          </button>
        </form>

        {hasFilters && (
          <div className="flex items-center gap-2 mt-4 pt-4 border-t border-surface-700/30">
            <span className="text-xs text-surface-500">Active filters:</span>
            {keyword && (
              <span className="inline-flex items-center gap-1 text-xs bg-primary-500/10 text-primary-400 px-2.5 py-1 rounded-full border border-primary-500/20">
                "{keyword}"
                <button onClick={() => { setKeyword(''); setSearchInput(''); }} className="hover:text-primary-300">
                  <X className="w-3 h-3" />
                </button>
              </span>
            )}
            {category && (
              <span className="inline-flex items-center gap-1 text-xs bg-accent-500/10 text-accent-400 px-2.5 py-1 rounded-full border border-accent-500/20">
                {category}
                <button onClick={() => setCategory('')} className="hover:text-accent-300">
                  <X className="w-3 h-3" />
                </button>
              </span>
            )}
            <button
              onClick={clearFilters}
              className="text-xs text-surface-500 hover:text-surface-300 transition-colors ml-2"
            >
              Clear all
            </button>
          </div>
        )}
      </div>

      {/* Job Grid */}
      {isLoading ? (
        <LoadingSpinner />
      ) : jobs && jobs.content.length > 0 ? (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
            {jobs.content.map((job, index) => (
              <JobCard key={job.id} job={job} index={index} />
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
          <h3 className="text-lg font-semibold text-surface-300 mb-2">No jobs found</h3>
          <p className="text-sm text-surface-500 text-center max-w-sm">
            Try adjusting your search criteria or browse all available opportunities.
          </p>
          {hasFilters && (
            <button
              onClick={clearFilters}
              className="mt-4 text-sm text-primary-400 hover:text-primary-300 font-medium transition-colors"
            >
              Clear all filters
            </button>
          )}
        </div>
      )}
    </div>
  );
}
