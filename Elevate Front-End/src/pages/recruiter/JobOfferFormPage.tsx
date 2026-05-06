import { useState, useEffect, type FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { jobService } from '../../services/jobService';
import { JOB_TYPES, JOB_CATEGORIES } from '../../types';
import type { JobOfferRequest } from '../../types';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Button from '../../components/ui/Button';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import {
  ArrowLeft,
  Save,
  FileText,
  MapPin,
  Banknote,
} from 'lucide-react';

export default function JobOfferFormPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const [form, setForm] = useState<JobOfferRequest>({
    title: '',
    description: '',
    location: '',
    jobType: '',
    category: '',
    salary: 0,
  });
  const [errors, setErrors] = useState<Partial<Record<keyof JobOfferRequest, string>>>({});
  const [isLoading, setIsLoading] = useState(false);
  const [isFetching, setIsFetching] = useState(isEdit);
  const [submitError, setSubmitError] = useState('');

  // Fetch existing job for edit mode
  useEffect(() => {
    if (!isEdit || !id) return;
    const fetchJob = async () => {
      setIsFetching(true);
      try {
        const job = await jobService.getJobById(Number(id));
        setForm({
          title: job.title,
          description: job.description,
          location: job.location,
          jobType: job.jobType,
          category: job.category,
          salary: job.salary,
        });
      } catch {
        setSubmitError('Failed to load job offer.');
      } finally {
        setIsFetching(false);
      }
    };
    fetchJob();
  }, [isEdit, id]);

  const update = (field: keyof JobOfferRequest) => (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const value = field === 'salary' ? Number(e.target.value) : e.target.value;
    setForm((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: undefined }));
    }
  };

  const validate = (): boolean => {
    const newErrors: Partial<Record<keyof JobOfferRequest, string>> = {};
    if (!form.title.trim()) newErrors.title = 'Title is required';
    if (!form.description.trim()) newErrors.description = 'Description is required';
    if (!form.location.trim()) newErrors.location = 'Location is required';
    if (!form.jobType) newErrors.jobType = 'Job type is required';
    if (!form.category) newErrors.category = 'Category is required';
    if (!form.salary || form.salary <= 0) newErrors.salary = 'Salary must be greater than 0';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setSubmitError('');
    if (!validate()) return;

    setIsLoading(true);
    try {
      if (isEdit && id) {
        await jobService.updateJob(Number(id), form);
      } else {
        await jobService.createJob(form);
      }
      navigate('/recruiter/offers');
    } catch (err: unknown) {
      const axiosErr = err as { response?: { data?: { message?: string } } };
      setSubmitError(
        axiosErr.response?.data?.message ||
        `Failed to ${isEdit ? 'update' : 'create'} job offer.`
      );
    } finally {
      setIsLoading(false);
    }
  };

  if (isFetching) return <LoadingSpinner />;

  return (
    <div className="mx-auto max-w-3xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      {/* Back link */}
      <button
        onClick={() => navigate('/recruiter/offers')}
        className="inline-flex items-center gap-1.5 text-sm text-surface-400 hover:text-surface-200 transition-colors mb-6"
      >
        <ArrowLeft className="w-4 h-4" />
        Back to My Offers
      </button>

      <div className="glass rounded-2xl p-6 sm:p-8">
        <h1 className="text-2xl font-bold text-surface-100 mb-1">
          {isEdit ? 'Edit Job Offer' : 'Post a New Job'}
        </h1>
        <p className="text-surface-400 text-sm mb-8">
          {isEdit ? 'Update the details of your job offer.' : 'Fill in the details to attract the best candidates.'}
        </p>

        {submitError && (
          <div className="mb-6 rounded-xl bg-red-500/10 border border-red-500/20 px-4 py-3 text-sm text-red-400 animate-slide-down">
            {submitError}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          <Input
            label="Job Title"
            placeholder="e.g. Senior Frontend Developer"
            value={form.title}
            onChange={update('title')}
            error={errors.title}
            icon={<FileText className="w-4 h-4" />}
            required
          />

          {/* Description textarea */}
          <div className="space-y-1.5">
            <label htmlFor="job-description" className="block text-sm font-medium text-surface-300">
              Description
            </label>
            <textarea
              id="job-description"
              rows={6}
              value={form.description}
              onChange={update('description')}
              placeholder="Describe the role, responsibilities, and requirements..."
              className={`
                w-full rounded-xl border bg-surface-900/60 px-4 py-3 text-sm text-surface-100
                placeholder:text-surface-500 transition-all duration-200 resize-y
                focus:outline-none focus:ring-2 focus:ring-primary-500/40 focus:border-primary-500
                ${errors.description ? 'border-error-500/50' : 'border-surface-700/50 hover:border-surface-600'}
              `}
            />
            {errors.description && (
              <p className="text-xs text-error-500">{errors.description}</p>
            )}
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <Input
              label="Location"
              placeholder="e.g. Casablanca, Morocco"
              value={form.location}
              onChange={update('location')}
              error={errors.location}
              icon={<MapPin className="w-4 h-4" />}
              required
            />
            <Input
              label="Salary (USD)"
              type="number"
              placeholder="e.g. 75000"
              value={form.salary || ''}
              onChange={update('salary')}
              error={errors.salary}
              icon={<Banknote className="w-4 h-4" />}
              min={0}
              required
            />
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <Select
              label="Job Type"
              value={form.jobType}
              onChange={update('jobType')}
              error={errors.jobType}
              placeholder="Select job type"
              options={JOB_TYPES.map((t) => ({
                value: t,
                label: t.replace(/_/g, ' ').replace(/\b\w/g, (l) => l.toUpperCase()),
              }))}
              required
            />
            <Select
              label="Category"
              value={form.category}
              onChange={update('category')}
              error={errors.category}
              placeholder="Select category"
              options={JOB_CATEGORIES.map((c) => ({
                value: c,
                label: c.charAt(0) + c.slice(1).toLowerCase(),
              }))}
              required
            />
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-surface-700/30">
            <Button
              type="button"
              variant="secondary"
              onClick={() => navigate('/recruiter/offers')}
            >
              Cancel
            </Button>
            <Button
              type="submit"
              isLoading={isLoading}
              icon={<Save className="w-4 h-4" />}
            >
              {isEdit ? 'Save Changes' : 'Publish Job'}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
