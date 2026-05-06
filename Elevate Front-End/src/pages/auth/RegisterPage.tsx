import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Button from '../../components/ui/Button';
import { Mail, Lock, User as UserIcon, Phone, ArrowRight } from 'lucide-react';

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    phoneNumber: '',
    role: '',
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const update = (field: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
    setForm((prev) => ({ ...prev, [field]: e.target.value }));

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');

    if (!form.firstName || !form.lastName || !form.email || !form.password || !form.role) {
      setError('Please fill in all required fields.');
      return;
    }

    if (form.password.length < 6) {
      setError('Password must be at least 6 characters.');
      return;
    }

    setIsLoading(true);
    try {
      await register({
        firstName: form.firstName,
        lastName: form.lastName,
        email: form.email,
        password: form.password,
        phoneNumber: form.phoneNumber || undefined,
        role: form.role as 'CANDIDATE' | 'RECRUITER',
      });
      navigate('/login', {
        state: { message: 'Registration successful! Please sign in.' },
      });
    } catch (err: unknown) {
      const axiosErr = err as { response?: { status?: number; data?: { message?: string; details?: string } } };
      const data = axiosErr.response?.data;
      // Prefer the clean 'message' field; never show raw SQL/exceptions to users
      let msg = data?.message || 'Registration failed. Please try again.';
      if (data?.details && !data.details.includes('SQL') && !data.details.includes('could not execute')) {
        msg = data.details;
      }
      setError(msg);

    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <div className="mb-8">
        <h2 className="text-2xl font-bold text-surface-100">Create your account</h2>
        <p className="text-surface-400 mt-1">Join Elevate and start your journey</p>
      </div>

      {error && (
        <div className="mb-6 rounded-xl bg-red-500/10 border border-red-500/20 px-4 py-3 text-sm text-red-400 animate-slide-down">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <Input
            label="First Name"
            placeholder="John"
            value={form.firstName}
            onChange={update('firstName')}
            icon={<UserIcon className="w-4 h-4" />}
            required
          />
          <Input
            label="Last Name"
            placeholder="Doe"
            value={form.lastName}
            onChange={update('lastName')}
            required
          />
        </div>

        <Input
          label="Email address"
          type="email"
          placeholder="you@example.com"
          value={form.email}
          onChange={update('email')}
          icon={<Mail className="w-4 h-4" />}
          autoComplete="email"
          required
        />

        <Input
          label="Password"
          type="password"
          placeholder="Min. 6 characters"
          value={form.password}
          onChange={update('password')}
          icon={<Lock className="w-4 h-4" />}
          autoComplete="new-password"
          required
        />

        <Input
          label="Phone Number"
          type="tel"
          placeholder="+212 6XX-XXXXXX (optional)"
          value={form.phoneNumber}
          onChange={update('phoneNumber')}
          icon={<Phone className="w-4 h-4" />}
        />

        <Select
          label="I want to join as"
          value={form.role}
          onChange={update('role')}
          placeholder="Select your role"
          options={[
            { value: 'CANDIDATE', label: '🎯 Candidate — Looking for opportunities' },
            { value: 'RECRUITER', label: '🏢 Recruiter — Hiring talent' },
          ]}
          required
        />

        <Button
          type="submit"
          size="lg"
          isLoading={isLoading}
          className="w-full mt-2"
          icon={<ArrowRight className="w-4 h-4" />}
        >
          Create account
        </Button>
      </form>

      <p className="mt-8 text-center text-sm text-surface-500">
        Already have an account?{' '}
        <Link
          to="/login"
          className="font-semibold text-primary-400 hover:text-primary-300 transition-colors"
        >
          Sign in
        </Link>
      </p>
    </>
  );
}
